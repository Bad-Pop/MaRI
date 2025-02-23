package io.github.badpop.mari.domain.service.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.NoResourceFoundFail;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAd;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdSummary;
import io.github.badpop.mari.domain.port.spi.FavoriteAdFinderSpi;
import io.vavr.control.Either;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.SALE;
import static io.vavr.API.*;

class FavoriteAdFinderServiceTest {

  private static final FavoriteAd AN_AD = new FavoriteAd(
          FavoriteAdIdGenerator.generateFavoriteAdId("https://www.mari.fr/ads/123456789"),
          "My Wonderful Ad",
          "https://www.mari.fr/ads/123456789",
          SALE,
          Some("My ad description !"),
          None(),
          None(),
          Some(590000.0),
          None());

  private static final FavoriteAdSummary A_SUMMARY_AD = new FavoriteAdSummary(
          AN_AD.id(),
          AN_AD.name(),
          AN_AD.url(),
          AN_AD.type());

  @Nested
  class SuccessfulFind {

    private final FavoriteAdFinderService service = new FavoriteAdFinderService(new OkSpi());

    @Test
    void should_find_ad_by_id() {
      val actual = service.findById(AN_AD.id());
      VavrAssertions.assertThat(actual).containsOnRight(AN_AD);
    }

    @Test
    void should_find_all_ads() {
      val actual = service.findAll(0, 10);
      VavrAssertions.assertThat(actual)
              .containsOnRight(new Page<>(0, 1, false, 1, 1, Seq(A_SUMMARY_AD)));
    }

    @Test
    void should_find_all_ads_by_rental_type() {
      val actual = service.findAllByRentalType(0, 10);
      VavrAssertions.assertThat(actual)
              .containsOnRight(new Page<>(0, 1, false, 1, 1, Seq(A_SUMMARY_AD)));
    }

    @Test
    void should_find_all_ads_by_sale_type() {
      val actual = service.findAllBySaleType(0, 10);
      VavrAssertions.assertThat(actual)
              .containsOnRight(new Page<>(0, 1, false, 1, 1, Seq(A_SUMMARY_AD)));
    }

    private static class OkSpi implements FavoriteAdFinderSpi {
      @Override
      public Either<MariFail, FavoriteAd> findById(String id) {
        return Right(AN_AD);
      }

      @Override
      public Either<MariFail, Page<FavoriteAdSummary>> findAll(int page, int size) {
        return Right(new Page<>(
                0,
                1,
                false,
                1,
                1,
                Seq(A_SUMMARY_AD)));
      }

      @Override
      public Either<MariFail, Page<FavoriteAdSummary>> findAllByRentalType(int page, int size) {
        return Right(new Page<>(
                0,
                1,
                false,
                1,
                1,
                Seq(A_SUMMARY_AD)));
      }

      @Override
      public Either<MariFail, Page<FavoriteAdSummary>> findAllBySaleType(int page, int size) {
        return Right(new Page<>(
                0,
                1,
                false,
                1,
                1,
                Seq(A_SUMMARY_AD)));
      }
    }
  }

  @Nested
  class FindFailure {

    @Nested
    class NotFoundFailure {

      private final FavoriteAdFinderService service = new FavoriteAdFinderService(new NotFoundSpi());

      @Test
      void should_not_find_ad_by_id() {
        val actual = service.findById("not found id");
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(ResourceNotFoundFail.class);
      }

      @Test
      void should_not_find_all_ads() {
        val actual = service.findAll(0, 10);
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
      }

      @Test
      void should_not_find_all_ads_by_rental_type() {
        val actual = service.findAllByRentalType(0, 10);
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
      }

      @Test
      void should_not_find_all_ads_by_sale_type() {
        val actual = service.findAllBySaleType(0, 10);
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
      }

      private static class NotFoundSpi implements FavoriteAdFinderSpi {
        @Override
        public Either<MariFail, FavoriteAd> findById(String id) {
          return Left(new ResourceNotFoundFail("Ad not found for id " + id));
        }

        @Override
        public Either<MariFail, Page<FavoriteAdSummary>> findAll(int page, int size) {
          return Left(new NoResourceFoundFail("No ads where found"));
        }

        @Override
        public Either<MariFail, Page<FavoriteAdSummary>> findAllByRentalType(int page, int size) {
          return Left(new NoResourceFoundFail("No ads where found"));
        }

        @Override
        public Either<MariFail, Page<FavoriteAdSummary>> findAllBySaleType(int page, int size) {
          return Left(new NoResourceFoundFail("No ads where found"));
        }
      }
    }

    @Nested
    class TechnicalFailure {

      private final FavoriteAdFinderService service = new FavoriteAdFinderService(new TechnicalSpi());

      @Test
      void should_fail_to_find_ad_by_id() {
        val actual = service.findById(AN_AD.id());
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(TechnicalFail.class);
      }

      @Test
      void should_fail_to_find_all_ads() {
        val actual = service.findAll(0, 10);
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(TechnicalFail.class);
      }

      @Test
      void should_fail_to_find_all_ads_by_rental_type() {
        val actual = service.findAllByRentalType(0, 10);
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(TechnicalFail.class);
      }

      @Test
      void should_fail_to_find_all_ads_by_sale_type() {
        val actual = service.findAllBySaleType(0, 10);
        VavrAssertions.assertThat(actual).containsLeftInstanceOf(TechnicalFail.class);
      }

      private static class TechnicalSpi implements FavoriteAdFinderSpi {

        @Override
        public Either<MariFail, FavoriteAd> findById(String id) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }

        @Override
        public Either<MariFail, Page<FavoriteAdSummary>> findAll(int page, int size) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }

        @Override
        public Either<MariFail, Page<FavoriteAdSummary>> findAllByRentalType(int page, int size) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }

        @Override
        public Either<MariFail, Page<FavoriteAdSummary>> findAllBySaleType(int page, int size) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }
      }
    }
  }
}
