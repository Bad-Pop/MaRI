package io.github.badpop.mari.infra.database.model.favorite.ads.projection;

import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdSummary;
import io.github.badpop.mari.infra.database.model.MariProjection;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record FavoriteAdEntitySummaryProjection(String id,
                                                String name,
                                                String url,
                                                FavoriteAdType type) implements MariProjection<FavoriteAdSummary> {

  @Override
  public FavoriteAdSummary toDomain() {
    return new FavoriteAdSummary(id, name, url, type);
  }
}
