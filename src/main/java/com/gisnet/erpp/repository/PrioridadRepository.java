package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Prioridad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prioridad entity.
 */
@SuppressWarnings("unused")
public interface PrioridadRepository extends JpaRepository<Prioridad,Long> {

}
