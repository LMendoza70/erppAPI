package com.gisnet.erpp.web.api.acto;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.DocumentoArchivo;
import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.TipoDocumento;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.DocumentoArchivoRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.ActoPredioCampoService;
import com.gisnet.erpp.service.ActoPredioService;
import com.gisnet.erpp.service.ActoRequisitoService;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.BitacoraDigitalizadorService;
import com.gisnet.erpp.service.BitacoraService;
import com.gisnet.erpp.service.DocumentoService;
import com.gisnet.erpp.service.PdfService;
import com.gisnet.erpp.service.ActoDocumentoService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.util.Constantes;
//import com.gisnet.erpp.service.excepciones.ForbiddenException;
import com.gisnet.erpp.vo.ActoPredioReplicaVO;
import com.gisnet.erpp.vo.ArchivoFirmaVO;
import com.gisnet.erpp.vo.CadenaOriginalVO;
import com.gisnet.erpp.vo.RequisitoVO;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;

@RestController
@RequestMapping(value = "/api/tramite", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActoRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ActoService tramiteService;
	
    @Autowired
    CampoValoresRepository campoValoresRepository;
    
	@Autowired
	PredioService predioService;

	@Autowired
	ActoPredioService actoPredioService;

	@Autowired
	ActoPredioCampoService actoPredioCampoService;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	ActoRequisitoService actoRequisitoService;

	@Autowired
	DocumentoService documentoService;

	@Autowired
	ActoDocumentoService actoDocumentoService;

	@Autowired
	ActoService actoService;

	@Autowired
	PrelacionService prelacionService;
	
	@Autowired
	ActoRepository actoRepository;

	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	DocumentoArchivoRepository documentoArchivoRepository;
	
	@Autowired
	BitacoraDigitalizadorService bitacoraDigitalizadorService;
	
	@Autowired
	BitacoraService bitacoraService;
	
	
	@Autowired
	StatusRepository statusRepository;
	
	@Autowired
	PdfService pdfService;
	
	@GetMapping(value = "/prelacion/{id}")
	public ResponseEntity<List<Acto>> findByPrelacionId(@PathVariable("id") Long id) {
		List <Acto> listActos = tramiteService.findByPrelacionId(id);
		if (listActos.size() >0) {
			return new ResponseEntity<>(tramiteService.findByPrelacionId(id), HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value = "/findOne/{id}")
	public ResponseEntity<Acto> findOneOriginal(@PathVariable("id") Long id) {
		Acto acto = tramiteService.findOne(id);
		return new ResponseEntity<>(acto, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity <Acto> findOne(@PathVariable("id") Long id) {
		Acto acto = tramiteService.findOne(id);
		ActoPredio actoPredio1 =null;
		ActoPredioCampo actoPredioCampo1 =null;
		String nombreG = "",paternoG = "",maternoG="",montoG=" ",tipoG="",Datos="";
		int numActo=0,clasif=0;
		if (acto!=null) {
			numActo= acto.getTipoActo().getId().intValue();
			clasif=acto.getTipoActo().getClasifActo().getId().intValue();
			actoPredio1=acto.getActoPrediosParaActosOrderedByVersion().first();
			Boolean montoG_Aux=false;
			Set<ActoPredioCampo> actoPredioCampos1 = actoPredio1.getActoPredioCamposParaActoPredios();			
			for (ActoPredioCampo actoPredioCampo:actoPredioCampos1)
			{	try {
						
				int campoId = actoPredioCampo.getModuloCampo().getId().intValue();				
				if(campoId==124||campoId==89) {montoG=actoPredioCampo.getValor();}
				if(campoId==492) {CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));tipoG=cv.getNombre();}				
				switch (numActo) {
				case 227://CESION DE DERECHOS CREDITICIOS 1013 ,1014, 1015
					switch (campoId) 
					{
					case 1013:nombreG=actoPredioCampo.getValor(); break;
					case 1014:paternoG=actoPredioCampo.getValor(); break;
					case 1015:maternoG=actoPredioCampo.getValor(); break;							
					}
					break;
				case 38://ASEGURAMIENTO DE BIENES 925, 926, 927
					switch (campoId) 
					{
					case 925:nombreG=actoPredioCampo.getValor(); break;
					case 926:paternoG=actoPredioCampo.getValor(); break;
					case 927:maternoG=actoPredioCampo.getValor(); break;							
					}
					break;
				case 35://CONSTITUCION DE PATRIMONIO FAMILIAR 252 ,253 ,775
					switch (campoId) 
					{
					case 252:nombreG=actoPredioCampo.getValor(); break;
					case 253:paternoG=actoPredioCampo.getValor(); break;
					case 775:maternoG=actoPredioCampo.getValor(); break;							
					}
					break;
				case 230://CONTRATO DE OCUPACIÓN SUPERFICIAL 106 ,107 ,108
					switch (campoId) 
					{
					case 106:nombreG=actoPredioCampo.getValor(); break;
					case 107:paternoG=actoPredioCampo.getValor(); break;
					case 108:maternoG=actoPredioCampo.getValor(); break;							
					}
					break;
				case 14://FIDECOMISO 479, 723, 724
					switch (campoId) 
					{
					case 479:nombreG=actoPredioCampo.getValor(); break;
					case 723:paternoG=actoPredioCampo.getValor(); break;
					case 724:maternoG=actoPredioCampo.getValor(); break;							
					}
					break;
				case 48://SERVIDUMBRE 942 ,943, 944
					switch (campoId) 
					{
					case 942:nombreG=actoPredioCampo.getValor(); break;
					case 943:paternoG=actoPredioCampo.getValor(); break;
					case 944:maternoG=actoPredioCampo.getValor(); break;							
					}
					break;
				case 219://USUFRUTO 20180 ,20181 ,20182, 20733
					switch (campoId) 
					{
					case 537:
					case 20733:
					case 20180:
						nombreG=actoPredioCampo.getValor();
						break;
					case 20181:
					case 538:
						paternoG=actoPredioCampo.getValor();
						break;
					case 20182:
					case 539:
						maternoG=actoPredioCampo.getValor(); 
						break;
					}
					break;

				case 223: //RESERVA DE DOMINIO
					switch (campoId) 
					{
					case 537:
					case 1009:
					case 20180:
						nombreG = actoPredioCampo.getValor();  
						break;
					case 1010:
					case 538:
						paternoG = actoPredioCampo.getValor(); 
						break;
					case 1011:
					case 539:
						maternoG  = actoPredioCampo.getValor(); 
						break;
					}
					break;

				case 9:
					switch (campoId) 
					{
					case 843:nombreG=actoPredioCampo.getValor(); break;
					case 844:paternoG=actoPredioCampo.getValor(); break;
					case 845:maternoG=actoPredioCampo.getValor(); break;							
					}	
					Double montoG2;
					int auxMonto=0;
					if(montoG.length()>2&&!montoG_Aux)
					{	
						
						if(acto.getId_entrada()==null) {
							//montoG2=new Double(montoG);
							//NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
							//DecimalFormat formateador = new DecimalFormat("###,###.##");
							//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
							montoG=" MONTO "+CommonUtil.formatMoney(montoG);
						}else {
							montoG="MONTO "+montoG;
						}

						montoG_Aux=true;
					}					
					break;
				case 55: //COMODATO
					switch (campoId) 
					{
					 case 925:nombreG=actoPredioCampo.getValor(); break;
					 case 926:paternoG=actoPredioCampo.getValor(); break;
					 case 927:maternoG=actoPredioCampo.getValor(); break;							
					}	
					break;
				default://843, 844, 845
					switch (campoId) 
					{
					case 843:nombreG=actoPredioCampo.getValor(); break;
					case 844:paternoG=actoPredioCampo.getValor(); break;
					case 845:maternoG=actoPredioCampo.getValor(); break;							
					}					
					break;
				}//actos
			} catch (NullPointerException e) {
				// TODO: handle exception
				log.debug("ACTO_PREDIO_CAMPO NULL REVISA");
			}		
			}
			if(paternoG==null) {paternoG="";}if(maternoG==null) {maternoG="";}
			
			if((paternoG==null || paternoG.trim().isEmpty()) && (maternoG==null || maternoG.trim().isEmpty()) 
					&& (nombreG==null || nombreG.trim().isEmpty()) && (montoG==null || montoG.trim().isEmpty()) )
				Datos = "";
			else
				Datos=" EN FAVOR DE "+ nombreG+" "+paternoG+" "+maternoG+","+montoG;
			acto.setHashFila(Datos);
			return new ResponseEntity<>(acto, HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/{id}/tipo-acto")
	public ResponseEntity <TipoActo> findTipoActoByActoId(@PathVariable("id") Long id) {
		Acto acto = tramiteService.findOne(id);
		if (acto!=null) {
			return new ResponseEntity<>(acto.getTipoActo(), HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

	}

	@GetMapping(value = "/personas/predio/{id}")
	public ResponseEntity<Set<PredioPersona>> findPersonasByPredioId(@PathVariable("id") Long id) {
		return new ResponseEntity<>(actoPredioService.findPersonasbyPredioId(id), HttpStatus.OK);
	}



	@GetMapping(value = "/clasificacion/area/{id}")
	public ResponseEntity<List<ClasifActo>> getClasificacionActoByArea(@PathVariable("id") Long id) {
		List <ClasifActo> clasificaciones = tramiteService.findAllClasifActoByArea(id);
		if (clasificaciones.size() >0) {
			return new ResponseEntity<>(clasificaciones, HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/tipo/clasficacion/{id}")
	public ResponseEntity<List<TipoActo>> getTipoActoByClasif(@PathVariable("id") Long id) {
		List <TipoActo> tipos = tramiteService.findAllTipoActoByClasif(id);
		if (tipos.size() >0) {
			return new ResponseEntity<>(tipos, HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}


	@GetMapping(value = "/{id}/requisitos/{esPresencial}")
	public ResponseEntity<List<RequisitoVO>> requisitoVEByActoId(@PathVariable("id") Long id, @PathVariable("esPresencial") Boolean esPresencial) {

		System.out.println("Numero de acto : "+id+","+esPresencial)	;

		Acto acto = tramiteService.findOne(id);
		if(acto!=null){
			List <RequisitoVO> requisitos = actoRequisitoService.getRequisitosByActo(acto, esPresencial);
			if (requisitos!=null) {
				return new ResponseEntity<>(requisitos, HttpStatus.OK);
			}else{
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		}else{
			System.out.println("ENTRO NULO");
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

	}

	
	@PostMapping("/requisitos/{esPresencial}/{isReingreso}")
	public ResponseEntity<?> agregarRequisitoActo(
			@RequestParam("file") MultipartFile archivoAdjunto,
			@RequestParam("entidad") String reqEntidadVO,
			@PathVariable("esPresencial") Boolean esPresencial,
			@PathVariable("isReingreso") Boolean isReingreso)
	{


		if (archivoAdjunto.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.CONFLICT);
		}
		List<RequisitoVO> reqLst = new ArrayList<RequisitoVO>();

		RequisitoVO	requisito ;
		//Prelacion prelacionActual= prelacionService.findOne(prelacionId);
		try {

			//System.out.println("ESTRUCTURA JSON  :  "+  reqEntidadVO );


			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

			requisito = objectMapper.readValue(reqEntidadVO, com.gisnet.erpp.vo.RequisitoVO.class);

		}
		catch (Exception except){
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}



			if(requisito==null){


					return new ResponseEntity<>("Los datos de ingreso para guardar el archivo son incorrectos",HttpStatus.BAD_REQUEST);
			}
			try{
					if(requisito.getFundatorio()){
						Documento documento= documentoService.findOne(requisito.getIdDocumento());
						if(documento!=null)
						{
								documento=	documentoService.updateWithFile(documento,archivoAdjunto,requisito.getActo().getId(),isReingreso,requisito);
								reqLst=	actoRequisitoService.getRequisitosByActo(requisito.getActo(),esPresencial);
						}

					}else{
						reqLst=	actoRequisitoService.saveActoRequisitoWithFile(requisito.getRequisito(),archivoAdjunto,requisito.getActo(),esPresencial,isReingreso);
					}



			}
			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Los datos de ingreso para guardar el archivo son incorrectos",HttpStatus.BAD_REQUEST);
			}


		return new ResponseEntity<>(reqLst, HttpStatus.OK);
	}
	
	@PostMapping("/digitalizado/{esPresencial}/{isReingreso}")
	public ResponseEntity<?> agregarDigitalizadoActo(
			@RequestParam("file") MultipartFile archivoAdjunto,
			@RequestParam("prelacionId") String prelacionId,
			@PathVariable("esPresencial") Boolean esPresencial,
			@PathVariable("isReingreso") Boolean isReingreso)
	{


		if (archivoAdjunto.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.CONFLICT);
		}
		List<RequisitoVO> reqLst = new ArrayList<RequisitoVO>();	
			try{
					
				Prelacion prelacionActual= prelacionService.findOne(Long.parseLong(prelacionId));
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
				Boolean isArchivoCreado=false;
				Archivo archivoNvo = null;
				byte[] result = null;
				Set<Acto> actos  = prelacionActual.getActosParaPrelacions();
				Set<ActoDocumento> actoDocumentos;
				Documento documento = null;
				DocumentoArchivo documentoArchivo;
				
				if(prelacionActual.getReingresado() != null) {
					isReingreso = true;
				}
				
					for (Acto acto:actos){

						
						if(!isArchivoCreado){
							log.info("Crear nuevo Archivo:");
							archivoNvo= documentoService.crearArchivoDigitalizadoWithFile(archivoAdjunto,isReingreso,prelacionId);
							if(archivoNvo!=null){
								log.info("ArchivoNvo:"+archivoNvo.getId());
								isArchivoCreado=true;
							}
						}

					System.out.println("ACTOS ----> " + acto.getActoDocumentosParaActos().isEmpty());
						
						if(acto.getActoDocumentosParaActos().isEmpty() || acto.getActoDocumentosParaActos().size()==0) {
							log.info("Crear nuevo ActoDocumento para acto_id:"+acto.getId());
							System.out.println("1");
							//IHM crear documento
							TipoDocumento tipoDocto = new TipoDocumento();
							
							
							tipoDocto.setId(27L);  //Tipo Documento
							Documento docto = new Documento();
							docto.setTipoDocumento(tipoDocto);
							docto.setArchivo(archivoNvo);

							documentoService.save(docto);
							
							//IHM crear actoDocumento
							ActoDocumento ad = new ActoDocumento();
							ad.setVersion(1);
							ad.setActo(acto);
							ad.setDocumento(docto);
							actoDocumentoService.create(ad);
						} else {
							System.out.println("2");
							if(isReingreso) {
								System.out.println("3");
								Optional<ActoDocumento>  actoDocumento=actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
								
								if(actoDocumento.isPresent()) {
									documento=actoDocumento.get().getDocumento();
									documentoArchivo = new DocumentoArchivo();
									documentoArchivo.setArchivo(archivoNvo);
									documentoArchivo.setDocumento(documento);
									documentoArchivoRepository.saveAndFlush(documentoArchivo);	
								}
								
								
								
							}else {
								Optional<ActoDocumento>  actoDocumento=actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
								if(actoDocumento.isPresent())
									documento=actoDocumento.get().getDocumento();
								if(documento!=null)
								{
									if(prelacionActual.getStatus().getId() == Constantes.Status.DIGITALIZA_DEVOLUCION_DOCUMENTO.getIdStatus()) {
										prelacionService.updatePrelacionEstado(prelacionActual.getId(),
												Constantes.Status.PENDIENTE_DEVOLUCION_DOCUMENTO, "RECEPCIÓN DEVOLUCION DOCUMENTOS", null, null);

										if(documento.getArchivo() != null && documento.getArchivo().getId() != 1L ) {

											Archivo archivoAppend = null;
											byte[] bytes = archivoAdjunto.getBytes();
											Path path = Paths.get( documento.getArchivo().getRuta() + documento.getArchivo().getNombre() );
											byte[] fileContent = Files.readAllBytes(path);

											result = pdfService.appendPDF(fileContent,bytes);

											archivoAppend= documentoService.crearArchivoDigitalizadoWithFileAppend(result,isReingreso,prelacionId);

											if(archivoAppend!=null){
												log.info("ArchivoNvo:"+archivoAppend.getId());
											}

											documento=	documentoService.updatePrelacionDocumentoWithFile(documento,archivoAppend);
											reqLst=	actoRequisitoService.getRequisitosByActo(acto,esPresencial);

										} else {
											documento=	documentoService.updatePrelacionDocumentoWithFile(documento,archivoNvo);
											reqLst=	actoRequisitoService.getRequisitosByActo(acto,esPresencial);
										}

									} else {
										documento=	documentoService.updatePrelacionDocumentoWithFile(documento,archivoNvo);
										reqLst=	actoRequisitoService.getRequisitosByActo(acto,esPresencial);
									}

								}
							}

						}

					}
					prelacionActual.setEs_digitalizado(true);
					prelacionService.saveAndUpdate(prelacionActual);
					
					bitacoraDigitalizadorService.saveBitacoraDig(prelacionActual, usuario);
					
			}
			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Los datos de ingreso para guardar el archivo son incorrectos",HttpStatus.BAD_REQUEST);
			}

		return new ResponseEntity<>(reqLst, HttpStatus.OK);
	}
	
	@PostMapping("/reemplazar-digitalizado/")
	public ResponseEntity<?> reemplazarDigitalizado(
			@RequestParam("file") MultipartFile archivoAdjunto,
			@RequestParam("archivoId") String archivoId,
			@RequestParam("obs") String obs)
	{


		if (archivoAdjunto.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.CONFLICT);
		}
		List<RequisitoVO> reqLst = new ArrayList<RequisitoVO>();	
			try{			
				
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());	
				
				documentoService.reemplazarDigitalizado(archivoAdjunto, Long.parseLong(archivoId),obs,usuario);
			}
			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Los datos de ingreso para guardar el archivo son incorrectos",HttpStatus.BAD_REQUEST);
			}

		return new ResponseEntity<>(reqLst, HttpStatus.OK);
	}

	@PostMapping("/anexar-digitalizado/")
	public ResponseEntity<?> anexarDigitalizado(
			@RequestParam("file") MultipartFile archivoAdjunto,
			@RequestParam("archivoId") String archivoId,
			@RequestParam("obs") String obs)
	{


		if (archivoAdjunto.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.CONFLICT);
		}
		List<RequisitoVO> reqLst = new ArrayList<RequisitoVO>();	
			try{			
				
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());	
				
				documentoService.anexarDigitalizado(archivoAdjunto, Long.parseLong(archivoId),obs,usuario);
			}
			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Los datos de ingreso para anexar el archivo son incorrectos",HttpStatus.BAD_REQUEST);
			}

		return new ResponseEntity<>(reqLst, HttpStatus.OK);
	}

	@DeleteMapping(value = "/eliminar-requisito/{idActo}/{idArchivo}/{idRequisito}")
	public ResponseEntity<?>  eliminarRequisito(@PathVariable("idActo") Long idActo,
			@PathVariable("idArchivo") Long idArchivo,
			@PathVariable("idRequisito") Long idRequisito
			)
	{
		try 
		{
			return new ResponseEntity<>(actoRequisitoService.eliminarRequisito(idActo,idArchivo,idRequisito),HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
		}
	}

	
	
	
	@DeleteMapping(value = "/eliminar-fundatorio/{documentoArchivoId}")
	public ResponseEntity<?>  eliminarFundatorio(@PathVariable("documentoArchivoId") Long documentoArchivoId)
	{
		try 
		{
			return new ResponseEntity<>(documentoService.eliminarFundatorio(documentoArchivoId),HttpStatus.OK);
		}catch(Exception e)
		{
			return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
		}
	}


	@GetMapping(value="/{id}/predios")
    public ResponseEntity <Set<ActoPredio>> findActosPrediosByPredioId(@PathVariable("id") Long id) {
		return new ResponseEntity<> (tramiteService.getActoPrediosByActoId(id), HttpStatus.OK);
    }

	@GetMapping(value="/{actoId}/{tipoEntradaId}/predio-last-version")
    public ResponseEntity <Long> findActosPrediosByPredioIdVersion(@PathVariable("actoId") Long actoId, @PathVariable("tipoEntradaId") Long tipoEntradaId) {
		return new ResponseEntity<> (actoPredioService.findByLastVersion(actoId, tipoEntradaId), HttpStatus.OK);
    }

	@GetMapping(value="/{actoId}/{version}/{tipoEntradaId}")
    public ResponseEntity <ActoPredio> findActosPrediosByPredioIdVersion(@PathVariable("actoId") Long actoId, @PathVariable("version") Integer version, @PathVariable("tipoEntradaId") Long tipoEntradaId) {
		return new ResponseEntity<> (actoPredioService.findOneByActoIdAndVersion(actoId, version), HttpStatus.OK);
    }
	
	@GetMapping(value="/{id}/predio/linderos")
    public ResponseEntity <List<Colindancia>> findPredioLinderosByPredioId(@PathVariable("id") Long id) {
		return new ResponseEntity<> (predioService.findOneColindancias(id), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/predio/folio-frac-cond")
    public ResponseEntity <FoliosFracCond> findPredioFolioFracCondByPredioId(@PathVariable("id") Long id) {
		return new ResponseEntity<> (predioService.findOneFolioFracCond(id), HttpStatus.OK);
    }

	@PutMapping("/update-acto-predio")
	public ResponseEntity<?> updateActoPredio(@RequestBody ActoPredio actoPredio)
	{

		if (actoPredio==null) {
			return new ResponseEntity<>("El objeto de envio es invalido", HttpStatus.CONFLICT);
		}


		try {

			actoPredioService.save(actoPredio);

		}
		catch (Exception except){
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}



		return new ResponseEntity<>(actoPredio, HttpStatus.OK);
	}
	
	@GetMapping("{id}/desligar")
	public ResponseEntity<?> desligarActo(@PathVariable("id") Long id)
	{
		try 
		{
			return new ResponseEntity<>(actoPredioService.desligarActo(id),HttpStatus.OK);
		}catch(Exception e) 
		{
			return new ResponseEntity<>(null,HttpStatus.CONFLICT);
		}
	}

	@PostMapping("/acto/rechazo")
	public ResponseEntity<?> rechazarActo (@RequestParam("id") Long identificador, @RequestParam("actoId") String id,
			@RequestParam("motivoId") String motivoId, @RequestParam("fundamentoId") String fundamentoId, 
			@RequestParam("observaciones") String observaciones,@RequestParam("tipo") String tipo) {
		Prelacion prelacion = null;
		Acto acto = null;
		Long motivo = null;
		Long fundamento = null;
		Long actoI = null;
		if(!motivoId.equals("null")) {
			motivo = Long.parseLong(motivoId);
		}
		if(!fundamentoId.equals("null")) {
			fundamento = Long.parseLong(fundamentoId);
		}
		if(!id.equals("null")) {
			actoI = Long.parseLong(id);
		}
//		if (motivoId==null) {
//			return new ResponseEntity<>("El objeto de envio es invalido", HttpStatus.CONFLICT);
//		}
		try {
			if(tipo.equals("rechazo")) {
				acto = actoService.rechazarActo(actoI, motivo,fundamento, observaciones);
//				prelacion = actoService.rechazarPrelacion(id, motivo, tipoRechazo, observaciones,fundamento);
//				return new ResponseEntity<>(prelacion, HttpStatus.OK);
			}
			else if(tipo.equals("rechazo-antecedente")) {
				prelacion = actoService.rechazarAntecedente(identificador, motivo, observaciones,fundamento);
				return new ResponseEntity<>(prelacion, HttpStatus.OK);
			}
			else if (tipo.equals("rechazo-actos")) {
				acto = actoService.rechazarActos(identificador, motivo,fundamento, observaciones);
				Prelacion prel = prelacionService.findOne(identificador);
				if(prel != null) {
					prelacionService.createBitacora(prel, observaciones,null);
					prel.setStatus(statusRepository.findOne(Constantes.Status.ASIGNADO_A_COORDINADOR.getIdStatus()));
					prelacionService.saveAndUpdate(prel);
				}
			}
		}
		catch (Exception except) {
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(acto, HttpStatus.OK);

	}

	@PostMapping("/replica-acto-predio")
	public ResponseEntity<?> replicaActo(@RequestBody ActoPredio actoPredio)
	{

		if (actoPredio==null) {
			return new ResponseEntity<>("El objeto de envio es invalido", HttpStatus.CONFLICT);
		}


		try {

			actoPredio=actoPredioService.replicaActoPredio(actoPredio);

		}
		catch (Exception except){
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}



		return new ResponseEntity<>(actoPredio, HttpStatus.OK);
	}

	@PostMapping("/replica-acto-predio-actoprediocampo")
	public ResponseEntity<?> replicaActo(@RequestBody ActoPredioReplicaVO actoPredioReplicaVO )
	{
		if (actoPredioReplicaVO.getNewActoPredio()==null) {
			return new ResponseEntity<>("El objeto de envio es invalido", HttpStatus.CONFLICT);
		}

		
		try 
		{
		  List<PrelacionPredio> prelacionPredio =	actoPredioReplicaVO.getPrelacionPredio();
		  if(prelacionPredio!=null && prelacionPredio.size() > 0) {
			  prelacionPredio.parallelStream().forEach(x->{
				  ActoPredio actoPredio=null;
				  ActoPredio newActoPredio = actoPredioReplicaVO.getNewActoPredio(); 
				  if(x.getPredio()!=null)
					  newActoPredio.setPredio(x.getPredio());
				  if(x.getPersonaJuridica() != null)
					  newActoPredio.setPersonaJuridica(x.getPersonaJuridica());
				  if(x.getMueble()!=null)
					  newActoPredio.setMueble(x.getMueble());
				  
				  actoPredio=actoPredioService.replicaActoPredio(newActoPredio,x);				  
				  actoPredioService.replicaActoPrediosCampos(actoPredio,actoPredioReplicaVO.getParentActoPredio().getActo().getId());
			  });
		  }
			
		}
		catch (Exception except){
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}



		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	



	@GetMapping(value="/{id}/validaprecaptura")
    public ResponseEntity <Boolean> validaPrecaptura(@PathVariable("id") Long id) {
		Prelacion prelacionActual= prelacionService.findOne(id);
		return new ResponseEntity<> (prelacionService.validaActoPrecaptura(prelacionActual), HttpStatus.OK);
    }
	


	@PutMapping("/firma-acto")
	public ResponseEntity<ActoFirma> firmarActo(@RequestBody ArchivoFirmaVO firma) throws URISyntaxException {
		ActoFirma result=null;
		//try {
			result = tramiteService.firmaActo(firma);
		//}
		/*catch (Exception err) {
			System.out.println(err.getMessage());
			//return new ResponseEntity<>(err.getMessage(), HttpStatus.BAD_GATEWAY);
		}*/

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping(value="/firma/acto/{id}")
	public ResponseEntity<ActoFirma> getFirmarActo(@PathVariable("id") Long id) throws URISyntaxException {
		ActoFirma result=null;
		try {
			List <ActoFirma> results = tramiteService.getActosFirma(id);
			if(results!=null && results.size()>0){

				result=	results.get(0);
			}
		}
		catch (Exception err) {
			System.out.println(err.getMessage());
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	@DeleteMapping("/{actoId}/desfirma-acto")
	public ResponseEntity<?> deleteActoFirma(@PathVariable("actoId") Long actoId ){

		Boolean result = null;
		try {
			result = tramiteService.desfirmarActo(actoId);
		} catch (Exception e) {
		}

		if (result==null) {
			return new ResponseEntity<>(false, HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@DeleteMapping("/{actoId}/desfirma-desmaterializa-acto")
	public ResponseEntity<?> deleteActoFirmaDesmaterializa(@PathVariable("actoId") Long actoId ){
		try
		{
			Boolean result =tramiteService.desfirmarActo(actoId);
		    if(result) 
		    {
		    	tramiteService.desmaterializar(actoId);
		    }
			return new ResponseEntity<>(true, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/cadena-firma/{idActo}")
	public ResponseEntity<CadenaOriginalVO> getCadenaFirmadoActo(@PathVariable("idActo") Long idActo) {
		String cadena = tramiteService.getCadenaFirmado(idActo);
		CadenaOriginalVO cadVO = new CadenaOriginalVO();
		if (cadena!=null) {
			cadVO.setCadenaOriginal(cadena);
			return new ResponseEntity<>(cadVO, HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}



	@GetMapping(value = "/ultima-moficacion/{idActo}")
	public ResponseEntity<Boolean> regresarUltimaVersionActo(@PathVariable("idActo") Long idActo) {
		Boolean valido = actoPredioService.regresarUltimaVersion(idActo);

		if (valido) {
			return new ResponseEntity<>(valido, HttpStatus.OK);
		}
			return new ResponseEntity<>(valido, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value="/{prelacionId}/actos-predio/{predioId}")
    public ResponseEntity <List<Acto>> findActosByPredioId(@PathVariable("prelacionId") Long prelacionId, @PathVariable("predioId") Long predioId) {
		return new ResponseEntity<> (actoService.findByActoPrediosParaActosPredioIdAndPrelacionId(predioId, prelacionId), HttpStatus.OK);
    } 
	
	@GetMapping(value="/{idMueble}/actos-mueble")
    public ResponseEntity <List<Acto>> findActosByMuebleId(@PathVariable("idMueble") Long idMueble) {
		return new ResponseEntity<> (actoService.findByActoPrediosParaActosMuebleId(idMueble), HttpStatus.OK);
    } 
	
	@GetMapping(value="/{idPj}/actos-pj")
    public ResponseEntity <List<Acto>> findActosByPjId(@PathVariable("idPj") Long idPj) {
		return new ResponseEntity<> (actoService.findByActoPrediosParaActosPersonaJuridicaId(idPj), HttpStatus.OK);
    } 
	
	@GetMapping(value="/{idSeccionAuxiliar}/actos-auxiliar")
    public ResponseEntity <List<Acto>> findActosByAuxiliarId(@PathVariable("idSeccionAuxiliar") Long idSeccionAuxiliar) {
		return new ResponseEntity<> (actoService.findByActoPrediosParaActosFolioSeccionAuxiliarId(idSeccionAuxiliar), HttpStatus.OK);
    } 
	
	
	@GetMapping(value="/actos-para-heredar/predio/{predioId}")
    public ResponseEntity <List<Acto>> getActosParaHeredarByPredio(@PathVariable("predioId") Long predioId) {
		return new ResponseEntity<> (actoService.getActosParaHeredarByPredio(predioId), HttpStatus.OK);
    } 
	
	@GetMapping(value = "/tipo-acto-tipo-acto/tipo-acto/{tipoActoId}/folio/{folioId}/tipo-folio/{tipoFolioId}")
	public ResponseEntity<List<Acto>> findActosByTipoActoPadreEnTipoActoTipoActo(@PathVariable("tipoActoId") Long tipoActoId, @PathVariable("folioId") Long folioId, @PathVariable("tipoFolioId") Long tipoFolioId) {
		return new ResponseEntity<>(actoService.findActosByTipoActoPadreEnTipoActoTipoActo(tipoActoId, folioId, tipoFolioId), HttpStatus.OK);		
	}

	@DeleteMapping("/{actoId}")
	public ResponseEntity<Boolean> deleteActoPrelacion(@PathVariable("actoId") Long id) {
		actoService.deleteActoPrevioDesmaterializacion(id);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@GetMapping("/findActoByActoId/{id}")
	public ResponseEntity<Acto> findActoByActoId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(actoRepository.findOneById(id));
	}
	
	@GetMapping(value = "/findTitularesFusionByActoId/{actoId}")
	public ResponseEntity<Pair<Boolean, List<PredioPersona>>> findTitularesFusionByActoId(@PathVariable("actoId") Long actoId) {
		return new ResponseEntity<>(actoService.findTitularesFusionByActoId(actoId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/findTitularesByActoId/{actoId}")
	public ResponseEntity<List<PredioPersona>> findTitularesByActoId(@PathVariable("actoId") Long actoId) {
		return new ResponseEntity<>(actoService.findTitularesByActoId(actoId), HttpStatus.OK);
	}

 	@GetMapping(value = "/findActosByActoIdActivosIsReplicaPorFusion/{actoId}")
	public ResponseEntity<List<Acto>> findActosByPredioIdActivosIsReplicaPorFusion(@PathVariable("actoId")  Long actoId) {
		return new ResponseEntity<>(actoService.findActosByActoIdActivosIsReplicaPorFusion(actoId), HttpStatus.OK);
	}
 	
 	@GetMapping(value = "/findActosByActoIdActivosIsReplica/{actoId}")
	public ResponseEntity<List<Acto>> findActosByPredioIdActivosIsReplica(@PathVariable("actoId")  Long actoId) {
		return new ResponseEntity<>(actoService.findActosByActoIdActivosIsReplica(actoId), HttpStatus.OK);
	}
 	
 	@GetMapping(value = "/findActosByPredioIdReplica/{actoId}")
	public ResponseEntity<List<Acto>> findActosByPredioIdReplica(@PathVariable("actoId")  Long actoId) {
		return new ResponseEntity<>(actoService.findActosByPredioIdReplica(actoId), HttpStatus.OK);
	}
 	
 	
 	
 	
 	
	/*
	@GetMapping(value = "/descripcion-corta/{id}")
	public ResponseEntity<String> getDescripcionCortaActo(@PathVariable("id") Long id) {
		String desc = tramiteService.getDescripcionCortaActo(id);
		if (desc!=null) {
			return new ResponseEntity<>(desc, HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}	
	*/
	@DeleteMapping("/acto/{actoId}")
	public ResponseEntity<?> deleteActo(@PathVariable("actoId") Long id){
		this.actoService.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/acto/{actoId}/hard-delete")
	public ResponseEntity<?> hardDeleteActo(@PathVariable("actoId") Long id){
		this.actoService.hardDelete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/activa-acto/{actoId}")
	public ResponseEntity<?> activaActo(@PathVariable("actoId") Long id)
	{

		try {
				
			actoService.activaActo(actoService.findOne(id));

		}
		catch (Exception except){
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}



		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@PostMapping("/invalida-acto/{actoId}")
	public ResponseEntity<?> invalidaActo(@PathVariable("actoId") Long id)
	{

		try {

			actoService.invalidarActo(actoService.findOne(id));

		}
		catch (Exception except){
			except.printStackTrace();
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}



		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
	@GetMapping(value = "/findPrelacionPredioByActoId/{actoId}")
	public ResponseEntity<List<PrelacionPredio>> findPrelacionPredioByActoId(@PathVariable("actoId") Long actoId) {
		return new ResponseEntity<>(actoService.findPrelacionPredioByActoId(actoId), HttpStatus.OK);
	}
	
	@GetMapping(value="/{id}/predio/colindancias")
    public ResponseEntity <List<Colindancia>> findPredioLinderosByActoId(@PathVariable("id") Long id) {
		try
		{
				ActoPredio ap =	 actoPredioService.findMaxVersion(id);
			    Long predioId =  ap != null ? ap.getPredio().getId() : null;
			    if(predioId != null)
			    	return new ResponseEntity<> (predioService.findOneColindancias(predioId), HttpStatus.OK);
			    else 
			    	return new ResponseEntity<> (null, HttpStatus.NOT_FOUND); 	    
		}catch(Exception e)
		{
			return new ResponseEntity<> (null, HttpStatus.NOT_FOUND);
		}
	}
}
