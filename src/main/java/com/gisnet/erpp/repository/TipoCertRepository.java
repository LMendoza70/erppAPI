package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoCert;
import com.gisnet.erpp.domain.TipoInmueble;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoCert entity.
 */
@SuppressWarnings("unused")
public interface TipoCertRepository extends JpaRepository<TipoCert,Long> {

	@Query(value= "SELECT tc FROM TipoCert tc WHERE REPLACE(tc.nombre,' ', '') =:EC")
    List <TipoCert>findTipoCertByNombre(@Param("EC") String EC);
}
