package io.github.badpop.mari.app.model.favorite.ads.response;

import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdSummary;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record FavoriteAdSummaryResponse(@Schema(description = "The unique identifier of this ad") String id,
                                        @Schema(description = "The ad name") String name,
                                        @Schema(description = "The ad url") String url,
                                        @Schema(description = "The ad type") FavoriteAdType type) {

  public static FavoriteAdSummaryResponse fromDomain(FavoriteAdSummary domain) {
    return new FavoriteAdSummaryResponse(domain.id(), domain.name(), domain.url(), domain.type());
  }
}
