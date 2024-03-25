package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.domain.UsuarioAreaRol;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Funcion entity.
 */
@SuppressWarnings("unused")
public interface UsuarioAreaRolRepository extends JpaRepository<UsuarioAreaRol,Long> {
	Optional<UsuarioAreaRol> findOneByUsuarioIdAndAreaRolId(Long usuarioId, Long areaRolId);
	
	@Query(value="SELECT u FROM UsuarioAreaRol u where u.areaRol in(:arearol)")
	List<UsuarioAreaRol> findAllByAreaRol(@Param("arearol")List<AreaRol> areaRol);

}
