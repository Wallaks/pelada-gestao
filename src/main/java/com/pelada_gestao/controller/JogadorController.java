package com.pelada_gestao.controller;

import com.pelada_gestao.model.Jogador;
import com.pelada_gestao.service.JogadorService;
import com.pelada_gestao.service.SorteioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {

    @Autowired
    private JogadorService jogadorService;

    @Autowired
    private SorteioService sorteioService;

    @PostMapping
    public ResponseEntity<Jogador> criar(@RequestBody Jogador jogador) {
        Long sorteioId = jogador.getSorteio() != null ? jogador.getSorteio().getId() : null;
        if (sorteioId == null) {
            return ResponseEntity.badRequest().build();
        }

        jogador.setSorteio(sorteioService.buscarPorId(sorteioId));
        Jogador salvo = jogadorService.salvar(jogador);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Jogador>> listarTodos() {
        return ResponseEntity.ok(jogadorService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        jogadorService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
