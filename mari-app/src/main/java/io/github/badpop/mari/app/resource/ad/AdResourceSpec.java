package io.github.badpop.mari.app.resource.ad;

import io.github.badpop.mari.app.model.ApiFailResponse;
import io.github.badpop.mari.app.model.ad.request.AdAdditionRequestBody;
import io.github.badpop.mari.app.model.ad.response.AdResponse;
import io.github.badpop.mari.app.model.ad.response.AdSummaryPageResponse;
import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path(value = "/ads")
@Tags(value = {@Tag(name = "Ads")})
public interface AdResourceSpec {

  //TODO REFACTOR : MERGE WITH ADDITION SALE
  @POST
  @Path("/rental")
  @Operation(summary = "Create new rental ad", description = "Create a new rental ad")
  @APIResponses(value = {
          @APIResponse(responseCode = "200",
                  description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = AdResponse.class))}),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500",
                  description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response addNewRentalAd(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                          @Valid @NotNull AdAdditionRequestBody additionRequest);

  //TODO REFACTOR : MERGE WITH ADDITION RENTAL
  @POST
  @Path("/sale")
  @Operation(summary = "Create new sale ad", description = "Create a new sale ad")
  @APIResponses(value = {
          @APIResponse(responseCode = "200",
                  description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = AdResponse.class))}),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500",
                  description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response addNewSaleAd(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                        @Valid @NotNull AdAdditionRequestBody additionRequest);

  @GET
  @Path("/{adId}")
  @Operation(summary = "Get an ad", description = "Get an ad by its id")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = AdResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getAdById(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                     @NotBlank @PathParam(value = "adId") String adId);

  @GET
  @Path("/rental")
  @Operation(summary = "Get all rental ads", description = "Get all the rental ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = AdSummaryPageResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getAllRentalAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                           @Parameter(description = "The desired page number") @QueryParam("page") Integer page,
                           @Parameter(description = "The desired page items limit") @QueryParam("limit") Integer limit);

  @GET
  @Path("/sale")
  @Operation(summary = "Get all sale ads", description = "Get all the sale ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = AdSummaryPageResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getAllSaleAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                         @Parameter(description = "The desired page number") @QueryParam("page") Integer page,
                         @Parameter(description = "The desired page items limit") @QueryParam("limit") Integer limit);

  @GET
  @Operation(summary = "Get all ads", description = "Get all ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = AdSummaryPageResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getAllAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                     @Parameter(description = "The desired page number") @QueryParam("page") Integer page,
                     @Parameter(description = "The desired page items limit") @QueryParam("limit") Integer limit);

  @DELETE
  @Path("/{adId}")
  @Operation(summary = "Delete an ad", description = "Delete an ad by its id")
  @APIResponses(value = {
          @APIResponse(responseCode = "202", description = "Accepted"),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response deleteAdById(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                        @NotBlank @PathParam(value = "adId") String adId);

  @DELETE
  @Operation(summary = "Delete all ads", description = "Delete all ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "202", description = "Accepted"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response deleteAllAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId);
}
