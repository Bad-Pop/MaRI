package io.github.badpop.mari.app.resource;


import io.github.badpop.mari.WithoutPostgres;
import io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit.MONTHS;
import static io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit.YEARS;
import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;

@QuarkusTest
@WithoutPostgres
class HomeLoanCalculatorResourceTest {

  private static final String BASE_PATH = "/home-loan/calculator";
  private static final String MONTHLY_PAYMENT_BASE_PATH = "/monthly-payment?borrowedAmount=%s&annualInterestRate=%s&term=%s&termUnit=%s";
  private static final String BORROWING_CAPACITY_BASE_PATH = "/borrowing-capacity";

  @BeforeAll
  static void setup() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Nested
  class ComputeMonthlyPayment {

    @ParameterizedTest
    @MethodSource("provideValidParams")
    void should_compute_monthly_payment(double borrowedAmount,
                                        double interestRate,
                                        int term,
                                        HomeLoanTermUnit termUnit,
                                        double expectedMonthlyPaymentAmount,
                                        double expectedInterestCost) {
      given()
              .when()
              .get(BASE_PATH + MONTHLY_PAYMENT_BASE_PATH.formatted(borrowedAmount, interestRate, term, termUnit))
              .then()
              .statusCode(200)
              .body(jsonEquals("""
                      {
                          "monthlyPaymentAmount": %s,
                          "interestCost": %s
                      }
                      """.formatted(expectedMonthlyPaymentAmount, expectedInterestCost)));
    }

    @Test
    void should_not_compute_monthly_payment_on_invalid_query_params() {
      given()
              .when()
              .get(BASE_PATH + "/monthly-payment")
              .then()
              .statusCode(400);
      //TODO ASSERT BODY
    }

    private static Stream<Arguments> provideValidParams() {
      return Stream.of(
              //100_000€ sur 25 ans avec un taux à 0%
              Arguments.of(100000.0, 0.0, 300, MONTHS, 333.33, -1.0),
              Arguments.of(100000.0, 0.0, 25, YEARS, 333.33, -1.0),
              //100_000 sur 25 ans avec un taux à 4%
              Arguments.of(100000.0, 4.0, 300, MONTHS, 527.84, 58352.0),
              Arguments.of(100000.0, 4.0, 25, YEARS, 527.84, 58352.0),
              //127_520 sur 20 ans avec un taux à 3,99%
              Arguments.of(127520.0, 3.99, 240, MONTHS, 772.07, 57776.8),
              Arguments.of(127520.0, 3.99, 20, YEARS, 772.07, 57776.8),
              //500_990 sur 10 ans avec un taux à 1%
              Arguments.of(500990.0, 1.0, 120, MONTHS, 4388.88, 25675.6),
              Arguments.of(500990.0, 1.0, 10, YEARS, 4388.88, 25675.6)
      );
    }
  }

}
