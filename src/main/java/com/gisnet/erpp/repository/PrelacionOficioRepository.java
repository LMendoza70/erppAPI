package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoOficio;
import com.gisnet.erpp.domain.PrelacionOficio;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Funcion entity.
 */
@SuppressWarnings("unused")
public interface PrelacionOficioRepository extends JpaRepository<PrelacionOficio,Long> {

	@Query(value=" select max(version) from prelacion_oficio where prelacion_id=:prelacionId ",
							nativeQuery=true)
	public Integer maxByPrelacion(@Param("prelacionId") Long prelacionId);


	public PrelacionOficio findByPrelacionIdAndVersion(Long id,Integer version);

	@Query(value="SELECT  nextval('PRELACION_OFICIO_SEQUENCE')",nativeQuery=true)
	Integer getConsecutivo();

	

	@Query(value="select max(id) id,max(tipo_oficio_id) tipo_oficio_id, max(prelacion_id) prelacion_id, max(version) version,max(plantilla) plantilla, max(num_oficio) num_oficio from prelacion_oficio where   prelacion_id=:prelacionId",nativeQuery=true)
	public PrelacionOficio findByMaxPrelacion(@Param("prelacionId") Long prelacionId);
}


