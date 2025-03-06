package io.github.badpop.mari.application.app.ad.model.shared;

import io.github.badpop.mari.application.app.ad.model.AdApiSchema;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.vavr.control.Option;

import java.time.LocalDateTime;
import java.util.UUID;

public record SharedAdApiSchema(UUID id, AdApiSchema ad, boolean expires, Option<LocalDateTime> expireAt) {

  public static SharedAdApiSchema fromDomain(SharedAd sharedAd) {
    return new SharedAdApiSchema(sharedAd.id(), AdApiSchema.fromDomain(sharedAd.ad()), sharedAd.expires(), sharedAd.expireAt());
  }
}
