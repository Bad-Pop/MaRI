package io.github.badpop.mari.bootstrap;

import io.github.badpop.mari.domain.port.api.FavoriteAdAdditionApi;
import io.github.badpop.mari.domain.port.api.FavoriteAdFinderApi;
import io.github.badpop.mari.domain.port.api.HomeLoanCalculatorApi;
import io.github.badpop.mari.domain.port.spi.FavoriteAdAdditionSpi;
import io.github.badpop.mari.domain.port.spi.FavoriteAdFinderSpi;
import io.github.badpop.mari.domain.service.favorite.ads.FavoriteAdAdditionService;
import io.github.badpop.mari.domain.service.favorite.ads.FavoriteAdFinderService;
import io.github.badpop.mari.domain.service.home.loan.HomeLoanCalculatorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class DomainConfiguration {

  @Produces
  @Singleton
  public HomeLoanCalculatorApi homeLoanCalculatorApi() {
    return new HomeLoanCalculatorService();
  }

  @Produces
  @Singleton
  public FavoriteAdAdditionApi favoriteAdAdditionApi(FavoriteAdAdditionSpi favoriteAdAdditionSpi) {
    return new FavoriteAdAdditionService(favoriteAdAdditionSpi);
  }

  @Singleton
  @Produces
  public FavoriteAdFinderApi favoriteAdFinderApi(FavoriteAdFinderSpi favoriteAdFinderSpi) {
    return new FavoriteAdFinderService(favoriteAdFinderSpi);
  }
}
