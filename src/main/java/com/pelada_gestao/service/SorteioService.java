package com.pelada_gestao.service;

import com.pelada_gestao.model.Jogador;
import com.pelada_gestao.model.Sorteio;

import java.util.List;
import java.util.Map;

public interface SorteioService {

    Sorteio salvar(Sorteio sorteio);

    List<Sorteio> listarTodos();

    void deletarPorId(Long id);

    Map<String, List<Jogador>> sortearTimes(Long id);

    Map<String, List<Jogador>> resultadoDoSorteio(Long id);

}
