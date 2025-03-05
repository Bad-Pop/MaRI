package io.github.badpop.mari.application.domain.ad;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.port.AdApi;
import io.github.badpop.mari.application.domain.ad.port.AdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.AdFinderSpi;
import io.github.badpop.mari.application.domain.ad.port.AdUpdaterSpi;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static io.github.badpop.mari.application.domain.ad.module.AdValidations.validateAdCreationUrl;
import static io.github.badpop.mari.application.domain.ad.module.AdValidations.validateThatUpdateOperationsDoesNotContainsForbiddenOperations;

@RequiredArgsConstructor
public class AdService implements AdApi {

  private final AdCreatorSpi creatorSpi;
  private final AdFinderSpi finderSpi;
  private final AdUpdaterSpi updaterSpi;

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
  public Either<MariFail, Seq<Ad>> findAll() {
    return finderSpi.findAll();
  }

  @Override
  public Either<MariFail, Ad> updateAdById(UUID id, Seq<UpdateOperation> operations) {
    return validateThatUpdateOperationsDoesNotContainsForbiddenOperations(operations)
            .toEither()
            .flatMap(unused -> updaterSpi.updateAdById(id, operations));
  }
}
