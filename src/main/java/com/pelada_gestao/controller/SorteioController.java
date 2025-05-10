package com.pelada_gestao.controller;

import com.pelada_gestao.model.Sorteio;
import com.pelada_gestao.service.SorteioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sorteios")
public class SorteioController {

    @Autowired
    private SorteioService sorteioService;

    @PostMapping
    public ResponseEntity<Sorteio> criar(@RequestBody Sorteio sorteio) {
        Sorteio salvo = sorteioService.salvar(sorteio);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Sorteio>> listar() {
        return ResponseEntity.ok(sorteioService.listarTodos());
    }
}
