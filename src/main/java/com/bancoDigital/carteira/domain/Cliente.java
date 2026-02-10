package com.bancoDigital.carteira.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbCliente")
public class Cliente {

    @Id
    private String documento;
    private String nome;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cliente(String documento, String nome) {
        this.documento = documento;
        this.nome = nome;
    }


    public Cliente (){

    }

    public Cliente(Cliente cliente) {
    }
}
