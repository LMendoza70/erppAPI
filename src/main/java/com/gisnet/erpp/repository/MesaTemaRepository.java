package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.MesaTema;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MesaTema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MesaTemaRepository extends JpaRepository<MesaTema,Long> {
    
}
