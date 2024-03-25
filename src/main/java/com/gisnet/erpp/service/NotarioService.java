package com.gisnet.erpp.service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.MantenimientoNotario;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.TipoPersona;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.web.api.notario.NotarioDTO;
import com.gisnet.erpp.web.api.notario.NotarioMttoDTO;
import com.gisnet.erpp.repository.NotarioRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.MantenimientoNotarioRepository;
import com.gisnet.erpp.repository.TelefonoRepository;

import com.gisnet.erpp.security.PasswordGenerator;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class NotarioService {
	
	@Autowired
	NotarioRepository notarioRepository;
	
	@Autowired
	PersonaRepository personaRepository;
	
	@Autowired
	DireccionRepository direccionRepository;
	
	@Autowired
	TelefonoRepository telefonoRepository;
	
	@Autowired
	MantenimientoNotarioRepository mantenimientoNotarioRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Transactional(readOnly = true)
	public Page<Notario> findAllByNombre(Long municipioId, Long estadoId, String paterno, String materno, String nombre, Integer noNotario,
			Pageable pageable) {
		return notarioRepository.findAllByNombre(municipioId, estadoId, paterno, materno, nombre, noNotario, pageable);
	}

	public Notario findOne(Long id) {
		return notarioRepository.findOne(id);
	}
	
	public List<NotarioDTO> findAllByMunicipioId(Long municipioId) {
		return notarioRepository.findAllByMunicipioId(municipioId).stream().map(NotarioDTO::new).collect(Collectors.toList());
	}
	
	
	@Transactional
	public Notario save(Persona persona, Notario notario, Direccion direccion, Telefono telefono)throws Exception {		
		try {
			personaRepository.saveAndFlush(persona);	
			notario.setPersona(persona);
			notarioRepository.saveAndFlush(notario);
			direccion.setNotario(notario);
			direccion.setPersona(persona);
			telefono.setNotario(notario);
			direccionRepository.saveAndFlush(direccion);
			telefonoRepository.saveAndFlush(telefono);
		}
		catch (Exception e) {
			throw new Exception("no se pudo guardar el notario");
		}
        
		return notario;
	}
	
	public List<Notario> findAllNotariosActivos(){
		return notarioRepository.findAllByActivo(true);
	}
	
	public List<Notario> findAllNotarios(){
		return notarioRepository.findAll();
	}
	
	public List<Notario> saveNotarioBandeja(NotarioMttoDTO notario){
		
		
		
		System.out.println("Notario ------- > " + notario.getNotario());
		System.out.println("Motivo ------- > " + notario.getMotivo());
		
		
		TipoPersona tipoPersona = new TipoPersona();
		Nacionalidad nacionalidad = new Nacionalidad();
		tipoPersona.setId(1L);
		nacionalidad.setId(87L);
		notario.getNotario().getPersona().setTipoPersona(tipoPersona);
		notario.getNotario().getPersona().setNacionalidad(nacionalidad);
		notario.getNotario().getPersona().setActivo(true);
		
		if(notario.getNotario().getId() == null) {
			notario.getNotario().setActivo(true);	
		}
		if(notario.getMotivo() != "" &&  notario.getMotivo() != null){
			MantenimientoNotario mantenimiento = new MantenimientoNotario();
			Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			String movimiento = "";
			
			mantenimiento.setFecha(new Date());
			mantenimiento.setUsuario(usuario);
			mantenimiento.setNotario(notario.getNotario());
			mantenimiento.setMotivo(notario.getMotivo());
			movimiento = notario.getNotario().isActivo() == true ? "Activación" : "Inactivación" ;
			mantenimiento.setMovimiento(movimiento);
			
			mantenimientoNotarioRepository.save(mantenimiento);
		}
		
		
		personaRepository.saveAndFlush(notario.getNotario().getPersona());	
		notario.setNotario(notarioRepository.saveAndFlush(notario.getNotario()));
	
		return notarioRepository.findAllById(notario.getNotario().getId());
	}
	
}
