package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoActo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the TipoActo entity.
 */
@SuppressWarnings("unused")
public interface TipoActoRepository extends JpaRepository<TipoActo,Long>, TipoActoRepositoryCustom {

	public abstract List<TipoActo> findByClasifActoId(Long id);

	public abstract List<TipoActo> findByClasifActoIdAndActivoIsTrue(Long id);

	public abstract List<TipoActo> findByClasifActoIdAndActivoIsTrueOrderByNombreAsc(Long id);
	
	public abstract List<TipoActo> findByClasifActoIdOrderByNombreAsc(Long id);

	@EntityGraph(attributePaths = "moduloTipoActosParaTipoActos.modulo.moduloCamposParaModulos.campo")
	public abstract Optional<TipoActo> findOneWithModulosById(Long id);

	@Query(value="SELECT p FROM TipoActo p where p.clasifActo.id in(:ids) and p.activo = :activos  and subClasificacion is null")
	public abstract List <TipoActo> findAllByClasif(@Param("ids")List<Long> id,@Param("activos")Boolean activos);

	@Query(value="SELECT tp FROM TipoActo tp INNER JOIN tp.clasifActo ca"+
			" where ca.tipoFolio.id = :tipoFolioId"+
			" order by tp.nombre")
	public abstract List<TipoActo> findAllByTipoFolioId(@Param("tipoFolioId") Long tipoFolioId);

	@Query(value="SELECT tp FROM TipoActo tp INNER JOIN tp.clasifActo ca INNER JOIN ca.areaClasifActosParaClasifActos aca"+
			" where aca.area.id = :areaId"+
			" and tp.activo = true"+
			" order by tp.nombre")
	public abstract List<TipoActo> findAllByAreaId(@Param("areaId") Long areaId);

	public abstract List<TipoActo> findAllByTipoActoTipoActoParaTipoActoPadreTipoActoId(Long tipoActoId);

	public List<TipoActo> findAllByActivoIsTrueOrderByNombreAsc();

	public List<TipoActo> findAllByServiciosParaTipoActosIsNullAndActivoIsTrueOrderByNombreAsc();
	
	public List<TipoActo> findByActivoAndMasivo(Boolean activo,Boolean masivo);
	
	@Query(value="SELECT ta FROM TipoActo ta where ta.clasifActo.id =:clasifActoId and ta.activo =true and ta.id > 1007")
	public abstract List<TipoActo> findByClasifActoTraslativoAndActivoIsTrueOrderByNombreAsc( @Param("clasifActoId")Long clasifActoId);
	
	public abstract List<TipoActo> findAllByIdIn(List<Long> ids);
}
