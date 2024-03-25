package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Modulo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Modulo entity.
 */
@SuppressWarnings("unused")
public interface ModuloRepository extends JpaRepository<Modulo,Long>, ModuloRepositoryCustom {

}
