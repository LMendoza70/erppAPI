package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioRol;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UsuarioRol entity.
 */
@SuppressWarnings("unused")
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol,Long> {
	Optional<UsuarioRol> findOneByRolIdAndUsuarioId(Long rolId, Long usuarioId);
	
	@Query(value ="SELECT ur FROM UsuarioRol ur WHERE ur.usuario.id=:usuarioId")
	  UsuarioRol findUsuarioRolByUsuarioId(@Param("usuarioId")Long usuarioId);
}
