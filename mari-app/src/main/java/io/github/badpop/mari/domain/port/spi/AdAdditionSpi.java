package io.github.badpop.mari.domain.port.spi;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.vavr.control.Either;

public interface AdAdditionSpi {

  Either<MariFail, Ad> createNewAd(Ad ad);
}
