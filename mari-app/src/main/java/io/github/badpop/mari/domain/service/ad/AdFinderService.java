package io.github.badpop.mari.domain.service.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import io.github.badpop.mari.domain.port.api.ad.AdFinderApi;
import io.github.badpop.mari.domain.port.spi.AdFinderSpi;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdFinderService implements AdFinderApi {

  private final AdFinderSpi spi;

  @Override
  public Either<MariFail, Ad> findById(String id) {
    return spi.findById(id);
  }

  @Override
  public Either<MariFail, Page<AdSummary>> findAll(int page, int size) {
    return spi.findAll(page, size);
  }

  @Override
  public Either<MariFail, Page<AdSummary>> findAllByRentalType(int page, int size) {
    return spi.findAllByRentalType(page, size);
  }

  @Override
  public Either<MariFail, Page<AdSummary>> findAllBySaleType(int page, int size) {
    return spi.findAllBySaleType(page, size);
  }
}
