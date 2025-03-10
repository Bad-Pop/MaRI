package io.github.badpop.mari.application.domain.address;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.control.Either;
import org.geojson.FeatureCollection;

public interface AddressReverseSearchSpi {

  Either<MariFail, FeatureCollection> reverseSearch(double longitude, double latitude);
}
