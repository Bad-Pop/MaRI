package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.port.spi.FavoriteAdDeleterSpi;
import io.github.badpop.mari.infra.database.model.favorite.ads.FavoriteAdEntity;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@Transactional
@RequiredArgsConstructor
public class FavoriteAdDeleterAdapter implements FavoriteAdDeleterSpi {

  @Override
  public Either<MariFail, Void> deleteById(String id) {
    return FavoriteAdEntity.deleteById(id);
  }

  @Override
  public Either<MariFail, Void> deleteAll() {
    return FavoriteAdEntity.deleteAllAds();
  }
}
