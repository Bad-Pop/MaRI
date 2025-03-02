package io.github.badpop.mari.infra.database.ad.adapter;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.port.spi.AdDeleterSpi;
import io.github.badpop.mari.infra.database.ad.AdEntity;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@Transactional
@RequiredArgsConstructor
public class AdDeleterAdapter implements AdDeleterSpi {

  @Override
  public Either<MariFail, Void> deleteById(String id) {
    return AdEntity.deleteById(id);
  }

  @Override
  public Either<MariFail, Void> deleteAll() {
    return AdEntity.deleteAllAds();
  }
}
