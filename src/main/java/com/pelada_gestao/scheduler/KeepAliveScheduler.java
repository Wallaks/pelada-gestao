package com.pelada_gestao.scheduler;

import com.pelada_gestao.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeepAliveScheduler {

    private final JogadorRepository jogadorRepository;

    @Value("${keepalive.jogador-id:1}")
    private Long jogadorId;

    @Scheduled(fixedRate = 30 * 1000)
    public void manterServicoAtivo() {
        long inicio = System.currentTimeMillis();
        try {
            boolean existe = jogadorRepository.existsById(jogadorId);
            long duracao = System.currentTimeMillis() - inicio;
            log.info("KeepAlive executado com sucesso. Status da consulta: {} ({} ms)", existe, duracao);
        } catch (Exception e) {
            log.warn("Falha no KeepAlive", e);
        }
    }
}