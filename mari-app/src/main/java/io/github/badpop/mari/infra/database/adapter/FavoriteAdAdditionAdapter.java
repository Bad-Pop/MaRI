package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.port.spi.FavoriteAdAdditionSpi;
import io.github.badpop.mari.infra.database.model.favorite.ads.FavoriteAdEntity;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FavoriteAdAdditionAdapter implements FavoriteAdAdditionSpi {

  @Override
  @Transactional
  public Either<MariFail, FavoriteAd> createNewFavoriteAd(FavoriteAd ad) {
    return FavoriteAdEntity.fromDomain(ad).persistIfAbsent();
  }
}
