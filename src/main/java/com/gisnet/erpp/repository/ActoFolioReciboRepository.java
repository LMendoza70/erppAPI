package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ActoFolioRecibo;

/**
 * Spring Data JPA repository for the ActoFolioRecibo entity.
 */
@SuppressWarnings("unused")
public interface ActoFolioReciboRepository extends JpaRepository<ActoFolioRecibo, Long> {
	public Long deleteByActoPredioId(Long actoId);

}
