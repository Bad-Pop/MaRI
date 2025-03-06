package io.github.badpop.mari.application.app.ad.model.shared;

import io.github.badpop.mari.application.domain.ad.control.AdSharingParameters;
import io.vavr.control.Option;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AdSharingParametersApiSchema(@NotNull Boolean expires, Option<LocalDateTime> expireAt) {

  public AdSharingParameters toDomain() {
    return new AdSharingParameters(expires, expireAt);
  }
}
