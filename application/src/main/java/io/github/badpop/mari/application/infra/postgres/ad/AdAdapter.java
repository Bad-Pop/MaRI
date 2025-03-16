package io.github.badpop.mari.application.infra.postgres.ad;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.port.AdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.AdDeleterSpi;
import io.github.badpop.mari.application.domain.ad.port.AdFinderSpi;
import io.github.badpop.mari.application.domain.ad.port.AdUpdaterSpi;
import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.application.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.github.badpop.mari.application.infra.postgres.EntityUpdateOperationsApplier;
import io.github.badpop.mari.application.infra.postgres.user.CurrentUserEntityProvider;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;
import io.quarkus.logging.Log;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.UUID;

import static io.vavr.API.*;

@Singleton
@RequiredArgsConstructor
public class AdAdapter implements AdCreatorSpi, AdFinderSpi, AdUpdaterSpi, AdDeleterSpi {

  private final CurrentUserEntityProvider userEntityProvider;
  private final AdRepository repository;
  private final EntityUpdateOperationsApplier updateOperationApplier;
  private final ObjectMapper objectMapper;

  @Override
  @Transactional
  public Either<MariFail, Ad> create(Ad ad) {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(currentUserEntity -> persistAdForUser(ad, currentUserEntity))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  public Either<MariFail, Ad> findById(UUID id) {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(currentUserEntity -> retrieveAdByIdAndUser(id, currentUserEntity))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  public Either<MariFail, Seq<Ad>> findAll() {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(this::retrieveAllAdsForUser)
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  @Transactional
  public Either<MariFail, Ad> updateAdById(UUID id, Seq<UpdateOperation> operations) {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(currentUserEntity -> updateAdByIdForUser(id, operations, currentUserEntity))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  @Transactional
  public Either<MariFail, Void> deleteAdById(UUID id) {
    return userEntityProvider.withCurrentUserEntity()
            .flatMap(currentUserEntity -> retrieveAdEntityByIdAndUser(id, currentUserEntity.getId()))
            .flatMap(this::deleteAd)
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  private Either<MariFail, Ad> persistAdForUser(Ad ad, UserEntity currentUserEntity) {
    return fromDomain(ad, currentUserEntity)
            .flatMapTry(repository::persistIfAbsent)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Unable to create ad with id=%s for user %s".formatted(ad.id(), currentUserEntity.getId()), t))
            .map(this::toDomain);
  }

  private Either<MariFail, Ad> retrieveAdByIdAndUser(UUID adId, UserEntity currentUserEntity) {
    return retrieveAdEntityByIdAndUser(adId, currentUserEntity.getId()).map(this::toDomain);
  }

  private Either<MariFail, Seq<Ad>> retrieveAllAdsForUser(UserEntity currentUserEntity) {
    return repository.findAllByUser(currentUserEntity)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Unable to retrieve all ads for user with id=" + currentUserEntity.getId(), t))
            .flatMap(adEntities -> {
              if (adEntities.isEmpty()) {
                return Left(new ResourceNotFoundFail("No ads for this user"));
              } else {
                return Right(adEntities.map(this::toDomain));
              }
            });
  }

  public Either<MariFail, AdEntity> retrieveAdEntityByIdAndUser(UUID adId, String userId) {
    return repository.findByIdAndUser(adId, userId)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred, unable to retrieve ad by id=" + adId, t))
            .flatMap(maybeAdEntity -> maybeAdEntity
                    .toEither(() -> new ResourceNotFoundFail("Unable to find ad with id=" + adId)));
  }

  private Either<MariFail, Ad> updateAdByIdForUser(UUID id, Seq<UpdateOperation> operations, UserEntity currentUserEntity) {
    return retrieveAdEntityByIdAndUser(id, currentUserEntity.getId())
            .flatMap(adToUpdate -> updateOperationApplier.applyUpdateOperations(adToUpdate, operations, AdEntity.class)
                    .map(updatedAdEntity -> Tuple(adToUpdate.getTechnicalId(), updatedAdEntity)))
            .flatMap(technicalIdAndUpdatedAdEntity ->
                    persistUpdatedAdEntityForUser(technicalIdAndUpdatedAdEntity._1, technicalIdAndUpdatedAdEntity._2, currentUserEntity))
            .peekLeft(fail -> Log.error(fail.asLog()))
            .map(this::toDomain);
  }

  private Either<MariFail, AdEntity> persistUpdatedAdEntityForUser(UUID adTechnicalId, AdEntity updatedAdEntity, UserEntity currentUserEntity) {
    try {
      updatedAdEntity.setTechnicalId(adTechnicalId);
      updatedAdEntity.setUser(currentUserEntity);

      /*
      Le fait de passer par des JsonNodes pour appliquer un JsonPatch à l'entité
      fait que l'on se retrouve avec une entité détachée.
      Afin de la réattacher à l'entity manager, il faut utiliser la méthode merge de ce dernier.
       */
      val em = repository.getEntityManager();
      em.merge(updatedAdEntity);

      return Right(updatedAdEntity);
    } catch (Exception e) {
      return Left(new TechnicalFail("Unable to persist updated ad entity...", e));
    }
  }

  private Either<MariFail, Void> deleteAd(AdEntity adEntity) {
    try {
      repository.delete(adEntity);
      return Right(null);
    } catch (Exception e) {
      return Left(new TechnicalFail("Unable to delete ad with id=" + adEntity.getId(), e));
    }
  }

  private Try<AdEntity> fromDomain(Ad ad, UserEntity user) {
    try {
      String jsonAddress;
      if (ad.address().isDefined()) {
        jsonAddress = objectMapper.writeValueAsString(ad.address().get());
      } else {
        jsonAddress = null;
      }

      return Success(AdEntity.fromDomain(ad, user, jsonAddress));
    } catch (JsonProcessingException e) {
      return Failure(e);
    }
  }

  private Ad toDomain(AdEntity entity) {
    var address = deserializeAddress(entity.getAddress());
    return entity.toDomain().withAddress(address);
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
