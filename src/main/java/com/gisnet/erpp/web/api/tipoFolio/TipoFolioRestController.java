package com.gisnet.erpp.web.api.tipoFolio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.TipoFolio;

import com.gisnet.erpp.service.TipoFolioService;

@RestController
@RequestMapping(value = "/api/tipo-folio", produces = MediaType.APPLICATION_JSON_VALUE)
public class TipoFolioRestController {

	@Autowired
	private TipoFolioService tipoFolioService;

	@GetMapping(value="/")
    public ResponseEntity<List<TipoFolio>> listAreas() {
        return ResponseEntity.ok(tipoFolioService.findAll());
    }
	
	@GetMapping(value="/all")
    public ResponseEntity<List<TipoFolio>> listTipoFolio() {
        return ResponseEntity.ok(tipoFolioService.findTipoFolios());
    }
}
