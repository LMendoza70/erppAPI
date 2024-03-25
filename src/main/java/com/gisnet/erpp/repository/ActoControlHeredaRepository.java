package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ActoControlHereda;

public interface ActoControlHeredaRepository extends JpaRepository<ActoControlHereda, Long> {

	List<ActoControlHereda> findAllByActoId(Long actoId);

	Long deleteByActoId(Long actoId);
	Long deleteByActoHeredadoId(Long actoId);


}
