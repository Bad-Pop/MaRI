package io.github.badpop.mari.application.domain.ad.port;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.control.Either;

import java.util.UUID;

public interface AdDeleterSpi {

  Either<MariFail, Void> deleteAdById(UUID id);
}
