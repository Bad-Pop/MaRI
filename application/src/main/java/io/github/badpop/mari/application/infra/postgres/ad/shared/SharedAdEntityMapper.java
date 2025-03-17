package io.github.badpop.mari.application.infra.postgres.ad.shared;

import io.github.badpop.mari.application.domain.ad.control.AdSharingParameters;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.infra.postgres.ad.AdEntity;
import io.github.badpop.mari.application.infra.postgres.ad.AdEntityMapper;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;

import java.util.UUID;

import static io.vavr.API.Option;

public interface SharedAdEntityMapper {

  static SharedAd toDomain(SharedAdEntity entity) {
    return new SharedAd(
            entity.getId(),
            AdEntityMapper.toDomain(entity.getAd()),
            entity.isExpires(),
            Option(entity.getExpireAt()));
  }

  static SharedAdEntity fromDomain(UUID id, AdEntity ad, UserEntity user, AdSharingParameters parameters) {
    return new SharedAdEntity(id, ad, user, parameters.expires(), parameters.expireAt().getOrNull());
  }
}
