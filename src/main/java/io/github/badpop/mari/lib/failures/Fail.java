package io.github.badpop.mari.lib.failures;

import io.vavr.control.Option;
import lombok.val;

import java.io.StringWriter;
import java.util.Arrays;

import static io.github.badpop.mari.lib.failures.Fail.Level.FATAL;
import static io.vavr.API.None;

public interface Fail {

  String code();

  String message();

  default Level level() {
    return FATAL;
  }

  default Option<Throwable> cause() {
    return None();
  }

  default boolean isInfo() {
    return switch (level()) {
      case FATAL -> false;
      case INFO -> true;
    };
  }

  default boolean isFatal() {
    return switch (level()) {
      case FATAL -> true;
      case INFO -> false;
    };
  }

  default boolean hasCause() {
    return cause().isDefined();
  }

  default String asLog() {
    val prefix = level().name();

    if (hasCause()) {
      var causeWriter = new StringWriter();
      causeWriter.append(cause().get().getClass().getName())
              .append("> ")
              .append(cause().get().getMessage())
              .append("\n")
              .append(String.join("\n", Arrays.stream(cause().get().getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)));

      return "%s : %s with message %s. \nCause : %s".formatted(prefix, code(), message(), causeWriter.toString());
    } else {
      return "%s : %s with message %s".formatted(prefix, code(), message());
    }
  }

  enum Level {
    FATAL,
    INFO
  }
}
