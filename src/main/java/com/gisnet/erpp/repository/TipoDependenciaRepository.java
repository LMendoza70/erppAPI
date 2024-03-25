package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoDependencia;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoNotario entity.
 */
@SuppressWarnings("unused")
public interface TipoDependenciaRepository extends JpaRepository<TipoDependencia,Long> {

}
