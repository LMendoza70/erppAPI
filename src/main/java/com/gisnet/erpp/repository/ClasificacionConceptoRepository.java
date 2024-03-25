package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ClasificacionConcepto;

public interface ClasificacionConceptoRepository  extends JpaRepository<ClasificacionConcepto,Long>{
	List<ClasificacionConcepto> findByServiciosClasificacionesConceptoServiciosCotizadorIdOrderByNombre(Long servicioCotizadorId);


}
