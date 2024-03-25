package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoAclaracion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoEntrada entity.
 */
@SuppressWarnings("unused")
public interface TipoAclaracionRepository extends JpaRepository<TipoAclaracion,Long> {

    List<TipoAclaracion> findAllByInterno(boolean interno);
}
