package com.gisnet.erpp.web.api.juez;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gisnet.erpp.domain.Juez;
import com.gisnet.erpp.domain.Juez;
import com.gisnet.erpp.service.JuezService;

@RestController
@RequestMapping(value = "/api/juez", produces = MediaType.APPLICATION_JSON_VALUE)
public class JuezRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	JuezService juezService;

	@GetMapping("/jueces")
	public ResponseEntity<Page<Juez>> getAllJueces(Pageable pageable, String paterno, String materno, String nombre, Long materiaId, Integer noJuez) {
		log.debug("paterno=" + paterno);
		log.debug("materno=" + materno);
		log.debug("nombre=" + nombre);
		log.debug("materiaId=" + materiaId);
		Page<Juez> page = juezService.findAllByNombre(paterno, materno, nombre, materiaId, noJuez, pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findOne(@PathVariable ("id") Long id) {
		Juez juez = juezService.findOne(id);
		return new ResponseEntity<>(juez, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<?> saveJuez(@RequestBody Juez juez) {
		return save(juez);
	}
	
	@PutMapping
	public ResponseEntity<?> updateJuez(@RequestBody Juez juez) {
		return save(juez);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAsetnamiento(@PathVariable("id") Long id) {
		try {
			juezService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private ResponseEntity<?> save(Juez juez) {
		try {
			return new ResponseEntity<Juez>(juezService.save(juez), HttpStatus.OK);
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
}
