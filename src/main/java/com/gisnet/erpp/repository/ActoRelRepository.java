package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoRel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActoRel entity.
 */
@SuppressWarnings("unused")
public interface ActoRelRepository extends JpaRepository<ActoRel,Long> {
	public List<ActoRel> findByActoId(long actoId);
	
	public Long deleteByActoId(long actoId);
	public Long deleteByActoSigIn(List<Acto> actos);
	public Long deleteByActoSigId(long actoSigId);	
	public Long deleteByActoAntId(long actoSigId);	
}
