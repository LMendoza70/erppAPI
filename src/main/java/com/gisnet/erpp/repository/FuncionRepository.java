package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Funcion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Funcion entity.
 */
@SuppressWarnings("unused")
public interface FuncionRepository extends JpaRepository<Funcion,Long> {

}
