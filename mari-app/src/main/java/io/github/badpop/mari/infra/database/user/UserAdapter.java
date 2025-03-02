package io.github.badpop.mari.infra.database.user;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.model.user.User;
import io.github.badpop.mari.domain.port.spi.UserSpi;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class UserAdapter implements UserSpi {

  //TODO TEST
  @Override
  public Either<MariFail, User> createNewUser(User user) {
    return UserEntity.fromDomain(user).persistIfAbsentAsDomain();
  }

  //TODO TEST
  @Override
  public Either<MariFail, Option<User>> findUserById(String id) {
    return UserEntity.findById(id)
            .map(maybeUser -> maybeUser.map(UserEntity::toDomain));
  }
}
