package io.github.badpop.mari.application.domain.ad.port.shared;

import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.control.Either;

import java.util.UUID;

public interface SharedAdApi {

  Either<MariFail, SharedAd> findById(UUID id);
}
