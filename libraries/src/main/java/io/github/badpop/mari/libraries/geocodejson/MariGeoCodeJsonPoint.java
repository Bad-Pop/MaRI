package io.github.badpop.mari.libraries.geocodejson;

import io.vavr.collection.Seq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static io.vavr.API.Seq;

@Data
@EqualsAndHashCode(callSuper = true)
public class MariGeoCodeJsonPoint extends MariGeoCodeJsonObject {

  Seq<Double> coordinates = Seq();// 0 = longitude; 1 = latitude
}
