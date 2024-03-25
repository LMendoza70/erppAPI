package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.FuncionRolUsu;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Funcion entity.
 */
@SuppressWarnings("unused")
public interface FuncionRolUsuRepository extends JpaRepository<FuncionRolUsu,Long> {

	Set<FuncionRolUsu> findAllByActivoTrueAndUsuarioId(Long id);
	FuncionRolUsu findOneByUsuarioIdAndFuncionRolId(Long usuarioId, Long funcionRolId);
}
