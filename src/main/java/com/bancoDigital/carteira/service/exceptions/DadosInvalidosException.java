package com.bancoDigital.carteira.service.exceptions;

public class DadosInvalidosException extends RuntimeException{
    public DadosInvalidosException(String msg){
        super(msg);
    }
}
