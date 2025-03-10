package io.github.badpop.mari.application.infra.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class MariEntityBase<ENTITY, DOMAIN> {

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID technicalId;

  public abstract DOMAIN toDomain();

  @Override
  public String toString() {
    return """
            MariEntity{
              technicalId=%s
            }
            """.formatted(technicalId);
  }
}
