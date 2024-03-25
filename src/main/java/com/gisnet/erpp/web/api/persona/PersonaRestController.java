package com.gisnet.erpp.web.api.persona;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.net.URISyntaxException;

import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.service.ActoPredioCampoService;
import com.gisnet.erpp.service.ActoPredioService;
import com.gisnet.erpp.service.BitacoraMantenimientoService;
import com.gisnet.erpp.service.ModuloCampoService;
import com.gisnet.erpp.service.NacionalidadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.BitacoraMantenimiento;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.ModuloCampo;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.BitacoraMantenimientoRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.vo.PersonaVO;
import com.gisnet.erpp.service.PersonaService;
import com.gisnet.erpp.service.PredioPersonaService;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.vo.PredioPersonaVO;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.MessageObject;

@RestController
@RequestMapping(value = "/api/persona", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonaRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PersonaService personaService;

	@Autowired
	NacionalidadService nacionalidadService;
	
	@Autowired
	ModuloCampoService moduloCampoService;

	@Autowired
	ActoPredioCampoService actoPredioCampoService;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;
	
	@Autowired
	ActoPredioService actoPredioService;
	
	@Autowired
	BitacoraMantenimientoRepository bitacoraMantenimientoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	PredioPersonaService predioPersonaService;
	
	@Autowired
	TipoRelPersonaRepository tipoRelPersonaRepository;
	
	@Autowired
	DireccionRepository direccionRepository;
	
	@Autowired
    PredioPersonaRepository predioPersonaRepository;
	
	@Autowired
	ActoPredioRepository actoPredioRepository;
	
	@Autowired
	BitacoraMantenimientoService bitacoraMantenimientoService; 

	@GetMapping("/")
	public ResponseEntity<Page<Persona>> getAllPropietarios(Pageable pageable, String paterno, String materno, String nombre) {
		log.debug("paterno=" + paterno);
		log.debug("materno=" + materno);
		log.debug("nombre=" + nombre);
		Page<Persona> page = personaService.findAllByNombre(paterno, materno, nombre, pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<?> createPersona(@RequestBody PersonaVO persona) throws URISyntaxException {
		log.debug("REST request crear persona TipoPersona : {}",persona.getTipoPersona());
		log.debug("REST request crear persona : {}", persona);
		log.debug("REST request crear direcciones : {}", persona.getDireccionesParaPersonas().size());
		log.debug("REST request crear identificaciones : {}", persona.getIdentificacionesParaPersonas().size());
		
		
		 if(personaService.findByEmail(persona.getEmail())!=null) {
			 
				return new ResponseEntity<>(new MessageObject("El Email no esta disponible"), HttpStatus.BAD_REQUEST); 
			 }
		
		Persona newPersona = persona.transformIntoPersona();
		Long nuevaPersonaId = personaService.save(newPersona);
		return new ResponseEntity<>(nuevaPersonaId, HttpStatus.OK);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updatePersona(@RequestBody PersonaVO persona) throws URISyntaxException {
		log.debug("REST request crear persona TipoPersona : {}",persona.getTipoPersona());
		log.debug("REST request crear persona : {}", persona);
		log.debug("REST request crear direcciones : {}", persona.getDireccionesParaPersonas().size());
		log.debug("REST request crear identificaciones : {}", persona.getIdentificacionesParaPersonas().size());
		
		Persona newPersona = persona.transformIntoPersona();
		Long nuevaPersonaId = personaService.update(newPersona);
		return new ResponseEntity<>(nuevaPersonaId, HttpStatus.OK);
	}

	@GetMapping("/personas")
	public ResponseEntity<Page<Persona>> getAllPersonas(Pageable pageable,  String paterno, String materno, String nombre, Long tipoPersonaId, Long isPublica) {
		log.debug("paterno=" + paterno);
		log.debug("materno=" + materno);
		log.debug("nombre=" + nombre);
		Page<Persona> page = personaService.getAllPersonas(pageable, paterno, materno, nombre, tipoPersonaId, isPublica);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PersonaDTO> findOneById(@PathVariable ("id") Long id) {
		Persona persona = personaService.findOneById(id);
		return new ResponseEntity<>(new PersonaDTO(persona), HttpStatus.OK);
	}

	@GetMapping("/{id}/direcciones")
	public ResponseEntity<Set<Direccion>> findDireccionesByPersonaId(@PathVariable ("id") Long id) {
		Persona persona = personaService.findOneById(id);
		return new ResponseEntity<>(persona.getDireccionesParaPersonas(), HttpStatus.OK);
	}

	@GetMapping("/similares")
	public ResponseEntity<Set<Persona>> findAllSimilar(String nombre, String paterno, String materno) {
		Set<Persona> similares = personaService.findAllSimilar(nombre, paterno, materno);
		return new ResponseEntity<>(similares, HttpStatus.OK);
	}

	@GetMapping("/{id}/telefonos")
	public ResponseEntity<Set<Telefono>> findTelefonosByPersonaId(@PathVariable ("id") Long id) {
		Persona persona = personaService.findOneById(id);
		return new ResponseEntity<>(persona.getTelefonosParaPersonas(), HttpStatus.OK);
	}


	@PutMapping("/{id}/nacionalidad")
	public ResponseEntity<Long> actualizarPersonaNacionalidad(@PathVariable("id") long id, @RequestBody Nacionalidad nacionalidad) throws URISyntaxException {
		Nacionalidad nac = nacionalidadService.findOne(nacionalidad.getId());
		Persona persona = personaService.findOneById(id);
		persona.setNacionalidad(nac);
		Long nuevaPersonaId = personaService.save(persona);
		return new ResponseEntity<>(nuevaPersonaId, HttpStatus.OK);
	}
	
	@PostMapping("/add-predio-persona")
	public ResponseEntity<?> addPredioPersona(@RequestBody PredioPersonaVO predioPersonaVo) throws URISyntaxException {
		
		try {
			
			Optional<ActoPredio> oActoPredio = actoPredioRepository.findFirstByActoIdAndTipoEntradaIdOrderByVersionDesc(predioPersonaVo.getActoPredio().getActo().getId(),Constantes.TipoEntrada.SALIDA.getIdTipoEntrada());
			ActoPredio actoPredio =  oActoPredio.get();

			predioPersonaVo.setActoPredio(actoPredio);
			
			PredioPersona predioPersona = predioPersonaVo.transformIntoPredioPersona();
			Persona newPersona = predioPersonaVo.getPersona().transformIntoPersona();
			Long nuevaPersonaId = personaService.save(newPersona);
			predioPersona.setPersona(personaService.findOneById(nuevaPersonaId));
			predioPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(1L));
//			Direccion direccion = new Direccion();
//			direccion.setActivo(true);
//			direccion.setPersona(personaService.findOneById(nuevaPersonaId));
//			predioPersona.setDireccion(direccionRepository.saveAndFlush(direccion));
			
			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			String comentario = "AGREGO EL TITULAR " + bitacoraMantenimientoService.getTitularString(predioPersona);
			bitacoraMantenimientoService.create(usuario, predioPersona,comentario,"ADD_TITULAR");
			
			return new ResponseEntity<>(predioPersonaService.savePredioPersona(predioPersona), HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/update-predio-persona")
	public ResponseEntity<?> updatePredioPersona(@RequestBody PredioPersonaVO predioPersonaVo) throws URISyntaxException {
		
		try {
			PredioPersona antPredioPersona = predioPersonaRepository.findOne(predioPersonaVo.getId());
			updatePersona(predioPersonaVo.getPersona());
			PredioPersona predioPersona = predioPersonaVo.transformIntoPredioPersona();
			predioPersona.setDireccion(null);
			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());		
			String comentario = "ACTUALIZO EL TITULAR "+ bitacoraMantenimientoService.getTitularString(antPredioPersona)  +" A "+   bitacoraMantenimientoService.getTitularString(predioPersona);
			bitacoraMantenimientoService.create(usuario, predioPersona,comentario, "UPDATE_TITULAR");
			return new ResponseEntity<>(predioPersonaService.savePredioPersona(predioPersona), HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/update-predio-persona-titular-inicial")
	public ResponseEntity<?> updatePredioPersonaTitularInicial(@RequestBody PredioPersonaVO predioPersonaVo) throws URISyntaxException {

		try {

			
			updatePersona(predioPersonaVo.getPersona());

			PredioPersona predioPersona = predioPersonaVo.transformIntoPredioPersona();
			PredioPersona antePredioPerona =  predioPersonaRepository.findOne(predioPersona.getId());
			ActoPredio actoPredio = actoPredioService.findMaxVersion(predioPersonaVo.getActoPredio().getActo().getId());
			predioPersona.setDireccion(null);
			List<Acto> actosPrimerReg = new ArrayList<Acto>();

			actosPrimerReg = actoPredio.getPredio().getActoPrediosParaPredios()
			.stream()
			.filter(x -> x.getActo() != null  && x.getActo().getVf() != null && x.getActo().getVf() && x.getActo().getTipoActo() != null && x.getActo().getTipoActo().getClasifActo() != null && x.getActo().getTipoActo().getClasifActo().getId() == 1L)
			.map(act -> act.getActo())
			.sorted((o1, o2)->o1.getId().
                    compareTo(o2.getId())).
                    collect(Collectors.toList());


			ActoPredio actoPredioTranslativoInicial = new ActoPredio();

			for (Acto acto : actosPrimerReg) {

				for (ActoPredio acp : acto.getActoPrediosParaActos()) {

					actoPredioTranslativoInicial = acp ;
					break;
				}

			}


			Map<Long, Set<ActoPredioCampo>> listSetTitularesApc = actoPredioCampoService.findActosModulosCamposByActoIdModuloId(actoPredioTranslativoInicial.getId() , 10L);

		    for (Set<ActoPredioCampo> actoPredioCampo : listSetTitularesApc.values()) {

		    	Integer orden = 0;

		    	Set<ActoPredioCampo> actoPredioCampoTitular = actoPredioCampo.stream()
														      .filter(x -> x.getValor().equals(predioPersona.getPersona().getId().toString())  && x.getModuloCampo().getId() == 135L)
														      .collect(Collectors.toSet());

		    	//OBTIENE ORDEN
				for (ActoPredioCampo actoPredioCampo2 : actoPredioCampoTitular) {

					orden = actoPredioCampo2.getOrden();

				}

		    	//MODIFICA Dominio Directo
				Set<ActoPredioCampo> actoPredioCampoPorcentajesDD = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 55L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo actoPredioCampoDD : actoPredioCampoPorcentajesDD) {

					if(actoPredioCampoDD.getOrden() == orden) {
						actoPredioCampoDD.setValor(predioPersona.getPorcentajeDd().toString());
						actoPredioCampoService.ModificaActoPredioCampo(actoPredioCampoDD);
					}

				}

		    	//MODIFICA Usufructu
				Set<ActoPredioCampo> actoPredioCampoPorcentajesUV = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 56L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo actoPredioCampoUV : actoPredioCampoPorcentajesUV) {

					if(actoPredioCampoUV.getOrden() == orden) {
						actoPredioCampoUV.setValor(predioPersona.getPorcentajeUv().toString());
						actoPredioCampoService.ModificaActoPredioCampo(actoPredioCampoUV);
					}

				}
		    }
			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());			
			String comentario = "ACTUALIZO EL TITULAR INICIAL "+ bitacoraMantenimientoService.getTitularString(antePredioPerona)  +" A "+   bitacoraMantenimientoService.getTitularString(predioPersona);
			bitacoraMantenimientoService.create(usuario, predioPersona, comentario,"UPDATE_TITULAR_INICIAL");

			return new ResponseEntity<>(predioPersonaService.savePredioPersona(predioPersona), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/add-predio-persona-titular-inicial")
	public ResponseEntity<?> addPredioPersonaTitularInicial(@RequestBody PredioPersonaVO predioPersonaVo) throws URISyntaxException {

		try {

			ActoPredio actoPredio = actoPredioService.findMaxVersion(predioPersonaVo.getActoPredio().getActo().getId());

			List<Acto> actosPrimerReg = new ArrayList<Acto>();

			actosPrimerReg = actoPredio.getPredio().getActoPrediosParaPredios()
			.stream()
			.filter(x -> (x.getActo().getVf()!=null && x.getActo().getVf())  && x.getActo().getTipoActo() != null && x.getActo().getTipoActo().getClasifActo().getId() == 1L)
			.map(act -> act.getActo())
			.sorted((o1, o2)->o1.getId().
                    compareTo(o2.getId())).
                    collect(Collectors.toList());


			ActoPredio actoPredioTranslativoInicial = new ActoPredio();

			for (Acto acto : actosPrimerReg) {

				for (ActoPredio acp : acto.getActoPrediosParaActos()) {

					actoPredioTranslativoInicial = acp ;
					break;
				}

			}

			predioPersonaVo.setActoPredio(actoPredio);

			PredioPersona predioPersona = predioPersonaVo.transformIntoPredioPersona();
			Persona newPersona = predioPersonaVo.getPersona().transformIntoPersona();
			Long nuevaPersonaId = personaService.save(newPersona);
			predioPersona.setPersona(personaService.findOneById(nuevaPersonaId));
			predioPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(1L));

			predioPersona = predioPersonaService.savePredioPersona(predioPersona);

		    List<ActoPredioCampo> listAct = new ArrayList<ActoPredioCampo>() ;
		    ActoPredioCampo apc = new ActoPredioCampo();
		    ModuloCampo mc = moduloCampoService.findOne(135L);

		    Map<Long, Set<ActoPredioCampo>> listSetTitularesApc = actoPredioCampoService.findActosModulosCamposByActoIdModuloId(actoPredioTranslativoInicial.getId() , mc.getModulo().getId());

		    List<Integer> listaOrden = new ArrayList<Integer>();

		    for (Set<ActoPredioCampo> actoPredioCampo : listSetTitularesApc.values()) {

				for (ActoPredioCampo item : actoPredioCampo) {
					listaOrden.add(item.getOrden());
				}
			}

		    int orden = listaOrden.stream().mapToInt(i -> i).max().getAsInt();
		    orden++;

			apc.setActoPredio(actoPredioTranslativoInicial);
			apc.setModuloCampo(mc);
			apc.setOrden(orden);
			apc.setValor(predioPersona.getPersona().getId().toString());
			actoPredioCampoRepository.save(apc);

			apc = new ActoPredioCampo();
			mc = moduloCampoService.findOne(55L);
			apc.setActoPredio(actoPredioTranslativoInicial);
			apc.setModuloCampo(mc);
			apc.setOrden(orden);
			apc.setValor(predioPersona.getPorcentajeDd().toString());
			actoPredioCampoRepository.save(apc);

			apc = new ActoPredioCampo();
			mc = moduloCampoService.findOne(56L);
			apc.setActoPredio(actoPredioTranslativoInicial);
			apc.setModuloCampo(mc);
			apc.setOrden(orden);
			apc.setValor(predioPersona.getPorcentajeUv().toString());
			actoPredioCampoRepository.save(apc);


			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			String comentario = "AGREGO EL TITULAR INICIAL " +bitacoraMantenimientoService.getTitularString(predioPersona);
			bitacoraMantenimientoService.create(usuario, predioPersona, comentario,"ADD_TITULAR_INICIAL");
			
			return new ResponseEntity<>(predioPersona, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/delete-predio-persona")
	public ResponseEntity<?> deletePredioPersona(@RequestBody PredioPersonaVO predioPersonaVo) throws URISyntaxException {
		
		try {
			PredioPersona predioPersona = predioPersonaVo.transformIntoPredioPersona();
			predioPersona.setDireccion(null);
			
			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			String comentario =  "ELIMINO EL TITULAR "+bitacoraMantenimientoService.getTitularString(predioPersona);
			bitacoraMantenimientoService.create(usuario, predioPersona,comentario,"DELETE_TITULAR");
			predioPersonaService.deleteById(predioPersona.getId());
			return new ResponseEntity<>(predioPersona, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/delete-predio-persona-titular-inicial")
	public ResponseEntity<?> deletePredioPersonaTitularInicial(@RequestBody PredioPersonaVO predioPersonaVo) throws URISyntaxException {

		try {

			//ActoPredio actoPredio = actoPredioService.findMaxVersion(predioPersonaVo.getActoPredio().getActo().getId());

			/*List<Acto> actosPrimerReg = new ArrayList<Acto>();

			actosPrimerReg = actoPredio.getPredio().getActoPrediosParaPredios()
			.stream()
			.filter(x -> ( x.getActo().getVf()!=null && x.getActo().getVf())  && x.getActo().getTipoActo() != null && x.getActo().getTipoActo().getClasifActo().getId() == 1L)
			.map(act -> act.getActo())
			.sorted((o1, o2)->o1.getId().
                    compareTo(o2.getId())).
                    collect(Collectors.toList());


			ActoPredio actoPredioTranslativoInicial = new ActoPredio();

			for (Acto acto : actosPrimerReg) {

				for (ActoPredio acp : acto.getActoPrediosParaActos()) {

					actoPredioTranslativoInicial = acp ;
					break;
				}

			}*/

			PredioPersona predioPersona = predioPersonaVo.transformIntoPredioPersona();

			/*Map<Long, Set<ActoPredioCampo>> listSetTitularesApc = actoPredioCampoService.findActosModulosCamposByActoIdModuloId(actoPredioTranslativoInicial.getId() , 10L);

		    for (Set<ActoPredioCampo> actoPredioCampo : listSetTitularesApc.values()) {

		    	Integer orden = 0;

		    	Set<ActoPredioCampo> actoPredioCampoTitular = actoPredioCampo.stream()
														      .filter(x -> x.getValor().equals(predioPersona.getPersona().getId().toString())  && x.getModuloCampo().getId() == 135L)
														      .collect(Collectors.toSet());

		    	//REMUEVE TITULAR
				for (ActoPredioCampo actoPredioCampo2 : actoPredioCampoTitular) {

					orden = actoPredioCampo2.getOrden();

					actoPredioCampoService.DeleteActoPredioCampo(actoPredioCampo2);


				}

		    	//REMUEVE Dominio Directo
				Set<ActoPredioCampo> actoPredioCampoPorcentajesDD = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 55L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo actoPredioCampoDD : actoPredioCampoPorcentajesDD) {

					if(actoPredioCampoDD.getOrden() == orden) {
						actoPredioCampoService.DeleteActoPredioCampo(actoPredioCampoDD);
					}

				}

		    	//REMUEVE Usufructu
				Set<ActoPredioCampo> actoPredioCampoPorcentajesUV = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 56L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo actoPredioCampoUV : actoPredioCampoPorcentajesUV) {

					if(actoPredioCampoUV.getOrden() == orden) {
						actoPredioCampoService.DeleteActoPredioCampo(actoPredioCampoUV);
					}

				}

		    	//REMUEVE Activacion Transmitente
				Set<ActoPredioCampo> actoPredioCampoACT = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 57L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo obj : actoPredioCampoACT) {

					if(obj.getOrden() == orden) {
						actoPredioCampoService.DeleteActoPredioCampo(obj);
					}

				}

		    	//REMUEVE Actua Por
				Set<ActoPredioCampo> actoPredioCampoACP = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 60L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo obj : actoPredioCampoACP) {

					if(obj.getOrden() == orden) {
						actoPredioCampoService.DeleteActoPredioCampo(obj);
					}

				}

		    	//REMUEVE DD a transmitir
				Set<ActoPredioCampo> actoPredioCampoDDT = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 58L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo obj : actoPredioCampoDDT) {

					if(obj.getOrden() == orden) {
						actoPredioCampoService.DeleteActoPredioCampo(obj);
					}

				}

		    	//REMUEVE UV a transmitir
				Set<ActoPredioCampo> actoPredioCampoUVT = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 59L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo obj : actoPredioCampoUVT) {

					if(obj.getOrden() == orden) {
						actoPredioCampoService.DeleteActoPredioCampo(obj);
					}

				}

		    	//REMUEVE Datos Generales
				Set<ActoPredioCampo> actoPredioCampoDGA = actoPredioCampo.stream()
					      .filter(x ->  x.getModuloCampo().getId() == 308L )
					      .collect(Collectors.toSet());

				for (ActoPredioCampo obj : actoPredioCampoDGA) {

					if(obj.getOrden() == orden) {
						actoPredioCampoService.DeleteActoPredioCampo(obj);
					}

				}



			}*/


			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			String comentario =  "ELIMINO EL TITULAR INICIAL "+bitacoraMantenimientoService.getTitularString(predioPersona);
			bitacoraMantenimientoService.create(usuario, predioPersona,comentario,"DELETE_TITULAR_INICIAL");
			
			predioPersonaService.deleteById(predioPersona.getId());

			return new ResponseEntity<>(predioPersona, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

}
