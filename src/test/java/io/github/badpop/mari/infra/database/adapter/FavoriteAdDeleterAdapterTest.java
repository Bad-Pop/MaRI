package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.WithSharedPostgres;
import io.github.badpop.mari.infra.database.model.favorite.ads.FavoriteAdEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.RENTAL;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@WithSharedPostgres
class FavoriteAdDeleterAdapterTest {

  private static final String ID_ONE = "one";
  private static final String ID_TWO = "two";

  @Inject
  FavoriteAdDeleterAdapter adapter;

  @BeforeEach
  @Transactional
  void setUp() {
    new FavoriteAdEntity(ID_ONE,
            "My first ad",
            "http://mari.fr/ads/123456789",
            RENTAL,
            "description",
            null,
            null,
            null,
            null).persist();
    new FavoriteAdEntity(ID_TWO,
            "My second ad",
            "http://mari.fr/ads/987654321",
            RENTAL,
            "description",
            null,
            null,
            null,
            null).persist();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    try {
      FavoriteAdEntity.deleteAll();
    } catch (Exception e) {
    }
  }

  @Test
  void should_delete_ad_by_id() {
    val actual = adapter.deleteById(ID_ONE);

    VavrAssertions.assertThat(actual).isRight();
    assertThat(FavoriteAdEntity.findAll().list()).hasSize(1);
    assertThat(FavoriteAdEntity.find("id", ID_ONE).<FavoriteAdEntity>firstResult()).isNull();
    assertThat(FavoriteAdEntity.find("id", ID_TWO).<FavoriteAdEntity>firstResult()).isNotNull();
  }

  @Test
  void should_delete_all_ads() {
    val actual = adapter.deleteAll();

    VavrAssertions.assertThat(actual).isRight();
    assertThat(FavoriteAdEntity.findAll().list()).isEmpty();
  }
}
