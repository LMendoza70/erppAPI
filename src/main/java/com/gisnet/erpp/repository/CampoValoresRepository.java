package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.CampoValores;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CampoValores entity.
 */
@SuppressWarnings("unused")
public interface CampoValoresRepository extends JpaRepository<CampoValores,Long> {
	public abstract List<CampoValores> findByCampoId(Long id);
	public abstract CampoValores findById(Long id);
}
