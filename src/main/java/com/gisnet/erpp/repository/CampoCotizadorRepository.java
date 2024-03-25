package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.CampoCotizador;
public interface CampoCotizadorRepository extends JpaRepository<CampoCotizador,Long> {

	List<CampoCotizador> findByConfCotizadoresParaCamposCotizadorConceptoPagoId(Long conceptoPagoId);
}
