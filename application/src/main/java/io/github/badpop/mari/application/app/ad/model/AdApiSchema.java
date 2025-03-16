package io.github.badpop.mari.application.app.ad.model;

import io.github.badpop.mari.application.app.address.AddressApiSchema;
import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.model.AdType;
import io.vavr.control.Option;

import java.util.UUID;

public record AdApiSchema(UUID id,
                          String name,
                          String url,
                          double price,
                          AdType type,
                          Option<String> description,
                          Option<String> remarks,
                          Option<AddressApiSchema> address) {

  public static AdApiSchema fromDomain(Ad ad) {
    return new AdApiSchema(ad.id(),
            ad.name(),
            ad.url(),
            ad.price(),
            ad.type(),
            ad.description(),
            ad.remarks(),
            ad.address().map(AddressApiSchema::fromDomain));
  }
}
