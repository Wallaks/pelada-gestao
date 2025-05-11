package com.pelada_gestao.service;

import com.pelada_gestao.enuns.TimeEnum;
import com.pelada_gestao.model.JogadorSorteado;
import com.pelada_gestao.model.Sorteio;
import com.pelada_gestao.repository.SorteioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SorteioService {

    @Autowired
    private SorteioRepository sorteioRepository;

    private static final Logger logger = LoggerFactory.getLogger(SorteioService.class);

    public Sorteio salvar(Sorteio sorteio) {
        try {
            sortear(sorteio);
            return sorteioRepository.save(sorteio);
        } catch (Exception e) {
            logger.error("Erro ao salvar sorteio: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Sorteio> listarTodos() {
        return sorteioRepository.findAll(Sort.by(Sort.Direction.DESC, "data"));
    }

    public void sortear(Sorteio sorteioRecebido) {
        List<JogadorSorteado> jogadoresRecebidos = sorteioRecebido.getJogadores();

        if (jogadoresRecebidos.size() > 28) {
            throw new IllegalArgumentException("Máximo de 28 jogadores permitidos.");
        }

        List<JogadorSorteado> goleiros = new ArrayList<>();
        List<JogadorSorteado> jogadoresLinha = new ArrayList<>();

        for (JogadorSorteado j : jogadoresRecebidos) {
            if (Boolean.TRUE.equals(j.getGoleiro())) {
                goleiros.add(j);
            } else {
                jogadoresLinha.add(j);
            }
        }

        Collections.shuffle(goleiros);
        Collections.shuffle(jogadoresLinha);

        List<JogadorSorteado> timeAzul = new ArrayList<>();
        List<JogadorSorteado> timeVermelho = new ArrayList<>();
        List<JogadorSorteado> timeReserva = new ArrayList<>();

        timeAzul.add(definirGoleiro(goleiros, jogadoresLinha, TimeEnum.AZUL));
        timeVermelho.add(definirGoleiro(goleiros, jogadoresLinha, TimeEnum.VERMELHO));

        preencherTime(timeAzul, jogadoresLinha, TimeEnum.AZUL);
        preencherTime(timeVermelho, jogadoresLinha, TimeEnum.VERMELHO);
        preencherTimeReserva(timeReserva, jogadoresLinha, goleiros);

        sorteioRecebido.setData(LocalDate.now());

        List<JogadorSorteado> todos = new ArrayList<>();
        todos.addAll(timeAzul);
        todos.addAll(timeVermelho);
        todos.addAll(timeReserva);

        for (JogadorSorteado j : todos) {
            j.setSorteio(sorteioRecebido);
        }

        sorteioRecebido.setJogadores(todos);
    }

    private JogadorSorteado definirGoleiro(List<JogadorSorteado> goleiros, List<JogadorSorteado> jogadoresLinha, TimeEnum time) {
        JogadorSorteado escolhido = !goleiros.isEmpty() ? goleiros.remove(0) : jogadoresLinha.remove(0);
        escolhido.setGoleiro(true);
        escolhido.setTime(time);
        return escolhido;
    }

    private void preencherTime(List<JogadorSorteado> time, List<JogadorSorteado> jogadoresLinha, TimeEnum timeEnum) {
        while (time.size() < 7 && !jogadoresLinha.isEmpty()) {
            JogadorSorteado jogador = jogadoresLinha.remove(0);
            jogador.setGoleiro(false);
            jogador.setTime(timeEnum);
            time.add(jogador);
        }
    }

    private void preencherTimeReserva(List<JogadorSorteado> timeReserva, List<JogadorSorteado> jogadoresLinha, List<JogadorSorteado> goleiros) {

        if (!goleiros.isEmpty() || !jogadoresLinha.isEmpty()) {
            JogadorSorteado goleiroReserva = definirGoleiro(goleiros, jogadoresLinha, TimeEnum.RESERVA);
            timeReserva.add(goleiroReserva);
        }

        while (!jogadoresLinha.isEmpty()) {
            JogadorSorteado jogador = jogadoresLinha.remove(0);
            jogador.setGoleiro(false);
            jogador.setTime(TimeEnum.RESERVA);
            timeReserva.add(jogador);
        }
    }


}