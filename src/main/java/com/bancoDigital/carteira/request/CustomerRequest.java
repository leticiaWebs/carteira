package com.bancoDigital.carteira.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest implements Serializable {

    @NotBlank(message = "Documento é obrigatório")
    private String document;

    @NotBlank(message = "Nome é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 números")
    private String name;


}
