package io.github.badpop.mari.lib.http.monitoring.input;

import io.github.badpop.mari.lib.http.monitoring.HeaderMasker;
import io.github.badpop.mari.lib.http.monitoring.HttpIOLogBean;
import io.github.badpop.mari.lib.http.monitoring.HttpIOLogBody;
import io.github.badpop.mari.lib.http.monitoring.HttpStatusToIOLogStatusMapper;
import io.vavr.collection.HashMap;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;
import lombok.val;
import org.apache.commons.io.input.TeeInputStream;
import org.jboss.logging.Logger;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Map.Entry;

import static io.github.badpop.mari.lib.http.monitoring.IOLogStatus.KO;
import static io.vavr.API.Option;
import static jakarta.ws.rs.Priorities.ENTITY_CODER;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.toMap;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

@HttpIOLogs
@Provider
@Priority(ENTITY_CODER)
public class HttpIOLogFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String REQUEST_ENTITY_STREAM = "REQUEST_ENTITY_STREAM";
    private static final String INSTANT_KEY = "request-start-instant";
    private static final Logger LOG = Logger.getLogger(HttpIOLogFilter.class);
    private static final HeaderMasker HEADER_MASKER = HeaderMasker.getInstance();

    @Context
    ResourceInfo resourceInfo;

    private final String applicationVersion;
    private final boolean logSuccessDetail;

    public HttpIOLogFilter() {
        this.applicationVersion = getConfig().getOptionalValue("quarkus.application.version", String.class)
                .orElse("UNKNOWN");
        this.logSuccessDetail = getConfig().getOptionalValue("mari.http.monitoring.io-logs.log-success-details", Boolean.class)
                .orElse(true);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        requestContext.setProperty(INSTANT_KEY, currentTimeMillis());

        if (requestContext.hasEntity() && getAnnotation().logRequestDetails()) {
            val stream = new ByteArrayOutputStream();
            requestContext.setEntityStream(new TeeInputStream(requestContext.getEntityStream(), stream, true));
            requestContext.setProperty(REQUEST_ENTITY_STREAM, stream);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        val statusInfo = responseContext.getStatusInfo();
        val ioLogStatus = HttpStatusToIOLogStatusMapper.getInstance().apply(statusInfo.getFamily());
        val annotation = getAnnotation();
        val forceLogDetails = this.logSuccessDetail || KO.equals(ioLogStatus);

        val logBodyInput = annotation.logRequestDetails() && forceLogDetails
                ? HttpIOLogBody.builder()
                .headers(HEADER_MASKER.filter(requestContext.getHeaders()))
                .body(requestContext.getProperty(REQUEST_ENTITY_STREAM))
                .build()
                : HttpIOLogBody.EMPTY;

        val logBodyOutput = annotation.logResponseDetails() && forceLogDetails
                ? HttpIOLogBody.builder()
                .headers(HEADER_MASKER.filter(responseContext.getHeaders()))
                .body(responseContext.getEntity())
                .build()
                : HttpIOLogBody.EMPTY;

        val service = requestContext.getMethod() + " " + requestContext.getUriInfo().getPath();
        var correlationId = requestContext.getHeaderString("X-Correlation-ID");
        val duration = Option(requestContext.getProperty(INSTANT_KEY))
                .map(Long.class::cast)
                .map(start -> currentTimeMillis() - start)
                .getOrElse(0L);

        val ioLog = HttpIOLogBean.builder()
                .version(applicationVersion)
                .correlationId(correlationId)
                .status(ioLogStatus)
                .statusCode(statusInfo.getStatusCode())
                .serviceName(service)
                .queryParams(toMapStringObject(requestContext.getUriInfo().getQueryParameters()))
                .input(logBodyInput)
                .output(logBodyOutput)
                .duration(duration)
                .build();

        LOG.infov("<[IO_LOG]>: {0}", ioLog);
    }

    private HttpIOLogs getAnnotation() {
        return Option(resourceInfo.getResourceMethod().getAnnotation(HttpIOLogs.class))
                .getOrElse(() -> resourceInfo.getResourceClass().getAnnotation(HttpIOLogs.class));
    }

    private io.vavr.collection.Map<String, Object> toMapStringObject(MultivaluedMap<String, String> queryParams) {
        return HashMap.ofAll(Option(queryParams)
                .map(params -> params.entrySet()
                        .stream()
                        .collect(toMap(
                                Entry::getKey,
                                value -> value.getValue().size() == 1 ? value.getValue().getFirst() : value.getValue())))
                .getOrElse(Map::of));
    }
}
