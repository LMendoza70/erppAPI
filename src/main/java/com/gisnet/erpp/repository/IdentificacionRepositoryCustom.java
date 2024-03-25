package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Identificacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Identificacion entity.
 */
@SuppressWarnings("unused")
public interface IdentificacionRepositoryCustom {
	public abstract Page<Identificacion> findAllByNombre(Pageable pageable, String valor, Long personaId, Long tipoIdenId );
}
