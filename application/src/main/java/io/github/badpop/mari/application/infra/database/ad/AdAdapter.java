package io.github.badpop.mari.application.infra.database.ad;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.port.AdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.AdFinderSpi;
import io.github.badpop.mari.application.domain.ad.port.AdUpdaterSpi;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.application.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.github.badpop.mari.application.infra.database.EntityUpdateOperationsApplier;
import io.github.badpop.mari.application.infra.database.user.CurrentUserEntityProvider;
import io.github.badpop.mari.application.infra.database.user.UserEntity;
import io.quarkus.logging.Log;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.UUID;

import static io.vavr.API.*;

@Singleton
@RequiredArgsConstructor
public class AdAdapter implements AdCreatorSpi, AdFinderSpi, AdUpdaterSpi {

  private final CurrentUserEntityProvider userEntityProvider;
  private final AdRepository repository;
  private final EntityUpdateOperationsApplier updateOperationApplier;

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

  private Either<MariFail, Ad> persistAdForUser(Ad ad, UserEntity currentUserEntity) {
    val adEntity = AdEntity.fromDomain(ad, currentUserEntity);
    return repository.persistIfAbsent(adEntity)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Unable to create ad with id=%s for user %s".formatted(ad.id(), currentUserEntity.getId()), t))
            .map(AdEntity::toDomain);
  }

  private Either<MariFail, Ad> retrieveAdByIdAndUser(UUID adId, UserEntity currentUserEntity) {
    return retrieveAdEntityByIdAndUser(adId, currentUserEntity.getId()).map(AdEntity::toDomain);
  }

  private Either<MariFail, Seq<Ad>> retrieveAllAdsForUser(UserEntity currentUserEntity) {
    return repository.findAllByUser(currentUserEntity)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Unable to retrieve all ads for user with id=" + currentUserEntity.getId(), t))
            .flatMap(adEntities -> {
              if (adEntities.isEmpty()) {
                return Left(new ResourceNotFoundFail("No ads for this user"));
              } else {
                return Right(adEntities.map(AdEntity::toDomain));
              }
            });
  }

  private Either<MariFail, AdEntity> retrieveAdEntityByIdAndUser(UUID adId, String userId) {
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
            .map(AdEntity::toDomain);
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
}
