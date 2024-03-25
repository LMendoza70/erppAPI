package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Identificacion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Identificacion entity.
 */
@SuppressWarnings("unused")
public interface IdentificacionRepository extends JpaRepository<Identificacion,Long>, IdentificacionRepositoryCustom {

}
