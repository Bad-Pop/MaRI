package io.github.badpop.mari.domain.port.spi;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.model.user.User;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface UserSpi {

  Either<MariFail, User> createNewUser(User user);

  Either<MariFail, Option<User>> findUserById(String id);
}
