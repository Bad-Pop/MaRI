package io.github.badpop.mari.app.resource;

import io.github.badpop.mari.app.resource.Paginator.Pagination;
import io.github.badpop.mari.lib.test.WithoutPostgres;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@WithoutPostgres
class PaginatorTest {

  @Inject
  Paginator paginator;

  @ParameterizedTest
  @MethodSource("provideParams")
  void should_validate_pagination(int page, int limit, Pagination expected) {
    val actual = paginator.validate(page, limit);
    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> provideParams() {
    return Stream.of(
            Arguments.of(0, 1, new Pagination(0, 1)),
            Arguments.of(0, 50, new Pagination(0, 50)),
            Arguments.of(-1, 15, new Pagination(0, 15)),
            Arguments.of(0, -1, new Pagination(0, 10)),
            Arguments.of(-1, -1, new Pagination(0, 10)),
            Arguments.of(1, 51, new Pagination(1, 50)));
  }
}
