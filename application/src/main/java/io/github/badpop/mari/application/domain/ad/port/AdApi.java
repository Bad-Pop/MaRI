package io.github.badpop.mari.application.domain.ad.port;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.control.AdSharingParameters;
import io.github.badpop.mari.application.domain.ad.model.Ad;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAd;
import io.github.badpop.mari.application.domain.ad.model.shared.SharedAdCreated;
import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.util.UUID;

public interface AdApi {

  Either<MariFail, Ad> create(AdCreation adCreation);

  Either<MariFail, Ad> findById(UUID id);

  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  Either<MariFail, Seq<Ad>> findAll();

  Either<MariFail, Ad> updateAdById(UUID id, Seq<UpdateOperation> operations);

  Either<MariFail, Ad> updateAdAddressById(UUID id, Address address);

  Either<MariFail, Void> deleteAdById(UUID id);

  Either<MariFail, SharedAdCreated> shareAdById(UUID id, AdSharingParameters parameters);

  //TODO REFACTOR : USE PAGINATION WITH PROJECTION
  Either<MariFail, Seq<SharedAd>> findAllSharedAds();

  Either<MariFail, Void> deleteSharedAdById(UUID id);
}
