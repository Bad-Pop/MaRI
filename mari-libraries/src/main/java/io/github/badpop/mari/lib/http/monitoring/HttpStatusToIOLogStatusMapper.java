package io.github.badpop.mari.lib.http.monitoring;

import jakarta.ws.rs.core.Response.Status.Family;

import java.util.Map;
import java.util.function.Function;

import static io.github.badpop.mari.lib.http.monitoring.IOLogStatus.KO;
import static io.github.badpop.mari.lib.http.monitoring.IOLogStatus.OK;
import static jakarta.ws.rs.core.Response.Status.Family.*;

public final class HttpStatusToIOLogStatusMapper implements Function<Family, IOLogStatus> {

    private static final HttpStatusToIOLogStatusMapper INSTANCE = new HttpStatusToIOLogStatusMapper();

    public static HttpStatusToIOLogStatusMapper getInstance() {
        return INSTANCE;
    }

    private static final Map<Family, IOLogStatus> FAMILY_TO_STATUS_MAP = Map.of(
            INFORMATIONAL, OK,
            SUCCESSFUL, OK,
            REDIRECTION, OK,
            CLIENT_ERROR, KO,
            SERVER_ERROR, KO,
            OTHER, KO);

    @Override
    public IOLogStatus apply(Family family) {
        return FAMILY_TO_STATUS_MAP.getOrDefault(family, KO);
    }
}
