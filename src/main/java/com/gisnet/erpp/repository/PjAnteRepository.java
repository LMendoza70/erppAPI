package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.PjAnte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the ActoRel entity.
 */
@SuppressWarnings("unused")
public interface PjAnteRepository extends JpaRepository<PjAnte,Long> {

    PjAnte findOneByPersonaJuridicaId(Long id);
    
    PjAnte findOneByAntecedenteId(Long id);
}
