package com.pelada_gestao.service;

import com.pelada_gestao.enuns.TimeEnum;
import com.pelada_gestao.model.JogadorSorteado;
import com.pelada_gestao.model.Sorteio;
import com.pelada_gestao.repository.JogadorSorteadoRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(SorteioService.class);

    @Autowired
    private SorteioRepository sorteioRepository;

    @Autowired
    private JogadorSorteadoRepository jogadorRepository;

    public Sorteio salvar(Sorteio sorteio) {
        try {
            validarSorteio(sorteio);
            sortear(sorteio);

            Sorteio salvo = sorteioRepository.save(sorteio);

            sorteio.getJogadores().forEach(jogador -> {
                jogador.setSorteio(salvo);
                jogadorRepository.save(jogador);
            });

            return salvo;
        } catch (IllegalArgumentException e) {
            logger.warn("Erro de validação ao salvar sorteio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao salvar sorteio: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Sorteio> listarTodos() {
        return sorteioRepository.findAll(Sort.by(Sort.Direction.DESC, "data"));
    }

    private void validarSorteio(Sorteio sorteio) {
        if (sorteio.getJogadores() == null || sorteio.getJogadores().isEmpty()) {
            throw new IllegalArgumentException("A lista de jogadores não pode estar vazia.");
        }
        if (sorteio.getJogadores().size() > 28) {
            throw new IllegalArgumentException("Máximo de 28 jogadores permitidos.");
        }
    }

    public void sortear(Sorteio sorteioRecebido) {
        List<JogadorSorteado> jogadoresRecebidos = new ArrayList<>(sorteioRecebido.getJogadores());

        List<JogadorSorteado> goleiros = filtrarGoleiros(jogadoresRecebidos);
        List<JogadorSorteado> jogadoresLinha = filtrarJogadoresLinha(jogadoresRecebidos);

        Collections.shuffle(goleiros);
        Collections.shuffle(jogadoresLinha);

        List<JogadorSorteado> timeAzul = criarTime(goleiros, jogadoresLinha, TimeEnum.AZUL);
        List<JogadorSorteado> timeVermelho = criarTime(goleiros, jogadoresLinha, TimeEnum.VERMELHO);
        List<JogadorSorteado> timeReserva = criarTimeReserva(goleiros, jogadoresLinha);

        List<JogadorSorteado> todos = new ArrayList<>();
        todos.addAll(timeAzul);
        todos.addAll(timeVermelho);
        todos.addAll(timeReserva);

        todos.forEach(jogador -> jogador.setSorteio(sorteioRecebido));

        sorteioRecebido.setData(LocalDate.now());
        sorteioRecebido.setJogadores(todos);
    }

    private List<JogadorSorteado> filtrarGoleiros(List<JogadorSorteado> jogadores) {
        List<JogadorSorteado> goleiros = new ArrayList<>();
        jogadores.removeIf(jogador -> {
            if (Boolean.TRUE.equals(jogador.getGoleiro())) {
                goleiros.add(jogador);
                return true;
            }
            return false;
        });
        return goleiros;
    }

    private List<JogadorSorteado> filtrarJogadoresLinha(List<JogadorSorteado> jogadores) {
        return new ArrayList<>(jogadores);
    }

    private List<JogadorSorteado> criarTime(List<JogadorSorteado> goleiros, List<JogadorSorteado> jogadoresLinha, TimeEnum time) {
        List<JogadorSorteado> timeCriado = new ArrayList<>();
        timeCriado.add(definirGoleiro(goleiros, jogadoresLinha, time));
        preencherTime(timeCriado, jogadoresLinha, time);
        return timeCriado;
    }

    private List<JogadorSorteado> criarTimeReserva(List<JogadorSorteado> goleiros, List<JogadorSorteado> jogadoresLinha) {
        List<JogadorSorteado> timeReserva = new ArrayList<>();
        if (!goleiros.isEmpty() || !jogadoresLinha.isEmpty()) {
            timeReserva.add(definirGoleiro(goleiros, jogadoresLinha, TimeEnum.RESERVA));
        }
        jogadoresLinha.forEach(jogador -> {
            jogador.setTime(TimeEnum.RESERVA);
            timeReserva.add(jogador);
        });
        return timeReserva;
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
            jogador.setTime(timeEnum);
            time.add(jogador);
        }
    }
}