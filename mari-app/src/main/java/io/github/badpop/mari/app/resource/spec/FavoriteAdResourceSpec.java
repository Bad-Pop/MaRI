package io.github.badpop.mari.app.resource.spec;

import io.github.badpop.mari.app.model.ApiFailResponse;
import io.github.badpop.mari.app.model.favorite.ads.request.FavoriteAdAdditionRequestBody;
import io.github.badpop.mari.app.model.favorite.ads.response.FavoriteAdResponse;
import io.github.badpop.mari.app.model.favorite.ads.response.FavoriteAdSummaryPageResponse;
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
@Path(value = "/favorite-ads")
@Tags(value = {@Tag(name = "Favorite Ads")})
public interface FavoriteAdResourceSpec {

  @POST
  @Path("/rental")
  @Operation(summary = "Create new rental favorite ad", description = "Create a new rental favorite ad")
  @APIResponses(value = {
          @APIResponse(responseCode = "200",
                  description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = FavoriteAdResponse.class))}),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500",
                  description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response addNewRentalFavoriteAd(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                                  @Valid @NotNull FavoriteAdAdditionRequestBody additionRequest);

  @POST
  @Path("/sale")
  @Operation(summary = "Create new sale favorite ad", description = "Create a new sale favorite ad")
  @APIResponses(value = {
          @APIResponse(responseCode = "200",
                  description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = FavoriteAdResponse.class))}),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500",
                  description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response addNewSaleFavoriteAd(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                                @Valid @NotNull FavoriteAdAdditionRequestBody additionRequest);

  @GET
  @Path("/{favoriteAdId}")
  @Operation(summary = "Get a favorite ad", description = "Get a favorite ad by its id")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = FavoriteAdResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getFavoriteAdById(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                             @NotBlank @PathParam(value = "favoriteAdId") String favoriteAdId);

  @GET
  @Path("/rental")
  @Operation(summary = "Get all rental favorite ads", description = "Get all the rental favorite ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = FavoriteAdSummaryPageResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getAllRentalFavoriteAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                                   @Parameter(description = "The desired page number") @QueryParam("page") Integer page,
                                   @Parameter(description = "The desired page items limit") @QueryParam("limit") Integer limit);

  @GET
  @Path("/sale")
  @Operation(summary = "Get all sale favorite ads", description = "Get all the sale favorite ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = FavoriteAdSummaryPageResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getAllSaleFavoriteAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                                 @Parameter(description = "The desired page number") @QueryParam("page") Integer page,
                                 @Parameter(description = "The desired page items limit") @QueryParam("limit") Integer limit);

  @GET
  @Operation(summary = "Get all favorite ads", description = "Get all favorite ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "200", description = "Success",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = FavoriteAdSummaryPageResponse.class))}),
          @APIResponse(responseCode = "204", description = "No content"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response getAllFavoriteAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                             @Parameter(description = "The desired page number") @QueryParam("page") Integer page,
                             @Parameter(description = "The desired page items limit") @QueryParam("limit") Integer limit);

  @DELETE
  @Path("/{favoriteAdId}")
  @Operation(summary = "Delete a favorite ad", description = "Delete a favorite ad by its id")
  @APIResponses(value = {
          @APIResponse(responseCode = "202", description = "Accepted"),
          @APIResponse(responseCode = "400", description = "Bad request",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ViolationReport.class))}),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response deleteFavoriteAdById(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId,
                                @NotBlank @PathParam(value = "favoriteAdId") String favoriteAdId);

  @DELETE
  @Operation(summary = "Delete all favorite ads", description = "Delete all favorite ads")
  @APIResponses(value = {
          @APIResponse(responseCode = "202", description = "Accepted"),
          @APIResponse(responseCode = "500", description = "Internal error",
                  content = {@Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = ApiFailResponse.class))})
  })
  Response deleteAllFavoriteAds(@Parameter(description = "The request id") @HeaderParam(value = CORRELATION_ID) String correlationId);
}
