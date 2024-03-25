package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.PredioPersonaControl;

@SuppressWarnings("unused")
public interface PredioPersonaControlRepository extends JpaRepository<PredioPersonaControl,Long> {
	List<PredioPersonaControl> findByActoId(Long actoId);
	Long deleteByActoId(Long actoId);
}
