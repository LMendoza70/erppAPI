package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ReciboServicio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ReciboServicio entity.
 */
@SuppressWarnings("unused")
public interface ReciboServicioRepository extends JpaRepository<ReciboServicio,Long> {

}
