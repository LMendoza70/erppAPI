package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoNotario;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoNotario entity.
 */
@SuppressWarnings("unused")
public interface TipoNotarioRepository extends JpaRepository<TipoNotario,Long> {
	
	@Query(value= "SELECT tn FROM TipoNotario tn WHERE REPLACE(tn.nombre, ' ', '') =:EC")
    List <TipoNotario>findTipoNotarioByNombre(@Param("EC") String EC);
	
}
