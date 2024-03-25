package com.gisnet.erpp.web.api.prelacion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPublicitario;
import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.BitacoraActoRechazo;
import com.gisnet.erpp.domain.BitacoraReingreso;
import com.gisnet.erpp.domain.BusquedaResultado;
import com.gisnet.erpp.domain.BusquedaResultadoActo;
import com.gisnet.erpp.domain.Certificado;
import com.gisnet.erpp.domain.Cita;
import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.DireccionArea;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionAnte;
import com.gisnet.erpp.domain.PrelacionBoleta;
import com.gisnet.erpp.domain.PrelacionOficio;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.PrelacionServicio;
import com.gisnet.erpp.domain.PrelacionTestamento;
import com.gisnet.erpp.domain.PrelacionUsuarioDef;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.domain.ReciboConcepto;
import com.gisnet.erpp.domain.ServicioConceptoPago;
import com.gisnet.erpp.domain.StatusExterno;
import com.gisnet.erpp.domain.Suspension;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.BusquedaResultadoRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.ReciboConceptoRepository;
import com.gisnet.erpp.repository.ReciboRepository;
import com.gisnet.erpp.repository.SeccionRepository;
import com.gisnet.erpp.repository.TipoMotivoRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.ActoRequisitoService;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.AntecedenteService;
import com.gisnet.erpp.service.BitacoraDigitalizadorService;
import com.gisnet.erpp.service.BitacoraReingresoService;
import com.gisnet.erpp.service.BitacoraEntregaService;
import com.gisnet.erpp.service.BitacoraService;
import com.gisnet.erpp.service.BusquedaResultadoService;
import com.gisnet.erpp.service.BusquedaService;
import com.gisnet.erpp.service.CitaService;
import com.gisnet.erpp.service.ConceptoPagoService;
import com.gisnet.erpp.service.DocumentoService;
import com.gisnet.erpp.service.MaterializacionActoService;
import com.gisnet.erpp.service.MuebleService;
import com.gisnet.erpp.service.OficinaService;
import com.gisnet.erpp.service.PersonaAuxiliarService;
import com.gisnet.erpp.service.PersonaJuridicaService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.service.PrelacionAnteService;
import com.gisnet.erpp.service.PrelacionBoletaService;
import com.gisnet.erpp.service.PrelacionPredioService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.service.PrelacionTestamentoService;
import com.gisnet.erpp.service.ReciboService;
import com.gisnet.erpp.service.ServicioService;
import com.gisnet.erpp.service.SolicitudDevolucionService;
import com.gisnet.erpp.service.SuspensionBitacoraService;
import com.gisnet.erpp.service.TipoActoService;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.service.SuspensionService;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.MessageObject;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.ArchivoFirmaVO;
import com.gisnet.erpp.vo.CadenaOriginalVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetalleAtendioVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetalleFoliosVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetallePagoVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetalleTramiteVO;
import com.gisnet.erpp.vo.ConsultaPrelacionVO;
import com.gisnet.erpp.vo.PredioAnteVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.PersonaJuridicaAnteVO;
import com.gisnet.erpp.vo.PrelacionBitacoraImpresionVO;
import com.gisnet.erpp.vo.PrelacionCopiaCertificadaFolioVO;
import com.gisnet.erpp.vo.PrelacionVO;
import com.gisnet.erpp.vo.PublicVO;
import com.gisnet.erpp.vo.ReciboVO;
import com.gisnet.erpp.vo.ReingresoVO;
import com.gisnet.erpp.vo.RequisitoVO;
import com.gisnet.erpp.vo.ResultPrelacionVO;
import com.gisnet.erpp.vo.ServicioAndSubVO;
import com.gisnet.erpp.vo.prelacion.ModificarPrelacionVO;
import com.gisnet.erpp.vo.prelacion.TotalPagesVO;
import com.gisnet.erpp.vo.PrelacionActosVO;
import com.gisnet.erpp.vo.PrelacionDocumentosVO;
import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.vo.BuscarFolioVO;

import net.sf.jasperreports.engine.JRException;
@RestController
@RequestMapping(value = "/api/prelacion", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrelacionRestController {


	private final Logger log = LoggerFactory.getLogger(this.getClass());


    private final PrelacionService prelacionService;

	@Autowired
	ActoService		   actoService;

	@Autowired
	TipoActoService tipoActoService;

	@Autowired
	AntecedenteService antecedenteService;

	@Autowired
	ReciboService	   reciboService;

	@Autowired
	ActoRequisitoService   actoRequisitoService;

	@Autowired
	ServicioService   servicioService;

	@Autowired
	CitaService   citaService;

	@Autowired
	PredioService   predioService;

	@Autowired
	PrelacionTestamentoService   prelacionTestamentoService;

	@Autowired
	DocumentoService documentoService;

	@Autowired
	ConceptoPagoService conceptoPagoService;

	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	PersonaJuridicaService personaJuridicaService;

	@Autowired
	MuebleService muebleService;

	@Autowired
	PersonaAuxiliarService personaAuxiliarService;

	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;

	@Autowired
	private BitacoraService bitacoraService;

	@Autowired
	SeccionRepository seccionRepository;

	@Autowired
	private PrelacionAnteService prelacionAnteService;

	@Autowired
	private PrelacionPredioService prelacionPredioService;

	@Autowired
	private OficinaService oficinaService;

	@Autowired
	private BusquedaService busquedaService;
	
	@Autowired
	private TipoMotivoRepository tipoMotivoRepository;
	
	@Autowired
	private MaterializacionActoService materializacionActoService;

	@Autowired
	private ReciboRepository reciboRepository;

	@Autowired
	private SuspensionService suspensionService;
	
	@Autowired
	PrelacionRepository prelacionRepository;
	
	@Autowired
	private BusquedaResultadoService busquedaResultadoService;
	
	@Autowired
	private BusquedaResultadoRepository busquedaResultadoRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private BitacoraDigitalizadorService bitacoraDigitalizadorService;

	@Autowired
	private BitacoraReingresoService bitacoraReingresoService;
	
	@Autowired
	private PrelacionBoletaService prelacionBoletaService;
	
	@Autowired
	private BitacoraEntregaService bitacoraEntregaService;
	@Autowired 
	private SolicitudDevolucionService solicitudDevolucionService;
	
	@Autowired
	private SuspensionBitacoraService suspensionBitacoraService; 
	
	
    @Autowired
    public PrelacionRestController(PrelacionService prelacionService) {
        this.prelacionService = prelacionService;
    }


    @GetMapping("/findAll")
	public ResponseEntity<List<Prelacion>> findAll() {
		List<Prelacion> prelaciones = prelacionService.findAll();
		return new ResponseEntity<>(prelaciones, HttpStatus.OK);
	}

	@GetMapping("/findAll/{id}")
	public ResponseEntity<List<Prelacion>> findAllByUser(@PathVariable("id") int id) {
		//IHM 03/03/2020
		List<Prelacion> prelaciones = prelacionService.findAllByUsuario(id);

		System.out.println("Listado de prelaciones ---->" + prelaciones.size());
		return new ResponseEntity<>(prelaciones, HttpStatus.OK);
	}


	@PostMapping("/prelacion")
	public ResponseEntity<Long> createPrelacion(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso);

		Long prelacionId = prelacionService.create(prelacionIngreso);
		return new ResponseEntity<>(prelacionId, HttpStatus.OK);
	}

	@PutMapping("/")
	public ResponseEntity<?> finalizaPrelacion(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {

			Prelacion result=null;
			log.debug("prelacionIngreso{} "+prelacionIngreso);

			 Prelacion preEnvio=null;

			if(!prelacionIngreso.getEsPresencial()){
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
				if(usuario.getTipoUsuario().getId()==2 && prelacionIngreso.getOficina()!=null){
					Prelacion prelacion=prelacionRepository.findOne(prelacionIngreso.getId());
					//Se manda al turnador la prelacion
					preEnvio = new Prelacion();
					preEnvio.setId(prelacion.getId());
					preEnvio.setOficina(prelacionIngreso.getOficina());
				}
			}
			

			ResultPrelacionVO validacion = prelacionService.validaPrelacion(prelacionIngreso.getId(),prelacionIngreso.getContienePago(),prelacionIngreso.getEsPresencial());

			if(!validacion.getValida()) {

				 	return new ResponseEntity<>(validacion.getCodigos(), HttpStatus.BAD_REQUEST);
			}

		try {
			result = prelacionService.finalizaPrelacionVentanilla(prelacionIngreso.getId(), prelacionIngreso.getObservacion(), prelacionIngreso.getObservaciones(),prelacionIngreso.getTipoEntrega(),prelacionIngreso.getSolicitante(),prelacionIngreso.getTieneTermino(),prelacionIngreso.getDias(),prelacionIngreso.getHoras(),prelacionIngreso.getIsExcentoPago(),prelacionIngreso.getPrimerRegistro(),prelacionIngreso.getArea(), prelacionIngreso.getUrgente(),preEnvio);

		}
		catch (Exception err) {
			err.printStackTrace();
			System.out.println(err.getMessage());
		}




		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/testamento")
	public ResponseEntity<PrelacionTestamento> finalizaTestamento(@RequestBody PrelacionTestamento prelacionTestamento) throws URISyntaxException {
		log.debug("REST request crear testamento : {}");
			PrelacionTestamento result=null;
			System.out.println(prelacionTestamento.getId()+"--"+prelacionTestamento.getIndividual()+"--"+prelacionTestamento.getGrupal()+"--"+prelacionTestamento.getYaDepositado()+"--"+prelacionTestamento.getNumeroPersonas()+"--");
		try {
			 result = prelacionTestamentoService.saveAndUpdate(prelacionTestamento);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@PutMapping("/update-calificador")
	public ResponseEntity<Prelacion> pasaACalificador(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso.getId());
			Prelacion result=null;
		try {
			 result = prelacionService.updatePrelacionEstado(prelacionIngreso.getId(), Constantes.Status.ASIGNADO_A_COORDINADOR,null,null,null);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}




		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@PutMapping("/update-coordinador")
	public ResponseEntity<Prelacion> pasaACoordinador(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso.getId());
			Prelacion result=null;
		try {
			 result = prelacionService.updatePrelacionEstado(prelacionIngreso.getId(), Constantes.Status.ASIGNADO_A_COORDINADOR,null,null,null);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}




		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PutMapping("/update-listo-entregar")
	public ResponseEntity<Prelacion> pasaAListoParaEntregar(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso.getId());
			Prelacion result=null;
		try {
			 result = prelacionService.updatePrelacionEstado(prelacionIngreso.getId(), Constantes.Status.LISTO_PARA_ENTREGARSE,null,null,null);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PutMapping("/update-soporteOperacion")
	public ResponseEntity<Prelacion> pasaASoporte(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso.getId());
			Prelacion result=null;
		try {
			 result = prelacionService.updatePrelacionEstado(prelacionIngreso.getId(), Constantes.Status.SOPORTE_A_OPERACION,null,null,null);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@PutMapping("/regresar-analista")
	public ResponseEntity<Prelacion> regresarAnalista(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso.getId());
			Prelacion result=null;
		try {
			 result = prelacionService.updatePrelacionEstado(prelacionIngreso.getId(), Constantes.Status.ASIGNADO_A_ANALISTA,prelacionIngreso.getObservaciones(),prelacionIngreso.getTipoAclaracion(),null);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}




		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@PutMapping("/regresar-calificador")
	public ResponseEntity<Prelacion> regresarCalificador(@RequestBody PrelacionVO prelacionIngreso) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso.getId());
			Prelacion result=null;
		try {
			 result = prelacionService.updatePrelacionEstado(prelacionIngreso.getId(), Constantes.Status.ASIGNADO_A_CALIFICADOR,prelacionIngreso.getObservaciones(),prelacionIngreso.getTipoAclaracion(),null);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}


    @GetMapping("/{id}")
    public ResponseEntity<Prelacion> getPrelacion(@PathVariable Long id) {
        Prelacion prelacion = prelacionService.findOne(id);
        return new ResponseEntity<>(prelacion, HttpStatus.OK);
    }

    @GetMapping("/findPrelacionByUser")
    public ResponseEntity<List<Prelacion>> findPrelacionByUser() {
    	log.debug("Buscando prelaciones para el usuario : {}",SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<>(prelacionService.findNextPrelacionAsignadaAnalista(SecurityUtils.getCurrentUserLogin()), HttpStatus.OK);
    }

    @GetMapping("/finalizaPrelacionCyvf/{idPrelacion}")
    public ResponseEntity<?> finalizaPrelacionCyvf(@PathVariable Long idPrelacion){
		System.out.println("finalizaPrelacionCyvf");
    	try {
    		return  new ResponseEntity<>(prelacionService.finalizaPrelacionCyvf(idPrelacion), HttpStatus.OK);	
    	}
    	catch (Exception e) {
    		return  new ResponseEntity<>(new MessageObject("No se pudo finalizar la prelacion " + e.getMessage()), HttpStatus.BAD_REQUEST);
    	}
    }


	@PostMapping("/antecedente")
	public ResponseEntity<?> crearAntecedentePrelacion(@RequestBody AntecedenteVO datosAnte){


    	PrelacionAnte preAnte = new PrelacionAnte();
    	try {


			preAnte.setDocumento(datosAnte.getDocumento());
			preAnte.setDocumentoBis(datosAnte.getDocumentoBis());
			preAnte.setAnio(datosAnte.getAnio());
			preAnte.setLibro(datosAnte.getLibro());
			preAnte.setLibroBis(datosAnte.getLibroBis());
			preAnte.setSeccion(seccionRepository.findOne( Long.parseLong(datosAnte.getSeccion()) ));
			preAnte.setOficina(preAnte.getOficina());
			preAnte.setTipoFolio( datosAnte.getTipoFolio());

		}
		catch (Exception except) {
			log.debug("Error en la conversion" );
			except.printStackTrace();
		}


		PrelacionAnte antTemp=antecedenteService.saveAndUpdateAntecedente(preAnte,datosAnte.getPrelacionId());
		if (antTemp != null) {
    			Prelacion pre = prelacionService.findOne(datosAnte.getPrelacionId());
				pre.setTipoBusqueda(datosAnte.getTipoBusqueda());

				prelacionService.saveAndUpdate(pre);


		}else{
			return new ResponseEntity<>("Los datos de ingreso para guardar el antecedente son incorrectos",HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(antTemp, HttpStatus.OK);
	}


	/***
	 * Servicios de actos
	 */
	@PostMapping("/acto")
	public ResponseEntity<?> crearActoPrelacion(@RequestBody PrelacionVO prelacionIngreso){

			List<Acto> actos= new ArrayList<Acto>();
			if(prelacionIngreso.getActos()!=null){
				Prelacion prelacion = prelacionService.findOne(prelacionIngreso.getId());
				for(Acto acto:prelacionIngreso.getActos() ){
						//Acto actoNuevo= actoService.getActoByVO(acto);
						acto.setPrelacion(prelacion);
						acto=actoService.saveAndUpdate(acto);
						if(acto!=null){
							actos.add(acto);
						}


				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar el acto son incorrectos",HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(actos, HttpStatus.OK);
	}


	
	@DeleteMapping("/acto/{actoId}")
	public ResponseEntity<?> deleteActoPrelacion(@PathVariable("actoId") Long id){

			List<Acto> actos= null;

			Acto acto= actoService.deleteActo(id);
			if(acto!=null){

				actos=actoService.findByPrelacionId(acto.getPrelacion().getId());
			}else{
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(actos, HttpStatus.OK);
	}
	

	@GetMapping("/acto/{actoId}/materializa")
	public ResponseEntity<Boolean> materializaActo(@PathVariable("actoId") Long actoId){
		Acto acto = actoService.findOne(actoId);
		materializacionActoService.materializar(acto);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/actos")
	public ResponseEntity<List<Acto>> actosByPrelacionId(@PathVariable("id") Long id) {
		List <Acto> listActos = actoService.findByPrelacionId(id);
		if (listActos.size() >0) {


			return new ResponseEntity<>(listActos, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<Acto>) null, HttpStatus.NOT_FOUND);
	}



	@GetMapping("/acto/{actoId}/dematerializa")
	public ResponseEntity<Boolean> deMaterializaActo(@PathVariable("actoId") Long actoId){
		Acto acto = actoService.findOne(actoId);
		materializacionActoService.deMaterializar(acto);

		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@GetMapping(value = "/recibosconcepto/{id}")
	public ResponseEntity<List<Recibo>> recibosConceptoByPrelacionId(@PathVariable("id") Long id) {
		System.out.println("funcion antes de recuperar recibos para prelacion: " + id);
		List <Recibo> recibos = reciboRepository.findByPrelacionId(id);
		System.out.println(recibos);
		System.out.println("recibos");
		if (recibos.size() >0) {
			return new ResponseEntity<>(recibos, HttpStatus.OK);
		} 
			return new ResponseEntity<>((List<Recibo>) null, HttpStatus.NOT_FOUND);
	}


	@PostMapping("/recibo")
	public ResponseEntity<?> crearReciboPrelacion(@RequestBody PrelacionVO prelacionIngreso){

			List<ReciboVO> recibos= new ArrayList<ReciboVO>();
			if(prelacionIngreso.getRecibos()!=null){

				for(ReciboVO recibo:prelacionIngreso.getRecibos() ){

						recibo.setPrelacion(prelacionService.findOne(prelacionIngreso.getId()));

						Recibo rec= reciboService.saveAndUpdateWithVO(recibo);
						if(rec!=null) {
							reciboService.saveAndUpdate( rec, recibo.getActos() );
							recibos.add( reciboService.convertVO( rec,recibo.getActos() ) );
						}
						System.out.println(recibo.getReferencia());
				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar el recibo de pago son incorrectos",HttpStatus.BAD_REQUEST);
			}
			log.debug("Lo siguiente es el recibo");
			log.debug(recibos.toString());
			return new ResponseEntity<>(recibos, HttpStatus.OK);
	}


	@DeleteMapping ("/recibo/{reciboId}")
	public ResponseEntity<?> removePrelacionRecibo (@PathVariable Long reciboId) {

		Recibo rec = reciboService.findByReciboId(reciboId);
		if (rec != null) {
			if ( reciboService.removeRecibo(rec) ) {
				return new ResponseEntity<>(true,HttpStatus.OK);
			}else {
				return new ResponseEntity<>("Error al eliminar el recibo",HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>("No se encontró el recibo especificado",HttpStatus.NOT_FOUND);


	}

	@PostMapping("/servicio")
	public ResponseEntity<?> crearServicioPrelacion(@RequestBody PrelacionVO prelacionIngreso){

			List<ServicioAndSubVO> servicios= new ArrayList<ServicioAndSubVO>();
			if(prelacionIngreso.getServicios()!=null){

				for(ServicioAndSubVO servicio:prelacionIngreso.getServicios() ){

						Prelacion prelacionActual= prelacionService.findOne(prelacionIngreso.getId());
						servicio=servicioService.saveServicioAndSubPrelacion(servicio,prelacionActual);

						servicios.add(servicio);

				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar el servicio son incorrectos",HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(servicios, HttpStatus.OK);
	}

	@PostMapping("/predio")
	public ResponseEntity<?> crearPredioPrelacion(@RequestBody PrelacionVO prelacionIngreso){

			List<PredioVO> predios= new ArrayList<>();
			if(prelacionIngreso.getPredios()!=null){

				for(PredioVO predio:prelacionIngreso.getPredios() ){
						System.out.println("Predio  : "+predio.getId() + " prelacion :  "+prelacionIngreso.getId() );
						Prelacion prelacionActual= prelacionService.findOne(prelacionIngreso.getId());
						predio=predioService.savePredioPrelacion(predio,prelacionActual);

						predios.add(predio);

				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar el predio son incorrectos",HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(predios, HttpStatus.OK);
	}

	@PostMapping("/prelacionPredio")
	public ResponseEntity<?> crearPrelacionPredio(@RequestBody PrelacionVO prelacionIngreso){

			List<PrelacionPredio> prelacionesPredio= new ArrayList<PrelacionPredio>();
			if(prelacionIngreso.getPrelacionesPredio()!=null){

				for(PrelacionPredio prelacionPredio:prelacionIngreso.getPrelacionesPredio() ){
						System.out.println("TIPOFOLIO  : "+prelacionPredio.getTipoFolio().getId());
						Prelacion prelacionActual= prelacionService.findOne(prelacionIngreso.getId());
						prelacionPredio=prelacionService.savePrelacionPredio(prelacionPredio,prelacionActual);

						prelacionesPredio.add(prelacionPredio);

				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar el predio son incorrectos",HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(prelacionesPredio, HttpStatus.OK);
	}
	
	@GetMapping("/crearPrelacionPredio/{prelacionId}/{predioId}/{primerRegistro}")
	public ResponseEntity<?> crearPrelacionPredioAnalisis(@PathVariable("prelacionId") Long prelacionId,
														  @PathVariable("predioId") Long predioId,
														  @PathVariable("primerRegistro") boolean primerRegistro) {
		PrelacionPredio prelacionPredio = prelacionService.savePrelacionPredioAnalisis(prelacionId,predioId,primerRegistro);
		return new ResponseEntity<>(prelacionPredio, HttpStatus.OK);
	}
	
	@GetMapping("/crearPrelacionPredioCyVF/{prelacionId}/{predioId}/{tipoFolioId}")
	public ResponseEntity<?> crearPrelacionPredioAnalisisCyVF(@PathVariable("prelacionId") Long prelacionId,
			@PathVariable("predioId") Long predioId,@PathVariable("tipoFolioId") Integer tipoFolioId) {
		PrelacionPredio prelacionPredio = new PrelacionPredio();
		Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromInt(tipoFolioId);
		
		switch (tipo) {
		case PERSONAS_JURIDICAS:
			Prelacion pre = prelacionService.findOne(prelacionId);
			prelacionPredio = personaJuridicaService.savePersonaJuridicaPrelacion(predioId, pre);
			break;
		case PERSONAS_AUXILIARES:
			break;
		case MUEBLE:
			break;
		case PREDIO:
			prelacionPredio = prelacionService.savePrelacionPredioAnalisis(prelacionId, predioId,true);
			break;
		}
		return new ResponseEntity<>(prelacionPredio, HttpStatus.OK);
	}
	
	@PostMapping("/personaJuridica")
	public ResponseEntity<?> crearPersonaJuridicaPrelacion(@RequestBody PrelacionVO prelacionIngreso){
			log.debug(prelacionIngreso.toString());

			List<PrelacionPredio> pp= new ArrayList<PrelacionPredio>();
			if(prelacionIngreso.getPersonasJuridicas()!=null){

				Prelacion prelacionActual= prelacionService.findOne(prelacionIngreso.getId());
				for(PersonaJuridica pjId:prelacionIngreso.getPersonasJuridicas() ){

						PrelacionPredio pj=personaJuridicaService.savePersonaJuridicaPrelacion(pjId.getId(),prelacionActual);

						log.debug("Prelacion predio guardado" + pj.getId());
						pp.add(pj);

				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar la prelacion son incorrectos",HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(pp, HttpStatus.OK);
	}


	@PostMapping("/folioSeccionAuxiliar")
	public ResponseEntity<?> crearPersonaAuxiliar(@RequestBody PrelacionVO prelacionIngreso){
			log.debug(prelacionIngreso.toString());

			List<PrelacionPredio> pp= new ArrayList<PrelacionPredio>();
			if(prelacionIngreso.getFolioSeccionAuxiliares()!=null){

				Prelacion prelacionActual= prelacionService.findOne(prelacionIngreso.getId());
				for(FolioSeccionAuxiliar folSecAux:prelacionIngreso.getFolioSeccionAuxiliares() ){

						PrelacionPredio pj=personaAuxiliarService.saveFolioSeccionAuxPrelacion(folSecAux.getId(),prelacionActual);

						log.debug("Prelacion predio guardado" + folSecAux.getId());
						pp.add(pj);

				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar la prelacion son incorrectos",HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(pp, HttpStatus.OK);
	}

	@PostMapping("/mueble")
	public ResponseEntity<?> crearMueblePrelacion(@RequestBody PrelacionVO prelacionIngreso){
			log.debug(prelacionIngreso.toString());

			List<PrelacionPredio> pp= new ArrayList<PrelacionPredio>();
			if(prelacionIngreso.getMuebles()!=null){

				Prelacion prelacionActual= prelacionService.findOne(prelacionIngreso.getId());
				for(Mueble mueble:prelacionIngreso.getMuebles()){

					PrelacionPredio ppMueble= muebleService.saveMueblePrelacion(mueble.getId(),prelacionActual);

						log.debug("Prelacion predio guardado" + mueble.getId());
						pp.add(ppMueble);

				}

			}else{
				return new ResponseEntity<>("Los datos de ingreso para guardar la prelacion son incorrectos",HttpStatus.BAD_REQUEST);
			}

			return new ResponseEntity<>(pp, HttpStatus.OK);
	}


	@DeleteMapping("/{prelacionId}/predio/{predioId}")
	public ResponseEntity<?> deletePredioPrelacion(@PathVariable("prelacionId") Long prealacionId, @PathVariable("predioId") Long predioId){

			PrelacionPredio prelPredio;

			prelPredio = prelacionService.deletePredioPrelacion(prealacionId, predioId);
			if(prelPredio == null && predioId != -1){

				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@DeleteMapping("/{prelacionId}/actos")
	public ResponseEntity<?> deleteActosPrelacion(@PathVariable("prelacionId") Long prealacionId){

			Boolean resultDelete = actoService.deleteActosPrelacion(prealacionId);
			if(!resultDelete){

				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(true, HttpStatus.OK);
	}


	@DeleteMapping("/{prelacionId}/prelacionPredio/{prelacionPredioId}")
	public ResponseEntity<?> deletePredioPrelacionById(@PathVariable("prelacionId") Long prealacionId, @PathVariable("prelacionPredioId") Long prelacionPredioId){

			PrelacionPredio prelPredio= null;

			prelPredio = prelacionService.deletePredioPrelacionById(prealacionId, prelacionPredioId);
			if(prelPredio != null){

				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@DeleteMapping("/{prelacionId}/antecedente/{antecedenteId}")
	public ResponseEntity<?> deleteAntecedentePrelacion(@PathVariable("prelacionId") Long prelacionId, @PathVariable("antecedenteId") Long antecedenteId){

		PrelacionAnte antePredio= null;

		antePredio = prelacionService.deleteAntecedentePrelacion(prelacionId, antecedenteId);
		if(antePredio == null){

			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@DeleteMapping("/{prelacionId}/servicio")
	public ResponseEntity<?> deleteServicioPrelacion(@PathVariable("prelacionId") Long prelacionId ){

		Boolean result= null;

		result= servicioService.deleteServicioPrelacion(prelacionId);
		if(! result ){

			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	//JADV-SUSPENSION
	@PostMapping("/suspencion")
	public ResponseEntity<?> SuspenderPrelacion (@RequestParam("prelacionId") Long id, @RequestParam("tipoUsuarioId") Long tipoUsuarioId, @RequestParam("motivoId") Long motivoId, @RequestParam("fundamentoId") Long fundamentoId, @RequestParam("observaciones") String observaciones) {
	
		boolean resultado = false;
		if (motivoId==null) {
			return new ResponseEntity<>("El objeto de envio es invalido", HttpStatus.CONFLICT);
		}
		try {
			resultado = prelacionService.SuspenderPrelacion(id, tipoUsuarioId,  motivoId,fundamentoId, observaciones);
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(resultado, HttpStatus.OK);

	}

	@GetMapping("/suspension/diasadicionales/{prelacionId}/{observaciones}")
	public ResponseEntity<?> AgregarDiasAdicionalesSuspension (@PathVariable("prelacionId") Long id, @PathVariable("observaciones") String observaciones) {
	
		boolean resultado = false;
		try {
			resultado = prelacionService.AgregarDiasAdicionalesSuspension(id, observaciones);
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(resultado, HttpStatus.OK);

	}

	@GetMapping("/MotivoFundamentoSuspension/{id}")
	public ResponseEntity<?> MotivoFundamentoSuspension (@PathVariable("id") Long id) {
	
		Bitacora resultado = null;
		Prelacion prelacion = new Prelacion();
		
		try {
			prelacion = prelacionService.findOne(id);
			resultado = bitacoraService.findOneBitacoraByPrelacionIdAndStatusId(prelacion);
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(resultado, HttpStatus.OK);

	}
	
	@GetMapping("/PrelacionesSuspendidas/{oficinaId}")
	public ResponseEntity<?> PrelacionesSuspendidas (@PathVariable("oficinaId") Long oficinaId) {

		try {

			return new ResponseEntity<>(prelacionService.listaPrelacionesSuspendidas(oficinaId), HttpStatus.OK);
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/EsPrelacionSuspendida/{prelacionId}")
	public ResponseEntity<?> EsPrelacionSuspendida (@PathVariable("prelacionId") Long prelacionId) {
		try {
			return new ResponseEntity<>(prelacionService.esPrelacionSuspendida(prelacionId), HttpStatus.OK);
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/*	
	@GetMapping(value = "/{id}/antecedentes")
	public ResponseEntity<List<Antecedente>> antecedentesByPrelacionId(@PathVariable("id") Long id) {
		List<PrelacionAnte> listAnte = antecedenteService.findByPrelacionId(id);
		List<Antecedente> antecedentes=null;
		if (listAnte.size() >0) {

			antecedentes = new ArrayList<Antecedente>();
			for(PrelacionAnte ante: listAnte){
				Antecedente anteTemp= ante.getAntecedente();
				//anteTemp.setPrelacionAnte(ante.getId());
				antecedentes.add(anteTemp);

			}

			return new ResponseEntity<>(antecedentes, HttpStatus.OK);
		}else{
			return new ResponseEntity<>((List<Antecedente>) null,HttpStatus.NOT_FOUND);
		}

	}*/


	/*@GetMapping(value = "/{id}/conceptos/{tipo}")
	public ResponseEntity<?> conceptosByPrelacionIdAndTipoFolioId(@PathVariable("id") Long id,@PathVariable("tipo") Long tipo){
				long[] tipos = {PERSONAS_JURIDICAS,PERSONAS_AUXILIARES,MUEBLE,PREDIO};
				long tipol=tipo.longValue();
				if(LongStream.of(tipos).anyMatch(x -> x == tipol)){
				List <PrelacionPredio> listPrelacionPredio = predioService.findByPrelacionIdAndTipoFolioId(id,tipo);
				if (listPrelacionPredio.size() >0) {
						if(tipol == PERSONAS_JURIDICAS){
							List<PersonaJuridica> personasJuridicas=null;
							personasJuridicas = new ArrayList<PersonaJuridica>();
							for(PrelacionPredio prelacion: listPrelacionPredio){
								personasJuridicas.add(prelacion.getPersonaJuridica());
							}
							return new ResponseEntity<>(personasJuridicas, HttpStatus.OK);
						}
						if(tipol == PERSONAS_AUXILIARES){
							List<FolioSeccionAuxiliar> auxiliares=null;
							auxiliares = new ArrayList<FolioSeccionAuxiliar>();
							for(PrelacionPredio prelacion: listPrelacionPredio){
								auxiliares.add(prelacion.getFolioSeccionAuxiliar());
							}
							return new ResponseEntity<>(auxiliares, HttpStatus.OK);
						}
						if(tipol == MUEBLE){
							List<Mueble> muebles=null;
							muebles = new ArrayList<Mueble>();
							for(PrelacionPredio prelacion: listPrelacionPredio){
								muebles.add(prelacion.getMueble());
							}
							return new ResponseEntity<>(muebles, HttpStatus.OK);
						}
						if(tipol == PREDIO){
							List<Predio> predios=null;
							predios = new ArrayList<Predio>();
							for(PrelacionPredio predio: listPrelacionPredio){
								predios.add(predio.getPredio());
							}
							return new ResponseEntity<>(predios, HttpStatus.OK);
						}

				}else{

						if(tipo == PERSONAS_JURIDICAS){
							return new ResponseEntity<>((List<PersonaJuridica>) null, HttpStatus.NOT_FOUND);
						}
						if(tipo == PERSONAS_AUXILIARES){
							return new ResponseEntity<>((List<AuxiliarPersona>) null, HttpStatus.NOT_FOUND);
						}
						if(tipo == MUEBLE){
							return new ResponseEntity<>((List<Mueble>) null, HttpStatus.NOT_FOUND);
						}
						if(tipo == PREDIO){
							return new ResponseEntity<>((List<Predio>) null, HttpStatus.NOT_FOUND);
						}
				}
				return new ResponseEntity<>("El tipo de folio es incorrecto.",HttpStatus.BAD_REQUEST);
			}else{
				return new ResponseEntity<>("El tipo de folio es incorrecto.",HttpStatus.BAD_REQUEST);
			}
	}

	*/

	@GetMapping(value = "/{id}/predios")
	public ResponseEntity<List<Predio>> prediosByPrelacionId(@PathVariable("id") Long id) {
		List<Predio> predios=null;
		List <PrelacionPredio> listPredio = predioService.findByPrelacionId(id);
		if (listPredio.size() >0) {

			predios = new ArrayList<Predio>();
			for(PrelacionPredio predio: listPredio){

				predios.add(predio.getPredio());
			}

			return new ResponseEntity<>(predios, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<Predio>) null, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/{id}/prelacion-predio")
	public ResponseEntity<List<PrelacionPredio>> prelacionPredioByPrelacionId(@PathVariable("id") Long id) {
		List <PrelacionPredio> listPredio = predioService.findByPrelacionId(id);
		if (listPredio.size() >0) {
			return new ResponseEntity<>(listPredio, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<PrelacionPredio>) null, HttpStatus.NOT_FOUND);
	}
	
	
	@GetMapping (value = "/{folio}/{tipoFolio}/isFolioOcupado")
	public ResponseEntity<Boolean> isFolioOcupado(@PathVariable("folio") Integer folio, @PathVariable("tipoFolio") Integer tipoFolio){

		return new ResponseEntity<Boolean>(prelacionService.isFolioOcupado(folio, tipoFolio), HttpStatus.OK);

	}

	@PutMapping(value = "/acto-herencia")
	public ResponseEntity<List<Acto>> findActosHerencia(@RequestBody PrelacionPredio prelacionPredio) {
		List <Acto> listActo = predioService.findActosHerencia(prelacionPredio);

		return new ResponseEntity<>(listActo, HttpStatus.OK);
	}

	@PutMapping(value = "/updatePrelacionPredio")
	public ResponseEntity<PrelacionPredio> updatePrelacionPredio(@RequestBody PrelacionPredio prelacionPredio) {
		PrelacionPredio newPrelacionPredio = new PrelacionPredio();
		try{
			PrelacionPredio parentPrelacionPredio = prelacionPredioRepository.findOne(prelacionPredio.getId());
			int tipoFolio=(int)(long)parentPrelacionPredio.getTipoFolio().getId();

			newPrelacionPredio.setPrelacion(parentPrelacionPredio.getPrelacion());

			if(tipoFolio == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
				newPrelacionPredio.setPersonaJuridica(prelacionPredio.getPersonaJuridica());
			}
			if(tipoFolio == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
				newPrelacionPredio.setFolioSeccionAuxiliar(prelacionPredio.getFolioSeccionAuxiliar());
			}
			if(tipoFolio == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
				newPrelacionPredio.setMueble(prelacionPredio.getMueble());
			}
			if(tipoFolio == Constantes.ETipoFolio.PREDIO.idTipoFolio){
				newPrelacionPredio.setPredio(prelacionPredio.getPredio());
			}
			newPrelacionPredio.setTipoFolio(parentPrelacionPredio.getTipoFolio());
			newPrelacionPredio.setIdVersion(parentPrelacionPredio.getIdVersion());
			newPrelacionPredio.setVersion(parentPrelacionPredio.getVersion()+1);
			newPrelacionPredio.setEstatus(1);
			prelacionPredioRepository.save(newPrelacionPredio);
			
			PrelacionPredio prelExist = prelacionService.deletePredioPrelacionById(parentPrelacionPredio.getPrelacion().getId(),parentPrelacionPredio.getId());
			
			return new ResponseEntity<>(newPrelacionPredio, HttpStatus.OK);
		}catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return new ResponseEntity<>(newPrelacionPredio, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}/recibos")
	public ResponseEntity<List<Recibo>> recibosByPrelacionId(@PathVariable("id") Long id) {
		List <Recibo> recibos = reciboService.findByPrelacionId(id);
		if (recibos.size() >0) {
			return new ResponseEntity<>(recibos, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<Recibo>) null, HttpStatus.NOT_FOUND);
	}


	@GetMapping(value = "/{id}/servicios")
	public ResponseEntity<List<ServicioAndSubVO>> serviciosByPrelacionId(@PathVariable("id") Long id) {
		List<ServicioAndSubVO> listServicios = servicioService.findByPrelacionId(id);
		if (listServicios.size() >0) {
			return new ResponseEntity<>(listServicios, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<ServicioAndSubVO>) null, HttpStatus.NOT_FOUND);
	}


	@GetMapping(value = "/conceptos-pago/{idPrelacion}")
	public ResponseEntity<List<ConceptoPago>> getConceptoPagoActosPrelacion(@PathVariable("idPrelacion") Long idPrelacion) {
		List<ConceptoPago> listCon = conceptoPagoService.findAllByActoPrelacion(idPrelacion);
		if (listCon != null && listCon.size() >0) {
			return new ResponseEntity<>(listCon, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<ConceptoPago>) null, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/conceptos-pago/servicio/{idPrelacion}")
	public ResponseEntity<List<ServicioConceptoPago>> getConceptoPagoServicioPrelacion(@PathVariable("idPrelacion") Long idPrelacion) {
		List<ServicioConceptoPago> listCon = conceptoPagoService.findAllByServicioPrelacion(idPrelacion);
		if (listCon.size() >0) {
			return new ResponseEntity<>(listCon, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<ServicioConceptoPago>) null, HttpStatus.NOT_FOUND);
	}


	@PostMapping("/update-status-aclaracion")
	public ResponseEntity<Prelacion> statusEnAclaracion(@RequestParam("prelacionId") Long id, @RequestParam("observaciones")  String observaciones, @RequestParam("tipoAclaracion")  Long aclaracion) throws URISyntaxException {
		Prelacion result=null;
		try {
			result = prelacionService.updatePrelacionEstadoAclaracion(id, Constantes.Status.EN_ACLARACION_ANTECEDENTE, observaciones ,aclaracion);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@PutMapping("/firma-prelacion")
	public ResponseEntity<Prelacion> firmarPrelacion(@RequestBody ArchivoFirmaVO firma) throws URISyntaxException {
		Prelacion result=null;
		try {
			result = prelacionService.firmaPrelacion(firma);
			if(result==null){

				System.out.println("\n\n\n /********+*** RESULTADO FALSO  *************/");

				return new ResponseEntity<>((Prelacion) null, HttpStatus.BAD_REQUEST);
			}else{
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>((Prelacion) null, HttpStatus.BAD_REQUEST);
		}

		//return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/firma-suspension")
	public ResponseEntity<Suspension> firmarSuspension(@RequestBody ArchivoFirmaVO firma) throws URISyntaxException {
		Suspension result=null;
		try {			
			  result = suspensionService.saveSuspensionFirma(firma);
			if(result==null){

				System.out.println("\n\n\n /********+*** RESULTADO FALSO  *************/");

				return new ResponseEntity<>((Suspension) null, HttpStatus.BAD_REQUEST);
			}else{
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>((Suspension) null, HttpStatus.BAD_REQUEST);
		}

		//return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@GetMapping(value = "/cadena-firma/{idPrelacion}")
	public ResponseEntity<CadenaOriginalVO> getCadenaFirmadoPrelacion(@PathVariable("idPrelacion") Long idPrelacion) {
		String cadena = prelacionService.getCadenaFirmado(idPrelacion);
		log.info(cadena);
		CadenaOriginalVO cadVO = new CadenaOriginalVO();
		if (cadena!=null) {
			cadVO.setPrelacionId(idPrelacion);
			cadVO.setCadenaOriginal(cadena);
			return new ResponseEntity<>(cadVO, HttpStatus.OK);
		}

		return new ResponseEntity<>((CadenaOriginalVO) null, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/cadena-firma-suspension/{idPrelacion}")
	public ResponseEntity<CadenaOriginalVO> getCadenaFirmadoSuspension(@PathVariable("idPrelacion") Long idPrelacion) {
		String cadena = prelacionService.getCadenaSuspensionFirmado(idPrelacion); 
		log.info(cadena);
		CadenaOriginalVO cadVO = new CadenaOriginalVO();
		if (cadena!=null) {			
			cadVO.setCadenaOriginal(cadena);
			return new ResponseEntity<>(cadVO, HttpStatus.OK);
		}

		return new ResponseEntity<>((CadenaOriginalVO) null, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/prelaciones")
	public 	ResponseEntity<?> getAllPrelaciones( Pageable pageable, Long folio, String fechaIngreso, String fechaEnvioFirma, String fechaBaja, Long solicitante,
												   Integer status, Integer tipoUsuarioSeleccionado
	) {

		if (tipoUsuarioSeleccionado  == null|| tipoUsuarioSeleccionado == 0)
			return new ResponseEntity<String>("Tipo de usuario no especificado", HttpStatus.BAD_REQUEST);
		Page<Prelacion> page = prelacionService.findAllByStatusAndUsuario(folio, fechaIngreso, fechaEnvioFirma, fechaBaja, solicitante, pageable, status, tipoUsuarioSeleccionado );
		if (page == null)
			return new ResponseEntity<>((Page<Prelacion>) null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(page, HttpStatus.OK);  
	}
	
	@GetMapping("/prelaciones-rechazadas")
	public 	ResponseEntity<?> getAllPrelacionesRechazadas( 
			Pageable pageable,
			Integer consecutivo, String desdeFecha, String hastaFecha, Integer escritura, 
			Long notarioId,
			Long contratanteId,
			Long tipoActoId,
			Long registradorId ) {
		log.debug( "===> consecutivo = "+consecutivo );
		log.debug( "===> desdeFecha = "+desdeFecha );
		log.debug( "===> hastaFecha = "+hastaFecha );
		log.debug( "===> escritura = "+escritura );
		log.debug( "===> notarioId = "+notarioId );
		log.debug( "===> contratanteId = "+contratanteId );
		log.debug( "===> tipoActoId = "+tipoActoId );
		log.debug( "===> registradorId = "+registradorId );
		
		Page<ConsultaPrelacionVO> page = prelacionService.findAllByRechazada(
				pageable, consecutivo, desdeFecha, hastaFecha, escritura,
				notarioId, contratanteId, tipoActoId, registradorId);

		if (page == null) {
			return new ResponseEntity<>((Page<ConsultaPrelacionVO>) null, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(page, HttpStatus.OK);
		}
	}

	@GetMapping("/prelaciones-by-status")
	public 	ResponseEntity<?> getAllPrelacionesByStatus( 
			Pageable pageable, Long folio, Integer folioPredio, String fechaIngreso, String fechaHasta,
			Long contratante,
			String referencia, Long region, Long area,
			Long coordinador,
			Long acto,
			Long notario,
			String escritura,
			String fechaEnvioFirma, String fechaBaja, 
			Long solicitante,
			Long registrador,
			Long calificador,
			Integer status, Integer tipoUsuarioSeleccionado,
			Boolean pantallaCoordinador,Integer prioridad
	) {
		
		log.debug( "===> pantallaCoordinador="+pantallaCoordinador );
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		if(region == null) {
			region= usuario.getOficina().getId();
		}

		if (tipoUsuarioSeleccionado  == null|| tipoUsuarioSeleccionado == 0)
			return new ResponseEntity<String>("Tipo de usuario no especificado", HttpStatus.BAD_REQUEST);
		Page<ConsultaPrelacionVO> page = prelacionService.findAllByStatus(
				folio, folioPredio, fechaIngreso, fechaHasta, fechaEnvioFirma, fechaBaja, 
				solicitante, registrador,calificador, pageable, status, tipoUsuarioSeleccionado,
				contratante,
				referencia, region, area,
				coordinador,
				acto,
				notario,
				escritura,
				pantallaCoordinador,prioridad
				);
		if (page == null)
			return new ResponseEntity<>((Page<ConsultaPrelacionVO>) null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@GetMapping("/prelacionesFirma")
	public 	ResponseEntity<?> getAllPrelaciones(
			Pageable pageable,
			Long tipo,
			Long folio,
			String fechaIngreso,
			String fechaEnvioFirma,
			Long solicitante,
			Long notario,
			Long calificador,
			Integer status,
			Integer tipoUsuarioSeleccionado
	) {


		//status = 1;   // Los que tienen estado de prelacion Asignados a coordinador, no finalizados
		List<Prelacion> page = prelacionService.findAllByStatusAndUsuarioCoordinador(tipo, folio, fechaIngreso, fechaEnvioFirma,solicitante, notario, calificador, pageable, status, tipoUsuarioSeleccionado );

		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		//page = page.stream().filter(prelacion -> prelacion.getUsuarioAnalista().getId().equals(usuario.getId())).collect(Collectors.toList());

		if (page == null)
			return new ResponseEntity<>((Page<Prelacion>) null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}


	@GetMapping(value = "/{id}/documentos-adjuntos")
	public ResponseEntity<List<RequisitoVO>> getDocumentosAdjuntosPrelacionId(@PathVariable("id") Long id) {
		List<RequisitoVO> listDocs = prelacionService.findAllDocAdjuntosByPrelacionId(id);
		if (listDocs.size() >0) {
			return new ResponseEntity<>(listDocs, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<RequisitoVO>) null, HttpStatus.NOT_FOUND);
	}


	@GetMapping(value = "/{id}/acto-predio")
	public ResponseEntity<List<ActoPredio>> getActoPredioByPrelacion(@PathVariable("id") Long id) {
		List<ActoPredio> listAP = prelacionService.findAllActoPredioByPrelacionAndVfFalse(id);
		if (listAP.size() >0) {
			return new ResponseEntity<>(listAP, HttpStatus.OK);
		}
			return new ResponseEntity<>((List<ActoPredio>) null, HttpStatus.NOT_FOUND);
	}


	@PostMapping("/oficio")
	public ResponseEntity<PrelacionOficio> createPrelacionOficio(@RequestBody PrelacionOficio prelacionOfi) throws URISyntaxException {

		 prelacionOfi = prelacionService.savePrelacionOficio(prelacionOfi);
		return new ResponseEntity<>(prelacionOfi, HttpStatus.OK);
	}


    @GetMapping("/prelacion-oficio/{prelacionId}")
    public ResponseEntity<PrelacionOficio> findPrelacionOficioByPrelacionId(@PathVariable("prelacionId") Long prelacionId) {
    	PrelacionOficio oficio=prelacionService.getPrelacionOficio(prelacionId);
    	if(oficio !=null){
    		return new ResponseEntity<>(oficio, HttpStatus.OK);
    	}else{
    		 return new ResponseEntity<>(oficio, HttpStatus.NOT_FOUND);
    	}

    }
    @PostMapping("/oficioEnd")
	public ResponseEntity<PrelacionOficio> finalizarPrelacionOficio(@RequestBody PrelacionOficio prelacionOfi) throws URISyntaxException {

		 prelacionOfi = prelacionService.endPrelacionOficio(prelacionOfi);
		return new ResponseEntity<>(prelacionOfi, HttpStatus.OK);
	}

	@GetMapping(value = "/findPrelacionPredio/{idPrelacion}")
	public ResponseEntity<List<PrelacionPredio>> findPrelacionPredio(@PathVariable("idPrelacion") Long idPrelacion) {
		return new ResponseEntity<>(prelacionService.findPrelacionPredio(idPrelacion), HttpStatus.OK);
	}
	
	@GetMapping(value = "/findPrelPredio/{idPrelacion}")
	public ResponseEntity<List<PrelacionPredio>> findPrelPredio(@PathVariable("idPrelacion") Long idPrelacion) {
		return new ResponseEntity<>(prelacionService.findPrelPredio(idPrelacion), HttpStatus.OK);
	}
	
	@GetMapping(value = "/turnarAAnalista/{idPrelacion}")
	public ResponseEntity<String> turnarAAnalista(@PathVariable("idPrelacion") Long idPrelacion){
		return new ResponseEntity<>(prelacionService.turnarAAnalista(idPrelacion), HttpStatus.OK);
	}


	@PostMapping(value = "/cita")
	public ResponseEntity<Cita> buscarCita(@RequestBody Cita cita) throws URISyntaxException {
		if(citaService.findDiasInhabilesByDate(cita)){
	    return new ResponseEntity<>(cita, HttpStatus.GONE);
	  }
	  List<Cita> listCita = citaService.buscarCitaByFechaAndOficina(cita);
	  if (listCita.size() > 0) {
	    Cita oldCita=listCita.get(0);
	    if(!oldCita.getId().equals(cita.getId())){
	      return new ResponseEntity<>(cita, HttpStatus.GONE);
	    }
	  }
	  Cita newCita = citaService.saveAndUpdate(cita);
	  
	  return new ResponseEntity<>(newCita, HttpStatus.OK);
	}

	@PostMapping("/personadefault")
	public ResponseEntity<?> definirUsuarioDefault(@RequestBody PrelacionVO pre) throws URISyntaxException {

		if (pre == null)
			return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);

		PrelacionUsuarioDef prelacion = prelacionService.setUsuarioDefault(pre);
		return new ResponseEntity<>(prelacion, HttpStatus.OK);

	}

	@PutMapping("/updatePersonaDefault/{prelacionUsuarioDefId}")
	public ResponseEntity<?> updateUsuarioDefault(@RequestBody PrelacionVO pre, @PathVariable Long prelacionUsuarioDefId) {

		if (pre == null)
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

		PrelacionUsuarioDef prelacion = prelacionService.updateUsuarioDefault(pre, prelacionUsuarioDefId);
		return new ResponseEntity<>(prelacion, HttpStatus.OK);
	}
	
	@GetMapping("/getPersonadefault/{idPrelacion}")
	public ResponseEntity<?> GetUsuarioDefault(@PathVariable long idPrelacion) throws URISyntaxException {
		PrelacionUsuarioDef prelacion = prelacionService.getUsuarioDefault(idPrelacion);
		return new ResponseEntity<>(prelacion, HttpStatus.OK);

	}


	@PostMapping(value = "/eviarCorreo/{oficina}/{prelacion}")
	public ResponseEntity<Boolean> enviarOficio(@PathVariable long oficina, @PathVariable long prelacion,@RequestBody List<DireccionArea> direcciones)throws URISyntaxException  {

		return new ResponseEntity<>(prelacionService.enviarCorreo(direcciones,oficina,prelacion), HttpStatus.OK);
	}


	
	@GetMapping("/findPrelacionAnte/{idPrelacion}")
	public ResponseEntity<List<PrelacionAnte>> findPrelacionAnte(@PathVariable Long idPrelacion){
		log.debug("Buscando prelacion ante para la prelacion con id : {}",idPrelacion);
		return new ResponseEntity<>(prelacionAnteService.findPrelacionAnteByPrelacion(idPrelacion), HttpStatus.OK);
	}


	@GetMapping("/rechazados")
	public 	ResponseEntity<?> getAllPrelacionesRechazados( Pageable pageable,
															 Long folio,
															 String escritura,
															 Long solicitante,
															 Long acto,
															 Long usuarioAnalista,
															 Long usuarioCalificador,
															 Long usuarioNotario,
															 String rFechaIninicial,
															 String rFechafin ) {


		//Page<Prelacion> page = prelacionService.findAllByStatusAndUsuario(folio, fechaIngreso, fechaEnvioFirma, fechaBaja, solicitante, pageable, status, tipoUsuarioSeleccionado );
		//if (page == null)
		//	return new ResponseEntity<>((Page<Prelacion>) null, HttpStatus.NOT_FOUND);
		//return new ResponseEntity<>(page, HttpStatus.OK);
		return null;
	}


	@PutMapping("/oficina/{prelacionId}/{oficinaId}")
	public ResponseEntity<Prelacion> updateOficina(@PathVariable Long prelacionId, @PathVariable Long oficinaId) throws URISyntaxException {

		Prelacion result=null;
		try {
			result = prelacionService.updateOficina(prelacionId, oficinaId);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/getCita/{idPrelacion}")
	public ResponseEntity<Cita> findCitaByPrelacion(@PathVariable Long idPrelacion){
		
		return new ResponseEntity<>(citaService.findCitaByPrelacion(idPrelacion), HttpStatus.OK);
	}

	@GetMapping("/getUrl/{idPrelacion}/{idActo}")
	public ResponseEntity<byte[]> getPrelacionUrl(@PathVariable  Long idPrelacion  ,@PathVariable  Long idActo){

		Acto acto = actoService.findOne(idActo);
		byte[] archivobyte=null;
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> responseEntity  =new ResponseEntity<>(archivobyte,headers,HttpStatus.NOT_FOUND);

		if(acto.getTipoActo().getEsTestamento()==false){		  
	      
	       archivobyte=prelacionService.generarPDF(idPrelacion);	        
	        if (archivobyte == null )
				return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);	               
			try {
				Path path = Paths.get(Constantes.IMG_PATH+"oficio-"+idPrelacion+".pdf");
				archivobyte = Files.readAllBytes(path);
			} catch (IOException e) {				
				
				responseEntity = new ResponseEntity<>(archivobyte, headers, HttpStatus.NOT_FOUND);
				return responseEntity;
			}	        
	       
	    }else{
	    	try{
	    		//archivobyte=prelacionService.generarPdfActaRevocacionTestamento();
	    		
	    		if(acto.getTipoActo().getClave()=="DEPTEST"){//Deposito de testamento
	    			System.out.println("DEPTEST:::::::::");
	    			archivobyte=prelacionService.generPdfActaDepositoTestamento();
	    		}
	    		if(acto.getTipoActo().getClave()=="REVTEST"){//Revocacion de testamento
	    			System.out.println("REVTEST:::::::::");
	    			archivobyte=prelacionService.generarPdfActaRevocacionTestamento();
	    		}
	    		if(acto.getTipoActo().getClave()=="ACTTEST"){//Revocacion de testamento
	    			System.out.println("ACTTEST:::::::::");
	    			archivobyte= prelacionService.generarPdfActaRetiroTestamento();
	    		}
	    		
	    		if(archivobyte ==null){
	    			System.out.println("ESTA VACIO:::::::::"+archivobyte);
	    			return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
	    		}	
	    	}catch(JRException ex){
	    		ex.printStackTrace();
	    		responseEntity = new ResponseEntity<>(archivobyte, headers, HttpStatus.NOT_FOUND);
				return responseEntity;
	    	}
	    		    		
	    }
	        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	        headers.setContentType(contentFile("application/pdf"));	        
	        headers.setContentDispositionFormData("attachment", "oficio-"+idPrelacion+".pdf");
	        responseEntity = new ResponseEntity<>(archivobyte, headers, HttpStatus.OK);

	        return responseEntity;
	    
		
	}
	 private MediaType contentFile(String mediaStr)
	 {  
	    	MediaType media;
	    	  switch (mediaStr) {
	            case "image/jpeg":  media = MediaType.IMAGE_JPEG;
	                      break;
	            case "application/pdf":  media =  MediaType.APPLICATION_PDF;
	                     break;
	            case "image/png":  media =  MediaType.IMAGE_PNG;
	                     break;
	            default: media = MediaType.APPLICATION_OCTET_STREAM;
	                     break;
	        }
	    	
	    	return media; 
	 }

	@PutMapping("/adelantar")
	public ResponseEntity<?> adelantarPrelacion(@RequestBody ModificarPrelacionVO[] objeto) throws URISyntaxException {

		Prelacion result=null;
		try {
			if (objeto != null && objeto.length > 0)
				for (Integer index = 0; index < objeto.length ; index++) {
					result = prelacionService.adelantarPrelacion(objeto[index]);
				}
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>(err.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PutMapping("/returnar")
	public ResponseEntity<?> returnarPrelacion(@RequestBody ModificarPrelacionVO[] objeto) throws URISyntaxException {
		log.debug(String.valueOf(objeto.length));
		List<Prelacion> result = new ArrayList<>();
		try {
			if (objeto != null && objeto.length > 0)
				for (Integer index = 0; index < objeto.length ; index++) {
					if (objeto[index].getRegistradorId().equals(-1L)) {
						result.add(prelacionService.returnarAnalistaVirtual(objeto[index]));
					} else {
						result.add(prelacionService.returnarPrelacion(objeto[index]));
					}	
				}
		}
		catch (Exception err) {
			System.out.println(err.getMessage());

		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PutMapping("/restaurar-especial")
	public ResponseEntity<?> restaurarFromEspecial(@RequestBody Long[] objeto) throws URISyntaxException {
		
		Prelacion result=null;
		try {
			if (objeto != null && objeto.length > 0)
				for (Integer index = 0; index < objeto.length ; index++) {
					log.debug(objeto[index].toString());
					result = prelacionService.restaurarFromEspecial(objeto[index]);
				}
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/findAllByStatus/{status}")
	public ResponseEntity<List<Prelacion>> findAllByStatus(@PathVariable("status") List<Long> status) {
	
		List<Prelacion> prelaciones = prelacionService.findAllByStatus(status);
		return new ResponseEntity<>(prelaciones, HttpStatus.OK);
	}
	
	//TODO : Se cambio de metodo de filtado 
	@GetMapping("/findAllByConsecutivoAndStatus/{consecutivo}/{status}")
	public ResponseEntity<List<Prelacion>> findAllByConsecutivo(@PathVariable("consecutivo") int consecutivo,@PathVariable("status") Long status) {
		List<Prelacion> prelaciones = prelacionService.findAllByConsecutivoAndStatus(consecutivo,status);
		return new ResponseEntity<>(prelaciones, HttpStatus.OK);
	}
	
	@GetMapping("/update-entrega/{id}")
	public ResponseEntity<Prelacion> realizaEntrega(@PathVariable("id") Long prelacionId) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionId);
			Prelacion result=null;
		try {
			 Prelacion prelacion =  this.prelacionService.findOne(prelacionId);
			 if(prelacion.getStatus().getId().equals(Constantes.Status.DEVOLUCION_DOCUMENTO_AUTORIZADO.getIdStatus())){
				 solicitudDevolucionService.entragaSolicitudDevolucion(prelacionId);
			 }else {
				 result = prelacionService.updatePrelacionEstado(prelacionId, Constantes.Status.ENTREGADO_AL_USUARIO,null,null,null);
			 }
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);
		}
	
	@GetMapping (value ="/servicio-detalle-prelacion/{consecutivo}/{anio}") 
	public ResponseEntity<Prelacion> findByConsecutivo(@PathVariable ("consecutivo")int consecutivo,@PathVariable ("anio")int anio){
       return ResponseEntity.ok(prelacionService.findByConsecutivo(consecutivo,anio));
	  }

	@GetMapping (value ="/servicio-detalle-prelacion-referencia-bancaria/{referencia}/{anio}")
	public ResponseEntity<Prelacion> findByReferenciaBancaria(@PathVariable ("referencia")String referencia,@PathVariable ("anio")int anio){
		return ResponseEntity.ok(prelacionService.findByReferenciaBancaria(referencia, anio));
	}
	
	
	@GetMapping (value ="/prelacion-for-devolucion/{consecutivo}/{anio}") 
	public ResponseEntity<?> findByConsecutivoDevolucion(@PathVariable ("consecutivo")int consecutivo,@PathVariable ("anio")int anio){
		Prelacion prelacion = prelacionService.findByConsecutivo(consecutivo,anio);
		if(prelacion!=null) {
			 if(prelacion.getStatus().getId().equals(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()))
				 return new ResponseEntity<>("La entrada ya fue tramitada y entregada al usuario.",HttpStatus.NOT_FOUND);
			 if(prelacion.getStatus().getId().equals(Constantes.Status.PENDIENTE_DEVOLUCION_DOCUMENTO.getIdStatus()))
				 return new ResponseEntity<>("La entrada tiene una solicitud de devolución en proceso.",HttpStatus.NOT_FOUND);
			 if(prelacion.getStatus().getId().equals(Constantes.Status.DEVOLUCION_DOCUMENTO_AUTORIZADO.getIdStatus()))
				 return new ResponseEntity<>("La entrada tiene una solicitud de devolución autorizada.",HttpStatus.NOT_FOUND);
			 if(prelacion.getStatus().getId().equals(Constantes.Status.DEVOLUCION_DOCUMENTO_ENTREGADO.getIdStatus()))
				 return new ResponseEntity<>("La entrada tiene una solicitud de devolución autorizada y entregada.",HttpStatus.NOT_FOUND);
			 
			  return  new ResponseEntity<>(prelacion,HttpStatus.OK);
		}else { 
			return new ResponseEntity<>("No identificamos entrada con la información propocionada, valide e intente de nuevo",HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping(value = "/bitacora-prelacion")
	public ResponseEntity<Bitacora> findBitacoraByprelacion(@RequestBody Prelacion prelacion) {
		return ResponseEntity.ok(bitacoraService.findBitacoraByprelacion(prelacion));
	}

	@PostMapping(value = "/bitacora-reingreso")
	public ResponseEntity<List<BitacoraReingreso>> findBitacoraReingresoByprelacion(@RequestBody Prelacion prelacion) {
		log.info("bitacora-reingreso.PrelacionId:"+prelacion.getId());
		return ResponseEntity.ok(bitacoraReingresoService.findBitacoraReingresoByprelacion(prelacion));
	}

	
	@GetMapping (value ="/findAllByNotario/{usuario}/{status}/{fechaIni}/{fechaFin}") 
	public ResponseEntity<List<Prelacion>> findAllNotario(@PathVariable ("usuario")Long idUsuario,@PathVariable ("status")Long status,@PathVariable ("fechaIni")String fechaIni,@PathVariable ("fechaFin")String fechaFin){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateIni = null ;
		Date dateFin = null;
		System.out.println("Inicial : " + fechaIni);
		System.out.println("Final : " + fechaFin);
		try {
			dateIni = format.parse(fechaIni);
			dateFin = format.parse(fechaFin);
		} catch (ParseException e) {
		}
		 
		
		return ResponseEntity.ok(prelacionService.findAllUsuarioAndRangoFechasAndStatus(idUsuario, status, dateIni, dateFin));
	  }
	
	@GetMapping (value ="/findAllStatus/")
	public ResponseEntity<List<StatusExterno>> findAllStatus(){
	    return ResponseEntity.ok(prelacionService.findAllStatus());
	}
	
	@GetMapping (value ="/prelacion-acto-documento/{idPrelacion}") 
	public ResponseEntity<Long> getDocumentoByPrelacion(@PathVariable ("idPrelacion")Long idPrelacion){
       return ResponseEntity.ok(prelacionService.getDocumentoByPrelacion(idPrelacion));
	  }
	
	

	@GetMapping (value ="/{idPrelacion}/resultado-busquedas") 
	public ResponseEntity<List<PredioAnteVO>> getBusquedasPrelacionAnteByPrelacion(@PathVariable ("idPrelacion")Long idPrelacion){
       	return ResponseEntity.ok(busquedaService.listPredioAnteFromBusqueda(idPrelacion));
	}

	@GetMapping (value ="/{idPrelacion}/resultado-pj-busquedas") 
	public ResponseEntity<List<PersonaJuridicaAnteVO>> getBusquedasPersonaJuridicaByPrelacion(@PathVariable ("idPrelacion")Long idPrelacion){
       	return ResponseEntity.ok(busquedaService.listPersonaJuridicaFromBusqueda(idPrelacion));
	}

	@GetMapping(value = "/findPrelacionFechas/{prelacionId}")
	public ResponseEntity<ConsultaPrelacionDetalleTramiteVO> findPrelacionFechas(@PathVariable("prelacionId") Long prelacionId) {
		return ResponseEntity.ok(prelacionService.findPrelacionFechas(prelacionId));
	}

	@GetMapping(value = "/findPrelacionAtendio/{prelacionId}")
	public ResponseEntity<ConsultaPrelacionDetalleAtendioVO> findPrelacionAtendio(@PathVariable("prelacionId") Long prelacionId) {
		return ResponseEntity.ok(prelacionService.findPrelacionAtendio(prelacionId));
	}

	@GetMapping(value = "/findPrelacionFolios/{prelacionId}")
	public ResponseEntity<ConsultaPrelacionDetalleFoliosVO> findPrelacionFolios(@PathVariable("prelacionId") Long prelacionId) {
		return ResponseEntity.ok(prelacionService.findPrelacionFolios(prelacionId,false));
	}

	@GetMapping(value = "/findPrelacionFolios/{prelacionId}/{idReingreso}")
	public ResponseEntity<ConsultaPrelacionDetalleFoliosVO> findPrelacionFolios(@PathVariable("prelacionId") Long prelacionId,@PathVariable("idReingreso") Boolean isReingreso) {
		return ResponseEntity.ok(prelacionService.findPrelacionFolios(prelacionId,isReingreso));
	}
	
	@GetMapping(value = "/findPrelacionPago/{prelacionId}")
	public ResponseEntity<List<ConsultaPrelacionDetallePagoVO>> findPrelacionPago(@PathVariable("prelacionId") Long prelacionId) {
		return ResponseEntity.ok(prelacionService.findPrelacionPago(prelacionId));
	}

	@GetMapping (value = "/{prelacionId}/blocking-prelaciones") 
	public ResponseEntity<List<Prelacion>> isPrelacionAnalizable(@PathVariable("prelacionId") Long prelacionId){
		return new ResponseEntity<List<Prelacion>>(this.prelacionService.isPrelacionAnalizable(this.prelacionService.findOne(prelacionId)), HttpStatus.OK);
	}
	
	@GetMapping (value = "/{prelacionId}/blocking-prelaciones-ante") 
	public ResponseEntity<List<Prelacion>> isPrelacionCapturableAnte(@PathVariable("prelacionId") Long prelacionId){
		return new ResponseEntity<List<Prelacion>>(this.prelacionService.isPrelacionCapturableAnte(this.prelacionService.findOne(prelacionId)), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{folio}/blocking-folio/{tipoFolio}")
	public ResponseEntity<List<Prelacion>> isFolioAsociable(@PathVariable("folio") Integer folio,
			@PathVariable("tipoFolio") Integer tipoFolio) {
		return new ResponseEntity<List<Prelacion>>(this.prelacionService.isFolioOcupadoPrelacion(folio, tipoFolio),
				HttpStatus.OK);
	}

	@GetMapping (value = "/{prelacionId}?isSigned")
	public ResponseEntity<Boolean> isPrelacionFirmado(@PathVariable("prelacionId") Long prelacionId){
		return new ResponseEntity<Boolean>(this.prelacionService.isPrelacionFirmado(this.prelacionService.findOne(prelacionId)), HttpStatus.OK);
	}

	@PutMapping("/{prelacionId}/primer-registro")
	public ResponseEntity<?> actualizaPrimerRegistro(@PathVariable ("prelacionId") Long prelacionId, @RequestBody Boolean pr) throws URISyntaxException {

		try {
			Prelacion prelacion = prelacionService.findOne(prelacionId);
			if (prelacion != null)
				prelacionService.actualizarPrimerRegistro (prelacion, pr);
			else
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@PutMapping("/{prelacionId}/anterior-abril81")
	public ResponseEntity<?> actualizaAnteriorAbril(@PathVariable ("prelacionId") Long prelacionId, @RequestBody Boolean aa81) throws URISyntaxException {

		try {
			Prelacion prelacion = prelacionService.findOne(prelacionId);
			if (prelacion != null)
				prelacionService.actualizarAnteriorAbril (prelacion, aa81);
			else
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@GetMapping (value = "/findPrelacionVoByPrelacionId/{prelacionId}/{actoId}")
	public ResponseEntity<PrelacionVO> findPrelacionVo(@PathVariable ("prelacionId") Long prelacionId, @PathVariable ("actoId") Long actoId ){
		return ResponseEntity.ok(prelacionService.findPrelacionVo(prelacionId,actoId));
	}
	
	//VALIDA REINGRESO
	@GetMapping (value = "/valida-reingreso/{prelacionId}")
	public ResponseEntity<?> validaReingreso(@PathVariable ("prelacionId") Long prelacionId)
	{
		try
		{
			return new ResponseEntity<>(prelacionService.validaReingreso(prelacionId),HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value="/reingreso")
	public ResponseEntity<?> reingreso(@RequestBody ReingresoVO reingreso)
	{
		try
		{
			return new ResponseEntity<>(prelacionService.reingreso(reingreso),HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
		
	@GetMapping(value = "/findPrelacionReingresos/{prelacionId}")
	public ResponseEntity<?> findPrelacionReingresos(@PathVariable Long prelacionId){
		return new ResponseEntity<>(bitacoraService.findByPrelacionAndTipoMotivo(prelacionId,tipoMotivoRepository.findOneByNombre("REINGRESO").getId()) ,HttpStatus.OK);
	}
	
	@GetMapping("/dateVisualizacion/{prelacionId}")
	public ResponseEntity<Boolean> findBitacoraImpresion(@PathVariable Long prelacionId) throws ParseException {
		return ResponseEntity.ok(prelacionService.findBitacoraImpresionAndUserLogged(prelacionId));
	}


    @GetMapping("/findPrelBitacoraImpresionVO/{tipoUsuario}")
	public ResponseEntity<List<PrelacionBitacoraImpresionVO>> findPrelBitacoraImpresionVO(@PathVariable int tipoUsuario)  {
		return ResponseEntity.ok(prelacionService.findPrelBitacoraImpresionVO(tipoUsuario));
	}
    
    @GetMapping("/findPrelServ/{idPrel}")
    public ResponseEntity<PrelacionServicio> findPrelacionServicio(@PathVariable Long idPrel){
    	return  ResponseEntity.ok(prelacionService.findByPrelacionId(idPrel));
	}

	@GetMapping(value = "/{prelacionId}/certificado")
	public ResponseEntity<?> isCertificado(@PathVariable("prelacionId") Long prelacionId) {
		return new ResponseEntity<Certificado>(this.prelacionService.tipoCertificado(prelacionId), HttpStatus.OK);
	}

	
	@GetMapping( value = "/findFormulario/{oficina}/consecutivo/{consecutivo}/fechaEnvioFirma/{fechaEnvioFirma}/firma/{firma}")
	//@GetMapping( value = "/findFormulario/oficina/{oficinaId}/consecutivo/{consecutivo}")
	public ResponseEntity<List<Prelacion>> findFormulario(@PathVariable ("oficina") Long oficinaId, 
			@PathVariable ("consecutivo") Integer consecutivo, 
			@PathVariable ("fechaEnvioFirma") String fechaEnvioFirma,
			@PathVariable ("firma") String firma){
			return new ResponseEntity<List<Prelacion>>(this.prelacionService.findFormValida(oficinaId,consecutivo,fechaEnvioFirma,firma), HttpStatus.OK);
		}

		@GetMapping( value = "/findPrelacionByPredioProcedeDe/{noFolio}")
		public ResponseEntity<List<PrelacionPredio>> findPrelacionByPredioProcedeDe(@PathVariable ("noFolio") Long noFolio){
				return new ResponseEntity<List<PrelacionPredio>>(this.prelacionService.findPrelacionByPredioProcedeDe(noFolio), HttpStatus.OK);
			}

	

	//JADV-SUSPENSION
	@RequestMapping(value = "/file/{id}", method = RequestMethod.GET )
    public ResponseEntity<byte[]> getPdfBoletaSuspencion(@PathVariable Long id) {

    	
    	return null;
    }
    
	@GetMapping("/findFechaInscripcion/{id}")
	public ResponseEntity<Date> findFechaInscripcion(@PathVariable Long id) {
		return new ResponseEntity<>(prelacionService.buscaFechaInscripcion(id), HttpStatus.OK);
	}

	@PutMapping("/indEntrega/{prelacionId}/{indEntrega}")
	public ResponseEntity<?> actualizaIndEntrega(@PathVariable ("prelacionId") Long prelacionId, @PathVariable ("indEntrega") Boolean indEntrega) throws URISyntaxException {

		try {
			Prelacion prelacion = prelacionService.findOne(prelacionId);
			if (prelacion != null)
				prelacionService.actualizarIndEntrega (prelacion, indEntrega);
			else
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PutMapping("/clonePrelacionIngreso")
	public ResponseEntity<PrelacionVO> clonePrelacionIngreso(@RequestBody PrelacionVO prelacionIngreso){
		log.debug("clonar prelacion : {}", prelacionIngreso.getId());
		PrelacionVO result = prelacionService.clonePrelacionIngreso(prelacionIngreso);		
		log.debug("nueva prelacion : {}", result.getId());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/findAllByOficinaIdAndConsecutivoAndAnioAndSubindice/{oficinaId}/{consecutivo}/{anio}/{clave}")
	public ResponseEntity<?> findAllByOficinaIdAndConsecutivoAndAnioAndSubindice(@PathVariable Long oficinaId, 
			@PathVariable Integer consecutivo, @PathVariable Integer anio, @PathVariable String clave){
		PublicVO valor= new PublicVO();
		//List<Prelacion> result = prelacionService.findAllByOficinaIdAndConsecutivoAndAnio(oficinaId, consecutivo, anio);
		List<Prelacion> result = prelacionService.findAllByOficinaIdAndConsecutivoAndAnioAndClave(oficinaId, consecutivo, anio,clave);
		
		if (result.size()==0) {
			return new ResponseEntity<>("No se encontro la entrada", HttpStatus.NOT_FOUND);
		} else  if (result.size()>1) {
			return new ResponseEntity<>("Hay mas de una entrada con los datos seleccionados", HttpStatus.NOT_FOUND);
		} else {
			valor = prelacionService.datosPublic(result.get(0));
			//mensaje=result.get(0).getStatus().getStatusExterno().getNombre() + (result.get(0).getResultado()!=null? " - " + result.get(0).getResultado().getNombre():"");
		}	
		return new ResponseEntity<>(valor, HttpStatus.OK);
	}
	/* Función que obtiene la información del actos, tipo de actos y usuario solicitante
	 *  27/02/2024
	 *  JLCI
	*/
	@GetMapping("/servicio-aux-prelacion/{prelacionId}")
	public ResponseEntity<PrelacionIngresoVO>  getBoletaIngresoAux(@PathVariable Long prelacionId) {
		PrelacionIngresoVO prelacion = prelacionService.findOneVOv2(prelacionId);
		if(prelacion!=null)
			return new ResponseEntity<PrelacionIngresoVO>(prelacion, HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findAllByOficinaIdAndConsecutivoAndAnioAndSubindice/{oficinaId}/{consecutivo}/{anio}/{clave}/{subindice}")
	public ResponseEntity<?> findAllByOficinaIdAndConsecutivoAndAnioAndSubindice(@PathVariable Long oficinaId, 
			@PathVariable Integer consecutivo, @PathVariable Integer anio, @PathVariable String clave, @PathVariable Long subindice){
		PublicVO valor= new PublicVO();
		//List<Prelacion> result = prelacionService.findAllByOficinaIdAndConsecutivoAndAnioAndSubindice(oficinaId, consecutivo, anio, subindice);
		List<Prelacion> result = prelacionService.findAllByOficinaIdAndConsecutivoAndAnioAndSubindiceAndClave(oficinaId, consecutivo, anio,subindice,clave);
		
		if (result.size()==0) {
			return new ResponseEntity<>("No se encontro la entrada", HttpStatus.NOT_FOUND);
		} else  if (result.size()>1) {
			return new ResponseEntity<>("Hay mas de una entrada con los datos seleccionados", HttpStatus.NOT_FOUND);
		} else {
			valor = prelacionService.datosPublic(result.get(0));
			//mensaje=result.get(0).getStatus().getStatusExterno().getNombre() + (result.get(0).getResultado()!=null? " - " + result.get(0).getResultado().getNombre():"");
		}	
		return new ResponseEntity<>(valor, HttpStatus.OK);
	}
	/*
	@GetMapping("/findPrelacionIdlByOficinaIdAndConsecutivoAndAnioAndSubindice/{oficinaId}/{consecutivo}/{anio}")
	public ResponseEntity<Long> findPrelacionIdlByOficinaIdAndConsecutivoAndAnioAndSubindice(@PathVariable Long oficinaId, @PathVariable Integer consecutivo, @PathVariable Integer anio){
		Long prelacionId  = null;
		List<Prelacion> result = prelacionService.findAllByOficinaIdAndConsecutivoAndAnio(oficinaId, consecutivo, anio);
		
		if (result.size()==1) {
			prelacionId =result.get(0).getId();
		}	
		return new ResponseEntity<>(prelacionId, HttpStatus.OK);
	} */
	
	@GetMapping("/findPrelacionIdlByOficinaIdAndConsecutivoAndAnioAndSubindice/{oficinaId}/{consecutivo}/{anio}/{subindice}")
	public ResponseEntity<PrelacionActosVO> findPrelacionIdlByOficinaIdAndConsecutivoAndAnioAndSubindice(@PathVariable Long oficinaId, @PathVariable Integer consecutivo, @PathVariable Integer anio, @PathVariable Long subindice){
		PrelacionActosVO result = prelacionService.findOneByOficinaIdAndConsecutivoAndAnioAndSubindice(oficinaId, consecutivo, anio, subindice);
		
		if(result!=null)
			return new ResponseEntity<PrelacionActosVO>(result, HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findDocumentosEntradaByOficinaIdAndConsecutivoAndAnioAndSubindice/{oficinaId}/{consecutivo}/{anio}/{subindice}")
	public ResponseEntity<PrelacionDocumentosVO> findDocumentosEntradaByOficinaIdAndConsecutivoAndAnioAndSubindice(@PathVariable Long oficinaId, @PathVariable Integer consecutivo, @PathVariable Integer anio, @PathVariable Long subindice){
		log.info("IHM-PrelacionRestController.findDocumentosEntradaByOficinaIdAndConsecutivoAndAnioAndSubindice:");
		PrelacionDocumentosVO result = prelacionService.findDocumentosEntradaByOficinaIdAndConsecutivoAndAnioAndSubindice(oficinaId, consecutivo, anio, subindice);
		
		if(result!=null)
			return new ResponseEntity<PrelacionDocumentosVO>(result, HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@PutMapping("/findPrelacionActobyFolio/{folioBuscar}")
	public ResponseEntity<List<PrelacionActosVO>> findPrelacionActobyFolio(@RequestBody BuscarFolioVO folioBuscar){

		List<PrelacionActosVO> lista = null;
		lista = prelacionService.findPrelacionActosByFolio(folioBuscar);
		
		if(lista!=null)
			return new ResponseEntity<List<PrelacionActosVO>>(lista, HttpStatus.OK);
		else
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/findByEntrada/{entrada}/anio/{anio}/subIndice/{subIndice}/prelacionActual/{prelacionActual}")
	public ResponseEntity <Prelacion> findByEntradaAnioSubIndiceAndOficina(@PathVariable Integer entrada, @PathVariable String anio, @PathVariable Long subIndice, @PathVariable Long prelacionActual ) {
		return new ResponseEntity<>(prelacionService.findByEntradaAnioSubIndice(entrada,anio,subIndice, prelacionActual), HttpStatus.OK);
	}
	@GetMapping("/findByEntrada/{entrada}/anio/{anio}/subIndice/{subIndice}/prelacionActual/{prelacionActual}/oficina/{oficinaId}")
	public ResponseEntity <Prelacion> findByEntradaAnioSubIndiceByOficina(@PathVariable Integer entrada, @PathVariable String anio, @PathVariable Long subIndice, @PathVariable Long prelacionActual, @PathVariable Long oficinaId) {
		return new ResponseEntity<>(prelacionService.findByEntradaAnioSubIndiceByOficina(entrada,anio,subIndice, prelacionActual,oficinaId), HttpStatus.OK);
	}
	
	@GetMapping("/findByFolio/{folio}/folioRealAnterior/{folioRealAnterior}/auxiliar/{auxiliar}/tipoFolio/{tipoFolio}")
	public ResponseEntity <?> findByFolioAndOficina(@PathVariable Integer folio, @PathVariable Integer folioRealAnterior,@PathVariable Integer auxiliar,@PathVariable Integer tipoFolio) {		Predio predio = new Predio();
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		if(folioRealAnterior > 0 && auxiliar > 0) 
		{
			return new ResponseEntity<>(predioService.findPredioByNoFolioRealAndAuxiliarAndOficina(folioRealAnterior,auxiliar, usuario.getOficina()), HttpStatus.OK);
			
		}else if(auxiliar <= 0 && folioRealAnterior >  0 ){
			if(tipoFolio== Constantes.ETipoFolio.PREDIO.getTipoFolio()) {
				return new ResponseEntity<>(predioService.findByNoFolioRealAndOficina(folioRealAnterior, usuario.getOficina()),HttpStatus.OK);
			}else if(tipoFolio== Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio()) {
				return new ResponseEntity<>(personaJuridicaService.findByNumeroFolioRealAndOficinaId(folioRealAnterior, usuario.getOficina().getId()),HttpStatus.OK);
			}
		}else {
			if(tipoFolio== Constantes.ETipoFolio.PREDIO.getTipoFolio()) {
				 return new ResponseEntity<>(predioService.findByNoFolioAndOficina(folio, usuario.getOficina()),HttpStatus.OK);
			}else if(tipoFolio== Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio()) {
			     return new ResponseEntity<>(personaJuridicaService.findOneByNumFolio(folio, usuario.getOficina().getId()),HttpStatus.OK);
			}
		}
	
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		
	}
	
	@GetMapping("/findByFolio/{folio}/folioRealAnterior/{folioRealAnterior}/auxiliar/{auxiliar}/oficinaId/{oficinaId}/tipoFolio/{tipoFolio}")
	public ResponseEntity <?> findByFolioAndOficinaId(@PathVariable Integer folio, @PathVariable Integer folioRealAnterior,@PathVariable Integer auxiliar,@PathVariable Long oficinaId,@PathVariable Integer tipoFolio) {	
		Predio predio = new Predio();
		Oficina oficina= oficinaService.findById(oficinaId);
		if(oficina!=null){
			if(folioRealAnterior > 0 && auxiliar > 0) 
			{
				return new ResponseEntity<>(predioService.findPredioByNoFolioRealAndAuxiliarAndOficina(folioRealAnterior,auxiliar, oficina), HttpStatus.OK);
				
			}else if(auxiliar <= 0 && folioRealAnterior >  0 ){
				if(tipoFolio== Constantes.ETipoFolio.PREDIO.getTipoFolio()) {
					return new ResponseEntity<>(predioService.findByNoFolioRealAndOficina(folioRealAnterior, oficina),HttpStatus.OK);
				}else if(tipoFolio== Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio()) {
					return new ResponseEntity<>(personaJuridicaService.findByNumeroFolioRealAndOficinaId(folioRealAnterior, oficina.getId()),HttpStatus.OK);
				}
			}else {
				if(tipoFolio== Constantes.ETipoFolio.PREDIO.getTipoFolio()) {
					 return new ResponseEntity<>(predioService.findByNoFolioAndOficina(folio, oficina),HttpStatus.OK);
				}else if(tipoFolio== Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio()) {
				     return new ResponseEntity<>(personaJuridicaService.findOneByNumFolio(folio, oficina.getId()),HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<>(predio, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/find-actos-copia/{predioId}/{busquedaResultadoId}/{tipoFolio}")
	public ResponseEntity<List<PrelacionCopiaCertificadaFolioVO>> findActosForCopiaByPredio(@PathVariable("predioId") Long predioId,@PathVariable("busquedaResultadoId") Long busquedaReultadoId,@PathVariable("tipoFolio") Integer tipoFolio){
			return new ResponseEntity<>(prelacionService.findByFolio(predioId,busquedaReultadoId,tipoFolio), HttpStatus.OK);
	}
	
	@GetMapping("/save-copia-folio/{predioId}/{prelacionId}/{tipoFolio}")
	public ResponseEntity<BusquedaResultado> saveBusquedaFolio(@PathVariable("predioId") Long predioId,@PathVariable("prelacionId") Long prelacionId,@PathVariable("tipoFolio") Integer tipoFolio){
		return new ResponseEntity<>(busquedaResultadoService.savePredioCopia(prelacionId,predioId,tipoFolio),HttpStatus.OK);
	}
	
	
	@GetMapping(value = "getObtenTotalHojasOfEntrada/{prelacionId}")
	public ResponseEntity <Integer> getObtencionTotalHojas(@PathVariable("prelacionId") Long  prelacionId ) {
		try {
		return new ResponseEntity<>(1,HttpStatus.OK);
		}catch(Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/getImagePathsFromArchivoOfEntrada/{prelacionId}")
	public ResponseEntity<List<String>> getImagePathsByEntrada(@PathVariable("prelacionId") Long  prelacionId){
		try {
		return new ResponseEntity<>(prelacionService.getPathOfImagesOfArchivoByEntrada(prelacionId),HttpStatus.OK);
		}catch(Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
			}
		
	}
	
	@GetMapping("/getImagePathsFromAsiento/{actoId}")
	public ResponseEntity<List<String>> getImagePathsFromAsiento(@PathVariable("actoId") Long actoId){
		try {
			return new ResponseEntity<>(prelacionService.getPathOfImagesByActo(actoId),HttpStatus.OK);
	    }catch(Exception e) {
				return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/saveBusquedaResultado/entradaId/{entradaId}/prelacionActual/{prelacionActual}/folioRealAnterior/{folioRealAnterior}/auxiliar/{auxiliar}/folio/{folio}")
	public ResponseEntity<BusquedaResultado> saveBusquedaResultado(@PathVariable ("entradaId") Long entradaId, @PathVariable ("prelacionActual") Long prelacionActual,@PathVariable ("folioRealAnterior") Integer folioRealAnterior,@PathVariable ("auxiliar") Integer auxiliar, @PathVariable ("folio") Integer folio) {
		BusquedaResultado br = new BusquedaResultado();
		Predio predio = new Predio();
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		if(folioRealAnterior>0 &&auxiliar>0) {
			
			predio = predioService.findPredioByNoFolioRealAndAuxiliarAndOficina(folioRealAnterior,auxiliar, usuario.getOficina());
		
		}else if(auxiliar<=0&&folioRealAnterior>0 ){

			predio = predioService.findByNoFolioRealAndOficina(folioRealAnterior, usuario.getOficina());
		
		}else {

			predio = predioService.findByNoFolioAndOficina(folio, usuario.getOficina());
		}
		
		Prelacion p = prelacionService.findOne(entradaId);
		List<BusquedaResultado> brs= busquedaResultadoService.findAllBusquedResultadoByPrelacionIdAndPrelacionHistorica(prelacionActual, entradaId);
		
		if(brs.size()==0) {
		Prelacion pActual= prelacionRepository.findById(prelacionActual);
		br.setPrelacionHistorica(p);
		br.setPrelacion(pActual);
		br.setPredio(predio);
		busquedaResultadoService.guardar(br);
		}
		return new ResponseEntity<>(br, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteBusquedaResultado/{prelacionActualId}/{prelacionHistoricaId}")
	public ResponseEntity<?> deleteBusquedaResultado(@PathVariable("prelacionActualId") Long prelacionActualId, @PathVariable("prelacionHistoricaId") Long prelacionHistoricaId){

		 List<BusquedaResultado> brs= busquedaResultadoService.findAllBusquedResultadoByPrelacionIdAndPrelacionHistorica(prelacionActualId, prelacionHistoricaId);
		   if(brs.size()>0) {
			   for(BusquedaResultado br: brs) {
			   busquedaResultadoRepository.delete(br);
			   }
		   }else {
			   return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		   }
			

			return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
/*	@GetMapping("/findPrelacionServioByPrelacionId/{prelacionId}")
	public ResponseEntity <PrelacionServicio> findPrelacionServioByPrelacionId(@PathVariable Long prelacionId) {
		return new ResponseEntity<>(prelacionService.findPrelacionServioByPrelacionId(prelacionId), HttpStatus.OK);
	}*/
	
	@GetMapping("/findActoPublicitario/prelacionActual/{prelacionActual}/numeroActoPublicitario/{numeroActoPublicitario}/oficina/{idOficina}")
	public ResponseEntity <ActoPublicitario> findActoPublicitario(@PathVariable Long prelacionActual, @PathVariable Integer numeroActoPublicitario, @PathVariable Long idOficina) {
		return new ResponseEntity<>(prelacionService.findActoPublicitario(prelacionActual,numeroActoPublicitario,idOficina), HttpStatus.OK);
	}
	
	@GetMapping("/findActoPublicitario/prelacionActual/{prelacionActual}/numeroActoPublicitario/{numeroActoPublicitario}")
	public ResponseEntity <ActoPublicitario> findActoPublicitario( @PathVariable Long prelacionActual, @PathVariable Integer numeroActoPublicitario) {
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		return new ResponseEntity<>(prelacionService.findActoPublicitario(prelacionActual,numeroActoPublicitario,usuario.getOficina().getId()), HttpStatus.OK);
	}
	
	@GetMapping("/findEntradaByConsecutivoAndAnioAndSubindice/{entrada}/anio/{anio}/subIndice/{subIndice}")
	public ResponseEntity <Prelacion> findEntradaByConsecutivoAndAnioAndSubindice(@PathVariable Integer entrada, @PathVariable String anio, @PathVariable Long subIndice) {
		return new ResponseEntity<>(prelacionService.findEntradaByConsecutivoAndAnioAndSubindice(entrada,anio,subIndice), HttpStatus.OK);
	}
	
	@PostMapping("/tipoActoPrelacion/{actoId}/{tipoActoId}")
	public ResponseEntity<?> changeTipoActoPrelacion(@PathVariable("actoId") Long actoId,
			@PathVariable("tipoActoId") Long tipoActoId, @RequestBody PrelacionVO prelacionIngreso) {

		List<Acto> actos = new ArrayList<Acto>();
		if (prelacionIngreso.getActos() != null) {
			actos= prelacionService.updateTipoActo(prelacionIngreso, tipoActoId, actoId);
		} else {
			return new ResponseEntity<>("Los datos de ingreso para guardar el acto son incorrectos",
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(actos, HttpStatus.OK);
	}

	@GetMapping("/returnDigitalizar/{prelacionId}")
	public ResponseEntity <?> returnDigitalizar(@PathVariable Long prelacionId) {
		Prelacion pre = prelacionService.changePrelacionDigitalizado(prelacionId);
		if (pre != null) {
			return new ResponseEntity<>(pre, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Los datos de ingreso para guardar el acto son incorrectos",
					HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{id}/actos-predio")
	public ResponseEntity<List<ActoPredio>> findActoPredioByPrelacion(@PathVariable("id") Long id) {
		List<ActoPredio> listAP = prelacionService.findAllActoPredioByPrelacion(id);
		if (listAP.size() > 0) {
			return new ResponseEntity<>(listAP, HttpStatus.OK);
		}
		return new ResponseEntity<>((List<ActoPredio>) null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value="/save-acto-copia/{actoId}/{busquedaResultadoId}")
	public ResponseEntity<BusquedaResultadoActo> saveActoCopia(@PathVariable("actoId") Long actoId,@PathVariable("busquedaResultadoId") Long busquedaResultadoId){
		try {
			return new ResponseEntity<>(busquedaResultadoService.saveActo(actoId, busquedaResultadoId),HttpStatus.OK);	
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.CONFLICT);
		}
		
	}
	
	@DeleteMapping(value="/delete-acto-copia/{actoId}/{busquedaResultadoId}")
	public ResponseEntity<Boolean> deleteActoCopia(@PathVariable("actoId") Long actoId,@PathVariable("busquedaResultadoId") Long busquedaResultadoId){
		try {
			return new ResponseEntity<>(busquedaResultadoService.deleteActo(actoId, busquedaResultadoId),HttpStatus.OK);	
		}catch(Exception e) {
			return new ResponseEntity<>(null,HttpStatus.CONFLICT);
		}
		
	}
	
	@GetMapping(value = "/findBitacoraDig/{prelacionId}")
	public ResponseEntity<?> findBitacoraDig(@PathVariable Long prelacionId){
		return new ResponseEntity<>(bitacoraDigitalizadorService.findBitacoraDByprelacion(prelacionId) ,HttpStatus.OK);
	}
	
	@GetMapping("/returnValidador/{prelacionId}")
	public ResponseEntity <?> returnValidador(@PathVariable Long prelacionId) {
		Prelacion pre = prelacionService.changePrelacionValidador(prelacionId);
		if (pre != null) {
			return new ResponseEntity<>(pre, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Los datos de ingreso para guardar el acto son incorrectos",
					HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/get-page-report/{prelacionId}")
	public ResponseEntity<TotalPagesVO> getPagesReport(@PathVariable Long prelacionId){
		 Prelacion pre = prelacionService.findOne(prelacionId);
		 Optional<PrelacionBoleta> prelacionBoleta = prelacionBoletaService.findFirstByPrelacionIdOrderByPaginaDesc(prelacionId);
		 Page<Acto> actos = null;
		 TotalPagesVO total = new TotalPagesVO();
		 if(pre!=null && pre.getStatus().getId()==7L||pre.getStatus().getId()==8L){
			 if(prelacionBoleta.isPresent()) {
				 total.setTotalPages(prelacionBoleta.get().getPagina()+1);
			 }else {
				 actos = actoService.getActosPageReport(prelacionId);
				  total = new TotalPagesVO(actos.getTotalElements(),actos.getTotalPages());
			 }
		 }else {
			 actos = actoService.getActosPageReport(prelacionId);
			  total = new TotalPagesVO(actos.getTotalElements(),actos.getTotalPages());
		 }
		 
		 
		 if(total!=null)
		    return  new ResponseEntity<TotalPagesVO>(total,HttpStatus.OK);
		 else
			 return null;
		
	}
	
	@DeleteMapping(value="/{prelacionId}/deleteBusqueda")
	public ResponseEntity<Boolean> deleteBusqueda(@PathVariable("prelacionId") Long prelacionId){
		return new ResponseEntity<>(busquedaService.deleteBusqueda(prelacionId),HttpStatus.OK);
	}
	
	@PostMapping("/tipoActoPrelacionCLG/{actoId}/{tipoActoId}")
	public ResponseEntity<?> changeTipoActoPrelacionCLG(@PathVariable("actoId") Long actoId,
			@PathVariable("tipoActoId") Long tipoActoId, @RequestBody PrelacionVO prelacionIngreso) {

		List<Acto> actos = new ArrayList<Acto>();
		if (prelacionIngreso.getActos() != null) {
			actos= prelacionService.updateTipoActoCLG(prelacionIngreso, tipoActoId, actoId);
		} else {
			return new ResponseEntity<>("Los datos de ingreso para guardar el acto son incorrectos",
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(actos, HttpStatus.OK);
	}
	
	@PutMapping("/reproceso/{tipoBoleta}")
	public ResponseEntity<Prelacion> reproceso(@RequestBody PrelacionVO prelacionIngreso,@PathVariable("tipoBoleta") Long tipoBoleta) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", prelacionIngreso.getId());
			Prelacion result=null;
		try {
			result = prelacionService.reproceso(prelacionIngreso, tipoBoleta);
			//result = prelacionService.updatePrelacionEstado(prelacionIngreso.getId(), Constantes.Status.ASIGNADO_A_COORDINADOR,null,null);
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping(value = "/findBitacoraReproceso/{prelacionId}")
	public ResponseEntity<?> findBitacoraReproceso(@PathVariable Long prelacionId){
		return new ResponseEntity<>(bitacoraService.findByPrelacionAndReproceso(prelacionId) ,HttpStatus.OK);
	}
	
	@GetMapping(value = "/{folios}/blocking-folios/{tipoFolio}/{oficinaId}/{consecutivo}")
	public ResponseEntity<String> isFoliosAsociable(@PathVariable("folios") String folios,
			@PathVariable("tipoFolio") Integer tipoFolio,@PathVariable("oficinaId") Long oficinaId, @PathVariable("consecutivo") Integer consecutivo) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbOfi = new StringBuilder();
		Boolean folioEnProceso = false;
		String[] ids = folios.split(",");
		for(String folio: ids) {
			List<Prelacion> resultado = this.prelacionService.isFolioOcupadoPrelacion(Integer.parseInt(folio), tipoFolio);
			
			
			
			if(resultado.size()>0) {
				resultado = resultado.stream().filter(x -> x.getConsecutivo() < consecutivo).collect(Collectors.toList());
				for(Prelacion pre: resultado) {
					if(pre.getStatus().getId() == Constantes.Status.EN_PROCESO_ASIGNACION.getIdStatus() ||
							pre.getStatus().getId() == Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus() ||
							pre.getStatus().getId() == Constantes.Status.ASIGNADO_A_COORDINADOR.getIdStatus() ||
							pre.getStatus().getId() == Constantes.Status.SUSPENDIDA_CALIFICADOR.getIdStatus()) {
						folioEnProceso = true;
					}
				}
				
				if(folioEnProceso) {
					sb.append( "La o Las Prelaciones: ");
					for(Prelacion pre: resultado) {
						sb.append( " No. Prelacion: " + pre.getConsecutivo() + ", " );
					}
					sb.append( "Están en proceso y contienen el folio "+ folio +" que intenta agregar.");
					sb.append("<br/>");
				}
			}
			if(this.prelacionService.folioOficina(Integer.parseInt(folio),tipoFolio,oficinaId)) {
				sbOfi.append("EL FOLIO " + folio + " NO PERTENECE A SU OFICINA REGISTRAL.");
				sbOfi.append("<br/>");
			}
		}
		
		if(sbOfi.toString().length()>0) {
			return new ResponseEntity<>(sbOfi.toString(), HttpStatus.BAD_REQUEST);
		}
		if(sb.toString().length()>0) {
			return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
		}
	}
	
	
	
	@GetMapping(value="/valida-pagos/{id}")
	public ResponseEntity<?> validaPagos(@PathVariable("id") Long prelacionId){
		List<Acto> actos =  actoService.findByPrelacionId(prelacionId);
		Long total =  (long)actos.size();
		Long rechazados  = actos.stream().filter(x->x.getStatusActo().getId().equals(Constantes.StatusActo.RECHAZADO.getIdStatusActo())).count();
		if(!total.equals(rechazados.longValue()) )//SI TODOS LOS ACTOS ESTÁN RECHAZADOS VALIDA PAGOS
		{
			List<Recibo> recibos = reciboService.findByPrelacionId(prelacionId);
			if(recibos !=null && recibos.size()>0) {
				List<Recibo> noPagados = recibos.stream().filter(x->x.getNoRecibo() == null || !x.getNoRecibo().equals(1)).collect(Collectors.toList());
				
				if(noPagados!=null && noPagados.size()>0) {
					
					StringBuilder sb =  new StringBuilder("La(s) referencia(s)  ");
					noPagados.forEach(x->{
						sb.append(x.getReferencia()).append(", ");
					});
					sb.append(" No está(n) pagada(s). valide las referencias");
					
					return new ResponseEntity<>(sb.toString(),HttpStatus.CONFLICT);
				}
			}
		} 
		
		return new ResponseEntity<>(true,HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/upkeep-Prelacion")
	public ResponseEntity<?> upkeepPrelacion(@RequestBody Prelacion prelacion) {
		Prelacion prelacionU = this.prelacionService.upkeepPrelacion(prelacion);
		return new ResponseEntity<>(prelacionU,HttpStatus.OK);
	}
	
	@DeleteMapping("/actoServ/{actoId}")
	public ResponseEntity<?> deleteActoPrelacionServ(@PathVariable("actoId") Long id){

			List<Acto> actos= null;

			Acto acto= actoService.deleteActoServ(id);
			if(acto!=null){

				actos=actoService.findByPrelacionId(acto.getPrelacion().getId());
			}else{
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(actos, HttpStatus.OK);
	}
	
	@GetMapping("/find-vo/{id}")
	public ResponseEntity<?> findPrelacionVO(@PathVariable("id") Long id){
		return new ResponseEntity<>(prelacionService.findPrelacionVO(id),HttpStatus.OK);
	}
	
	
	@GetMapping("/prelaciones-denegadas/{actoId}")
	public ResponseEntity<?> findPrelacion(@PathVariable("actoId") Long actoId){
		return new ResponseEntity<>(prelacionService.findPrelacionesDenegadas(actoId),HttpStatus.OK);
	}
	
	@PutMapping("/bitacora-entrega/{prelacionId}")
	public ResponseEntity<?> bitacoraEntrega(@PathVariable("prelacionId") Long prelacionId, @RequestBody Persona persona) {

		Prelacion prelacionU = this.prelacionService.bitacoraEntrega(prelacionId, persona);
		return new ResponseEntity<>(prelacionU,HttpStatus.OK);
	}
	
	@PutMapping("/bitacora-entregas/{prelacionesId}")
	public ResponseEntity<?> bitacoraEntregas(@PathVariable("prelacionesId") List<Long> ids, @RequestBody Persona persona) {
		Prelacion prelacionU = new Prelacion();
		for(Long prelacionId:ids) {
			prelacionU = this.prelacionService.bitacoraEntrega(prelacionId, persona);
		}
		return new ResponseEntity<>(prelacionU,HttpStatus.OK);
	}

	@GetMapping("/update-entregas/{prelacionesId}")
	public ResponseEntity<Prelacion> realizaEntregas(@PathVariable("prelacionesId") List<Long> ids) throws URISyntaxException {
		log.debug("REST request crear prelacion : {}", ids);
		Prelacion result=null;
		try {
			for(Long prelacionId:ids) {
				 Prelacion prelacion =  this.prelacionService.findOne(prelacionId);
				 if(prelacion.getStatus().getId().equals(Constantes.Status.DEVOLUCION_DOCUMENTO_AUTORIZADO.getIdStatus())){
					 solicitudDevolucionService.entragaSolicitudDevolucion(prelacionId);
				 }else {
					 result = prelacionService.updatePrelacionEstado(prelacionId, Constantes.Status.ENTREGADO_AL_USUARIO,null,null,null);
				 }
			}
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/findBitacoraEntrega/{prelacionId}")
	public ResponseEntity<?> findBitacoraEntrega(@PathVariable("prelacionId") Long prelacionId){
		return new ResponseEntity<>(bitacoraEntregaService.findBitacoraEByprelacion(prelacionId) ,HttpStatus.OK);
	}
	
	@PostMapping("/devolucion")
	public ResponseEntity<?> prelacionDevolucion (@RequestParam("id") Long identificador, 
			@RequestParam("observaciones") String observaciones) {
		Prelacion prelacion = null;
		try {
			prelacion = this.prelacionService.prelacionDevolucion(identificador, observaciones);
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(prelacion, HttpStatus.OK);

	}
	
	@GetMapping(value = "/findDevoluciones/{prelacionId}")
	public ResponseEntity<?> findDevoluciones(@PathVariable Long prelacionId){
		return new ResponseEntity<>(bitacoraService.findBitacoraDevolucion(prelacionId) ,HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{prelacionId}/deleteBusquedaResultado")
	public ResponseEntity<Boolean> deleteBusquedaResultado(@PathVariable("prelacionId") Long prelacionId){
		try {
			return new ResponseEntity<>(busquedaResultadoService.clearBusquedaFromPrelacion(prelacionId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping(value = "/findSuspensiones/{prelacionId}")
	public ResponseEntity<?> findSuspensiones(@PathVariable Long prelacionId){
		return new ResponseEntity<>(bitacoraService.findBitacoraSuspencion(prelacionId) ,HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/findSuspensionesBitacora/{prelacionId}")
	public ResponseEntity<?> findSuspensionesBitacora(@PathVariable Long prelacionId){
		return new ResponseEntity<>(suspensionBitacoraService.suspensionesByPrelacion(prelacionId) ,HttpStatus.OK);
	}
	
	
	
	@GetMapping(value = "/{id}/actoRechazado")
	public ResponseEntity<?> actoRechazoByPrelacionId(@PathVariable("id") Long id) {
		BitacoraActoRechazo rechazo = null;
		rechazo = actoService.actoRechazo(id);
		
		if (rechazo != null) {
			return new ResponseEntity<>(rechazo, HttpStatus.OK);
		}
			return new ResponseEntity<>( null, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value = "/inicia-prelacion-cyvf")
	public ResponseEntity<?> iniciaPrelacionCyvf(@RequestBody AntecedenteVO antecedente) {
		try {
			Prelacion prelacion = prelacionService.iniciaPrelacionCyvf(antecedente);
			return new ResponseEntity<>(
					new MessageObject(
							"Puede carpturar el folio en el tramite " + prelacion.getConsecutivo().toString()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageObject(e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	@GetMapping("/update-prioridad/{prelacionId}/{prioridadId}")
	public ResponseEntity<?> updatePrioridad(@PathVariable Long prelacionId, @PathVariable Long prioridadId) {

		Optional<Prelacion> prelacion = prelacionService.updatePrelacionPrioridad(prelacionId, prioridadId);

		if(prelacion.isPresent()) {
			return ResponseEntity.ok(prelacion.get());
		}

		return ResponseEntity.badRequest().body("{\"mensaje\":\"Error al actualizar la prioridad de la prelacion, verifique que prioridadId sea correcto.\"}");
	}
}
