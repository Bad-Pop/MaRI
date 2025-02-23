package io.github.badpop.mari.lib.http.monitoring;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Value
@Builder
public class HttpIOLogBody implements IOLogBody {

    public static HttpIOLogBody EMPTY = new HttpIOLogBody(emptyMap(), "---MASKED---");

    Map<String, Object> headers;
    Object body;
}
