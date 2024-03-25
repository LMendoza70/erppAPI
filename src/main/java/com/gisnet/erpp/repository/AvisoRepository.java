package com.gisnet.erpp.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Aviso;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {

	Aviso findOneByActoId(Long actoId);

	@Query("select a.acto from Aviso a where a.acto.statusActo.id = 1 and a.fechaExp < :fecha")
	List<Acto> findActosActivosVigenciaCaduca(@Param("fecha") Date fecha);
	
	Long deleteByActoId(Long actoId);
}
