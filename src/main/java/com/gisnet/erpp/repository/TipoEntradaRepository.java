package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoEntrada;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoEntrada entity.
 */
@SuppressWarnings("unused")
public interface TipoEntradaRepository extends JpaRepository<TipoEntrada,Long> {

}
