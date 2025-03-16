package io.github.badpop.mari.application.domain.ad.model;

import io.github.badpop.mari.application.domain.address.model.Address;
import io.vavr.API;
import io.vavr.control.Option;

import java.util.UUID;

import static io.vavr.API.Option;

public record Ad(UUID id,
                 String name,
                 String url,
                 double price,
                 AdType type,
                 Option<String> description,
                 Option<String> remarks,
                 Option<Address> address) {

  public Ad withAddress(Option<Address> address) {
    return new Ad(id, name, url, price, type, description, remarks, address);
  }
}
