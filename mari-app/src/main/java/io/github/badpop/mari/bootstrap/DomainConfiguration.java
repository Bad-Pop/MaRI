package io.github.badpop.mari.bootstrap;

import io.github.badpop.mari.domain.port.api.ad.AdAdditionApi;
import io.github.badpop.mari.domain.port.api.ad.AdDeleterApi;
import io.github.badpop.mari.domain.port.api.ad.AdFinderApi;
import io.github.badpop.mari.domain.port.api.home.loan.HomeLoanCalculatorApi;
import io.github.badpop.mari.domain.port.spi.AdAdditionSpi;
import io.github.badpop.mari.domain.port.spi.AdDeleterSpi;
import io.github.badpop.mari.domain.port.spi.AdFinderSpi;
import io.github.badpop.mari.domain.service.ad.AdAdditionService;
import io.github.badpop.mari.domain.service.ad.AdDeleterService;
import io.github.badpop.mari.domain.service.ad.AdFinderService;
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
  public AdAdditionApi adAdditionApi(AdAdditionSpi adAdditionSpi) {
    return new AdAdditionService(adAdditionSpi);
  }

  @Produces
  @Singleton
  public AdFinderApi adFinderApi(AdFinderSpi adFinderSpi) {
    return new AdFinderService(adFinderSpi);
  }

  @Produces
  @Singleton
  public AdDeleterApi adDeleterApi(AdDeleterSpi adDeleterSpi) {
    return new AdDeleterService(adDeleterSpi);
  }
}
