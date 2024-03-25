package com.gisnet.erpp.web.api.prelacionAnte;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.PrelacionAnte;
import com.gisnet.erpp.service.PrelacionAnteService;


@RestController
@RequestMapping(value = "/api/prelacion-ante", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrelacionAnteRestController {
	
	private static final Logger log = LoggerFactory.getLogger(PrelacionAnteRestController.class);
	
	@Autowired
	PrelacionAnteService prelacionAnteService;
	
	@PostMapping("/create-desde-antecedente/{prelacionId}")
	public ResponseEntity<PrelacionAnte> createFromAntecedente(@RequestBody Antecedente antecedente, @PathVariable Long prelacionId){
		log.debug("antecedente={}", antecedente);
		log.debug("prelacionId={}", prelacionId);
	
		return new ResponseEntity<>(prelacionAnteService.createFromAntecedente(antecedente, prelacionId), HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<PrelacionAnte> findOne(@PathVariable("id")Long id) {
		return new ResponseEntity<>(prelacionAnteService.findOne(id), HttpStatus.OK);
	}
	
	@PostMapping("/savePredioAnteDTO")
	public ResponseEntity<Long> savePredioAndAntecedente(@RequestBody PrelacionAnteDTO prelacionAnteDTO){
		log.debug("antecedente={}", prelacionAnteDTO.getAntecedente());
		log.debug("predio={}", prelacionAnteDTO.getPredioDTO());

		return new ResponseEntity<>(prelacionAnteService.save(prelacionAnteDTO), HttpStatus.CREATED);
	}
	
	@PostMapping("/updatePredioDTO")
	public ResponseEntity<Long> updatePredioAndAntecedente(@RequestBody PrelacionAnteDTO prelacionAnteDTO){
		log.debug("updatePredio antecedente={}", prelacionAnteDTO.getAntecedente());
		log.debug("updatePredio predio={}", prelacionAnteDTO.getPredioDTO());

		return new ResponseEntity<>(prelacionAnteService.updatePredio(prelacionAnteDTO), HttpStatus.CREATED);
	}

	@PostMapping("/save-validado")
	public ResponseEntity<Long> saveValidado(@RequestBody PrelacionAnteDTO prelacionAnteDTO){
		return new ResponseEntity<>(prelacionAnteService.validado(prelacionAnteDTO), HttpStatus.CREATED);
	}
		
	@GetMapping("/findPredioAnteDTOByPrelacionAnteId/{prelacionAnteId}")
	public ResponseEntity<PrelacionAnteDTO> findPredioAnteDTOByPrelacionAnteId(@PathVariable Long prelacionAnteId){
		return new ResponseEntity<>(prelacionAnteService.findPredioAnteDTOByPrelacionAnteId(prelacionAnteId), HttpStatus.OK);
	}

	@GetMapping("/deletePrelacionAnteById/{prelacionAnteId}")
	public ResponseEntity<PrelacionAnte> deletePrelacionAnteById(@PathVariable Long prelacionAnteId){
		return new ResponseEntity<PrelacionAnte>(prelacionAnteService.deletePrelacionAnteById(prelacionAnteId), HttpStatus.OK);
	}
	
	@PostMapping("/updatePrelacionAnteCyv")
	public ResponseEntity<Long> savePrelAnte(@RequestBody PrelacionAnte prelacionAnte){
		log.debug("antecedente={}", prelacionAnte.getId());
		return new ResponseEntity<>(prelacionAnteService.updatePrelacionAnte(prelacionAnte), HttpStatus.CREATED);
	}

}
