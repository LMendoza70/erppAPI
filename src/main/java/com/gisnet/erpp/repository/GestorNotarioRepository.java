package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.GestorNotario;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AreaClasifActo entity.
 */
@SuppressWarnings("unused")
public interface GestorNotarioRepository extends JpaRepository<GestorNotario,Long> {
       
}
