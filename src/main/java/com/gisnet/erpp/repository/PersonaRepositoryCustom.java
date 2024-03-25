package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gisnet.erpp.domain.Persona;

public interface PersonaRepositoryCustom {
	public abstract Page<Persona> findAllByNombre(String paterno, String materno, String nombre, Pageable pageable);
	public abstract Page<Persona> getAllPersonas(Pageable pageable, String paterno, String materno, String nombre, Long tipoPersonaId, Long isPublica);
	public abstract List<Persona> findPersonaByParams(Persona persona);
}
