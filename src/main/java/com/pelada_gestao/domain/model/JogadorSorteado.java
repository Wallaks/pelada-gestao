package com.pelada_gestao.domain.model;

import com.pelada_gestao.enuns.TimeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

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

