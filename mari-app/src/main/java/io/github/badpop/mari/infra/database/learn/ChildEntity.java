package io.github.badpop.mari.infra.database.learn;

import static jakarta.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.github.badpop.mari.infra.database.MariEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "child")
@ToString(callSuper = true)
public class ChildEntity extends MariEntity<ChildEntity, Object> {

  @Column(columnDefinition = "varchar(50)")
  private String value;

  @MapsId
  @JsonBackReference
  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "id")
  private ParentEntity parent;

  @Override
  public Object toDomain() {
    return null;
  }
}
