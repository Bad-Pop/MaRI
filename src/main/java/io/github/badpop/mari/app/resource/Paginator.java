package io.github.badpop.mari.app.resource;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static io.vavr.API.Option;

@Singleton
public class Paginator {

  @ConfigProperty(name = "mari.pagination.default-page", defaultValue = "0")
  private int defaultPage;

  @ConfigProperty(name = "mari.pagination.default-limit", defaultValue = "10")
  private int defaultLimit;

  @ConfigProperty(name = "mari.pagination.max-limit", defaultValue = "50")
  private int maxLimit;

  public Pagination validate(Integer page, Integer limit) {
    return new Pagination(
            Option(page)
                    .filter(givenPage -> givenPage >= 0)
                    .getOrElse(defaultPage),
            Option(limit)
                    .filter(givenLimit -> givenLimit > 0)
                    .map(givenLimit -> givenLimit > maxLimit ? maxLimit : givenLimit)
                    .getOrElse(defaultLimit));
  }

  public record Pagination(int page, int limit) {
  }
}
