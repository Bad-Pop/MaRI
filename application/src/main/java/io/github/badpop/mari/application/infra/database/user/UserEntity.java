package io.github.badpop.mari.application.infra.database.user;

import io.github.badpop.mari.application.domain.user.User;
import io.github.badpop.mari.application.infra.database.MariEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@Entity
@Getter
@Setter
@NaturalIdCache
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = READ_ONLY)
@ToString(callSuper = true)
@Table(
        name = "mari_user",//User is a reserved keyword...
        indexes = {@Index(name = "user_id_idx", columnList = "id")},
        uniqueConstraints ={
                @UniqueConstraint(name = "user_id_uc", columnNames = {"id"}),
                @UniqueConstraint(name = "user_email_uc", columnNames = {"email"})
        })
public class UserEntity extends MariEntityBase<UserEntity, User> {

  @NaturalId
  @Column(nullable = false, updatable = false)
  private String id;

  String name;
  String nickname;
  String pictureUrl;
  String email;

  @Override
  public User toDomain() {
    return new User(id, name, nickname, pictureUrl, email);
  }

  public static UserEntity fromDomain(User user) {
    return new UserEntity(user.id(), user.name(), user.nickname(), user.pictureUrl(), user.email());
  }
}
