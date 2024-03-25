package com.gisnet.erpp.web.api.servicio;

import java.math.BigDecimal;
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

import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.service.ServicioService;
import com.gisnet.erpp.vo.ServicioVO;

@RestController
@RequestMapping(value = "/api/servicio", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServicioRestController {

	@Autowired
	private ServicioService servicioService;
	

	@GetMapping(value = "/")
	public ResponseEntity<List<Servicio>> list() {
		return ResponseEntity.ok(servicioService.findAll());
	}
		

	@GetMapping(value = "/area/{areaId}")
	public ResponseEntity<List<Servicio>> findByAreaId(@PathVariable("areaId") Long areaId) {
		return ResponseEntity.ok(servicioService.findByAreaId(areaId));
	}
		
	@PostMapping("/areaClasifActo")
	public ResponseEntity<List<Servicio>> findByAreaIdAndClasifActoId(@RequestBody Servicio servicio) {
		return ResponseEntity.ok(servicioService.findByAreaIdAndClasifActoId(servicio));
	}
	
	@PostMapping("/areaClasifActoTipoActo")
	public ResponseEntity<List<Servicio>> findByAreaIdAndClasifActoIdAndTipoActoId(@RequestBody Servicio servicio) {
		return ResponseEntity.ok(servicioService.findByAreaIdAndClasifActoIdAndTipoActoId(servicio));
	}


	@GetMapping("/tipoUsuario/{acceso}")
	public ResponseEntity<List<Servicio>> listServicioByTipoUsuario(@PathVariable("acceso") String acceso) {
		return ResponseEntity.ok(servicioService.findAllByTipoUsuario(acceso));
	}
	
	@GetMapping("/tipoUsuario/{acceso}/area/{areaId}")
	public ResponseEntity<List<Servicio>> listServicioByTipoUsuarioAndArea(@PathVariable("acceso") String acceso, @PathVariable("areaId")  Long area) {
		return ResponseEntity.ok(servicioService.findAllByTipoUsuarioAndArea(acceso,area));
	}
}
