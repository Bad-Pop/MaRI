package io.github.badpop.mari.domain.control.favorite.ads;

import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import io.vavr.control.Option;

public record FavoriteAdAddition(String adName,
                                 String adUrl,
                                 FavoriteAdType adType,
                                 Option<String> description,
                                 Option<String> remarks,
                                 Option<String> address,
                                 Option<Double> price,
                                 Option<Double> pricePerSquareMeter) {

  public FavoriteAd toAd(String adId) {
    return new FavoriteAd(adId, adName, adUrl, adType, description, remarks, address, price, pricePerSquareMeter);
  }
}
