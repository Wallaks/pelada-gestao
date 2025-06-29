package com.pelada_gestao.service;

import com.pelada_gestao.domain.model.Jogador;

import java.util.List;

public interface JogadorService {

    Jogador buscarPorId(Long id);

    Jogador salvar(Jogador jogador);

    List<Jogador> listarTodos();

    void deletarPorId(Long id);

    boolean existsByNomeAndSorteioId(String nome, Long sorteioId);
}
