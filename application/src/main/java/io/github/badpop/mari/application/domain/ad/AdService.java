package io.github.badpop.mari.application.domain.ad;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.control.AdSharingParameters;
import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAdCreated;
import io.github.badpop.mari.application.domain.ad.port.*;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdDeleterSpi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdFinderSpi;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static io.github.badpop.mari.application.domain.ad.module.AdValidations.*;
import static io.vavr.API.Right;
import static io.vavr.API.Seq;

@RequiredArgsConstructor
public class AdService implements AdApi {

  private final AdCreatorSpi creatorSpi;
  private final AdFinderSpi finderSpi;
  private final AdUpdaterSpi updaterSpi;
  private final AdDeleterSpi deleterSpi;
  private final SharedAdCreatorSpi sharedAdCreatorSpi;
  private final SharedAdFinderSpi sharedAdFinderSpi;
  private final SharedAdDeleterSpi sharedAdDeleterSpi;

  @Override
  public Either<MariFail, Ad> create(AdCreation adCreation) {
    return validateAdCreationUrl(adCreation)
            .toEither()
            .map(unused -> adCreation.toAd(UUID.randomUUID()))
            .flatMap(creatorSpi::create);
  }

  @Override
  public Either<MariFail, Ad> findById(UUID id) {
    return finderSpi.findById(id);
  }

  @Override
  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  public Either<MariFail, Seq<Ad>> findAll() {
    return finderSpi.findAll();
  }

  @Override
  public Either<MariFail, Ad> updateAdById(UUID id, Seq<UpdateOperation> operations) {
    return validateThatUpdateOperationsDoesNotContainsForbiddenOperations(operations)
            .toEither()
            .flatMap(unused -> updaterSpi.updateAdById(id, operations));
  }

  @Override
  public Either<MariFail, Void> deleteAdById(UUID id) {
    return deleterSpi.deleteAdById(id);
  }

  @Override
  public Either<MariFail, SharedAdCreated> shareAdById(UUID id, AdSharingParameters parameters) {
    return validateSharingAdParameters(parameters)
            .toEither()
            .flatMap(unused -> sharedAdCreatorSpi.createSharedAdById(id, parameters, UUID.randomUUID()));
  }

  @Override
  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  public Either<MariFail, Seq<SharedAd>> findAllSharedAds() {
    return sharedAdFinderSpi.findAll();
  }

  @Override
  public Either<MariFail, Void> deleteSharedAdById(UUID id) {
    return sharedAdDeleterSpi.deleteById(id);
  }
}
