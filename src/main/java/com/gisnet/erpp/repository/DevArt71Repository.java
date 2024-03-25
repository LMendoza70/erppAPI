package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.DevArt71;
import com.gisnet.erpp.domain.Pases;

/**
 * Spring Data JPA repository for the DiaInhabil entity.
 */
@SuppressWarnings("unused")
public interface DevArt71Repository extends JpaRepository<DevArt71,Long> {
	
	@Query("SELECT d FROM DevArt71 d join fetch d.prelacion p WHERE d.predio.id = :predioId ORDER BY d.id")
	List<DevArt71> findByPredioId(@Param("predioId") Long predioId);

	@Query("SELECT d FROM DevArt71 d WHERE d.personaJuridica.id = :personaJuridicaId ORDER BY d.id")
	List<DevArt71> findByPersonaJuridicaId(@Param("personaJuridicaId") Long personaJuridicaId);

	@Query("SELECT d FROM DevArt71 d WHERE d.folioSeccionAuxiliar.id = :folioSeccionAuxiliarId ORDER BY d.id")
	List<DevArt71> findByFolioSeccionAuxiliarId(@Param("folioSeccionAuxiliarId") Long folioSeccionAuxiliarId);
}
