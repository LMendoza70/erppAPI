package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.UsuNotario;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UsuNotario entity.
 */
@SuppressWarnings("unused")
public interface UsuNotarioRepository extends JpaRepository<UsuNotario,Long> {

}
