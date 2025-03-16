package io.github.badpop.mari.application.infra.partner.baseadressenationale;

import io.github.badpop.mari.application.domain.address.AddressReverseSearchSpi;
import io.github.badpop.mari.application.domain.address.AddressSearchSpi;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeatureCollection;
import io.quarkus.logging.Log;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import lombok.val;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import static io.vavr.API.Try;

@Singleton
public class AddressSearchAdapter implements AddressSearchSpi, AddressReverseSearchSpi {

  private final BanClient client;

  public AddressSearchAdapter(@RestClient BanClient client) {
    this.client = client;
  }

  @Override
  public Either<MariFail, MariGeoCodeJsonFeatureCollection> search(String query, Option<Integer> postCode, Option<String> type) {
    return Try(() -> client.search(query, postCode.getOrNull(), type.getOrNull()))
            .toEither()
            .mapLeft(this::handleWebApplicationException)
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  @Override
  public Either<MariFail, MariGeoCodeJsonFeatureCollection> reverseSearch(double longitude, double latitude) {
    return Try(() -> client.reverse(longitude, latitude))
            .onFailure(WebApplicationException.class, this::handleWebApplicationException)
            .toEither()
            .mapLeft(t -> new TechnicalFail("Unable to reverse search for addresses, an error occurred.", t));
  }

  private MariFail handleWebApplicationException(Throwable t) {
    if(t instanceof WebApplicationException wae) {
      val response = wae.getResponse();

      if(response.hasEntity()) {
        return new TechnicalFail(
                "An error occurred while calling partner base-adresse-nationale. Unable to properly handle response with body : " + response.readEntity(String.class), wae);
      } else {
        return new TechnicalFail(
                "An error occurred while calling partner base-adresse-nationale. Unable to properly handle response", wae);
      }
    }

    return new TechnicalFail("An unknown error occurred while trying to call partner base-adresse-nationale", t);
  }
}
