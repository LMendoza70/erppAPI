package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.domain.UsoSuelo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UsoSuelo entity.
 */
@SuppressWarnings("unused")
public interface UsoSueloRepository extends JpaRepository<UsoSuelo,Long> {

    @Query(value= "SELECT us FROM UsoSuelo us WHERE REPLACE(us.nombre, ' ', '') =:EC")
    List <UsoSuelo>findUsoSueloByNombre(@Param("EC") String EC);
}
