package com.gisnet.erpp.web.api.predio;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gisnet.erpp.service.*;
import com.gisnet.erpp.vo.*;
import com.gisnet.erpp.vo.caratula.PropietarioVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.PredioAnte;
import com.gisnet.erpp.domain.PredioBitacora;
import com.gisnet.erpp.domain.PredioBitacoraColindancia;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.BloqueoFolio;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.repository.BloqueoFolioRepository;
@RestController
@RequestMapping(value = "/api/predio", produces = MediaType.APPLICATION_JSON_VALUE)
public class PredioRestController {
	private static final Logger log = LoggerFactory.getLogger(PredioRestController.class);

	@Autowired
	private PredioService predioService;

	@Autowired
	private PersonaJuridicaService personaJuridicaService;

	@Autowired
	private FolioSeccionAuxiliarService folioSeccionAuxiliarService;

	@Autowired
	private MuebleService muebleService;

	@Autowired
	private BloqueoFolioService bloqueoFolioService;

	@Autowired
	ActoPredioService actoPredioService;

	@Autowired
	private PrelacionPredioService prelacionPredioService;

	@Autowired
	private BloqueoFolioRepository bloqueoFolioRepository;
	
	@Autowired
	private PredioPersonaService predioPersonaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PaseFracCondService paseFracCondService;
	
	
	@PostMapping("/save")
	public ResponseEntity<Predio> savePredio(@RequestBody PredioDTO predioDTO){
		log.debug("precede de foli", predioDTO.getProcedeDeFolio());
		System.out.println("idLibro"+ predioDTO.getLibro());
		System.out.println("idDocumento"+ predioDTO.getDocumento());

		return new ResponseEntity<>(predioService.savePredio(predioDTO), HttpStatus.CREATED);
	}
	
	@PostMapping("/save-titulares-iniciales/{predioId}")
	public ResponseEntity<Predio> saveTitularesIniciales(@PathVariable Long predioId, @RequestBody List<PropietarioVO> titulares){
		log.debug("titulares={}", titulares);

		return new ResponseEntity<>(predioService.saveTitularesIniciales(predioId, titulares), HttpStatus.CREATED);
	}


	@PostMapping("/create")
	public ResponseEntity<Long> createPredio(@RequestBody PredioVO predio){
		return new ResponseEntity<>(predioService.createPredio(predio), HttpStatus.OK);
	}

	@GetMapping("/findPredioByAntecedente/{idAntecedente}")
	public ResponseEntity<PredioVO> findPredioByAntecedente(@PathVariable Long idAntecedente){
		log.debug("Buscando el predio con id de antecedente: {}",idAntecedente);
		return new ResponseEntity<>(predioService.findPredioByAntecedente(idAntecedente), HttpStatus.OK);
	}

	@GetMapping("/findPredioByFolio/{idFolio}")
	public ResponseEntity<PredioAnteVO> findPredioByFolio(@PathVariable Integer idFolio){
		log.debug("Buscando el predio con folio: {}",idFolio);
		return new ResponseEntity<>(predioService.findPredioByFolio(idFolio), HttpStatus.OK);
	}
	
	@GetMapping("/findPredioByNoFolio/{idFolio}")
	public ResponseEntity<?> findPredioByNoFolio(@PathVariable Integer idFolio){
		try {
			return new ResponseEntity<>(predioService.findPredioByNoFolioOficina(idFolio), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	

	@GetMapping("/findFolioByFolio/{idFolio}/TipoFolio/{tipo}/Oficina/{oficinaId}")
	public ResponseEntity<?> findFolioByFolioAndTipoFolio(@PathVariable Integer idFolio,@PathVariable Integer tipo, @PathVariable Long oficinaId){
		log.debug("Buscando el folio con folio: {} y tipo: {}",idFolio,tipo);
			Usuario userActive = null;
			try {
				userActive = usuarioService.getLoguedUser();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			oficinaId = userActive.getOficina().getId();
			boolean valido=false;
			Long tipol = Long.valueOf(tipo.longValue());
			for (Constantes.ETipoFolio tp : Constantes.ETipoFolio.values()) {
				if ((int)(long)tp.idTipoFolio == tipol) {
						valido = true;
				}
			}
			if(valido == true){
					if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
						return new ResponseEntity<>(personaJuridicaService.findPersonaJuridicaByFolio(idFolio, oficinaId), HttpStatus.OK);
					}
					if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
						return new ResponseEntity<>(folioSeccionAuxiliarService.findFolioSeccionAuxiliarByFolio(idFolio, oficinaId), HttpStatus.OK);
					}
					if((int)(long)tipol == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
						return new ResponseEntity<>(muebleService.findMuebleByFolio(idFolio), HttpStatus.OK);
					}
					if((int)(long)tipol == Constantes.ETipoFolio.PREDIO.idTipoFolio){
						return new ResponseEntity<>(predioService.findPredioByFolio(idFolio), HttpStatus.OK);
					}
		}else{
			return new ResponseEntity<>("El tipo de folio es incorrecto.",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("El tipo de folio es incorrecto.",HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Predio> findOne(@PathVariable("id") Long id) {
		return new ResponseEntity<>(predioService.findOne(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/predios")
	public ResponseEntity<Page<Predio>> getAllPrediosPageable(Pageable pageable, Integer noFolio) {
		return new ResponseEntity<>(predioService.findAllPageable(noFolio, pageable), HttpStatus.OK);
	}

	/// Predio-Fraccionamiento
	@GetMapping(value = "/prediosFraccionamiento")
	public ResponseEntity<Page<Predio>> getAllPrediosFraccionamientoPageable(Pageable pageable, Long idActo, Integer noFolio, String manzana, String lote) {
		return new ResponseEntity<>(predioService.findAllPrediosFraccionamientoPageable(idActo, noFolio, manzana, lote, pageable), HttpStatus.OK);
	}

	@GetMapping(value = "/totalSuperficie/{id}")
	public ResponseEntity<Double> getTotalSuperficie(@PathVariable("id") Long id) {
		return new ResponseEntity<> (predioService.findTotalSuperficie(id), HttpStatus.OK);		
	}

	@GetMapping(value = "/totalIndivisos/{id}")
	public ResponseEntity<Double> getTotalIndivisos(@PathVariable("id") Long id) {
		return new ResponseEntity<> (predioService.findTotalIndivisos(id), HttpStatus.OK);		
	}

	@PostMapping("/create-predio-frac-col/{actoId}")
	public ResponseEntity<Predio> createPredioFracCol(@RequestBody PredioDTO predio, @PathVariable("actoId") Long actoId){
		return new ResponseEntity<>(predioService.savePredioFracCond(predio, actoId), HttpStatus.CREATED);
	}

	@GetMapping("/delete-predio-frac-col/{actoId}")
	public ResponseEntity<Long> deletePredioFracCol(@PathVariable("actoId") Long actoId){
		return new ResponseEntity<>(predioService.deletePrediosFracCond(actoId), HttpStatus.CREATED);
	}

	@GetMapping("/change-procedente-predio-frac-col/{folioFracCondId}")
	public ResponseEntity<Long> changeProcedentePredioFracCol(@PathVariable("folioFracCondId") Long folioFracCondId){
		return new ResponseEntity<>(predioService.changeProcedentePrediosFracCond(folioFracCondId), HttpStatus.CREATED);
	}
	
	@GetMapping("/asociar-pase/{numPase}/acto/{actoId}")
	public ResponseEntity<Boolean> asociarPase(@PathVariable("numPase") Integer numPase,@PathVariable("actoId") Long actoId)
	{
		try {
			if(paseFracCondService.asociarPase(numPase, actoId))
				return new ResponseEntity<Boolean>(true,HttpStatus.OK);
			else
				return new ResponseEntity<Boolean>(false,HttpStatus.CONFLICT);
		}catch(Exception e) 
		{
			return new ResponseEntity<Boolean>(false,HttpStatus.CONFLICT);
		}
	}
	/// FIN Predio-Fraccionamiento

	@GetMapping(value = "/{id}/titulares")
	public ResponseEntity<List<PredioPersona>> findPersonasbyPredioId(@PathVariable("id")Long id) {
		log.debug("Buscando titulares para id=" + id);		
		return new ResponseEntity<>(predioPersonaService.findPropietariosActuales(id,true), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/titulares-iniciales")
	public ResponseEntity<List<PropietarioVO>> findTitularesIniciales(@PathVariable("id")Long id) {
		log.debug("Buscando titulares iniciales para id=" + id);		
	  return new ResponseEntity<>(predioPersonaService.findPropietariosIniciales(id), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/actos/{tiposActo}")
	public ResponseEntity<Set<Acto>> findActosbyPredioId(@PathVariable("id")Long id, @PathVariable("tiposActo") Long[] tiposActo) {
		System.out.println(tiposActo);
		Set<Acto> list = actoPredioService.findActosbyPredioId(id, tiposActo);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	
	@PostMapping("/finalizaPredioCyvf")
	public ResponseEntity<Long> finalizaPredioCyvf(@RequestBody PredioVO predioVO){
		return new ResponseEntity<>(predioService.finalizaPredioCyvf(predioVO), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/{prelacion}/actos")
	public ResponseEntity<List<ActoPredio>>  listActoPredioByPredioAndPrelacion(@PathVariable("id")Long idPredio, @PathVariable("prelacion")Long idPrelacion){


		return new ResponseEntity<>(actoPredioService.getActosPrediosByPredioIdAndPrelacionId(idPredio ,idPrelacion), HttpStatus.OK);

	}

	@GetMapping(value = "/{prelacion}/{folio}/{tipo}/actos")
	public ResponseEntity<List<ActoPredio>>  listActoPredioByPredioAndPrelacion(@PathVariable("prelacion")Long prelacion, @PathVariable("folio")Long folio,@PathVariable("tipo")Long tipo){
		boolean valido=false;
		for (Constantes.ETipoFolio tp : Constantes.ETipoFolio.values()) {
			if ((int)(long)tp.idTipoFolio == tipo) {
					valido = true;
			}
		}
		if(valido == true){
			if((int)(long)tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
				return new ResponseEntity<>(actoPredioService.getActosPrediosByPersonaJuridicaIdAndPrelacionId(folio ,prelacion), HttpStatus.OK);
			}
			if((int)(long)tipo == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
				return new ResponseEntity<>(actoPredioService.getActosPrediosByFolioSeccionAuxiliarIdAndPrelacionId(folio ,prelacion), HttpStatus.OK);
			}
			if((int)(long)tipo == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
				return new ResponseEntity<>(actoPredioService.getActosPrediosByMuebleIdAndPrelacionId(folio ,prelacion), HttpStatus.OK);
			}
			if((int)(long)tipo == Constantes.ETipoFolio.PREDIO.idTipoFolio){
				return new ResponseEntity<>(actoPredioService.getActosPrediosByPredioIdAndPrelacionId(folio ,prelacion), HttpStatus.OK);
			}
		}
			return new ResponseEntity<>((List<ActoPredio>) null,HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/prelacion/{id}/concepto/{concepto}/actos")
	public ResponseEntity<List<ActoPredio>>  listActoPredioByPrelacionAndConcepto(@PathVariable("id")Long idPrelacion, @PathVariable("concepto")Long concepto){


		return new ResponseEntity<>(actoPredioService.getActosPrediosByPrelacionAndConcepto(idPrelacion , concepto), HttpStatus.OK);

	}



	@GetMapping("/findPredioByByNoFolio/{noFolio}")
	public ResponseEntity<PredioVO> findPredioByByNoFolio(@PathVariable Integer noFolio){
		log.debug("Buscando el predio con no folio: {}",noFolio);
		return new ResponseEntity<>(predioService.findPredioByByNoFolio(noFolio), HttpStatus.OK);
	}
	
	@GetMapping("/findPredioByPrelacion/{idPrelacion}")
	public ResponseEntity<PredioVO> findPredioByPrelacion(@PathVariable Long idPrelacion){
		log.debug("Buscando el predio con id de prelacion : {}",idPrelacion);
		return new ResponseEntity<>(predioService.findPredioByPrelacion(idPrelacion), HttpStatus.OK);
	}

	@GetMapping("/buscarPorFolio/{folio}/{tipoFolio}")
	public ResponseEntity<List<BloqueoFolioVO>> buscarPorFolio(@PathVariable("folio") Long folio,@PathVariable("tipoFolio") Long tipoFolio){
		
		List<BloqueoFolioVO> bloqueoFolio=null; 
		
		bloqueoFolio= bloqueoFolioService.findAllByPredio(folio,tipoFolio);

		return new ResponseEntity<>(bloqueoFolio, HttpStatus.OK);	
		
	}


	@GetMapping("/buscarAntePorFolio/{folio}")
	public ResponseEntity<PredioAnte> buscarAntePorFolio(@PathVariable Integer folio){
		Predio predio= predioService.findPredioByNoFolio(folio);
		PredioAnte prea = null;
		if (predio != null){
				prea = predioService.findPredioAnteByPredioId(predio.getId()).get() ;

			if (prea == null ) {
				return new ResponseEntity<>((PredioAnte) null, HttpStatus.NOT_FOUND);
			}
		}else{
			return new ResponseEntity<>((PredioAnte) null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(prea, HttpStatus.OK);
	}


	@PostMapping("/bloquear")
	public ResponseEntity<BloqueoFolioVO> bloquear(@RequestBody BloqueoFolioVO bloqueoFolio){
		Predio predio= predioService.findOne(bloqueoFolio.getPredio().getId());
		BloqueoFolio bloqueoFolioOrig=null;
		if(predio!=null){
			predio.setBloqueado(!bloqueoFolio.getPredio().getBloqueado());
			predioService.saveAndUpdate(predio);
			if(bloqueoFolio.getId()!=null){
				bloqueoFolioOrig=bloqueoFolioService.findOne(bloqueoFolio.getId());
				if(bloqueoFolioOrig!=null){

					Integer version=bloqueoFolioRepository.maxByPredio(bloqueoFolio.getPredio().getId());

					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
					bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
					bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
					bloqueoFolioService.saveAndUpdate(bloqueoFolioOrig);
				}
			}else{
				bloqueoFolioOrig=new BloqueoFolio();
				Integer version=bloqueoFolioRepository.maxByPredio(bloqueoFolio.getPredio().getId());

					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
				bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
				bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
				bloqueoFolioOrig.setPredio(predio);
				bloqueoFolioService.saveAndUpdate(bloqueoFolioOrig);
				bloqueoFolio.setId(bloqueoFolioOrig.getId());
				bloqueoFolio.setUsuario(bloqueoFolioOrig.getUsuario());
			}
		}else{
			return new ResponseEntity<>(bloqueoFolio, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(bloqueoFolio, HttpStatus.OK);
	}



	@PostMapping("/actualzar/bloqueo")
	public ResponseEntity<BloqueoFolioVO> acutalizarBloquear(@RequestBody BloqueoFolioVO bloqueoFolio){
		Predio predio= predioService.findOne(bloqueoFolio.getPredio().getId());
		BloqueoFolio bloqueoFolioOrig=null;
		if(predio!=null){
			if(bloqueoFolio.getId()!=null){
				bloqueoFolioOrig=bloqueoFolioService.findOne(bloqueoFolio.getId());
				if(bloqueoFolioOrig!=null){

					Integer version=bloqueoFolioRepository.maxByPredio(bloqueoFolio.getPredio().getId());

					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
					bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
					bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
					bloqueoFolioService.saveAndUpdate(bloqueoFolioOrig);
					bloqueoFolio.setUsuario(bloqueoFolio.getUsuario());
				}
			}else{
				bloqueoFolioOrig=new BloqueoFolio();
				Integer version=bloqueoFolioRepository.maxByPredio(bloqueoFolio.getPredio().getId());

					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
				bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
				bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
				bloqueoFolioOrig.setPredio(predio);
				bloqueoFolioService.saveAndUpdate(bloqueoFolioOrig);
				bloqueoFolio.setId(bloqueoFolioOrig.getId());
				bloqueoFolio.setVersion(version);
				bloqueoFolio.setUsuario(bloqueoFolioOrig.getUsuario());
			}
		}else{
			return new ResponseEntity<>(bloqueoFolio, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(bloqueoFolio, HttpStatus.OK);
	}

	@GetMapping("/ultimo-bloqueo/{folio}")
	public ResponseEntity<BloqueoFolioVO> getLastVersion (@PathVariable("folio")Long folio){
		BloqueoFolioVO bloqueoFolioVO=null;
		BloqueoFolio bloqueoFolio=null;
		bloqueoFolio=bloqueoFolioService.getLastVersion(folio);

		if(bloqueoFolio!=null){

			bloqueoFolioVO = new BloqueoFolioVO();

			bloqueoFolioVO.setUsuario(bloqueoFolio.getUsuario());
			bloqueoFolioVO.setId(bloqueoFolio.getId());
			bloqueoFolioVO.setVersion(bloqueoFolio.getVersion());
			bloqueoFolioVO.setMotivo(bloqueoFolio.getMotivo());
			bloqueoFolioVO.setObservaciones(bloqueoFolio.getObservaciones());
			bloqueoFolioVO.setPredio(bloqueoFolioService.copyProperties(bloqueoFolio.getPredio()));
		}

		return new ResponseEntity<>(bloqueoFolioVO, HttpStatus.OK);
	}

	
	

















	@GetMapping (value = "/findFolioByPrelacionId/{prelacionId}")
	public ResponseEntity<Long> findDocumentos(@PathVariable ("prelacionId") Long prelacionId){
		return ResponseEntity.ok(predioService.findNumFolio(prelacionId));
	}



	@PutMapping("/valido/")
	public ResponseEntity<?> changeStatusPrelacionPredio(@RequestBody PrelacionVO pre) throws URISyntaxException {


		try {
			Long prelacionId = pre.getId();
			Long predioId = null;
			Boolean status = pre.getPrelacionesPredio()[0].getValido();
			Long tipoFolioId = pre.getPrelacionesPredio()[0].getTipoFolio().getId();

			Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromLong(tipoFolioId);

      
		
			switch (tipo) {
				case PREDIO:
				 predioId = pre.getPrelacionesPredio()[0].getPredio().getId();
				break;
				case MUEBLE:
				predioId=  pre.getPrelacionesPredio()[0].getMueble().getId(); 
				
					break;
				case PERSONAS_JURIDICAS:
				predioId=  pre.getPrelacionesPredio()[0].getPersonaJuridica().getId();
					break;
				case PERSONAS_AUXILIARES:
				predioId=  pre.getPrelacionesPredio()[0].getFolioSeccionAuxiliar().getId();
					break;
				
			}
			
			System.out.println("DATOS DE BUSQUEDA "+predioId+"   Tipo folio "+ tipoFolioId);

			prelacionPredioService.setPrelacionPredioStatusByTipoFolio(prelacionId, tipoFolioId, predioId, status);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@PostMapping("/predio-persona-by-persona/")
	public ResponseEntity<?> getPredioPersonaByPersona(@RequestBody Persona persona) throws URISyntaxException {
		return new ResponseEntity<>(predioPersonaService.getPredioPersonaByPersona(persona), HttpStatus.OK);
		}
	
	@PostMapping("/predio-persona-by-libro/")
	public ResponseEntity<?> getPredioPersonaByLibro(@RequestBody AntecedenteVO ante) throws URISyntaxException {
		return new ResponseEntity<>(predioPersonaService.getPredioPersonaByLibro(ante), HttpStatus.OK);
		}
	
	@PostMapping("/predio-persona-by-busquedaVO/")
	public ResponseEntity<?> getPredioPersonaByBusquedaVO(@RequestBody BusquedaVO busquedaVO) throws URISyntaxException {
		return new ResponseEntity<>(predioPersonaService.getPredioPersonaByBusquedaVo(busquedaVO), HttpStatus.OK);
		}

	@GetMapping("/findPredioPersonaByNoFolio/{noFolio}")
	public ResponseEntity<?> findPredioPersonaByNoFolio(@PathVariable Integer noFolio){
		log.debug("Buscando el predio con no folio: {}",noFolio);
		return new ResponseEntity<>(predioPersonaService.getPredioPersonaByNoFolio(noFolio), HttpStatus.OK);
		}
	@GetMapping(value = "findPredioBitacoraByPredioID/{id}")
	public ResponseEntity<List<PredioBitacora>> findPredioBitacoraByPredioID(@PathVariable("id") Long id) {
			return new ResponseEntity<>(predioService.findPredioBitacoraByPredioID(id), HttpStatus.OK);
	}
	@GetMapping(value = "findColindanciasByPredioBitacoraId/{id}")
	public ResponseEntity<List<PredioBitacoraColindancia>> findColinciasByPredioBitacoraId(@PathVariable("id") Long id) {
			return new ResponseEntity<>(predioService.findColindanciasByPredioBitacoraId(id), HttpStatus.OK);
	}

	@GetMapping("/findPrediosByAntecedente/{idAntecedente}")
	public ResponseEntity<List<PredioVO>> findPrediosByAntecedente(@PathVariable Long idAntecedente){
		log.debug("Buscando los predios con id de antecedente: {}",idAntecedente);
		return new ResponseEntity<>(predioService.findPrediosByAntecedente(idAntecedente), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}/titulares-iniciales-mantenimiento")
	public ResponseEntity<List<PredioPersona>> findTitularesInicialesMantenimiento(@PathVariable("id")Long id) {
		log.debug("Buscando titulares iniciales para id=" + id);		
		return new ResponseEntity<>(predioPersonaService.findPropietariosInicialesMantenimiento(id), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/findFoliosCapturaMasiva/", method = RequestMethod.POST, consumes = {"multipart/form-data"})
	public ResponseEntity<?> findAllFoliosCapturaMasiva(@RequestBody MultipartFile file){
		//predioService.validaFoliosMasivo(file, null);			
		return new ResponseEntity<>(predioService.validaFoliosMasivo(file, null), HttpStatus.OK);
	}
	
	@PostMapping("/extinto")
	public ResponseEntity<Predio> statusEnAclaracion(@RequestParam("prelacionId") Long id, @RequestParam("folioId")  Long predioId, @RequestParam("fundamento")  String fundamento) throws URISyntaxException {
		Predio result=null;
		try {
			result = predioService.updateStatusExtinto(predioId ,fundamento,true);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/activo")
	public ResponseEntity<Predio> statusEnActivo(@RequestParam("prelacionId") Long id, @RequestParam("folioId")  Long predioId, @RequestParam("fundamento")  String fundamento) throws URISyntaxException {
		Predio result=null;
		try {
			result = predioService.updateStatusExtinto(predioId ,fundamento,false);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping(value = "/findExtintos/{predioId}")
	public ResponseEntity<?> findExtintos(@PathVariable Long predioId){
		return new ResponseEntity<>(predioService.findPredioExtinto(predioId) ,HttpStatus.OK);
	}
	
	@GetMapping(value = "/{predioId}/avisos-vigentes")
	public ResponseEntity<Boolean>  contienePreventivosVigentes(@PathVariable Long predioId){
		return  new ResponseEntity<>(predioService.existAvisosVigentes(predioId), HttpStatus.OK);
	}
	
	
	@GetMapping("/findFoliosByFolio/{folios}/TipoFolio/{tipo}/Oficina/{oficinaId}")
	public ResponseEntity<?> findFoliosByFolioAndTipoFolio(@PathVariable String folios,@PathVariable Integer tipo, @PathVariable Long oficinaId){
		Usuario userActive = null;
		StringBuilder sb = new StringBuilder();
		StringBuilder sbOfi = new StringBuilder();
		List<FoliosAnteVO> foliosVo = new ArrayList<FoliosAnteVO>();
		try {
			userActive = usuarioService.getLoguedUser();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		oficinaId = userActive.getOficina().getId();
		boolean valido=false;
		String[] ids = folios.split(",");
		Long tipol = Long.valueOf(tipo.longValue());
		for (Constantes.ETipoFolio tp : Constantes.ETipoFolio.values()) {
			if ((int)(long)tp.idTipoFolio == tipol) {
				valido = true;
			}
		}
		if(valido == true){
			for(String idFolio: ids) {
				FoliosAnteVO fAnteVO = predioService.busquedaFolios(Integer.parseInt(idFolio), tipo, oficinaId);
				if(fAnteVO.getValores()) {
					foliosVo.add(fAnteVO);
				}
			}
			
			if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
				
			}
			if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
				
			}
			if((int)(long)tipol == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
				
			}
			if((int)(long)tipol == Constantes.ETipoFolio.PREDIO.idTipoFolio){
				for(FoliosAnteVO vo: foliosVo) {
					if(vo.getPredio().getBloqueado()!=null && vo.getPredio().getBloqueado()) {
						sb.append("NO ES POSIBLE AGREGAR EL FOLIO " + vo.getPredio().getNoFolio() + " POR ENCONTRARSE BLOQUEADO.");
						sb.append("\n");
					}
					if(vo.getPredio().getStatusActo().getId().equals(Constantes.StatusActo.INVALIDO.getIdStatusActo())) {
						sb.append("NO ES POSIBLE AGREGAR EL FOLIO " + vo.getPredio().getNoFolio() + " POR ENCONTRARSE EXTINTO.");
						sb.append("\n");
					}
					if(vo.getPredio().getOficina().getId() != oficinaId ) {
						sbOfi.append("EL FOLIO " + vo.getPredio().getNoFolio() + " NO PERTENECE A SU OFICINA REGISTRAL.");
						sbOfi.append("\n");
					}
				}
			}
			if(sb.toString().length()>0) {
				return new ResponseEntity<>(sb.toString(),HttpStatus.BAD_REQUEST);
			}
			else if(sbOfi.toString().length()>0) {
				return new ResponseEntity<>(sbOfi.toString(),HttpStatus.BAD_REQUEST);
			}else {
					return new ResponseEntity<>(foliosVo,HttpStatus.OK);
			}
			
		}else{
			return new ResponseEntity<>("El tipo de folio es incorrecto.",HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/heredar-actos/{predioTargetId}")
	public ResponseEntity<Boolean> heredarActos(@PathVariable("predioTargetId") Long predioTargetId,@RequestBody List<Acto> actos)
	{
		try {
			return new ResponseEntity<>(predioService.heredarActos(actos, predioTargetId),HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<>(false,HttpStatus.CONFLICT);
		}
	}
	
}
