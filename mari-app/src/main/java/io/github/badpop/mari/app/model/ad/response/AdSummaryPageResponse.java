package io.github.badpop.mari.app.model.ad.response;

import io.github.badpop.mari.app.model.PageResponse;
import io.vavr.collection.Seq;

public class AdSummaryPageResponse extends PageResponse<AdSummaryResponse> {

  public AdSummaryPageResponse(int pageNumber,
                               long pageSize,
                               boolean hasNextPage,
                               long totalItemsCount,
                               long totalPagesCount,
                               Seq<AdSummaryResponse> items) {
    super(pageNumber, pageSize, hasNextPage, totalItemsCount, totalPagesCount, items);
  }
}
