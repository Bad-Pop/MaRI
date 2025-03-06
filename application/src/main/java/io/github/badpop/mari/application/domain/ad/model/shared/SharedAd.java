package io.github.badpop.mari.application.domain.ad.model.shared;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.vavr.control.Option;

import java.time.LocalDateTime;
import java.util.UUID;

public record SharedAd(UUID id, Ad ad, boolean expires, Option<LocalDateTime> expireAt) {

  public boolean isNotExpired() {
    if (!expires()) {
      return true;
    } else if (expireAt().filter(expireAt -> LocalDateTime.now().isAfter(expireAt)).isEmpty()) {
      return true;
    }
    return false;
  }
}
