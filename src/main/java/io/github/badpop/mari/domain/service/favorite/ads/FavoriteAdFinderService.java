package io.github.badpop.mari.domain.service.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.SummaryFavoriteAd;
import io.github.badpop.mari.domain.port.api.FavoriteAdFinderApi;
import io.github.badpop.mari.domain.port.spi.FavoriteAdFinderSpi;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FavoriteAdFinderService implements FavoriteAdFinderApi {

  private final FavoriteAdFinderSpi spi;

  @Override
  public Either<MariFail, FavoriteAd> findById(String id) {
    return spi.findById(id);
  }

  @Override
  public Either<MariFail, Page<SummaryFavoriteAd>> findAll(int page, int size) {
    return spi.findAll(page, size);
  }

  @Override
  public Either<MariFail, Page<SummaryFavoriteAd>> findAllByRentalType(int page, int size) {
    return spi.findAllByRentalType(page, size);
  }

  @Override
  public Either<MariFail, Page<SummaryFavoriteAd>> findAllBySaleType(int page, int size) {
    return spi.findAllBySaleType(page, size);
  }
}
