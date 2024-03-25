package com.gisnet.erpp.web.api.asentamiento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.gisnet.erpp.domain.Asentamiento;
import com.gisnet.erpp.service.AsentamientoService;

@RestController
@RequestMapping(value = "/api/asentamiento", produces = MediaType.APPLICATION_JSON_VALUE)
public class AsentamientoRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AsentamientoService asentamientoService;

	@GetMapping("/asentamientos")
	public ResponseEntity<Page<Asentamiento>> getAllAsentamientos(Pageable pageable, String nombre, Long tipoAsentamientoId, Long estadoId, Long municipioId) {
		log.debug("nombre=" + nombre);
		Page<Asentamiento> page = asentamientoService.findAllByNombre(nombre, tipoAsentamientoId, pageable, estadoId, municipioId);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	
	@PostMapping
	public ResponseEntity<?> saveAsentamiento(@RequestBody Asentamiento asentamiento) {
		return save(asentamiento);
	}
	
	@PutMapping
	public ResponseEntity<?> updateAsentamiento(@RequestBody Asentamiento asentamiento) {
		return save(asentamiento);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAsetnamiento(@PathVariable("id") Long id) {
		try {
			asentamientoService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private ResponseEntity<?> save(Asentamiento asentamiento) {
		try {
			asentamientoService.save(asentamiento);
			return new ResponseEntity<>(asentamiento, HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
