package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Area;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Area entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaRepository extends JpaRepository<Area,Long>, AreaRepositoryCustom {
	public abstract List<Area> findByOrderByNombre();
	
	@Query(value=" select a from Area a join fetch a.tipoUsuarioAreasParaArea t join fetch a.tipoFolio where t.tipoUsuario.id= :tipoUsuarioId order by a.nombre")
	public List<Area> findAllByTipoUsuario(@Param("tipoUsuarioId") Long tipoUsuarioId );
	
	@Query(value=" select a.* from Area a where a.id in (:ids) order by a.id", nativeQuery=true)
	List<Area> findAreafilter(@Param("ids") Integer[]ids);
	
}
