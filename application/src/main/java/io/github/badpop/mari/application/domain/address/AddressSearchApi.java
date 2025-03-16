package io.github.badpop.mari.application.domain.address;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeatureCollection;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.geojson.FeatureCollection;

public interface AddressSearchApi {

  Either<MariFail, MariGeoCodeJsonFeatureCollection> search(String query, Option<Integer> postCode, Option<String> type);

  Either<MariFail, MariGeoCodeJsonFeatureCollection> reverseSearch(double longitude, double latitude);
}
