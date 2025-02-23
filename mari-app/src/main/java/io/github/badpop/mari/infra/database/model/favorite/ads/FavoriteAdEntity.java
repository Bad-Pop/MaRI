package io.github.badpop.mari.infra.database.model.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdSummary;
import io.github.badpop.mari.infra.database.model.MariEntity;
import io.github.badpop.mari.infra.database.model.favorite.ads.projection.FavoriteAdEntitySummaryProjection;
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
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@Getter
@Setter
@Entity
@NaturalIdCache
@Cache(usage = READ_ONLY)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favorite_ad",
        indexes = {@Index(name = "favorite_ad_id", columnList = "id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "FavoriteAdEntity.findAll", query = "from FavoriteAdEntity"),
        @NamedQuery(name = "FavoriteAdEntity.deleteAll", query = "delete from FavoriteAdEntity"),
})
public class FavoriteAdEntity extends MariEntity<FavoriteAdEntity, FavoriteAd> {

  @NaturalId
  @Column(nullable = false, updatable = false)
  private String id;

  @Column(nullable = false, length = 80)
  private String name;

  @Column(nullable = false)
  private String url;

  @Enumerated(STRING)
  @Column(nullable = false)
  private FavoriteAdType type;

  @Column(columnDefinition = "text")
  private String description;

  @Column(columnDefinition = "text")
  private String remarks;

  @Column(length = 200)
  private String address;

  @Column
  private Double price;

  @Column(name = "price_per_square_meter")
  private Double pricePerSquareMeter;

  public static FavoriteAdEntity fromDomain(FavoriteAd domain) {
    return new FavoriteAdEntity(
            domain.id(),
            domain.name(),
            domain.url(),
            domain.type(),
            domain.description().getOrNull(),
            domain.remarks().getOrNull(),
            domain.address().getOrNull(),
            domain.price().getOrNull(),
            domain.pricePerSquareMeter().getOrNull());
  }

  @Override
  public FavoriteAd toDomain() {
    return new FavoriteAd(
            id,
            name,
            url,
            type,
            Option(description),
            Option(remarks),
            Option(address),
            Option(price),
            Option(pricePerSquareMeter));
  }

  public static Either<MariFail, FavoriteAdEntity> findById(String id) {
    val result = Try(() -> FavoriteAdEntity.find("id", id).<FavoriteAdEntity>firstResult())
            .map(API::Option);

    if (result.isFailure()) {
      val t = result.getCause();
      val fail = new TechnicalFail("An error occurred while trying to retrieve favorite ad with id=" + id, t);
      Log.error(fail.asLog());
      return Left(fail);
    }

    return result.mapTry(maybeEntity -> maybeEntity
                    .getOrElseThrow(() -> new EntityNotFoundException("Unable to find favorite ad with id=" + id)))
            .toEither()
            .mapLeft(t -> new ResourceNotFoundFail(t.getMessage()));
  }

  public static Either<MariFail, Page<FavoriteAdSummary>> findAll(int page, int size) {
    return Try(() -> FavoriteAdEntity.<FavoriteAdEntity>find("#FavoriteAdEntity.findAll"))
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to retrieve favorite ads page %s with size of %s".formatted(page, size), t))
            .flatMap(query -> newPage(query, page, size, FavoriteAdEntitySummaryProjection.class))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  public static Either<MariFail, Page<FavoriteAdSummary>> findAllByType(FavoriteAdType type, int page, int size) {
    return Try(() -> FavoriteAdEntity.<FavoriteAdEntity>find("type", type))
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to retrieve favorite ads of type %s page %s with size of %s".formatted(type, page, size), t))
            .flatMap(query -> newPage(query, page, size, FavoriteAdEntitySummaryProjection.class))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  public static Either<MariFail, Void> deleteById(String id) {
    return findById(id)
            .peekLeft(fail -> Log.errorv("Unable to retrieve ad with id {0} for deletion. Detail : {1}", id, fail.asLog()))
            .<Either<MariFail, Void>>fold(
                    resourceNotFoundFail -> Right(null),
                    ad -> Try(() -> run(ad::delete))
                            .toEither()
                            .mapLeft(t -> new TechnicalFail("An error occurred while trying to delete favorite ad with id=" + id, t)))
            .peekLeft(fail -> Log.error(fail.asLog()));
  }

  public static Either<MariFail, Void> deleteAllAds() {
    return Try(() -> FavoriteAdEntity.delete("#FavoriteAdEntity.deleteAll"))
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("An error occurred while trying to delete all favorite ads", t))
            .peekLeft(fail -> Log.error(fail.asLog()))
            .peek(deletedCount -> Log.infov("Successfully deleted {0} favorite ads", deletedCount))
            .map(deletedCount -> null);
  }
}
