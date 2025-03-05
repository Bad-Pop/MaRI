package io.github.badpop.mari.application.app.home.loan.model;

import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanMonthlyPayment;

public record HomeLoanMonthlyPaymentApiSchema(double monthlyPaymentAmount,
                                              double interestCost) {

  public static HomeLoanMonthlyPaymentApiSchema fromDomain(HomeLoanMonthlyPayment domain) {
    return new HomeLoanMonthlyPaymentApiSchema(domain.monthlyPaymentAmount(), domain.interestCost());
  }
}
