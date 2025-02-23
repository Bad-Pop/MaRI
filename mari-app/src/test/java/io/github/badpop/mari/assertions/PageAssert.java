package io.github.badpop.mari.assertions;

import io.github.badpop.mari.domain.control.Page;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class PageAssert<T> extends AbstractAssert<PageAssert<T>, Page<T>> {

  public PageAssert(Page actual) {
    super(actual, PageAssert.class);
  }

  public static <U> PageAssert<U> assertThatPage(Page<U> actual) {
    return new PageAssert<>(actual);
  }

  public PageAssert<T> isEqualToWithItemsContainsExactly(Page<T> expected) {
    isNotNull();
    Assertions.assertThat(actual)
            .satisfies(page -> {
              Assertions.assertThat(page)
                      .usingRecursiveComparison()
                      .ignoringFields("items")
                      .isEqualTo(expected);
              containsExactly((T[]) expected.items().asJava().toArray(Object[]::new));
    });
    return this;
  }

  public PageAssert<T> isEqualToWithItemsContainsExactlyInAnyOrder(Page<T> expected) {
    isNotNull();
    Assertions.assertThat(actual)
            .satisfies(page -> {
              Assertions.assertThat(page)
                      .usingRecursiveComparison()
                      .ignoringFields("items")
                      .isEqualTo(expected);
              containsExactlyInAnyOrder((T[]) expected.items().asJava().toArray(Object[]::new));
            });
    return this;
  }

  public PageAssert<T> hasSize(int size) {
    isNotNull();
    Assertions.assertThat(actual.items()).hasSize(size);
    return this;
  }

  @SafeVarargs
  public final PageAssert<T> containsExactly(T... items) {
    isNotNull();
    Assertions.assertThat(actual.items()).containsExactly(items);
    return this;
  }

  @SafeVarargs
  public final PageAssert<T> containsExactlyInAnyOrder(T... items) {
    isNotNull();
    Assertions.assertThat(actual.items()).containsExactlyInAnyOrder(items);
    return this;
  }
}
