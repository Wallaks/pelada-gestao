package com.pelada_gestao.service.impl;

import com.pelada_gestao.enuns.TimeEnum;
import com.pelada_gestao.domain.model.Jogador;
import com.pelada_gestao.domain.model.JogadorSorteado;
import com.pelada_gestao.domain.model.Sorteio;
import com.pelada_gestao.repository.JogadorRepository;
import com.pelada_gestao.repository.JogadorSorteadoRepository;
import com.pelada_gestao.repository.SorteioRepository;
import com.pelada_gestao.service.SorteioService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SorteioServiceImpl implements SorteioService {

    private final SorteioRepository repository;
    private final JogadorRepository jogadorRepository;
    private final JogadorSorteadoRepository jogadorSorteadoRepository;

    public SorteioServiceImpl(SorteioRepository repository, JogadorRepository jogadorRepository, JogadorSorteadoRepository jogadorSorteadoRepository) {
        this.repository = repository;
        this.jogadorRepository = jogadorRepository;
        this.jogadorSorteadoRepository = jogadorSorteadoRepository;
    }

    @Override
    public Sorteio salvar(Sorteio sorteio) {
        return repository.save(sorteio);
    }

    @Override
    public List<Sorteio> listarTodos() {
        return repository.findAll();
    }

    @Override
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Map<String, List<Jogador>> sortearTimes(Long id) {
        Sorteio sorteio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sorteio não encontrado"));

        if (sorteio.isSorteado()) {
            throw new RuntimeException("Este sorteio já foi realizado e não pode ser feito novamente.");
        }

        List<Jogador> jogadores = jogadorRepository.findBySorteioId(id);
        Collections.shuffle(jogadores);

        List<Jogador> goleiros = new ArrayList<>();
        List<Jogador> naoGoleiros = new ArrayList<>();
        for (Jogador jogador : jogadores) {
            if (jogador.isGoleiro()) {
                goleiros.add(jogador);
            } else {
                naoGoleiros.add(jogador);
            }
        }

        List<Jogador> timeAzul = new ArrayList<>();
        List<Jogador> timeVermelho = new ArrayList<>();
        List<Jogador> reservas = new ArrayList<>();
        int qtdPorTime = sorteio.getJogadoresPorEquipe();

        if (goleiros.size() >= 2) {
            Jogador goleiroAzul = goleiros.get(0);
            Jogador goleiroVermelho = goleiros.get(1);
            timeAzul.add(goleiroAzul);
            timeVermelho.add(goleiroVermelho);

            for (int i = 2; i < goleiros.size(); i++) {
                reservas.add(goleiros.get(i));
            }
        } else if (goleiros.size() == 1) {
            Jogador unicoGoleiro = goleiros.get(0);
            timeAzul.add(unicoGoleiro);
        }

        for (Jogador jogador : naoGoleiros) {
            if (timeAzul.size() < qtdPorTime) {
                timeAzul.add(jogador);
            } else if (timeVermelho.size() < qtdPorTime) {
                timeVermelho.add(jogador);
            } else {
                reservas.add(jogador);
            }
        }

        salvarSorteioEmLote(timeAzul, id, TimeEnum.AZUL);
        salvarSorteioEmLote(timeVermelho, id, TimeEnum.VERMELHO);
        salvarSorteioEmLote(reservas, id, TimeEnum.RESERVA);

        sorteio.setSorteado(true);
        repository.save(sorteio);

        Map<String, List<Jogador>> resultado = new HashMap<>();
        resultado.put("timeAzul", timeAzul);
        resultado.put("timeVermelho", timeVermelho);
        resultado.put("reservas", reservas);

        return resultado;
    }

    private boolean podeAdicionarNoTime(List<Jogador> time, int limite, Jogador jogador, boolean jaTemGoleiro) {
        if (time.size() >= limite) return false;
        return !jogador.isGoleiro() || !jaTemGoleiro;
    }

    private void salvarSorteioEmLote(List<Jogador> jogadores, Long sorteioId, TimeEnum time) {
        List<JogadorSorteado> lista = new ArrayList<>();
        for (Jogador jogador : jogadores) {
            JogadorSorteado js = new JogadorSorteado();
            js.setJogadorId(jogador.getId());
            js.setSorteioId(sorteioId);
            js.setTime(time);
            js.setData(LocalDate.now());
            lista.add(js);
        }
        jogadorSorteadoRepository.saveAll(lista);
    }

    public Map<String, List<Jogador>> resultadoDoSorteio(Long id) {
        Sorteio sorteio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sorteio não encontrado"));

        if (!sorteio.isSorteado()) {
            throw new RuntimeException("Sorteio ainda não foi realizado.");
        }

        List<JogadorSorteado> sorteados = jogadorSorteadoRepository.findBySorteioId(id);

        List<Jogador> timeAzul = new ArrayList<>();
        List<Jogador> timeVermelho = new ArrayList<>();
        List<Jogador> reservas = new ArrayList<>();

        for (JogadorSorteado js : sorteados) {
            Jogador jogador = jogadorRepository.findById(js.getJogadorId())
                    .orElseThrow(() -> new RuntimeException("Jogador não encontrado: id " + js.getJogadorId()));

            switch (js.getTime()) {
                case AZUL:
                    timeAzul.add(jogador);
                    break;
                case VERMELHO:
                    timeVermelho.add(jogador);
                    break;
                case RESERVA:
                    reservas.add(jogador);
                    break;
            }
        }

        Map<String, List<Jogador>> resultado = new HashMap<>();
        resultado.put("timeAzul", timeAzul);
        resultado.put("timeVermelho", timeVermelho);
        resultado.put("reservas", reservas);

        return resultado;
    }
}
