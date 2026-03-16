package com.bancoDigital.carteira.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbBankStatement")
public class BankStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String operationType;
    private BigDecimal value;
    private LocalDateTime dateTime;
    private String operationDescription;

    @ManyToOne
    @JoinColumn(name = "tbAccount")
    private Account account;

}
