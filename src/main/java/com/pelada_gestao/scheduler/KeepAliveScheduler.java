package com.pelada_gestao.scheduler;

import com.pelada_gestao.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeepAliveScheduler {

    private final JogadorRepository jogadorRepository;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void manterServicoAtivo() {
        try {
            boolean existe = jogadorRepository.existsById(1L);
            log.info("KeepAlive executado com sucesso. Status da consulta: {}", existe);
        } catch (Exception e) {
            log.warn("Falha no KeepAlive: {}", e.getMessage());
        }
    }
}
