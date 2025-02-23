package io.github.badpop.mari.app.model.favorite.ads.response;

import io.github.badpop.mari.app.model.PageResponse;
import io.vavr.collection.Seq;

public class SummaryFavoriteAdPageResponse extends PageResponse<SummaryFavoriteAdResponse> {

  public SummaryFavoriteAdPageResponse(int pageNumber,
                                       long pageSize,
                                       boolean hasNextPage,
                                       long totalItemsCount,
                                       long totalPagesCount,
                                       Seq<SummaryFavoriteAdResponse> items) {
    super(pageNumber, pageSize, hasNextPage, totalItemsCount, totalPagesCount, items);
  }
}
