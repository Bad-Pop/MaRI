package io.github.badpop.mari.domain.port.api;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.SummaryFavoriteAd;
import io.vavr.control.Either;

public interface FavoriteAdFinderApi {

  Either<MariFail, FavoriteAd> findById(String id);

  Either<MariFail, Page<SummaryFavoriteAd>> findAll(int page, int size);

  Either<MariFail, Page<SummaryFavoriteAd>> findAllByRentalType(int page, int size);

  Either<MariFail, Page<SummaryFavoriteAd>> findAllBySaleType(int page, int size);
}
