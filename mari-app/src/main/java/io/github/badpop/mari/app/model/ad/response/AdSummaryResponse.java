package io.github.badpop.mari.app.model.ad.response;

import io.github.badpop.mari.domain.model.ad.AdType;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record AdSummaryResponse(@Schema(description = "The unique identifier of this ad") String id,
                                @Schema(description = "The ad name") String name,
                                @Schema(description = "The ad url") String url,
                                @Schema(description = "The ad type") AdType type,
                                @Schema(description = "The property price") Double price) {

  public static AdSummaryResponse fromDomain(AdSummary domain) {
    return new AdSummaryResponse(domain.id(), domain.name(), domain.url(), domain.type(), domain.price());
  }
}
