package io.github.badpop.mari.application.app.model;

import io.vavr.collection.Seq;

public record FailsApiSchema(Seq<FailApiSchema> failures) {

  public record FailApiSchema(String code, String message) {}
}
