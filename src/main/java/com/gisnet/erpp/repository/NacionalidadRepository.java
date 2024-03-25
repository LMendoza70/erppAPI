package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Parentesco;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Nacionalidad entity.
 */
@SuppressWarnings("unused")
public interface NacionalidadRepository extends JpaRepository<Nacionalidad,Long> {

    Nacionalidad findDistinctByNombreEquals(String nombre);
    
    @Query(value= "SELECT n FROM Nacionalidad n WHERE REPLACE(n.nombre, ' ', '') =:EC")
    List <Nacionalidad>findNacionalidadByNombre(@Param("EC") String EC);

}
