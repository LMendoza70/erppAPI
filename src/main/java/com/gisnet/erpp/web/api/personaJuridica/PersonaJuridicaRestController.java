package com.gisnet.erpp.web.api.personaJuridica;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.service.ActoPredioService;
import com.gisnet.erpp.service.PersonaJuridicaService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.PersonaJuridicaAnteVO;


@RestController
@RequestMapping(value = "/api/persona-juridica", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonaJuridicaRestController {
	
	private static final Logger log = LoggerFactory.getLogger(PersonaJuridicaRestController.class);

	
	@Autowired
	ActoPredioService actoPredioService;
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity <PersonaJuridica> findById(@PathVariable("id")Long id) {
		log.debug("id=" + id);
		PersonaJuridica response = this.personaJuridicaService.findOne(id) ;
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody PersonaJuridica personaJuridica) {
		try {
			return new ResponseEntity<PersonaJuridica>(personaJuridicaService.savePersonaJuridica(personaJuridica), HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping(value = "/{id}/titulares")
	public ResponseEntity<Set<PjPersona>> findPersonasbyPjPersonaId(@PathVariable("id")Long id) {
		log.debug("id=" + id);
		Set<PjPersona> list = actoPredioService.findPersonasbyPjPersonaId(id);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/findPjByAntecedente/{idAntecedente}")
	public ResponseEntity<PersonaJuridica> findPjByAntecedente(@PathVariable Long idAntecedente){
		log.debug("Buscando el mueble con id de antecedente: {}",idAntecedente);
		return new ResponseEntity<>(personaJuridicaService.findByAnteceneteId(idAntecedente).get(), HttpStatus.OK);
	}
			
	@GetMapping("/findPjByPrelacion/{idPrelacion}")
	public ResponseEntity<PersonaJuridica> findPjByPrelacion(@PathVariable Long idPrelacion){
		log.debug("Buscando Persona Juridica con id de prelacion: {}",idPrelacion);
		return new ResponseEntity<>(personaJuridicaService.findPjByPrelacion(idPrelacion), HttpStatus.OK);
	}
	
	@GetMapping("/{actoId}/socios")
	public ResponseEntity<List<PjPersona>> socios(@PathVariable Long actoId){
		ActoPredio actoPredio =  actoPredioService.findMaxVersion(actoId);
		List<PjPersona> pjPersonas =  new ArrayList<PjPersona>();
		if(actoPredio!=null && actoPredio.getPersonaJuridica() != null) {
			pjPersonas =  personaJuridicaService.findSociosOrganosApoderadosActuales(actoPredio.getPersonaJuridica().getId(),Constantes.TipoRelPersona.SOCIO);
		}
		return new ResponseEntity<>(pjPersonas,HttpStatus.OK);
	}
	
	@GetMapping("/{actoId}/organos")
	public ResponseEntity<List<PjPersona>> organos(@PathVariable Long actoId){
		ActoPredio actoPredio =  actoPredioService.findMaxVersion(actoId);
		List<PjPersona> pjPersonas =  new ArrayList<PjPersona>();
		if(actoPredio!=null && actoPredio.getPersonaJuridica() != null) {
			pjPersonas =  personaJuridicaService.findSociosOrganosApoderadosActuales(actoPredio.getPersonaJuridica().getId(),Constantes.TipoRelPersona.ORGANO_ADMINISTRACION);
		}
		return new ResponseEntity<>(pjPersonas,HttpStatus.OK);
	}
	
	
	@GetMapping("/{actoId}/apoderados")
	public ResponseEntity<List<PjPersona>> apoderados(@PathVariable Long actoId){
		ActoPredio actoPredio =  actoPredioService.findMaxVersion(actoId);
		List<PjPersona> pjPersonas =  new ArrayList<PjPersona>();
		if(actoPredio!=null && actoPredio.getPersonaJuridica() != null) {
			pjPersonas =  personaJuridicaService.findSociosOrganosApoderadosActuales(actoPredio.getPersonaJuridica().getId(),Constantes.TipoRelPersona.APODERADO);
		}
		return new ResponseEntity<>(pjPersonas,HttpStatus.OK);
	}

	@PostMapping("/findPjByDenominacion")
	public ResponseEntity<List<PersonaJuridica>> findPjByDenominacion(@RequestBody Persona persona){
		return new ResponseEntity<>(personaJuridicaService.findByDenominacion(persona), HttpStatus.OK);
	}
}
