package com.bancoDigital.carteira.request;

import com.bancoDigital.carteira.domain.Cliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Valid
public class ClienteRequest implements Serializable {

    @NotBlank(message = "Nome é obrigatório")
    private String documento;

    @NotBlank(message = "Documento é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números")
    private String nome;

    public ClienteRequest(Cliente entity) {
        this.documento = entity.getDocumento();
        this.nome = entity.getNome();
    }

    public ClienteRequest(String documento, String nome) {
        this.documento = documento;
        this.nome = nome;
    }
}
