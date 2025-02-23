package io.github.badpop.mari.app.model.home.loan;

import io.github.badpop.mari.domain.model.home.loan.HomeLoanBorrowingCapacity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record HomeLoanBorrowingCapacityResponse(@Schema(description = "The amount of the loan")
                                                double amount,
                                                @Schema(description = "The cost of interest at the end of the loan")
                                                double interestCost) {

    public static HomeLoanBorrowingCapacityResponse fromDomain(HomeLoanBorrowingCapacity domain) {
        return new HomeLoanBorrowingCapacityResponse(domain.amount(), domain.interestCost());
    }
}
