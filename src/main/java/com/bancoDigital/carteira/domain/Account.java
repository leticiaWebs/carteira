package com.bancoDigital.carteira.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbAccount")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String accountNumber;
    private String agencyNumber;
    @Column(name = "balance")
    private BigDecimal balance;
    private OffsetDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "tbCliente")
    @Valid
    private Customer customer;

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        this.balance = this.balance.add(amount);
    }


    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");
        if (this.balance.compareTo(amount) < 0)
            throw new RuntimeException("Insufficient balance");
        this.balance = this.balance.subtract(amount);
    }

}
