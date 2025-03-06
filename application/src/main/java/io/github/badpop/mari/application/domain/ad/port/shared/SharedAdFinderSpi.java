package io.github.badpop.mari.application.domain.ad.port.shared;

import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.UUID;

public interface SharedAdFinderSpi {

  Either<MariFail, SharedAd> findById(UUID id);

  Either<MariFail, Seq<SharedAd>> findAll();
}
