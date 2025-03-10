package io.github.badpop.mari.application.generators;

import io.github.badpop.mari.application.infra.postgres.ad.AdEntity;
import io.github.badpop.mari.application.infra.postgres.ad.shared.SharedAdEntity;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SharedAdEntityGenerator {

  static SharedAdEntity generate(UUID id, AdEntity ad, UserEntity user, boolean expires, LocalDateTime expireAt) {
    return new SharedAdEntity(id, ad, user, expires, expireAt);
  }
}
