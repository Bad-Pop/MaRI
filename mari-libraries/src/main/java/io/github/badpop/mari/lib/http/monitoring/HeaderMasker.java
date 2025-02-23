package io.github.badpop.mari.lib.http.monitoring;

import io.vavr.API;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import jakarta.ws.rs.core.MultivaluedMap;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

public class HeaderMasker {

    private final Set<String> ignoredHeaders;

    private static HeaderMasker INSTANCE;

    protected HeaderMasker(Set<String> ignoredHeaders) {
        this.ignoredHeaders = ignoredHeaders;
    }

    public static HeaderMasker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HeaderMasker(
                    Option.ofOptional(getConfig().getOptionalValues("mari.http.monitoring.io-logs.masked-headers", String.class))
                            .<Set<String>>map(HashSet::ofAll)
                            .getOrElse(API::Set));
        }
        return INSTANCE;
    }

    public static HeaderMasker getInstance(Set<String> ignoredHeaders) {
        if (INSTANCE == null) {
            INSTANCE = new HeaderMasker(ignoredHeaders);
        }
        return INSTANCE;
    }

    public Map<String, Object> filter(MultivaluedMap<String, ?> headers) {
        if (headers == null || headers.isEmpty()) {
            return emptyMap();
        }

        return headers
                .entrySet()
                .stream()
                .collect(toMap(Entry::getKey, this::mapValue));
    }

    private Object mapValue(Entry<String, ? extends List<?>> entry) {
        return ignoredHeaders
                .find(key -> key.equalsIgnoreCase(entry.getKey()))
                .isDefined()
                ? "---MASKED---"
                : flatten(entry.getValue());
    }

    private Object flatten(List<?> value) {
        if ((value != null) && (value.size() == 1)) {
            return value.getFirst();
        }
        return value;
    }
}
