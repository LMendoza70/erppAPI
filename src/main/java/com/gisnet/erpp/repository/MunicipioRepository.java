package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Municipio;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Municipio entity.
 */
@SuppressWarnings("unused")
public interface MunicipioRepository extends JpaRepository<Municipio,Long> {

	//Se agreguego municipio de prueba
	
	List<Municipio> findAllByOrderByNombreAsc();
	
	List<Municipio> findByEstadoIdAndActivoTrueOrderByNombreAsc(Long id);
	Municipio findDistinctByNombreEquals(String nombre);
	
	Municipio findByClaveCatastral(String clave);
	
	 @Query(value= "SELECT m FROM Municipio m WHERE m.estado =:estado AND REPLACE(m.nombre, ' ', '') =:nombre")
		List <Municipio>findMunicipioByEstadodoAndNombre(@Param("nombre") String nombre,@Param("estado") Estado estado);
	
}
