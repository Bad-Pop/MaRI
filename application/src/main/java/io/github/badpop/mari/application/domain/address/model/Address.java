package io.github.badpop.mari.application.domain.address.model;

public record Address(String label,
                      String name,
                      int postCode,
                      String city,
                      String municipality,
                      String houseNumber,
                      String street,
                      AddressCoordinates coordinates,
                      AddressMetadata metadata) {

  public record AddressCoordinates(double longitude, double latitude) {}

  public record AddressMetadata(String cityCode,
                                int population,
                                String context,
                                double importance,
                                String type,
                                String _type) {}
}
