package io.github.badpop.mari.application.infra.postgres.address;

import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.infra.postgres.MariEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "address")
public class AddressEntity extends MariEntityBase<AddressEntity, Address> {

  //Base data
  private String label;
  private String name;
  private int postCode;
  private String city;
  private String municipality;
  private String houseNumber;
  private String street;

  //Coordinates data
  private double longitude;
  private double latitude;

  //Metadata
  private String cityCode;
  private int population;
  private String context;
  private double importance;
  private String type;
  private String _type;
}
