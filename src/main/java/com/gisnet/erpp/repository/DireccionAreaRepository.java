package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.DireccionArea;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Oficina entity.
 */
@SuppressWarnings("unused")
public interface DireccionAreaRepository extends JpaRepository<DireccionArea,Long> {

DireccionArea findByClave(String clave);


}
