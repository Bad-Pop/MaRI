package io.github.badpop.mari.application.domain.ad.module;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.ad.control.AdSharingParameters;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.ForbiddenPatchOperationFail;
import io.github.badpop.mari.application.domain.control.MariFail.InvalidRequestFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import lombok.val;

import java.net.URI;

import static io.vavr.API.*;

public interface AdValidations {

  static Validation<MariFail, Void> validateAdCreationUrl(AdCreation adCreation) {
    try {
      URI.create(adCreation.url()).toURL();
      return Valid(null);
    } catch (Exception e) {
      return Invalid(new MariFail.InvalidAdUrlFail(adCreation.url()));
    }
  }

  static Validation<MariFail, Void> validateThatUpdateOperationsDoesNotContainsForbiddenOperations(Seq<UpdateOperation> operations) {
    val forbiddenOperations = Option(operations)
            .getOrElse(API::List)
            .filter(operation -> operation.path().equalsIgnoreCase("/id") || operation.path().contains("/address"));

    if(forbiddenOperations.nonEmpty()) {
      return Invalid(new ForbiddenPatchOperationFail("id ou address"));
    }
    return Valid(null);
  }

  static Validation<MariFail, Void> validateSharingAdParameters(AdSharingParameters parameters) {
    if(parameters.expires() == false && parameters.expireAt().isDefined()) {
      return Invalid(new InvalidRequestFail("Une annonce partagée qui n'expire pas, ne peut pas avoir de date d'expiration fixée."));
    } else if (parameters.expires() == true && parameters.expireAt().isEmpty()) {
      return Invalid(new InvalidRequestFail("Une annonce partagée avec expiration explicite doit avoir un date d'expiration fixée"));
    } else {
      return Valid(null);
    }
  }
}
