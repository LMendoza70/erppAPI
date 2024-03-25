package com.gisnet.erpp.web.api.antecedente;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.PredioAnteRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.PersonaJuridicaService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.util.Constantes;

import com.querydsl.core.NonUniqueResultException;

import org.springframework.dao.DataIntegrityViolationException;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.service.ActoPredioService;
import com.gisnet.erpp.service.AntecedenteService;
import com.gisnet.erpp.service.BitacoraMantenimientoService;
import com.gisnet.erpp.service.CaratulaService;
import com.gisnet.erpp.service.ImagenService;
import com.gisnet.erpp.service.LibroService;
import com.gisnet.erpp.service.MuebleService;
import com.gisnet.erpp.service.PersonaAuxiliarService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.MessageObject;

@RestController
@RequestMapping(value = "/api/antecedente", produces = MediaType.APPLICATION_JSON_VALUE)
public class AntecedenteRestController {

	private static final Logger log = LoggerFactory.getLogger(AntecedenteRestController.class);

	@Autowired
	private AntecedenteService antecedenteService;

	@Autowired
	private LibroService libroService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PersonaJuridicaService personaJuridicaService;

	@Autowired
	private PersonaAuxiliarService personaAuxiliarService;

	@Autowired
	private MuebleService muebleService;

	@Autowired
	private PredioService predioService;
	
	@Autowired
	private ImagenService imagenService;
	
	@Autowired
	private CaratulaService caratulaService;
	
	@Autowired
	private BitacoraMantenimientoService bitacoraMantenimientoService;
	
	@Autowired
	private PredioAnteRepository predioAnteRepository;

	@PostMapping("/antecedente-vo")
	public ResponseEntity<PrelacionAnte> saveAntecedenteByVO (@RequestBody AntecedenteVO antevo){		
		return new ResponseEntity<>(antecedenteService.createPrelacionAntecedente(antevo), HttpStatus.OK);
	}

	@GetMapping("/findAntecedentesDePrelacion/{idPrelacion}")
	public ResponseEntity<List<AntecedenteVO>> findAntecedentesDePrelacion(@PathVariable Long idPrelacion){
		log.debug("Buscando antecedente para la prelacion con id : {}",idPrelacion);
		return new ResponseEntity<>(antecedenteService.findAntecedentesDePrelacion(idPrelacion), HttpStatus.OK);
	}
	
	@GetMapping("/encontrar")
	public ResponseEntity<AntecedenteVO> busquedaAntecedente(@RequestParam("page") Integer page, @RequestParam("size") Integer size,  @RequestParam("libro")  Integer libro, @RequestParam("libroBis")  String librobis,@RequestParam("seccion")   String seccion, @RequestParam("oficina")  String oficina, @RequestParam("anio")  Integer anio, @RequestParam("volumen")  String volumen,  @RequestParam("documento") String doc, @RequestParam("documentoBis") String docbis) {
		log.debug("Libro=" + libro);
		log.debug("LibroBis=" + librobis);
		log.debug("Documento=" + doc);
		log.debug("DocumentoBis=" + docbis);
		log.debug("seccion=" + seccion);
		log.debug("oficina=" + oficina);
		log.debug("anio=" + anio);
		log.debug("volumne=" + volumen);
				Antecedente ante= antecedenteService.findByDatosAntecedente(doc, docbis, libro, librobis, seccion, oficina);
		return new ResponseEntity<>(AntecedenteVO.antecedenteTransform(ante), HttpStatus.OK);
	}

	@GetMapping("/encontrar-libro")
	public ResponseEntity<AntecedenteVO> busquedaAntecedenteLibro(@RequestParam("page") Integer page, @RequestParam("size") Integer size,
			@RequestParam("tipoBusqueda") String tipoBusqueda,     @RequestParam("clasificacion") String clasificacion,

			@RequestParam("libro")     Integer libro,   @RequestParam("libroBis")     String librobis,
			@RequestParam("seccion")   String seccion, @RequestParam("oficina")      String oficina,
			@RequestParam("documento") String doc,     @RequestParam("documentoBis") String docbis

			) {
		log.debug("Libro=" + libro);
		log.debug("LibroBis=" + librobis);
		log.debug("Documento=" + doc);
		log.debug("DocumentoBis=" + docbis);
		log.debug("seccion=" + seccion);
		log.debug("oficina=" + oficina);

		/*Libro libro = libroService.findLibroFromVentanilla (libro, libroBis, doc, docbis, seccion, oficina);
				Antecedente ante= antecedenteService.findLibroFromVentanilla(doc, docbis, libro, librobis, seccion, oficina);
				*/
		//return new ResponseEntity<>(AntecedenteVO.antecedenteTransform(ante), HttpStatus.OK);
		return null;
	}


    /*
	@PostMapping("/busqueda")
	public ResponseEntity<Antecedente>  getAntencedente(@RequestBody Antecedente antecedente) {
				Antecedente ante=null;
				if(antecedente!=null){
					Libro lib = libroService.findByNumeroLibro(antecedente.getLibro().getNumLibro());
					log.debug(antecedente.toString());
					log.debug(lib.toString());
					ante= antecedenteService.findByComponentes(antecedente.getLibro(), antecedente.getLibroBis(), antecedente.getSeccion(), antecedente.getOficina(),antecedente.getDocumento(), antecedente.getDocumentoBis());
				}

		return new ResponseEntity<>(ante, HttpStatus.OK);
	}*/

	@GetMapping ("/folioreal/{tipo}/{numFolio}/{oficinaId}" )
	public ResponseEntity<?> findByFolioReal (@PathVariable Long tipo, @PathVariable Integer numFolio, @PathVariable Long oficinaId) {
		Usuario userActive = null;
		try {
			userActive = usuarioService.getLoguedUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if ((int)(long)tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio() ) {
			Long idOficina = 11L;
			
			if(userActive.getOficina() != null) {
				idOficina =userActive.getOficina().getId();
			}
				
			PersonaJuridica pj= personaJuridicaService.findOneByNumFolio(numFolio, idOficina);
			System.out.println("encontro? "+pj);
			PjAnte pa = null;
			if ( pj != null )
				pa = personaJuridicaService.findPjAnteByPersonaJuridica(pj);
			if (pa != null ) {
				return new ResponseEntity<>(pa, HttpStatus.OK);
			}else{
				if(pj!=null){
					return new ResponseEntity<>(pj, HttpStatus.OK);
				}
			}

		}


		if ( (int)(long)tipo == Constantes.ETipoFolio.PERSONAS_AUXILIARES.getTipoFolio() ) {

				FolioSeccionAuxiliar fsa= personaAuxiliarService.findOneByNoFolio(numFolio);

				AuxiliarAnte aa = null;
				if (fsa != null )
					aa = personaAuxiliarService.findAuxiliarAnteByPersonaAuxiliar(fsa);

				if (aa != null ) {
					return new ResponseEntity<>(aa, HttpStatus.OK);
				}

		}

		if ( (int)(long)tipo == Constantes.ETipoFolio.MUEBLE.getTipoFolio() ) {
				Mueble mueble = muebleService.findMuebleByNoFolio (numFolio);

				MuebleAnte ma = null; 
				if (mueble != null ) {
					try {
						log.info("Mueble ID: " + mueble.getId());
						Long muebleId = mueble.getId();
						ma = muebleService.findMuebleAnteByMuebleId (muebleId);
					}

					catch (Exception except) {
						log.error("Excepción en la búsqueda del mueble");
					}
				}
				if (ma != null ) {
					return new ResponseEntity<>(ma, HttpStatus.OK);
				}

		}

		if ( (int)(long)tipo == Constantes.ETipoFolio.PREDIO.getTipoFolio() ) {
			Predio predio= predioService.findPredioByNoFolio(numFolio);

			PredioAnte prea = null;
			if (predio != null) {
				try {
					prea = predioService.findPredioAnteByPredioId(predio.getId()).get();
				} catch (Exception e) {
					prea = null;
				}
				
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (prea != null ) {
				return new ResponseEntity<>(prea, HttpStatus.OK);
			} else {
				Antecedente antecedente = new Antecedente();
				PredioAnte predioAnte = new PredioAnte();
				predioAnte.setAntecedente( antecedente );
				predioAnte.setPredio( predio );
				return new ResponseEntity<>(predioAnte, HttpStatus.OK);
			}

		}


		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);


	}

	@GetMapping ("/foliorealante/{tipo}/{numFolio}/{aux}/{oficinaId}" )
	public ResponseEntity<?> findByFolioRealAnterior (@PathVariable Long tipo, @PathVariable Integer numFolio, @PathVariable Integer aux, @PathVariable Long oficinaId) {
		Usuario userActive = null;
		try {
			userActive = usuarioService.getLoguedUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info(  "Tipo: "+ tipo.toString() + " Folio: " + numFolio.toString());
		//System.out.println(userActive.getOficina().getId());
		/*
		if ((int)(long)tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio() ) {
			PersonaJuridica pj= personaJuridicaService.findOneByNumFolio(numFolio, userActive.getOficina().getId());
			System.out.println("encontro? "+pj);
			PjAnte pa = null;
			if ( pj != null )
				pa = personaJuridicaService.findPjAnteByPersonaJuridica(pj);
			if (pa != null ) {
				return new ResponseEntity<>(pa, HttpStatus.OK);
			}else{
				if(pj!=null){
					return new ResponseEntity<>(pj, HttpStatus.OK);
				}
			}

		} */

		/*
		if ( (int)(long)tipo == Constantes.ETipoFolio.PERSONAS_AUXILIARES.getTipoFolio() ) {

				FolioSeccionAuxiliar fsa= personaAuxiliarService.findOneByNoFolio(numFolio);

				AuxiliarAnte aa = null;
				if (fsa != null )
					aa = personaAuxiliarService.findAuxiliarAnteByPersonaAuxiliar(fsa);

				if (aa != null ) {
					return new ResponseEntity<>(aa, HttpStatus.OK);
				}

		}*/
		/*
		if ( (int)(long)tipo == Constantes.ETipoFolio.MUEBLE.getTipoFolio() ) {
				Mueble mueble = muebleService.findMuebleByNoFolio (numFolio);

				MuebleAnte ma = null; 
				if (mueble != null ) {
					try {
						log.info("Mueble ID: " + mueble.getId());
						Long muebleId = mueble.getId();
						ma = muebleService.findMuebleAnteByMuebleId (muebleId);
					}

					catch (Exception except) {
						log.error("Excepción en la búsqueda del mueble");
					}
				}
				if (ma != null ) {
					return new ResponseEntity<>(ma, HttpStatus.OK);
				}

		}*/

		if ( (int)(long)tipo == Constantes.ETipoFolio.PREDIO.getTipoFolio() ) {
			Predio predio = null;
			if(aux == 0){
				predio= predioService.findPredioByNoFolioAnterior(numFolio,oficinaId);
			} else {
				predio= predioService.findPredioByNoFolioAnteriorAndAuxiliar(numFolio,aux,oficinaId);
			}
			PredioAnte prea = null;
			if (predio != null) {
				try {
					prea = predioService.findPredioAnteByPredioId(predio.getId()).get();
				} catch (Exception e) {
					prea = null;
				}
				
			} else {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (prea != null ) {
				return new ResponseEntity<>(prea, HttpStatus.OK);
			} else {
				Antecedente antecedente = new Antecedente();
				PredioAnte predioAnte = new PredioAnte();
				predioAnte.setAntecedente( antecedente );
				predioAnte.setPredio( predio );
				return new ResponseEntity<>(predioAnte, HttpStatus.OK);
			}

		}


		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);


	}


	@GetMapping ("/foliopersonas/{tipo}/{numFolio}/{oficinaId}" )
	public ResponseEntity<?> findByFolioAndOficina (@PathVariable Long tipo, @PathVariable Integer numFolio, @PathVariable Long oficinaId) {

		log.info(  "Tipo: "+ tipo.toString() + " Folio: " + numFolio.toString());

		if ((int)(long)tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio() ) {
			PersonaJuridica pj= personaJuridicaService.findOneByNumFolio(numFolio, oficinaId);

			if ( pj != null ) {
				return new ResponseEntity<>(pj, HttpStatus.OK);
			}
		}


		if ( (int)(long)tipo == Constantes.ETipoFolio.PERSONAS_AUXILIARES.getTipoFolio() ) {

			FolioSeccionAuxiliar fsa= personaAuxiliarService.findOneByNoFolio(numFolio);

			if (fsa != null ) {
				return new ResponseEntity<>(fsa, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);


	}


	@PostMapping("/busqueda-vo")
	public ResponseEntity<? >  getAntencedente(@RequestBody AntecedenteVO antecedente) {


		/*if (!antecedenteService.validoVO(antecedente))
			return new ResponseEntity<>("Argumentos incorrectos en la llamada.", HttpStatus.BAD_REQUEST);
		*/

		Object ante = null;

		try {
			ante = libroService.findOneLibroFromVentanilla(antecedente);
			log.info("ante: {}",ante);
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>(except.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<> (ante, HttpStatus.OK);

	}

	@GetMapping("/findLibrosByNumLibro/{numLibro}")
	public ResponseEntity<List<Libro>> findLibrosByNumLibro(@PathVariable Long numLibro){
		List<Libro> libs = libroService.findAllLikeNumLibro(numLibro.toString());
		
		if (libs != null)
			return new ResponseEntity<>( libs, HttpStatus.OK);
		
		return new ResponseEntity<>((List<Libro>) null, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByPredio/{predioId}")
	public ResponseEntity<Antecedente> findAntecedenteByPredioId(@PathVariable Long predioId){
		Antecedente ante = antecedenteService.findAntecedenteByPredioId(predioId);
		
		if (ante != null)
			return new ResponseEntity<>( ante, HttpStatus.OK);
		else {
			Predio predio = predioService.findOne(predioId);
			if( predio != null ) {
				ante = new Antecedente();
				return new ResponseEntity<>( ante, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>((Antecedente) null, HttpStatus.NOT_FOUND);

	}

	@GetMapping("/findByFolio/{folioId}/tipoFolio/{tipoFolio}")
	public ResponseEntity<?> findAntecedenteByFolioId(@PathVariable Long folioId,@PathVariable Long tipoFolio){
		Antecedente ante=null;

		if((int)(long)tipoFolio == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
			ante = antecedenteService.findAntecedenteByPersonaJuridicaId(folioId);
		}
		if((int)(long)tipoFolio == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
			ante = antecedenteService.findAntecedenteByFolioSeccionAuxiliarId(folioId);
		}
		if((int)(long)tipoFolio == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
			ante = antecedenteService.findAntecedenteByMuebleId(folioId);
		}
		if((int)(long)tipoFolio == Constantes.ETipoFolio.PREDIO.idTipoFolio){
			ante = antecedenteService.findAntecedenteByPredioId(folioId);
		}

		if (ante == null) {
			return new ResponseEntity<>((Antecedente) null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>( ante, HttpStatus.OK);
	}

	@GetMapping("/findByLibroDocumento")
	public ResponseEntity<List<AntecedenteVO>> findByLibroIdAndDocumento(@RequestParam ("idLibro") Long idLibro,@RequestParam ("documento") String documento){
		List<AntecedenteVO> antecedenteVOs=null;
		List<Antecedente> antecedentes=antecedenteService.findAntecedenteByLibroIdAndDocumento(idLibro, documento);
		if(antecedentes!=null && !antecedentes.isEmpty()){	
			antecedenteVOs= new ArrayList<>();
			for(Antecedente ante:antecedentes){
				AntecedenteVO avo=AntecedenteVO.antecedenteTransform(ante);
				antecedenteVOs.add(avo);
			}
		}
		System.out.println("/findByLibroDocumento{}"+antecedentes);
		return new ResponseEntity<>(antecedenteVOs,HttpStatus.OK);
	}

	@PostMapping("findLibroByAmplia")
	public ResponseEntity<List<Antecedente>> getImagePathsFromLibroAmplia(@RequestBody BitacoraGestor bitacoraGestor){
		return new ResponseEntity<>(imagenService.findLibroByAmplia(bitacoraGestor),HttpStatus.OK);
	}

	@GetMapping("/findUsuarioBloqueoFolio/{folioId}/tipoFolio/{tipoFolio}")
	public ResponseEntity<?> findUsuarioBloqueoFolio(@PathVariable Long folioId,@PathVariable Long tipoFolio){
		Antecedente ante=null;
		Usuario usuario = null;

		if((int)(long)tipoFolio == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
			usuario = caratulaService.getUsuarioBloqueoPersonaJuridica(folioId);
		}
		if((int)(long)tipoFolio == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
			usuario = caratulaService.getUsuarioBloqueoSeccionAuxiliar(folioId);
		}
		if((int)(long)tipoFolio == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
			usuario = caratulaService.getUsuarioBloqueoMueble(folioId);
		}
		if((int)(long)tipoFolio == Constantes.ETipoFolio.PREDIO.idTipoFolio){
			usuario = caratulaService.getUsuarioBloqueo(folioId);
		}

		if (usuario == null) {
			return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>( usuario.getNombreCompleto(), HttpStatus.OK);
	}
	
	@GetMapping("/delete-antecedente/{id}/{folioId}/{tipoFolioId}")
	public ResponseEntity<?> deleteAntecedentes(@PathVariable Long id, @PathVariable Long folioId, @PathVariable Integer tipoFolioId) throws URISyntaxException {
		
		try {
			
			PredioAnte pa = predioAnteRepository.findByPredioIdAndAntecedenteId(folioId, id);
			if(pa != null) {
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
				String comentario =  "ELIMINO EL ANTECEDENTE ";
				comentario += antecedenteService.createComentarioAntecedente(pa.getAntecedente());
				bitacoraMantenimientoService.create( usuario,comentario,"DELETE_ANTECEDENTE", pa.getPredio() );
				predioAnteRepository.delete(pa);
			}
			
			return new ResponseEntity<>(pa, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
}
