package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.TipoInmueble;

public interface TipoInmuebleRepository  extends JpaRepository<TipoInmueble,Long>{
    TipoInmueble findDistinctByNombreEquals(String nombre);
    
    @Query(value= "SELECT ti FROM TipoInmueble ti WHERE REPLACE(ti.nombre, ' ', '') =:EC")
    List <TipoInmueble>findTipoInmuebleByNombre(@Param("EC") String EC);
}
