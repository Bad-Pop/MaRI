package io.github.badpop.mari.domain.port.api.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import io.vavr.control.Either;

public interface AdFinderApi {

  Either<MariFail, Ad> findById(String id);

  Either<MariFail, Page<AdSummary>> findAll(int page, int size);

  Either<MariFail, Page<AdSummary>> findAllByRentalType(int page, int size);

  Either<MariFail, Page<AdSummary>> findAllBySaleType(int page, int size);
}
