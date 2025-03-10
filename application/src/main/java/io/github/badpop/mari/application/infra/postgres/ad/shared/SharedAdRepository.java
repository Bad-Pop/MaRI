package io.github.badpop.mari.application.infra.postgres.ad.shared;

import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.infra.postgres.MariRepositoryBase;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static io.vavr.API.Try;

@Singleton
@RequiredArgsConstructor
public class SharedAdRepository implements MariRepositoryBase<SharedAd, SharedAdEntity> {

  public Try<Option<SharedAdEntity>> findSharedAdById(UUID id) {
    return Try(() -> find("id", id).<SharedAdEntity>firstResult()).mapTry(API::Option);
  }

  public Try<Option<SharedAdEntity>> findByIdAndUser(UUID sharedAdId, String userId) {
    return Try(() ->
            find("#SharedAdEntity.findByIdAndUser",
                    Parameters.with("sharedAdId", sharedAdId).and("userId", userId))
                    .<SharedAdEntity>firstResult())
            .map(API::Option);
  }

  public Try<Seq<SharedAdEntity>> findAllByUser(UserEntity user) {
    return Try(() -> find("#SharedAdEntity.findAllByUser", Parameters.with("userId", user.getId())))
            .mapTry(PanacheQuery::list)
            .mapTry(API::Option)
            .map(maybeSharedAds -> maybeSharedAds
                    .map(List::ofAll)
                    .getOrElse(API::List));
  }
}
