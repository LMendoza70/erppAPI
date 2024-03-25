package com.gisnet.erpp.web.api.FolioSeccionAuxiliar;

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

import com.gisnet.erpp.domain.AuxiliarPersona;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.service.ActoPredioService;
import com.gisnet.erpp.service.FolioSeccionAuxiliarService;
import com.gisnet.erpp.vo.FolioSeccionAuxiliarAnteVO;



	@RestController
	@RequestMapping(value = "/api/folio-sec-aux", produces = MediaType.APPLICATION_JSON_VALUE)
	public class FolioSeccionAuxiliarRestController {
		
		private static final Logger log = LoggerFactory.getLogger(FolioSeccionAuxiliarRestController.class);

		
		@Autowired
		ActoPredioService actoPredioService;
		
		@Autowired
		private FolioSeccionAuxiliarService folioSeccionAuxiliarService;
		
		
		@GetMapping(value = "/{id}/titulares")
		public ResponseEntity<Set<AuxiliarPersona>> findPersonasbyFolioSeccionAuxiliarId(@PathVariable("id")Long id) {
			log.debug("id=" + id);
			Set<AuxiliarPersona> list = actoPredioService.findPersonasbyFolioSeccionAuxiliarId(id);
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		@GetMapping("/findFolioSeccionAuxByAntecedente/{idAntecedente}")
		public ResponseEntity<FolioSeccionAuxiliar> findFolioSeccionAuxByAntecedente(@PathVariable Long idAntecedente){
			log.debug("Buscando el mueble con id de antecedente: {}",idAntecedente);
			return new ResponseEntity<>(folioSeccionAuxiliarService.findFolioSeccionAuxByAntecedente(idAntecedente), HttpStatus.OK);
		}
						
		@GetMapping("/findFolioSeccionAuxByPrelacion/{idPrelacion}")
		public ResponseEntity<FolioSeccionAuxiliar> findFolioSeccionAuxByPrelacion(@PathVariable Long idPrelacion){
			log.debug("Buscando el folio seccion auxiliar con id de prelacion: {}",idPrelacion);
			return new ResponseEntity<>(folioSeccionAuxiliarService.findFolioSeccionAuxByPrelacion(idPrelacion), HttpStatus.OK);
		}
	}