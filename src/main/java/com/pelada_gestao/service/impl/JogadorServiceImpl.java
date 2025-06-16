package com.pelada_gestao.service.impl;

import com.pelada_gestao.domain.event.JogadorCadastradoEvent;
import com.pelada_gestao.domain.model.Jogador;
import com.pelada_gestao.kafka.KafkaProducerService;
import com.pelada_gestao.repository.JogadorRepository;
import com.pelada_gestao.repository.SorteioRepository;
import com.pelada_gestao.service.JogadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JogadorServiceImpl implements JogadorService {

    private static final Logger log = LoggerFactory.getLogger(JogadorServiceImpl.class);

    private final JogadorRepository repository;
    private final SorteioRepository sorteioRepository;
    private final KafkaProducerService kafkaProducerService;

    public JogadorServiceImpl(JogadorRepository repository,
                              SorteioRepository sorteioRepository,
                              KafkaProducerService kafkaProducerService) {
        this.repository = repository;
        this.sorteioRepository = sorteioRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    @Transactional
    public Jogador salvar(Jogador jogador) {
        Jogador salvo = repository.save(jogador);

        sorteioRepository.findById(salvo.getSorteioId()).ifPresent(sorteio -> {
            JogadorCadastradoEvent evento = new JogadorCadastradoEvent();
            evento.setNomeJogador(salvo.getNome());
            evento.setGoleiro(salvo.isGoleiro());
            evento.setDataCadastro(salvo.getData());
            evento.setNomeSorteio(sorteio.getNome());

            try {
                kafkaProducerService.enviarEventoJogador(evento);
            } catch (Exception e) {
                log.warn("Falha ao enviar evento Kafka: {}", e.getMessage());
            }
        });

        return salvo;
    }

    @Override
    public List<Jogador> listarTodos() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}