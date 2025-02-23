package io.github.badpop.mari.lib.test;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

public class PostgresTestResource implements QuarkusTestResourceConfigurableLifecycleManager<WithSharedPostgres> {

  private static final String POSTGRES_IMAGE = "postgres:14-alpine";
  private static final PostgreSQLContainer<?> CONTAINER = new PostgreSQLContainer<>(POSTGRES_IMAGE);

  private String databaseName;
  private String username;
  private String password;
  private boolean enableLiquibase;
  private String generation;

  @Override
  public void init(WithSharedPostgres annotation) {
    databaseName = annotation.databaseName();
    username = annotation.username();
    password = annotation.password();
    enableLiquibase = annotation.enableLiquibase();
    generation = annotation.generation();
  }

  @Override
  public Map<String, String> start() {
    Log.infov("Starting postgres container with database name {0}", databaseName);

    CONTAINER
            .withDatabaseName(databaseName)
            .withUsername(username)
            .withPassword(password)
            .start();
    Log.infov("Postgres container started with jdbc url {0}", CONTAINER.getJdbcUrl());
    return Map.of(
            "mari.database.kind", "postgres",
            "mari.database.url", CONTAINER.getJdbcUrl(),
            "mari.database.username", username,
            "mari.database.password", password,
            "mari.database.generation", generation,
            "mari.database.liquibase.enabled", String.valueOf(enableLiquibase)
    );
  }

  @Override
  public void stop() {
    CONTAINER.stop();
    Log.infov("Postgres container stopped");
  }
}
