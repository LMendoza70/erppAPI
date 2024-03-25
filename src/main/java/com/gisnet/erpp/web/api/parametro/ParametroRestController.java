package com.gisnet.erpp.web.api.parametro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.gisnet.erpp.domain.Parametro;
import com.gisnet.erpp.domain.ParametroCotizadorCosto;
import com.gisnet.erpp.service.ParametroService;
import com.gisnet.erpp.service.UsuarioService;

@RestController
@RequestMapping(value = "api/parametros", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParametroRestController {
	
	@Autowired
	ParametroService parametroService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping ("/buscar-parametros")
	public ResponseEntity<List<Parametro>> getAllParametro(){
		return ResponseEntity.ok(parametroService.findAll());
	}
	
	@GetMapping ("/clave/{clave}")
	public ResponseEntity<Parametro> findOneByClave(@PathVariable String clave ){
		return ResponseEntity.ok(parametroService.findByCve(clave));
	}
	
	@PostMapping ("/update-parametro")
	public ResponseEntity<List<Parametro>> updateParametro ( @RequestBody List <Parametro> parametros ){
		return ResponseEntity.ok(parametroService.updateParametro(parametros));
	}
	
	@PostMapping ("/add-parametro")
	public ResponseEntity<Boolean> addParametro(@RequestBody  Parametro parametro){
		System.out.println("Parametro   ----->" + parametro.getCve());
		return ResponseEntity.ok(parametroService.save(parametro));
	}
	
	@GetMapping ("/diaPortal")
	public ResponseEntity<Boolean> diaPortalValidate(){
		return ResponseEntity.ok(usuarioService.validaUsuarioPortal());
	}
}
