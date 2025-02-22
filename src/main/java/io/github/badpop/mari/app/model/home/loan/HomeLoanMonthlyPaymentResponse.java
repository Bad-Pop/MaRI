package io.github.badpop.mari.app.model.home.loan;

import io.github.badpop.mari.domain.model.home.loan.HomeLoanMonthlyPayment;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record HomeLoanMonthlyPaymentResponse(@Schema(description = "Le montant des mensualités")
                                             double monthlyPaymentAmount,
                                             @Schema(description = "Le coût des intérets à la fin du crédit")
                                             double interestCost) {

    public static HomeLoanMonthlyPaymentResponse fromDomain(HomeLoanMonthlyPayment domain) {
        return new HomeLoanMonthlyPaymentResponse(domain.monthlyPaymentAmount(), domain.interestCost());
    }
}
