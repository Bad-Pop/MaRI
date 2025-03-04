package io.github.badpop.mari.application.domain.ad.control;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.model.AdType;
import io.vavr.control.Option;

import java.util.UUID;

public record AdCreation(String name,
                         String url,
                         double price,
                         AdType type,
                         Option<String> description,
                         Option<String> remarks,
                         Option<String> address) {

  public Ad toAd(UUID adId) {
    return new Ad(adId, name, url, price, type, description, remarks, address);
  }
}
