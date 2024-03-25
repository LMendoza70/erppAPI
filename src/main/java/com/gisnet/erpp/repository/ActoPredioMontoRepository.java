package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioMonto;

/**
 * Spring Data JPA repository for the ActoPredio entity.
 */
@SuppressWarnings("unused")
public interface ActoPredioMontoRepository extends JpaRepository<ActoPredioMonto,Long> {
	public List<ActoPredioMonto> findByActoId(Long actoId);
	
	public Long deleteByActoPredioActoId(long actoId);
	
	public Long deleteByActoPredio(ActoPredio actoPredio);
	
	public Long deleteByActoId(Long actoId);
}
