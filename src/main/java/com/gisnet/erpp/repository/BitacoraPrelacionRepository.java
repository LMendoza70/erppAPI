package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.BitacoraPrelacion;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the BitacoraPrelacion entity.
 */
@SuppressWarnings("unused")
public interface BitacoraPrelacionRepository extends JpaRepository<BitacoraPrelacion,Long> {
	List<BitacoraPrelacion> findAllBitacoraByPrelacionIdAndStatusId(Long prelacion, Long status);
	BitacoraPrelacion findBitacoraByPrelacionIdAndStatusId(Long prelacion, Long status);
	
	List<BitacoraPrelacion> findAllBitacotoraByPrelacionId(Long prelacion);

}

