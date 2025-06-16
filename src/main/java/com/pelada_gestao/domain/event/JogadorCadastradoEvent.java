package com.pelada_gestao.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JogadorCadastradoEvent {

    private String nomeJogador;
    private boolean goleiro;
    private LocalDate dataCadastro;
    private String nomeSorteio;


}
