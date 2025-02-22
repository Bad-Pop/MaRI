package io.github.badpop.mari.app.model.favorite.ads.response;

import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "A favorite ad")
public record FavoriteAdResponse(@Schema(description = "The unique identifier of this ad") String id,
                                 @Schema(description = "The ad name") String name,
                                 @Schema(description = "The ad url") String url,
                                 @Schema(description = "The ad type") FavoriteAdType type,
                                 @Schema(description = "The ad description") String description,
                                 @Schema(description = "Any remarks ?") String remarks,
                                 @Schema(description = "The Property address") String address,
                                 @Schema(description = "The property price") Double price,
                                 @Schema(description = "The property price per square meter") Double pricePerSquareMeter) {

  public static FavoriteAdResponse fromDomain(FavoriteAd domain) {
    return new FavoriteAdResponse(
            domain.id(),
            domain.name(),
            domain.url(),
            domain.type(),
            domain.description().getOrNull(),
            domain.remarks().getOrNull(),
            domain.address().getOrNull(),
            domain.price().getOrNull(),
            domain.pricePerSquareMeter().getOrNull());
  }
}
