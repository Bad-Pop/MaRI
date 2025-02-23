package io.github.badpop.mari.app.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.github.badpop.mari.domain.control.Page;
import io.vavr.API;
import io.vavr.Function1;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static io.vavr.API.Option;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@JsonAutoDetect(fieldVisibility = ANY)
@Schema(description = "A paginated result")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PageResponse<T> {

  @Schema(description = "The returned page number")
  int pageNumber;

  @Schema(description = "The returned page size")
  long pageSize;

  @Schema(description = "Is there a next page")
  boolean hasNextPage;

  @Schema(description = "The total items count")
  long totalItemsCount;

  @Schema(description = "The total pages count")
  long totalPagesCount;

  @Schema(description = "The returned page items")
  Iterable<T> items;

  public static <M, D> PageResponse<M> fromDomain(Page<D> domain, Function1<D, M> mapper) {
    return new PageResponse<>(
            domain.pageNumber(),
            domain.pageSize(),
            domain.hasNextPage(),
            domain.totalItemsCount(),
            domain.totalPagesCount(),
            Option(domain.items())
                    .getOrElse(API::Seq)
                    .map(mapper)) {
    };
  }

  public static <M, D> PageResponse<M> fromDomain(Page<D> domain, Iterable<M> items) {
    return new PageResponse<>(
            domain.pageNumber(),
            domain.pageSize(),
            domain.hasNextPage(),
            domain.totalItemsCount(),
            domain.totalPagesCount(),
            items) {
    };
  }
}
