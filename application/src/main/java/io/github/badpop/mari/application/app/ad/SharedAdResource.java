package io.github.badpop.mari.application.app.ad;

import io.github.badpop.mari.application.app.ResponseBuilder;
import io.github.badpop.mari.application.app.ad.model.shared.SharedAdApiSchema;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdApi;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static io.github.badpop.mari.application.app.MariHeaders.CORRELATION_ID;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

//TODO OPEN API SPECS
@Singleton
@Path(value = "/shared-ads")
@RequiredArgsConstructor
@Produces(APPLICATION_JSON)
public class SharedAdResource {

  private final SharedAdApi api;

  @GET
  @Path("/{id}")
  public Response findById(@HeaderParam(value = CORRELATION_ID) String correlationId,
                           @PathParam("id") @NotNull UUID id) {
    return api.findById(id)
            .map(SharedAdApiSchema::fromDomain)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }
}
