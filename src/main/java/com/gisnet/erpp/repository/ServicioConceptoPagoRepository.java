package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.ServicioConceptoPago;

public interface ServicioConceptoPagoRepository  extends JpaRepository<ServicioConceptoPago,Long>{

	List<ServicioConceptoPago> findByServiciosCotizadorId(Long servicioCotizadorId);
	
	List<ServicioConceptoPago> findByConceptoPagoId(Long idConceptoPago);
	
	   @Query(value="select DISTINCT(sc.id) as id, sc.CONCEPTO_PAGO_ID, sc.SERVICIOS_COTIZADOR_ID, sc.SERVICIO_ID from PRELACION_SERVICIO  ps "+
               " join SERVICIO s on s.ID=ps.SERVICIO_ID "+
               " join SERVICIO_CONCEPTO_PAGO sc on sc.SERVICIO_ID=S.ID  "+
               //" join CONCEPTO_PAGO c on c.ID=sc.CONCEPTO_PAGO_ID "+
               " where ps.PRELACION_ID=:idPrelacion order by sc.ID", nativeQuery=true)
public List<ServicioConceptoPago> findAllByServiciosPrelacion(@Param("idPrelacion") Long idPrelacion);
	
}
