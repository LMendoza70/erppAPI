package com.gisnet.erpp.service;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PersonaJuridicaBitacora;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.PersonaJuridicaBitacoraRepository;
import com.gisnet.erpp.security.SecurityUtils;



@Service
public class PersonaJuridicaBitacoraService {

	@Autowired
	PersonaJuridicaBitacoraRepository personaJuridicaBitacoraRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Transactional
	public PersonaJuridicaBitacora save(PersonaJuridica pj,Acto acto) {
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		PersonaJuridicaBitacora bitacora =  new PersonaJuridicaBitacora();
		bitacora.setUsuario(usuario);
		bitacora.setFechaAct(new Date());
		bitacora.setActo(acto);
		bitacora.setPersonaJuridica(pj);
		bitacora.setDenominacionNombre(pj.getDenominacionNombre());
		bitacora.setDuracion(pj.getDuracion());
		bitacora.setFechaApertura(pj.getFechaApertura());
		bitacora.setMunicipio(pj.getMunicipio());
		bitacora.setTipoSociedad(pj.getTipoSociedad());
		bitacora.setTipoColectivo(pj.getTipoColectivo());
		bitacora.setObjeto(pj.getObjeto());
		bitacora.setOficina(pj.getOficina());
		bitacora.setBloqueado(pj.getBloqueado());
		bitacora.setCaratulaActualizada(pj.getCaratulaActualizada());
		bitacora.setDireccion(pj.getDireccion());
		bitacora.setOrgano(pj.getOrgano());
		personaJuridicaBitacoraRepository.save(bitacora);
		return bitacora;
	}
	
	@Transactional
	public Boolean deleteByActoId(Long actoId) {
		Long result = personaJuridicaBitacoraRepository.deleteByActoId(actoId);
		return result !=null && result > 0 ? true: false;
	}
	
	public Optional<PersonaJuridicaBitacora> findByActoId(Long actoId){
		return personaJuridicaBitacoraRepository.findFirstByActoId(actoId);
	}
	
}
 