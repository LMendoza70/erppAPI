package com.gisnet.erpp.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Aviso;
import com.gisnet.erpp.domain.AvisoCanceladoPorActo;

public interface AvisoCanceladoPorActoRepository extends JpaRepository<AvisoCanceladoPorActo, Long> {

	List<AvisoCanceladoPorActo> findByActoId(Long actoId);
	
	Long deleteByActoId(Long actoId);
}
