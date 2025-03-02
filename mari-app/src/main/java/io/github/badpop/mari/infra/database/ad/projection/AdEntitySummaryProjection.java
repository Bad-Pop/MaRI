package io.github.badpop.mari.infra.database.ad.projection;

import io.github.badpop.mari.domain.model.ad.AdType;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import io.github.badpop.mari.infra.database.MariProjection;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record AdEntitySummaryProjection(String id,
                                        String name,
                                        String url,
                                        AdType type,
                                        Double price) implements MariProjection<AdSummary> {

  @Override
  public AdSummary toDomain() {
    return new AdSummary(id, name, url, type, price);
  }
}
