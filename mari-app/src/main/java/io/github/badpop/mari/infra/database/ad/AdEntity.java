package io.github.badpop.mari.infra.database.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.model.ad.AdType;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import io.github.badpop.mari.infra.database.MariEntity;
import io.github.badpop.mari.infra.database.ad.projection.AdEntitySummaryProjection;
import io.github.badpop.mari.infra.database.user.UserEntity;
import io.quarkus.logging.Log;
import io.vavr.API;
import io.vavr.control.Either;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import static io.vavr.API.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@Getter
@Setter
@Entity
@NaturalIdCache
@Cache(usage = READ_ONLY)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ad",
        indexes = {@Index(name = "ad_id_idx", columnList = "id")},
        uniqueConstraints = {@UniqueConstraint(name = "ad_id_uc", columnNames = {"id"})})
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "AdEntity.findAll", query = "from AdEntity"),
        @NamedQuery(name = "AdEntity.deleteAll", query = "delete from AdEntity"),
})
public class AdEntity extends MariEntity<AdEntity, Ad> {

  @NaturalId
  @Column(nullable = false, updatable = false)
  private String id;

  @ManyToOne(fetch = LAZY, optional = false)
  private UserEntity user;

  @Column(nullable = false, length = 80)
  private String name;

  @Column(nullable = false)
  private String url;

  @Enumerated(STRING)
  @Column(nullable = false)
  private AdType type;

  @Column(nullable = false)
  private Double price;

  @Column(columnDefinition = "text")
  private String description;

  @Column(columnDefinition = "text")
  private String remarks;

  @Column(length = 200)
  private String address;

  @Column(name = "price_per_square_meter")
  private Double pricePerSquareMeter;

  @Override
  public Ad toDomain() {
    return new Ad(
            id,
            name,
            url,
            type,
            price,
            Option(description),
            Option(remarks),
            Option(address),
            Option(pricePerSquareMeter));
  }

  public static AdEntity fromDomain(Ad domain, UserEntity userEntity) {
    return new AdEntity(
            domain.id(),
            userEntity,
            domain.name(),
            domain.url(),
            domain.type(),
            domain.price(),
            domain.description().getOrNull(),
            domain.remarks().getOrNull(),
            domain.address().getOrNull(),
            domain.pricePerSquareMeter().getOrNull());
  }

  public static Either<MariFail, AdEntity> findById(String id) {
    val result = Try(() -> AdEntity.find("id", id).<AdEntity>firstResult())
            .map(API::Option);

    if (result.isFailure()) {
      val t = result.getCause();
      val fail = new TechnicalFail("An error occurred while trying to retrieve ad with id=" + id, t);
      Log.error(fail.asLog());
      return Left(fail);
    }

    return result.mapTry(maybeEntity -> maybeEntity
                    .getOrElseThrow(() -> new EntityNotFoundException("Unable to find ad with id=" + id)))
            .toEither()
            .mapLeft(t -> new ResourceNotFoundFail(t.getMessage()));
  }

  public static Either<MariFail, Page<AdSummary>> findAll(int page, int size) {
    return Try(() -> AdEntity.<AdEntity>find("#AdEntity.findAll"))
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to retrieve ads page %s with size of %s".formatted(page, size), t))
            .flatMap(query -> newPage(query, page, size, AdEntitySummaryProjection.class))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  public static Either<MariFail, Page<AdSummary>> findAllByType(AdType type, int page, int size) {
    return Try(() -> AdEntity.<AdEntity>find("type", type))
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to retrieve ads of type %s page %s with size of %s".formatted(type, page, size), t))
            .flatMap(query -> newPage(query, page, size, AdEntitySummaryProjection.class))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  public static Either<MariFail, Void> deleteById(String id) {
    return findById(id)
            .peekLeft(fail -> Log.errorv("Unable to retrieve ad with id {0} for deletion. Detail : {1}", id, fail.asLog()))
            .<Either<MariFail, Void>>fold(
                    resourceNotFoundFail -> Right(null),
                    ad -> Try(() -> run(ad::delete))
                            .toEither()
                            .mapLeft(t -> new TechnicalFail("An error occurred while trying to delete ad with id=" + id, t)))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  public static Either<MariFail, Void> deleteAllAds() {
    return Try(() -> AdEntity.delete("#AdEntity.deleteAll"))
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to delete all ads", t))
            .peekLeft(fail -> Log.error(fail.asLog()))
            .peek(deletedCount -> Log.infov("Successfully deleted {0} ads", deletedCount))
            .map(deletedCount -> null);
  }
}
