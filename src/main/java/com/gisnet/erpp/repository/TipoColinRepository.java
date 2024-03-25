package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoColin;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoColin entity.
 */
@SuppressWarnings("unused")
public interface TipoColinRepository extends JpaRepository<TipoColin,Long> {

	 @Query(value= "SELECT tc FROM TipoColin tc WHERE REPLACE(tc.nombre, ' ', '') =:EC")
		List <TipoColin>findTipoColinByNombre(@Param("EC") String EC);
}
