package com.gisnet.erpp.web.api.parametroCotizadorCosto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.ParametroCotizadorCosto;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.service.ParametroCotizadorCostoService;

@RestController
@RequestMapping(value = "/api/parametro-cotizador-costos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParametroCotizadorCostoRestController {
	@Autowired
	ParametroCotizadorCostoService parametroCotizadorCostoService;
	
	/*
	 @GetMapping ("/cotizador-costo")
	public ResponseEntity<ParametroCotizadorCostoDTO> getAllParametroCotizadorCosto (){
		return ResponseEntity.ok(parametroCotizadorCostoService.findParamCoCosto());
	}*/
	
	
	@GetMapping ("/cotizador-costo")
	public ResponseEntity<List<ParametroCotizadorCosto>> getAllParametroCotizadorCosto (){
		return ResponseEntity.ok(parametroCotizadorCostoService.findParamCoCosto());
	}
	
	@PostMapping ("/update-paramCotizadorCo")
	public ResponseEntity<List<ParametroCotizadorCosto>> updateParametroCotizadorCosto ( @RequestBody List <ParametroCotizadorCosto> parametroCoCo ){
		return ResponseEntity.ok(parametroCotizadorCostoService.updateParametroCoCo(parametroCoCo));
	}
	
	@PostMapping ("/add-paramCotizadorCo")
	public ResponseEntity<Boolean> addParametroCotizadorCosto(@RequestBody  ParametroCotizadorCosto parametroCotizadorCosto){
		
		System.out.println("PCC    ----->" + parametroCotizadorCosto.getParametroCotizador());
		return ResponseEntity.ok(parametroCotizadorCostoService.save(parametroCotizadorCosto));
	}
}