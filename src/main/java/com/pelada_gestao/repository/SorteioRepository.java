package com.pelada_gestao.repository;

import com.pelada_gestao.model.Sorteio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SorteioRepository extends JpaRepository<Sorteio, Long> {

}
