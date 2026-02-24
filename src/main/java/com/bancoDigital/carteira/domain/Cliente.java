package com.bancoDigital.carteira.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbCliente")
public class Cliente {

    @Id
    private String documento;
    private String nome;

    public Cliente(String documento, String nome) {
        this.documento = documento;
        this.nome = nome;
    }


    public Cliente(@Valid Cliente cliente) {
    }
}
