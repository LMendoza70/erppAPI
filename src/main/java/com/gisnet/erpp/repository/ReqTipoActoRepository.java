package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ReqTipoActo;
import com.gisnet.erpp.domain.TipoActo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ReqClasifActo entity.
 */
@SuppressWarnings("unused")
public interface ReqTipoActoRepository extends JpaRepository<ReqTipoActo,Long> {
	public abstract List<ReqTipoActo> findAllByTipoActo(TipoActo tipo);

    @Query(value=" select rta.* from REQ_TIPO_ACTO rta, REQUISITO req "+
            	 " where rta.requisito_id = req.id "+
            	 " and rta.tipo_acto_id = :tipoActoId"+
            	 " and req.ve = true"
            ,nativeQuery=true)
	public abstract List<ReqTipoActo> findAllByTipoActoVE(@Param("tipoActoId") Long tipoActoId );
}
