package io.github.badpop.mari.application;

import io.github.badpop.mari.application.domain.ad.AdService;
import io.github.badpop.mari.application.domain.ad.port.AdApi;
import io.github.badpop.mari.application.domain.ad.port.AdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.AdFinderSpi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class DomainConfig {

  @Produces
  @Singleton
  public AdApi adApi(AdCreatorSpi creatorSpi, AdFinderSpi finderSpi) {
    return new AdService(creatorSpi, finderSpi);
  }
}
