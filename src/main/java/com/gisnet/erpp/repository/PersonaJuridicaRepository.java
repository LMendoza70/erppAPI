package com.gisnet.erpp.repository;

import java.util.Set;

import com.gisnet.erpp.domain.PersonaJuridica;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the PersonaJuridica entity.
 */
@SuppressWarnings("unused")
public interface PersonaJuridicaRepository extends JpaRepository<PersonaJuridica,Long>, PersonaJuridicaRepositoryCustom{

	public PersonaJuridica findById(Long folio);

	public PersonaJuridica findByNoFolio(Integer folio);

	public PersonaJuridica findByNoFolioAndOficinaId(Integer folio, Long oficinaId);
	
	public PersonaJuridica findByNumeroFolioRealAndOficinaId(Integer folioAnterior, Long oficinaId);
	
    Optional<PersonaJuridica> findByPjAnteParaPersonasJuridicasAntecedenteId(Long idAntecedente);
    
    @Query(value="select nextval('PJ_FOLIO_SEQ')", nativeQuery = true)
	Long getFolioFromPjSequence();

	public List<PersonaJuridica> findByDenominacionNombreContainingIgnoreCase(String nombre);
}
