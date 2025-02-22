package io.github.badpop.mari.app.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.badpop.mari.app.model.PageResponse;
import io.github.badpop.mari.app.model.favorite.ads.request.FavoriteAdAdditionRequestBody;
import io.github.badpop.mari.app.model.favorite.ads.response.FavoriteAdResponse;
import io.github.badpop.mari.app.model.favorite.ads.response.SummaryFavoriteAdResponse;
import io.github.badpop.mari.app.resource.spec.FavoriteAdResourceSpec;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import io.github.badpop.mari.domain.port.api.FavoriteAdAdditionApi;
import io.github.badpop.mari.domain.port.api.FavoriteAdFinderApi;
import io.github.badpop.mari.lib.http.monitoring.input.HttpIOLogs;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.RENTAL;
import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.SALE;

@Singleton
@HttpIOLogs
@RequiredArgsConstructor
public class FavoriteAdResource implements FavoriteAdResourceSpec {

  private final FavoriteAdAdditionApi additionApi;
  private final FavoriteAdFinderApi finderApi;

  private final ObjectMapper om;

  @Override
  public Response addNewRentalFavoriteAd(String correlationId, FavoriteAdAdditionRequestBody additionRequest) {
    return addNewFavoriteAd(RENTAL, additionRequest);
  }

  @Override
  public Response addNewSaleFavoriteAd(String correlationId, FavoriteAdAdditionRequestBody additionRequest) {
    return addNewFavoriteAd(SALE, additionRequest);
  }

  @Override
  public Response getFavoriteAdById(String correlationId, String favoriteAdId) {
    return finderApi.findById(favoriteAdId)
            .map(FavoriteAdResponse::fromDomain)
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  public Response getAllRentalFavoriteAds(String correlationId, Integer page, Integer limit) {
    val pagination = PaginationValidator.validate(page, limit);
    return finderApi.findAllByRentalType(pagination.page(), pagination.limit())
            .map(ads -> PageResponse.fromDomain(ads, SummaryFavoriteAdResponse::fromDomain))
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  public Response getAllSaleFavoriteAds(String correlationId, Integer page, Integer limit) {
    val pagination = PaginationValidator.validate(page, limit);
    return finderApi.findAllBySaleType(pagination.page(), pagination.limit())
            .map(ads -> PageResponse.fromDomain(ads, SummaryFavoriteAdResponse::fromDomain))
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  public Response getAllFavoriteAds(String correlationId, Integer page, Integer limit) {
    val pagination = PaginationValidator.validate(page, limit);
    return finderApi.findAll(pagination.page(), pagination.limit())
            .map(ads -> PageResponse.fromDomain(ads, SummaryFavoriteAdResponse::fromDomain))
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  public Response deleteFavoriteAdById(String correlationId, String favoriteAdId) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public Response deleteAllFavoriteAds(String correlationId) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  private Response addNewFavoriteAd(FavoriteAdType type, FavoriteAdAdditionRequestBody additionRequest) {
    val addition = additionRequest.toDomain(type);
    return additionApi
            .createNewFavoriteAd(addition)
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }
}
