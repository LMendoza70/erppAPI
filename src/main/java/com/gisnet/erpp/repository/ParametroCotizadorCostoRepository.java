package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.ParametroCotizador;
import com.gisnet.erpp.domain.ParametroCotizadorCosto;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ParametroCotizadorCosto entity.
 */
@SuppressWarnings("unused")
public interface ParametroCotizadorCostoRepository extends JpaRepository<ParametroCotizadorCosto,Long> {

	ParametroCotizadorCosto findByAnioAndParametroCotizadorNombre(Integer anio,String nombreParametro);
	
	@EntityGraph(attributePaths = "parametroCotizador")
	List<ParametroCotizadorCosto> findByAnio(Integer anio);
	
	 @Query(value= "SELECT pcc FROM ParametroCotizadorCosto pcc WHERE pcc.anio =:anio  AND pcc.parametroCotizador =:ParametroCotizadorId")
     List<ParametroCotizadorCosto> findByAnioAndParametroCotizadorCosto( @Param("anio") Integer anio,@Param("ParametroCotizadorId") ParametroCotizador ParametroCotizadorId);
}
