package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.EstructuraOrg;
import com.gisnet.erpp.domain.Usuario;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Vialidad entity.
 */
@SuppressWarnings("unused")
public interface EstructuraOrgRepository extends JpaRepository<EstructuraOrg,Long> {

	List<EstructuraOrg> findByUsuarioMaster(Usuario usuario);
	List<EstructuraOrg> findByUsuarioDetail(Usuario usuario);

	@Query(value="SELECT EO FROM EstructuraOrg EO where EO.usuarioMaster in (:usuarios)")
	List<EstructuraOrg> findByUsuariosMaster(@Param("usuarios")List<Usuario> usuarios );
	
	@Query(value="SELECT EO FROM EstructuraOrg EO where EO.usuarioDetail in (:usuarios)")
	List<EstructuraOrg> findByUsuariosDetail(@Param("usuarios")List<Usuario> usuarios );
}
