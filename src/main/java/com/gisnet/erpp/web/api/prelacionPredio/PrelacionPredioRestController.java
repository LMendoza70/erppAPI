package com.gisnet.erpp.web.api.prelacionPredio;

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
import org.springframework.dao.DataAccessException;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.StatusActo;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.service.PredioPersonaService;
import com.gisnet.erpp.service.PrelacionPredioService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.MessageObject;


@RestController
@RequestMapping(value = "/api/prelacion-predio", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrelacionPredioRestController {
	
	private static final Logger log = LoggerFactory.getLogger(PrelacionPredioRestController.class);
	
	@Autowired
	PrelacionPredioService prelacionPredioService;
	
	@Autowired
	PredioPersonaService predioPersonaService;
	
	@Autowired
	PersonaRepository personaRepository;
	
	@Autowired
    PredioPersonaRepository predioPersonaRepository;
	
	@Autowired
	ActoRepository actoRepository;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<PrelacionPredioDTO> findPrelacionPredioDTO(@PathVariable("id")Long id) {
		return new ResponseEntity<>(prelacionPredioService.findPrelacionPredioDTO(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getPredioPersona/{prelacionId}/{predioId}")
	public ResponseEntity<Set<PredioPersona>> getPredioPersona(
			@PathVariable("prelacionId") Long prelacionId,
			@PathVariable("predioId") Long predioId ) {
		Set<PredioPersona> list = predioPersonaService.getPredioPersona(predioId,prelacionId);
		log.debug( "===> getPredioPersona = "+list.size() );
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PostMapping("/savePredioPersona")
	public ResponseEntity<?> savePredioPersona( @RequestBody PredioPersona[] predioPersonas ) {
		log.debug( "===> predioPersonas = "+predioPersonas.length );
		
		List<PredioPersona> predioPersonasList = new ArrayList<PredioPersona>();
		
		PredioPersona predioPersona;
		Persona persona;
		for( PredioPersona pp : predioPersonas ) {
			log.debug( "===> getTipoPersona = "+pp.getPersona().getTipoPersona() );
			persona = personaRepository.saveAndFlush( pp.getPersona() );
			pp.setPersona(persona);
			pp.setPrimerRegistro( true );
			predioPersona = predioPersonaService.savePredioPersona( pp );
			predioPersonasList.add( predioPersona );
		}
		
		if( predioPersonas.length > 0 ) {
			Acto acto = predioPersonas[0].getActoPredio().getActo();
			acto = actoRepository.findOne( acto.getId() );
			if( acto.getStatusActo().getId() != Constantes.StatusActo.ACTIVO.getIdStatusActo() ) {
				StatusActo sa = new StatusActo();
				sa.setId( Constantes.StatusActo.ACTIVO.getIdStatusActo() );
				acto.setStatusActo( sa );
				actoRepository.saveAndFlush( acto );
			}
		}
		
		return new ResponseEntity<>(predioPersonasList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/actualizaPredioPersona/{actoPredioId}/{newActoPredioId}")
	public ResponseEntity<?> actualizaPredioPersona( @PathVariable("actoPredioId") Long actoPredioId,
													 @PathVariable("newActoPredioId") Long newActoPredioId ) {
		log.debug( "===> actualizaPredioPersona ("+actoPredioId+","+newActoPredioId+")" );
		
		ActoPredio ap = new ActoPredio();
		ap.setId(newActoPredioId);

		List<PredioPersona> predioPersonas = predioPersonaRepository.findAllByActoPredioId(actoPredioId);
		for( PredioPersona pp : predioPersonas ) {
			pp.setActoPredio( ap );
			predioPersonaService.savePredioPersona(pp);
		}
		
		return new ResponseEntity<>(1L, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> save(@RequestBody PrelacionPredio prelacionPredio) {
		try {
			return new ResponseEntity<PrelacionPredio>(prelacionPredioService.save(prelacionPredio), HttpStatus.OK);	
		}catch (DataAccessException e) {
			return new ResponseEntity<MessageObject>(new MessageObject("fallo al ligar la prelacion con el folio \n" + e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		catch(Exception e) {
			return new ResponseEntity<MessageObject>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST); 
		}
	}

	@GetMapping(value = "/predio/{id}/tipoFolio/{idTF}")
	public ResponseEntity<PrelacionPredioDTO> findPrelacionPredioDTOByPredio(@PathVariable("id")Long id, @PathVariable("idTF") Long idTF) {
		return new ResponseEntity<>(prelacionPredioService.findPrelacionPredioDTOByPredio(id,idTF), HttpStatus.OK);
	}
}
