package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.UnidadMedida;
import com.gisnet.erpp.domain.UsoSuelo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the UnidadMedida entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida,Long> {
    UnidadMedida findDistinctByNombreEquals(String nombre);
    
    @Query(value= "SELECT um FROM UnidadMedida um WHERE REPLACE(um.nombre, ' ', '') =:EC")
    List <UnidadMedida>findUnidadMedidaByNombre(@Param("EC") String EC);
    
    @Query(value= "SELECT um.* FROM unidad_medida um WHERE um.id in (:ids) order by um.id", nativeQuery=true)
	List<UnidadMedida> findAllfilter(@Param("ids") Integer[]ids);

}
