package io.github.badpop.mari.application.generators;

import io.github.badpop.mari.application.infra.postgres.ad.AdEntity;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;

import java.util.UUID;

import static io.github.badpop.mari.application.domain.ad.model.AdType.SALE;

public interface AdEntityGenerator {

  static AdEntity generate(UUID id, UserEntity user) {
    return new AdEntity(id,
            user,
            "test ad",
            "http://www.mari.fr/ads/1234",
            149999.99,
            SALE,
            "description",
            "remarks",
            null);
  }
}
