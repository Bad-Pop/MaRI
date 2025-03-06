package io.github.badpop.mari.application.generators;

import io.github.badpop.mari.application.infra.database.ad.AdEntity;
import io.github.badpop.mari.application.infra.database.ad.shared.SharedAdEntity;
import io.github.badpop.mari.application.infra.database.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SharedAdEntityGenerator {

  static SharedAdEntity generate(UUID id, AdEntity ad, UserEntity user, boolean expires, LocalDateTime expireAt) {
    return new SharedAdEntity(id, ad, user, expires, expireAt);
  }
}
