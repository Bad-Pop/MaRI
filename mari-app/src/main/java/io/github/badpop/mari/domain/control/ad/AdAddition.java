package io.github.badpop.mari.domain.control.ad;

import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.model.ad.AdType;
import io.vavr.control.Option;

public record AdAddition(String adName,
                         String adUrl,
                         AdType adType,
                         Double price,
                         Option<String> description,
                         Option<String> remarks,
                         Option<String> address,
                         Option<Double> pricePerSquareMeter) {

  public Ad toAd(String adId) {
    return new Ad(adId, adName, adUrl, adType, price, description, remarks, address, pricePerSquareMeter);
  }
}
