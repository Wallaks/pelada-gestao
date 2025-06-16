package com.pelada_gestao.repository;

import com.pelada_gestao.domain.model.JogadorSorteado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogadorSorteadoRepository extends JpaRepository<JogadorSorteado, Long> {

    List<JogadorSorteado> findBySorteioId(Long sorteioId);

}
