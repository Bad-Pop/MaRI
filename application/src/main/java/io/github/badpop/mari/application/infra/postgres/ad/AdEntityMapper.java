package io.github.badpop.mari.application.infra.postgres.ad;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.infra.postgres.address.AddressEntityMapper;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;
import lombok.val;

import static io.vavr.API.Option;

public interface AdEntityMapper {

  static Ad toDomain(AdEntity entity) {
    return new Ad(
            entity.getId(),
            entity.getName(),
            entity.getUrl(),
            entity.getPrice(),
            entity.getType(),
            Option(entity.getDescription()),
            Option(entity.getRemarks()),
            Option(entity.getAddress()).map(AddressEntityMapper::toDomain));
  }

  static AdEntity fromDomain(Ad ad, UserEntity owner) {
    val addressEntity = ad.address().map(AddressEntityMapper::fromDomain);
    return new AdEntity(
            ad.id(),
            owner,
            ad.name(),
            ad.url(),
            ad.price(),
            ad.type(),
            ad.description().getOrNull(),
            ad.remarks().getOrNull(),
            addressEntity.getOrNull());
  }
}
