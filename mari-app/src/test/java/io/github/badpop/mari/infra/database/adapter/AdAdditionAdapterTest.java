package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.domain.control.ad.AdAddition;
import io.github.badpop.mari.infra.database.ad.AdEntity;
import io.github.badpop.mari.infra.database.ad.adapter.AdAdditionAdapter;
import io.github.badpop.mari.lib.test.WithSharedPostgres;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.ad.AdType.SALE;
import static io.github.badpop.mari.domain.service.ad.AdIdGenerator.generateAdId;
import static io.vavr.API.None;
import static io.vavr.API.Some;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@WithSharedPostgres
class AdAdditionAdapterTest {

  @Inject
  AdAdditionAdapter adapter;

  @AfterEach
  @Transactional
  void tearDown() {
    try {
      AdEntity.deleteAll();
    } catch (Exception e) {}
  }

  @Test
  void should_create_ad() {
    val addition = new AdAddition(
            "My Ad",
            "https://www.mari.fr/ad/123456789",
            SALE,
            125789.0,
            Some("My ad description"),
            None(),
            None(),
            None());
    val ad = addition.toAd(generateAdId(addition.adUrl()));

    val actual = adapter.createNewAd(ad);

    VavrAssertions.assertThat(actual).containsOnRight(ad);

    val inDbAd = AdEntity.find("id", ad.id()).<AdEntity>firstResult();
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
              assertThat(entity.getPrice()).isEqualTo(ad.price());
              assertThat(entity.getPricePerSquareMeter()).isEqualTo(ad.pricePerSquareMeter().getOrNull());
            });
  }
}
