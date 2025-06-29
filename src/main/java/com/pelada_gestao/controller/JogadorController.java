package com.pelada_gestao.controller;

import com.pelada_gestao.domain.model.Jogador;
import com.pelada_gestao.domain.model.request.JogadorRequest;
import com.pelada_gestao.exception.custom.AcessoNegadoException;
import com.pelada_gestao.exception.custom.NomeDeJogadorDuplicadoException;
import com.pelada_gestao.service.JogadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final JogadorService jogadorService;

    public JogadorController(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    @PostMapping
    public ResponseEntity<Jogador> criar(@RequestBody JogadorRequest jogadorRequest) {
        if (jogadorRequest.getSorteioId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (jogadorService.existsByNomeAndSorteioId(jogadorRequest.getNome(), jogadorRequest.getSorteioId())) {
            throw new NomeDeJogadorDuplicadoException("Já existe um jogador com esse nome neste sorteio.");
        }

        Jogador jogador = new Jogador();
        jogador.setNome(jogadorRequest.getNome());
        jogador.setGoleiro(jogadorRequest.isGoleiro());
        jogador.setData(jogadorRequest.getData());
        jogador.setSorteioId(jogadorRequest.getSorteioId());
        jogador.setCadastradoPor(SecurityContextHolder.getContext().getAuthentication().getName());

        Jogador salvo = jogadorService.salvar(jogador);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Jogador>> listar() {
        return ResponseEntity.ok(jogadorService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        String usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Jogador jogador = jogadorService.buscarPorId(id);

        if (!usuarioLogado.equals(jogador.getCadastradoPor())) {
            throw new AcessoNegadoException("Você não pode excluir este jogador");
        }

        jogadorService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}