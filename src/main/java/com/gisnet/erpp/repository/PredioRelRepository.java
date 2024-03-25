package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioRel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PredioRel entity.
 */
@SuppressWarnings("unused")
public interface PredioRelRepository extends JpaRepository<PredioRel,Long> {
	public List<PredioRel> findByPredioId(long predioId);
	
	public Long deleteByPredioIn(List<Predio> predios);
	public Long deleteByPredioSigIn(List<Predio> predios);
	public Long deleteByActoId(Long actoId);
	public List<PredioRel> findByActoId(Long actoId);
}
