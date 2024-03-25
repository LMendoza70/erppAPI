package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.Usuario;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Rol entity.
 */
@SuppressWarnings("unused")
public interface RolRepository extends JpaRepository<Rol,Long>, RolRepositoryCustom{

	@Query("select max(r.orden) from Rol r")
	public abstract Integer findMaxOrden();
	
	//@EntityGraph(attributePaths = "funcionRolesParaRols.rol.funcionRolesParaRols.funcion")
    //List <Rol> findAllWithFunciones();
	
	public List<Rol> findAllByNivelIsNotNullOrderByOrdenAsc();
	public List<Rol> findAllByNivelIsNotNullOrderByNombreAsc();

}
