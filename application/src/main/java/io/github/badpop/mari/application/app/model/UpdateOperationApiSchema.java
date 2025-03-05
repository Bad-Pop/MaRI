package io.github.badpop.mari.application.app.model;

import io.github.badpop.mari.application.domain.patch.UpdateOperation;

public record UpdateOperationApiSchema(String op, String path, String value) {

  public UpdateOperation toDomain() {
    return new UpdateOperation(op, path, value);
  }
}
