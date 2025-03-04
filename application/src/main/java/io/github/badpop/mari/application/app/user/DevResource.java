package io.github.badpop.mari.application.app.user;

import io.github.badpop.mari.application.security.UserProvider;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/dev/users")
@IfBuildProfile("dev")
@RequiredArgsConstructor
@Produces(APPLICATION_JSON)
public class DevResource {

  private final UserProvider userProvider;

  @GET
  @Authenticated
  @Path("auth")
  public String testAuth() {
    val user = userProvider.getCurrentUser().getOrNull();

    return "Hello id=%s, name=%s, nickname=%s, pictureUrl=%s, email=%s"
            .formatted(user.id(), user.name(), user.nickname(), user.pictureUrl(), user.email());
  }

  @GET
  @Authenticated
  @Path("logout")
  public String postLogout() {
    return "You were logged out";
  }
}
