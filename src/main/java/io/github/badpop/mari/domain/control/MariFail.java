package io.github.badpop.mari.domain.control;

import io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit;
import io.github.badpop.mari.lib.failures.Fail;
import io.vavr.control.Option;

import static io.github.badpop.mari.lib.failures.Fail.Level.INFO;
import static io.vavr.API.Option;

public sealed interface MariFail extends Fail {

  record TechnicalFail(String message, Throwable causedBy) implements MariFail {

    @Override
    public String code() {
      return "TECHNICAL_FAILURE";
    }

    @Override
    public Option<Throwable> cause() {
      return Option(causedBy);
    }

    static TechnicalFail technicalWithoutThrowable(String message) {
      return new TechnicalFail(message, null);
    }
  }

  record HomeLoanMonthlyPaymentFail(double borrowedAmount,
                                    double annualInterestRate,
                                    int term,
                                    HomeLoanTermUnit termUnit) implements MariFail {
    @Override
    public String code() {
      return "HOME_LOAN_MONTHLY_PAYMENT_FAILURE";
    }

    @Override
    public String message() {
      return "Unable to compute the monthly payment for the home loan. Borrowed amount: %s, annual interest rate: %s, term: %s, term unit: %s"
              .formatted(borrowedAmount, annualInterestRate, term, termUnit.name());
    }
  }

  record HomeLoanBorrowingCapacityFail(double annualInterestRate,
                                       int term,
                                       HomeLoanTermUnit termUnit,
                                       double monthlyPayment) implements MariFail {

    @Override
    public String code() {
      return "HOME_LOAN_BORROWING_CAPACITY_FAILURE";
    }

    @Override
    public String message() {
      return "Unable to compute the borrowing capacity for the home loan. Annual interest rate: %s, term: %s, term unit: %s, monthly payment: %s"
              .formatted(annualInterestRate, term, termUnit.name(), monthlyPayment);
    }
  }

  record ResourceNotFoundFail(String message) implements MariFail {
    @Override
    public String code() {
      return "RESOURCE_NOT_FOUND_FAILURE";
    }

    @Override
    public Level level() {
      return INFO;
    }
  }

  record NoResourceFoundFail(String message) implements MariFail {
    @Override
    public String code() {
      return "NO_RESOURCE_FOUND_FAILURE";
    }

    @Override
    public Level level() {
      return INFO;
    }
  }
}
