package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ConceptoPago;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Asentamiento entity.
 */
@SuppressWarnings("unused")
public interface ConceptoPagoRepositoryCustom {
	public abstract Page<ConceptoPago> findAllBy(Pageable pageable, String nombre, Long areaId, Long clasifActoId);

	//public List<ConceptoPago> findAllByActosPrelacion(Long idPrelacion);

}
