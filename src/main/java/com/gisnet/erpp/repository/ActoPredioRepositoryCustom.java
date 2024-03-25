package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.domain.AuxiliarPersona;
import com.gisnet.erpp.domain.MueblePersona;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the ActoPredio entity.
 */
@SuppressWarnings("unused")
public interface ActoPredioRepositoryCustom {

	public abstract Set<PredioPersona> findPersonasbyPredioId(Long id);
	public abstract Set<PjPersona> findPersonasbyPersonaJuridicaId(Long id);
	public abstract Set<AuxiliarPersona> findPersonasbyFolioSeccionAuxiliarId(Long id);
	public abstract Set<MueblePersona> findPersonasbyMuebleId(Long id);
	
	public ActoPredio findAllByActoAndVf(Long actoId, boolean validacionFolios,Constantes.TipoEntrada tipoEntrada);
	
	public List<ActoPredio> findByFolioIdAndPrelacionIdAndTipoEntradaId(Long folioId, Long prelacionId, Constantes.TipoEntrada tipoEntrada);
	public List<ActoPredio> findByFolioIdAndTipoFolioAndPrelacionIdAndTipoEntradaId(Long folioId, Constantes.ETipoFolio tipoFolio, Long prelacionId, Constantes.TipoEntrada tipoEntrada);
	public List<ActoPredio> findAllByPrelacionAndVf(Long prelacionId, boolean validacionFolios,Constantes.TipoEntrada tipoEntrada);
	public List<ActoPredio> findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaId(Long folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo, Constantes.TipoEntrada tipoEntrada);
	public List<ActoPredio> findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaIdAndPrimerRegistro(Long folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo, Constantes.TipoEntrada tipoEntrada, Boolean primerRegistro) ;
	public List<ActoPredio> findAllByActoIdAndTipoEntrada(Long actoId, Constantes.TipoEntrada tipoEntrada);	
	public List<ActoPredio> findBydPrelacionIdAndTipoEntradaIdPredioIsNull(Long prelacionId, Constantes.TipoEntrada tipoEntrada);
	public List<ActoPredio> findByFolioIdAndTipoFolioAndTipoEntradaIdAndPrimerRegistro(Long folioId, Constantes.ETipoFolio tipoFolio,  Constantes.TipoEntrada tipoEntrada, Boolean primerRegistro);
	//AVM FIRMA POR ACTO
	public List<ActoPredio> findAllByPrelacion(Long prelacionId);
}
