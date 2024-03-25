package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.SolicitudDevolucion;

public interface SolicitudDevolucionRepository extends JpaRepository<SolicitudDevolucion,Long> {
 
	SolicitudDevolucion findFirstByPrelacionId (Long prelacionId);
	
	
	@Query(value="SELECT nextval(:nameSequence)",nativeQuery=true)
	Integer getConsecutivo(@Param("nameSequence") String nameSequence);
	
	SolicitudDevolucion findFirstByPrelacionIdOrderByIdDesc(Long prelacionId);
}
