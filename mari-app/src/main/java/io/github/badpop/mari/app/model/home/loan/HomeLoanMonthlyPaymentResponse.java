package io.github.badpop.mari.app.model.home.loan;

import io.github.badpop.mari.domain.model.home.loan.HomeLoanMonthlyPayment;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record HomeLoanMonthlyPaymentResponse(@Schema(description = "The amount of the monthly payments")
                                             double monthlyPaymentAmount,
                                             @Schema(description = "The cost of interest at the end of the loan")
                                             double interestCost) {

    public static HomeLoanMonthlyPaymentResponse fromDomain(HomeLoanMonthlyPayment domain) {
        return new HomeLoanMonthlyPaymentResponse(domain.monthlyPaymentAmount(), domain.interestCost());
    }
}
