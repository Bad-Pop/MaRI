package io.github.badpop.mari.infra.database.learn;

import static io.vavr.API.Try;
import static jakarta.persistence.FetchType.LAZY;

import io.github.badpop.mari.infra.database.MariEntity;
import io.quarkus.panache.common.Parameters;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "optimal_one_to_many_child")
@NamedQueries({
  @NamedQuery(
      name = "OptimalOneToManyChildEntity.findAllByParentId",
      query = "from OptimalOneToManyChildEntity as c where c.parent.id = :parentId")
})
public class OptimalOneToManyChildEntity extends MariEntity<OptimalOneToManyChildEntity, Object> {

  @Column(columnDefinition = "varchar(50)")
  private String value;

  @ManyToOne(fetch = LAZY)
  private ParentEntity parent;

  public static Try<Seq<OptimalOneToManyChildEntity>> findAllByParentId(String parentId) {
    return Try(
        () ->
            List.ofAll(
                find(
                        "#OptimalOneToManyChildEntity.findAllByParentId",
                        Parameters.with("parentId", parentId))
                    .list()));
  }

  @Override
  public Object toDomain() {
    return null;
  }
}
