package com.pelada_gestao.exception.custom;

public class EntidadeEmUsoException extends RuntimeException {
    public EntidadeEmUsoException(String mensagem) {
        super(mensagem);
    }
}
