package io.github.badpop.mari.application.app.ad.model.shared;

import io.github.badpop.mari.application.domain.ad.model.shared.SharedAdCreated;

import java.util.UUID;

public record SharedAdCreatedApiSchema(UUID id) {

  public static SharedAdCreatedApiSchema fromDomain(SharedAdCreated sharedAdCreated) {
    return new SharedAdCreatedApiSchema(sharedAdCreated.id());
  }
}
