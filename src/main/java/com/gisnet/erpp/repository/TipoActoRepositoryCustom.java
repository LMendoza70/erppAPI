package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoActo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the TipoActo entity.
 */
@SuppressWarnings("unused")
public interface TipoActoRepositoryCustom {
	public abstract Page<TipoActo> getAllTiposActos(Pageable pageable, String nombre);
}