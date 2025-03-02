package io.github.badpop.mari.context;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.vavr.control.Option;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static io.github.badpop.mari.context.UserContextProvider.UserContext.authenticated;
import static io.vavr.API.None;
import static io.vavr.API.Some;

@Singleton
public class UserContextOidcProvider implements UserContextProvider {

  @Inject
  SecurityIdentity identity;

  @Inject
  JsonWebToken token;

  @Override
  public Option<UserContext> getUserContext() {
    if (identity.getPrincipal() != null && identity.getPrincipal() instanceof JWTCallerPrincipal principal) {
      return Some(authenticated(
              principal.getName(),
              principal.getClaim("name"),
              principal.getClaim("nickname")));
    } else if (token != null) {
      return Some(authenticated(
              token.getName(),
              token.getClaim("name"),
              token.getClaim("nickname")));
    } else {
      return None();
    }
  }
}
