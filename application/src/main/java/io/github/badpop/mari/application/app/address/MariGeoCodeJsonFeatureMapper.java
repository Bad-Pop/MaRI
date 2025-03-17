package io.github.badpop.mari.application.app.address;

import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.address.model.Address.AddressCoordinates;
import io.github.badpop.mari.application.domain.address.model.Address.AddressMetadata;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeature;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonPoint;

public interface MariGeoCodeJsonFeatureMapper {

  static Address toDomain(MariGeoCodeJsonFeature address) {
    var props = address.getProperties();
    return new Address(
            props.getLabel(),
            props.getName(),
            props.getPostCode(),
            props.getCity(),
            props.getMunicipality(),
            props.getHouseNumber(),
            props.getStreet(),
            toDomain(address.getGeometry()),
            toDomain(props));
  }

  private static AddressCoordinates toDomain(MariGeoCodeJsonPoint geometry) {
    return new AddressCoordinates(geometry.getCoordinates().head(), geometry.getCoordinates().last());
  }

  private static AddressMetadata toDomain(MariGeoCodeJsonFeature.MariGeoCodeJsonFeatureProperties props) {
    return new AddressMetadata(props.getCityCode(),
            props.getPopulation(),
            props.getContext(),
            props.getImportance(),
            props.getType().name(),
            props.get_type());
  }
}
