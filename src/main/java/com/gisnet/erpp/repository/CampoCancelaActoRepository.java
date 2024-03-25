package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.CampoCancelaActo;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * Spring Data JPA repository for the CampoCancelaActo entity.
 */
@SuppressWarnings("unused")
public interface CampoCancelaActoRepository extends JpaRepository<CampoCancelaActo,Long> {
	public Set<CampoCancelaActo> findByCampoValorId(Long id);
	
	@Query("select ca from CampoValores cv inner join cv.cancelaActos ca where cv.campo.id = :campoId")
	public Set<CampoCancelaActo> findCancelaActoByCampoIdQuery(@Param("campoId") Long campo);
}
