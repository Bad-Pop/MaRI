package io.github.badpop.mari.app.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.badpop.mari.app.model.favorite.ads.request.FavoriteAdAdditionRequestBody;
import io.github.badpop.mari.domain.control.MariFail.ResourceNotFoundFail;
import io.github.badpop.mari.infra.database.model.favorite.ads.FavoriteAdEntity;
import io.github.badpop.mari.lib.test.WithSharedPostgres;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.RENTAL;
import static io.github.badpop.mari.domain.model.favorite.ads.FavoriteAdType.SALE;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static jakarta.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@WithSharedPostgres
class FavoriteAdResourceTest {

  private static final String BASE_PATH = "/favorite-ads";
  private static final String RENTAL_PATH = BASE_PATH + "/rental";
  private static final String SALE_PATH = BASE_PATH + "/sale";

  @Inject
  ObjectMapper objectMapper;

  @BeforeAll
  static void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @AfterEach
  @Transactional
  void tearDown() {
    try {
      FavoriteAdEntity.deleteAll();
    } catch (Exception e) {
    }
  }

  @Nested
  class Addition {

    @Nested
    class ByRentalType {

      @Test
      void should_create_new_favorite_ad() throws JsonProcessingException {
        val body = new FavoriteAdAdditionRequestBody(
                "My Ad",
                "https://www.mari.fr/ads/123456789",
                "My awsome ad description",
                "My personal remarks",
                "The property address",
                150000.0,
                3253.0);

        val responseBody = given()
                .body(body)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .post(RENTAL_PATH)
                .then()
                .statusCode(200)
                .body(jsonEquals("""
                        {
                            "id": "IGNORED",
                            "name": "My Ad",
                            "url": "https://www.mari.fr/ads/123456789",
                            "type": "RENTAL",
                            "description": "My awsome ad description",
                            "remarks": "My personal remarks",
                            "address": "The property address",
                            "price": 150000.0,
                            "pricePerSquareMeter": 3253.0
                        }
                        """)
                        .whenIgnoringPaths("id"))
                .body("id", notNullValue())
                .extract()
                .body()
                .asString();

        val adId = extractAdIdFromJsonBody(responseBody);
        val maybeInDbAd = FavoriteAdEntity.findById(adId);

        VavrAssertions.assertThat(maybeInDbAd).isRight();
      }

      @Test
      void should_not_create_favorite_ad_with_missing_mandatory_fields() {
        val body = new FavoriteAdAdditionRequestBody(
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        given()
                .body(body)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .post(RENTAL_PATH)
                .then()
                .statusCode(400)
                .body("title", is("Constraint Violation"))
                .body("status", is(400))
                .body("violations", hasSize(2))
                .body("violations.field", hasItems(
                        "addNewRentalFavoriteAd.additionRequest.adUrl",
                        "addNewRentalFavoriteAd.additionRequest.adName"));
      }
    }

    @Nested
    class BySaleType {

      @Test
      void should_create_new_favorite_ad() throws JsonProcessingException {
        val body = new FavoriteAdAdditionRequestBody(
                "My Ad",
                "https://www.mari.fr/ads/123456789",
                "My awsome ad description",
                "My personal remarks",
                "The property address",
                150000.0,
                3253.0);

        val responseBody = given()
                .body(body)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .post(SALE_PATH)
                .then()
                .statusCode(200)
                .body(jsonEquals("""
                        {
                            "id": "IGNORED",
                            "name": "My Ad",
                            "url": "https://www.mari.fr/ads/123456789",
                            "type": "SALE",
                            "description": "My awsome ad description",
                            "remarks": "My personal remarks",
                            "address": "The property address",
                            "price": 150000.0,
                            "pricePerSquareMeter": 3253.0
                        }
                        """)
                        .whenIgnoringPaths("id"))
                .body("id", notNullValue())
                .extract()
                .body()
                .asString();

        val adId = extractAdIdFromJsonBody(responseBody);
        val maybeInDbAd = FavoriteAdEntity.findById(adId);

        VavrAssertions.assertThat(maybeInDbAd).isRight();
      }

      @Test
      void should_not_create_favorite_ad_with_missing_mandatory_fields() {
        val body = new FavoriteAdAdditionRequestBody(
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        given()
                .body(body)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .post(SALE_PATH)
                .then()
                .statusCode(400)
                .body("title", is("Constraint Violation"))
                .body("status", is(400))
                .body("violations", hasSize(2))
                .body("violations.field", hasItems(
                        "addNewSaleFavoriteAd.additionRequest.adUrl",
                        "addNewSaleFavoriteAd.additionRequest.adName"));
      }
    }
  }

  @Nested
  class GetById {

    @Test
    void should_find_ad_by_id() {
      val entity = new FavoriteAdEntity(
              "id",
              "My Ad",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entity);

      given()
              .pathParam("favoriteAdId", entity.getId())
              .get(BASE_PATH + "/{favoriteAdId}")
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                            "id": "id",
                            "name": "My Ad",
                            "url": "https://www.mari.fr/ads/123456789",
                            "type": "SALE",
                            "description": "My awsome ad description",
                            "remarks": "My personal remarks",
                            "address": "The property address",
                            "price": 150000.0,
                            "pricePerSquareMeter": 3253.0
                        }
                      """));
    }

    @Test
    void should_not_find_ad_by_id_if_does_not_exist() {
      given()
              .pathParam("favoriteAdId", "unknown-id")
              .get(BASE_PATH + "/{favoriteAdId}")
              .then()
              .statusCode(204);
    }
  }

  @Nested
  class GetAll {

    @Test
    void should_get_all_ads() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);

      given()
              .queryParam("page", 0)
              .queryParam("limit", 2)
              .get(BASE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 2,
                          "hasNextPage": false,
                          "totalItemsCount": 2,
                          "totalPagesCount": 1,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(2))
              .body("items.id", hasItems(entityOne.getId(), entityTwo.getId()))
              .body("items.name", hasItems(entityOne.getName(), entityTwo.getName()))
              .body("items.url", hasItems(entityOne.getUrl(), entityTwo.getUrl()))
              .body("items.type", hasItems(entityOne.getType().name(), entityTwo.getType().name()));
    }

    @Test
    void should_get_first_page() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);

      given()
              .queryParam("page", 0)
              .queryParam("limit", 1)
              .get(BASE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 1,
                          "hasNextPage": true,
                          "totalItemsCount": 2,
                          "totalPagesCount": 2,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityOne.getId()))
              .body("items.name", hasItems(entityOne.getName()))
              .body("items.url", hasItems(entityOne.getUrl()))
              .body("items.type", hasItems(entityOne.getType().name()));
    }

    @Test
    void should_get_last_page() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);

      given()
              .queryParam("page", 1)
              .queryParam("limit", 1)
              .get(BASE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 1,
                          "pageSize": 1,
                          "hasNextPage": false,
                          "totalItemsCount": 2,
                          "totalPagesCount": 2,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityTwo.getId()))
              .body("items.name", hasItems(entityTwo.getName()))
              .body("items.url", hasItems(entityTwo.getUrl()))
              .body("items.type", hasItems(entityTwo.getType().name()));
    }

    @Test
    void should_get_all_ads_with_default_pagination() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);

      given()
              //No query params to use default pagination
              .get(BASE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 10,
                          "hasNextPage": false,
                          "totalItemsCount": 2,
                          "totalPagesCount": 1,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(2))
              .body("items.id", hasItems(entityOne.getId(), entityTwo.getId()))
              .body("items.name", hasItems(entityOne.getName(), entityTwo.getName()))
              .body("items.url", hasItems(entityOne.getUrl(), entityTwo.getUrl()))
              .body("items.type", hasItems(entityOne.getType().name(), entityTwo.getType().name()));
    }

    @Test
    void should_not_get_all_on_empty_table() {
      given()
              .queryParam("page", 0)
              .queryParam("limit", 10)
              .get(BASE_PATH)
              .then()
              .statusCode(204);
    }

    @Test
    void should_not_get_all_on_empty_table_with_default_pagination() {
      given()
              //No query params to use default pagination
              .get(BASE_PATH)
              .then()
              .statusCode(204);
    }
  }

  @Nested
  class GetAllByRentalType {

    @Test
    void should_get_all_ads() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);

      given()
              .queryParam("page", 0)
              .queryParam("limit", 2)
              .get(RENTAL_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 2,
                          "hasNextPage": false,
                          "totalItemsCount": 1,
                          "totalPagesCount": 1,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityTwo.getId()))
              .body("items.name", hasItems(entityTwo.getName()))
              .body("items.url", hasItems(entityTwo.getUrl()))
              .body("items.type", hasItems(entityTwo.getType().name()));
    }

    @Test
    void should_get_first_page() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityThree = new FavoriteAdEntity(
              "id3",
              "My Ad Three",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);
      persistFavoriteAd(entityThree);

      given()
              .queryParam("page", 0)
              .queryParam("limit", 1)
              .get(RENTAL_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 1,
                          "hasNextPage": true,
                          "totalItemsCount": 2,
                          "totalPagesCount": 2,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityTwo.getId()))
              .body("items.name", hasItems(entityTwo.getName()))
              .body("items.url", hasItems(entityTwo.getUrl()))
              .body("items.type", hasItems(entityTwo.getType().name()));
    }

    @Test
    void should_get_last_page() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityThree = new FavoriteAdEntity(
              "id3",
              "My Ad Three",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);
      persistFavoriteAd(entityThree);

      given()
              .queryParam("page", 1)
              .queryParam("limit", 1)
              .get(RENTAL_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 1,
                          "pageSize": 1,
                          "hasNextPage": false,
                          "totalItemsCount": 2,
                          "totalPagesCount": 2,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityThree.getId()))
              .body("items.name", hasItems(entityThree.getName()))
              .body("items.url", hasItems(entityThree.getUrl()))
              .body("items.type", hasItems(entityThree.getType().name()));
    }

    @Test
    void should_get_all_ads_with_default_pagination() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityThree = new FavoriteAdEntity(
              "id3",
              "My Ad Three",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);
      persistFavoriteAd(entityThree);

      given()
              //No query params to use default pagination
              .get(RENTAL_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 10,
                          "hasNextPage": false,
                          "totalItemsCount": 2,
                          "totalPagesCount": 1,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(2))
              .body("items.id", hasItems(entityTwo.getId(), entityThree.getId()))
              .body("items.name", hasItems(entityTwo.getName(), entityThree.getName()))
              .body("items.url", hasItems(entityTwo.getUrl(), entityThree.getUrl()))
              .body("items.type", hasItems(entityTwo.getType().name(), entityThree.getType().name()));
    }

    @Test
    void should_not_get_all_on_empty_table() {
      given()
              .queryParam("page", 0)
              .queryParam("limit", 10)
              .get(RENTAL_PATH)
              .then()
              .statusCode(204);
    }

    @Test
    void should_not_get_all_on_empty_table_with_default_pagination() {
      given()
              //No query params to use default pagination
              .get(RENTAL_PATH)
              .then()
              .statusCode(204);
    }
  }

  @Nested
  class GetAllBySaleType {
    @Test
    void should_get_all_ads() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);

      given()
              .queryParam("page", 0)
              .queryParam("limit", 2)
              .get(SALE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 2,
                          "hasNextPage": false,
                          "totalItemsCount": 1,
                          "totalPagesCount": 1,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityTwo.getId()))
              .body("items.name", hasItems(entityTwo.getName()))
              .body("items.url", hasItems(entityTwo.getUrl()))
              .body("items.type", hasItems(entityTwo.getType().name()));
    }

    @Test
    void should_get_first_page() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityThree = new FavoriteAdEntity(
              "id3",
              "My Ad Three",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);
      persistFavoriteAd(entityThree);

      given()
              .queryParam("page", 0)
              .queryParam("limit", 1)
              .get(SALE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 1,
                          "hasNextPage": true,
                          "totalItemsCount": 2,
                          "totalPagesCount": 2,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityTwo.getId()))
              .body("items.name", hasItems(entityTwo.getName()))
              .body("items.url", hasItems(entityTwo.getUrl()))
              .body("items.type", hasItems(entityTwo.getType().name()));
    }

    @Test
    void should_get_last_page() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityThree = new FavoriteAdEntity(
              "id3",
              "My Ad Three",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);
      persistFavoriteAd(entityThree);

      given()
              .queryParam("page", 1)
              .queryParam("limit", 1)
              .get(SALE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 1,
                          "pageSize": 1,
                          "hasNextPage": false,
                          "totalItemsCount": 2,
                          "totalPagesCount": 2,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(1))
              .body("items.id", hasItems(entityThree.getId()))
              .body("items.name", hasItems(entityThree.getName()))
              .body("items.url", hasItems(entityThree.getUrl()))
              .body("items.type", hasItems(entityThree.getType().name()));
    }

    @Test
    void should_get_all_ads_with_default_pagination() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              RENTAL,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityThree = new FavoriteAdEntity(
              "id3",
              "My Ad Three",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);
      persistFavoriteAd(entityThree);

      given()
              //No query params to use default pagination
              .get(SALE_PATH)
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "pageNumber": 0,
                          "pageSize": 10,
                          "hasNextPage": false,
                          "totalItemsCount": 2,
                          "totalPagesCount": 1,
                          "items": "IGNORED"
                      }
                      """).whenIgnoringPaths("items"))
              .body("items", hasSize(2))
              .body("items.id", hasItems(entityTwo.getId(), entityThree.getId()))
              .body("items.name", hasItems(entityTwo.getName(), entityThree.getName()))
              .body("items.url", hasItems(entityTwo.getUrl(), entityThree.getUrl()))
              .body("items.type", hasItems(entityTwo.getType().name(), entityThree.getType().name()));
    }

    @Test
    void should_not_get_all_on_empty_table() {
      given()
              .queryParam("page", 0)
              .queryParam("limit", 10)
              .get(SALE_PATH)
              .then()
              .statusCode(204);
    }

    @Test
    void should_not_get_all_on_empty_table_with_default_pagination() {
      given()
              //No query params to use default pagination
              .get(SALE_PATH)
              .then()
              .statusCode(204);
    }
  }

  @Nested
  class Delete {

    @Test
    void should_delete_existing_ad() {
      val entity = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entity);

      given()
              .pathParam("favoriteAdId", entity.getId())
              .delete(BASE_PATH + "/{favoriteAdId}")
              .then()
              .statusCode(202);

      VavrAssertions.assertThat(FavoriteAdEntity.findById(entity.getId()))
              .containsLeftInstanceOf(ResourceNotFoundFail.class);
    }

    @Test
    void should_not_delete_ad_if_it_does_not_exists() {
      given()
              .pathParam("favoriteAdId", "not-existing-id")
              .delete(BASE_PATH + "/{favoriteAdId}")
              .then()
              .statusCode(202);

      val inDbAds = FavoriteAdEntity.findAll().list();
      Assertions.assertThat(inDbAds).isEmpty();
    }
  }

  @Nested
  class DeleteAll {

    @Test
    void should_delete_all_ads() {
      val entityOne = new FavoriteAdEntity(
              "id1",
              "My Ad One",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      val entityTwo = new FavoriteAdEntity(
              "id2",
              "My Ad Two",
              "https://www.mari.fr/ads/123456789",
              SALE,
              "My awsome ad description",
              "My personal remarks",
              "The property address",
              150000.0,
              3253.0);
      persistFavoriteAd(entityOne);
      persistFavoriteAd(entityTwo);

      given()
              .delete(BASE_PATH)
              .then()
              .statusCode(202);

      val inDbAds = FavoriteAdEntity.findAll().list();
      Assertions.assertThat(inDbAds).isEmpty();
    }
  }

  private String extractAdIdFromJsonBody(String jsonBody) throws JsonProcessingException {
    return objectMapper.readTree(jsonBody)
            .at("/id")
            .asText();
  }

  @Transactional
  protected void persistFavoriteAd(FavoriteAdEntity entity) {
    entity.persist();
  }
}
