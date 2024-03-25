package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ServicioClasifConcepto;

public interface ServicioClasifConceptoRepository   extends JpaRepository<ServicioClasifConcepto,Long>{

	List<ServicioClasifConcepto> findByServiciosCotizadorId(Long idServicioCotizador);
}
