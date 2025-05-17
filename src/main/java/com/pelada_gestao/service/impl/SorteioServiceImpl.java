package com.pelada_gestao.service.impl;

import com.pelada_gestao.model.Sorteio;
import com.pelada_gestao.repository.SorteioRepository;
import com.pelada_gestao.service.SorteioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SorteioServiceImpl implements SorteioService {

    @Autowired
    private SorteioRepository repository;

    @Override
    public Sorteio salvar(Sorteio sorteio) {
        return repository.save(sorteio);
    }

    @Override
    public List<Sorteio> listarTodos() {
        return repository.findAll();
    }

    @Override
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Sorteio buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Sorteio não encontrado"));
    }
}
