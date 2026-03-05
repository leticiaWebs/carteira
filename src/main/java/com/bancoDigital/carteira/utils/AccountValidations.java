package com.bancoDigital.carteira.utils;

import com.bancoDigital.carteira.exception.InsufficientBalanceException;

import java.math.BigDecimal;

public class AccountValidations {

    public static void verifyPositiveValues(BigDecimal value) {
        if (value == null || value.signum() <= 0) {
            throw new IllegalArgumentException(
                    "O valor para essa operação deve ser maior que zero"
            );

        }
    }
    public static void validateSufficientBalance(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    String.format(
                            "Saldo insuficiente. Saldo: R$ %.2f, Valor solicitado: R$ %.2f", balance, amount
                    )
            );
        }
    }
}


