package io.github.badpop.mari.app.resource.ad;

import io.github.badpop.mari.app.model.PageResponse;
import io.github.badpop.mari.app.model.ad.request.AdAdditionRequestBody;
import io.github.badpop.mari.app.model.ad.response.AdResponse;
import io.github.badpop.mari.app.model.ad.response.AdSummaryResponse;
import io.github.badpop.mari.app.resource.Paginator;
import io.github.badpop.mari.app.resource.ResponseBuilder;
import io.github.badpop.mari.context.UserContextProvider;
import io.github.badpop.mari.domain.model.ad.AdType;
import io.github.badpop.mari.domain.port.api.ad.AdAdditionApi;
import io.github.badpop.mari.domain.port.api.ad.AdDeleterApi;
import io.github.badpop.mari.domain.port.api.ad.AdFinderApi;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.eclipse.microprofile.jwt.JsonWebToken;

import static io.github.badpop.mari.domain.model.ad.AdType.RENTAL;
import static io.github.badpop.mari.domain.model.ad.AdType.SALE;

@Singleton
@RequiredArgsConstructor
public class AdResource implements AdResourceSpec {

  private final Paginator paginator;
  private final AdAdditionApi additionApi;
  private final AdFinderApi finderApi;
  private final AdDeleterApi deleterApi;

  @Inject
  //@IdToken
  JsonWebToken idToken;

  @Inject
  UserContextProvider userContextProvider;

  @GET
  @Authenticated
  @Path("test-auth")
  public String testAuth() {
    val userContext = userContextProvider.getUserContext().getOrNull();
    return "Hello id=" + userContext.id() + " real_name=" + userContext.name() + " nickname=" + userContext.nickname();
  }

  @GET
  @Authenticated
  @Path("post-logout")
  public String postLogout() {
    return "You were logged out";
  }

  @Override
  @Authenticated
  public Response addNewRentalAd(String correlationId, AdAdditionRequestBody additionRequest) {
    return addNewAd(RENTAL, additionRequest);
  }

  @Override
  @Authenticated
  public Response addNewSaleAd(String correlationId, AdAdditionRequestBody additionRequest) {
    return addNewAd(SALE, additionRequest);
  }

  @Override
  @Authenticated
  public Response getAdById(String correlationId, String adId) {
    return finderApi.findById(adId)
            .map(AdResponse::fromDomain)
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  @Authenticated
  public Response getAllRentalAds(String correlationId, Integer page, Integer limit) {
    val pagination = paginator.validate(page, limit);
    return finderApi.findAllByRentalType(pagination.page(), pagination.limit())
            .map(ads -> PageResponse.fromDomain(ads, AdSummaryResponse::fromDomain))
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  @Authenticated
  public Response getAllSaleAds(String correlationId, Integer page, Integer limit) {
    val pagination = paginator.validate(page, limit);
    return finderApi.findAllBySaleType(pagination.page(), pagination.limit())
            .map(ads -> PageResponse.fromDomain(ads, AdSummaryResponse::fromDomain))
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  @Authenticated
  public Response getAllAds(String correlationId, Integer page, Integer limit) {
    val pagination = paginator.validate(page, limit);
    return finderApi.findAll(pagination.page(), pagination.limit())
            .map(ads -> PageResponse.fromDomain(ads, AdSummaryResponse::fromDomain))
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }

  @Override
  @Authenticated
  public Response deleteAdById(String correlationId, String adId) {
    return deleterApi.deleteById(adId)
            .fold(ResponseBuilder::fromFail, ResponseBuilder::accepted);
  }

  @Override
  @Authenticated
  public Response deleteAllAds(String correlationId) {
    return deleterApi.deleteAll().fold(ResponseBuilder::fromFail, ResponseBuilder::accepted);
  }

  private Response addNewAd(AdType type, AdAdditionRequestBody additionRequest) {
    val addition = additionRequest.toDomain(type);
    return additionApi
            .createNewAd(addition)
            .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
  }
}
