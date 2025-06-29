package com.pelada_gestao.service;

import com.pelada_gestao.domain.model.Jogador;
import com.pelada_gestao.domain.model.Sorteio;

import java.util.List;
import java.util.Map;

public interface SorteioService {

    Sorteio buscarPorId(Long id);

    Sorteio salvar(Sorteio sorteio);

    List<Sorteio> listarTodos();

    void deletarPorId(Long id);

    Map<String, List<Jogador>> sortearTimes(Long id);

    Map<String, List<Jogador>> resultadoDoSorteio(Long id);

    boolean existsByNomeAndCadastradoPor(String nome, String cadastradoPor);

}
