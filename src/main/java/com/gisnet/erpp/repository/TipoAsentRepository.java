package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoAsent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoAsent entity.
 */
@SuppressWarnings("unused")
public interface TipoAsentRepository extends JpaRepository<TipoAsent,Long> {
    TipoAsent findDistinctByNombreEquals(String nombre);
}
