package io.github.badpop.mari.infra.database;

/**
 * WARNING : projection classes must not have an empty constructor
 */
public interface MariProjection<D> {

  D toDomain();
}
