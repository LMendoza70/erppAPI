package com.gisnet.erpp.web.api.fichaRegistral;

import java.util.ArrayList;
import java.util.List;

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

import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.repository.MunicipioRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.service.FichaRegistralService;
import com.gisnet.erpp.vo.fichaRegistral.FichaRegistralVO;
import com.gisnet.erpp.vo.fichaRegistral.PeticionFichaVO;

@RestController
@RequestMapping(value = "/api/ficha-registral", produces = MediaType.APPLICATION_JSON_VALUE)

public class FichaRegistralRestConstroller {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private FichaRegistralService fichaRegistralService;
	
	@Autowired
	PredioRepository predioRepository;
	
	@Autowired
	MunicipioRepository municipioRepository;
	
	@PostMapping("/findFichaRegistral")
	public ResponseEntity<?> findCaratulaoByFolioAndTipoFolio(@RequestBody PeticionFichaVO ficha){
		StringBuilder mensaje = new StringBuilder();
		System.out.println(ficha);
		FichaRegistralVO fichaVO = new FichaRegistralVO();
		List<FichaRegistralVO> listFicha = new ArrayList<FichaRegistralVO>();

		if(ficha.getNoFolio() != null) {
			
			List<Predio> predio = predioRepository.findPredioByFolio(ficha.getNoFolio());
			mensaje.append(fichaRegistralService.validaPredio(predio,false));
			if(mensaje.toString().equals("OK")) {
				for(Predio p:predio) {
					fichaVO =fichaRegistralService.getFichaRegistral(p);
					listFicha.add(fichaVO);
				}
				return new ResponseEntity<>(listFicha, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(mensaje.toString(), HttpStatus.NOT_FOUND);
			}


		} else if(ficha.getClaveCatastral() != null && ficha.getClaveCatastral().trim().length() > 0 ) {
			
			List<Predio> predio = predioRepository.findByClaveCatastral(ficha.getClaveCatastral());
			mensaje.append(fichaRegistralService.validaPredio(predio,true));
			if(mensaje.toString().equals("OK")) {
				for(Predio p:predio) {
					fichaVO =fichaRegistralService.getFichaRegistral(p);
					listFicha.add(fichaVO);
				}
				return new ResponseEntity<>(listFicha, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(mensaje.toString(), HttpStatus.NOT_FOUND);
			}

		} else if(ficha.getCuentaCatastral() != null && ficha.getCuentaCatastral().trim().length() > 0 ) {
			
			//List<Predio> predio = predioRepository.findByCuentaCatastral(ficha.getCuentaCatastral());
			Municipio muni = municipioRepository.findByClaveCatastral(ficha.getMunicipio());
			List<Predio> predio = predioRepository.findByCuentaCatastralAndMunicipioId(ficha.getCuentaCatastral(), muni.getId());
			mensaje.append(fichaRegistralService.validaPredio(predio,true));
			if(mensaje.toString().equals("OK")) {
				for(Predio p:predio) {
					fichaVO =fichaRegistralService.getFichaRegistral(p);
					listFicha.add(fichaVO);
				}
				return new ResponseEntity<>(listFicha, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(mensaje.toString(), HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"No se encontró Ficha Registral\"}", HttpStatus.NOT_FOUND);
		}

	}
}
