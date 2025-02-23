package io.github.badpop.mari.domain.port.spi;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.vavr.control.Either;

public interface FavoriteAdAdditionSpi {

  Either<MariFail, FavoriteAd> createNewFavoriteAd(FavoriteAd ad);
}
