package io.github.badpop.mari.application.app.ad;

import io.github.badpop.mari.application.app.ResponseBuilder;
import io.github.badpop.mari.application.app.ad.model.AdApiSchema;
import io.github.badpop.mari.application.app.ad.model.AdCreationApiSchema;
import io.github.badpop.mari.application.app.ad.model.shared.AdSharingParametersApiSchema;
import io.github.badpop.mari.application.app.ad.model.shared.SharedAdApiSchema;
import io.github.badpop.mari.application.app.ad.model.shared.SharedAdCreatedApiSchema;
import io.github.badpop.mari.application.app.model.UpdateOperationApiSchema;
import io.github.badpop.mari.application.domain.ad.port.AdApi;
import io.quarkus.security.Authenticated;
import io.vavr.collection.List;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.UUID;

import static io.github.badpop.mari.application.app.MariHeaders.CORRELATION_ID;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_PATCH_JSON;

//TODO OPEN API SPECS
@Singleton
@Authenticated
@Path(value = "/ads")
@RequiredArgsConstructor
@Produces(APPLICATION_JSON)
public class AdResource {

  private final AdApi api;

  @POST
  @Consumes(APPLICATION_JSON)
  public Response create(@HeaderParam(value = CORRELATION_ID) String correlationId,
                         @Valid @NotNull AdCreationApiSchema adCreationApiSchema) {
    val adCreation = adCreationApiSchema.toDomain();
    return api.create(adCreation)
            .map(AdApiSchema::fromDomain)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @GET
  @Path("/{id}")
  public Response findById(@HeaderParam(value = CORRELATION_ID) String correlationId,
                           @PathParam("id") @NotNull UUID id) {
    return api.findById(id)
            .map(AdApiSchema::fromDomain)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  @GET
  public Response findAll(@HeaderParam(value = CORRELATION_ID) String correlationId) {
    return api.findAll()
            .map(ads -> ads.map(AdApiSchema::fromDomain))
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @PATCH
  @Path("/{id}")
  @Consumes(APPLICATION_JSON_PATCH_JSON)
  public Response update(@HeaderParam(value = CORRELATION_ID) String correlationId,
                         @PathParam("id") @NotNull UUID id,
                         @NotNull @NotEmpty java.util.List<UpdateOperationApiSchema> operations) {
    val domainOperations = List.ofAll(operations).map(UpdateOperationApiSchema::toDomain);
    return api.updateAdById(id, domainOperations)
            .map(AdApiSchema::fromDomain)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteById(@HeaderParam(value = CORRELATION_ID) String correlationId, @PathParam("id") @NotNull UUID id) {
    return api.deleteAdById(id).fold(ResponseBuilder::fail, ResponseBuilder::accepted);
  }

  @POST
  @Path("/{id}/share")
  @Consumes(APPLICATION_JSON)
  public Response shareAdById(@HeaderParam(value = CORRELATION_ID) String correlationId,
                              @PathParam("id") @NotNull UUID id,
                              @Valid @NotNull AdSharingParametersApiSchema parameters) {
    val domainParameters = parameters.toDomain();
    return api.shareAdById(id, domainParameters)
            .map(SharedAdCreatedApiSchema::fromDomain)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @GET
  @Path("/shared")
  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  public Response findAllSharedAds(@HeaderParam(value = CORRELATION_ID) String correlationId) {
    return api.findAllSharedAds()
            .map(sharedAds -> sharedAds.map(SharedAdApiSchema::fromDomain))
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @DELETE
  @Path("/{id}/shared")
  public Response deleteSharedAdById(@HeaderParam(value = CORRELATION_ID) String correlationId,
                                     @PathParam("id") @NotNull UUID id) {
    return api.deleteSharedAdById(id).fold(ResponseBuilder::fail, ResponseBuilder::accepted);
  }
}
