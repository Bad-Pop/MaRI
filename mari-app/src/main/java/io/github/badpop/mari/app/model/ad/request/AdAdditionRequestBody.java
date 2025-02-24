package io.github.badpop.mari.app.model.ad.request;

import io.github.badpop.mari.domain.control.ad.AdAddition;
import io.github.badpop.mari.domain.model.ad.AdType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import static io.vavr.API.Option;

@Schema(required = true, description = "Ad addition request body")
public record AdAdditionRequestBody(@Schema(description = "The ad name", required = true) @NotBlank String adName,
                                    @Schema(description = "the ad url", required = true) @NotBlank String adUrl,
                                    @Schema(description = "Property price", required = true) @NotNull Double price,
                                    @Schema(description = "the ad description") String description,
                                    @Schema(description = "Some remarks ?") String remarks,
                                    @Schema(description = "Property address") String address,
                                    @Schema(description = "Property price per square meter") Double pricePerSquareMeter) {

  public AdAddition toDomain(AdType adType) {
    return new AdAddition(
            adName,
            adUrl,
            adType,
            price,
            Option(description),
            Option(remarks),
            Option(address),
            Option(pricePerSquareMeter));
  }
}
