package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.SubSubServicio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SubSubServicio entity.
 */
@SuppressWarnings("unused")
public interface SubSubServicioRepository extends JpaRepository<SubSubServicio,Long> {
	public abstract List<SubSubServicio> findBySubServicioId(Long id);
}
