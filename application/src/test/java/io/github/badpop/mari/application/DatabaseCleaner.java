package io.github.badpop.mari.application;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;

import java.util.List;

@Singleton
public class DatabaseCleaner {

  @Inject
  EntityManager em;

  public void cleanAllTables() {
    Log.info("Deleting all rows from all mari tables");
    // Désactiver les contraintes pour éviter les problèmes de FK
    em.createNativeQuery("SET session_replication_role = 'replica'").executeUpdate();

    List<String> tableNames = em.getMetamodel().getEntities().stream()
            .map(this::getTableName) // Récupère le vrai nom de la table
            .toList();

    // Supprimer toutes les lignes de toutes les tables
    tableNames.forEach(table -> em.createNativeQuery("DELETE FROM " + table).executeUpdate());

    // Réactiver les contraintes
    em.createNativeQuery("SET session_replication_role = 'origin'").executeUpdate();
    Log.info("Successfully deleted all rows from all mari tables");
  }

  private String getTableName(EntityType<?> entityType) {
    Class<?> entityClass = entityType.getJavaType();
    Table tableAnnotation = entityClass.getAnnotation(Table.class);
    return (tableAnnotation != null) ? tableAnnotation.name() : entityType.getName().toLowerCase();
  }
}
