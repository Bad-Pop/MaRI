package io.github.badpop.mari.application.infra.postgres.ad.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.badpop.mari.application.domain.ad.control.AdSharingParameters;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAdCreated;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdDeleterSpi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdFinderSpi;
import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.ExpiredSharedAdFail;
import io.github.badpop.mari.application.domain.control.MariFail.InvalidRequestFail;
import io.github.badpop.mari.application.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.application.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.application.infra.postgres.ad.AdAdapter;
import io.github.badpop.mari.application.infra.postgres.ad.AdEntity;
import io.github.badpop.mari.application.infra.postgres.user.CurrentUserEntityProvider;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;
import io.quarkus.logging.Log;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hibernate.exception.ConstraintViolationException;

import java.util.UUID;

import static io.vavr.API.*;

@Singleton
@RequiredArgsConstructor
public class SharedAdAdapter implements SharedAdCreatorSpi, SharedAdFinderSpi, SharedAdDeleterSpi {

  private final CurrentUserEntityProvider userEntityProvider;
  private final AdAdapter adAdapter;
  private final SharedAdRepository repository;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public Either<MariFail, SharedAdCreated> createSharedAdById(UUID adId, AdSharingParameters parameters, UUID sharedAdId) {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(currentUserEntity -> retrieveAdEntityByIdAndUser(adId, currentUserEntity)
                    .map(foundAdEntity -> Tuple(foundAdEntity, currentUserEntity)))
            .flatMap(foundAdAndCurrentUser ->
                    persistSharedAd(
                            adId,
                            foundAdAndCurrentUser._1,
                            foundAdAndCurrentUser._2,
                            parameters))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  public Either<MariFail, SharedAd> findById(UUID id) {
    return retrieveSharedAdEntityById(id)
            .map(this::toDomain)
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  public Either<MariFail, Seq<SharedAd>> findAll() {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(this::retrieveAllSharedAdsForUser)
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  @Transactional
  public Either<MariFail, Void> deleteById(UUID id) {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(currentUserEntity -> retrieveSharedAdEntityByIdAndUser(id, currentUserEntity))
            .flatMap(this::deleteSharedAd)
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  private Either<MariFail, SharedAdCreated> persistSharedAd(UUID adId, AdEntity adEntity, UserEntity userEntity, AdSharingParameters parameters) {
    val entity = SharedAdEntity.from(adId, adEntity, userEntity, parameters);
    return repository.persistIfAbsent(entity)
            .mapTry(persistedSharedAdEntity -> {
              /*
              On est obligé de flusher ici car si on essaie de créer un partage sur un partage qui existe déjà,
              c'est la transaction qui flush les opérations effectuées. Le souci, c'est qu'en faisant ça, on ne catch jamais l'exception.
              L'autre solution serait de vérifier en base que l'annonce n'est pas déjà partagée avant de persister...
               */
              repository.flush();
              return persistedSharedAdEntity;
            })
            .toEither()
            .mapLeft(this::manageSharedAdPersistenceFailure)
            .map(persistedSharedAd -> new SharedAdCreated(persistedSharedAd.getId()));
  }

  private Either<MariFail, AdEntity> retrieveAdEntityByIdAndUser(UUID adId, UserEntity currentUserEntity) {
    return adAdapter.retrieveAdEntityByIdAndUser(adId, currentUserEntity.getId());
  }

  private MariFail manageSharedAdPersistenceFailure(Throwable t) {
    MariFail fail;

    //Ça veut dire que cette annonce est déjà partagée
    if (t instanceof ConstraintViolationException e && e.getConstraintName().equals("shared_ad_id_uc")) {
      fail = new InvalidRequestFail("Cette annonce est déjà partagée.");
    } else {
      fail = new TechnicalFail("Unable to create a shared ad", t);
    }
    return fail;
  }

  private Either<MariFail, Seq<SharedAd>> retrieveAllSharedAdsForUser(UserEntity currentUserEntity) {
    return repository.findAllByUser(currentUserEntity)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Unable to retrieve all ads for user with id=" + currentUserEntity.getId(), t))
            .flatMap(sharedAdEntities -> {
              if(sharedAdEntities.isEmpty()) {
                return Left(new ResourceNotFoundFail("Aucune annonce partagée."));
              } else {
                return Right(sharedAdEntities.map(this::toDomain));
              }
            });
  }

  private Either<MariFail, SharedAdEntity> retrieveSharedAdEntityById(UUID id) {
    return repository.findSharedAdById(id)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Unable to retrieve shared ad with id=" + id, t))
            .flatMap(maybeSharedAdEntity -> maybeSharedAdEntity.toEither(ExpiredSharedAdFail::new));
  }

  private Either<MariFail, SharedAdEntity> retrieveSharedAdEntityByIdAndUser(UUID id, UserEntity currentUserEntity) {
    return repository.findByIdAndUser(id, currentUserEntity.getId())
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Unable to retrieve shared ad to delete with id=" + id, t))
            .flatMap(maybeSharedAdEntity -> maybeSharedAdEntity
                    .toEither(() -> new ResourceNotFoundFail("L'annonce partagée avec l'id %s n'éxiste pas".formatted(id))));
  }

  private Either<MariFail, Void> deleteSharedAd(SharedAdEntity sharedAdToDelete) {
    try {
      repository.delete(sharedAdToDelete);
      return Right(null);
    } catch (Exception e) {
      return Left(new TechnicalFail("Unable to delete shared ad with id=" + sharedAdToDelete.getId(), e));
    }
  }

  private SharedAd toDomain(SharedAdEntity entity) {
    var adEntity = entity.getAd();
    var adAddress = deserializeAddress(adEntity.getAddress());
    var domainAd = adEntity.toDomain().withAddress(adAddress);
    return new SharedAd(entity.getId(), domainAd, entity.isExpires(), Option(entity.getExpireAt()));
  }

  private Option<Address> deserializeAddress(String jsonAddress) {
    if (jsonAddress == null) {
      return None();
    } else {
      try {
        var address = objectMapper.readValue(jsonAddress, Address.class);
        return Option(address);
      } catch (JsonProcessingException e) {
        return None();
      }
    }
  }
}
