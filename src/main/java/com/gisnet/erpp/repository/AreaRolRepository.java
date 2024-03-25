package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.UsuarioArea;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the AreaClasifActo entity.
 */
@SuppressWarnings("unused")
public interface AreaRolRepository extends JpaRepository<AreaRol,Long> {
    @EntityGraph(attributePaths = "area")
	List<AreaRol> findAllWithRolsByRolId(Long id);
	AreaRol findOneWithRolsById(Long id);
	
	@Query(value="SELECT a FROM AreaRol a where a.area in(:area) and rol = :rol ")
	List<AreaRol>findAllByRolAndArea(@Param("area")List<Area> usuarioArea,@Param("rol")Rol rol);
	
	@Query(value="SELECT a FROM AreaRol a where a.area.id = :area and rol = :rol ")
	List<AreaRol>findAllByRolAndAreaId(@Param("area")Long usuarioArea,@Param("rol")Rol rol);
       
}
