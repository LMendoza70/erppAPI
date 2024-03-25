package com.gisnet.erpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gisnet.erpp.domain.ClasifActo;

public interface ClasifActoRepositoryCustom {
	public abstract Page<ClasifActo> getAllClasifActos(Pageable pageable, String nombre);
}
