package io.github.badpop.mari.app.resource;

import io.github.badpop.mari.app.model.ApiFailResponse;
import io.github.badpop.mari.app.model.ApiFailResponse.ApiFail;
import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.*;
import io.vavr.collection.List;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.core.Response;
import lombok.val;

import static io.vavr.API.Option;
import static io.vavr.API.Seq;

public interface ResponseBuilder {

  String TECHNICAL_FAIL_ENTITY = """
          {
            "failures": [
              {
                "code": "TECHNICAL_FAILURE",
                "message": "A technical failure occurred. Please try again later..."
              }
            ]
          }
          """;

  static Response ok(@Nonnull Object body) {
    return Response.ok(body).build();
  }

  static Response accepted(@Nullable Object body) {
    return Option(body)
            .map(entity -> Response.accepted(entity).build())
            .getOrElse(() -> Response.accepted().build());
  }

  static Response fromFail(MariFail fail) {
    val entity = failResponseFromFail(fail);
    val builder = switch (fail) {
      //Home Loan calculator fails
      case HomeLoanMonthlyPaymentFail ignored -> Response.serverError().entity(entity);
      case HomeLoanBorrowingCapacityFail ignored -> Response.serverError().entity(entity);
      //Persistence fails
      case ResourceNotFoundFail ignored -> Response.noContent();
      case NoResourceFoundFail ignored -> Response.noContent();
      //Technical fails
      //  On ne retourne pas d'infos détaillées des TechnicalFails car elles peuvent contenir des infos sensibles
      case TechnicalFail ignored -> Response.serverError().entity(TECHNICAL_FAIL_ENTITY);
    };
    return builder.build();
  }

  static Response fromFails(Iterable<MariFail> fails) {
    val failures = List.ofAll(fails);
    val fatalFails = failures.filter(MariFail::isFatal);

    if (fatalFails.nonEmpty()) {
      return Response.serverError()
              .entity(failResponseFromFails(failures))
              .build();
    }
    return Response.status(418) // I'm a tea pot !
            .entity(failResponseFromFails(failures))
            .build();
  }

  private static ApiFailResponse failResponseFromFail(MariFail fail) {
    return new ApiFailResponse(Seq(ApiFail.from(fail)));
  }

  private static ApiFailResponse failResponseFromFails(Iterable<MariFail> fails) {
    val failures = List.ofAll(fails).map(ApiFail::from);
    return new ApiFailResponse(failures);
  }
}
