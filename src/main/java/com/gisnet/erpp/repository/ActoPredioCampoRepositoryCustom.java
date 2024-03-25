package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoPredioCampo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Spring Data JPA repository for the ActoPredioCampo entity.
 */
@SuppressWarnings("unused")
public interface ActoPredioCampoRepositoryCustom {
	public abstract Map<Long, Set<ActoPredioCampo>> getActoPredioCampoValuesByActoPredioIdModuloId(Long actoPredioId, Long moduloId);
}
