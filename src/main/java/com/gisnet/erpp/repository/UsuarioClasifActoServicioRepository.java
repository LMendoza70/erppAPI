package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.UsuarioClasifActoServicio;

public interface UsuarioClasifActoServicioRepository  extends JpaRepository<UsuarioClasifActoServicio,Long>{

	Long countByUsuarioIdAndTurnarTodosAndClasifActoIdIn(Long idUsuario,Integer turnarTodos,List<Long> clasificaciones);
	
	Long countByUsuarioIdAndTurnarTodosAndServicioIdIn(Long idUsuario,Integer turnarTodos,List<Long> servicios);
	
	UsuarioClasifActoServicio findAllByUsuarioIdAndClasifActoId(Long id,Long idClasif);
	
}
