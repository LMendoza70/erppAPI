package com.gisnet.erpp.web.api.actoRequisito;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gisnet.erpp.domain.ActoRequisito;
import com.gisnet.erpp.service.ActoRequisitoService;

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


@RestController
@RequestMapping(value = "/api/acto-requisitos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActoRequisitoRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
 	ActoRequisitoService actoRequisitoService;

	@PostMapping()
	
	  public  ResponseEntity<ActoRequisito> createActoRequisito(@RequestBody ActoRequisito actoRequisito) {
		ActoRequisito actoRequisito2  = null;
		System.out.println("ENTRA A CONTROLLER URL");
		try{
			actoRequisito2 =  actoRequisitoService.saveActoRequisito(actoRequisito);
			
		  }
		  catch(Exception e){
			e.printStackTrace();
		  }
		 return new ResponseEntity<ActoRequisito>(actoRequisito2,HttpStatus.OK);
	  }
    

}
