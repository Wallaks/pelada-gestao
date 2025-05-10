package com.pelada_gestao.repository;

import com.pelada_gestao.model.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long> {

    List<Jogador> findBySorteioId(Long sorteioId);

}
