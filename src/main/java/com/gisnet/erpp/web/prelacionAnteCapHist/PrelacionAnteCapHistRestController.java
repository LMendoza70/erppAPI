package com.gisnet.erpp.web.prelacionAnteCapHist;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.service.PrelacionAnteCapHistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.domain.SeccionPorOficina;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.util.MessageObject;
import com.gisnet.erpp.web.api.prelacionAnte.PrelacionAnteDTO;

@RestController
@RequestMapping(value = "api/prelacion-ante-cap-hist", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrelacionAnteCapHistRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PrelacionAnteCapHistService prelacionAnteCapHistService;
	
	@PostMapping("/set-capturado")
	public ResponseEntity<Long> setCapturado(@RequestBody PrelacionAnteCapHist prelacionAnteCapHist){
		return new ResponseEntity<>(prelacionAnteCapHistService.setCapturado(prelacionAnteCapHist), HttpStatus.CREATED);
	}

	@GetMapping("/find-capturables-by")
	public ResponseEntity<Page<PrelacionAnteCapHist>> findAllCapturablesBy(String numLibro, String libroBis, Long seccionPorOficinaId, Integer anio, String volumen, String carga, Pageable pageable) {
		return new ResponseEntity<>(prelacionAnteCapHistService.findAllCapturableBy(numLibro, libroBis, seccionPorOficinaId, anio, volumen, carga, pageable, "CAPTURISTAS"), HttpStatus.OK);
	}
	
	@GetMapping("/find-validables-by")
	public ResponseEntity<Page<PrelacionAnteCapHist>> findAllValidablesBy(String numLibro, String libroBis, Long seccionPorOficinaId, Integer anio, String volumen, String carga, Pageable pageable) {
		return new ResponseEntity<>(prelacionAnteCapHistService.findAllCapturableBy(numLibro, libroBis, seccionPorOficinaId, anio, volumen, carga, pageable, "VALIDADORES"), HttpStatus.OK);
	}

	@GetMapping("/{id}")	
	public ResponseEntity<?> findOne(@PathVariable("id") Long id) {
		log.debug(id.toString());
		PrelacionAnteCapHist prelacionAnteCapHist = prelacionAnteCapHistService.findOne(id);
		return new ResponseEntity<>(prelacionAnteCapHist, HttpStatus.OK);
	}
	
	@GetMapping("/find-carga")
	public ResponseEntity<?> findCarga(String numLibro,Long seccionPorOficinaId,Integer anio,String volumen,String cargaTrabajo)
	{
		try 
		{
			PrelacionAnteCapHist pre = prelacionAnteCapHistService.findFirstCarga(numLibro, seccionPorOficinaId, anio, volumen, cargaTrabajo);
			if(pre!= null)
			    return new ResponseEntity<>(pre, HttpStatus.OK);
			else
				return new ResponseEntity<>(new 
						MessageObject("No identificamos carga de trabajo con estás caracteristicas."), 
						HttpStatus.BAD_REQUEST);

		} catch (Exception e ) 
		{
			return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/generar-carga")	
	public ResponseEntity<?> generarCarga(@RequestBody List<PrelacionAnteCapHist> cargas ) {
		try {
			return new ResponseEntity<>(prelacionAnteCapHistService.generarCargas(cargas), HttpStatus.OK);	
		} catch (Exception e ) {
			return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping("/actualiza-validador")
	public ResponseEntity<?> actualizaValidador(@RequestBody PrelacionAnteCapHist carga)
	{
		try
		{
			  this.prelacionAnteCapHistService.actualizaValidador(carga);
			  return new ResponseEntity<>(new MessageObject("La carga se actualizo correctamente"), HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/valida-capturados")
	public ResponseEntity<?> validaCapturados(@RequestBody List<PrelacionAnteCapHist> cargas)
	{
		try
		{
			  this.prelacionAnteCapHistService.validaCapturadas(cargas);
			  return new ResponseEntity<>(new MessageObject("Las inscripciones se validaron correctamente!."), HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST);
		}	
	}
	
	@PostMapping(value="/agrega-predio")
	public ResponseEntity<?> agregaPredio(@RequestBody PrelacionAnteCapHist carga){
		
		 try{
			   
			   return new ResponseEntity<>(prelacionAnteCapHistService.cloneInscripcion(carga),HttpStatus.OK);  
		   }catch(Exception e)
		   {
			   return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST);
		   }		
	}
	
	
	@PostMapping("")
	public ResponseEntity<?> save(@RequestParam("prelacionAnteCapHist") String prelacionAnteCapHistStr) {
		log.debug(prelacionAnteCapHistStr);
		PrelacionAnteCapHist prelacionAnteCapHist = new PrelacionAnteCapHist();
		try {
			prelacionAnteCapHist = new ObjectMapper().readValue(prelacionAnteCapHistStr, PrelacionAnteCapHist.class);
		}
		catch (Exception e) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(new MessageObject("Ocurrio un error al recibir el objeto"), HttpStatus.EXPECTATION_FAILED);
		}
		log.debug(prelacionAnteCapHist.toString());
		try {
			return new ResponseEntity<>(prelacionAnteCapHistService.save(prelacionAnteCapHist), HttpStatus.OK);		
		}catch (Exception e) {
			return new ResponseEntity<>(new MessageObject("Ocurrio un error al guardar los cambios"), HttpStatus.EXPECTATION_FAILED);	
		}
	}
	
	@PostMapping("/masivo")
	public ResponseEntity<?> createWorkLoad (
		@RequestParam("numLibro") String numLibro,
		@RequestParam("libroBis") String libroBis,
		@RequestParam("seccionPorOficina") String seccionOficinaString, 
		@RequestParam("anio") Integer anio,
		@RequestParam("usuario") String usuarioString,
		@RequestParam("usuarioValidador") String usuarioValidadorString,
		@RequestParam("dInsFin") Integer dInsFin,
		@RequestParam("dInsInicio") Integer dInsInicio,
		@RequestParam("cargaTrabajo") String cargaTrabajo,
		@RequestParam("documentoBis") String documentoBis,
		@RequestParam("volumen") String volumen,
		@RequestParam("matriz") Boolean  matriz
		
		){
		SeccionPorOficina seccionOficina = new SeccionPorOficina();
		Usuario usuario = new Usuario();
		Usuario usuarioValidador = new Usuario();
		try {
			usuarioValidador =  new ObjectMapper().readValue(usuarioValidadorString, Usuario.class);
			usuario = new ObjectMapper().readValue(usuarioString, Usuario.class);
			seccionOficina = new ObjectMapper().readValue(seccionOficinaString, SeccionPorOficina.class);
				
		}
		catch (Exception e) {
			log.debug(e.getMessage());
		}
		try {
			return new ResponseEntity<>(prelacionAnteCapHistService.createWorkLoad(numLibro, libroBis, seccionOficina, anio, usuario, usuarioValidador,dInsFin, dInsInicio, cargaTrabajo, documentoBis, volumen,matriz), HttpStatus.OK);	
		}
		catch(DataIntegrityViolationException ex ) {
			log.debug(ex.getMessage());
			return new ResponseEntity<> (new MessageObject("Ya existe una carga masiva creada con estos datos"), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.debug(e.getMessage());
			return new ResponseEntity<> (new MessageObject(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/masivo-prelacion")
	public ResponseEntity<?> createWorkLoad (
		@RequestParam("folio") Integer folio,
		@RequestParam("usuario") String usuarioString,
		@RequestParam("usuarioValidador") String usuarioValidadorString,
		@RequestParam("oficina") String oficinaString,
		@RequestParam("tipoActo") String tipoActoString
		){
		Oficina oficina = new Oficina();
		Usuario usuario = new Usuario();
		Usuario usuarioValidador = new Usuario();
		TipoActo tipoActo = new TipoActo();
		try {
			usuario = new ObjectMapper().readValue(usuarioString, Usuario.class);
			usuarioValidador = new ObjectMapper().readValue(usuarioValidadorString, Usuario.class);
			oficina = new ObjectMapper().readValue(oficinaString, Oficina.class);
			tipoActo  = new ObjectMapper().readValue(tipoActoString, TipoActo.class);
			
			return new ResponseEntity<> (prelacionAnteCapHistService.createPrelacion(folio, oficina, tipoActo,usuario,usuarioValidador), HttpStatus.OK);
			
		}
		catch (Exception e) {
		   log.debug(e.getMessage());
		   return new ResponseEntity<> (new MessageObject(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
