package io.github.badpop.mari.application.infra.partner.baseadressenationale;

import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeatureCollection;
import io.quarkus.logging.Log;
import io.quarkus.rest.client.reactive.runtime.DefaultMicroprofileRestClientExceptionMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import static io.github.badpop.mari.application.app.MariHeaders.CORRELATION_ID;

/**
 * <li>
 *   <ul>https://adresse.data.gouv.fr/ressources-et-documentations</ul>
 *   <ul>https://doc.adresse.data.gouv.fr/</ul>
 *   <ul>https://adresse.data.gouv.fr/outils/api-doc/adresse</ul>
 *   <ul>https://www.data.gouv.fr/fr/dataservices/api-adresse-base-adresse-nationale-ban</ul>
 *   <ul>https://github.com/BaseAdresseNationale/addok-docker#installer-une-instance-avec-les-donn%C3%A9es-de-la-base-adresse-nationale</ul>
 * </li>
 */
@RegisterProvider(BanClient.BanClientLogger.class)
@RegisterRestClient(configKey = "base-adresse-nationale")
@RegisterProvider(value = DefaultMicroprofileRestClientExceptionMapper.class, priority = 5000)
public interface BanClient {

  @GET
  @Path("/search")
  MariGeoCodeJsonFeatureCollection search(@HeaderParam(CORRELATION_ID) String correlationId,
                                          @QueryParam("q") String query,
                                          @QueryParam("postCode") Integer postCode,
                                          @QueryParam("type") String type,
                                          @QueryParam("limit") Integer limit);

  @GET
  @Path("/reverse")
  MariGeoCodeJsonFeatureCollection reverse(@HeaderParam(CORRELATION_ID) String correlationId,
                                           @QueryParam("lon") double longitude,
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
