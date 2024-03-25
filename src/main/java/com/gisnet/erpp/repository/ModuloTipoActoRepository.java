package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.Modulo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ModuloTipoActo entity.
 */
@SuppressWarnings("unused")
public interface ModuloTipoActoRepository extends JpaRepository<ModuloTipoActo,Long> {
	public abstract List<ModuloTipoActo> findByTipoActoId(Long id);
	
	@Query("select mta from ModuloTipoActo mta where mta.tipoActo.id = :tipoActoId and mta.orden = :orden")
	public ModuloTipoActo findModuloPorOrden(@Param("tipoActoId") Long tipoActoId, @Param("orden") Integer orden);
	
	@Query("select mta from ModuloTipoActo mta where mta.tipoActo.id = :tipoActoId and mta.modulo.id = :moduloId")
	public List<ModuloTipoActo> findAllByTipoActoIdAndModuloId(@Param("tipoActoId") Long tipoActoId, @Param("moduloId") Long moduloId);
	
	@Query("select mta from ModuloTipoActo mta where mta.modulo in(:modulos) and mta.tipoActo.id = :tipoActoId order by mta.orden asc")
	public List<ModuloTipoActo> findAllByModuloInOrderByOrdenAsc(@Param("modulos")Set<Modulo> modulos,@Param("tipoActoId")Long tipoActoId);

	@Query("select coalesce(max(mta.orden), 0) from ModuloTipoActo mta where mta.tipoActo.id = :tipoActoId")
	public Integer findModuloMaxOrden(@Param("tipoActoId") Long tipoActoId);
}
