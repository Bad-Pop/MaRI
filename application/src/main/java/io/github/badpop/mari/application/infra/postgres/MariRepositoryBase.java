package io.github.badpop.mari.application.infra.postgres;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.vavr.control.Try;

import java.util.UUID;

import static io.vavr.API.Try;

public interface MariRepositoryBase<DOMAIN, ENTITY extends MariEntityBase<ENTITY, DOMAIN>> extends PanacheRepositoryBase<ENTITY, UUID> {

  default Try<Boolean> isPresent(ENTITY entity) {
    return Try(() -> isPersistent(entity));
  }

  default Try<Boolean> isAbsent(ENTITY entity) {
    return isPresent(entity).map(present -> !present);
  }

  default Try<ENTITY> persistIfAbsent(ENTITY entity) {
    return isAbsent(entity)
            .map(isAbsent -> {
              if (isAbsent) persist(entity);
              return entity;
            });
  }
}
