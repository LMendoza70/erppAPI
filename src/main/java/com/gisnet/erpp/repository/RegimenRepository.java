package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Regimen;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Regimen entity.
 */
@SuppressWarnings("unused")
public interface RegimenRepository extends JpaRepository<Regimen,Long> {

    Regimen findDistinctByNombreEquals(String nombre);
    
    @Query(value= "SELECT r FROM Regimen r WHERE REPLACE(r.nombre, ' ', '') =:EC")
	List <Regimen>findRegimenByNombre(@Param("EC") String EC);
    Optional<Regimen> findOneByCampoValoresId(Long campoValorId);
}
