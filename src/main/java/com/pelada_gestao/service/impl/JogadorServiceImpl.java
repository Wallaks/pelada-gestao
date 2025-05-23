package com.pelada_gestao.service.impl;

import com.pelada_gestao.model.Jogador;
import com.pelada_gestao.repository.JogadorRepository;
import com.pelada_gestao.service.JogadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JogadorServiceImpl implements JogadorService {

    @Autowired
    private JogadorRepository repository;

    @Override
    public Jogador salvar(Jogador jogador) {
        return repository.save(jogador);
    }

    @Override
    public List<Jogador> listarTodos() {
        return repository.findAll();
    }

    @Override
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}
