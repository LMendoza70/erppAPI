package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.MantenimientoNotario;
import com.gisnet.erpp.domain.Notario;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the MantenimientoNotario entity.
 */
@SuppressWarnings("unused")
public interface MantenimientoNotarioRepository extends JpaRepository<MantenimientoNotario,Long> {
	
	
}
