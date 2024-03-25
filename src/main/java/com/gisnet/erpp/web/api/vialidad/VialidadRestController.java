package com.gisnet.erpp.web.api.vialidad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Vialidad;
import com.gisnet.erpp.service.VialidadService;

@RestController
@RequestMapping(value = "/api/vialidad", produces = MediaType.APPLICATION_JSON_VALUE)
public class VialidadRestController {

	@Autowired
	VialidadService vialidadService;

	@GetMapping("/")
	public ResponseEntity<Vialidad> getVialidad(Long id) {
		Vialidad vialidad = vialidadService.getVialidad(id);
		return new ResponseEntity<>(vialidad, HttpStatus.OK);
	}
}
