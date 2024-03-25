package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoPersona;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoPersona entity.
 */
@SuppressWarnings("unused")
public interface TipoPersonaRepository extends JpaRepository<TipoPersona,Long> {

	@Query(value= "SELECT tp FROM TipoPersona tp WHERE REPLACE(tp.nombre, ' ', '') =:EC")
    List <TipoPersona>findTipoPersonaByNombre(@Param("EC") String EC);
}
