package io.github.badpop.mari.infra.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.NoResourceFoundFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.control.Page;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.vavr.API;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.persistence.*;
import lombok.*;
import org.jboss.logging.Logger;

import java.util.UUID;

import static io.vavr.API.*;

/**
 * @param <E> Le type de l'entité
 * @param <D> La classe associée du domaine
 */
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class MariEntity<E, D> extends PanacheEntityBase {

  @Transient
  @JsonIgnore
  private final Logger LOG = Logger.getLogger(getClass());

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID technicalId;

  @Override
  public String toString() {
    return getClass().getSimpleName() + "<" + technicalId.toString() + ">";
  }

  /**
   * Méthode permettant de savoir si l'entité est déjà persité ou non en base de données.
   *
   * @return true si absent de la base, sinon false
   */
  public boolean isAbsent() {
    return !isPersistent();
  }

  /**
   * Persiste l'entité en base de données si elle est absente, sinon ne fait rien.
   *
   * @return l'entité mappé vers son équivalent dans le domaine
   */
  public Either<MariFail, D> persistIfAbsent() {
    return Try(() -> {
      if (isAbsent()) persist();
      return toDomain();
    })
            .toEither()
            .<MariFail>mapLeft(t -> new TechnicalFail("Error while trying to persist entity of type " + entityName(), t))
            .peekLeft(fail -> LOG.error(fail.asLog()));
  }

  /**
   * Méthode permettant de récupérer en base de données les entités pour une page donnée et de les mapper vers la projection du domaine
   * tout en gérant les cas standards
   */
  public static <D, E extends MariEntity<E, D>, S, P extends MariProjection<S>> Either<MariFail, Page<S>> newPage(PanacheQuery<E> query,
                                                                                                               int page,
                                                                                                               int size,
                                                                                                               Class<P> projectionTarget) {
    val result = buildPage(query, page, size, MariProjection::toDomain, projectionTarget);

    if (result.isFailure()) {
      return Left(new TechnicalFail("An error occurred while trying to retrieve paginated resources", result.getCause()));
    }

    val pageResult = result.get();

    if (pageResult.items().isEmpty()) {
      return Left(new NoResourceFoundFail("No paginated resources found for the given parameters"));
    } else {
      return Right(pageResult);
    }
  }

  /**
   * Méthode permettant de récupérer en base de données les entités pour une page donnée et de les mapper vers la projection du domaine
   */
  public static <D, E extends MariEntity<E, D>, S, P extends MariProjection<S>> Try<Page<S>> buildPage(PanacheQuery<E> query,
                                                                       int page,
                                                                       int size,
                                                                       Function1<P, S> projectionToDomainMapper,
                                                                       Class<P> projectionTarget) {
    try {
      val paginatedQuery = query.page(page, size).project(projectionTarget);

      val entityPage = paginatedQuery.page();
      val pageNumber = entityPage.index;
      val pageSize = entityPage.size;
      Seq<P> items = Option(paginatedQuery.list())
              .map(List::ofAll)
              .getOrElse(API::List);
      val totalItems = paginatedQuery.count();
      val totalPageCount = paginatedQuery.pageCount();
      val hasNextPage = pageNumber < totalPageCount - 1;

      return Success(new Page<>(pageNumber, pageSize, hasNextPage, totalItems, totalPageCount, items.map(projectionToDomainMapper)));
    } catch (Exception e) {
      return Failure(e);
    }
  }

  private String entityName() {
    return getClass().getSimpleName();
  }

  public abstract D toDomain();
}
