package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.TipoMoneda;

/**
 * Spring Data JPA repository for the TipoCert entity.
 */
@SuppressWarnings("unused")
public interface TipoMonedaRepository extends JpaRepository<TipoMoneda,Long> {

}
