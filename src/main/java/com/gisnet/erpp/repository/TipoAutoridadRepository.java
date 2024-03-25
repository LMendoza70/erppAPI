package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoAutoridad;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoAutoridad entity.
 */
@SuppressWarnings("unused")
public interface TipoAutoridadRepository extends JpaRepository<TipoAutoridad,Long> {

}
