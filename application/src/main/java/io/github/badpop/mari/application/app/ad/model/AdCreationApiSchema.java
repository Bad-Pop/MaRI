package io.github.badpop.mari.application.app.ad.model;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.model.AdType;
import io.vavr.control.Option;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdCreationApiSchema(@NotBlank String name,
                                  @NotBlank String url,
                                  @NotNull double price,
                                  @NotNull AdType type,
                                  Option<String> description,
                                  Option<String> remarks,
                                  Option<String> address) {

  public AdCreation toDomain() {
    return new AdCreation(name, url, price, type, description, remarks, address);
  }
}
