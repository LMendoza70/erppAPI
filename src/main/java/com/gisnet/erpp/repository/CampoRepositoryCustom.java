package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Campo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Campo entity.
 */
@SuppressWarnings("unused")
public interface CampoRepositoryCustom {
	public abstract Page<Campo> getAllCampos(Pageable pageable, String nombre);
}
