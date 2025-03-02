package io.github.badpop.mari.infra.database.adapter;

import io.github.badpop.mari.infra.database.ad.adapter.AdFinderAdapter;
import io.github.badpop.mari.lib.test.WithSharedPostgres;
import io.github.badpop.mari.domain.control.MariFail.NoResourceFoundFail;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.domain.control.Page;
import io.github.badpop.mari.domain.model.ad.AdType;
import io.github.badpop.mari.domain.model.ad.AdSummary;
import io.github.badpop.mari.infra.database.ad.AdEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.ad.AdType.RENTAL;
import static io.github.badpop.mari.domain.model.ad.AdType.SALE;
import static io.github.badpop.mari.assertions.PageAssert.assertThatPage;
import static io.vavr.API.Seq;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

@QuarkusTest
@WithSharedPostgres
class AdFinderAdapterTest {

  @Inject
  AdFinderAdapter adapter;

  @AfterEach
  @Transactional
  void tearDown() {
    try {
      AdEntity.deleteAll();
    } catch (Exception e) {
    }
  }

  @Nested
  class FindById {
    @Test
    void should_find_ad_by_id() {
      val id = "myId";
      insertNewAd(id, SALE);

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
      insertNewAd("one", SALE);
      insertNewAd("two", RENTAL);

      val actual = adapter.findAll(0, 10);

      assertThat(actual).isRight();

      val page = actual.get();

      assertThatPage(page).isEqualToWithItemsContainsExactly(
              new Page<>(0,
                      10,
                      false,
                      2,
                      1,
                      Seq(new AdSummary("one", "title", "url", SALE, 100000.0),
                              new AdSummary("two", "title", "url", RENTAL, 100000.0))));
    }

    @Test
    void should_find_all_ads_page_by_page() {
      insertNewAd("one", SALE);
      insertNewAd("two", RENTAL);

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
                      Seq(new AdSummary("one", "title", "url", SALE, 100000.0))));

      assertThatPage(secondPage).isEqualToWithItemsContainsExactly(
              new Page<>(1,
                      1,
                      false,
                      2,
                      2,
                      Seq(new AdSummary("two", "title", "url", RENTAL, 100000.0))));
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
      insertNewAd("one", SALE);
      insertNewAd("two", RENTAL);

      val actual = adapter.findAllByRentalType(0, 10);

      assertThat(actual).isRight();
      val page = actual.get();
      assertThatPage(page).isEqualToWithItemsContainsExactly(
              new Page<>(0,
                      10,
                      false,
                      1,
                      1,
                      Seq(new AdSummary("two", "title", "url", RENTAL, 100000.0))));
    }

    @Test
    void should_find_all_ads_by_rental_type_page_by_page() {
      insertNewAd("one", RENTAL);
      insertNewAd("two", RENTAL);
      insertNewAd("three", SALE);

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
                      Seq(new AdSummary("one", "title", "url", RENTAL, 100000.0))));

      assertThatPage(secondPage).isEqualToWithItemsContainsExactly(
              new Page<>(
                      1,
                      1,
                      false,
                      2,
                      2,
                      Seq(new AdSummary("two", "title", "url", RENTAL, 100000.0))));
    }

    @Test
    void should_not_find_all_ads_by_rental_type_when_no_ads_in_db() {
      insertNewAd("one", SALE);
      val actual = adapter.findAllByRentalType(0, 10);
      assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
    }
  }

  @Nested
  class FindAllBySaleType {

    @Test
    void should_find_all_ads_by_sale_type() {
      insertNewAd("one", RENTAL);
      insertNewAd("two", SALE);

      val actual = adapter.findAllBySaleType(0, 10);

      assertThat(actual).isRight();
      val page = actual.get();
      assertThatPage(page).isEqualToWithItemsContainsExactly(
              new Page<>(0,
                      10,
                      false,
                      1,
                      1,
                      Seq(new AdSummary("two", "title", "url", SALE, 100000.0))));
    }

    @Test
    void should_find_all_ads_by_sale_type_page_by_page() {
      insertNewAd("one", SALE);
      insertNewAd("two", SALE);
      insertNewAd("three", RENTAL);

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
                      Seq(new AdSummary("one", "title", "url", SALE, 100000.0))));

      assertThatPage(secondPage).isEqualToWithItemsContainsExactly(
              new Page<>(
                      1,
                      1,
                      false,
                      2,
                      2,
                      Seq(new AdSummary("two", "title", "url", SALE, 100000.0))));
    }

    @Test
    void should_not_find_all_ads_by_sale_type_when_no_ads_in_db() {
      insertNewAd("one", RENTAL);
      val actual = adapter.findAllBySaleType(0, 10);
      assertThat(actual).containsLeftInstanceOf(NoResourceFoundFail.class);
    }
  }

  @Transactional
  protected void insertNewAd(String id, AdType type) {
    new AdEntity(id, "title", "url", type, 100000.0, null, null, null, null).persist();
  }
}
