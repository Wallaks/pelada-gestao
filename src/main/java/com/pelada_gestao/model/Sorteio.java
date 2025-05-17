package com.pelada_gestao.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sorteio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Integer jogadoresPorEquipe;

    private LocalDate data;

    @OneToMany(mappedBy = "sorteio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jogador> jogadores = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getJogadoresPorEquipe() {
        return jogadoresPorEquipe;
    }

    public void setJogadoresPorEquipe(Integer jogadoresPorEquipe) {
        this.jogadoresPorEquipe = jogadoresPorEquipe;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }
}
