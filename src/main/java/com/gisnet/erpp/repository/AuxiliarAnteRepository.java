package com.gisnet.erpp.repository;

import java.util.Set;

import com.gisnet.erpp.domain.AuxiliarAnte;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonaJuridica entity.
 */
@SuppressWarnings("unused")
public interface AuxiliarAnteRepository extends JpaRepository<AuxiliarAnte,Long>{

	public AuxiliarAnte findOneByFolioSeccionAuxiliarId( long Id);
	
	public AuxiliarAnte findOneByAntecedenteId( long Id);
	
}
