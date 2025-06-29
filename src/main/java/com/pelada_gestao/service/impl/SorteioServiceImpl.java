package com.pelada_gestao.service.impl;

import com.pelada_gestao.domain.model.Jogador;
import com.pelada_gestao.domain.model.JogadorSorteado;
import com.pelada_gestao.domain.model.Sorteio;
import com.pelada_gestao.enuns.TimeEnum;
import com.pelada_gestao.exception.custom.OperacaoNaoPermitidaException;
import com.pelada_gestao.exception.custom.RecursoNaoEncontradoException;
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
    public Sorteio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sorteio não encontrado com id: " + id));
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
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sorteio não encontrado com id: " + id));

        if (sorteio.isSorteado()) {
            throw new OperacaoNaoPermitidaException("Este sorteio já foi realizado e não pode ser feito novamente.");
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
            timeAzul.add(goleiros.get(0));
            timeVermelho.add(goleiros.get(1));
            reservas.addAll(goleiros.subList(2, goleiros.size()));
        } else if (goleiros.size() == 1) {
            timeAzul.add(goleiros.get(0));
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

    public Map<String, List<Jogador>> resultadoDoSorteio(Long id) {
        Sorteio sorteio = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sorteio não encontrado com id: " + id));

        if (!sorteio.isSorteado()) {
            throw new OperacaoNaoPermitidaException("Sorteio ainda não foi realizado.");
        }

        List<JogadorSorteado> sorteados = jogadorSorteadoRepository.findBySorteioId(id);

        List<Jogador> timeAzul = new ArrayList<>();
        List<Jogador> timeVermelho = new ArrayList<>();
        List<Jogador> reservas = new ArrayList<>();

        for (JogadorSorteado js : sorteados) {
            Jogador jogador = jogadorRepository.findById(js.getJogadorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Jogador não encontrado com id: " + js.getJogadorId()));

            switch (js.getTime()) {
                case AZUL -> timeAzul.add(jogador);
                case VERMELHO -> timeVermelho.add(jogador);
                case RESERVA -> reservas.add(jogador);
            }
        }

        Map<String, List<Jogador>> resultado = new HashMap<>();
        resultado.put("timeAzul", timeAzul);
        resultado.put("timeVermelho", timeVermelho);
        resultado.put("reservas", reservas);

        return resultado;
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

    @Override
    public boolean existsByNomeAndCadastradoPor(String nome, String cadastradoPor) {
        return repository.existsByNomeAndCadastradoPor(nome, cadastradoPor);
    }
}
