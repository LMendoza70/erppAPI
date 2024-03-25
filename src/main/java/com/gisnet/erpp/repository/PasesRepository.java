package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Pases;

/**
 * Spring Data JPA repository for the PredioPersona entity.
 */
@SuppressWarnings("unused")
public interface PasesRepository extends JpaRepository<Pases,Long> {

	@Query("SELECT p FROM Pases p WHERE p.predioOrigen.id = :predioId ORDER BY p.fraccion")
	List<Pases> findByPredioOrigenId(@Param("predioId") Long predioId);
	
	Long deleteByActoId(Long actoId);
}
