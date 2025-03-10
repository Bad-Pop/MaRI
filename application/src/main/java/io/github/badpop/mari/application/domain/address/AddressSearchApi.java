package io.github.badpop.mari.application.domain.address;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.geojson.FeatureCollection;

public interface AddressSearchApi {

  Either<MariFail, FeatureCollection> search(String query, Option<Integer> postCode, Option<String> type);

  Either<MariFail, FeatureCollection> reverseSearch(double longitude, double latitude);
}
