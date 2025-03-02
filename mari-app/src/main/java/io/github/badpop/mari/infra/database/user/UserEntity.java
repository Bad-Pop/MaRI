package io.github.badpop.mari.infra.database.user;

import io.github.badpop.mari.context.UserContextProvider.UserContext;
import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.model.user.User;
import io.github.badpop.mari.infra.database.MariEntity;
import io.quarkus.logging.Log;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import static io.vavr.API.Try;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@Getter
@Setter
@Entity
@NaturalIdCache
@Cache(usage = READ_ONLY)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "user",
        indexes = {@Index(name = "user_id_idx", columnList = "id")},
        uniqueConstraints = {@UniqueConstraint(name = "user_id_uc", columnNames = {"id"})})
public class UserEntity extends MariEntity<UserEntity, User> {

  @NaturalId
  @Column(nullable = false, updatable = false)
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String nickname;

  @Override
  public User toDomain() {
    return new User(id, name, nickname);
  }

  public static UserEntity fromDomain(User domain) {
    return new UserEntity(domain.id(), domain.name(), domain.nickname());
  }

  public static UserEntity fromContext(UserContext context) {
    return new UserEntity(context.id(), context.name(), context.nickname());
  }

  public static Either<MariFail, UserEntity> findByIdOrCreate(UserContext userContext) {
    val findResult =  findById(userContext.id());

    if(findResult.isLeft() || (findResult.isRight() && findResult.get().isEmpty())) {
      return fromContext(userContext).persistIfAbsent();
    } else {
      return findResult.map(Option::get);
    }
  }

  public static Either<MariFail, Option<UserEntity>> findById(String id) {
    return Try(() -> UserEntity.find("id", id).<UserEntity>firstResult())
            .map(API::Option)
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to retrieve user with id=" + id, t))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }
}
