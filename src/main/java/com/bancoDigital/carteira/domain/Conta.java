package com.bancoDigital.carteira.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbAccount")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String numeroConta;
    private String numeroAgencia;
    private double saldo;
    private LocalDateTime dataCriacao;

    @OneToOne
    @JoinColumn(name = "tbCliente")
    @Valid
    private Cliente cliente;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

}
