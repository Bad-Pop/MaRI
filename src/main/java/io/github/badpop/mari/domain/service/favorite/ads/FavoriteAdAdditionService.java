package io.github.badpop.mari.domain.service.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.favorite.ads.FavoriteAdAddition;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.port.api.FavoriteAdAdditionApi;
import io.github.badpop.mari.domain.port.spi.FavoriteAdAdditionSpi;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static io.github.badpop.mari.domain.service.favorite.ads.FavoriteAdIdGenerator.generateFavoriteAdId;

@RequiredArgsConstructor
public class FavoriteAdAdditionService implements FavoriteAdAdditionApi {

  private final FavoriteAdAdditionSpi spi;

  @Override
  public Either<MariFail, FavoriteAd> createNewFavoriteAd(FavoriteAdAddition addition) {
    val adId = generateFavoriteAdId(addition.adUrl());
    val favoriteAd = addition.toAd(adId);
    return spi.createNewFavoriteAd(favoriteAd);
  }
}
