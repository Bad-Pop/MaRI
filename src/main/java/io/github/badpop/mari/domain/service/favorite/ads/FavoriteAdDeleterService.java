package io.github.badpop.mari.domain.service.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.port.api.favorite.ads.FavoriteAdDeleterApi;
import io.github.badpop.mari.domain.port.spi.FavoriteAdDeleterSpi;
import io.quarkus.logging.Log;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FavoriteAdDeleterService implements FavoriteAdDeleterApi {

  private final FavoriteAdDeleterSpi spi;

  @Override
  public Either<MariFail, Void> deleteById(String id) {
    Log.infov("Deleting favorite ad with id {0}", id);
    return spi.deleteById(id);
  }

  @Override
  public Either<MariFail, Void> deleteAll() {
    Log.infov("Deleting all favorite ads");
    return spi.deleteAll();
  }
}
