package com.pelada_gestao.model;

import com.pelada_gestao.enuns.TimeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class JogadorSorteado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sorteioId;

    private Long jogadorId;

    @Enumerated(EnumType.STRING)
    private TimeEnum time;

    private LocalDate data;

}

