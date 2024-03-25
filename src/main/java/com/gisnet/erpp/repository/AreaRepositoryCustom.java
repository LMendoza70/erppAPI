package com.gisnet.erpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gisnet.erpp.domain.Area;

public interface AreaRepositoryCustom {
	public abstract Page<Area> getAllAreas(Pageable pageable, String nombre);
}
