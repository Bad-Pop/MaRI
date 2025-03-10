package io.github.badpop.mari.libraries.geocodejson;

import io.vavr.collection.Seq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static io.vavr.API.Seq;

@Data
@EqualsAndHashCode(callSuper = true)
public class MariGeoCodeJsonFeatureCollection extends MariGeoCodeJsonObject implements Iterable<MariGeoCodeJsonFeature> {

  private Seq<MariGeoCodeJsonFeature> features = Seq();

  @Override
  public @NotNull Iterator<MariGeoCodeJsonFeature> iterator() {
    return features.iterator();
  }
}
