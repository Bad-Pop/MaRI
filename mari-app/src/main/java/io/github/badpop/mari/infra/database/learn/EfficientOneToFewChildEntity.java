package io.github.badpop.mari.infra.database.learn;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.github.badpop.mari.infra.database.MariEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "efficient_one_to_few_child")
public class EfficientOneToFewChildEntity extends MariEntity<EfficientOneToFewChildEntity, Object> {

  @Column(columnDefinition = "varchar(50)")
  private String value;

  @JsonBackReference
  @ManyToOne(fetch = LAZY)
  private ParentEntity parent;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof EfficientOneToFewChildEntity)) return false;
    return getTechnicalId() != null && getTechnicalId().equals(((EfficientOneToFewChildEntity) obj).getTechnicalId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public Object toDomain() {
    return null;
  }
}
