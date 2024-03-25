package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.SubServicio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SubServicio entity.
 */
@SuppressWarnings("unused")
public interface SubServicioRepository extends JpaRepository<SubServicio,Long> {
	public abstract List<SubServicio> findByServicioId(Long id);
    public SubServicio findOneById(Long id);
}
