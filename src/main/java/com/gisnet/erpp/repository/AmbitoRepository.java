package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Ambito;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoNotario entity.
 */
@SuppressWarnings("unused")
public interface AmbitoRepository extends JpaRepository<Ambito,Long> {

}
