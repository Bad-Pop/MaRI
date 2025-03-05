package io.github.badpop.mari.application.infra.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;
import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.application.domain.patch.UpdateOperation;
import io.quarkus.logging.Log;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

@Singleton
@RequiredArgsConstructor
public class EntityUpdateOperationsApplier {

  private final ObjectMapper objectMapper;

  public <ENTITY extends MariEntityBase<ENTITY, ?>> Either<MariFail, ENTITY> applyUpdateOperations(ENTITY entityToUpdate,
                                                                                                   Seq<UpdateOperation> operationsToApplyOnEntity,
                                                                                                   Class<ENTITY> target) {
    val patchOperations = operationsToApplyOnEntity.map(PatchOperation::fromDomain);

    try {
      val entityNode = objectMapper.valueToTree(entityToUpdate);
      val patchNode = objectMapper.valueToTree(patchOperations);

      JsonPatch.validate(patchNode);//On valide que le patch est bien valide au sens de la spec JsonPatch
      val updatedEntityNode = JsonPatch.apply(patchNode, entityNode);
      val updatedEntity = objectMapper.treeToValue(updatedEntityNode, target);
      return Right(updatedEntity);
    } catch (Exception e) {
      Log.error("Unable to apply json patch to the given entity", e);
      return Left(new TechnicalFail("Unable to update the requested entity...", e));
    }
  }

  private record PatchOperation(String op, String path, String value) {

    public static PatchOperation fromDomain(UpdateOperation operation) {
      return new PatchOperation(operation.op(), operation.path(), operation.value());
    }
  }
}
