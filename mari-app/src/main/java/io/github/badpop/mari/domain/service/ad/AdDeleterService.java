package io.github.badpop.mari.domain.service.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.port.api.ad.AdDeleterApi;
import io.github.badpop.mari.domain.port.spi.AdDeleterSpi;
import io.quarkus.logging.Log;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdDeleterService implements AdDeleterApi {

  private final AdDeleterSpi spi;

  @Override
  public Either<MariFail, Void> deleteById(String id) {
    Log.infov("Deleting ad with id {0}", id);
    return spi.deleteById(id);
  }

  @Override
  public Either<MariFail, Void> deleteAll() {
    Log.infov("Deleting all ads");
    return spi.deleteAll();
  }
}
