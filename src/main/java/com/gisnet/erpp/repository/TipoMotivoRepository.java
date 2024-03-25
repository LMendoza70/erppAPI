package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoMotivo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoMotivo entity.
 */
@SuppressWarnings("unused")
public interface TipoMotivoRepository extends JpaRepository<TipoMotivo,Long> {
    public TipoMotivo findOneByNombre (String nombre);
    
    @Query(value= "SELECT tm FROM TipoMotivo tm WHERE REPLACE(tm.nombre, ' ', '') =:EC")
   	List <TipoMotivo>findTipoMotivoByNombre(@Param("EC") String EC);
}
