package com.gisnet.erpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gisnet.erpp.domain.Servicio;

public interface ServicioRepositoryCustom {
	public abstract Page<Servicio> getAllServicios(Pageable pageable, String nombre);
}
