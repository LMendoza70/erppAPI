package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Campo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Campo entity.
 */
@SuppressWarnings("unused")
public interface CampoRepository extends JpaRepository<Campo,Long>, CampoRepositoryCustom {}