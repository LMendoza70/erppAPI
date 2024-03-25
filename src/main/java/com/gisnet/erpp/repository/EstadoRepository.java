package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Estado;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Estado entity.
 */
@SuppressWarnings("unused")
public interface EstadoRepository extends JpaRepository<Estado,Long> {
    Estado findDistinctByNombreEquals(String nombre);
}
