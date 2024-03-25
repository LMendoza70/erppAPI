package com.gisnet.erpp.web.api.actopredio;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.service.ActoPredioService;

@RestController
@RequestMapping(value = "/api/acto-predios", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActoPredioRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ActoPredioService actoPredioService;


	@PostMapping()
	public ResponseEntity<Long> creaActoParaActoPredio(@RequestBody ActoPredio actoPredio) {
//	log.debug("Creando actoPredio con prelacionId ={} de tipo folio={}, tipo_acto={}, predioId={} ", actoPredio.getActo().getPrelacion().getId(), actoPredio.getTipoFolio().getId(), actoPredio.getActo().getTipoActo().getId(), actoPredio.getPredio().getId());
	log.debug("Creando actoPredio con prelacionId");
		actoPredioService.creaActoParaActoPredio(actoPredio);
	
		return new ResponseEntity<>(actoPredio.getId(), HttpStatus.OK);

	}
	
	@GetMapping(value="/{id}/campos")
    public ResponseEntity <?> findActoPredioCamposByActoPredioId(@PathVariable("id") Long id){
		return new ResponseEntity<> (actoPredioService.findAllActoPredioCamposByActoPredioId(id), HttpStatus.OK);
    }

}
