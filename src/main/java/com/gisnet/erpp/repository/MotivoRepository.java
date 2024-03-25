package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Motivo;

import com.gisnet.erpp.domain.TipoMotivo;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Motivo entity.
 */
@SuppressWarnings("unused")
public interface MotivoRepository extends JpaRepository<Motivo,Long> {

    public List<Motivo> findAllByTipoMotivoNombre(String nombreTipoMotivo);

    public List<Motivo> findAllByActivo(Boolean activo);
    
    @Query(value= "SELECT m FROM Motivo m WHERE m.tipoMotivo =:tipoMotivo AND REPLACE(m.nombre, ' ', '') =:nombre")
	List <Motivo>findMotivoByTipoMotivoAndNombre(@Param("nombre") String nombre,@Param("tipoMotivo") TipoMotivo tipoMotivo);
}
