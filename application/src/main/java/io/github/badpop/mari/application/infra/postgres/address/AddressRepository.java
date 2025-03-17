package io.github.badpop.mari.application.infra.postgres.address;

import io.github.badpop.mari.application.domain.address.model.Address;
import io.github.badpop.mari.application.infra.postgres.MariRepositoryBase;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AddressRepository implements MariRepositoryBase<Address, AddressEntity> {
}
