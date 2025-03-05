package io.github.badpop.mari.application.domain.ad.module;

import io.github.badpop.mari.application.domain.ad.control.AdCreation;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.ForbiddenPatchOperationFail;
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
            .filter(operation -> operation.path().equalsIgnoreCase("/id"));

    if(forbiddenOperations.nonEmpty()) {
      return Invalid(new ForbiddenPatchOperationFail("id"));
    }
    return Valid(null);
  }
}
