package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioActos;

public interface UsuarioActosRepository  extends JpaRepository<UsuarioActos,Long> {

	Long countByUsuarioIdAndIndTurnadoAndTipoActoIdIn(Long idUsuario,Boolean indTurnado,List<Long> tipoActos);
	List <UsuarioActos> findAllByUsuarioIdOrderByTipoActoClasifActoNombre(Long id);
	List <UsuarioActos> findAllByUsuario(Usuario usuario);
	
}
