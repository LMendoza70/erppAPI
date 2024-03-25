package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Resultado;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Resultado entity.
 */
@SuppressWarnings("unused")
public interface ResultadoRepository extends JpaRepository<Resultado,Long> {

}
