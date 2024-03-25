package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Predio;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Colindancia entity.
 */
@SuppressWarnings("unused")
public interface ColindanciaRepository extends JpaRepository<Colindancia,Long> {

	 Long deleteByPredioId(long predioId);

	 public Set<Colindancia> findByPredioId(Long predioId);
	 

	 public Set<Colindancia> findByPredioIdOrderByIdAsc(Long predioId);
	 
	 
	 
}
