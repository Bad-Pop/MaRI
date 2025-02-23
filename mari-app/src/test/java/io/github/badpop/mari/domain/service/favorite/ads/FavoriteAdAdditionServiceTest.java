package io.github.badpop.mari.domain.service.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.control.favorite.ads.FavoriteAdAddition;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.port.spi.FavoriteAdAdditionSpi;
import io.vavr.control.Either;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.SALE;
import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteAdAdditionServiceTest {

  @Nested
  class SuccessfulCreation {

    private final FavoriteAdAdditionService service = new FavoriteAdAdditionService(new OkSpi());

    @Test
    void should_return_the_created_ad() {
      val addition = new FavoriteAdAddition(
              "My Ad",
              "https://www.mari.fr/ad/123456789",
              SALE,
              Some("My ad description"),
              None(),
              None(),
              Some(125789.0),
              None());

      val actual = service.createNewFavoriteAd(addition);

      VavrAssertions.assertThat(actual).isRight();
      assertThat(actual.get())
              .satisfies(createdAd -> {
                assertThat(createdAd.id()).isNotBlank();
                assertThat(createdAd)
                        .usingRecursiveComparison()
                        .isEqualTo(addition.toAd(createdAd.id()));
              });
    }

    private static class OkSpi implements FavoriteAdAdditionSpi {
      @Override
      public Either<MariFail, FavoriteAd> createNewFavoriteAd(FavoriteAd ad) {
        return Right(ad);
      }
    }
  }

  @Nested
  class CreationFailure {

    private final FavoriteAdAdditionService service = new FavoriteAdAdditionService(new KoSpi());

    @Test
    void should_fail_to_create_ad() {
      val addition = new FavoriteAdAddition(
              "My Ad",
              "https://www.mari.fr/ad/123456789",
              SALE,
              Some("My ad description"),
              None(),
              None(),
              Some(125789.0),
              None());

      val actual = service.createNewFavoriteAd(addition);

      VavrAssertions.assertThat(actual).containsLeftInstanceOf(TechnicalFail.class);
    }

    private static class KoSpi implements FavoriteAdAdditionSpi {
      @Override
      public Either<MariFail, FavoriteAd> createNewFavoriteAd(FavoriteAd ad) {
        return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
      }
    }
  }
}
