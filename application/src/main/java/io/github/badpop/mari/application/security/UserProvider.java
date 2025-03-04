package io.github.badpop.mari.application.security;

import io.github.badpop.mari.application.domain.user.User;
import io.vavr.control.Option;

public interface UserProvider {

  Option<User> getCurrentUser();
}
