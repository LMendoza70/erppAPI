package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ServicioCosto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServicioCosto entity.
 */
@SuppressWarnings("unused")
public interface ServicioCostoRepository extends JpaRepository<ServicioCosto,Long> {

	List<ServicioCosto> findByServicioId(Long servicioId);
}
