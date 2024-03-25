package com.gisnet.erpp.web.api.identificacion;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.service.IdentificacionService;

@RestController
@RequestMapping(value = "/api/identificacion", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentificacionRestController {
//	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IdentificacionService identificacionService;

	@GetMapping("/identificaciones")
	public ResponseEntity<Page<Identificacion>> getAllIdentificaciones(Pageable pageable, String valor, Long personaId, Long tipoIdenId ) {
		Page<Identificacion> page = identificacionService.findAllByNombre(pageable, valor, personaId, tipoIdenId);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> saveIdentificacion(@RequestBody Identificacion identificacion) {
		return save(identificacion);
	}
	
	@PutMapping
	public ResponseEntity<?> updateIdentificacion(@RequestBody Identificacion identificacion) {
		return save(identificacion);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAsetnamiento(@PathVariable("id") Long id) {
		try {
			identificacionService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private ResponseEntity<?> save(Identificacion identificacion) {
		try {
			identificacionService.save(identificacion);
			return new ResponseEntity<>(identificacion, HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
