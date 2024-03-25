package com.gisnet.erpp.web.api.sello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Sello;
import com.gisnet.erpp.service.SelloService;
import com.gisnet.erpp.web.api.foliosrdig.LibroDTO;

@RestController
@RequestMapping(value = "/api/sello", produces = MediaType.APPLICATION_JSON_VALUE)
public class SelloRestController {

	@Autowired
	private SelloService selloService;
	
	@PostMapping("/findTimbres")
	public ResponseEntity<List<Sello>> findPredioByAntecedente(@RequestBody LibroDTO libroDTO){
		return new ResponseEntity<>(selloService.findByLibro(libroDTO), HttpStatus.OK);
	}
}
