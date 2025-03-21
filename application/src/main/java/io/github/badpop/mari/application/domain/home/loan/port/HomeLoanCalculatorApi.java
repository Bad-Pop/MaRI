package io.github.badpop.mari.application.domain.home.loan.port;

import io.github.badpop.mari.application.domain.control.MariFail;
import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanBorrowingCapacity;
import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanMonthlyPayment;
import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanTermUnit;
import io.vavr.control.Either;

public interface HomeLoanCalculatorApi {

    /**
     * Calculer le coût des mensualités d'un crédit et le coût total des intérêts
     * @param borrowedAmount le somme de l'emprunt
     * @param annualInterestRate le taux annuel du prêt
     * @param term la durée du prêt
     * @param termUnit le adType de durée du prêt
     */
    Either<MariFail, HomeLoanMonthlyPayment> computeMonthlyPayment(double borrowedAmount,
                                                                   double annualInterestRate,
                                                                   int term,
                                                                   HomeLoanTermUnit termUnit);

    /**
     * Calculer la capacité d'emprunt et le coût total des intérêts
     * @param annualInterestRate le taux annuel du prêt
     * @param term la durée du prêt
     * @param termUnit la adType de durée
     * @param monthlyPayment le montant des mensualités
     */
    Either<MariFail, HomeLoanBorrowingCapacity> computeBorrowingCapacity(double annualInterestRate,
                                                                         int term,
                                                                         HomeLoanTermUnit termUnit,
                                                                         double monthlyPayment);
}
