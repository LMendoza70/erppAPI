package com.gisnet.erpp.web.api.tipoDocumento;

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

import java.net.URISyntaxException;
import java.util.List;

import com.gisnet.erpp.domain.TipoDocumento;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.service.TipoDocumentoService;

@RestController
@RequestMapping(value = "/api/tipo-documento", produces = MediaType.APPLICATION_JSON_VALUE)
public class TipoDocumentoRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TipoDocumentoService tipoDocumentoService;

	@GetMapping("/tipos-documento")
    public ResponseEntity<List<TipoDocumento>> findAll() {
		List<TipoDocumento> result = tipoDocumentoService.findAll();
		return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
