package com.gisnet.erpp.web.api.firma;

import java.net.URISyntaxException;
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

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.service.FirmaService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.vo.PrelacionIngresoVO;

@RestController
@RequestMapping(value = "/api/firma", produces = MediaType.APPLICATION_JSON_VALUE)
public class FirmaRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	FirmaService firmaService;

	@PostMapping("/getEstampa")
	public ResponseEntity<FirmaDTO> registrarFirmaEnServicioEstatal(@RequestBody FirmaRequestDTO firmaRequestDTO) throws URISyntaxException {
		log.debug("REST request getEstampa para {} y  {} ", firmaRequestDTO.getOriginal(), firmaRequestDTO.getPkcs7());
		String estampa = firmaService.registrarFirmaEnServicioEstatal(firmaRequestDTO);
		
		log.debug("REST request getEstampa result: {}", estampa);
		return new ResponseEntity<>(firmaService.parseEstampa(estampa), HttpStatus.OK);

	}
	
}
