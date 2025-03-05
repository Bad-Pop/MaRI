package io.github.badpop.mari.application;

import io.github.badpop.mari.application.domain.ad.AdService;
import io.github.badpop.mari.application.domain.ad.port.AdApi;
import io.github.badpop.mari.application.domain.ad.port.AdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.AdFinderSpi;
import io.github.badpop.mari.application.domain.ad.port.AdUpdaterSpi;
import io.github.badpop.mari.application.domain.home.loan.HomeLoanCalculatorService;
import io.github.badpop.mari.application.domain.home.loan.port.HomeLoanCalculatorApi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class DomainConfig {

  @Produces
  @Singleton
  public AdApi adApi(AdCreatorSpi creatorSpi,
                     AdFinderSpi finderSpi,
                     AdUpdaterSpi updaterSpi) {
    return new AdService(creatorSpi, finderSpi, updaterSpi);
  }

  @Produces
  @Singleton
  public HomeLoanCalculatorApi homeLoanCalculatorApi() {
    return new HomeLoanCalculatorService();
  }
}
