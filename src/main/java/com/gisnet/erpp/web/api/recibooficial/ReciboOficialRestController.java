package com.gisnet.erpp.web.api.recibooficial;

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

import java.math.BigDecimal;

import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.service.ReciboService;
import com.gisnet.erpp.service.ReciboWebService;

@RestController
@RequestMapping(value = "/api/recibo-oficial", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReciboOficialRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ReciboWebService reciboWebService;
	
	@Autowired
	ReciboService reciboService;

	@GetMapping("/{estadoCuenta}")
	public ResponseEntity<ReciboOficialDTO> consulta(@PathVariable("estadoCuenta") String estadoCuenta) {
		log.debug( "===> consulta - cuenta = "+estadoCuenta );
		
		ReciboOficialDTO result = reciboWebService.consulta(estadoCuenta);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	//Función comentada para validar la referencia
	/*@GetMapping("/valida/{estadoCuenta}")
	public ResponseEntity<Boolean> valida(@PathVariable("estadoCuenta") String estadoCuenta) {
		log.debug( "===> valida - cuenta = "+estadoCuenta );
		
		boolean result = reciboService.valida(estadoCuenta);
		return new ResponseEntity<>(result, HttpStatus.OK);
	} */
	@GetMapping("/valida/{referencia}")
	public ResponseEntity<String> valida(@PathVariable("referencia") String referencia) {
		log.debug( "===> valida - referencia = "+referencia );
		String result = reciboService.valida_referencia(referencia);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/updateRecibo")
	public ResponseEntity<Recibo> updateRecibo(@RequestBody Recibo recibo){
		return new ResponseEntity<>(reciboService.UpdateRecibo(recibo), HttpStatus.OK);
	}
}
