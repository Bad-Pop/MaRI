package io.github.badpop.mari.application.app.address;

import io.github.badpop.mari.application.app.ResponseBuilder;
import io.github.badpop.mari.application.domain.address.AddressSearchApi;
import io.quarkiverse.bucket4j.runtime.RateLimited;
import io.quarkus.security.Authenticated;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static io.github.badpop.mari.application.app.MariHeaders.CORRELATION_ID;
import static io.vavr.API.Option;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

//TODO OPEN API SPECS
@Singleton
@Authenticated
@RequiredArgsConstructor
@Path(value = "/addresses")
@Produces(APPLICATION_JSON)
public class AddressResource {

  private final AddressSearchApi api;

  @ConfigProperty(name = "mari.addresses.search.max-limit")
  Integer maxLimit;

  @GET
  @Path("/search")
  @RateLimited(bucket = "search_addresses_bucket")
  public Response search(@HeaderParam(value = CORRELATION_ID) String correlationId,
                         @QueryParam("query") @NotBlank String query,
                         @QueryParam("postCode") Integer postCode,
                         @QueryParam("type") String type,
                         @QueryParam("limit") Integer limit) {
    var effectiveLimit = Option(limit)
            .map(this::validateLimit);

    return api.search(correlationId, query, Option(postCode), Option(type), effectiveLimit)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @GET
  @Path("/reverse-search")
  @RateLimited(bucket = "search_addresses_bucket")
  public Response reverse(@HeaderParam(value = CORRELATION_ID) String correlationId,
                          @QueryParam("longitude") @NotNull Double longitude,
                          @QueryParam("latitude") @NotNull Double latitude) {
    return api.reverseSearch(correlationId, longitude, latitude)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  private int validateLimit(Integer givenLimit) {
    if(givenLimit <= 0 || givenLimit > maxLimit) {
      return maxLimit;
    }
    return givenLimit;
  }
}
