package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.Usuario;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Colindancia entity.
 */
@SuppressWarnings("unused")
public interface ConceptoPagoRepository extends JpaRepository<ConceptoPago,Long>, ConceptoPagoRepositoryCustom {

        @Query(value="select DISTINCT(c.id) as id, c.ACTIVO , "+
        "c. nombre, c.AREA_ID, c.CLASIF_ACTO_ID, c.COMISION, c.CLAVE_CONCEPTO_FINANZAS, c.SERVICIO_CLASIF_CONCEPTO_ID , C.TARIFA_UNITARIA " + 
        ", c.FORMULA , c.CONCEPTOS_ADICIONALES, c.EXCENTO_PAGO, c.PAGO_MINIMO, c.PAGO_TOPE, c.COMISION "+
        "from ACTO  a "+
        " join CONC_PAGO_TIPO_ACTO ca on ca.TIPO_ACTO_ID=a.TIPO_ACTO_ID  "+
        " join CONCEPTO_PAGO c on c.ID=ca.CONCEPTO_PAGO_ID "+
        " where a.PRELACION_ID=:idPrelacion order by c.ID", nativeQuery=true)
        public List<ConceptoPago> findAllByActosPrelacion(@Param("idPrelacion") Long idPrelacion);


        List<ConceptoPago> findByAreaId(Long idArea);
        
        List<ConceptoPago> findByAreaIdAndClasifActoIsNull(Long idArea);
        
        List<ConceptoPago> findByAreaIdAndClasifActoId(Long idArea,Long idClasifActo);
        
        List<ConceptoPago> findByServiciosConceptoPagoServiciosCotizadorIdOrderByNombre(Long servicioCotizadorId);
        
        List<ConceptoPago> findByServicioClasifConceptoServiciosCotizadorIdAndServicioClasifConceptoClasificacionConceptoIdOrderByNombre(Long idServicioCotizador,Long idClasificacionConcepto);
       
       // @Query(value= " SELECT DISTINCT cp.conceptosAdicionales FROM ConceptoPago cp WHERE cp.conceptosAdicionales IS NOT NULL")
        @Query(value= " SELECT DISTINCT cp.conceptosAdicionales FROM ConceptoPago cp")
        List<ConceptoPago> findDistinctByConceptosAdicionales();
        
        List<ConceptoPago> findByCveConceptoFinanzas(String clave);
}
