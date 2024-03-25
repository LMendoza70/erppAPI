package com.gisnet.erpp.repository;
import com.gisnet.erpp.domain.Modulo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Modulo entity.
 */
@SuppressWarnings("unused")
public interface ModuloRepositoryCustom {
	public abstract Page<Modulo> getAllModulos(Pageable pageable, String nombre);
}
