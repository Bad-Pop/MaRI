package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.WithSharedPostgres;
import io.github.badpop.mari.domain.control.favorite.ads.FavoriteAdAddition;
import io.github.badpop.mari.infra.database.model.favorite.ads.FavoriteAdEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.SALE;
import static io.github.badpop.mari.domain.service.favorite.ads.FavoriteAdIdGenerator.generateFavoriteAdId;
import static io.vavr.API.None;
import static io.vavr.API.Some;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@WithSharedPostgres
class FavoriteAdAdditionAdapterTest {

  @Inject
  FavoriteAdAdditionAdapter adapter;

  @AfterEach
  @Transactional
  void tearDown() {
    try {
      FavoriteAdEntity.deleteAll();
    } catch (Exception e) {}
  }

  @Test
  void should_create_ad() {
    val addition = new FavoriteAdAddition(
            "My Ad",
            "https://www.mari.fr/ad/123456789",
            SALE,
            Some("My ad description"),
            None(),
            None(),
            Some(125789.0),
            None());
    val ad = addition.toAd(generateFavoriteAdId(addition.adUrl()));

    val actual = adapter.createNewFavoriteAd(ad);

    VavrAssertions.assertThat(actual).containsOnRight(ad);

    val inDbAd = FavoriteAdEntity.find("id", ad.id()).<FavoriteAdEntity>firstResult();
    assertThat(inDbAd)
            .isNotNull()
            .satisfies(entity -> {
              assertThat(entity.getId()).isEqualTo(ad.id());
              assertThat(entity.getName()).isEqualTo(ad.name());
              assertThat(entity.getUrl()).isEqualTo(ad.url());
              assertThat(entity.getType()).isEqualTo(ad.type());
              assertThat(entity.getDescription()).isEqualTo(ad.description().getOrNull());
              assertThat(entity.getRemarks()).isEqualTo(ad.remarks().getOrNull());
              assertThat(entity.getAddress()).isEqualTo(ad.address().getOrNull());
              assertThat(entity.getPrice()).isEqualTo(ad.price().getOrNull());
              assertThat(entity.getPricePerSquareMeter()).isEqualTo(ad.pricePerSquareMeter().getOrNull());
            });
  }
}
