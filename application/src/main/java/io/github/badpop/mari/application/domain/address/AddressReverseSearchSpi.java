package io.github.badpop.mari.application.domain.address;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeatureCollection;
import io.vavr.control.Either;

public interface AddressReverseSearchSpi {

  Either<MariFail, MariGeoCodeJsonFeatureCollection> reverseSearch(double longitude, double latitude);
}
