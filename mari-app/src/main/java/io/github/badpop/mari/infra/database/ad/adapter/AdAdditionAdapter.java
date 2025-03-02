package io.github.badpop.mari.infra.database.ad.adapter;

import io.github.badpop.mari.context.UserContextProvider;
import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.UnauthorizedFail;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.port.spi.AdAdditionSpi;
import io.github.badpop.mari.domain.port.spi.UserSpi;
import io.github.badpop.mari.infra.database.ad.AdEntity;
import io.github.badpop.mari.infra.database.user.UserEntity;
import io.quarkus.logging.Log;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Singleton
@RequiredArgsConstructor
public class AdAdditionAdapter implements AdAdditionSpi {

  private final UserContextProvider userContextProvider;

  @Override
  @Transactional
  public Either<MariFail, Ad> createNewAd(Ad ad) {
    return userContextProvider.getUserContext()
            .<MariFail>toEither(UnauthorizedFail::new)
            .flatMap(UserEntity::findByIdOrCreate)
            .map(userEntity -> AdEntity.fromDomain(ad, userEntity))
            .flatMap(AdEntity::persistIfAbsentAsDomain);
  }
}
