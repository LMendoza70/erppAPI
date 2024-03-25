package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Notario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Notario entity.
 */
@SuppressWarnings("unused")
public interface NotarioRepositoryCustom {
	public abstract Page<Notario> findAllByNombre(Long municipioId, Long estadoId, String paterno, String materno, String nombre, Integer noNotario, Pageable pageable);
}
