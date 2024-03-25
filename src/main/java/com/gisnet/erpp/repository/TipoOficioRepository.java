package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoOficio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoMotivo entity.
 */
@SuppressWarnings("unused")
public interface TipoOficioRepository extends JpaRepository<TipoOficio,Long> {
    public TipoOficio findOneByDescripcion (String descripcion);
}
