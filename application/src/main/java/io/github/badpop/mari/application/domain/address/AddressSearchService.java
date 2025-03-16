package io.github.badpop.mari.application.domain.address;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeatureCollection;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddressSearchService implements AddressSearchApi {

  private final AddressSearchSpi finderSpi;
  private final AddressReverseSearchSpi reverseFinderSpi;

  @Override
  public Either<MariFail, MariGeoCodeJsonFeatureCollection> search(String query, Option<Integer> postCode, Option<String> type) {
    return finderSpi.search(query, postCode, type);
  }

  @Override
  public Either<MariFail, MariGeoCodeJsonFeatureCollection> reverseSearch(double longitude, double latitude) {
    return reverseFinderSpi.reverseSearch(longitude, latitude);
  }
}
