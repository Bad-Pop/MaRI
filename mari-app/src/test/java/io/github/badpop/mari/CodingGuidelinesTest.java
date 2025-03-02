package io.github.badpop.mari;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "io.github.badpop.mari",
        importOptions = {DoNotIncludeTests.class})
public class CodingGuidelinesTest {

  private static final String DOMAIN_PACKAGE = "io.github.badpop.mari.domain..";
  private static final String DOMAIN_MODEL_PACKAGE = "io.github.badpop.mari.domain.model..";
  private static final String DOMAIN_SERVICE_PACKAGE = "io.github.badpop.mari.domain.service..";
  private static final String DOMAIN_VALIDATION_PACKAGE = "io.github.badpop.mari.domain.validation..";
  private static final String DOMAIN_CONTROL_PACKAGE = "io.github.badpop.mari.domain.control..";
  private static final String DOMAIN_PORT_API_PACKAGE = "io.github.badpop.mari.domain.port.api..";
  private static final String DOMAIN_PORT_SPI_PACKAGE = "io.github.badpop.mari.domain.port.spi..";

  private static final String LIB_PACKAGE = "io.github.badpop.mari.lib..";

  private static final String APP_PACKAGE = "io.github.badpop.mari.app..";
  private static final String INFRA_PACKAGE = "io.github.badpop.mari.infra..";
  private static final String BOOTSTRAP_PACKAGE = "io.github.badpop.mari.bootstrap..";

  private static final String[] ALLOWED_DOMAIN_PACKAGES = {
          DOMAIN_PACKAGE,
          "io.github.badpop.mari.context..",
          "io.github.badpop.mari.lib.failures..",
          "io.vavr..",
          "lombok..",
          "io.quarkus.logging..",
          "java.."
  };

  private static final String[] FORBIDDEN_APP_PACKAGES = {
          DOMAIN_SERVICE_PACKAGE, DOMAIN_VALIDATION_PACKAGE, DOMAIN_PORT_SPI_PACKAGE, INFRA_PACKAGE, BOOTSTRAP_PACKAGE};

  private static final String[] FORBIDDEN_INFRA_PACKAGES = {
          DOMAIN_SERVICE_PACKAGE, DOMAIN_VALIDATION_PACKAGE, DOMAIN_PORT_API_PACKAGE, APP_PACKAGE, BOOTSTRAP_PACKAGE};

  @ArchTest
  public static final ArchRule domain_should_be_isolated = classes()
          .that()
          .resideInAPackage(DOMAIN_PACKAGE)
          .should()
          .onlyDependOnClassesThat()
          .resideInAnyPackage(ALLOWED_DOMAIN_PACKAGES)
          .andShould()
          .onlyAccessClassesThat()
          .resideInAnyPackage(ALLOWED_DOMAIN_PACKAGES);

  @ArchTest
  public static final ArchRule app_should_only_access_domain_api_and_control_packages = noClasses()
          .that()
          .resideInAPackage(APP_PACKAGE)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(FORBIDDEN_APP_PACKAGES)
          .andShould()
          .accessClassesThat()
          .resideInAnyPackage(FORBIDDEN_APP_PACKAGES);

  @ArchTest
  public static final ArchRule infra_should_only_access_domain_spi_and_control_packages = noClasses()
          .that()
          .resideInAPackage(INFRA_PACKAGE)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(FORBIDDEN_INFRA_PACKAGES)
          .andShould()
          .accessClassesThat()
          .resideInAnyPackage(FORBIDDEN_INFRA_PACKAGES);

  @ArchTest
  public static final ArchRule layered = layeredArchitecture()
          .consideringOnlyDependenciesInLayers()
          .layer("DOMAIN").definedBy(DOMAIN_PACKAGE)
          .layer("APP").definedBy(APP_PACKAGE)
          .layer("INFRA").definedBy(INFRA_PACKAGE)
          .layer("BOOTSTRAP").definedBy(BOOTSTRAP_PACKAGE)
          .layer("LIB").definedBy(LIB_PACKAGE)
          .whereLayer("BOOTSTRAP").mayNotBeAccessedByAnyLayer()
          .whereLayer("APP").mayOnlyBeAccessedByLayers("BOOTSTRAP")
          .whereLayer("INFRA").mayOnlyBeAccessedByLayers("BOOTSTRAP")
          .whereLayer("DOMAIN").mayOnlyAccessLayers("LIB")
          .whereLayer("DOMAIN").mayOnlyBeAccessedByLayers("INFRA", "APP", "BOOTSTRAP")
          .whereLayer("LIB").mayOnlyBeAccessedByLayers("DOMAIN", "INFRA", "APP", "BOOTSTRAP")
          .whereLayer("LIB").mayNotAccessAnyLayer();
}
