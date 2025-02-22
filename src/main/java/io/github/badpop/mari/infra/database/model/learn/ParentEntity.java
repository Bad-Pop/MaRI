package io.github.badpop.mari.infra.database.model.learn;

import static jakarta.persistence.CascadeType.ALL;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.badpop.mari.infra.database.model.MariEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parent")
@ToString(callSuper = true)
public class ParentEntity extends MariEntity<ParentEntity, Object> {

  @Column(columnDefinition = "varchar(50)")
  private String value;

  @JsonManagedReference
  @OneToOne(mappedBy = "parent", cascade = ALL, orphanRemoval = true)
  private ChildEntity optionalChild;

  @JsonManagedReference
  @OneToMany(mappedBy = "parent", cascade = ALL, orphanRemoval = true)
  private final List<EfficientOneToFewChildEntity> efficientChildren = new ArrayList<>();

  public ParentEntity addEfficientChild(EfficientOneToFewChildEntity child) {
    efficientChildren.add(child);
    child.setParent(this);
    return this;
  }

  public ParentEntity removeEfficientChild(EfficientOneToFewChildEntity child) {
    efficientChildren.remove(child);
    child.setParent(null);
    return this;
  }

  @Override
  public Object toDomain() {
    return null;
  }
}
