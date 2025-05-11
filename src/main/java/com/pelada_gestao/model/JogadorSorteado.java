package com.pelada_gestao.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pelada_gestao.enuns.TimeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "jogador_sorteado")
public class JogadorSorteado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Boolean goleiro;

    @Enumerated(EnumType.STRING)
    private TimeEnum time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sorteio_id")
    @JsonBackReference
    private Sorteio sorteio;

}
