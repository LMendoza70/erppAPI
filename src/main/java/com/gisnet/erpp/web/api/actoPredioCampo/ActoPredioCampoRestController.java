package com.gisnet.erpp.web.api.actoPredioCampo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.service.ActoPredioCampoService;

@RestController
@RequestMapping(value = "/api/acto-predio-campo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActoPredioCampoRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());


	@Autowired
	ActoPredioCampoService actoPredioCampoService;

	@GetMapping(value="/{actoPredioId}/Modulo/{moduloId}")
    public ResponseEntity <Map<Long,Set<ActoPredioCampo>>> findActosModulosCamposByActoPredioIdModuloId(@PathVariable("actoPredioId") Long actoPredioId, @PathVariable("moduloId") Long moduloId){
		return new ResponseEntity<> (actoPredioCampoService.findActosModulosCamposByActoIdModuloId(actoPredioId, moduloId), HttpStatus.OK);
    }

	@PostMapping(value="/UpdateTitulares/{actoPredioId}")
    public ResponseEntity<Boolean> UpdateTitulares(@PathVariable("actoPredioId") Long actoPredioId){
		return new ResponseEntity<> (actoPredioCampoService.UpdateTitulares(actoPredioId), HttpStatus.OK);
    } 
	
}
