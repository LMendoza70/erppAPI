package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Archivo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Archivo entity.
 */
@SuppressWarnings("unused")
public interface ArchivoRepository extends JpaRepository<Archivo,Long> {

   

    @Query(value="  select  a.* from DOCUMENTO  d "+
                " join  ACTO_DOCUMENTO ad on d.ID=ad.DOCUMENTO_ID" +
                " join ARCHIVO a on a.ID=d.ARCHIVO_ID "+
                " WHERE ad.ACTO_ID=:idActo"
                ,nativeQuery=true
	)
    public List<Archivo> findAllByDocumentoActo(@Param("idActo") Long idActo);
    
  
}
