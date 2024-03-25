package com.gisnet.erpp.web.api.referencia;

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

import com.gisnet.erpp.service.ReferenciaWebService;
import com.gisnet.erpp.ws.referencia.client.Referencia;

@RestController
@RequestMapping(value = "/api/referencia", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReferenciaRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ReferenciaWebService referenciaWebService;

	@GetMapping("/{referencia}")
	public ResponseEntity<Referencia> consulta(@PathVariable("referencia") String referencia) {
		Referencia result = referenciaWebService.getConsulta(referencia);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
