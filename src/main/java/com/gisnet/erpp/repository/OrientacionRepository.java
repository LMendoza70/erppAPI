package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Orientacion;

import com.gisnet.erpp.domain.TipoVialidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Orientacion entity.
 */
@SuppressWarnings("unused")
public interface OrientacionRepository extends JpaRepository<Orientacion,Long> {

    Orientacion findOneByNombre (String nombre);
    
    @Query(value= "SELECT o FROM Orientacion  o WHERE REPLACE(o.nombre, ' ', '') =:EC")
	List <Orientacion>findOrientacionByNombre(@Param("EC") String EC);
}
