package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoIden;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoIden entity.
 */
@SuppressWarnings("unused")
public interface TipoIdenRepository extends JpaRepository<TipoIden,Long> {

    @Query(value= "SELECT ti FROM TipoIden ti WHERE REPLACE(ti.nombre,' ', '') =:EC")
    List <TipoIden>findTipoIdentificacionByNombre(@Param("EC") String EC);
}
