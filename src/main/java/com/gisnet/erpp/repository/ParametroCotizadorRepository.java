package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ParametroCotizador;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParametroCotizador entity.
 */
@SuppressWarnings("unused")
public interface ParametroCotizadorRepository extends JpaRepository<ParametroCotizador,Long> {

	ParametroCotizador findByNombre(String nombre);
}
