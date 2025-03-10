package io.github.badpop.mari.application.infra.postgres.ad;

import io.github.badpop.mari.application.domain.ad.model.Ad;
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
public class AdRepository implements MariRepositoryBase<Ad, AdEntity> {

  public Try<Option<AdEntity>> findByIdAndUser(UUID adId, String userId) {
    return Try(() ->
            find("#AdEntity.findByIdAndUser",
                    Parameters.with("adId", adId).and("userId", userId))
                    .<AdEntity>firstResult())
            .map(API::Option);
  }

  public Try<Seq<AdEntity>> findAllByUser(UserEntity user) {
    return Try(() -> find("#AdEntity.findAllByUser", Parameters.with("userId", user.getId())))
            .mapTry(PanacheQuery::list)
            .mapTry(API::Option)
            .map(maybeAds -> maybeAds
                    .map(List::ofAll)
                    .getOrElse(API::List));
  }
}
