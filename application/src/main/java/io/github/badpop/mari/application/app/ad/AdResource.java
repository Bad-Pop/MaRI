package io.github.badpop.mari.application.app.ad;

import io.github.badpop.mari.application.app.ad.model.AdCreationBody;
import io.github.badpop.mari.application.domain.ad.port.AdApi;
import io.quarkus.security.Authenticated;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.UUID;

import static io.github.badpop.mari.application.app.MariHeaders.CORRELATION_ID;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Singleton
@Authenticated
@Path(value = "/ads")
@RequiredArgsConstructor
@Produces(APPLICATION_JSON)
public class AdResource {

  private final AdApi api;

  @POST
  public Response create(@HeaderParam(value = CORRELATION_ID) String correlationId,
                         @Valid @NotNull AdCreationBody adCreationBody) {
    val adCreation = adCreationBody.toDomain();
    val toto = api.create(adCreation);
    return null;
  }

  @GET
  @Path("/{id}")
  public Response findById(@HeaderParam(value = CORRELATION_ID) String correlationId,
                           @PathParam("id") @NotNull UUID id) {
    return null;
  }

  @GET
  public Response findAll(@HeaderParam(value = CORRELATION_ID) String correlationId) {
    return null;
  }
}
