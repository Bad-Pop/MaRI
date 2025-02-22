package io.github.badpop.mari.app.model.home.loan;

import io.github.badpop.mari.domain.model.home.loan.HomeLoanBorrowingCapacity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record HomeLoanBorrowingCapacityResponse(@Schema(description = "Le montant de l'emprunt")
                                                double amount,
                                                @Schema(description = "Le coût des intérets à la fin du crédit")
                                                double interestCost) {

    public static HomeLoanBorrowingCapacityResponse fromDomain(HomeLoanBorrowingCapacity domain) {
        return new HomeLoanBorrowingCapacityResponse(domain.amount(), domain.interestCost());
    }
}
