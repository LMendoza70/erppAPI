package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.UsuarioServicios;

public interface UsuarioServicioRepository  extends JpaRepository<UsuarioServicios,Long> {

	Long countByUsuarioIdAndIndTurnadoAndServicioIdIn(Long idUsuario,Boolean indTurnado,List<Long> idsServicios);
	
	List<UsuarioServicios> findAllByUsuarioIdOrderByServicioNombre(Long id);
}
