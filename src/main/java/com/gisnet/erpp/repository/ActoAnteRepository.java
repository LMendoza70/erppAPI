package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoAnte;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActoAnte entity.
 */
@SuppressWarnings("unused")
public interface ActoAnteRepository extends JpaRepository<ActoAnte,Long> {

}
