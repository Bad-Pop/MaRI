package io.github.badpop.mari.domain.service.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.control.ad.AdAddition;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.port.spi.AdAdditionSpi;
import io.vavr.control.Either;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.ad.AdType.SALE;
import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

class AdAdditionServiceTest {

  @Nested
  class SuccessfulCreation {

    private final AdAdditionService service = new AdAdditionService(new OkSpi());

    @Test
    void should_return_the_created_ad() {
      val addition = new AdAddition(
              "My Ad",
              "https://www.mari.fr/ad/123456789",
              SALE,
              125789.0,
              Some("My ad description"),
              None(),
              None(),
              None());

      val actual = service.createNewAd(addition);

      VavrAssertions.assertThat(actual).isRight();
      assertThat(actual.get())
              .satisfies(createdAd -> {
                assertThat(createdAd.id()).isNotBlank();
                assertThat(createdAd)
                        .usingRecursiveComparison()
                        .isEqualTo(addition.toAd(createdAd.id()));
              });
    }

    private static class OkSpi implements AdAdditionSpi {
      @Override
      public Either<MariFail, Ad> createNewAd(Ad ad) {
        return Right(ad);
      }
    }
  }

  @Nested
  class CreationFailure {

    private final AdAdditionService service = new AdAdditionService(new KoSpi());

    @Test
    void should_fail_to_create_ad() {
      val addition = new AdAddition(
              "My Ad",
              "https://www.mari.fr/ad/123456789",
              SALE,
              125789.0,
              Some("My ad description"),
              None(),
              None(),
              None());

      val actual = service.createNewAd(addition);

      VavrAssertions.assertThat(actual).containsLeftInstanceOf(TechnicalFail.class);
    }

    private static class KoSpi implements AdAdditionSpi {
      @Override
      public Either<MariFail, Ad> createNewAd(Ad ad) {
        return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
      }
    }
  }
}
