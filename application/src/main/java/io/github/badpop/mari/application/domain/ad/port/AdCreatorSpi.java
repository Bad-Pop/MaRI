package io.github.badpop.mari.application.domain.ad.port;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.control.Either;

public interface AdCreatorSpi {

  Either<MariFail, Ad> create(Ad ad);
}
