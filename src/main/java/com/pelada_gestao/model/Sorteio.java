package com.pelada_gestao.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Sorteio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20)
    private String nome;

    private Integer jogadoresPorEquipe;

    private LocalDate data;

    private boolean sorteado = false;


}
