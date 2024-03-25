package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.PrelacionAnteCapHist;


/**
 * Spring Data JPA repository for the Antecedente entity.
 */
@SuppressWarnings("unused")
public interface AnteUsuarioCapHistRepository extends JpaRepository<PrelacionAnteCapHist,Long> {

	
}
