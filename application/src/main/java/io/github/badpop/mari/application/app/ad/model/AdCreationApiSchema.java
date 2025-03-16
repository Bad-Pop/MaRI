package io.github.badpop.mari.application.app.ad.model;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.model.AdType;
import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.address.model.Address.AddressCoordinates;
import io.github.badpop.mari.application.domain.address.model.Address.AddressMetadata;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeature;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonFeature.MariGeoCodeJsonFeatureProperties;
import io.github.badpop.mari.libraries.geocodejson.MariGeoCodeJsonPoint;
import io.vavr.control.Option;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdCreationApiSchema(@NotBlank String name,
                                  @NotBlank String url,
                                  @NotNull double price,
                                  @NotNull AdType type,
                                  Option<String> description,
                                  Option<String> remarks,
                                  Option<MariGeoCodeJsonFeature> address) {

  public AdCreation toDomain() {
    return new AdCreation(name, url, price, type, description, remarks, address.map(this::toDomain));
  }

  private Address toDomain(MariGeoCodeJsonFeature address) {
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

  private AddressCoordinates toDomain(MariGeoCodeJsonPoint geometry) {
    return new AddressCoordinates(geometry.getCoordinates().head(), geometry.getCoordinates().last());
  }

  private AddressMetadata toDomain(MariGeoCodeJsonFeatureProperties props) {
    return new AddressMetadata(props.getCityCode(),
            props.getPopulation(),
            props.getContext(),
            props.getImportance(),
            props.getType().name(),
            props.get_type());
  }
}
