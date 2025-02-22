package io.github.badpop.mari.infra.database.model;

/**
 * WARNING : projection classes must not have an empty constructor
 */
public interface MariProjection<D> {

  D toDomain();
}
