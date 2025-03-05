package io.github.badpop.mari.application.infra.database.user;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.application.domain.control.MariFail.UnauthorizedFail;
import io.github.badpop.mari.application.domain.user.User;
import io.github.badpop.mari.application.security.UserProvider;
import io.vavr.API;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Singleton
@RequiredArgsConstructor
public class CurrentUserEntityProvider {

  private final UserProvider userProvider;
  private final UserRepository repository;

  public Either<MariFail, UserEntity> withCurrentUserEntity() {
    return userProvider.getCurrentUser()
            .<MariFail>toEither(UnauthorizedFail::new)
            .flatMap(this::retrieveUserEntity);
  }

  private Either<MariFail, UserEntity> retrieveUserEntity(User currentUser) {
    return repository.findById(currentUser.id())
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to retrieve user by id=" + currentUser.id(), t))
            .flatMap(maybeUser -> maybeUser.fold(() -> createMissingUserEntity(currentUser), API::Right));
  }

  @Transactional//Si pas de transaction, on en crée une pour l'insertion
  protected Either<MariFail, UserEntity> createMissingUserEntity(User currentUser) {
    val userEntity = UserEntity.fromDomain(currentUser);
    return repository.persistIfAbsent(userEntity)
            .toEither()
            .mapLeft(t -> new TechnicalFail("An error occurred. Unable to create user with id=" + currentUser.id(), t));
  }
}
