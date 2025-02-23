package io.github.badpop.mari;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResourceConfigurableLifecycleManager;
import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Map;
import java.util.Set;

public class WithoutPostgresTestResource implements QuarkusTestResourceConfigurableLifecycleManager<WithoutPostgres>, QuarkusTestProfile {

  @Override
  public Map<String, String> start() {
    Log.info("Disabling datasource for this test class");
    return Map.of(
            "quarkus.datasource.active", "false",
            "quarkus.hibernate-orm.active", "false",
            "quarkus.panache.active", "false");
  }

  @Override
  public void stop() {
  }

  @Override
  public Set<Class<?>> getEnabledAlternatives() {
    return Set.of(NoOpEntityManager.class);  // Remplace l'EntityManager par une implémentation vide
  }
}
