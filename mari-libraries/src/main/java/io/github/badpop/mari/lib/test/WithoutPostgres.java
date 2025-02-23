package io.github.badpop.mari.lib.test;

import io.quarkus.test.common.QuarkusTestResource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation permettant de désactiver la module quarkus pour les tests ne nécessitant pas de base de données.
 */
@Target(TYPE)
@Retention(RUNTIME)
@QuarkusTestResource(value = WithoutPostgresTestResource.class)
public @interface WithoutPostgres {
}
