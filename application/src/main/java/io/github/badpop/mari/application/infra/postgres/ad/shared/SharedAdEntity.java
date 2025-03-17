package io.github.badpop.mari.application.infra.postgres.ad.shared;

import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.infra.postgres.MariEntityBase;
import io.github.badpop.mari.application.infra.postgres.ad.AdEntity;
import io.github.badpop.mari.application.infra.postgres.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Getter
@Setter
@NaturalIdCache
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = READ_WRITE)
@ToString(callSuper = true)
@Table(
        name = "shared_ad",
        indexes = {@Index(name = "shared_ad_id_idx", columnList = "id")},
        uniqueConstraints = {@UniqueConstraint(name = "shared_ad_id_uc", columnNames = {"id"})})
@NamedQueries({
        @NamedQuery(name = "SharedAdEntity.findByIdAndUser",
                query = """
                        select sharedAd from SharedAdEntity sharedAd
                        join fetch sharedAd.user user
                        where sharedAd.id = :sharedAdId
                        and user.id = :userId
                        """),
        @NamedQuery(name = "SharedAdEntity.findAllByUser",
                query = """
                        select sharedAd from SharedAdEntity sharedAd
                        join fetch sharedAd.user user
                        where user.id = :userId
                        """)
})
public class SharedAdEntity extends MariEntityBase<SharedAdEntity, SharedAd> {

  @NaturalId
  @Column(nullable = false, updatable = false)
  private UUID id;

  @ManyToOne(fetch = LAZY)
  //On utilise l'id fonctionnel pour la jointure, pour éviter un appel supplémentaire en base
  @JoinColumn(name = "ad_id", referencedColumnName = "id")
  private AdEntity ad;

  @ManyToOne(fetch = LAZY)
  //On utilise l'id fonctionnel pour la jointure, pour éviter un appel supplémentaire en base
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserEntity user;

  private boolean expires;

  private LocalDateTime expireAt;
}
