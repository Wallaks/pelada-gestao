package com.pelada_gestao.service;

import com.pelada_gestao.model.Sorteio;

import java.util.List;

public interface SorteioService {
    Sorteio salvar(Sorteio sorteio);
    List<Sorteio> listarTodos();
    void deletarPorId(Long id);
    Sorteio buscarPorId(Long id);
}
