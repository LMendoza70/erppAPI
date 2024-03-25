package com.gisnet.erpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gisnet.erpp.vo.PrelacionContratanteVO;

/**
 * Spring Data JPA repository for the Notario entity.
 */
@SuppressWarnings("unused")
public interface PrelacionContratanteRepositoryCustom {
	public abstract Page<PrelacionContratanteVO> findAllByNombre(String paterno, String materno, String nombre, Pageable pageable);
}
