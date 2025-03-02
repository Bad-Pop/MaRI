package io.github.badpop.mari.infra.database.ad.adapter;

import io.github.badpop.mari.context.UserContextProvider;
import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import io.github.badpop.mari.domain.port.spi.AdFinderSpi;
import io.github.badpop.mari.domain.port.spi.UserSpi;
import io.github.badpop.mari.infra.database.ad.AdEntity;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import static io.github.badpop.mari.domain.model.ad.AdType.RENTAL;
import static io.github.badpop.mari.domain.model.ad.AdType.SALE;

@Singleton
@RequiredArgsConstructor
public class AdFinderAdapter implements AdFinderSpi {

  private final UserContextProvider userContextProvider;

  public Either<MariFail, Ad> findById(String id) {
    return AdEntity.findById(id).map(AdEntity::toDomain);
  }

  @Override
  public Either<MariFail, Page<AdSummary>> findAll(int page, int size) {
    return AdEntity.findAll(page, size);
  }

  @Override
  public Either<MariFail, Page<AdSummary>> findAllByRentalType(int page, int size) {
    return AdEntity.findAllByType(RENTAL, page, size);
  }

  @Override
  public Either<MariFail, Page<AdSummary>> findAllBySaleType(int page, int size) {
    return AdEntity.findAllByType(SALE, page, size);
  }
}
