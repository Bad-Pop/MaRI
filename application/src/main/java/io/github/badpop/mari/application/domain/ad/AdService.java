package io.github.badpop.mari.application.domain.ad;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.port.AdApi;
import io.github.badpop.mari.application.domain.ad.port.AdCreatorSpi;
import io.github.badpop.mari.application.domain.ad.port.AdFinderSpi;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.UUID;

@RequiredArgsConstructor
public class AdService implements AdApi {

  private final AdCreatorSpi creatorSpi;
  private final AdFinderSpi finderSpi;

  @Override
  public Either<MariFail, Ad> create(AdCreation adCreation) {
    val adId = UUID.randomUUID();
    val adToCreate = adCreation.toAd(adId);
    return creatorSpi.create(adToCreate);
  }

  @Override
  public Either<MariFail, Ad> findById(UUID id) {
    return finderSpi.findById(id);
  }

  @Override
  public Either<MariFail, Seq<Ad>> findAll() {
    return finderSpi.findAll();
  }
}
