package com.pelada_gestao.dto;

import java.time.LocalDate;

public class JogadorResponseDTO {
    private Long id;
    private String nome;
    private boolean goleiro;
    private LocalDate data;
    private Long sorteioId;

    public JogadorResponseDTO(Long id, String nome, boolean goleiro, LocalDate data, Long sorteioId) {
        this.id = id;
        this.nome = nome;
        this.goleiro = goleiro;
        this.data = data;
        this.sorteioId = sorteioId;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public boolean isGoleiro() { return goleiro; }
    public LocalDate getData() { return data; }
    public Long getSorteioId() { return sorteioId; }
}
