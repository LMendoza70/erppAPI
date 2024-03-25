package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Asentamiento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Asentamiento entity.
 */
@SuppressWarnings("unused")
public interface AsentamientoRepositoryCustom {
	public abstract Page<Asentamiento> findAllByNombre(String nombre, Long tipoAsentId, Pageable pageable, Long estadoId, Long municipioId);
}
