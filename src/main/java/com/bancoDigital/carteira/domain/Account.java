package com.bancoDigital.carteira.domain;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private BigDecimal balance;
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "tbCliente")
    @Valid
    private Customer customer;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
