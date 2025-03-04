package io.github.badpop.mari.application.app;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.val;

import static io.github.badpop.mari.application.app.MariHeaders.CORRELATION_ID;
import static io.vavr.API.Option;
import static java.util.UUID.randomUUID;

@Provider
public class CorrelationIdHeaderRequestFilterProvider implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) {
    val headers = requestContext.getHeaders();

    Option(headers.getFirst(CORRELATION_ID))
            .filter(id -> !id.isBlank())
            .onEmpty(() -> headers.putSingle(CORRELATION_ID, randomUUID().toString()));
  }
}
