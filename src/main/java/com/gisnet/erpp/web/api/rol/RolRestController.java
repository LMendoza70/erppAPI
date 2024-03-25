package com.gisnet.erpp.web.api.rol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.domain.Funcion;
import com.gisnet.erpp.domain.FuncionRol;
import com.gisnet.erpp.service.RolService;
import com.gisnet.erpp.vo.PersonaVO;
import com.gisnet.erpp.vo.RolVO;

@RestController
@RequestMapping(value = "/api/rol", produces = MediaType.APPLICATION_JSON_VALUE)
public class RolRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RolService rolService;

	@GetMapping("/roles")
	public ResponseEntity<List<Rol>> getAllRoles() {
		List<Rol> roles = rolService.findAll();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}
	
	@GetMapping("/roles/rpp")
	public ResponseEntity<List<Rol>> getAllRppRoles() {
		List<Rol> roles = rolService.findAllRppRolesAssignableByUserName();
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}
	
	
	@GetMapping("/{id}/areas")
	public ResponseEntity<List<AreaRol>> getAreasByRolId(@PathVariable ("id") Long id) {
		List<AreaRol> areasRol = rolService.findAllAreasByRolId(id);
		return new ResponseEntity<>(areasRol, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Rol> findOneById(@PathVariable ("id") Long id) {
		Rol rol = rolService.findOneById(id);
		return new ResponseEntity<>(rol, HttpStatus.OK);
	}

	@GetMapping("/roles/custom-search")
	public ResponseEntity<Page<Rol>> findAllBy(Pageable pageable,  String nombre, Integer activo) {
		Boolean bactivo = null;
		if (activo != null) { 
			if(activo == 1) {
				bactivo = true;
			}
			else {
				bactivo = false;
			}
		}


		Page<Rol> page = rolService.findAllBy(pageable, nombre, bactivo);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@PostMapping("/save")
	public ResponseEntity<Long> createRol(@RequestBody Rol rol) throws URISyntaxException {
		Long nuevoRolId = rolService.save(rol);
		return new ResponseEntity<>(nuevoRolId, HttpStatus.OK);
	}
	
	@PostMapping("/update-funciones")
	public ResponseEntity<Rol> updateRolFunctions(@RequestBody RolVO rol) throws URISyntaxException {
		return new ResponseEntity<>(rolService.updateFunciones(rol.transformIntoRol()), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/funciones")
	public ResponseEntity<Set<FuncionRol>> findAllFunciones(@PathVariable("id") Long id) throws URISyntaxException {
		Rol rol = rolService.findOneById(id);
		return new ResponseEntity<>(rol.getFuncionRolesParaRols(), HttpStatus.OK);
	}
	
	@GetMapping("/funciones-padre")
	public ResponseEntity<Set<Funcion>> findAllFuncionesPadre() throws URISyntaxException {
		Set<Funcion> padres = new HashSet<Funcion>(); 
				padres = rolService.findAllFunciones().stream().filter(x -> x.getFuncionPadre() == null).collect(Collectors.toSet());
		return new ResponseEntity<>(padres, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/funciones-padre")
	public ResponseEntity<Set<FuncionRol>> findAllFuncionesRolPadreByRolId(@PathVariable("id") Long id) throws URISyntaxException {
		Set<FuncionRol> padres = new HashSet<>(); 
		padres = rolService.getAllFuncionesRolByRolId(id, false, true);
		return new ResponseEntity<>(padres, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/funciones-hijas")
	public ResponseEntity<Set<FuncionRol>> findAllFuncionesRolHijasByRolId(@PathVariable("id") Long id) throws URISyntaxException {
		Set<FuncionRol> padres = new HashSet<>(); 
		padres = rolService.getAllFuncionesRolByRolId(id, true, false);
		return new ResponseEntity<>(padres, HttpStatus.OK);
	}
	
	@GetMapping("/funciones-hijas")
	public ResponseEntity<Set<Funcion>> findAllFuncionesHijas() throws URISyntaxException {
		Set<Funcion> hijas = new HashSet<Funcion>(); 
				hijas = rolService.findAllFunciones().stream().filter(x -> x.getFuncionPadre() != null).collect(Collectors.toSet());
		return new ResponseEntity<>(hijas, HttpStatus.OK);
	}
}
