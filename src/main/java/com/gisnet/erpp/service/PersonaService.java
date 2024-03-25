package com.gisnet.erpp.service;

import java.util.Set;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.IdentificacionRepository;
import com.gisnet.erpp.repository.TelefonoRepository;

@Service
public class PersonaService{
	@Autowired
	PersonaRepository personaRepository;
	@Autowired
	DireccionRepository direccionRepository;
	@Autowired
	IdentificacionRepository identificacionRepository;
	@Autowired
	TelefonoRepository telefonoRepository;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Page<Persona> findAllByNombre(String paterno, String materno, String nombre, Pageable pageable) {
		return personaRepository.findAllByNombre(paterno, materno, nombre, pageable);
	}

	@Transactional
	public Long save(Persona persona) {
		try {
			personaRepository.save(persona);
			saveDirecciones(persona.getDireccionesParaPersonas(), persona);
		}catch (Exception except) {
			except.printStackTrace();
		}
			return persona.getId();
	}
	
	@Transactional
	public Long update(Persona persona) {
		this.deleteRelatedElements(persona);
		Persona newPersona = new Persona();
		try {
			newPersona.setId(persona.getId());
			newPersona.setActivo(persona.isActivo());
			newPersona.setCurp(persona.getCurp());
			newPersona.setEmail(persona.getEmail());
			newPersona.setFechaNac(persona.getFechaNac());
			newPersona.setMaterno(persona.getMaterno());
			newPersona.setPaterno(persona.getPaterno());
			newPersona.setNombre(persona.getNombre());
			newPersona.setNacionalidad(persona.getNacionalidad());
			newPersona.setNuTit(persona.getNuTit());
			newPersona.setPublica(persona.isPublica());
			newPersona.setRfc(persona.getRfc());
			newPersona.setTipoPersona(persona.getTipoPersona());
			personaRepository.saveAndFlush(newPersona);
			
			persona.getDireccionesParaPersonas().forEach((x)-> {
				x.setPersona(newPersona);
				x.setActivo(true);
				this.direccionRepository.save(x);
			});
			persona.getIdentificacionesParaPersonas().forEach((x) -> {
				x.setPersona(newPersona);
				identificacionRepository.save(x);
			});
			persona.getTelefonosParaPersonas().forEach((x) -> {
				x.setPersona(newPersona);
				telefonoRepository.save(x);
			});
			direccionRepository.flush();
			identificacionRepository.flush();
			telefonoRepository.flush();
		}catch (Exception except) {
			except.printStackTrace();
		}
			return persona.getId();
	}
	
	@Transactional
	private void deleteRelatedElements(Persona persona) {
		Persona personaAnterior = personaRepository.findOne(persona.getId());
		Set<Direccion> tmpDirList = new HashSet<>();
		Set<Identificacion> tmpIdenList = new HashSet<>();
		Set<Telefono> tmpTelList = new HashSet<>();
		
		for(Direccion tmpDir: personaAnterior.getDireccionesParaPersonas()) {
			tmpDirList.add(tmpDir);
		}
		log.debug(String.valueOf(tmpDirList.size()));
		for(Direccion tmpDir : tmpDirList) {
			personaAnterior.removeDireccionParaPersona(tmpDir);
		}

		for(Identificacion tmpIden: personaAnterior.getIdentificacionesParaPersonas()) {
			tmpIdenList.add(tmpIden);
		}
		log.debug(String.valueOf(tmpIdenList.size()));
		for(Identificacion tmpIden : tmpIdenList) {
			personaAnterior.removeIdentificacionParaPersona(tmpIden);
		}

		for(Telefono tmpTel: personaAnterior.getTelefonosParaPersonas()) {
			tmpTelList.add(tmpTel);
		}
		log.debug(String.valueOf(tmpTelList.size()));
		for(Telefono tmpTel : tmpTelList) {
			personaAnterior.removeTelefonoParaPersona(tmpTel);
		}		
		personaRepository.saveAndFlush(personaAnterior);
	}

	@Transactional
	public Set<Direccion> saveDirecciones(Set<Direccion> direcciones, Persona persona) {
		Set <Direccion> savedDirecciones =  new HashSet();
		for(Direccion direccion : direcciones ) {

			direccion.setPersona(persona);
			
			direccionRepository.save(direccion);
			System.out.println(direccion.toString());
			savedDirecciones.add(direccion);
		}
		
		return savedDirecciones;

	}
	/*
	@Transactional
	private Set<Identificacion> saveIdentificaciones(Set<Identificacion> identificaciones) {
		Set <Identificacion> savedIdentificaciones =  new HashSet();
		for(Identificacion identificacion : identificaciones ) {
			identificacionRepository.save(identificacion);
			System.out.println(identificacion.toString());
			savedIdentificaciones.add(identificacion);
		}
		
		return savedIdentificaciones;

	}
	*/
	public Persona findOneById(Long id) {
		return personaRepository.findOne(id);
	}

	
	public Persona findByEmail(String  mail) {
		return personaRepository.findByEmail(mail);
	}
	
	public Set<Persona> findAllSimilar(String nombre, String paterno, String materno) {
		String fullName = nombre.trim();
		if(paterno != null) {
			fullName += paterno.trim();
		}
		if(materno != null) {
			fullName += materno.trim();
		}

		return personaRepository.findSimilarByFullName(fullName);
	}

	@Transactional(readOnly = true)
	public Page<Persona> getAllPersonas(Pageable pageable,  String paterno, String materno, String nombre, Long tipoPersonaId, Long isPublica) {
		return personaRepository.getAllPersonas(pageable, paterno, materno, nombre, tipoPersonaId, isPublica);
	}
	
}
