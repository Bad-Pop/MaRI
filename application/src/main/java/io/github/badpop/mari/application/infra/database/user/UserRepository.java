package io.github.badpop.mari.application.infra.database.user;

import io.github.badpop.mari.application.domain.user.User;
import io.github.badpop.mari.application.infra.database.MariRepositoryBase;
import io.vavr.API;
import io.vavr.control.Option;
import io.vavr.control.Try;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import static io.vavr.API.Try;

@Singleton
@RequiredArgsConstructor
public class UserRepository implements MariRepositoryBase<User, UserEntity> {

  public Try<Option<UserEntity>> findById(String id) {
    return Try(() -> find("id", id).<UserEntity>firstResult()).map(API::Option);
  }
}
