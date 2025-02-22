package io.github.badpop.mari.domain.control;

import io.vavr.collection.Seq;

public record Page<T>(int pageNumber,
                      long pageSize,
                      boolean hasNextPage,
                      long totalItemsCount,
                      long totalPagesCount,
                      Seq<T> items) {
}
