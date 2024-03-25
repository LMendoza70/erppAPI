package com.gisnet.erpp.web.api.oficina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;




import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.SeccionPorOficina;
import com.gisnet.erpp.service.OficinaService;
import com.gisnet.erpp.vo.AntecedenteVO;


@RestController
@RequestMapping(value = "/api/oficina", produces = MediaType.APPLICATION_JSON_VALUE)
public class OficinaRestController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OficinaService oficinaService;
	
	@GetMapping("/oficinas")
	public ResponseEntity<List<Oficina>> getAllOficinas() {
		return new ResponseEntity<>(oficinaService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{oficinaId}/secciones")
	public ResponseEntity<?> getAllSeccionesByOficinaId(@PathVariable Long oficinaId) {
		return new ResponseEntity<>(oficinaService.findAllSeccionesByOficinaId(oficinaId), HttpStatus.OK);
	}
}
