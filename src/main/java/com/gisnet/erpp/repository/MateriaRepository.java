package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Materia;
import com.gisnet.erpp.domain.Nacionalidad;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Materia entity.
 */
@SuppressWarnings("unused")
public interface MateriaRepository extends JpaRepository<Materia,Long> {

	 @Query(value= "SELECT M FROM Materia M WHERE REPLACE(M.nombre, ' ', '') =:EC")
	 List <Materia>findMateriaByNombre(@Param("EC") String EC);
}
