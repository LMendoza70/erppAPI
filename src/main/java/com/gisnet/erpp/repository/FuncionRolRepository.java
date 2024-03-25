package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.FuncionRol;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FuncionRol entity.
 */
@SuppressWarnings("unused")
public interface FuncionRolRepository extends JpaRepository<FuncionRol,Long> {

}
