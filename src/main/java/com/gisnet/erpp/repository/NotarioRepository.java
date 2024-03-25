package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Persona;

/**
 * Spring Data JPA repository for the Notario entity.
 */
@SuppressWarnings("unused")
public interface NotarioRepository extends JpaRepository<Notario,Long>, NotarioRepositoryCustom {
	
	public abstract List<Notario> findAllByMunicipioId(Long municipioId);
	public abstract List<Notario> findAllByActivo(Boolean activo);
	public abstract List<Notario> findAllById(Long id);
	public abstract Notario findById(Long id);
	
	public abstract Notario findByPersona( Persona persona );
}
