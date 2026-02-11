package com.bancoDigital.carteira.dto;

import com.bancoDigital.carteira.domain.Cliente;
import com.bancoDigital.carteira.domain.Conta;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^0$", message = "O saldo inicial deve ser zero")
    private String saldo;
    private LocalDateTime dataCriacao;
    @Valid
    private Cliente cliente;


    public ContaDto(int id, String numeroConta, String numeroAgencia, String saldo, LocalDateTime dataCriacao, Cliente cliente) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.numeroAgencia = numeroAgencia;
        this.saldo = saldo;
        this.dataCriacao = dataCriacao;
        this.cliente = cliente;
    }

    public ContaDto(Conta entity) {
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
