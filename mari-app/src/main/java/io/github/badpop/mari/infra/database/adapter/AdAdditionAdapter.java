package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.port.spi.AdAdditionSpi;
import io.github.badpop.mari.infra.database.model.ad.AdEntity;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AdAdditionAdapter implements AdAdditionSpi {

  @Override
  @Transactional
  public Either<MariFail, Ad> createNewAd(Ad ad) {
    return AdEntity.fromDomain(ad).persistIfAbsent();
  }
}
