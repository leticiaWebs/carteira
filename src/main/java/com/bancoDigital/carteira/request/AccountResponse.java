package com.bancoDigital.carteira.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponse {

    private BigDecimal balance;

    public AccountResponse(BigDecimal balance) {
        this.balance = balance;

    }
}
