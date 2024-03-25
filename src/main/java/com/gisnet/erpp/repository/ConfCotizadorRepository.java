package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ConfCotizador;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ConfCotizador entity.
 */
@SuppressWarnings("unused")
public interface ConfCotizadorRepository extends JpaRepository<ConfCotizador,Long> {

}
