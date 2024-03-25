package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ClasifActo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


import java.util.List;

/**
 * Spring Data JPA repository for the ClasifActo entity.
 */
@SuppressWarnings("unused")
public interface ClasifActoRepository extends JpaRepository<ClasifActo,Long>, ClasifActoRepositoryCustom {
	public abstract List<ClasifActo> findByAreaClasifActosParaClasifActosAreaId(Long id);


	@Query(value=" select c.* from CLASIF_ACTO c"+
					" join AREA_CLASIF_ACTO  a on  c.ID=a.CLASIF_ACTO_ID"+
					" join SERVICIO s  on s.AREA_ID=a.AREA_ID"+
					" where s.id=:servicio ",
		nativeQuery=true
	)
	public abstract List<ClasifActo> findAllByServicio(@Param("servicio") Long id);

	@Query(value=" select c.* from CLASIF_ACTO c"+
					" join AREA_CLASIF_ACTO  a on  c.ID=a.CLASIF_ACTO_ID"+
					" where a.AREA_ID=:area and a.SECCION_FOLIO_ID=:seccion",
		nativeQuery=true
	)
	public abstract List<ClasifActo> findAllByAreaIdAndSeccionFolioId(@Param("area") Long idArea,@Param("seccion") Long idSeccionFolio);

}
