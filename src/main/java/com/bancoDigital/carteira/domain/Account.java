package com.bancoDigital.carteira.domain;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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


}
