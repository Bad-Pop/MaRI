package io.github.badpop.mari.context;

import io.github.badpop.mari.domain.model.user.User;
import io.vavr.control.Option;

public interface UserContextProvider {

  Option<UserContext> getUserContext();

  record UserContext(String id, String name, String nickname, boolean authenticated) {

    public static UserContext authenticated(String id, String name, String nickname) {
      return new UserContext(id, name, nickname, true);
    }

    public User toDomain() {
      return new User(id, name, nickname);
    }
  }
}
