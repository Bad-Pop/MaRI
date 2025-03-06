package io.github.badpop.mari.application.domain.ad;

import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdApi;
import io.github.badpop.mari.application.domain.ad.port.shared.SharedAdFinderSpi;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.ExpiredSharedAdFail;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static io.vavr.control.Option.when;

@RequiredArgsConstructor
public class SharedAdService implements SharedAdApi {

  private final SharedAdFinderSpi finderSpi;

  @Override
  public Either<MariFail, SharedAd> findById(UUID id) {
    return finderSpi.findById(id).flatMap(this::checkExpiration);
  }

  private Either<MariFail, SharedAd> checkExpiration(SharedAd sharedAd) {
    return when(sharedAd.isNotExpired(), sharedAd).toEither(ExpiredSharedAdFail::new);
  }
}
