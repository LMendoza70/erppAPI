package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Juez;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Juez entity.
 */
@SuppressWarnings("unused")
public interface JuezRepository extends JpaRepository<Juez,Long>, JuezRepositoryCustom {
}
