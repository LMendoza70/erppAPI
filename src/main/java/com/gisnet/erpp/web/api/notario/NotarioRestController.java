package com.gisnet.erpp.web.api.notario;

import java.net.URISyntaxException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.service.NotarioService;
import com.gisnet.erpp.domain.TipoNotario;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.service.TipoNotarioService;

@RestController
@RequestMapping(value = "/api/notario", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotarioRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	NotarioService notarioService;
	
	@Autowired
	TipoNotarioService tipoNotarioService;

	@GetMapping("/notarios")
	public ResponseEntity<Page<Notario>> getAllJueces(Pageable pageable, String paterno, String materno, String nombre, Integer noNotario, Long estadoId, Long municipioId) {
		log.debug("paterno=" + paterno);
		log.debug("materno=" + materno);
		log.debug("nombre=" + nombre);
		log.debug("estadoId=" + estadoId);
		log.debug("municipioId=" + estadoId);
		Page<Notario> page = notarioService.findAllByNombre(municipioId, estadoId, paterno, materno, nombre, noNotario, pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Notario> getNotario(@PathVariable ("id") Long id) {
		return new ResponseEntity<>(notarioService.findOne(id), HttpStatus.OK);
	}
	
	@GetMapping("/tipos-notario")
	public ResponseEntity<List<TipoNotario>> getAllTiposNotario() {
		return new ResponseEntity<>(tipoNotarioService.findAll() , HttpStatus.OK);
	}
	
	@PostMapping(path = "/save", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterSaveNotarioTitular(@Valid 
			@RequestParam("persona") String sPersona,
			@RequestParam("notario") String sNotario,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono
		) throws MessagingException {
		Persona persona = new Persona();
		Notario notario = new Notario();
		Direccion direccion = new Direccion();
		Telefono telefono = new Telefono();
		
		try {
			persona = new ObjectMapper().readValue(sPersona, Persona.class);
			notario = new ObjectMapper().readValue(sNotario, Notario.class);
			direccion = new ObjectMapper().readValue(sDireccion, Direccion.class);
			telefono = new ObjectMapper().readValue(sTelefono, Telefono.class);
			notario = notarioService.save(persona, notario, direccion, telefono);
			return new ResponseEntity<>(notario, HttpStatus.OK);
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/findAll/")
	public ResponseEntity<List<Notario>> getAllNotarios() {
		return new ResponseEntity<>(notarioService.findAllNotarios() , HttpStatus.OK);
	}
	
	@PostMapping(value = "/save-bandeja/")
	public ResponseEntity<List<Notario>> saveNotarioBandeja(@RequestBody NotarioMttoDTO notario) {
		return ResponseEntity.ok(notarioService.saveNotarioBandeja(notario));
	}
	
}
