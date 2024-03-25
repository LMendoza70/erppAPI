package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Parentesco;
import com.gisnet.erpp.domain.TipoCert;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Parentesco entity.
 */
@SuppressWarnings("unused")
public interface ParentescoRepository extends JpaRepository<Parentesco,Long> {
	public abstract List<Parentesco> findAll();
	
	@Query(value= "SELECT p FROM Parentesco p WHERE REPLACE(p.nombre, ' ', '') =:EC")
    List <Parentesco>findParentescoByNombre(@Param("EC") String EC);
}
