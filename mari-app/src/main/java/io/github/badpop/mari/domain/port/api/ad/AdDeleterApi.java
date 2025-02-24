package io.github.badpop.mari.domain.port.api.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.vavr.control.Either;

public interface AdDeleterApi {

  Either<MariFail, Void> deleteById(String id);

  Either<MariFail, Void> deleteAll();
}
