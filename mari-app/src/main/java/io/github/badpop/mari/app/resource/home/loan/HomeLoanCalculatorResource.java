package io.github.badpop.mari.app.resource.home.loan;

import io.github.badpop.mari.app.model.home.loan.HomeLoanBorrowingCapacityResponse;
import io.github.badpop.mari.app.model.home.loan.HomeLoanMonthlyPaymentResponse;
import io.github.badpop.mari.app.resource.ResponseBuilder;
import io.github.badpop.mari.domain.model.home.loan.HomeLoanTermUnit;
import io.github.badpop.mari.domain.port.api.home.loan.HomeLoanCalculatorApi;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class HomeLoanCalculatorResource implements HomeLoanCalculatorResourceSpec {

    private final HomeLoanCalculatorApi api;

    @Override
    public Response computeMonthlyPayment(String correlationId,
                                          Double borrowedAmount,
                                          Double annualInterestRate,
                                          Integer term,
                                          HomeLoanTermUnit termUnit) {
        return api.computeMonthlyPayment(borrowedAmount, annualInterestRate, term, termUnit)
                .map(HomeLoanMonthlyPaymentResponse::fromDomain)
                .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
    }

    @Override
    public Response computeBorrowingCapacity(String correlationId,
                                             Double annualInterestRate,
                                             Integer term,
                                             HomeLoanTermUnit termUnit,
                                             Double monthlyPayment) {
        return api.computeBorrowingCapacity(annualInterestRate, term, termUnit, monthlyPayment)
                .map(HomeLoanBorrowingCapacityResponse::fromDomain)
                .fold(ResponseBuilder::fromFail, ResponseBuilder::ok);
    }
}
