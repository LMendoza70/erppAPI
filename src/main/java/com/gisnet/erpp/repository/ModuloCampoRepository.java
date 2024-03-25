package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ModuloCampo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ModuloCampo entity.
 */
@SuppressWarnings("unused")
public interface ModuloCampoRepository extends JpaRepository<ModuloCampo,Long> {
	public abstract List<ModuloCampo> findByModuloId(Long id);

	public abstract List<ModuloCampo> findAllByIdIn(List<Long> ids);
	
	@Query("select mc from ModuloCampo mc where mc.modulo.id = :moduloId and mc.orden = :orden")
	public ModuloCampo findCampoPorOrden(@Param("moduloId") Long moduloId, @Param("orden") Integer orden);
	
	@Query("select coalesce(max(mc.orden), 0) from ModuloCampo mc where mc.modulo.id = :moduloId")
	public Integer findCampoMaxOrden(@Param("moduloId") Long moduloId);
}
