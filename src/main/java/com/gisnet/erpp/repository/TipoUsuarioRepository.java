package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.TipoUsuario;

/**
 * Spring Data JPA repository for the TipoUsuario entity.
 */
@SuppressWarnings("unused")
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario,Long> {
	
	List<TipoUsuario> findByRegistroTrue();

}
