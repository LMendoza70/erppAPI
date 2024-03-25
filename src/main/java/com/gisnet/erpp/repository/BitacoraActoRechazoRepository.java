package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.BitacoraActoRechazo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the BitacoraActoRechazo entity.
 */
@SuppressWarnings("unused")
public interface BitacoraActoRechazoRepository extends JpaRepository<BitacoraActoRechazo,Long> {
	List<BitacoraActoRechazo> findAllBitacoraActoRechazoByUsuarioId (Long id);
	
	List<BitacoraActoRechazo> findByActoIdOrderByIdDesc(Long id);
	
	Long deleteByActoId(Long actoId);

}

