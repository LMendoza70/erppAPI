package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioOfiAreas;

public interface UsuarioOfiAreasRepository extends JpaRepository<UsuarioOfiAreas,Long> {

	List<UsuarioOfiAreas> findByOficinaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(Long idOficina,Boolean activo,Long idRol);
	
	List<UsuarioOfiAreas> findByOficinaIdAndAreaIdAndActivoAndUsuarioUsuarioRolesParaUsuariosRolId(Long idOficina,Long idArea,Boolean activo,Long idRol);
	
	Long countByUsuarioIdAndActivoAndAreaIdIn(Long idUsuario,Boolean activo,List<Long> idsArea);
	
	@Query(value="SELECT uoa FROM UsuarioOfiAreas uoa WHERE uoa.usuario.id in (:iDs) AND uoa.area.id = :idArea AND uoa.oficina.id = :oficina")
    List<UsuarioOfiAreas> findAllByUsuariosIds(@Param("iDs") List<Long> iDs, @Param("idArea") Long idArea, @Param("oficina") Long oficina );
	
	@Query(value="SELECT uoa FROM UsuarioOfiAreas uoa WHERE uoa.oficina.id = :oficina AND uoa.usuario.id in (:iDs) and uoa.area.id in (:area)")
    List<UsuarioOfiAreas> findAllByUsuarios(@Param("iDs") List<Long> iDs,@Param("oficina") Long oficina, @Param("area") List<Long> area );
	
	List<UsuarioOfiAreas> findAllByUsuario(Usuario usuario);
	
	@Query(value="SELECT uoa FROM UsuarioOfiAreas uoa WHERE uoa.oficina.id = :officeId AND uoa.area in (:areas)")
	List<UsuarioOfiAreas> findAllByAreaAndOficce(@Param("areas") List<Area> areas , @Param("officeId") Long officeId);
}
