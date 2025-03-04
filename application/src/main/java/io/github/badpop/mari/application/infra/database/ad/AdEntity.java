package io.github.badpop.mari.application.infra.database.ad;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.model.AdType;
import io.github.badpop.mari.application.infra.database.MariEntityBase;
import io.github.badpop.mari.application.infra.database.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.util.UUID;

import static io.vavr.API.Option;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
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
        name = "ad",
        indexes = {@Index(name = "ad_id_idx", columnList = "id")},
        uniqueConstraints = {@UniqueConstraint(name = "ad_id_uc", columnNames = {"id"})})
@NamedQueries({
        @NamedQuery(name = "AdEntity.findByIdAndUser",
                query = """
                        select ad from AdEntity ad
                        join fetch ad.user u
                        where ad.id = :adId
                        and u.id = :userId
                        """),
        @NamedQuery(name = "AdEntity.findAllByUser",
                query = """
                        select ad from AdEntity ad
                        join fetch ad.user u
                        where u.id = :userId
                        """)})
public class AdEntity extends MariEntityBase<AdEntity, Ad> {

  @NaturalId
  @Column(nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = LAZY)
  //On utilise l'id fonctionnel pour la jointure, pour éviter un appel supplémentaire en base
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private double price;

  @Enumerated(STRING)
  @Column(nullable = false)
  private AdType type;

  @Column(columnDefinition = "text")
  private String description;

  @Column(columnDefinition = "text")
  private String remarks;

  private String address;

  @Override
  public Ad toDomain() {
    return new Ad(id, name, url, price, type, Option(description), Option(remarks), Option(address));
  }

  public static AdEntity fromDomain(Ad ad, UserEntity owner) {
    return new AdEntity(
            ad.id(),
            owner,
            ad.name(),
            ad.url(),
            ad.price(),
            ad.type(),
            ad.description().getOrNull(),
            ad.remarks().getOrNull(),
            ad.address().getOrNull());
  }
}
