package io.github.badpop.mari.application.domain.ad.port;

import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.UUID;

public interface AdUpdaterSpi {

  Either<MariFail, Ad> updateAdById(UUID id, Seq<UpdateOperation> operations);

  Either<MariFail, Ad> updateAdAddressById(UUID id, Address address);
}
