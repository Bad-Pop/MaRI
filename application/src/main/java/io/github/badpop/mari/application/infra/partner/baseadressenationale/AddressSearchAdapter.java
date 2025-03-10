package io.github.badpop.mari.application.infra.partner.baseadressenationale;

import io.github.badpop.mari.application.domain.address.AddressReverseSearchSpi;
import io.github.badpop.mari.application.domain.address.AddressSearchSpi;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.geojson.FeatureCollection;

import static io.vavr.API.Try;

@Singleton
public class AddressSearchAdapter implements AddressSearchSpi, AddressReverseSearchSpi {

  private final BanClient client;

  public AddressSearchAdapter(@RestClient BanClient client) {
    this.client = client;
  }

  @Override
  public Either<MariFail, FeatureCollection> search(String query, Option<Integer> postCode, Option<String> type) {
    var toto = Try(() -> client.search(query, postCode.toJavaOptional(), type.toJavaOptional()));
    return null;
  }

  @Override
  public Either<MariFail, FeatureCollection> reverseSearch(double longitude, double latitude) {
    var toto = Try(() -> client.reverse(longitude, latitude));
    return null;
  }
}
