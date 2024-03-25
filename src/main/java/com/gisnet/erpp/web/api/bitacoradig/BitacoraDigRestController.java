package com.gisnet.erpp.web.api.bitacoradig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.BitacoraDig;
import com.gisnet.erpp.service.BitacoraDigService;
import com.gisnet.erpp.web.api.foliosrdig.LibroDTO;

@RestController
@RequestMapping(value = "/api/bitacora-dig", produces = MediaType.APPLICATION_JSON_VALUE)
public class BitacoraDigRestController {

	@Autowired
	private BitacoraDigService bitacoraDigService;
	
	@PostMapping("/findObervaciones")
	public ResponseEntity<List<BitacoraDig>> findPredioByAntecedente(@RequestBody LibroDTO libroDTO){
		return new ResponseEntity<>(bitacoraDigService.findByLibroAndDocumento(libroDTO), HttpStatus.OK);
	}
}
