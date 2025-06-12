package com.pelada_gestao.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JogadorRequest {

    private String nome;

    private boolean goleiro;

    private LocalDate data;

    private Long sorteioId;

}
