package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.DiaInhabil;

import org.springframework.data.jpa.repository.*;

import java.util.Date;
import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the DiaInhabil entity.
 */
@SuppressWarnings("unused")
public interface DiaInhabilRepository extends JpaRepository<DiaInhabil,Long> {

	Long countByFechaDiaInhabilBetween(Date fechaIni,Date fechaFin);

	//@Query(value="SELECT di.* FROM DIA_INHABIL di WHERE TRUNC(FECHA_DIA_INHABIL) = TRUNC(TO_TIMESTAMP(:fecha, 'YYYY-MM-DD HH24:MI:SS')) and FECHA_DIA_INHABIL >= CURRENT_TIMESTAMP",nativeQuery=true)
	@Query(value="SELECT di.* FROM DIA_INHABIL di WHERE date_trunc('DAY', FECHA_DIA_INHABIL) = date_trunc('DAY',TO_TIMESTAMP(:fecha, 'YYYY-MM-DD HH24:MI:SS')) and FECHA_DIA_INHABIL >= CURRENT_TIMESTAMP",nativeQuery=true)
	List<DiaInhabil> findDiasInhabilesByDate(@Param("fecha") String fecha);
	
	List<DiaInhabil> findAllByMesAndDia(Integer mes, Integer dia);
	
	@Query(value="select \n" + 
			" count(1)\n" + 
			"from dia_inhabil di where  \n" + 
			"EXTRACT(YEAR FROM fecha_dia_inhabil) =:anio\n" + 
			"and EXTRACT(day FROM fecha_dia_inhabil) = :dia\n" + 
			"and  EXTRACT(MONTH FROM fecha_dia_inhabil) = :mes",nativeQuery = true)
	Long countByAnioMesDia(@Param("anio") int anio,@Param("mes") int mes,@Param("dia") int dia);
}
