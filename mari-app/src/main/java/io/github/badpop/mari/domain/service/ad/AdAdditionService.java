package io.github.badpop.mari.domain.service.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.ad.AdAddition;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.port.api.ad.AdAdditionApi;
import io.github.badpop.mari.domain.port.spi.AdAdditionSpi;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static io.github.badpop.mari.domain.service.ad.AdIdGenerator.generateAdId;

@RequiredArgsConstructor
public class AdAdditionService implements AdAdditionApi {

  private final AdAdditionSpi spi;

  @Override
  public Either<MariFail, Ad> createNewAd(AdAddition addition) {
    val adId = generateAdId(addition.adUrl());
    val ad = addition.toAd(adId);
    return spi.createNewAd(ad);
  }
}
