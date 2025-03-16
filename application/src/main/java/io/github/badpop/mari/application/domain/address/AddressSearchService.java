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
  public Either<MariFail, MariGeoCodeJsonFeatureCollection> search(String correlationId,
                                                                   String query,
                                                                   Option<Integer> postCode,
                                                                   Option<String> type,
                                                                   Option<Integer> limit) {
    return finderSpi.search(correlationId, query, postCode, type, limit);
  }

  @Override
  public Either<MariFail, MariGeoCodeJsonFeatureCollection> reverseSearch(String correlationId, double longitude, double latitude) {
    return reverseFinderSpi.reverseSearch(correlationId, longitude, latitude);
  }
}
