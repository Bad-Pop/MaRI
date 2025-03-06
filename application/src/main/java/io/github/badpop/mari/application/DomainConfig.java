package io.github.badpop.mari.application;

import io.github.badpop.mari.application.domain.ad.AdService;
import io.github.badpop.mari.application.domain.ad.SharedAdService;
import io.github.badpop.mari.application.domain.ad.port.*;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdApi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdDeleterSpi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdFinderSpi;
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
                     AdUpdaterSpi updaterSpi,
                     AdDeleterSpi deleterSpi,
                     SharedAdCreatorSpi sharedAdCreatorSpi,
                     SharedAdFinderSpi sharedAdFinderSpi,
                     SharedAdDeleterSpi sharedAdDeleterSpi) {
    return new AdService(creatorSpi,
            finderSpi,
            updaterSpi,
            deleterSpi,
            sharedAdCreatorSpi,
            sharedAdFinderSpi,
            sharedAdDeleterSpi);
  }

  @Produces
  @Singleton
  public SharedAdApi sharedAdApi(SharedAdFinderSpi finderSpi) {
    return new SharedAdService(finderSpi);
  }

  @Produces
  @Singleton
  public HomeLoanCalculatorApi homeLoanCalculatorApi() {
    return new HomeLoanCalculatorService();
  }
}
