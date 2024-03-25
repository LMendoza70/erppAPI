package com.gisnet.erpp.web.api.solicitudDevolucion;

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

import com.gisnet.erpp.domain.SolicitudDevolucion;
import com.gisnet.erpp.service.SolicitudDevolucionService;

@RestController
@RequestMapping(value = "/api/solicitud-devolucion", produces = MediaType.APPLICATION_JSON_VALUE)
public class SolicitudDevolucionController {
  @Autowired
  SolicitudDevolucionService solicitudDevolucionService;
  
  @PostMapping(value="/")
  public ResponseEntity<?> create(@RequestBody SolicitudDevolucion solicitudDevolucion){
	  try 
	  {
		  return new ResponseEntity<>(solicitudDevolucionService.create(solicitudDevolucion),HttpStatus.OK);
		  
	  }catch(Exception e) {
		  return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	  }
  }
  
  @GetMapping(value="/find-by-prelacion/{prelacionId}")
  public ResponseEntity<SolicitudDevolucion> findByPrelacion(@PathVariable("prelacionId") Long prelacionId){
	try {
		 return new ResponseEntity<>(solicitudDevolucionService.findByPrelacion(prelacionId),HttpStatus.OK);
	}catch(Exception e) {
		return new ResponseEntity<>(null,HttpStatus.CONFLICT);
	}
  }
  
  @PostMapping(value="/autoriza-devolucion")
  public ResponseEntity<?> autorizaDevolucion(@RequestBody SolicitudDevolucion solicitud){
		try {
			 return new ResponseEntity<>(solicitudDevolucionService.autorizaDevolucion(solicitud),HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error interno",HttpStatus.CONFLICT);
		}
  }
  
}
