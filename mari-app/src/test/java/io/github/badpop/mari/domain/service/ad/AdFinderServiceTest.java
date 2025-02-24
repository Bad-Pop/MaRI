package io.github.badpop.mari.domain.service.ad;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.NoResourceFoundFail;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.ad.Ad;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import io.github.badpop.mari.domain.port.spi.AdFinderSpi;
import io.vavr.control.Either;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.ad.AdType.SALE;
import static io.vavr.API.*;

class AdFinderServiceTest {

  private static final Ad AN_AD = new Ad(
          AdIdGenerator.generateAdId("https://www.mari.fr/ads/123456789"),
          "My Wonderful Ad",
          "https://www.mari.fr/ads/123456789",
          SALE,
          590000.0,
          Some("My ad description !"),
          None(),
          None(),
          None());

  private static final AdSummary A_SUMMARY_AD = new AdSummary(
          AN_AD.id(),
          AN_AD.name(),
          AN_AD.url(),
          AN_AD.type(),
          AN_AD.price());

  @Nested
  class SuccessfulFind {

    private final AdFinderService service = new AdFinderService(new OkSpi());

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

    private static class OkSpi implements AdFinderSpi {
      @Override
      public Either<MariFail, Ad> findById(String id) {
        return Right(AN_AD);
      }

      @Override
      public Either<MariFail, Page<AdSummary>> findAll(int page, int size) {
        return Right(new Page<>(
                0,
                1,
                false,
                1,
                1,
                Seq(A_SUMMARY_AD)));
      }

      @Override
      public Either<MariFail, Page<AdSummary>> findAllByRentalType(int page, int size) {
        return Right(new Page<>(
                0,
                1,
                false,
                1,
                1,
                Seq(A_SUMMARY_AD)));
      }

      @Override
      public Either<MariFail, Page<AdSummary>> findAllBySaleType(int page, int size) {
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

      private final AdFinderService service = new AdFinderService(new NotFoundSpi());

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

      private static class NotFoundSpi implements AdFinderSpi {
        @Override
        public Either<MariFail, Ad> findById(String id) {
          return Left(new ResourceNotFoundFail("Ad not found for id " + id));
        }

        @Override
        public Either<MariFail, Page<AdSummary>> findAll(int page, int size) {
          return Left(new NoResourceFoundFail("No ads where found"));
        }

        @Override
        public Either<MariFail, Page<AdSummary>> findAllByRentalType(int page, int size) {
          return Left(new NoResourceFoundFail("No ads where found"));
        }

        @Override
        public Either<MariFail, Page<AdSummary>> findAllBySaleType(int page, int size) {
          return Left(new NoResourceFoundFail("No ads where found"));
        }
      }
    }

    @Nested
    class TechnicalFailure {

      private final AdFinderService service = new AdFinderService(new TechnicalSpi());

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

      private static class TechnicalSpi implements AdFinderSpi {

        @Override
        public Either<MariFail, Ad> findById(String id) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }

        @Override
        public Either<MariFail, Page<AdSummary>> findAll(int page, int size) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }

        @Override
        public Either<MariFail, Page<AdSummary>> findAllByRentalType(int page, int size) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }

        @Override
        public Either<MariFail, Page<AdSummary>> findAllBySaleType(int page, int size) {
          return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
        }
      }
    }
  }
}
