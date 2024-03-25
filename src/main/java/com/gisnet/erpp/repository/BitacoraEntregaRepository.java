package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.BitacoraDigitalizador;
import com.gisnet.erpp.domain.BitacoraEntrega;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the BitacoraEntrega entity.
 */
@SuppressWarnings("unused")
public interface BitacoraEntregaRepository extends JpaRepository<BitacoraEntrega,Long> {
	List<BitacoraEntrega> findAllBitacoraEntregaByPrelacionIdOrderByIdDesc (Long id);
	List<BitacoraEntrega> findAllBitacoraEntregaByUsuarioId (Long id);
	
	List<BitacoraEntrega> findByPrelacionId(Long prelacionId);

}

