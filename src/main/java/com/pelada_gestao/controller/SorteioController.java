package com.pelada_gestao.controller;

import com.pelada_gestao.domain.model.Jogador;
import com.pelada_gestao.domain.model.Sorteio;
import com.pelada_gestao.domain.model.request.SorteioRequest;
import com.pelada_gestao.service.SorteioService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sorteios")
public class SorteioController {

    private final SorteioService sorteioService;

    public SorteioController(SorteioService sorteioService) {
        this.sorteioService = sorteioService;
    }

    @PostMapping
    public ResponseEntity<Sorteio> criar(@RequestBody SorteioRequest SorteioRequest) {
        Sorteio sorteio = new Sorteio();
        sorteio.setNome(SorteioRequest.getNome());
        sorteio.setJogadoresPorEquipe(SorteioRequest.getJogadoresPorEquipe());
        sorteio.setData(SorteioRequest.getData());
        sorteio.setSorteado(SorteioRequest.isSorteado());

        Sorteio salvo = sorteioService.salvar(sorteio);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Sorteio>> listar() {
        return ResponseEntity.ok(sorteioService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            sorteioService.deletarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não é possível excluir o sorteio: ele possui jogadores cadastrados.");
        }
    }


    @GetMapping("/sortear/{id}")
    public ResponseEntity<Map<String, List<Jogador>>> sortearTimes(@PathVariable Long id) {
        Map<String, List<Jogador>> resultado = sorteioService.sortearTimes(id);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/resultado/{id}")
    public ResponseEntity<Map<String, List<Jogador>>> resultadoSorteio(@PathVariable Long id) {
        Map<String, List<Jogador>> resultado = sorteioService.resultadoDoSorteio(id);
        return ResponseEntity.ok(resultado);
    }

}