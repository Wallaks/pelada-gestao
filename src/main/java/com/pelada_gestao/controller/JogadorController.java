package com.pelada_gestao.controller;

import com.pelada_gestao.model.Jogador;
import com.pelada_gestao.service.JogadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jogadores")
public class JogadorController {

    private final JogadorService jogadorService;

    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    @PostMapping
    public ResponseEntity<Jogador> criar(@RequestBody Jogador jogador) {
        if (jogador.getSorteioId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Jogador salvo = jogadorService.salvar(jogador);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Jogador>> listar() {
        return ResponseEntity.ok(jogadorService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        jogadorService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}