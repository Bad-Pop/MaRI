package io.github.badpop.mari.app.model.ad.response;

import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.model.ad.AdType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "An ad")
public record AdResponse(@Schema(description = "The unique identifier of this ad") String id,
                         @Schema(description = "The ad name") String name,
                         @Schema(description = "The ad url") String url,
                         @Schema(description = "The ad type") AdType type,
                         @Schema(description = "The property price") Double price,
                         @Schema(description = "The ad description") String description,
                         @Schema(description = "Any remarks ?") String remarks,
                         @Schema(description = "The Property address") String address,
                         @Schema(description = "The property price per square meter") Double pricePerSquareMeter) {

  public static AdResponse fromDomain(Ad domain) {
    return new AdResponse(
            domain.id(),
            domain.name(),
            domain.url(),
            domain.type(),
            domain.price(),
            domain.description().getOrNull(),
            domain.remarks().getOrNull(),
            domain.address().getOrNull(),
            domain.pricePerSquareMeter().getOrNull());
  }
}
