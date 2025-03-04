package io.github.badpop.mari.application.domain.control;

import io.github.badpop.mari.libraries.fails.Fail;
import io.vavr.control.Option;

import static io.github.badpop.mari.libraries.fails.Fail.Level.INFO;
import static io.vavr.API.Option;

public sealed interface MariFail extends Fail {

  record TechnicalFail(String message, Throwable causedBy) implements MariFail {
    @Override
    public String code() {
      return "TECHNICAL";
    }

    @Override
    public Option<Throwable> cause() {
      return Option(causedBy);
    }
  }

  record UnauthorizedFail() implements MariFail {
    @Override
    public String code() {
      return "UNAUTHORIZED_FAILURE";
    }

    @Override
    public String message() {
      return "Vous n'êtes pas autorisé à effectuer cette action...";
    }
  }

  record ResourceNotFoundFail(String message) implements MariFail {
    @Override
    public String code() {
      return "RESOURCE_NOT_FOUND_FAILURE";
    }

    @Override
    public Level level() {
      return INFO;
    }
  }
}
