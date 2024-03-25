package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoVialidad;
import com.gisnet.erpp.domain.UsoSuelo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoVialidad entity.
 */
@SuppressWarnings("unused")
public interface TipoVialidadRepository extends JpaRepository<TipoVialidad,Long> {
    TipoVialidad findDistinctByNombreEquals(String nombre);

	  @Query(value= "SELECT tv FROM TipoVialidad tv WHERE REPLACE(tv.nombre,' ', '' )=:EC")
	    List <TipoVialidad>findTipoVialidadByNombre(@Param("EC") String EC);
}
