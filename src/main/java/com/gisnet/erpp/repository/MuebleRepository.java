package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gisnet.erpp.domain.Mueble;

/**
 * Spring Data JPA repository for the PersonaJuridica entity.
 */
@SuppressWarnings("unused")
public interface MuebleRepository extends JpaRepository<Mueble,Long>{

	public Mueble findByNoFolio( int folio);
	
	public Mueble findByNoFolioAndOficinaId(Integer folio, Long oficinaId);
	
	public Mueble findByMuebleParaMueblesAntecedenteId(Long idAntecedente);
	
    @Query(value="select nextval('MUEBLE_FOLIO_SEQ')", nativeQuery = true)
	Long getFolioFromMuebleSequence();
	
}
