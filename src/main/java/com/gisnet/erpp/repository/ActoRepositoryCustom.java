package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.util.Constantes;

/**
 * Spring Data JPA repository for the Acto entity.
 */
@SuppressWarnings("unused")
public interface ActoRepositoryCustom {
	public abstract List<Acto> findActosByTipoActoPadreEnTipoActoTipoActo(Long tipoActoId, Long folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo);
	public abstract List<Acto> findActos(Long[] folioId, Constantes.ETipoFolio tipoFolio, Constantes.StatusActo statusActo, boolean isReplicaPorFusion);
	public abstract Page<Acto> findAllByPrelacionIdAndVfFalse(Long prelacionId,Pageable pageable);
}
