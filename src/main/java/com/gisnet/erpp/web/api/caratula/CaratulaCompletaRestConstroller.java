package com.gisnet.erpp.web.api.caratula;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.PredioMigrado;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.BitacoraMantenimientoService;
import com.gisnet.erpp.service.CaratulaService;
import com.gisnet.erpp.service.PersonaJuridicaService;
import com.gisnet.erpp.service.PredioBitacoraService;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.service.excepciones.PasesException;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.FolioRealVO;
import com.gisnet.erpp.vo.caratula.AntecedenteVO;
import com.gisnet.erpp.vo.caratula.DevArt71VO;
import com.gisnet.erpp.vo.caratula.FolioSeccionAuxiliarVO;
import com.gisnet.erpp.vo.caratula.MuebleVO;
import com.gisnet.erpp.vo.caratula.PaseVO;
import com.gisnet.erpp.vo.caratula.PersonaJuridicaVO;
import com.gisnet.erpp.vo.caratula.PredioVO;
import com.gisnet.erpp.vo.caratula.TramiteVO;

@RestController
@RequestMapping(value = "/api/caratula-completa", produces = MediaType.APPLICATION_JSON_VALUE)

public class CaratulaCompletaRestConstroller {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CaratulaService caratulaService;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private PredioBitacoraService predioBitacoraService;
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	private BitacoraMantenimientoService bitacoraMantenimientoService;
	
	@GetMapping("/findCaratulaByFolio/{noFolio}/{noFolioAnterior}/{auxiliar}/TipoFolio/{tipo}/Oficina/{oficinaId}/validate/{validate}")
	public ResponseEntity<?> findCaratulaoByFolioAndTipoFolio(@PathVariable Integer noFolio,@PathVariable Integer noFolioAnterior,@PathVariable Integer auxiliar,@PathVariable Integer tipo,@PathVariable Long oficinaId, @PathVariable boolean validate){
		
		log.debug("Buscando el folio con folio: {}, tipo: {} y oficina {}",noFolio,tipo,oficinaId);
			boolean valido=false;
			Long tipol = Long.valueOf(tipo.longValue());
			for (Constantes.ETipoFolio tp : Constantes.ETipoFolio.values()) {
				if ((int)(long)tp.idTipoFolio == tipol) {
						valido = true;
				}
			}
			System.out.println(!validate && valido);
			if(!validate && valido) {
				System.out.println("validating now");
				switch (tipol.intValue()) {
				case 1:
					return new ResponseEntity<>(caratulaService.getCaratulaPersonaJuridica(noFolio,oficinaId), HttpStatus.OK);
				case 2:
					return new ResponseEntity<>(caratulaService.getCaratulaFolioSeccionAuxiliar(noFolio, oficinaId), HttpStatus.OK);
				case 3:
					return new ResponseEntity<>(caratulaService.getCaratulaMueble(noFolio, oficinaId), HttpStatus.OK);
				case 4:
					return new ResponseEntity<>(caratulaService.getCaratulaPredio(noFolio, oficinaId), HttpStatus.OK);
				}
			}
			
			if(valido){
					System.out.println("valido!");
					Usuario usuarioSistema = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
					if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
						PersonaJuridicaVO personaJuridicaVO = null;
						if(noFolio!=null && noFolio>0) {
							personaJuridicaVO = caratulaService.getCaratulaPersonaJuridica(noFolio,oficinaId);
						}else if(noFolioAnterior!=null  && noFolioAnterior>0) {
							personaJuridicaVO = caratulaService.getCaratulaPersonaJuridicaByFolioAnterior(noFolioAnterior, oficinaId);							
						}
							
						
						if (personaJuridicaVO!=null){
							if( personaJuridicaVO.getBloqueado() ) {
								
								Usuario usuario = caratulaService.getUsuarioBloqueoPersonaJuridica(personaJuridicaVO.getPersonaJuridicaId());
								
								if (usuarioSistema.getTipoUsuario().getId() == 8) {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida, acuda con "+usuario.getNombreCompleto()+" para su revisión. \"}", HttpStatus.NOT_FOUND);
								} else {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida\"}", HttpStatus.NOT_FOUND);
								}

							} /*else if( personaJuridicaVO.isEnProceso() ) {
								return new ResponseEntity<>("{\"mensaje\":\"El folio se encuentra en proceso\"}", HttpStatus.NOT_FOUND);
							}*/
							return new ResponseEntity<>(personaJuridicaVO, HttpStatus.OK);
						} else {
							return new ResponseEntity<>("{\"mensaje\":\"No se encontro el folio de persona juridica\"}", HttpStatus.NOT_FOUND);
						}
					}
					if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
						FolioSeccionAuxiliarVO folioSeccionAuxiliarVO =caratulaService.getCaratulaFolioSeccionAuxiliar(noFolio,oficinaId);
						if (folioSeccionAuxiliarVO!=null){
							if( folioSeccionAuxiliarVO.getBloqueado() ) {
								
								Usuario usuario = caratulaService.getUsuarioBloqueoSeccionAuxiliar(folioSeccionAuxiliarVO.getFolioSeccionAuxiliarId());
								
								if (usuarioSistema.getTipoUsuario().getId() == 8) {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida, acuda con "+usuario.getNombreCompleto()+" para su revisión. \"}", HttpStatus.NOT_FOUND);
								} else {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida\"}", HttpStatus.NOT_FOUND);
								}
								
							} /*else if( folioSeccionAuxiliarVO.isEnProceso() ) {
								return new ResponseEntity<>("{\"mensaje\":\"El folio se encuentra en proceso\"}", HttpStatus.NOT_FOUND);
							}*/
							return new ResponseEntity<>(folioSeccionAuxiliarVO, HttpStatus.OK);
						} else {
							return new ResponseEntity<>("{\"mensaje\":\"No se encontro el folio de la sección auxiliar\"}", HttpStatus.NOT_FOUND);
						}
					}
					if((int)(long)tipol == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
						MuebleVO muebleVO = caratulaService.getCaratulaMueble(noFolio,oficinaId);
						if (muebleVO!=null){
							if( muebleVO.getBloqueado() ) {
								
								Usuario usuario = caratulaService.getUsuarioBloqueoMueble(muebleVO.getMuebleId());
								
								if (usuarioSistema.getTipoUsuario().getId() == 8) {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida, acuda con "+usuario.getNombreCompleto()+" para su revisión. \"}", HttpStatus.NOT_FOUND);
								} else {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida\"}", HttpStatus.NOT_FOUND);
								}
								
								
							} /* else if( muebleVO.isEnProceso() ) {
								return new ResponseEntity<>("{\"mensaje\":\"El folio se encuentra en proceso\"}", HttpStatus.NOT_FOUND);
							} */
							return new ResponseEntity<>(muebleVO, HttpStatus.OK);
						} else {
							return new ResponseEntity<>("{\"mensaje\":\"No se encontro el folio del mueble\"}", HttpStatus.NOT_FOUND);
						}
					}
					if((int)(long)tipol == Constantes.ETipoFolio.PREDIO.idTipoFolio){
						PredioVO predioVO = null;
						if(noFolio!=0){
							predioVO = caratulaService.getCaratulaPredio(noFolio,oficinaId);
						}else if (auxiliar!=0){
							predioVO = caratulaService.getCaratulaPredioFolioAnteriorAuxiliar(noFolioAnterior,auxiliar,oficinaId);
						}else {
							predioVO = caratulaService.getCaratulaPredioFolioAnterior(noFolioAnterior,oficinaId);
						}

						if (predioVO!=null) {
							if( predioVO.getBloqueado() ) {
								
								Usuario usuario = caratulaService.getUsuarioBloqueo(predioVO.getPredioId());
								
								if (usuarioSistema.getTipoUsuario().getId() == 8) {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida, acuda con "+usuario.getNombreCompleto()+" para su revisión. \"}", HttpStatus.NOT_FOUND);
								} else {
									return new ResponseEntity<>("{\"mensaje\":\"por el momento no es posible mostrar la información requerida\"}", HttpStatus.NOT_FOUND);
								}
								
								
							} /* else if( predioVO.isEnProceso() ) {
								return new ResponseEntity<>("{\"mensaje\":\"El folio se encuentra en proceso\"}", HttpStatus.OK);
							}*/

							if( predioVO.getstatusActo().getId()==4 ) 
							{
								return new ResponseEntity<>(predioVO, HttpStatus.OK);
							}
							return new ResponseEntity<>(predioVO, HttpStatus.OK);
						} else {
							return new ResponseEntity<>("{\"mensaje\":\"No se encontro el folio del predio\"}", HttpStatus.NOT_FOUND);
						}
					}
		}else{
			return new ResponseEntity<>("El tipo de folio es incorrecto.",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("El tipo de folio es incorrecto.",HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findTramitesInscritos/{predioId}/{statusActoId}/{clasifActoId}/{tipoFolioId}")
	public ResponseEntity<?> findTramitesInscritos(@PathVariable Integer predioId, @PathVariable Long statusActoId, @PathVariable Long clasifActoId, @PathVariable Integer tipoFolioId) {
		List<TramiteVO> tramites = caratulaService.findTramitesInscritos(predioId,statusActoId,clasifActoId,tipoFolioId);
		if( tramites != null ) {
			return new ResponseEntity<>(tramites, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"No se encontraron los tramites\"}", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/findAllTramites/{predioId}/{tipoFolioId}")
	public ResponseEntity<?> findAllTramites(@PathVariable Long predioId,  @PathVariable Integer tipoFolioId) {
		List<TramiteVO> tramites = caratulaService.findAllTramites(predioId,tipoFolioId);
		if( tramites != null ) {
			return new ResponseEntity<>(tramites, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"No se encontraron los tramites\"}", HttpStatus.NOT_FOUND);
		}
	}




	@GetMapping("/savePase/{predioId}/{oficinaId}/{folioReal}")
	public ResponseEntity<?> savePase(@PathVariable Long predioId, @PathVariable Long oficinaId, @PathVariable Integer folioReal) {
		PaseVO paseVO = null;
		try {
			paseVO = caratulaService.savePase(predioId,oficinaId,folioReal,null);
			if( paseVO != null ) {
				return new ResponseEntity<>(paseVO, HttpStatus.OK);
			}
		} catch (PasesException e) {
			return new ResponseEntity<>("{\"mensaje\":\""+e.getMessage()+"\"}", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>("{\"mensaje\":\"Error al guardar el pase.\"}", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/saveDevolucion/{id}/{consecutivo}/{causaRechazo}/{solicitud}/{tipoFolio}")
	public ResponseEntity<?> saveDevolucion(@PathVariable Long id,@PathVariable Integer consecutivo,@PathVariable String causaRechazo,@PathVariable String solicitud,@PathVariable Long tipoFolio) {
		DevArt71VO devolucionVO = null;
		
		Long predioId = null;
		Long personaJuridicaId = null;
		Long folioSeccionAuxiliarId = null;
		if( tipoFolio == 1 ) {
			personaJuridicaId = id;
		} else if( tipoFolio == 2 ) {
			folioSeccionAuxiliarId = id;
		} else if( tipoFolio == 4 ) {
			predioId = id;
		}
		
		devolucionVO = caratulaService.saveDevolucion(predioId,personaJuridicaId,folioSeccionAuxiliarId,consecutivo,causaRechazo,solicitud);
		if( devolucionVO != null ) {
			return new ResponseEntity<>(devolucionVO, HttpStatus.OK);
		}

		return new ResponseEntity<>("{\"mensaje\":\"Error al guardar la devolucion.\"}", HttpStatus.NOT_FOUND);
	}

	@GetMapping("/getPredioMigrado/{predioId}")
	public ResponseEntity<?> getPredioMigrado(@PathVariable Long predioId) {
		PredioMigrado predioMigrado = caratulaService.getPredioMigrado(predioId);
		if( predioMigrado != null ) {
			return new ResponseEntity<>(predioMigrado, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"PredioMigrado no encontrado.\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getPredioMigrado/personaJuridica/{personaJuridicaId}")
	public ResponseEntity<?> getPredioMigradoPersonaJuridica(@PathVariable Long personaJuridicaId) {
		PredioMigrado predioMigrado = caratulaService.getPredioMigradoPersonaJuridica(personaJuridicaId);
		if( predioMigrado != null ) {
			return new ResponseEntity<>(predioMigrado, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"PredioMigrado no encontrado.\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getPredioMigrado/auxiliar/{auxiliarId}")
	public ResponseEntity<?> getPredioMigradoAuxiliar(@PathVariable Long auxiliarId) {
		PredioMigrado predioMigrado = caratulaService.getPredioMigradoAuxiliar(auxiliarId);
		if( predioMigrado != null ) {
			return new ResponseEntity<>(predioMigrado, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"PredioMigrado no encontrado.\"}", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getSocios/personaJuridica/{personaJuridicaId}")
	public ResponseEntity<?> getSocios(@PathVariable Long personaJuridicaId) {
		List<PjPersona> sociosList = personaJuridicaService.findSociosOrganosApoderadosActuales(personaJuridicaId,Constantes.TipoRelPersona.SOCIO);
		if( sociosList != null ) {
			return new ResponseEntity<>(sociosList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"Socios no encontrados.\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getApoderados/personaJuridica/{personaJuridicaId}")
	public ResponseEntity<?> getApoderados(@PathVariable Long personaJuridicaId) {
		List<PjPersona> sociosList = personaJuridicaService.findSociosOrganosApoderadosActuales(personaJuridicaId,Constantes.TipoRelPersona.APODERADO);
		if( sociosList != null ) {
			return new ResponseEntity<>(sociosList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"Socios no encontrados.\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getOrganosAdministracion/personaJuridica/{personaJuridicaId}")
	public ResponseEntity<?> getOrganosAdministracion(@PathVariable Long personaJuridicaId) {
		List<PjPersona> sociosList = personaJuridicaService.findSociosOrganosApoderadosActuales(personaJuridicaId,Constantes.TipoRelPersona.ORGANO_ADMINISTRACION);
		if( sociosList != null ) {
			return new ResponseEntity<>(sociosList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"Socios no encontrados.\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/findAllTramitesInscritos/{predioId}/{tipoFolioId}")
	public ResponseEntity<?> findAllTramitesInscritos(@PathVariable Integer predioId, @PathVariable Integer tipoFolioId) {
		List<TramiteVO> tramites = caratulaService.findAllTramitesInscritos(predioId, tipoFolioId);
		if( tramites != null ) {
			return new ResponseEntity<>(tramites, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"No se encontraron los tramites\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/findAllTramitesTitulares/{predioId}/{tipoFolioId}")
	public ResponseEntity<?> findAllTramitesTitulares(@PathVariable Long predioId, @PathVariable Integer tipoFolioId) {
		List<TramiteVO> tramites = caratulaService.findAllTramitesTitulares(predioId);
		if( tramites != null ) {
			return new ResponseEntity<>(tramites, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"No se encontraron los tramites\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/bitacoraColindacia/{predioBitacoraId}")
	public ResponseEntity<?> findBitacoraColindancias(@PathVariable Long predioBitacoraId){
		return new ResponseEntity<>( predioBitacoraService.findBitacoraColindancia(predioBitacoraId), HttpStatus.OK);
	}
	
	@GetMapping("/findBitacoraMantenimiento/{id}/{tipoFolioId}")
	public ResponseEntity<?> findBitacoraMantenimiento(@PathVariable("id") Long id,@PathVariable("tipoFolioId") Long tipoFolioId){
		return new ResponseEntity<>(bitacoraMantenimientoService.findByTipoFolio(id, tipoFolioId),HttpStatus.OK);
	}
	
	
	@GetMapping(value="/findByFolio/{noFolio}/tipoFolio/{tipoFolio}")
	public ResponseEntity<FolioRealVO> findFolioReal(@PathVariable("noFolio") Integer noFolio,@PathVariable("tipoFolio") Integer tipoFolio ){
	  	try {
			return new ResponseEntity<>(caratulaService.findFolioByNoFolio(noFolio, tipoFolio, true),HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/findAllAntecedentes/{predioId}/{tipoFolioId}")
	public ResponseEntity<?> findAntecedentes(@PathVariable("predioId") Long predioId, @PathVariable("tipoFolioId") Integer tipoFolioId ){
		List<AntecedenteVO> antecedentes = caratulaService.findAllAntecedentes(predioId, tipoFolioId);
		if( antecedentes != null ) {
			return new ResponseEntity<>(antecedentes, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"No se encontraron los antecedentes\"}", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/marcarFolioCalidad")
	public ResponseEntity<?> marcarFolioCalidad(@RequestParam("predioId") Long predioId){		
		Boolean resp = false;
		
		resp = caratulaService.marcarFolioCalidad(predioId);
		
		if(resp) {
			return new ResponseEntity<>("{\"mensaje\":\"El folio se marco como Folio de Calidad\"}", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("{\"mensaje\":\"Error: No se marco como Folio de Calidad\"}", HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping("/quitarFolioCalidad")
	public ResponseEntity<?> quitarFolioCalidad(@RequestParam("predioId") Long predioId){
		Boolean resp = false;
		
		resp = caratulaService.quitarFolioCalidad(predioId);
		
		if(resp) {
			return new ResponseEntity<>("{\"mensaje\":\"El folio se quito como Folio de Calidad\"}", HttpStatus.OK);
		}else {
			return new ResponseEntity<>("{\"mensaje\":\"Error: No se puede quitar como Folio de Calidad\"}", HttpStatus.NOT_FOUND);
		}
		
	}

}
