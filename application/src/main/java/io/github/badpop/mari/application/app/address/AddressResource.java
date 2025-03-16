package io.github.badpop.mari.application.app.address;

import io.github.badpop.mari.application.app.ResponseBuilder;
import io.github.badpop.mari.application.domain.address.AddressSearchApi;
import io.quarkiverse.bucket4j.runtime.RateLimited;
import io.quarkus.security.Authenticated;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

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

  @GET
  @Path("/search")
  @RateLimited(bucket = "search_addresses_bucket")
  public Response search(@QueryParam("query") @NotBlank String query,
                         @QueryParam("postCode") Integer postCode,
                         @QueryParam("type") String type) {
    return api.search(query, Option(postCode), Option(type))
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }

  @GET
  @Path("/reverse-search")
  @RateLimited(bucket = "search_addresses_bucket")
  public Response reverse(@QueryParam("longitude") @NotNull Double longitude,
                          @QueryParam("latitude") @NotNull Double latitude) {
    return api.reverseSearch(longitude, latitude)
            .fold(ResponseBuilder::fail, ResponseBuilder::ok);
  }
}
