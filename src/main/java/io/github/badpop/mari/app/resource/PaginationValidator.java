package io.github.badpop.mari.app.resource;

import static io.vavr.API.Option;

public interface PaginationValidator {

  //TODO USE PROPERTIES ? IF SO, NEED TO TRANSFORME THIS INTERFACE TO A CLASS AND INJECT IT IN EACH RESOURCE THAT NEED PAGINATION
  Integer DEFAULT_PAGE = 0;
  Integer DEFAULT_LIMIT = 10;
  Integer MAX_LIMIT = 50;

  static Pagination validate(Integer page, Integer limit) {
    return new Pagination(
            Option(page).getOrElse(DEFAULT_PAGE),
            Option(limit)
                    .map(givenLimit -> givenLimit > MAX_LIMIT ? MAX_LIMIT : givenLimit)
                    .getOrElse(DEFAULT_LIMIT));
  }

  record Pagination(int page, int limit) {
  }
}
