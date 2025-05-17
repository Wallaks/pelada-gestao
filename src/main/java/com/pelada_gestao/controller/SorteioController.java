package com.pelada_gestao.controller;

import com.pelada_gestao.model.Sorteio;
import com.pelada_gestao.service.SorteioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sorteios")
public class SorteioController {

    @Autowired
    private SorteioService sorteioService;

    @PostMapping
    public ResponseEntity<Sorteio> criarSorteio(@RequestBody Sorteio sorteio) {
        Sorteio salvo = sorteioService.salvar(sorteio);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Sorteio>> listarTodos() {
        return ResponseEntity.ok(sorteioService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        sorteioService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
