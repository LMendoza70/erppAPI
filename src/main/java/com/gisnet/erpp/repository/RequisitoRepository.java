package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Requisito;
import com.gisnet.erpp.domain.TipoActo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Requisito entity.
 */
@SuppressWarnings("unused")
public interface RequisitoRepository extends JpaRepository<Requisito,Long> {



}
