package com.pelada_gestao.exception.custom;

public class NomeDeJogadorDuplicadoException extends RuntimeException {

    public NomeDeJogadorDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
