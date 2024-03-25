package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Bitacora entity.
 */
@SuppressWarnings("unused")
public interface BitacoraRepository extends JpaRepository<Bitacora,Long> {
	//Bitacora findBitacoraByPrelacionId(Long prelacion);
	List<Bitacora> findAllBitacoraByPrelacionIdAndStatusId(Long prelacion, Long status);
	Bitacora findBitacoraByPrelacionIdAndStatusId(Long prelacion, Long status);
	List<Bitacora> findByPrelacionIdAndTipoMotivoId(Long prelacionId,Long tipoMotivoId);
	List<Bitacora> findByPrelacionIdAndReprocesoIsTrue(Long prelacionId);
	//JADV-SUSPENSION
	Bitacora findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(Long prelacionId, Long statusId);
	Bitacora findTop1BitacoraByPrelacionIdOrderByIdDesc(Long prelacionId);
	
	List<Bitacora> findAllBitacotoraByPrelacionId(Long prelacion);
	Long deleteByActoId(Long actoId);
	Optional<Bitacora>  findFirstByActoId(Long actoId);
	
	List<Bitacora> findByPrelacionIdAndStatusIdAndMotivoIdIsNotNullOrderByFechaDesc(Long prelacionId, Long status);
}

