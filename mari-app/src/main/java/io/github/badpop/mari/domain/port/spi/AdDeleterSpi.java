package io.github.badpop.mari.domain.port.spi;

import io.github.badpop.mari.domain.control.MariFail;
import io.vavr.control.Either;

public interface AdDeleterSpi {

  Either<MariFail, Void> deleteById(String id);

  Either<MariFail, Void> deleteAll();
}
