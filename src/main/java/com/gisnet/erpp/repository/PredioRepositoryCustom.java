package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.DetalleBusqueda;
import com.gisnet.erpp.vo.BusquedaPersonaVO;
import com.gisnet.erpp.vo.BusquedaPredioVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gisnet.erpp.domain.Predio;

import java.util.List;
import java.util.Set;

public interface PredioRepositoryCustom {
	public abstract Page<Predio> findAllPageable(Integer noFolio, Pageable pageable,Long oficnaId);
	public abstract Page<Predio> findAllPrediosFraccionamientoPageable(Long acto, Integer noFolio, String manzana, String lote, Pageable pageable);
	public abstract Page<Predio> findAllPrediosFraccionamientoPageable(Long paseId, String manzana, String lote, Pageable pageable);
	Page<Predio> findPrediosByBusquedaVO(BusquedaPredioVO det, Set<BusquedaPersonaVO> busquedaPersonaVO,  Set<BusquedaPersonaVO> personaMoral, Long oficinaId , Pageable pageable);
	List<Predio> findPrediosByBusqueda(BusquedaPredioVO det);
}
