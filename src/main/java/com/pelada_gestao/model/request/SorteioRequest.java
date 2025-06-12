package com.pelada_gestao.model.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SorteioRequest {

    @Size(max = 20)
    private String nome;

    private Integer jogadoresPorEquipe;

    private LocalDate data;

    private boolean sorteado = false;

}
