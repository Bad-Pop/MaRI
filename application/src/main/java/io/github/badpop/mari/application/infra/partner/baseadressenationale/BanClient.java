package io.github.badpop.mari.application.infra.partner.baseadressenationale;

import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeatureCollection;
import io.quarkus.logging.Log;
import io.quarkus.rest.client.reactive.runtime.DefaultMicroprofileRestClientExceptionMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterProvider(BanClient.BanClientLogger.class)
@RegisterRestClient(configKey = "base-adresse-nationale")
@RegisterProvider(value = DefaultMicroprofileRestClientExceptionMapper.class, priority = 5000)
public interface BanClient {

  @GET
  @Path("/search")
  MariGeoCodeJsonFeatureCollection search(@QueryParam("q") String query,
                                          @QueryParam("postCode") Integer postCode,
                                          @QueryParam("type") String type);

  @GET
  @Path("/reverse")
  MariGeoCodeJsonFeatureCollection reverse(@QueryParam("lon") double longitude,
                                           @QueryParam("lat") double latitude);

  @Provider
  class BanClientLogger implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
      responseContext.getStatus();
      responseContext.getHeaders();
      var log = """
              Called base-adresse-nationale => %s > %s
                With headers : %s
               Received response => status: %s
                  With headers : %s
              """.formatted(
              requestContext.getMethod(),
              requestContext.getUri(),
              requestContext.getStringHeaders(),
              responseContext.getStatus(),
              responseContext.getHeaders());
      Log.info(log);
    }
  }
}
