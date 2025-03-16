package io.github.badpop.mari.libraries.geocodejson;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MariGeoCodeJsonFeature extends MariGeoCodeJsonObject {

  @JsonProperty("geometry")
  MariGeoCodeJsonPoint geometry;

  @JsonProperty("properties")
  MariGeoCodeJsonFeatureProperties properties;

  @Data
  @EqualsAndHashCode
  public static class MariGeoCodeJsonFeatureProperties {

    @JsonProperty("label")
    String label;

    @JsonProperty("type")
    TypeProperty type;

    @JsonProperty("name")
    String name;

    @JsonProperty("postcode")
    Integer postCode;

    @JsonProperty("citycode")
    String cityCode;

    @JsonProperty("population")
    int population;

    @JsonProperty("city")
    String city;

    @JsonProperty("context")
    String context;

    @JsonProperty("importance")
    double importance;

    @JsonProperty("municipality")
    String municipality;

    @JsonProperty("housenumber")
    String houseNumber;

    @JsonProperty("street")
    String street;

    @JsonProperty("_type")
    String _type;
  }
}
