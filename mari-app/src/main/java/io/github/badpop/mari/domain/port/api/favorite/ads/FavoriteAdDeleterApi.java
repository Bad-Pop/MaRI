package io.github.badpop.mari.domain.port.api.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.vavr.control.Either;

public interface FavoriteAdDeleterApi {

  Either<MariFail, Void> deleteById(String id);

  Either<MariFail, Void> deleteAll();
}
