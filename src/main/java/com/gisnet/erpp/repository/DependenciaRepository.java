package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Dependencia;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Notario entity.
 */
@SuppressWarnings("unused")
public interface DependenciaRepository extends JpaRepository<Dependencia,Long>{
}
