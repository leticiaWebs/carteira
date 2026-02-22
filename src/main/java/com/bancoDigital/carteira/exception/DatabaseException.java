package com.bancoDigital.carteira.exception;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String msg){
        super(msg);
    }
}
