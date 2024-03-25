package com.gisnet.erpp.repository;

import java.util.Set;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.TipoPersona;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Persona entity.
 */
@SuppressWarnings("unused")
public interface PersonaRepository extends JpaRepository<Persona,Long>, PersonaRepositoryCustom {


	@Query(
		value = "SELECT pe.* "+
			" from persona pe " +
			" WHERE  fuzzystrmatch(trim(pe.nombre) || ' ' || trim(pe.paterno) || ' ' || trim(pe.materno), trim(:ncomp)) >60 " +
			" ORDER BY fuzzystrmatch(trim(pe.nombre) || ' ' || trim(pe.paterno) || ' ' || trim(pe.materno), trim(:ncomp)) desc limit 10" +
			" limit 10 " ,
		nativeQuery=true
	) 
	public Set<Persona> findSimilarByFullName(@Param("ncomp") String fullName);
	
	public Persona findByEmail( String mail);
	
	public Optional<Persona> findByCurp(String curp);
	public Optional<Persona> findByRfc(String rfc);
	public Persona findByNombre( String nombre);
	
	public Set<Persona> findByNombreContainingIgnoreCaseAndTipoPersona(String nombre,TipoPersona tipoPersona);

}
