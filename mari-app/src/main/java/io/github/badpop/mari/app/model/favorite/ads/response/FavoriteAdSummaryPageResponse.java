package io.github.badpop.mari.app.model.favorite.ads.response;

import io.github.badpop.mari.app.model.PageResponse;
import io.vavr.collection.Seq;

public class FavoriteAdSummaryPageResponse extends PageResponse<FavoriteAdSummaryResponse> {

  public FavoriteAdSummaryPageResponse(int pageNumber,
                                       long pageSize,
                                       boolean hasNextPage,
                                       long totalItemsCount,
                                       long totalPagesCount,
                                       Seq<FavoriteAdSummaryResponse> items) {
    super(pageNumber, pageSize, hasNextPage, totalItemsCount, totalPagesCount, items);
  }
}
