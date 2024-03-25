package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ReciboConcepto;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ReciboConcepto entity.
 */
@SuppressWarnings("unused")
public interface ReciboConceptoRepository extends JpaRepository<ReciboConcepto,Long> {
		
	@Query(value=" select rc.* from RECIBO_CONCEPTO rc join RECIBO r on r.ID=rc.RECIBO_ID  where r.PRELACION_ID=:idPrelacion ",nativeQuery=true)
	public List<ReciboConcepto> getReciboConceptoByPrelacion(@Param("idPrelacion") Long idPrelacion);

}
