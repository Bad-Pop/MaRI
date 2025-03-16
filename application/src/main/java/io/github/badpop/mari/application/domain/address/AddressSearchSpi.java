package io.github.badpop.mari.application.domain.address;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeatureCollection;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface AddressSearchSpi {

  Either<MariFail, MariGeoCodeJsonFeatureCollection> search(String correlationId,
                                                            String query,
                                                            Option<Integer> postCode,
                                                            Option<String> type,
                                                            Option<Integer> limit);
}
