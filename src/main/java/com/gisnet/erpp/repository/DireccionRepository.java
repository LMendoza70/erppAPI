package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Direccion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Direccion entity.
 */
@SuppressWarnings("unused")
public interface DireccionRepository extends JpaRepository<Direccion,Long> {

}
