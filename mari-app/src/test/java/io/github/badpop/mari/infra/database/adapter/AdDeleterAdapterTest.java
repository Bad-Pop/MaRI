package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.infra.database.ad.AdEntity;
import io.github.badpop.mari.infra.database.ad.adapter.AdDeleterAdapter;
import io.github.badpop.mari.lib.test.WithSharedPostgres;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.ad.AdType.RENTAL;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@WithSharedPostgres
class AdDeleterAdapterTest {

  private static final String ID_ONE = "one";
  private static final String ID_TWO = "two";

  @Inject
  AdDeleterAdapter adapter;

  @BeforeEach
  @Transactional
  void setUp() {
    new AdEntity(
            ID_ONE,
            "My first ad",
            "http://mari.fr/ads/123456789",
            RENTAL,
            140000.0,
            "description",
            null,
            null,
            null).persist();
    new AdEntity(
            ID_TWO,
            "My second ad",
            "http://mari.fr/ads/987654321",
            RENTAL,
            130000.0,
            "description",
            null,
            null,
            null).persist();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    try {
      AdEntity.deleteAll();
    } catch (Exception e) {
    }
  }

  @Test
  void should_delete_ad_by_id() {
    val actual = adapter.deleteById(ID_ONE);

    VavrAssertions.assertThat(actual).isRight();
    assertThat(AdEntity.findAll().list()).hasSize(1);
    assertThat(AdEntity.find("id", ID_ONE).<AdEntity>firstResult()).isNull();
    assertThat(AdEntity.find("id", ID_TWO).<AdEntity>firstResult()).isNotNull();
  }

  @Test
  void should_delete_all_ads() {
    val actual = adapter.deleteAll();

    VavrAssertions.assertThat(actual).isRight();
    assertThat(AdEntity.findAll().list()).isEmpty();
  }
}
