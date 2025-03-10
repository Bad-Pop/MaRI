package io.github.badpop.mari.application.infra.partner.baseadressenationale;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.Optional;

@RegisterRestClient(configKey = "base-adresse-nationale")
public interface BanClient {

  //TODO : PEUT ÊTRE UNE REDIRECTION 304 À GÉRER : https://developer.mozilla.org/fr/docs/Web/HTTP/Status/304
  @GET
  @Path("/search")
  Response search(@QueryParam("q") String query,
                  @QueryParam("postCode") Optional<Integer> postCode,
                  @QueryParam("type") Optional<String> type);

  //TODO : PEUT ÊTRE UNE REDIRECTION 304 À GÉRER : https://developer.mozilla.org/fr/docs/Web/HTTP/Status/304
  @GET
  @Path("/reverse")
  Response reverse(@QueryParam("lon") double longitude,
                   @QueryParam("lat") double latitude);
}
