package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdSummary;
import io.github.badpop.mari.domain.port.spi.FavoriteAdFinderSpi;
import io.github.badpop.mari.infra.database.model.favorite.ads.FavoriteAdEntity;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.RENTAL;
import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.SALE;

@Singleton
@RequiredArgsConstructor
public class FavoriteAdFinderAdapter implements FavoriteAdFinderSpi {

  public Either<MariFail, FavoriteAd> findById(String id) {
    return FavoriteAdEntity.findById(id).map(FavoriteAdEntity::toDomain);
  }

  @Override
  public Either<MariFail, Page<FavoriteAdSummary>> findAll(int page, int size) {
    return FavoriteAdEntity.findAll(page, size);
  }

  @Override
  public Either<MariFail, Page<FavoriteAdSummary>> findAllByRentalType(int page, int size) {
    return FavoriteAdEntity.findAllByType(RENTAL, page, size);
  }

  @Override
  public Either<MariFail, Page<FavoriteAdSummary>> findAllBySaleType(int page, int size) {
    return FavoriteAdEntity.findAllByType(SALE, page, size);
  }
}
