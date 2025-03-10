package io.github.badpop.mari.libraries.geocodejson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(property = "type", use = NAME)
@JsonSubTypes({
        @Type(value = MariGeoCodeJsonFeatureCollection.class, name = "FeatureCollection"),
        @Type(value = MariGeoCodeJsonFeature.class, name = "Feature"),
})
public abstract class MariGeoCodeJsonObject {
}
