package io.github.badpop.mari.lib.http.monitoring.output;

import io.github.badpop.mari.lib.http.monitoring.HeaderMasker;
import io.github.badpop.mari.lib.http.monitoring.HttpIOLogBody;
import io.github.badpop.mari.lib.http.monitoring.HttpStatusToIOLogStatusMapper;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.Response.Status;
import lombok.val;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.badpop.mari.lib.http.monitoring.IOLogStatus.KO;
import static io.github.badpop.mari.lib.http.monitoring.IOLogStatus.OK;
import static io.vavr.API.Option;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyList;
import static org.eclipse.microprofile.config.ConfigProvider.getConfig;

public abstract class HttpPartnerIOLogFilter implements ClientRequestFilter, ClientResponseFilter {

    private static final String START_INSTANT_KEY = "partner-start-instant";
    private static final Logger LOG = Logger.getLogger(HttpPartnerIOLogFilter.class);
    private static final HeaderMasker HEADER_MASKER = HeaderMasker.getInstance();

    private final String applicationVersion;
    private final boolean logSuccessDetail;

    public HttpPartnerIOLogFilter() {
        this.applicationVersion = getConfig().getOptionalValue("quarkus.application.version", String.class)
                .orElse("UNKNOWN");
        this.logSuccessDetail = getConfig().getOptionalValue("mari.http.monitoring.io-logs.log-success-details", Boolean.class)
                .orElse(true);
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        requestContext.setProperty(START_INSTANT_KEY, currentTimeMillis());
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        val now = currentTimeMillis();
        val startedTimeMillis = Option((long) requestContext.getProperty(START_INSTANT_KEY)).getOrElse(now);
        val duration = now - startedTimeMillis;

        val statusInfo = responseContext.getStatusInfo();
        val status = Option(statusInfo.toEnum())
                .filter(considerSuccessful()::contains)
                .map(s -> OK)
                .getOrElse(() -> HttpStatusToIOLogStatusMapper.getInstance().apply(statusInfo.getFamily()));

        boolean logRequestDetails = logResponseDetails() && (this.logSuccessDetail || KO.equals(status));
        boolean logResponseDetails = logResponseDetails() && (this.logSuccessDetail || KO.equals(status));

        val queryParams = new HashMap<String, Object>();
        splitAsList(requestContext.getUri().getQuery(), "&")
                .stream()
                .map(params -> params.split("="))
                .forEach(param -> {
                    if (param.length == 1) {
                        queryParams.put(param[0], "");
                    } else {
                        queryParams.put(param[0], param[1]);
                    }
                });

        val logInputBody = logRequestDetails
                ? HttpIOLogBody.builder()
                .headers(HEADER_MASKER.filter(requestContext.getHeaders()))
                .body(extractResponseEntity(responseContext))
                .build()
                : HttpIOLogBody.EMPTY;

        val logOutputBody = logResponseDetails
                ? HttpIOLogBody.builder()
                .headers(HEADER_MASKER.filter(responseContext.getHeaders()))
                .body(responseContext.hasEntity() ? extractResponseEntity(responseContext) : "---NO_BODY---")
                .build()
                : HttpIOLogBody.EMPTY;

        val service = requestContext.getMethod() + " " + requestContext.getUri().getPath();
        val correlationId = requestContext.getHeaderString("X-Correlation-ID");

        val ioLog = HttpPartnerIOLogBean.builder()
                .partner(partnerName())
                .version(applicationVersion)
                .correlationId(correlationId)
                .status(status)
                .statusCode(statusInfo.getStatusCode())
                .serviceName(service)
                .queryParams(io.vavr.collection.HashMap.ofAll(queryParams))
                .input(logInputBody)
                .output(logOutputBody)
                .duration(duration)
                .build();

        LOG.infov("<[IO_PARTNER_LOG]>: {0}", ioLog);
    }

    protected abstract String partnerName();

    private List<String> splitAsList(String valueToSplit, String separator) {
        return Option(valueToSplit)
                .map(value -> Arrays.asList(value.split(separator)))
                .getOrElse(emptyList());
    }

    private ByteArrayInputStream extractResponseEntity(ClientResponseContext responseContext) {
        try {
            val entityStream = responseContext.getEntityStream();
            val streamBytes = entityStream.readAllBytes();
            responseContext.setEntityStream(new ByteArrayInputStream(streamBytes));
            entityStream.close();
            return new ByteArrayInputStream(streamBytes);
        } catch (IOException e) {
            LOG.error("Unable to extract and copy response entity...", e);
            return new ByteArrayInputStream("".getBytes());
        }
    }

    protected List<Status> considerSuccessful() {
        return emptyList();
    }

    protected boolean logRequestDetails() {
        return true;
    }

    protected boolean logResponseDetails() {
        return true;
    }
}
