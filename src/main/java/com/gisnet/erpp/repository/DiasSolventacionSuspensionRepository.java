package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Suspension;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.ConcPagoTipoActo;
import com.gisnet.erpp.domain.DiasSolventacionSuspension;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Bitacora entity.
 */
//JADV-SUSPENSION
@SuppressWarnings("unused")
public interface DiasSolventacionSuspensionRepository extends JpaRepository<DiasSolventacionSuspension,Long> {

	
	public DiasSolventacionSuspension findFirstBySuspensionOrderByIdDesc(Suspension suspension);
	
}

