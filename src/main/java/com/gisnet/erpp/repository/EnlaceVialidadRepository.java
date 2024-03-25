package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.EnlaceVialidad;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the EnlaceVialidad entity.
 */
@SuppressWarnings("unused")
public interface EnlaceVialidadRepository extends JpaRepository<EnlaceVialidad,Long> {
	
	@Query(value= "SELECT ev FROM EnlaceVialidad ev WHERE REPLACE(ev.nombre, ' ', '') =:EC")
    List <EnlaceVialidad>findEnlaceVialidadByNombre(@Param("EC") String EC);

}
