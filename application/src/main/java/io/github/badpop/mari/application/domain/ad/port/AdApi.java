package io.github.badpop.mari.application.domain.ad.port;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.UUID;

public interface AdApi {

  Either<MariFail, Ad> create(AdCreation adCreation);

  Either<MariFail, Ad> findById(UUID id);

  Either<MariFail, Seq<Ad>> findAll();
}
