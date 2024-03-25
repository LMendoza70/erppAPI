package com.gisnet.erpp.web.api.contratante;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.service.PrelacionContratanteService;
import com.gisnet.erpp.vo.PrelacionContratanteVO;

@RestController
@RequestMapping(value = "/api/contratante", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContratanteRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PrelacionContratanteService contratanteService;

	@GetMapping("/contratantes")
	public ResponseEntity<Page<PrelacionContratanteVO>> getAllContratantes(Pageable pageable, String paterno, String materno, String nombre) {
		log.debug("paterno=" + paterno);
		log.debug("materno=" + materno);
		log.debug("nombre=" + nombre);
		Page<PrelacionContratanteVO> page = contratanteService.findAllByNombre(paterno, materno, nombre, pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
}
