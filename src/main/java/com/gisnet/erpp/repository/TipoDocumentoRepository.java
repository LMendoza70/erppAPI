package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoDocumento;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoDocumento entity.
 */
@SuppressWarnings("unused")
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento,Long> {
	@Query(value= "SELECT td FROM TipoDocumento td WHERE REPLACE(td.nombre,' ', '' )=:EC")
    List <TipoDocumento>findTipoDocumentoByNombre(@Param("EC") String EC);
	
	TipoDocumento findTipoDocumentoById(Long id);
	
	List <TipoDocumento> findAllByIdIn(List<Long> ids);

}
