package io.github.badpop.mari.application.security;

import io.github.badpop.mari.application.domain.user.User;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.vavr.control.Option;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static io.vavr.API.None;
import static io.vavr.API.Some;

@Singleton
public class UserOidcProvider implements UserProvider {

  @Inject
  SecurityIdentity identity;

  @Inject
  JsonWebToken token;

  @Override
  public Option<User> getCurrentUser() {

    if (identity.getPrincipal() != null && identity.getPrincipal() instanceof JWTCallerPrincipal principal) {
      return Some(new User(
              principal.getSubject(),
              principal.getClaim("name"),
              principal.getClaim("nickname"),
              principal.getClaim("picture"),
              principal.getClaim("email")));
    } else if (token != null) {
      return Some(new User(
              token.getSubject(),
              token.getClaim("name"),
              token.getClaim("nickname"),
              token.getClaim("picture"),
              token.getClaim("email")));
    } else {
      return None();
    }
  }
}
