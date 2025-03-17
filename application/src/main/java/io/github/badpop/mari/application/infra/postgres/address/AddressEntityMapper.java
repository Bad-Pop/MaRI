package io.github.badpop.mari.application.infra.postgres.address;

import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.address.model.Address.AddressCoordinates;
import io.github.badpop.mari.application.domain.address.model.Address.AddressMetadata;

public interface AddressEntityMapper {

  static Address toDomain(AddressEntity entity) {
    return new Address(
            entity.getLabel(),
            entity.getName(),
            entity.getPostCode(),
            entity.getCity(),
            entity.getMunicipality(),
            entity.getHouseNumber(),
            entity.getStreet(),
            new AddressCoordinates(entity.getLongitude(), entity.getLatitude()),
            new AddressMetadata(
                    entity.getCityCode(),
                    entity.getPopulation(),
                    entity.getContext(),
                    entity.getImportance(),
                    entity.getType(),
                    entity.get_type()));
  }

  static AddressEntity fromDomain(Address address) {
    return new AddressEntity(
            address.label(),
            address.name(),
            address.postCode(),
            address.city(),
            address.municipality(),
            address.houseNumber(),
            address.street(),
            address.coordinates().longitude(),
            address.coordinates().latitude(),
            address.metadata().cityCode(),
            address.metadata().population(),
            address.metadata().context(),
            address.metadata().importance(),
            address.metadata().type(),
            address.metadata()._type());
  }
}
