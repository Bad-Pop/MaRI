package io.github.badpop.mari.lib.http.monitoring;

import io.vavr.collection.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IOLogBean {
    String version;
    String correlationId;
    String serviceName;
    Map<String, Object> queryParams;
    IOLogStatus status;
    IOLogBody input;
    IOLogBody output;
    long duration;

    @Override
    public String toString() {
        return IOLogParser.parse(this);
    }
}
