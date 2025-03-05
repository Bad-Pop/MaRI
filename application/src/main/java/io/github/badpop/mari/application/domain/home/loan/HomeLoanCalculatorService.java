package io.github.badpop.mari.application.domain.home.loan;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.control.MariFail.HomeLoanBorrowingCapacityFail;
import io.github.badpop.mari.application.domain.control.MariFail.HomeLoanMonthlyPaymentFail;
import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanBorrowingCapacity;
import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanMonthlyPayment;
import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanTermUnit;
import io.github.badpop.mari.application.domain.home.loan.port.HomeLoanCalculatorApi;
import io.quarkus.logging.Log;
import io.vavr.control.Either;

import java.math.BigDecimal;
import java.time.Period;

import static io.vavr.API.Try;
import static java.math.RoundingMode.HALF_UP;

public class HomeLoanCalculatorService implements HomeLoanCalculatorApi {

    @Override
    public Either<MariFail, HomeLoanMonthlyPayment> computeMonthlyPayment(double borrowedAmount,
                                                                          double annualInterestRate,
                                                                          int term,
                                                                          HomeLoanTermUnit termUnit) {

        return Try(() -> {
            var monthlyPaymentCount = computeMonthlyPaymentCount(term, termUnit);

            var monthlyPaymentAmount = computeMonthlyPaymentAmount(borrowedAmount, annualInterestRate, monthlyPaymentCount);
            var interestCost = computeInterestCost(monthlyPaymentAmount, monthlyPaymentCount, borrowedAmount);
            return new HomeLoanMonthlyPayment(monthlyPaymentAmount, interestCost);
        })
                .onFailure(t ->
                        Log.errorv(t, "An error occurred while computing the monthly payment. Borrowed amount: {0}, annual interest rate: {1}, term: {2}, term unit: {3}",
                                borrowedAmount,
                                annualInterestRate,
                                term,
                                termUnit))
                .toEither(() -> new HomeLoanMonthlyPaymentFail(borrowedAmount, annualInterestRate, term, termUnit));
    }

    @Override
    public Either<MariFail, HomeLoanBorrowingCapacity> computeBorrowingCapacity(double annualInterestRate,
                                                                                int term,
                                                                                HomeLoanTermUnit termUnit,
                                                                                double monthlyPayment) {
        return Try(() -> {
            var monthlyPaymentCount = computeMonthlyPaymentCount(term, termUnit);

            var capacityAmount = computeBorrowingCapacityAmount(annualInterestRate, monthlyPayment, monthlyPaymentCount);
            var interestCost = computeInterestCost(monthlyPayment, monthlyPaymentCount, capacityAmount);
            return new HomeLoanBorrowingCapacity(capacityAmount, interestCost);
        })
                .onFailure(t -> Log.errorv(t, "An error occurred while computing the borrowing capacity. Annual rate: {0}, term: {1}, term unit: {2}, monthly payment: {3}",
                        annualInterestRate,
                        term,
                        termUnit,
                        monthlyPayment))
                .toEither(() -> new HomeLoanBorrowingCapacityFail(annualInterestRate, term, termUnit, monthlyPayment));
    }

    private long computeMonthlyPaymentCount(int term, HomeLoanTermUnit termUnit) {
        return switch (termUnit) {
            case YEARS -> Period.ofYears(term).normalized().toTotalMonths();
            case MONTHS -> term;
        };
    }

    private double computeMonthlyPaymentAmount(double borrowedAmount, double annualInterestRate, long monthlyPaymentCount) {
        if(annualInterestRate == 0) {
            return round(borrowedAmount / monthlyPaymentCount);
        }

        var monthlyRate = yearlyInterestRateToMonthlyRate(annualInterestRate);
        return round((borrowedAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -monthlyPaymentCount)));
    }

    private double computeInterestCost(double monthlyPaymentAmount, long monthlyPaymentCount, double borrowedAmount) {
        return round((monthlyPaymentAmount * monthlyPaymentCount) - borrowedAmount);
    }

    private double yearlyInterestRateToMonthlyRate(double annualInterestRate) {
        return (annualInterestRate / 100) / 12;
    }

    private double computeBorrowingCapacityAmount(double annualInterestRate, double monthlyPayment, long monthlyPaymentCount) {
        var monthlyRate = yearlyInterestRateToMonthlyRate(annualInterestRate);
        if (monthlyRate == 0) {
            return round(monthlyPayment * monthlyPaymentCount);
        }

        return round((monthlyPayment * (1 - Math.pow(1 + monthlyRate, -monthlyPaymentCount))) / (monthlyRate));
    }

    private double round(double value) {
        return new BigDecimal(value).setScale(2, HALF_UP).doubleValue();
    }
}
