package io.github.badpop.mari.application;

import io.github.badpop.mari.application.domain.user.User;
import io.github.badpop.mari.application.security.UserProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.vavr.control.Option;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static io.vavr.API.None;
import static io.vavr.API.Some;

@Singleton
@Priority(1)
@Alternative
public class UserTestsProvider implements UserProvider {

  @Inject
  SecurityIdentity identity;

  @Override
  public Option<User> getCurrentUser() {
    if (identity.getPrincipal() != null && identity.getPrincipal(JsonWebToken.class) instanceof JsonWebToken jwt) {
      return Some(new User(
              jwt.getSubject(),
              jwt.getName(),
              jwt.getClaim("nickname"),
              jwt.getClaim("picture"),
              jwt.getClaim("email")));
    }
    return None();
  }
}
