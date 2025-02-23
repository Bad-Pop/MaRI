package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.lib.test.WithSharedPostgres;
import io.github.badpop.mari.domain.control.MariFail.NoResourceFoundFail;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType;
import io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdSummary;
import io.github.badpop.mari.infra.database.model.favorite.ads.FavoriteAdEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.RENTAL;
import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.SALE;
import static io.github.badpop.mari.assertions.PageAssert.assertThatPage;
import static io.vavr.API.Seq;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

@QuarkusTest
@WithSharedPostgres
class FavoriteAdFinderAdapterTest {

  @Inject
  FavoriteAdFinderAdapter adapter;

  @AfterEach
  @Transactional
  void tearDown() {
    try {
      FavoriteAdEntity.deleteAll();
    } catch (Exception e) {
    }
  }

  @Nested
  class FindById {
    @Test
    void should_find_ad_by_id() {
      val id = "myId";
      insertNewFavoriteAd(id, SALE);

      val actual = adapter.findById(id);

      assertThat(actual).isRight();
    }

    @Test
    void should_not_find_ad_by_id() {
      val id = "unknownId";

      val actual = adapter.findById(id);

      assertThat(actual).containsLeftInstanceOf(ResourceNotFoundFail.class);
    }
  }

  @Nested
  class FindAll {

    @Test
    void should_find_all_ads() {
      insertNewFavoriteAd("one", SALE);
      insertNewFavoriteAd("two", RENTAL);

      val actual = adapter.findAll(0, 10);

      assertThat(actual).isRight();

      val page = actual.get();

      assertThatPage(page).isEqualToWithItemsContainsExactly(
              new Page<>(0,
                      10,
                      false,
                      2,
                      1,
                      Seq(new FavoriteAdSummary("one", "title", "url", SALE),
                              new FavoriteAdSummary("two", "title", "url", RENTAL))));
    }

    @Test
    void should_find_all_ads_page_by_page() {
      insertNewFavoriteAd("one", SALE);
      insertNewFavoriteAd("two", RENTAL);

      val actualFirstPage = adapter.findAll(0, 1);
      val actualSecondPage = adapter.findAll(1, 1);
      val actualThirdPage = adapter.findAll(2, 1);

      assertThat(actualFirstPage).isRight();
      assertThat(actualSecondPage).isRight();
      assertThat(actualThirdPage).containsLeftInstanceOf(NoResourceFoundFail.class);

      val firstPage = actualFirstPage.get();
      val secondPage = actualSecondPage.get();

      assertThatPage(firstPage).isEqualToWithItemsContainsExactly(
              new Page<>(0,
                      1,
                      true,
                      2,
                      2,
                      Seq(new FavoriteAdSummary("one", "title", "url", SALE))));

      assertThatPage(secondPage).isEqualToWithItemsContainsExactly(
              new Page<>(1,
                      1,
                      false,
                      2,
                      2,
                      Seq(new FavoriteAdSummary("two", "title", "url", RENTAL))));
    }

    @Test
    void should_not_find_all_ads_when_no_ads_in_db() {
      val actual = adapter.findAll(0, 10);
      assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
    }
  }

  @Nested
  class FindAllByRentalType {

    @Test
    void should_find_all_ads_by_rental_type() {
      insertNewFavoriteAd("one", SALE);
      insertNewFavoriteAd("two", RENTAL);

      val actual = adapter.findAllByRentalType(0, 10);

      assertThat(actual).isRight();
      val page = actual.get();
      assertThatPage(page).isEqualToWithItemsContainsExactly(
              new Page<>(0,
                      10,
                      false,
                      1,
                      1,
                      Seq(new FavoriteAdSummary("two", "title", "url", RENTAL))));
    }

    @Test
    void should_find_all_ads_by_rental_type_page_by_page() {
      insertNewFavoriteAd("one", RENTAL);
      insertNewFavoriteAd("two", RENTAL);
      insertNewFavoriteAd("three", SALE);

      val actualFirstPage = adapter.findAllByRentalType(0, 1);
      val actualSecondPage = adapter.findAllByRentalType(1, 1);
      val actualThirdPage = adapter.findAllByRentalType(2, 1);

      assertThat(actualFirstPage).isRight();
      assertThat(actualSecondPage).isRight();
      assertThat(actualThirdPage).containsLeftInstanceOf(NoResourceFoundFail.class);

      val firstPage = actualFirstPage.get();
      val secondPage = actualSecondPage.get();

      assertThatPage(firstPage).isEqualToWithItemsContainsExactly(
              new Page<>(
                      0,
                      1,
                      true,
                      2,
                      2,
                      Seq(new FavoriteAdSummary("one", "title", "url", RENTAL))));

      assertThatPage(secondPage).isEqualToWithItemsContainsExactly(
              new Page<>(
                      1,
                      1,
                      false,
                      2,
                      2,
                      Seq(new FavoriteAdSummary("two", "title", "url", RENTAL))));
    }

    @Test
    void should_not_find_all_ads_by_rental_type_when_no_ads_in_db() {
      insertNewFavoriteAd("one", SALE);
      val actual = adapter.findAllByRentalType(0, 10);
      assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
    }
  }

  @Nested
  class FindAllBySaleType {

    @Test
    void should_find_all_ads_by_sale_type() {
      insertNewFavoriteAd("one", RENTAL);
      insertNewFavoriteAd("two", SALE);

      val actual = adapter.findAllBySaleType(0, 10);

      assertThat(actual).isRight();
      val page = actual.get();
      assertThatPage(page).isEqualToWithItemsContainsExactly(
              new Page<>(0,
                      10,
                      false,
                      1,
                      1,
                      Seq(new FavoriteAdSummary("two", "title", "url", SALE))));
    }

    @Test
    void should_find_all_ads_by_sale_type_page_by_page() {
      insertNewFavoriteAd("one", SALE);
      insertNewFavoriteAd("two", SALE);
      insertNewFavoriteAd("three", RENTAL);

      val actualFirstPage = adapter.findAllBySaleType(0, 1);
      val actualSecondPage = adapter.findAllBySaleType(1, 1);
      val actualThirdPage = adapter.findAllBySaleType(2, 1);

      assertThat(actualFirstPage).isRight();
      assertThat(actualSecondPage).isRight();
      assertThat(actualThirdPage).containsLeftInstanceOf(NoResourceFoundFail.class);

      val firstPage = actualFirstPage.get();
      val secondPage = actualSecondPage.get();

      assertThatPage(firstPage).isEqualToWithItemsContainsExactly(
              new Page<>(
                      0,
                      1,
                      true,
                      2,
                      2,
                      Seq(new FavoriteAdSummary("one", "title", "url", SALE))));

      assertThatPage(secondPage).isEqualToWithItemsContainsExactly(
              new Page<>(
                      1,
                      1,
                      false,
                      2,
                      2,
                      Seq(new FavoriteAdSummary("two", "title", "url", SALE))));
    }

    @Test
    void should_not_find_all_ads_by_sale_type_when_no_ads_in_db() {
      insertNewFavoriteAd("one", RENTAL);
      val actual = adapter.findAllBySaleType(0, 10);
      assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
    }
  }

  @Transactional
  protected void insertNewFavoriteAd(String id, FavoriteAdType type) {
    new FavoriteAdEntity(id, "title", "url", type, null, null, null, null, null).persist();
  }
}
