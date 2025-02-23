package io.github.badpop.mari.app.model.favorite.ads.request;

import io.github.badpop.mari.domain.control.favorite.ads.FavoriteAdAddition;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import static io.vavr.API.Option;

@Schema(required = true, description = "Favorite ad addition request body")
public record FavoriteAdAdditionRequestBody(@Schema(description = "The ad name", required = true) @NotBlank String adName,
                                            @Schema(description = "the ad url", required = true) @NotBlank String adUrl,
                                            @Schema(description = "the ad description") String description,
                                            @Schema(description = "Some remarks ?") String remarks,
                                            @Schema(description = "Property address") String address,
                                            @Schema(description = "Property price") Double price,
                                            @Schema(description = "Property price per square meter") Double pricePerSquareMeter) {

  public FavoriteAdAddition toDomain(FavoriteAdType adType) {
    return new FavoriteAdAddition(
            adName,
            adUrl,
            adType,
            Option(description),
            Option(remarks),
            Option(address),
            Option(price),
            Option(pricePerSquareMeter));
  }
}
