package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.SeccionFolio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SeccionFolio entity.
 */
@SuppressWarnings("unused")
public interface SeccionFolioRepository extends JpaRepository<SeccionFolio,Long> {
   	 List<SeccionFolio> findAll();
}
