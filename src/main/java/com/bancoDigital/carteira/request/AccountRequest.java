package com.bancoDigital.carteira.request;

import com.bancoDigital.carteira.domain.Account;
import com.bancoDigital.carteira.domain.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest implements Serializable {

    private int id;
    private String accountNumber;
    private String agencyNumber;
    @Pattern(regexp = "^0$", message = "O saldo inicial deve ser zero")
    private BigDecimal balance;
    private LocalDateTime createdAt;
    @Valid
    private Customer customer;

    public AccountRequest(int id, String accountNumber, String agencyNumber, BigDecimal balance, LocalDateTime createdAt, Customer customer) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.agencyNumber = agencyNumber;
        this.balance = balance;
        this.createdAt = createdAt;
        this.customer = customer;
    }

    @Valid
    public AccountRequest(Account entity) {
        this.id = entity.getId();
        this.accountNumber = entity.getAccountNumber();
        this.agencyNumber = entity.getAgencyNumber();
        this.balance = entity.getBalance();
        this.createdAt = entity.getCreatedAt();
        this.customer = entity.getCustomer();
    }

}
