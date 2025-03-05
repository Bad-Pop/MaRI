package io.github.badpop.mari.application.app.home.loan;

import io.github.badpop.mari.application.app.ResponseBuilder;
import io.github.badpop.mari.application.app.home.loan.model.HomeLoanBorrowingCapacityApiSchema;
import io.github.badpop.mari.application.app.home.loan.model.HomeLoanMonthlyPaymentApiSchema;
import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanTermUnit;
import io.github.badpop.mari.application.domain.home.loan.port.HomeLoanCalculatorApi;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import static io.github.badpop.mari.application.app.MariHeaders.CORRELATION_ID;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

//TODO OPEN API SPECS
@Singleton
@RequiredArgsConstructor
@Produces(APPLICATION_JSON)
@Path("/home-loan/calculator")
public class HomeLoanResource {

  private final HomeLoanCalculatorApi api;

  @GET
  @Path(value = "/monthly-payment")
  public Response computeMonthlyPayment(@HeaderParam(value = CORRELATION_ID) String correlationId,
                                        @QueryParam(value = "borrowedAmount") @NotNull Double borrowedAmount,
                                        @QueryParam(value = "annualInterestRate") @NotNull Double annualInterestRate,
                                        @QueryParam(value = "term") @NotNull Integer term,
                                        @QueryParam(value = "termUnit") @NotNull HomeLoanTermUnit termUnit) {
    return api.computeMonthlyPayment(borrowedAmount, annualInterestRate, term, termUnit)
            .map(HomeLoanMonthlyPaymentApiSchema::fromDomain)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @GET
  @Path("/borrowing-capacity")
  public Response computeBorrowingCapacity(@HeaderParam(value = CORRELATION_ID) String correlationId,
                                           @QueryParam(value = "annualInterestRate") @NotNull Double annualInterestRate,
                                           @QueryParam(value = "term") @NotNull Integer term,
                                           @QueryParam(value = "termUnit") @NotNull HomeLoanTermUnit termUnit,
                                           @QueryParam(value = "monthlyPayment") @NotNull Double monthlyPayment) {
    return api.computeBorrowingCapacity(annualInterestRate, term, termUnit, monthlyPayment)
            .map(HomeLoanBorrowingCapacityApiSchema::fromDomain)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }
}
