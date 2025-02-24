package io.github.badpop.mari.domain.port.api.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.ad.AdAddition;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.vavr.control.Either;

public interface AdAdditionApi {

  Either<MariFail, Ad> createNewAd(AdAddition addition);
}
