package com.gisnet.erpp.web.api.area;

import java.net.URISyntaxException;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.service.AreaService;

@RestController
@RequestMapping(value = "/api/area", produces = MediaType.APPLICATION_JSON_VALUE)
public class AreaRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AreaService areaService;

	@GetMapping(value = "/")
	public ResponseEntity<List<Area>> list() {
		return ResponseEntity.ok(areaService.findAll());
	}

	@GetMapping(value = "/areas")
	public ResponseEntity<List<Area>> findAll() {
		return ResponseEntity.ok(areaService.findAll());
	}

	@GetMapping(value = "/clasif-acto/{idClasifActo}")
	public ResponseEntity<List<Area>> getByClasifActo(@PathVariable("idClasifActo") Long idClasifActo) {
		return ResponseEntity.ok(areaService.findByClasifActoId(idClasifActo));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Area> getById(@PathVariable Long id) {
		return ResponseEntity.ok(areaService.findOne(id));
	}


}
