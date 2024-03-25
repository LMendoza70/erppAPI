package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Juez;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Juez entity.
 */
@SuppressWarnings("unused")
public interface JuezRepositoryCustom {
	public abstract Page<Juez> findAllByNombre(String paterno, String materno, String nombre, Integer noJuez, Pageable pageable); //, Long materiaId
}
