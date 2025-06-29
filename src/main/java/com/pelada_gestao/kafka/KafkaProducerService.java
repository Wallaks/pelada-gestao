package com.pelada_gestao.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pelada_gestao.domain.event.JogadorCadastradoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${pelada.kafka.enabled:true}")
    private boolean kafkaEnabled;

    private static final String TOPIC = "jogador-evento";

    public void enviarEventoJogador(JogadorCadastradoEvent event) {
        if (!kafkaEnabled) {
            return;
        }

        try {
            String mensagem = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, mensagem);
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar evento JogadorCadastradoEvent para JSON", e);
        }
    }

}
