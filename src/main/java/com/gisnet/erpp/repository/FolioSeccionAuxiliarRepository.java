package com.gisnet.erpp.repository;

import java.util.Set;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonaJuridica entity.
 */
@SuppressWarnings("unused")
public interface FolioSeccionAuxiliarRepository extends JpaRepository<FolioSeccionAuxiliar,Long>{

	public FolioSeccionAuxiliar findOneByNoFolio( int folio);

	public FolioSeccionAuxiliar findOneByNoFolioAndOficinaId( int folio, Long oficinaId);
	
	public FolioSeccionAuxiliar findByAuxiliarAnteParaFolioSeccionAuxiliaresAntecedenteId(Long idAntecedente);
	
    @Query(value="select nextval('SEC_AUX_FOLIO_SEQ')", nativeQuery = true)
	Long getFolioFromSecAuxSequence();
	
}
