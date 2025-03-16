package io.github.badpop.mari.application.domain.ad.port;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.UUID;

public interface AdFinderSpi {

  Either<MariFail, Ad> findById(UUID id);

  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  Either<MariFail, Seq<Ad>> findAll();
}
