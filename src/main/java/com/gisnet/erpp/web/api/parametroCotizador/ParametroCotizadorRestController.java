package com.gisnet.erpp.web.api.parametroCotizador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.ParametroCotizador;
import com.gisnet.erpp.service.ParametroCotizadorService;

@RestController
@RequestMapping(value = "api/parametro-cotizadors", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParametroCotizadorRestController {
	@Autowired
	ParametroCotizadorService parametroCotizadorService;
	
	@GetMapping ("/parametro-cotizador")
	public ResponseEntity<List<ParametroCotizador>> getAllParametroCotizadorCosto (){
		return ResponseEntity.ok(parametroCotizadorService.findAll());
	}
}
