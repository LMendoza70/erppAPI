package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioArea;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UsuarioArea entity.
 */
@SuppressWarnings("unused")
public interface UsuarioAreaRepository extends JpaRepository<UsuarioArea,Long> {
	
	public abstract UsuarioArea findFirstByAreaId(Long areaId);
	
	List<UsuarioArea> findAreaIdById(Long id);
	
	List<UsuarioArea> findAllByUsuario(Usuario usuario);
	
	@Query(value="SELECT UA FROM UsuarioArea UA WHERE UA.usuario.id = :idUser")
	List<UsuarioArea> findAllByUserId(@Param("idUser") Long idUser);
	
}
