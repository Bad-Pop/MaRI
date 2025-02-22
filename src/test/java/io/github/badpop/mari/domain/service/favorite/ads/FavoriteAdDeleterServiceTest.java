package io.github.badpop.mari.domain.service.favorite.ads;

import io.github.badpop.mari.domain.control.MariFail;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.domain.control.MariFail.TechnicalFail;
import io.github.badpop.mari.domain.port.spi.FavoriteAdDeleterSpi;
import io.vavr.control.Either;
import lombok.val;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

class FavoriteAdDeleterServiceTest {

  @Nested
  class SuccessfulDeletion {

    private final FavoriteAdDeleterService service = new FavoriteAdDeleterService(new OkSpi());

    @Test
    void should_delete_ad_by_id() {
      val actual = service.deleteById("734d2eea-13c2-4125-ad43-7b6c7521f0b8__2025-02-22T15:00:16.842669");
      VavrAssertions.assertThat(actual).isRight();
    }

    @Test
    void should_delete_all_ads() {
      val actual = service.deleteAll();
      VavrAssertions.assertThat(actual).isRight();
    }

    private static class OkSpi implements FavoriteAdDeleterSpi {
      @Override
      public Either<MariFail, Void> deleteById(String id) {
        return Right(null);
      }

      @Override
      public Either<MariFail, Void> deleteAll() {
        return Right(null);
      }
    }
  }

  @Nested
  class DeletionFailure {

    private final FavoriteAdDeleterService service = new FavoriteAdDeleterService(new KoSpi());

    @Test
    void should_fail_to_delete_ad_by_id() {
      val actual = service.deleteById("734d2eea-13c2-4125-ad43-7b6c7521f0b8__2025-02-22T15:00:16.842669");
      VavrAssertions.assertThat(actual).containsLeftInstanceOf(ResourceNotFoundFail.class);
    }

    @Test
    void should_fail_to_delete_all_ads() {
      val actual = service.deleteAll();
      VavrAssertions.assertThat(actual).containsLeftInstanceOf(TechnicalFail.class);
    }

    private static class KoSpi implements FavoriteAdDeleterSpi {
      @Override
      public Either<MariFail, Void> deleteById(String id) {
        return Left(new ResourceNotFoundFail("Ad with id " + id + " not found"));
      }

      @Override
      public Either<MariFail, Void> deleteAll() {
        return Left(new TechnicalFail("An error occurred", new Exception("Something went wrong")));
      }
    }
  }
}
