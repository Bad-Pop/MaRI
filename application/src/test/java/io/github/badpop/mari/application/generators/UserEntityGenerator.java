package io.github.badpop.mari.application.generators;

import io.github.badpop.mari.application.infra.database.user.UserEntity;

public interface UserEntityGenerator {

  static UserEntity generate(String id) {
   return new UserEntity(id, "test", "test", null, "test@test.test");
  }
}
