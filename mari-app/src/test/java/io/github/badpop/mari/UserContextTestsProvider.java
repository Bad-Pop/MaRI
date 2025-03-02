package io.github.badpop.mari;

import io.github.badpop.mari.context.UserContextProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.vavr.control.Option;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static io.github.badpop.mari.context.UserContextProvider.UserContext.authenticated;
import static io.vavr.API.None;
import static io.vavr.API.Some;

@Singleton
@Alternative
@Priority(1)
public class UserContextTestsProvider implements UserContextProvider {

  @Inject
  SecurityIdentity identity;

  @Override
  public Option<UserContext> getUserContext() {
    if(identity.getPrincipal() != null && identity.getPrincipal(JsonWebToken.class) instanceof JsonWebToken jwt) {
      return Some(authenticated(jwt.getSubject(), jwt.getName(), jwt.getClaim("nickname")));
    }
    return None();
  }
}
