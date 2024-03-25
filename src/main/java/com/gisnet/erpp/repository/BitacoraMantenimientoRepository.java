package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.BitacoraMantenimiento;

public interface BitacoraMantenimientoRepository  extends JpaRepository<BitacoraMantenimiento,Long> {


	List<BitacoraMantenimiento> findAllByActoPredioId(Long actoPredioId);
	
	Long deleteByActoPredioId(Long actoId);
	
	@Query(value="SELECT bm FROM BitacoraMantenimiento bm left join fetch bm.actoPredio ap where ap.predio.id =:predioId or bm.predio.id =:predioId  )")
	List<BitacoraMantenimiento> findAllByPredioIdOrderByIdDesc(@Param("predioId")Long predioId);
	List<BitacoraMantenimiento> findAllByActoPredioPersonaJuridicaIdOrderByIdDesc(Long personaJuridicaId);
	
}
