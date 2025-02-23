package io.github.badpop.mari.domain.port.api.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.favorite.ads.FavoriteAdAddition;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.vavr.control.Either;

public interface FavoriteAdAdditionApi {

  Either<MariFail, FavoriteAd> createNewFavoriteAd(FavoriteAdAddition addition);
}
