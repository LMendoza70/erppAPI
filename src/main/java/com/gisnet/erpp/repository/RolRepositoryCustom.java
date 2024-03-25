package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Rol;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("unused")
public interface RolRepositoryCustom {

	public abstract Page<Rol> findAllBy(Pageable pageable, String nombre, Boolean activo);

}
