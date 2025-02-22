package io.github.badpop.mari.lib.http.monitoring;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HttpIOLogBean extends IOLogBean{
    int statusCode;
}
