package io.github.badpop.mari.application.app.address;

import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.address.model.Address.AddressCoordinates;
import io.github.badpop.mari.application.domain.address.model.Address.AddressMetadata;

public record AddressApiSchema(String label,
                               String name,
                               int postCode,
                               String city,
                               String municipality,
                               String houseNumber,
                               String street,
                               AddressCoordinatesApiSchema coordinates,
                               AddressMetadataApiSchema metadata) {

    public static AddressApiSchema fromDomain(Address address) {
        return new AddressApiSchema(
                address.label(),
                address.name(),
                address.postCode(),
                address.city(),
                address.municipality(),
                address.houseNumber(),
                address.street(),
                AddressCoordinatesApiSchema.fromDomain(address.coordinates()),
                AddressMetadataApiSchema.fromDomain(address.metadata()));
    }

    public record AddressCoordinatesApiSchema(double longitude, double latitude) {

        public static AddressCoordinatesApiSchema fromDomain(AddressCoordinates coordinates) {
            return new AddressCoordinatesApiSchema(coordinates.longitude(), coordinates.latitude());
        }
    }

    public record AddressMetadataApiSchema(String cityCode,
                                           int population,
                                           String context,
                                           double importance,
                                           String type,
                                           String _type) {

        public static AddressMetadataApiSchema fromDomain(AddressMetadata coordinates) {
            return new AddressMetadataApiSchema(
                    coordinates.cityCode(),
                    coordinates.population(),
                    coordinates.context(),
                    coordinates.importance(),
                    coordinates.type(),
                    coordinates._type());
        }
    }
}
