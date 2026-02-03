package com.bancoDigital.carteira.dto;

import com.bancoDigital.carteira.domain.Cliente;
import com.bancoDigital.carteira.domain.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto implements Serializable {
    private String documento;
    private String nome;

    public ClienteDto(Cliente entity) {
        this.documento = entity.getDocumento();
        this.nome = entity.getNome();
    }

}
