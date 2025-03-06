package io.github.badpop.mari.application.domain.ad.port.shared;

import io.github.badpop.mari.application.domain.ad.control.AdSharingParameters;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAdCreated;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.control.Either;

import java.util.UUID;

public interface SharedAdCreatorSpi {

  Either<MariFail, SharedAdCreated> createSharedAdById(UUID adId, AdSharingParameters parameters, UUID sharedAdId);
}
