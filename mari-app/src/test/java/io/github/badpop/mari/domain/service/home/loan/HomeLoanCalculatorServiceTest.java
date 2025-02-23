package io.github.badpop.mari.domain.service.home.loan;

import io.github.badpop.mari.domain.model.home.loan.HomeLoanBorrowingCapacity;
import io.github.badpop.mari.domain.model.home.loan.HomeLoanMonthlyPayment;
import io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit.MONTHS;
import static io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit.YEARS;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

class HomeLoanCalculatorServiceTest {

  private final HomeLoanCalculatorService service = new HomeLoanCalculatorService();

  @Nested
  class ComputeMonthlyPayment {

    @ParameterizedTest
    @MethodSource("provideValidParams")
    void should_properly_calculate_home_loan_monthly_payment_amount(double borrowedAmount,
                                                                    double interestRate,
                                                                    int term,
                                                                    HomeLoanTermUnit termUnit,
                                                                    double expectedMonthlyPaymentAmount,
                                                                    double expectedInterestCost) {
      val actual = service.computeMonthlyPayment(borrowedAmount, interestRate, term, termUnit);
      assertThat(actual)
              .containsOnRight(new HomeLoanMonthlyPayment(expectedMonthlyPaymentAmount, expectedInterestCost));
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

  @Nested
  class ComputeBorrowingCapacity {

    @ParameterizedTest
    @MethodSource("provideValidParams")
    void should_properly_calculate_home_loan_capacity(double annualInterestRate,
                                                      double monthlyPayment,
                                                      int term,
                                                      HomeLoanTermUnit termUnit,
                                                      double expectedAmount,
                                                      double expectedInterestCost) {
      val actual = service.computeBorrowingCapacity(annualInterestRate, term, termUnit, monthlyPayment);
      assertThat(actual)
              .containsOnRight(new HomeLoanBorrowingCapacity(expectedAmount, expectedInterestCost));
    }

    private static Stream<Arguments> provideValidParams() {
      return Stream.of(
              //100_000€ sur 25 ans avec un taux à 0%
              Arguments.of(0.0, 333.33, 300, MONTHS, 99999.0, 0.0),
              Arguments.of(0.0, 333.33, 25, YEARS, 99999.0, 0.0),
              //100_000 sur 25 ans avec un taux à 4
              Arguments.of(4.0, 527.84, 300, MONTHS, 100000.6, 58351.4),
              Arguments.of(4.0, 527.84, 25, YEARS, 100000.6, 58351.4)
      );
    }
  }
}
