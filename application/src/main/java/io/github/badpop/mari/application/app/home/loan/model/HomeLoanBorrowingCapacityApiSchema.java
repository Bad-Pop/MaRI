package io.github.badpop.mari.application.app.home.loan.model;

import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanBorrowingCapacity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record HomeLoanBorrowingCapacityApiSchema(@Schema(description = "The amount of the loan")
                                                double amount,
                                                 @Schema(description = "The cost of interest at the end of the loan")
                                                double interestCost) {

    public static HomeLoanBorrowingCapacityApiSchema fromDomain(HomeLoanBorrowingCapacity domain) {
        return new HomeLoanBorrowingCapacityApiSchema(domain.amount(), domain.interestCost());
    }
}
