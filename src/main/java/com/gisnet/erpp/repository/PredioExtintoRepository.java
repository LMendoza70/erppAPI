package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioExtinto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PredioExtinto entity.
 */
@SuppressWarnings("unused")
public interface PredioExtintoRepository extends JpaRepository<PredioExtinto,Long> {
	
	public List<PredioExtinto> findByPredioIdOrderByFechaDesc(Long predioId);
	
	public List<PredioExtinto> findByUsuarioId(Long usuarioId);

	List<PredioExtinto> findByPredioId(Long predioId);
}
