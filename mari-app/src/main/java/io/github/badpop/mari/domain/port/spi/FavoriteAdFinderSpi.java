package io.github.badpop.mari.domain.port.spi;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdSummary;
import io.vavr.control.Either;

public interface FavoriteAdFinderSpi {

  Either<MariFail, FavoriteAd> findById(String id);

  Either<MariFail, Page<FavoriteAdSummary>> findAll(int page, int size);

  Either<MariFail, Page<FavoriteAdSummary>> findAllByRentalType(int page, int size);

  Either<MariFail, Page<FavoriteAdSummary>> findAllBySaleType(int page, int size);
}
