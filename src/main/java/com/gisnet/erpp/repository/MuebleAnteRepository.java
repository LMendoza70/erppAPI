package com.gisnet.erpp.repository;

import java.util.Set;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.MuebleAnte;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonaJuridica entity.
 */
@SuppressWarnings("unused")
public interface MuebleAnteRepository extends JpaRepository<MuebleAnte,Long>{

	public MuebleAnte findMuebleAnteByMuebleId( long id);
	
	public MuebleAnte findMuebleAnteByAntecedenteId( long id);
	
}
