package io.github.badpop.mari.application.domain.control;

import io.github.badpop.mari.application.domain.home.loan.model.HomeLoanTermUnit;
import io.github.badpop.mari.libraries.fails.Fail;
import io.vavr.control.Option;

import static io.github.badpop.mari.libraries.fails.Fail.Level.INFO;
import static io.vavr.API.Option;

public sealed interface MariFail extends Fail {

  record InvalidAdUrlFail(String invalidUrl) implements MariFail {
    @Override
    public String code() {
      return "INVALID_AD_URL_FAILURE";
    }

    @Override
    public String message() {
      return "Le lien fournit (%s) est invalide".formatted(invalidUrl);
    }
  }

  record ExpiredSharedAdFail() implements MariFail {
    @Override
    public String code() {
      return "EXPIRED_SHARED_AD_FAILURE";
    }

    @Override
    public String message() {
      return "Le partage de cette annonce est expiré. Rapprochez vous de la personne pour qu'elle vous partage cette annonce à nouveau.";
    }
  }

  record ForbiddenPatchOperationFail(String forbiddenOperationField) implements MariFail {
    @Override
    public String code() {
      return "FORBIDDEN_UPDATE_OPERATION_FAILURE";
    }

    @Override
    public String message() {
      return "Vous ne pouvez pas mettre à jour le champs %s".formatted(forbiddenOperationField);
    }
  }

  record InvalidRequestFail(String message) implements MariFail {
    @Override
    public String code() {
      return "INVALID_REQUEST_FAILURE";
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
      return "Une erreur est survenue pendant le calcul des mensualités de crédit avec les paramètres suivants : Montant emprunté %s, intérêts annuels : %s, durée du remboursement : %s et type de durée %s"
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

  record TechnicalFail(String message, Throwable causedBy) implements MariFail {
    @Override
    public String code() {
      return "TECHNICAL";
    }

    @Override
    public Option<Throwable> cause() {
      return Option(causedBy);
    }

    public static TechnicalFail of(String message) {
      return new TechnicalFail(message, null);
    }
  }

  record UnauthorizedFail() implements MariFail {
    @Override
    public String code() {
      return "UNAUTHORIZED_FAILURE";
    }

    @Override
    public String message() {
      return "Vous n'êtes pas autorisé à effectuer cette action...";
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
}
