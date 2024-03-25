package com.gisnet.erpp.web.api.clasifActo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.service.ClasifActoService;

@RestController
@RequestMapping(value = "/api/clasif-acto", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClasifActoRestController {

	@Autowired
	private ClasifActoService clasifActoService;

		
	@GetMapping("/clasif-actos")
	public ResponseEntity<List<ClasifActo>> findAll() {
		List<ClasifActo> clasifActo = clasifActoService.findAll();
		return new ResponseEntity<>(clasifActo, HttpStatus.OK);
	}

}
