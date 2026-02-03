package com.bancoDigital.carteira.dto;

import com.bancoDigital.carteira.domain.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ContaDto implements Serializable {

    private int id;
    private String numeroConta;
    private String numeroAgencia;
    private String titular;
    private String saldo;
    private LocalDateTime dataCriacao;


    public ContaDto(int id, String numeroConta, String numeroAgencia, String titular, String saldo, LocalDateTime dataCriacao) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.numeroAgencia = numeroAgencia;
        this.titular = titular;
        this.saldo = saldo;
        this.dataCriacao = dataCriacao;
    }

    public ContaDto(Conta entity) {
        this.id = entity.getId();
        this.numeroConta = entity.getNumeroConta();
        this.numeroAgencia = entity.getNumeroAgencia();
        this.titular = entity.getTitular();
        this.saldo = entity.getSaldo();
        this.dataCriacao = entity.getDataCriacao();

    }

}
