package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Set;

import com.gisnet.erpp.domain.TipoActo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ConcPagoTipoActo;

public interface ConcPagoTipoActoRepository extends JpaRepository<ConcPagoTipoActo,Long> {

	List<ConcPagoTipoActo> findByTipoActoIdAndConceptoPagoAreaIdAndConceptoPagoClasifActoId(Long idTipoActo,Long idArea,Long idClasifActo);
	List<ConcPagoTipoActo> findAllByTipoActoIn(List<TipoActo> tipos);
}
