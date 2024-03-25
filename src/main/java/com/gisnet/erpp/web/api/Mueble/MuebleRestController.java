package com.gisnet.erpp.web.api.Mueble;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.MueblePersona;
import com.gisnet.erpp.service.ActoPredioService;
import com.gisnet.erpp.service.MuebleService;


@RestController
@RequestMapping(value = "/api/mueble", produces = MediaType.APPLICATION_JSON_VALUE)
public class MuebleRestController {
	
	private static final Logger log = LoggerFactory.getLogger(MuebleRestController.class);

	
	@Autowired
	ActoPredioService actoPredioService;
	
	@Autowired
	private MuebleService muebleService;
	
	
	@GetMapping(value = "/{id}/titulares")
	public ResponseEntity<Set<MueblePersona>> findPersonasbyMuebleId(@PathVariable("id")Long id) {
		log.debug("id=" + id);
		Set<MueblePersona> list = actoPredioService.findPersonasbyMuebleId(id);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/findMuebleByAntecedente/{idAntecedente}")
	public ResponseEntity<Mueble> findMuebleByAntecedente(@PathVariable Long idAntecedente){
		log.debug("Buscando el mueble con id de antecedente: {}",idAntecedente);
		return new ResponseEntity<>(muebleService.findMuebleByAntecedente(idAntecedente), HttpStatus.OK);
	}
			
	@GetMapping("/findMuebleByPrelacion/{idPrelacion}")
	public ResponseEntity<Mueble> findMuebleByPrelacion(@PathVariable Long idPrelacion){
		log.debug("Buscando el mueble con id de prelacion: {}",idPrelacion);
		return new ResponseEntity<>(muebleService.findMuebleByPrelacion(idPrelacion), HttpStatus.OK);
	}
}