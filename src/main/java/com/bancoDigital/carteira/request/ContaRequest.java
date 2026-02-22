package com.bancoDigital.carteira.request;

import com.bancoDigital.carteira.domain.Cliente;
import com.bancoDigital.carteira.domain.Conta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ContaRequest implements Serializable {

    private int id;

    private String numeroConta;
    private String numeroAgencia;
    @Pattern(regexp = "^0$", message = "O saldo inicial deve ser zero")
    private double saldo;
    private LocalDateTime dataCriacao;
    @Valid
    private Cliente cliente;


    public ContaRequest(int id, String numeroConta, String numeroAgencia, Double saldo, LocalDateTime dataCriacao, Cliente cliente) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.numeroAgencia = numeroAgencia;
        this.saldo = saldo;
        this.dataCriacao = dataCriacao;
        this.cliente = cliente;
    }

    @Valid
    public ContaRequest(Conta entity) {
        this.id = entity.getId();
        this.numeroConta = entity.getNumeroConta();
        this.numeroAgencia = entity.getNumeroAgencia();
        this.saldo = entity.getSaldo();
        this.dataCriacao = entity.getDataCriacao();
        this.cliente = entity.getCliente();

        if (entity.getCliente() != null) {
            this.cliente = new Cliente(entity.getCliente());

        }
    }

}
