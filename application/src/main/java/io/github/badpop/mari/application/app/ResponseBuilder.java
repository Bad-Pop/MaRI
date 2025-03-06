package io.github.badpop.mari.application.app;

import io.github.badpop.mari.application.app.model.FailsApiSchema;
import io.github.badpop.mari.application.app.model.FailsApiSchema.FailApiSchema;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.*;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import jakarta.validation.constraints.Null;
import jakarta.ws.rs.core.Response;
import lombok.val;

import static io.vavr.API.Seq;

public interface ResponseBuilder {

  String DEFAULT_TECHNICAL_FAIL_BODY = """
          "failures" : [
            {
              "code" : "TECHNICAL_FAILURE",
              "message" : "Une erreur technique est survenue..."
            }
          ]
          """;

  static <T> Response ok(T body) {
    return Response.ok(body).build();
  }

  static <T> Response accepted(@Null T nullBody) {
    return Response.accepted().build();
  }

  static Response fail(MariFail fail) {
    val entity = fromFail(fail);
    return switch (fail) {
      //Ads fails
      case InvalidAdUrlFail ignored -> Response.status(400).entity(entity).build();
      //Shared ads fails
      case ExpiredSharedAdFail ignored -> Response.status(400).entity(entity).build();
      //Home Loan calculator fails
      case HomeLoanMonthlyPaymentFail ignored -> Response.serverError().entity(entity).build();
      case HomeLoanBorrowingCapacityFail ignored -> Response.serverError().entity(entity).build();
      //Common fails
      case UnauthorizedFail ignored -> Response.status(401).build();
      case ResourceNotFoundFail ignored -> Response.noContent().build();
      case ForbiddenPatchOperationFail ignored -> Response.status(400).entity(entity).build();
      case InvalidRequestFail ignored -> Response.status(400).entity(entity).build();
      case TechnicalFail ignored -> Response.serverError().entity(DEFAULT_TECHNICAL_FAIL_BODY).build();
    };
  }

  static Response fails(Iterable<MariFail> fails) {
    val failures = List.ofAll(fails);
    val fatalFails = failures.filter(MariFail::isFatal);

    if(fatalFails.nonEmpty()) {
      return Response.serverError()
              .entity(fromFails(failures))
              .build();
    }

    return Response.status(418) // I'm a tea pot !
            .entity(fromFails(failures))
            .build();
  }

  private static FailsApiSchema fromFail(MariFail fail) {
    return new FailsApiSchema(Seq(new FailApiSchema(fail.code(), fail.message())));
  }

  private static FailsApiSchema fromFails(Seq<MariFail> fails) {
    return new FailsApiSchema(fails.map(f -> {
      if(f instanceof TechnicalFail) {
        return new FailApiSchema(f.code(), "Une erreur technique est survenue...");
      }
      return new FailApiSchema(f.code(), f.message());
    }));
  }
}
