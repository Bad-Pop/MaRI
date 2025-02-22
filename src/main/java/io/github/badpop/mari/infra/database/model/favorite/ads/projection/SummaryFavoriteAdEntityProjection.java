package io.github.badpop.mari.infra.database.model.favorite.ads.projection;

import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import io.github.badpop.mari.domain.model.favorite.ads.SummaryFavoriteAd;
import io.github.badpop.mari.infra.database.model.MariProjection;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record SummaryFavoriteAdEntityProjection(String id,
                                                String name,
                                                String url,
                                                FavoriteAdType type) implements MariProjection<SummaryFavoriteAd> {

  @Override
  public SummaryFavoriteAd toDomain() {
    return new SummaryFavoriteAd(id, name, url, type);
  }
}
