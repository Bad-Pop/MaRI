package io.github.badpop.mari.application.app.address;

import io.quarkiverse.bucket4j.runtime.RateLimited;
import io.quarkus.security.Authenticated;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.geojson.FeatureCollection;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

//TODO OPEN API SPECS
@Singleton
@Authenticated
@RequiredArgsConstructor
@Path(value = "/addresses")
@Produces(APPLICATION_JSON)
public class AddressResource {

  @GET
  @Path("/search")
  @RateLimited(bucket = "search_addresses_bucket")
  public Response search(@QueryParam("q") @NotBlank String searchQuery,
                         @QueryParam("postCode") Integer postCode,
                         @QueryParam("type") String type) {
    FeatureCollection features;//TODO IMPLEMENTS
    return null;
  }
}
