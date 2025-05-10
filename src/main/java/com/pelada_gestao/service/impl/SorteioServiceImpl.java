package com.pelada_gestao.service.impl;

import com.pelada_gestao.enuns.TimeEnum;
import com.pelada_gestao.model.Jogador;
import com.pelada_gestao.model.JogadorSorteado;
import com.pelada_gestao.model.Sorteio;
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

    public Map<String, List<Jogador>> sortearTimes(Long id) {
        Sorteio sorteio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sorteio não encontrado"));

        if (sorteio.isSorteado()) {
            throw new RuntimeException("Este sorteio já foi realizado e não pode ser feito novamente.");
        }

        List<Jogador> jogadores = jogadorRepository.findBySorteioId(id);
        Collections.shuffle(jogadores);

        List<Jogador> timeAzul = new ArrayList<>();
        List<Jogador> timeVermelho = new ArrayList<>();
        List<Jogador> reservas = new ArrayList<>();

        int qtdPorTime = sorteio.getJogadoresPorEquipe();

        for (Jogador jogador : jogadores) {
            if (timeAzul.size() < qtdPorTime) {
                timeAzul.add(jogador);
                salvarSorteio(jogador, id, TimeEnum.AZUL);
            } else if (timeVermelho.size() < qtdPorTime) {
                timeVermelho.add(jogador);
                salvarSorteio(jogador, id, TimeEnum.VERMELHO);
            } else {
                reservas.add(jogador);
                salvarSorteio(jogador, id, TimeEnum.RESERVA);
            }
        }

        sorteio.setSorteado(true);
        repository.save(sorteio);

        Map<String, List<Jogador>> resultado = new HashMap<>();
        resultado.put("timeAzul", timeAzul);
        resultado.put("timeVermelho", timeVermelho);
        resultado.put("reservas", reservas);

        return resultado;
    }

    private void salvarSorteio(Jogador jogador, Long sorteioId, TimeEnum time) {
        JogadorSorteado js = new JogadorSorteado();
        js.setJogadorId(jogador.getId());
        js.setSorteioId(sorteioId);
        js.setTime(time);
        js.setData(LocalDate.now());
        jogadorSorteadoRepository.save(js);
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
