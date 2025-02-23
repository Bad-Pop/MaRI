package io.github.badpop.mari;

import io.quarkus.test.common.QuarkusTestResource;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation permettant de démarrer un conteneur postgres pour les tests.
 * <p>
 * <b>ATTENTION : le conteneur est partagé avec toutes les classes de tests annotées avec cette annotation pour des raisons de rapidité d'exécution des tests.
 * Pour éviter des conflits dans les tests, chaque test doit s'assurer de supprimer les données insérées en base.</b>
 */
@Target(TYPE)
@Retention(RUNTIME)
@QuarkusTestResource(value = PostgresTestResource.class)
public @interface WithSharedPostgres {

  String databaseName() default "mari_test";

  String username() default "mari_test";

  String password() default "password";

  boolean enableLiquibase() default false;

  String generation() default "drop-and-create";
}
