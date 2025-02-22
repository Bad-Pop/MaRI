package io.github.badpop.mari.app.resource.spec;

import io.github.badpop.mari.app.model.ApiFailResponse;
import io.github.badpop.mari.app.model.home.loan.HomeLoanBorrowingCapacityResponse;
import io.github.badpop.mari.app.model.home.loan.HomeLoanMonthlyPaymentResponse;
import io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import static io.github.badpop.mari.app.resource.common.MariHeaders.CORRELATION_ID;

@Produces("application/json")
@Path(value = "/home-loan/calculator")
@Tags(value = {@Tag(name = "Home Loan Calculator")})
public interface HomeLoanCalculatorResourceSpec {

    @GET
    @Path(value = "/monthly-payment")
    @Operation(summary = "Calculating monthly payments", description = "Calculate your monthly loan payments")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                         description = "Success",
                         content = {@Content(mediaType = "application/json", schema = @Schema(implementation = HomeLoanMonthlyPaymentResponse.class))}),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500",
                    description = "Internal error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiFailResponse.class))})
    })
    Response computeMonthlyPayment(@Parameter(description = "L'id de la requête'") @HeaderParam(value = CORRELATION_ID) String correlationId,
                                   @Parameter(description = "Le montant du capital à emprunter") @QueryParam(value = "borrowedAmount") @NotNull Double borrowedAmount,
                                   @Parameter(description = "Le taux d'intérêt annuel") @QueryParam(value = "annualInterestRate") @NotNull Double annualInterestRate,
                                   @Parameter(description = "La période de remboursement") @QueryParam(value = "term") @NotNull Integer term,
                                   @Parameter(description = "Le adType de période de remboursement") @QueryParam(value = "termUnit") @NotNull HomeLoanTermUnit termUnit);

    @GET
    @Path("/borrowing-capacity")
    @Operation(summary = "Calculating your borrowing capacity", description = "Calculate your borrowing capacity for a loan")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Success",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = HomeLoanBorrowingCapacityResponse.class))}),
            @APIResponse(responseCode = "400", description = "Bad request"),
            @APIResponse(responseCode = "500",
                    description = "Internal error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiFailResponse.class))})
    })
    Response computeBorrowingCapacity(@Parameter(description = "L'id de la requête'") @HeaderParam(value = CORRELATION_ID) String correlationId,
                                      @Parameter(description = "Le taux d'intérêt annuel") @QueryParam(value = "annualInterestRate") @NotNull Double annualInterestRate,
                                      @Parameter(description = "La durée de remboursement") @QueryParam(value = "term") @NotNull Integer term,
                                      @Parameter(description = "Le adType de durée de remboursement") @QueryParam(value = "termUnit") @NotNull HomeLoanTermUnit termUnit,
                                      @Parameter(description = "Le montant des mensualités") @QueryParam(value = "monthlyPayment") @NotNull Double monthlyPayment);
}
