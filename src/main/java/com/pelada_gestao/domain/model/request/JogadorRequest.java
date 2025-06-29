package com.pelada_gestao.domain.model.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JogadorRequest {

    @Size(max = 15)
    private String nome;
    private boolean goleiro;
    private LocalDate data;
    private Long sorteioId;

}
