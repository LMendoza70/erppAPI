package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoModulo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActoModulo entity.
 */
@SuppressWarnings("unused")
public interface ActoModuloRepository extends JpaRepository<ActoModulo,Long> {

}
