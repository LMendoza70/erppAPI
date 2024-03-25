package com.gisnet.erpp.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.ActoModulo;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPublicitario;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.DocumentoArchivo;
import com.gisnet.erpp.domain.Juez;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Modulo;
import com.gisnet.erpp.domain.ModuloCampo;
import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.domain.Motivo;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioRel;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionBoleta;
import com.gisnet.erpp.domain.PrelacionContratante;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.PrelacionServicio;
import com.gisnet.erpp.domain.PrelacionUsuarioDef;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.domain.RecursoInconformidad;
import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.Suspension;
import com.gisnet.erpp.domain.SuspensionFirma;
import com.gisnet.erpp.domain.TipoNotario;
import com.gisnet.erpp.domain.TipoRechazo;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoPublicitarioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.ArchivoRepository;
import com.gisnet.erpp.repository.AreaRepository;
import com.gisnet.erpp.repository.BitacoraRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.ColindanciaRepository;
import com.gisnet.erpp.repository.DocumentoArchivoRepository;
import com.gisnet.erpp.repository.DocumentoRepository;
import com.gisnet.erpp.repository.ModuloTipoActoRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PjPersonaRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.repository.PredioRelRepository;
import com.gisnet.erpp.repository.PrelacionAnteRepository;
import com.gisnet.erpp.repository.PrelacionBoletaRepository;
import com.gisnet.erpp.repository.PrelacionContratanteRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.PrelacionUsuarioDefRepository;
import com.gisnet.erpp.repository.PrioridadRepository;
import com.gisnet.erpp.repository.RecursoInconformidadRepository;
import com.gisnet.erpp.repository.RegimenRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.repository.SuspensionFirmaRepository;
import com.gisnet.erpp.repository.SuspensionRepository;
import com.gisnet.erpp.repository.TipoBoletaRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.excepciones.RequerimientoException;
import com.gisnet.erpp.service.materializacion.ActoPorcentajeVO;
import com.gisnet.erpp.service.materializacion.ActoUtilService;
import com.gisnet.erpp.service.reportes.AvisoCautelarService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.vo.AcreditanteVO;
import com.gisnet.erpp.vo.AvisoCautelarVO;
import com.gisnet.erpp.vo.BoletaRechazoVO;
import com.gisnet.erpp.vo.BoletaSuspensionVO;
import com.gisnet.erpp.vo.PredioActoVO;
import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.vo.certificado.CertificadoLibertadOGravamenVO;
import com.gisnet.erpp.vo.juridico.CertificadoDeNoInscripcionVO;
import com.gisnet.erpp.vo.juridico.CertificadoPersonaJuridicasVO;
import com.gisnet.erpp.vo.juridico.RespuestaBusquedaSimpleVO;
import com.gisnet.erpp.vo.prelacion.ActoModel;
import com.gisnet.erpp.vo.prelacion.AntecedenteModel;
import com.gisnet.erpp.vo.prelacion.PredioModel;
import com.gisnet.erpp.vo.prelacion.PrelacionBoletaRegistralVO;
import com.gisnet.erpp.vo.prelacion.ReciboModel;
import com.gisnet.erpp.vo.prelacion.ResultantesModel;
import com.gisnet.erpp.vo.prelacion.TitularModel;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
@Transactional
@Service
public class BoletaRegistralService {


    private final String FECHA_FORMATO_CAMPO_VALOR = "yyyy-mm-dd";

    private final long NOMBRE = 286;
    private final long PATERNO = 287;
    private final long MATERNO = 288;
    private final long FECHA_NACIMIENTO = 289;
    private final long RFC = 290;
    private final long CURP = 291;
    private final long ESTADO_CIVIL = 292;
    private final long REGIMEN = 293;
    private final long NACIONALIDAD = 119;
    private final long TIPO_DE_PERSONA = 84;
    private final long DD = 55;
    private final long UV = 56;

    private final long MONTO_DEL_CREDITO = 72;
    private final long PORCENTAJE_AFECTACION_DEL_INMUEBLE = 263;
    private final long TIPO_DE_MONEDA = 265;

    private final long PREDIO_A_SUBDIVIDIR = 270;
    private final long SUPERFICIE_SUBDIVIDIDA = 271;

    

    @Autowired
    PrelacionRepository prelacionRepository;
    
    @Autowired
	PrelacionUsuarioDefRepository prelacionUsuarioDefRepository;
    
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ActoRepository actoRepository;

    @Autowired
    ActoPredioRepository actoPredioRepository;
    
    @Autowired
    ActoPublicitarioRepository actoPublicitarioRepository;

    @Autowired
	ColindanciaRepository colindanciaRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    StatusActoRepository statusActoRepository;
    
    @Autowired
	PredioRepository predioRepository;

    @Autowired
    PrioridadRepository prioridadRepository;

    @Autowired
    PredioRelRepository  predioRelRepository ;

    @Autowired
	BitacoraRepository bitacoraRepository;

    @Autowired
    AreaRepository areaRepository;
    
    @Autowired
    ActoFirmaRepository actoFirmaRepository;

    @Autowired
    OficinaRepository oficinaRepository;

    @Autowired
    PrelacionPredioRepository prelacionPredioRepository;

    @Autowired
    PrelacionAnteRepository prelacionAnteRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ActoService actoService;
    
	@Autowired
	ColindanciaService colindanciaService;
	
    @Autowired
    TipoActoService tipoActoService;
    
    @Autowired
    PrelacionBoletaService prelacionBoletaService;
    
    @Autowired
	private PrelacionBoletaRepository prelacionBoletaRepository;

    
    @Autowired
    PrelacionService prelacionService;

    @Autowired
    MailSenderService mailSenderService;
    
    @Autowired
    ArchivoRepository archivoRepository;

    @Autowired
    DocumentoRepository documentoRepository;
    
    @Autowired
    ParametroRepository parametroRepository;
    
    @Autowired
    ActoDocumentoService actoDocumentoService;

    @Autowired
    ActoPredioService actoPredioService;

    @Autowired
    ReciboService reciboService;

    @Autowired
    AntecedenteService antecedenteService;

    @Autowired
    PrelacionPredioService prelacionPredioService;

    @Autowired
    private TurnadorService turnadorService;

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private AnalisisPorSistemaService analisisPorSistemaService;

    @Autowired
    private BusquedaService busquedaService;

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    SuspensionRepository suspencionRepository;
    
    @Autowired
	SuspensionFirmaRepository suspensionFirmaRepository;

    @Autowired
    private AvisoCautelarService avisoCautelarService;

    @Autowired
    PdfService pdfService;

    @Autowired
    PredioPersonaService predioPersonaService;

    @Autowired
    RegimenRepository regimenRepository;
    @Autowired
    NacionalidadRepository nacionalidadRepository;
    @Autowired
    TipoPersonaRepository tipoPersonaRepositor;
    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    PrelacionContratanteRepository prelacionContratanteRepository;
    
    @Autowired
    CampoValoresRepository campoValoresRepository;
    
    @Autowired
    PjPersonaRepository pjPersonaRepository;

    @Autowired
    ActoPredioCampoRepository actoPredioCampoRepository;

    @Autowired
	NotarioService notarioService;
    
    @Autowired
	EstadoService estadoService;

	@Autowired
	MunicipioService municipioService;

	@Autowired
	ActoService tramiteService;
	
	@Autowired
	TipoPersonaService tipoPersonaService;
	
	@Autowired
	PersonaService personaService;
    
	@Autowired
	ParentescoService parentescoService;
	
	@Autowired
	NacionalidadService nacionalidadService;
	
	@Autowired
	OrientacionService orientacionService;
	
	@Autowired
	TipoColinService tipoColinService;
	
	@Autowired
	CampoValorService campoValorService;
	
	@Autowired
	PredioService predioService;
	
	@Autowired
	ModuloTipoActoRepository moduloTipoActoRepository;
	
	@Autowired
	private TipoBoletaRepository tipoBoletaRepository;
	
	@Autowired
	private ActoDocumentoRepository actoDocumentoRepository;
	
	@Autowired
	private DocumentoArchivoRepository documentoArchivoRepository;
    
	@Autowired
	private CaratulaService caratulaService;
	
	@Autowired
	CopiasService copiasService;
	
	@Autowired
	RecursoInconformidadRepository recursoInconformidadRepository;
	
	
	@Autowired
    ActoUtilService actoUtilService;
	
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper = new ObjectMapper();
    
    public BoletaRegistralService() {
		mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
    /**
     * Integra los datos de los predios para la boleta registral
     * @param prelacionId  id de la prelación a buscar los predios
     * @return List<PrelacionBoletaRegistralVO> PrelacionBoletaRegistralVO> con cada registro de predio y sus correspondientes actos.
     *         Vacío cuando no hay información de predios para la prelación
     *         Vacío cuando todos los actos están rechazados
     */
    public List<PrelacionBoletaRegistralVO> findVOPredios(Long prelacionId,boolean pageable,Integer pagina,boolean save,Prelacion _prelacion){ 
        StringBuilder sb = new StringBuilder();
        
        boolean buscarDireccion=true;
		boolean buscarTextoRegistro=true;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Prelacion prelacion = save ? _prelacion : prelacionRepository.findOne(prelacionId);
        Prelacion _prelContext  = prelacionRepository.findOne(prelacionId);
        System.out.println("PAGINA "+pagina);
        boolean materializacion = false;
        Status estatus = prelacion.getStatus();
        Long estatusId = estatus.getId(); 
        if( estatusId == 7 || estatusId == 8 || estatusId == 9 ) {
        	materializacion = true;
        }
        
        List<PrelacionBoletaRegistralVO> pvos = new ArrayList<PrelacionBoletaRegistralVO>();

        
        Constantes.ETipoFolio tipo = prelacionService.getTipoFolio(_prelContext);
        PrelacionBoletaRegistralVO pvo = this.getBRHeader (prelacion);

        ArrayList<PrelacionPredio> predios  = (ArrayList) prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacion.getId());
        
        List<Acto> porActos  = null;
        
        if(pageable) 
        {
           Pageable pagea = new PageRequest(pagina,save ? 100 : Constantes.PAGE_SIZE_REPORT);
           Page<Acto> pageActos = actoRepository.findAllByPrelacionIdAndVfFalse(prelacionId,pagea);
           porActos = pageActos.getContent();
           
        }else 
        {
        	porActos = actoRepository.findAllByPrelacionIdAndVfFalseOrderByOrdenAsc(prelacion.getId(),false);	
        }
        
        
                
        if(porActos!=null && !porActos.isEmpty() && porActos.size()>0 && predios!=null && !predios.isEmpty())
        {
           
            for(Acto acto :porActos){
            	ActoPredio actoPredio = actoPredioRepository.findActoPredioByLastVersion(acto.getId());
                Predio predioOk = actoPredio.getPredio();
                PersonaJuridica pj =  actoPredio.getPersonaJuridica();
                
                if( acto.getStatusActo().getId()!=5L 
                	&& acto.getStatusActo().getId()!=6L 
                	&& (predioOk!=null || pj!=null) && !acto.getTipoActo().getId().equals(123L)){
                	
                	//PARA FUSION DESCARTA ACTO SIN RESULTANTES
                	if(acto.getTipoActo().getId().equals(41l)) {
                		List<PredioRel> predioRels=predioRelRepository.findByActoId(acto.getId());
                		if(predioRels==null || predioRels.size()<=0)
                			 continue;
                	}
                		
                	PrelacionPredio predio = null;
                	if(predioOk!=null)
                        predio  = prelacionPredioRepository.findFirstByPrelacionIdAndPredioId(acto.getPrelacion().getId(),predioOk.getId());
                	if(pj!=null)
                		predio  = prelacionPredioRepository.findByPrelacionIdAndPersonaJuridicaId(acto.getPrelacion().getId(),pj.getId()); 

                       if( predio!=null && 
                    		   ( 
                    		     (predio.getPredio()!= null && predio.getPredio().getId().equals(predioOk.getId()) )
                    		     || (predio.getPersonaJuridica()!= null && predio.getPersonaJuridica().getId().equals(pj.getId()) )
                    		   )
                    	 ){

                            sb = new StringBuilder();
        

                            List<Acto> actos = actoRepository.findAllByPrelacionId(prelacion.getId());
                
                            if( predio.getEstatus() != 1 )
                                continue;
                
                            if  (todosActosRechazados (predio))
                                continue;
                            
                           
                            PersonaJuridica personaJ = new PersonaJuridica();
                
                            for(Acto act : actos) {
                                if(tipo==Constantes.ETipoFolio.PERSONAS_JURIDICAS){
                                    ActoPredio ap = actoPredioRepository.findFirstByActoId(act.getId());
                                    personaJ=ap.getPersonaJuridica();
                                  } 
                            }
                
                            List<PrelacionPredio> lPredioUnico = new ArrayList<PrelacionPredio> ();
                            lPredioUnico.add(predio);
                
                            pvo.setAntecedentes(this.buildAntecedentes(lPredioUnico, tipo));
                
                            pvo.setPredios(this.buildPredios ( lPredioUnico , tipo) );
                
                            if( tipo == Constantes.ETipoFolio.PREDIO ) {
                               // pvo.setUbicacion(predio.getPredio().getDireccion());
                               // if( materializacion ) {
                                    log.debug( "===> con materializacion" );
                                    log.debug("Inicia BuildTitulares");
                                    pvo.setTitulares(this.buildTitulares(lPredioUnico, tipo) );
                                    log.debug("Termina BuildTitulares");
                                /*} else {
                                    log.debug( "===> sin materializacion" );
                                    pvo.setTitulares( this.buildTitularesPrelacionContratante(prelacion,predio.getPredio()) );
                                }*/
                                   buscarDireccion =  true;
                                if(predio.getPredio().getProcedeDeFolio()!=null ){
                                    log.debug("Procede de Folio:"+predio.getPredio().getProcedeDeFolio());
                                    if(!predio.getPredio().getProcedeDeFolio().isEmpty()){
                                        pvo.setProcedeDeFolio("PROCEDE DE FOLIO:"+ predio.getPredio().getProcedeDeFolio());
                                    }
                                }
                            
                               if(predio.getPredio().getPrimerRegistro()!=null){
                                   log.debug("ES PRIMER REGISTRO");
                                   if(predio.getPredio().getPrimerRegistro() == 1){pvo.setProcedeDeFolio("PRIMER REGISTRO");}
                               } 
                        
                            } else if( tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS ) {
                                pvo.setUbicacion(predio.getPersonaJuridica().getDireccion() );
                                List<TitularModel> listTM = new ArrayList<TitularModel>();
                                TitularModel tm = new TitularModel();
                                tm.setNombre(predio.getPersonaJuridica().getDenominacionNombre());
                                if(predio.getPersonaJuridica().getPrimerRegistro()!=null){
                                    if(predio.getPersonaJuridica().getPrimerRegistro()==1){
                                        pvo.setProcedeDeFolio("PRIMER REGISTRO");
                                    }else{
                                        pvo.setProcedeDeFolio("");
                                    }
                                }else{
                                    this.buildAntecedentes(lPredioUnico, tipo);
                                }
                                listTM.add(tm);
                                pvo.setTitulares(listTM);                    
                                if(pvo.getProcedeDeFolio()==null && pvo.getAntecedentes()==null){
                                    pvo.setProcedeDeFolio("PRIMER REGISTRO");
                                }
                                buscarDireccion=false;
                            } else if( tipo == Constantes.ETipoFolio.MUEBLE ) {
                            } else if( tipo == Constantes.ETipoFolio.PERSONAS_AUXILIARES ) {
                            }
                
                            if(pvo.getAntecedentes()!=null){
                                pvo.setProcedeDeFolio("");
                            }else{
                                if( tipo == Constantes.ETipoFolio.PREDIO ) {
                                    if(predio.getPredio().getLibro()!=null && predio.getPredio().getDocumento()!=null){
                                        pvo.setAntecedentes(this.buildAntecedentesFromPredio(lPredioUnico));
                                        if(pvo.getAntecedentes()!=null){
                                            pvo.setProcedeDeFolio("");
                                        }
                                    }
                                }   
                            }
                            log.debug("Inicia BuildActos");
                            List<ActoModel> models=this.buildActos(predio,actoPredio, pvo,tipo,prelacion);
                            log.debug("Termina BuildActos");
                            pvo.setActos(models); //<--  Cambiar a actoPredio
                            System.out.println("INICIO "+models.size());
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            if(acto!=null){
                                List<ActoPredioCampo> actoPredioCampos = new ArrayList();
                                List<ActoPredioCampo> naturalezaProcedencia = new ArrayList<ActoPredioCampo>();
                                int tipoActo = acto.getTipoActo().getId().intValue();
                                System.out.println("TIPO DE ACTO "+tipoActo);
                                String obs="";
                              
                                actoPredioCampos=getActoPredioCampo(acto);
                               
                                
                        
                                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) 
                                {
                                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                    int id_modulo_campo =  moduloCampo.getId().intValue();
                                    if (id_modulo_campo == 431) { obs = actoPredioCampo.getValor(); }
                                }
                                
                              
                                if(!obs.isEmpty()&&obs!=null){
                                    pvo.setObservaciones(obs);
                                }
                                switch(tipoActo){
                                    case 62:  //CANCELACIONES
                                        Set<Integer> order=new  HashSet();
                                        Set<Modulo> modulos=new  HashSet();
                                        List<ModuloTipoActo> mtas= null;
                                        String reconstruyeActo="";
                                        StringBuilder stringBuilder= new StringBuilder();
                                        Map<Long, String> campoValorMap = new HashMap<Long, String>();
                                        Set<ActoPredioCampo> acpsAux=new HashSet();
                                        ArrayList<ActoPredioCampo> apcRecolectados = new ArrayList<ActoPredioCampo>();
        
                                        apcRecolectados=getActoPredioCampo(acto);
        
                                        for (ActoPredioCampo apc : apcRecolectados) {
                                            modulos.add(apc.getModuloCampo().getModulo());  
                                        } 
                                        
                                        if(modulos!=null && modulos.size()>0)
                                        	mtas = moduloTipoActoRepository.findAllByModuloInOrderByOrdenAsc(modulos, acto.getTipoActo().getId());
                                        else{
                                        	mtas = new ArrayList<>();
                                        }
                                    /*  for (ActoPredioCampo apc : actoPredioCampos) {
                                            System.out.println("apcID:{} "+ apc);
                                        }  */
        
                                        for (ActoPredioCampo apc : apcRecolectados) {                                       
                                            Long moduloId=apc.getModuloCampo().getModulo().getId();                                         
                                            if(moduloId==743L || moduloId==155L){//modulo cancelacion total
                                                order.add(apc.getOrden());                                                                             
                                            }//fin (modulo cancelacion total)                                                                                     
                                        }
                                        
                                        System.out.println("construlleActo ");                      
                                        reconstruyeActo=this.construlleActo(apcRecolectados,order); 
        
                                /*        for (ActoPredioCampo apc : apcRecolectados) {
                                            if(apc.getModuloCampo().getId()!=20761L && apc.getModuloCampo().getId()!=460L){
                                                String val=modoDeImpresion(apc);
                                            //  apc.setValor(val);
                                            }
                                            
                                        } */
                                
                                        if(mtas!=null && mtas.size()>0) {
                                        for(ModuloTipoActo mta:mtas){
                                            stringBuilder.append("\n"+"\n"+mta.getEtiqueta()+"\n");
                                            Long ordenId=mta.getModulo().getId();
                                            if(ordenId==743L  || ordenId==155L){
                                                stringBuilder.append(reconstruyeActo);
                                            }else{
                                                for (ActoPredioCampo apc :apcRecolectados) {
                                                    if(apc.getModuloCampo().getModulo().getId()==ordenId){
                                                        if(apc.getModuloCampo().getId()!=20761L && apc.getModuloCampo().getId()!=460L){
                                                            stringBuilder.append(modoDeImpresion(apc));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        }
                                        System.out.println(stringBuilder.toString());
                                        pvo.setObservacionesCancelacion(stringBuilder.toString());
        //__________________________________________________________________________________________________________
                                        ArrayList<Acto> actosRecolectados = new ArrayList<Acto>();
                                        apcRecolectados = new ArrayList<ActoPredioCampo>();
                                        ArrayList<ActoModulo> actoModulos = new  ArrayList<ActoModulo>();
                                        Set<Modulo> modulosRecolectados=new  HashSet();
                                        stringBuilder= new StringBuilder();
                                        String check="";
                                        Long idActo=-1l;
                                    /*  for (ActoPredioCampo apc : actoPredioCampos) {
                                            System.out.println("apcID:{} "+ apc);
                                        }  */
                                        if(mtas!=null && mtas.size()>0) {
                                        for(ModuloTipoActo mta:mtas){
                                            Long ordenId=mta.getModulo().getId();
                                            if(ordenId==743L){//cancelacion total
                                            for (ActoPredioCampo apc : actoPredioCampos) {
                                                Long moduloCampoId=apc.getModuloCampo().getId();
                                                    if(apc.getModuloCampo().getModulo().getId()==ordenId){//por orden                                               
                                                        if(moduloCampoId==20762L){
                                                            check=apc.getValor();
                                                        }
                                                        if(moduloCampoId==20761L){
                                                            idActo = Long.parseLong(apc.getValor());
                                                        }
                                                        if(idActo!=-1L&&!check.isEmpty()){
                                                        
        
                                                            Acto actoRecuperado = actoRepository.findOne(idActo);
                                                            actosRecolectados.add(actoRecuperado);
                                                            
                                                            ArrayList<ActoPredioCampo> apcRe=getActoPredioCampo(actoRecuperado);
                                                            for (ActoPredioCampo apcAux :apcRe){
                                                                apcRecolectados.add(apcAux); 
                                                            } 
                                                            check="";
                                                            idActo=-1l;                                                
                                                        }
                                                    }
                                                }
                                            } 
                                            if(ordenId==155L){//cancelacion parcial
                                                for (ActoPredioCampo apc1 : actoPredioCampos) {
                                                    Long moduloCampoId=apc1.getModuloCampo().getId();
                                                    // System.out.println("apcID:{} "+ apc1);
                                                    if(apc1.getModuloCampo().getModulo().getId()==ordenId){//por orden                                               
                                                        if(moduloCampoId==461L){
                                                            check=apc1.getValor();
                                                        }
                                                        if(moduloCampoId==460L){                                               
                                                            idActo = Long.parseLong(apc1.getValor());
                                                        }
                                                        if(idActo!=-1L&&!check.isEmpty()){
                                                        
        
                                                            Acto actoRecuperado = actoRepository.findOne(idActo);
                                                            actosRecolectados.add(actoRecuperado);
                                                            
                                                            ArrayList<ActoPredioCampo> apcRe=getActoPredioCampo(actoRecuperado);
                                                            for (ActoPredioCampo apcAux :apcRe){
                                                                apcRecolectados.add(apcAux); 
                                                            } 
                                                            check="";
                                                            idActo=-1l;                                                
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                      }
                                    for (Acto acAux : actosRecolectados) { 
                                        //System.out.println("acto: "+ acAux.getTipoActo());
                                        for (ActoPredioCampo apcAux : apcRecolectados) {
                                            Long id=apcAux.getActoPredio().getActo().getId();
                                            if(acAux.getId()==id){
                                                
                                            modulosRecolectados.add( apcAux.getModuloCampo().getModulo());
                                            }
                                        }                               
                                        //System.out.println("modulosRecolectadosSize: "+ modulosRecolectados.size());
                                        if(modulosRecolectados!=null && modulosRecolectados.size()>0  ) {
                                        	List<ModuloTipoActo> mtasRecolectados= moduloTipoActoRepository.findAllByModuloInOrderByOrdenAsc(modulosRecolectados,acAux.getTipoActo().getId());
                                            modulosRecolectados=new  HashSet();
                                            //System.out.println("mtas: "+ mtasRecolectados.size());
                                            for(ModuloTipoActo mta:mtasRecolectados){
                                            ActoModulo am= new ActoModulo();
                                            am.setActo(acAux);
                                            am.setModuloTipoActo(mta);
                                            actoModulos.add(am);
                                            }
                                        }
                                        
                                    }
                                    /*  for(ActoModulo am:actoModulos){
                                        System.out.println("acto: "+am.getActo().getId()+" tipo Acto id: "+am.getActo().getTipoActo().getId()+" mta id: "+am.getModuloTipoActo().getId()); 
                                    } */
                                    boolean buscarFechaRegistro=true;
                                    boolean imprimir=false;
                                    for(ModuloTipoActo mta:mtas){
                                        Long ordenId=mta.getModulo().getId();
                                        //System.out.println("orden "+ordenId);
                                            if(ordenId==743L || ordenId==155L){
                                                //System.out.println("entro 155 ");
                                                for (ActoPredioCampo apc : actoPredioCampos) {
                                                    Long moduloCampoId=apc.getModuloCampo().getId();
                                                
                                                    if(apc.getModuloCampo().getModulo().getId()==ordenId){//por orden                                              
                                                    // System.out.println("moduloCampoId==20761L || moduloCampoId==460L "+moduloCampoId);
                                                        if(moduloCampoId==20761L || moduloCampoId==460L){
                                                            Acto actoR = actoRepository.findOne(Long.parseLong(apc.getValor()));                                                    
                                                            System.out.println("actoR "+actoR);
                                                            for(ActoModulo am:actoModulos){
                                                                if(apc.getValor().equals(am.getActo().getId().toString()) && !imprimir){//compara actos         
                                                                    imprimir=true;
                                                                }
                                                            }
                                                            if(imprimir){
                                                                stringBuilder.append("\n\nACTO CANCELADO: "+actoR.getTipoActo().getNombre()+"\n");
                                                                for(ActoModulo am:actoModulos){
                                                                    if(apc.getValor().equals(am.getActo().getId().toString())){//compara actos         
                                                                    //encabezados
                                                                    Integer imprimirEncabezdo=am.getModuloTipoActo().getInd_impresion();
                                                                    //System.out.println("imprimirEncabezdo "+imprimirEncabezdo);
                                                                    if(imprimirEncabezdo == null|| imprimirEncabezdo==1){
                                                                        String encabezado=am.getModuloTipoActo().getEtiqueta();
                                                                        stringBuilder.append("\n "+encabezado+" \n");
                                                                    }else{
                                                                        stringBuilder.append("\n ");
                                                                    }
                                                                    
                                                                    //llenado de campos
                                                                    ArrayList<ActoPredioCampo> apcRe=getActoPredioCampo(am.getActo());
                                                                        for (ActoPredioCampo apcAux :apcRe){
                                                                            if(apcAux.getModuloCampo().getModulo().getId().compareTo(am.getModuloTipoActo().getModulo().getId())==0){
                                                                                if(am.getModuloTipoActo().getEtiqueta().equalsIgnoreCase("FECHA DE INSCRIPCION")){
                                                                                    if(am.getActo().getFechaRegistro()!=null){
                                                                                        if(buscarFechaRegistro){
                                                                                            stringBuilder.append( new SimpleDateFormat("dd-MM-yyyy").format(am.getActo().getFechaRegistro()));
                                                                                            buscarFechaRegistro=false;  
                                                                                        }
                                                                                        
                                                                                    }else{
                                                                                        stringBuilder.append(modoDeImpresion(apcAux));
                                                                                    }
                                                                                 
                                                                                }else{
                                                                                    stringBuilder.append(modoDeImpresion(apcAux));
                                                                                }                                                                                
                                                                            }                                                                
                                                                        }                                                                                                   
                                                                    }
                                                                }
                                                                imprimir=false; 
                                                            }                                                      
                                                        }
                                                    }
                                                }
                                            }
                                            buscarFechaRegistro=true;
                                        }
                                        System.out.println(stringBuilder.toString());
                                        pvo.setObservaciones(stringBuilder.toString());
                                        break;
                                    case 41://PREDIO FUSIONADO
                      
                                        buscarDireccion=false;
                                        List<PredioRel> predioRels=predioRelRepository.findByActoId(acto.getId());
                                        String folioPredioFusion="";
                                        List<TitularModel> titulares = new ArrayList<TitularModel>();
                                        
                                        for(PredioRel pr:predioRels){

                                            if(folioPredioFusion.isEmpty()){
                                                if(pr.getPredioSig().getNoFolio()!=null){
                                                    folioPredioFusion=""+pr.getPredioSig().getNoFolio();
                                                }                                               
                                                else{
                                                    folioPredioFusion="(PENDIENTE DE GENERAR) ";
                                                }
                                                
                                                     
                                                pvo.setUbicacion(pr.getPredioSig().getDireccionComplete());
                                            }
                                            
                                         
                                            titulares.addAll(this.buildTitulares(pr.getPredio(),true));
                                             
                                            sb.append(" "+pr.getPredio().getNoFolio()+"\n");
                                        }
                                        buscarTextoRegistro = false;
                                        pvo.setFolioResultanteMaster(folioPredioFusion);
                                        pvo.setFoliosFusionadosMaster(sb.toString());
                                        pvo.setTitulares(titulares);
                                    break;
                                    
                                    case 42:  // subdivision                                
                                    case 40:  // Fraccionamiento
                                    case 39:  // Condominio
                                    case 245: // Lotificacion
                                    case 246: // Relotificacion

                                        System.out.println("Subdivision/Fraccionamiento ");
                                        List<PredioRel> predioRels2=predioRelRepository.findByActoId(acto.getId());
                                        buscarDireccion=false;
                                        Predio predioPadre = null;
                                        ArrayList<Predio>prediosFraccionados= new ArrayList<Predio>();
                             
                                            for(PredioRel pr:predioRels2){
                                                if(predioPadre==null){
                                                    predioPadre=pr.getPredio();
                                                    }
                                                prediosFraccionados.add(pr.getPredioSig());
                                            }
                                                                    
        
                                        //datos de predios fraccionados
                                        StringBuilder foliosFraccionados = new StringBuilder();
                                        ArrayList<ResultantesModel> resultantesModels=new  ArrayList<ResultantesModel>();
                                        log.info("prediosFraccionados.size()"+prediosFraccionados.size());
                                        for(Predio pF:prediosFraccionados){
                                            ResultantesModel resultantesModel= new ResultantesModel();
                                            String u = pF.getDireccionComplete();
                                            if(null != pF.getNoFolio()){
                                                String ff =""+pF.getNoFolio();
                                                resultantesModel.setUbicacionResultante(ff+" "+ u+"\n\n");
                                            }else{
                                                resultantesModel.setUbicacionResultante("(FOLIO PENDIENTE DE GENERAR)"+ u+"\n\n");
                                            }
                                            resultantesModels.add(resultantesModel);
                                        }                    
                                            //ANTECEDENTES
                                            pvo.setAntecedentes(null);
                                            pvo.setResultantes(resultantesModels);
                                            //buscar doc fundatorio                            
                                            buscarTextoRegistro = false;
                                            //pvo.setTextoRegistro (getFundatorioActoSubdivision(acto));                                    
                                        
                                          if(foliosFraccionados!=null)
                                        	  pvo.setFolioResultanteMaster(""+foliosFraccionados.toString());
                                          if(predioPadre != null)
                                        	  pvo.setFoliosFusionadosMaster(""+predioPadre.getNoFolio());
                                    break;                                                                
                                    case 66: //DEMANDAS Y/O NOTIFICACIONES DE JUICIOS DE AMPARO
                                        actoPredioCampos=getActoPredioCampo(acto);
                                        pvo.setNaturalezaProcedencia(" ");
                                        for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                            ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                            int id_modulo_campo =  moduloCampo.getId().intValue();
        
                                            switch (id_modulo_campo) {
                                                case 20137: // numExpediente
                                                    pvo.setNumExpediente(actoPredioCampo.getValor()); 
                                                break;
                                                case 20194: // juzgadoTribunal
                                                    pvo.setJuzgadoTribunal(actoPredioCampo.getValor()); 
                                                break;
                                                case 20775: // tipoInforme                                                
                                                    pvo.setTipoInforme(campoValoresRepository.findById(new Long(actoPredioCampo.getValor())).getNombre());
                                                    pvo.setObservaciones(campoValoresRepository.findById(new Long(actoPredioCampo.getValor())).getNombre());
                                                break;
                                                case 20776: //fechaTermino
                                                    pvo.setFechaTermino(actoPredioCampo.getValor());    
                                                break;
                                                case 20777: // naturalezaProcedencia
                                                    log.debug("aqui1 orden7472 ");
                                                    naturalezaProcedencia.add(actoPredioCampo);
                                                    //orden.add(aux3, actoPredioCampo.getValor());                                            	
                                                    //pvo.setNaturalezaProcedencia(aux1+aux2);
                                                    
                                                break;
                                                
                                            }         
        
                                        }
                                        Collections.sort(naturalezaProcedencia);///se ordena gracias a la interfaz implementada (Comparable)
                                        for(ActoPredioCampo naturalezaProcedencia1 : naturalezaProcedencia) 
                                        {
                                            pvo.setNaturalezaProcedencia(pvo.getNaturalezaProcedencia()+naturalezaProcedencia1.getValor());
                                        }
                                        //log.debug("orden7472 {}",orden);
                                        
                                        log.debug("aqui2 orden7472 ");
                                    break;////aqui acaba el cae del 66
                                    case 233:   // ANOTACIÓN
                                        actoPredioCampos=getActoPredioCampo(acto);                                
                                        for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                            ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                            int id_modulo_campo =  moduloCampo.getId().intValue();
        
                                            switch (id_modulo_campo) {
                                                case 431: // OBSERVACIONES
                                                    pvo.setObservaciones(actoPredioCampo.getValor()); 
                                                break;
                                            }
                                        }
                                    break;
                                    case 121://retencion
                                    String proN="";
                                        actoPredioCampos=getActoPredioCampo(acto);                                
                                        for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                            ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                            int id_modulo_campo =  moduloCampo.getId().intValue();
        
                                            switch (id_modulo_campo) {
        
                                                case 273: // nombre
                                                    proN+=" "+actoPredioCampo.getValor();
                                                break;
                                                case 186: // paterno
                                                    proN+=" "+actoPredioCampo.getValor();
                                                break;
                                                case 794: // materno
                                                    proN+=" "+actoPredioCampo.getValor();
                                                break;
                                            }
                                        }
                                        pvo.setAFavorDe(proN);
                                    break;
                                    case 50: //SEGUNDO AVISO
                                     StringBuilder afavorDe = new  StringBuilder("A FAVOR DE: ");
                                     StringBuilder actosAviso = new  StringBuilder("ACTOS A INSCRIBIR: ");
                                     HashMap<Integer,Persona> contratantes =  new HashMap<Integer,Persona>();
                                     actoPredioCampos=getActoPredioCampo(acto);                                
                                     for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                    	 Long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
                                    	 int index = actoPredioCampo.getOrden();
                                    	 
                                    	 if(campoId == 286 || campoId==287 || campoId== 288) {
                                    		 Persona  persona=  contratantes.get(index);
                                    		 if(persona == null) {
                                    			 persona =  new Persona();
                                    			 contratantes.put(index,persona);
                                    		 }
                                    		 if(campoId == 286) {
                                    			 persona.setNombre(actoPredioCampo.getValor());
                                    		 }
                                    		 if(campoId == 287) {
                                    			 persona.setPaterno(actoPredioCampo.getValor());
                                    		 }
                                    		 if(campoId == 288) {
                                    			 persona.setMaterno(actoPredioCampo.getValor());
                                    		 }
                                    	 }
                                    	 
                                    	 if(campoId == 389 || campoId == 521 || campoId == 524 || campoId == 523  || campoId == 525) {
                                    		 if(actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().trim().isEmpty() ) {
                                    			 CampoValores cv = campoValoresRepository.findById(Long.parseLong(actoPredioCampo.getValor()));
                                    			 if(cv!=null)
                                    				 actosAviso.append(cv.getNombre()).append(", ");
                                    		 }
                                    	 }
                                    	 
                                     }
                                     contratantes.forEach((index,persona)->{
                                    	 if(index>1) {
                                    		 afavorDe.append(", ");	 
                                    	 }
                                    	 afavorDe.append(persona.getNombre());
                                    	 afavorDe.append(persona.getPaterno()!=null ? " "+persona.getPaterno() : "");
                                    	 afavorDe.append(persona.getMaterno()!=null ? " "+persona.getMaterno() : "");
                                    	  
                                     });
                                     
                                     
                                     pvo.setAFavorDe(afavorDe.append("\n").append(actosAviso.toString()).toString());
                                     
                                 
                                    break;
                                    case 70: //RECURSO DE INCONFORMIDAD
                                    case 249:
                                    	 StringBuilder recurrente = new  StringBuilder("RECURRENTE(S): ");
                                    	 StringBuilder strRecurso =  new StringBuilder();
                                         HashMap<Integer,Persona> rrecurentes =  new HashMap<Integer,Persona>();
                                         actoPredioCampos=getActoPredioCampo(acto);
                                         
                                         for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                        	 Long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
                                        	 Long moduloId  =  actoPredioCampo.getModuloCampo().getModulo().getId();
                                        	 int index = actoPredioCampo.getOrden();
                                        	 
                                        	 if(moduloId == 231 && (campoId == 1286 || campoId==1287 || campoId==1288)) {
                                        		 Persona  persona=  rrecurentes.get(index);
                                        		 if(persona == null) {
                                        			 persona =  new Persona();
                                        			 rrecurentes.put(index,persona);
                                        		 }	
                                        		 if(campoId == 1286) {
                                        			 persona.setNombre(actoPredioCampo.getValor());
                                        		 }
                                        		 if(campoId == 1287) {
                                        			 persona.setPaterno(actoPredioCampo.getValor());
                                        		 }
                                        		 if(campoId == 1288) {
                                        			 persona.setMaterno(actoPredioCampo.getValor());
                                        		 }
                                        	 }	 
                                         }
                                         rrecurentes.forEach((index,persona)->{
                                        	 if(index>1) {
                                        		 recurrente.append(", ");	 
                                        	 }
                                        	 recurrente.append(persona.getNombre());
                                        	 recurrente.append(persona.getPaterno()!=null ? " "+persona.getPaterno() : "");
                                        	 recurrente.append(persona.getMaterno()!=null ? " "+persona.getMaterno() : "");
                                        	  
                                         });
                                         
                                         //OBTIENE RECURSO SE INCONFORMIDAD
                                         Optional<RecursoInconformidad> recurso =  recursoInconformidadRepository.findFirstByActoId(acto.getId());
                                         if(recurso.isPresent()) {
                                        	 if(recurso.get().getPrelacion() != null)
                                        		strRecurso.append("ENTRADA: ").append(recurso.get().getPrelacion().getConsecutivo()).append(" AÑO: "+recurso.get().getPrelacion().getAnio()).append(" \n\n");
                                        	 
                                        	 strRecurso.append("FUNDAMENTO: ").append(recurso.get().getFundamento());
                                        	 
                                         }
                                         
                                         pvo.setAFavorDe(recurrente.toString()+" \n\n"+strRecurso.toString());
                                    break;
                              
                                }//fin swicht   
                            }//fin if acto!=null
                        }
                
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////        
                        if(buscarDireccion){
                            sb=new StringBuilder();
                            String colindanciaObservacion = "";
                            if(predio!=null) {
                      
                                int noFolio = predio.getPredio().getNoFolio();
                                Predio predio1 = predioRepository.findByNoFolio(noFolio);
                                //log.debug("BRS401-ESTO ES PREDIO:{}", predio1);
                                Set<Colindancia> colindancias = colindanciaService.findColindancias(predioOk.getId());
                                log.debug("BRS403 ESTO ES colindancias{}", colindancias);
                                if(colindancias!=null){                                        
                                        for (Colindancia colindancia : colindancias) {                            
                                            if (colindancia.getOrientacion().getId() == 17L) {
                                                    colindanciaObservacion +="\n\nOBSERVACIONES:\n"+colindancia.getNombre()+"\n";
                                        }else {
                                        colindanciaObservacion+="\n"+ colindancia.getOrientacion().getNombre()+" "+ colindancia.getNombre();                         
                                        }
                                        
                                    }
                                }
                                    sb.append(predio1.getDireccionComplete() + "\n" + colindanciaObservacion + "\n");
                                
                            }
                                pvo.setUbicacion(sb.toString());
                            }
                        log.debug(" pvo.getUbicacion()", pvo.getUbicacion());
                            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            //if(buscarTextoRegistro){//SE MODIFICO PARA PONERLO ABAJO DEL ACTO EN ACTOS-MODEL
                              //  pvo.setTextoRegistro (this.buildTextoRegistro(predio, pvo.getActos(), tipo)); // Busqueda de persona Notario y Acto
                            //}                
                            pvo.setTextoConsta(this.getTextoConsta(prelacion.getId(), tipo) );
                
                            //pvo.setAcreedores(this.buildAcreedores(predio, tipo));
                
                            //pvo.setRegistrador(this.buildUsuario( prelacion.getUsuarioVentanilla() ));
                            
                            List<ActoFirma> aFi = actoFirmaRepository.findAllByActoIdAndEsActoOrderByIdDesc(acto.getId(),true);
                            if( aFi != null && aFi.size()>0 ) {
                            	pvo.setRegistrador(this.buildUsuario( aFi.get(0).getUsuario() ));
                            }else {
                            	pvo.setRegistrador(this.buildUsuario( prelacion.getUsuarioAnalista()));
                            }
                
                            pvo.setCoordinador(this.buildUsuario( prelacion.getUsuarioCoordinador()) );
                
                            log.info("IHM REVISA FUNDATORIO: "+pvo.getTextoRegistro());
            
                
                            // FIRMA ELECTRONICA REGISTRADOR
                            pvo.setFirmaRegistrador(this.buildStringFirmaActos(acto));
                
                            // FIRMA ELECTRONICA COORDINADOR
                            String firmaCoord = "-SIN FIRMA -";
                            System.out.println("AQUI BRS 334 "+prelacion.getFirma());
                            if (prelacion.getFirma() != null )
                                firmaCoord = prelacion.getFirma() + "\r\n";           
                            pvo.setFirmaCoordinador(firmaCoord);
                                         
                            pvos.add(pvo);
                            Boolean digitaliza=prelacionRepository.findOne(prelacionId).getOficina().getReq_digitalizar();
                            if(save && digitaliza) {
                            	try 
                            	{
									this.empaquetarActo(acto.getId(), pvo, acto.getPrelacion().getConsecutivo().toString(), pagina);
									System.out.println("ACTO EMPAQUETADO "+acto.getId());
								} catch (Exception e) {
									System.out.println("NO SE EMPAQUETO ACTO "+acto.getId());
								}
                            }
                            
                            pvo = this.getBRHeader (prelacion);

                      
                    }// fin  for (PrelacionPredio predio: predios )
                 
                }//fin for
            System.out.println("TERMINA PAGINA "+pagina);
          
        }         
     else{
            List<Acto> actos = actoRepository.findAllByPrelacionIdAndVfFalseOrderByOrdenAsc(prelacion.getId(),false); 
            StringBuilder doc = new StringBuilder(); 
           
            for(Acto acto2 : actos) {
                    if(acto2!=null && ( acto2.getStatusActo().getId()!=5L && acto2.getStatusActo().getId()!=6L)){                     
                        ArrayList<ActoPredioCampo> actoPredioCampos = new ArrayList();
                        int tipoActo = acto2.getTipoActo().getId().intValue();
                        System.out.println("TIPO DE ACTO 2 "+tipoActo+" id "+acto2.getId());
                        String obs=null;
                        ActoModel am=new ActoModel();
                        am.setNombre(acto2.getTipoActo().getNombre());
                        am.setActo(acto2);
                        am.setAsiento(buildAsiento(acto2.getFechaRegistro()));
                        ArrayList<ActoModel> actoModels = new ArrayList();
                        actoModels.add(am);
                        pvo = this.getBRHeader (prelacion);
                        pvo.setActos(actoModels);
                      //numero
                        ActoPublicitario ap=null;
                        switch(tipoActo){
                            case 54:  // declaracion de hdos
                            case 242: // LECTURA DE TESTAMENTO
                            case 56:  // CESION DE DERECHOS HEREDITARIOS
                            case 57:  //NOMBRAMIENTO DE ALBACEA
                        
                                    System.out.println("ACTO 54");
                                     sb = new StringBuilder();
                                    StringBuilder ant = new StringBuilder();                              
                                    Set<String> beneficiarios=new HashSet<>();
                                    Set<String> herederos=new HashSet<>();
                                    String nombre=null;String aPaterno=null; 
                                    String aMaterno=null; String rfc =null;
                                    String curp =null; String hNombre=null;String hPaterno=null; 
                                    String hMaterno=null;
                                    String nombraOCambio="";
                                    buscarTextoRegistro=false;    
                                    Boolean TextoRegistro=false; 
                                    String tipoDeDocumento="",autoridad="";                                   
                                    actoPredioCampos=getActoPredioCampo(acto2);
                                    obs="";
                                    
                                    for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                        ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                        int id_modulo_campo =  moduloCampo.getId().intValue();
                                        int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                                        ///////////////////////////albacea
                                        if (id_modulo_campo == 20742 || id_modulo_campo == 925) {// nombre
                                            nombre = actoPredioCampo.getValor() + " ";
                                        }
                                        if (id_modulo_campo == 20743 || id_modulo_campo == 926) {// primer apellido
                                            aPaterno= actoPredioCampo.getValor() + " ";
                                        }
                                        if (id_modulo_campo == 20744 || id_modulo_campo == 927) {// segundo apellido
                                            aMaterno= actoPredioCampo.getValor() + " ";
                                        }
                                        if (id_modulo_campo == 431 ){
                                            // OBSERVACIONES
                                            obs=actoPredioCampo.getValor();  
                                        }
                                        if (id_modulo_campo == 20833 ){
                                            // NOMBRAMIENTO O CAMBIO ALBACEA
                                            nombraOCambio=this.obtenerValor(actoPredioCampo);
                                        }
                                        switch(moduloId){
                                            case 1501:
                                                autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                                            break;
                                            case 501:
                                                tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                                            break;
                                        }
                                        //doc fund
                                        
                                       
                                        if(nombre!=null && aPaterno!=null && aMaterno!=null){
                                            beneficiarios.add(""+nombre+aPaterno+aMaterno+"\n");	
                                            nombre=null;aPaterno=null;aMaterno=null; rfc =null; curp =null;
                                        }		
                                                                               
                                    }
                                    for(String b:beneficiarios){
                                        sb.append(b);
                                    }	
                                    String albacea=sb.toString();
                                    System.out.println("albacea "+albacea);
                                    pvo.setTextoRegistro(this.quitarUltimoCaracter(tipoDeDocumento)+"\n\n"+"AUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                                    pvo.setUbicacion(tipoActo==54?"DECLARACION DE HEREDEROS":"LECTURA DE TESTAMENTO");
                                    
                                    if(tipoActo==57){
                                       
                                        pvo.setUbicacion(acto2.getTipoActo().getNombre());
                                    }
                                    if(!nombraOCambio.isEmpty()){
                                    	 am=new ActoModel();
                                         am.setNombre(nombraOCambio);
                                         am.setActo(acto2);
                                         am.setAsiento(buildAsiento(acto2.getFechaRegistro()));
                                         actoModels = new ArrayList();
                                         actoModels.add(am);
                                         pvo.setActos(actoModels);                                      
                                    }
                                    pvo.setObservaciones(obs);	                                

                                   /*  sb = new StringBuilder();
                                    for(String b:herederos){                  
                                        sb.append(b);
                                    }*/
                                    pvo.setAFavorDe(albacea);
                                    //numero
                                    ap=actoPublicitarioRepository.findByLastNumero(acto2.getId());
                                    
                                    if(null!=ap) {
                                    	pvo.setInscritoEn(""+ap.getNumero());
                                    }else {
                                    	pvo.setInscritoEn("");
                                    }
                                    System.out.println("pvo "+pvo.toString());
                                    pvos.add(pvo);
                            break;
                            case 132://REVOCACIÓN DE TESTAMENTO
                            case 133://RETIRO DE TESTAMENTO
                            case 134://deposito testamento
                                tipoDeDocumento="";autoridad="";
                                String tNombre="";
                                String tAM="";
                                String tAP="";
                                String sol="";
                                
                                String tsNombre=null,tsAP=null,tsAM=null;  
                                obs="";
                                Set<String> testigos=new HashSet<>();
                                String documento="";String notario="";String nEscritura="";String fecha="";
                                actoPredioCampos=getActoPredioCampo(acto2);
                                //testador
                                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                    int id_modulo_campo =  moduloCampo.getId().intValue();
                                    if (id_modulo_campo == 561) {// nombreTesteador
                                        tNombre= actoPredioCampo.getValor()+" ";                                       
                                    }   
                                    if (id_modulo_campo == 562) {// apMaTesteador 111
                                       tAP = actoPredioCampo.getValor()+" ";
                                    }
                                    if (id_modulo_campo == 563) {// aMMaTesteador
                                       tAM = actoPredioCampo.getValor()+" ";
                                    }
                                    if (id_modulo_campo == 431 ){
                                        // OBSERVACIONES
                                        obs=actoPredioCampo.getValor();  
                                    }
                                }
                                pvo.setObservaciones(obs);
                                                             
                                System.out.println("acto "+acto2.getId());
                                ap=actoPublicitarioRepository.findByLastNumero(acto2.getId());
                            	System.out.println("acto publicitario "+ap);
                                if(null!=ap) {
                                   pvo.setInscritoEn(""+ap.getNumero());
                                }else {
                                	pvo.setInscritoEn("");
                                }
                                //doc fundatorio
                                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                    int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                                    switch(moduloId){
                                        case 1501:
                                            autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                                        break;
                                        case 501:
                                            tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                                        break;
                                    }        
                                }
                                if(acto2.getTipoActo().getId()==132L || acto2.getTipoActo().getId()==133L) {

                                    for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                        int moduloId=actoPredioCampo.getModuloCampo().getId().intValue();
                                        switch(moduloId){
                                            case 252:
                                                sol+="\n"+this.obtenerValor(actoPredioCampo)+" ";
                                            break;
                                            case 253:
                                            	sol+=" "+this.obtenerValor(actoPredioCampo)+" ";
                                            break;
                                            case 775:
                                            	sol+=" "+this.obtenerValor(actoPredioCampo)+" ";
                                            break;
                                        }        
                                    }
                                    pvo.setAFavorDe("A FAVOR DE:"+sol);
                                }
                                
                                else {
                                	  pvo.setAFavorDe(tNombre+tAP+tAM);

                                }
                                pvo.setTextoRegistro(this.quitarUltimoCaracter(tipoDeDocumento)+"\n\n"+"AUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                                pvos.add(pvo);
                            break;
                            case 76://INFORMES DE DISPOSICION TESTAMENTARIA
                                    String texto1="",autoridad76="",representanteAutoridad76="",numeroOficio="",fecha76="",aBienes=""
                                    ,juicioS="",solicitante="",nombre76="",paterno76="",materno76="",numExpediente="";obs="";
                                    tipoDeDocumento="";autoridad="";
                                    Boolean testamento=false;
                                    actoPredioCampos=getActoPredioCampo(acto2);
                                    //testador
                                    for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                        ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                        int id_modulo_campo =  moduloCampo.getId().intValue();
                                        switch (id_modulo_campo) {
                                        case 20779:
                                            CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));juicioS=cv.getNombre();
                                            break;                             
                                        case 20152:
                                            representanteAutoridad76=actoPredioCampo.getValor();
                                            break;
                                        case 20154:
                                            autoridad76=actoPredioCampo.getValor();
                                            break;
                                        case 20725:
                                            numeroOficio=actoPredioCampo.getValor();
                                            break;
                                        case 206:
                                            fecha76=actoPredioCampo.getValor();
                                            break;								
                                        case 431:
                                            aBienes=actoPredioCampo.getValor();
                                            break;
                                        case 252:
                                            nombre76=actoPredioCampo.getValor();
                                            break;
                                        case 253:
                                            paterno76=actoPredioCampo.getValor();
                                            break;
                                        case 775:
                                            materno76=actoPredioCampo.getValor();
                                            break;
                                        case 20137:
                                            numExpediente=actoPredioCampo.getValor();
                                            break;
                                        case 20778:
                                            testamento=Boolean.valueOf(actoPredioCampo.getValor());
                                            break;
                                        case 20194: 
                                            obs=actoPredioCampo.getValor();
                                        break;
                                        }
                                        int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                                        switch(moduloId){
                                            case 1501:
                                                autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                                            break;
                                            case 501:
                                                tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                                            break;
                                        }        
                                    }////aqui se debe unir dg
                                    solicitante=nombre76+" "+paterno76+" "+materno76;
                                    String aux76="NO";
                                    if(testamento) {aux76="SI";}
                                    texto1="EN ATENCIÓN A SU OFICIO "+numeroOficio+" DE FECHA "+fecha76+" DEDUCIDO DEL JUICIO SUCESORIO "+juicioS+" A BIENES DE "+aBienes+" PROMOVIDO POR "+solicitante+", EXP. "+numExpediente
                                            +"\n\n\n"+"ME PERMITO INFORMAR A USTED QUE "+aux76+" SE ENCONTRÓ REGISTRADO EN ESTA INSTITUCIÓN TESTAMENTO A NOMBRE DE "+aBienes+".";
                                    pvo.setAFavorDe(texto1);
                                    pvo.setObservaciones(" "+this.quitarUltimoCaracter(tipoDeDocumento)+"\n\nAUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad)+"\n\n"+obs);
                                 
                                    pvo.setInscritoEn("");
                                    
                                    pvos.add(pvo);
                            break;
                            case 244://RESOLUCIÓN ADMINISTRATIVA PARA AUTORIZACIÓN DE ACTO MODIFICATORIO DEL INMUEBLE
                            	
                                String texto244="",solicitante244="",nombre244="",paterno244="",materno244="",TIPO_DE_ACTO_MODIFICATORIO="",LOTES="",
                                		MANZANAS="",
                                		DENOMINACION="",
                                		USO_DE_SUELO="",
                                		SUPERFICIE="",
                                		UNIDAD_DE_MEDIDA="",
                                		DATOS_RESLUCION="";
                                        obs="";
                                
                                pvo.setSolicitante(getUsuarioSolicitan(acto2.getPrelacion().getUsuarioSolicitan(), acto2.getPrelacion().getId()));
                                
                                actoPredioCampos=getActoPredioCampo(acto2);
                                //testador
                                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                    int id_modulo_campo =  moduloCampo.getId().intValue();
                                    switch (id_modulo_campo) {                         
                                    case 843:
                                        nombre244=actoPredioCampo.getValor();
                                        break;
                                    case 844:
                                        paterno244=actoPredioCampo.getValor();
                                        break;
                                    case 845:
                                        materno244=actoPredioCampo.getValor();
                                        break;
                                    case 20789:
                                    	CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor())); TIPO_DE_ACTO_MODIFICATORIO=cv.getNombre();
                                    	break;
                                    case 20781:
                                    	DENOMINACION=actoPredioCampo.getValor();
                                        break;                                        
                                    case 20782:
                                    	LOTES=actoPredioCampo.getValor();
                                        break;
                                    case 20783:
                                    	MANZANAS=actoPredioCampo.getValor();
                                        break;                                        
                                    case 20784:
                                    	CampoValores cv3=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor())); USO_DE_SUELO=cv3.getNombre(); 
                                        break;
                                    case 20785:
                                    	SUPERFICIE=actoPredioCampo.getValor();
                                        break;
                                    case 20786:
                                    	CampoValores cv2=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor())); UNIDAD_DE_MEDIDA=cv2.getNombre();                                   
                                        break;
                                    case 20787:
                                    	DATOS_RESLUCION+= ", DATOS DE LA RESOLUCIÓN: "+actoPredioCampo.getValor();                               
                                        break;

                                    case 5021:
                                        DATOS_RESLUCION+="NUMERO DE AUTORIZACION: "+actoPredioCampo.getValor();
                                    break;

                                    case 5022:
                                        DATOS_RESLUCION+=", FECHA DE AUTORIZACIÓN: "+this.obtenerValor(actoPredioCampo);
                                    break;

                                    case 5023:
                                        DATOS_RESLUCION+=", CON LA COMPARECENCIA DE: "+actoPredioCampo.getValor();
                                    break;

                                    case 5024:
                                        DATOS_RESLUCION+=", AUTORIDAD QUE OTORGA: "+actoPredioCampo.getValor();
                                    break;
                                    case 431:
                                    // OBSERVACIONES
                                        obs=actoPredioCampo.getValor();  
                                    break;
                                    default:
                                        break;
                                    }
                                }
                                tipoDeDocumento="";autoridad="";
                              //doc fundatorio
                              for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                              	int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                              	switch(moduloId){
                              		case 1501:
                              			autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                              		break;
                              		case 501:
                              			tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                              		break;
                              	}        
                              }
                                
                                solicitante244=nombre244+" "+paterno244+" "+materno244;
                               // solicitante244=solicitante244.replace(null, " ");
                              //aqui va ir los campo del predio  texto1=""+solicitante244+"";
                        	//	Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
                		//		prelacion.setUsuarioSolicitan( (solicitante == null) ? usuario : solicitante);
                                am=new ActoModel();
                                am.setNombre(acto2.getTipoActo().getNombre());
                                am.setActo(acto2);
                                am.setAsiento(buildAsiento(acto2.getFechaRegistro()));
                                actoModels = new ArrayList();
                                actoModels.add(am);
                                pvo.setActos(actoModels);  
                        		if(obs!=null && !obs.isEmpty()){
                                    obs="\n\nOBSERVACIONES: \n\n"+obs;
                                }
                                pvo.setAFavorDe(solicitante244);
                                pvo.setObservaciones("TIPO DE ACTO MODIFICATORIO: "+TIPO_DE_ACTO_MODIFICATORIO+"\n\n DATOS DEL INMUEBLE\nDENOMINACIÓN: "+DENOMINACION+", LOTES: "+LOTES+", MANZANAS: "+MANZANAS+", USO DE SUELO: "+USO_DE_SUELO
                                +", SUPERFICIE: "+SUPERFICIE+" "+UNIDAD_DE_MEDIDA+"\n\n\n "+DATOS_RESLUCION+"\n\n"+this.quitarUltimoCaracter(tipoDeDocumento)+"\n\nAUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                                ap=actoPublicitarioRepository.findByLastNumero(acto2.getId());
                                if(null!=ap) {
                                    pvo.setInscritoEn(""+ap.getNumero());
                                }else {
                                    pvo.setInscritoEn("");
                                }
                                pvos.add(pvo);
                        break;
                        case 105://poder
                        case 106://modificacion de poder
                        case 107://revocacion de poder
                            actoPredioCampos=getActoPredioCampo(acto2);
                            autoridad="";
                            tipoDeDocumento="";
                            String apoderado="";
                            String tipoDePoder="";
                            String tipoDeRevocacionDePoder="";
                            String vigencia="";
                            String datosDelPoder="";
                            String datosDelPoderdante="";
                            String facultades="";
                            String tipoDeModificacion="";
                            String foliosRealAfectados = "";
                            boolean afectaFolio = false;
                           
                                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                    
                                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                    int id_modulo_campo =  moduloCampo.getId().intValue();
                                    int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                                    Long campoId =  actoPredioCampo.getModuloCampo().getCampo().getId();
                                    if (id_modulo_campo == 440) {// nombre
                                        apoderado+= actoPredioCampo.getValor()+" ";                                       
                                    }   
                                    if (id_modulo_campo == 441) {// ap
                                        apoderado+=" "+actoPredioCampo.getValor()+" ";
                                    }
                                    if (id_modulo_campo == 452) {// aM
                                        apoderado+=" "+actoPredioCampo.getValor()+" ";
                                    }
                                    if (id_modulo_campo == 1222) {//facultades
                                        facultades=obtenerValor(actoPredioCampo);
                                    }
                                    if (id_modulo_campo == 431 ){
                                        // OBSERVACIONES
                                        obs=actoPredioCampo.getValor();  
                                    }
                                    if (id_modulo_campo ==448 ){
                                        //tipo de poder
                                        tipoDePoder="TIPO(S) DE PODER (ES)\n "+this.obtenerValor(actoPredioCampo);  
                                    } 
                                    //vigencia
                                    if (id_modulo_campo ==449 ){
                                        //año
                                        vigencia+="\nVIGENCIA DE PODER(ES)\n"+actoPredioCampo.getValor()+" AÑO(S) ";  
                                    } 
                                    if (id_modulo_campo ==450 ){
                                        //mes
                                        vigencia+=actoPredioCampo.getValor()+" MES(ES) ";  
                                    } 
                                    if (id_modulo_campo ==451 ){
                                        //dias
                                        vigencia+=actoPredioCampo.getValor()+" DIA(S)"; 
                                    }
                                    //datos del poder
                                    if (id_modulo_campo ==62 ){
                                        //escritura
                                        datosDelPoder+="\nDATOS DEL PODER \n"+" ESCRITURA: "+this.obtenerValor(actoPredioCampo);  
                                    } 
                                    if (id_modulo_campo ==63){
                                        //bis
                                        datosDelPoder+=" BIS:"+this.obtenerValor(actoPredioCampo); 
                                    } 
                                    if (id_modulo_campo ==64 ){
                                        //fecha
                                        String fechaobtenida = stringToDateFormater(actoPredioCampo.getValor());
                                        datosDelPoder+=" FECHA:"+ fechaobtenida ;   
                                    }
                                    if (id_modulo_campo ==65 ){
                                        //notario
                                        datosDelPoder+=" NOTARIO:"+this.obtenerValor(actoPredioCampo);  
                                    }

                                    //datos del poderdante
                                    if (id_modulo_campo ==252 ){
                                        //nombre
                                        datosDelPoderdante+="\nDATOS DEL PODERDANTE \n"+" NOMBRE: "+actoPredioCampo.getValor();  
                                    } 
                                    if (id_modulo_campo ==253){
                                        //ap
                                        datosDelPoderdante+=" "+actoPredioCampo.getValor(); 
                                    } 
                                    if (id_modulo_campo ==775 ){
                                        //am
                                        datosDelPoderdante+=" "+actoPredioCampo.getValor();   
                                    }
                                    //tipo de modificacion de poderes
                                    if (id_modulo_campo ==20813 ){
                                        //tipo
                                        tipoDeModificacion = "TIPO MODIFICACION PODER(ES)\n"+obtenerValor(actoPredioCampo);
                                    }                                    
                                    if (id_modulo_campo ==20814 ){
                                        //motivo
                                        tipoDeModificacion+=", MOTIVO:"+ actoPredioCampo.getValor();
                                    }
                                    if (id_modulo_campo ==20815 ){
                                        //vigencia
                                        tipoDeModificacion+= ", VIGENCIA:"+obtenerValor(actoPredioCampo);
                                    } 
                                    //DATOS DE REVOCACIÓN
                                    if (id_modulo_campo ==442 ){
                                        //TIPO DE REVOCACION PODER
                                        tipoDeRevocacionDePoder= "TIPO DE REVOCACION PODER:\n "+obtenerValor(actoPredioCampo);
                                    } 
                                    if (id_modulo_campo ==20816 ){
                                        //MOTIVO
                                        tipoDeRevocacionDePoder+= ", MOTIVO:"+obtenerValor(actoPredioCampo);
                                    }

                        			if (campoId == Constantes.AFECTA_FOLIO_CAMPO_ID) {
                        				afectaFolio = actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
                        						? "true".equals(actoPredioCampo.getValor().toLowerCase())
                        				  			   				: false;
                        				}
                        			   
                        			if (campoId == Constantes.FOLIO_AFECTADO_CAMPO_ID && actoPredioCampo.getValor() != null
                        					&& !actoPredioCampo.getValor().isEmpty()) {
                        				foliosRealAfectados += "\nFOLIO REAL AFECTADO: " +actoPredioCampo.getValor();
                                      }
                                      switch(moduloId){
                                        case 1501:
                                            autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                                        break;
                                        case 501:
                                            tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                                        break;
                                    }
                                }
                                pvo.setTextoRegistro(this.quitarUltimoCaracter(tipoDeDocumento)+"\n\n"+"AUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                                pvo.setObservaciones(obs);
                                pvo.setUbicacion(acto2.getTipoActo().getNombre());
                                System.out.println("acto "+acto2.getId());
                                if(tipoActo==105){//PODER(ES)      
                                    pvo.setAFavorDe(apoderado);                               
                                    pvo.setTipoInforme(tipoDePoder+vigencia+datosDelPoder+datosDelPoderdante+ (afectaFolio ? foliosRealAfectados : "" ));
                                    pvo.setNaturalezaProcedencia(null);
                                }
                                if(tipoActo==106){//modificacion
                                    pvo.setAFavorDe(apoderado+"\n FACULTADES:"+facultades);  
                                    pvo.setTipoInforme(tipoDeModificacion);
                                    pvo.setNaturalezaProcedencia(null);
                                }
                                if(tipoActo==107){//revocacion
                                    pvo.setAFavorDe(null);  
                                    pvo.setNaturalezaProcedencia(datosDelPoderdante);
                                    pvo.setTipoInforme(tipoDeRevocacionDePoder);
                                }
                                
                                ap=actoPublicitarioRepository.findByLastNumero(acto2.getId());
                                if(null!=ap) {
                                    pvo.setInscritoEn(""+ap.getNumero());
                                }else {
                                    pvo.setInscritoEn("");
                                }
                            pvos.add(pvo);
                        break;
                        case 1:
                            String aFavorDe="";
                            String repudiante="";
                            obs="";
                            actoPredioCampos=getActoPredioCampo(acto2);
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                int id_modulo_campo =  moduloCampo.getId().intValue();
                                switch(id_modulo_campo){
                                    case 843:
                                        aFavorDe+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 844:
                                        aFavorDe+=this.obtenerValor(apc)+" ";
                                    break;
                                    case 845:
                                        aFavorDe+=this.obtenerValor(apc)+",";
                                    break;
                                    case 20086://Nombre
                                        repudiante+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 20087://ap
                                        repudiante+=this.obtenerValor(apc)+" ";
                                    break;
                                    case 20088://am
                                        repudiante+=this.obtenerValor(apc)+",";
                                    break;
                                    case 431:
                                        obs+=this.obtenerValor(apc);
                                    break;

                                }
                              
                            }

                            String quedandoInscrito="",hisAnte="" ;

                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                Modulo modulo=moduloCampo.getModulo();
                                switch(modulo.getId().intValue()){
                                   case 2027:
                                        hisAnte+=" "+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc);
                                    break;
                                    case 3016:
                                        hisAnte+=" "+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc);
                                    break;
                                

                                }
                            
                            }
                            tipoDeDocumento="";autoridad="";
                          //doc fundatorio
                          for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                          	int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                          	switch(moduloId){
                          		case 1501:
                          			autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          		case 501:
                          			tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          	}        
                          }
                            

                            aFavorDe=this.quitarUltimoCaracter(aFavorDe);
                            repudiante=this.quitarUltimoCaracter(repudiante);
                            quedandoInscrito=this.quitarUltimoCaracter(quedandoInscrito);
                            am = new ActoModel();
                            am.setNombre(acto2.getTipoActo().getNombre()+"\n\nPERSONAS QUE REPUDIAN LA HERENCIA:\n"+repudiante);
                            am.setActo(acto2);
                            am.setAsiento(buildAsiento(acto2.getFechaRegistro()));
                            actoModels = new ArrayList();
                            actoModels.add(am);
                            pvo.setActos(actoModels);

                         
                            if(!obs.isEmpty() && obs!=null) {
                                
                                obs="\n\n OBSERVACIONES:\n"+obs;
                            }
                            pvo.setAFavorDe("\nA FAVOR DE :\n"+aFavorDe+
                            		"\n\nQUEDANDO INSCRITO:"+                            		
                                    "\n\n"+this.quitarUltimoCaracter(tipoDeDocumento)+
                                    "\n\n AUTORIDAD:\n "+this.quitarUltimoCaracter(autoridad)+
                            		"."+obs);
                            
                            ap=actoPublicitarioRepository.findByLastNumero(acto2.getId());
                            if(null!=ap) {
                                pvo.setInscritoEn(""+ap.getNumero());
                            }else {
                                pvo.setInscritoEn("");
                            }

                        break;
                        case 4:
                        	Optional<ActoPredio> actoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto2.getId());
                        	Set<ActoPredioCampo> _actoPredioCampos = actoPredio.get().getActoPredioCamposParaActoPredios();
                        	HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService.parseActos(_actoPredioCampos);
                        	StringBuilder sbAfavorDe = new StringBuilder("CON FECHA ");
                        	sbAfavorDe.append(this.buildAsientoText(acto2.getFechaRegistro()));
                        	sbAfavorDe.append(" SE CANCELA EL (LOS) SIGUIENTES ACTOS PUBLICITARIOS");
                        	actosSeleccionados.forEach((index,x)->{
                        	   if(x.actoSeleccionado) {
                        			Acto _acto = x.actoPredioMonto.getActo();
                        			Optional<ActoPublicitario> actoPub =  actoPublicitarioRepository.findFirstByActoId(_acto.getId());
                        			if(actoPub.isPresent()) {
                        				if(actoPub.get().getNum_folio_real()!=null)
                        					sbAfavorDe.append(", FOLIO ").append(actoPub.get().getNum_folio_real());
                        				else
                        					sbAfavorDe.append(", ACTO PUBLICITARIO No. ").append(actoPub.get().getNumero());
                        					
                        					sbAfavorDe.append(" ").append(_acto.getTipoActo().getNombre()).append(" DE FECHA ").append(buildAsiento(_acto.getFechaRegistro()));
                        			}
                        	   }
                        	 });
                             tipoDeDocumento="";autoridad="";
                        	for(ActoPredioCampo actoPredioCampo  :_actoPredioCampos) {                		
                        		int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();

                                switch(moduloId){
                                    case 1501:
                                        autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                                    break;
                                    case 501:
                                        tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                                    break;
                                }
                        		if( actoPredioCampo.getModuloCampo().getCampo().getId().equals(243l)) {
                        			pvo.setObservaciones(actoPredioCampo.getValor());
                        		}
                        		
                        	}
                            pvo.setTextoRegistro(this.quitarUltimoCaracter(tipoDeDocumento)+"\n\n"+"AUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                        	 sbAfavorDe.append("\n\n\n");
                        	 sbAfavorDe.append(" QUEDANDO INSCRITO EN ").append(pvo.getTextoRegistro());
                        	 pvo.setAFavorDe(sbAfavorDe.toString());
                        	 ap=actoPublicitarioRepository.findByLastNumero(acto2.getId());
                             if(null!=ap) {
                                 pvo.setInscritoEn(""+ap.getNumero());
                             }else {
                                 pvo.setInscritoEn("");
                             }
                        	break;
                        case 2://PROTOCOLIZACIÓN DE AUTORIZACIÓN DE VENTA
                         
                            String datosDelPromovente ="",datosDelaResolucion="";//mod 5017
                            actoPredioCampos=getActoPredioCampo(acto2);
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                int id_modulo_campo =  moduloCampo.getId().intValue();
                                switch(id_modulo_campo){
                                    case 843:
                                        datosDelPromovente+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 844:
                                        datosDelPromovente+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 845:
                                        datosDelPromovente+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 431:
                                        obs=" "+this.obtenerValor(apc);
                                    break;
                                }
                            }
                            
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                Modulo modulo=moduloCampo.getModulo();
                                switch(modulo.getId().intValue()){
                                    case 5017:
                                        datosDelaResolucion+=" "+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc)+",";
                                    break;
                                }
                            }
                            tipoDeDocumento="";autoridad="";
                          //doc fundatorio
                          for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                          	int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                          	switch(moduloId){
                          		case 1501:
                          			autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          		case 501:
                          			tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          	}        
                          }
                          
                            ap = actoPublicitarioRepository.findByLastNumero(acto2.getId());
                            if(obs!=null && !obs.isEmpty()){
                                pvo.setObservaciones("OBSERVACIONES:\n\n"+obs);
                            }
                            if(null!=ap) {
                                pvo.setInscritoEn(""+ap.getNumero());
                            }else {
                                pvo.setInscritoEn("");
                            }
                            pvo.setAFavorDe("DATOS DEL PROMOVENTE \n\n"+datosDelPromovente+
                            		"\n\n "+this.quitarUltimoCaracter(datosDelaResolucion)+                            		  
                                      "\n\n"+this.quitarUltimoCaracter(tipoDeDocumento)+
                                      "\n\nAUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                        break;
                        case 3://RECTIFICACIÓN
                         
                            String datosDelSolicitante ="",datosDelaAclaracion="";//mod 5017
                            actoPredioCampos=getActoPredioCampo(acto2);
                            obs="";
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                int id_modulo_campo =  moduloCampo.getId().intValue();
                                switch(id_modulo_campo){
                                    case 843:
                                    datosDelSolicitante+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 844:
                                    datosDelSolicitante+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 845:
                                    datosDelSolicitante+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 431:
                                    obs=" "+this.obtenerValor(apc);
                                    break;
                                }
                            }
                            
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                Modulo modulo=moduloCampo.getModulo();
                                switch(modulo.getId().intValue()){
                                    case 115:
                                        datosDelaAclaracion+=" "+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc)+",";
                                    break;
                                }
                            }
                            tipoDeDocumento="";autoridad="";
                          //doc fundatorio
                          for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                          	int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                          	switch(moduloId){
                          		case 1501:
                          			autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          		case 501:
                          			tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          	}        
                          }
                           
                            ap = actoPublicitarioRepository.findByLastNumero(acto2.getId());
                            if(null!=ap) {
                                pvo.setInscritoEn(""+ap.getNumero());
                            }else {
                                pvo.setInscritoEn("");
                            }
                            pvo.setAFavorDe("DATOS DEL SOLICITANTE \n\n"+datosDelSolicitante+
                            		"\n\n DATOS DE LA ACLARACIÓN \n\n"+this.quitarUltimoCaracter(datosDelaAclaracion)+                            		
                                     "\n\n"+this.quitarUltimoCaracter(tipoDeDocumento)+
                                     "\n\nAUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                            if(obs!=null && !obs.isEmpty()){
                                pvo.setObservaciones("OBSERVACIONES:\n\n"+obs);
                            }
                        break;
                        case 5://PROTOCOLIZACIÓN DE PRORROGA DE VIGENCIA PARA ACTO MODIFICATORIO
                        
                            datosDelPromovente ="";datosDelaResolucion=""; String tipoDeActoModificatorio="",datosDelInmueble="";//mod 5017
                            actoPredioCampos=getActoPredioCampo(acto2);
                            obs="";
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                int id_modulo_campo =  moduloCampo.getId().intValue();
                                switch(id_modulo_campo){
                                    case 843:
                                    datosDelPromovente+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 844:
                                    datosDelPromovente+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 845:
                                    datosDelPromovente+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 20789:
                                    tipoDeActoModificatorio=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 431:
                                    obs=" "+this.obtenerValor(apc);
                                    break;
                                }
                            }
                            
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                Modulo modulo=moduloCampo.getModulo();
                                switch(modulo.getId().intValue()){
                                    case 5015:
                                        datosDelInmueble+=" "+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc)+",";
                                    break;
                                    case 5017:
                                            datosDelaResolucion+=" "+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc)+",";
                                    break;
                                }
                            }
                            tipoDeDocumento="";autoridad="";
                          //doc fundatorio
                          for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                          	int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                          	switch(moduloId){
                          		case 1501:
                          			autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          		case 501:
                          			tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          	}        
                          }
                            ap = actoPublicitarioRepository.findByLastNumero(acto2.getId());
                            if(null!=ap) {
                                pvo.setInscritoEn(""+ap.getNumero());
                            }else {
                                pvo.setInscritoEn("");
                            }
                         
                            pvo.setAFavorDe("TIPO DE ACTO MODIFICATORIO: \n\n   "+tipoDeActoModificatorio+
                            "\n\n DATOS DEL PROMOVENTE: \n\n    "+datosDelPromovente+
                            "\n\n DATOS DEL INMUEBLE \n\n   "+this.quitarUltimoCaracter(datosDelInmueble)+
                            "\n\n"+this.quitarUltimoCaracter(datosDelaResolucion)+
                        
                            "\n\n"+this.quitarUltimoCaracter(tipoDeDocumento)+
                            "\n\nAUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                            am = new ActoModel();
                            am.setNombre(acto2.getTipoActo().getNombre());
                            am.setActo(acto2);
                            am.setAsiento(buildAsiento(acto2.getFechaRegistro()));
                            actoModels = new ArrayList();
                            actoModels.add(am);
                            pvo.setActos(actoModels);
                            if(obs!=null && !obs.isEmpty()){
                                pvo.setObservaciones("OBSERVACIONES:\n\n"+obs);
                            }
                        
                        break;

                        case 6://ACTO PUBLICITARIO GENERAL
                            datosDelSolicitante="";
                            datosDelaResolucion="";
                            obs="";
                            String puntoResolutivos="",nExpediente="",inteN="",inteAp="",inteAm="";
                            actoPredioCampos=getActoPredioCampo(acto2);
                            tipoDeDocumento="";autoridad="";
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                int id_modulo_campo =  moduloCampo.getId().intValue();
                                switch(id_modulo_campo){
                                    case 20045:
                                        nExpediente=this.obtenerValor(apc);
                                    break;
                                    case 20834:
                                        inteN+="\n"+this.obtenerValor(apc);
                                    break;
                                    case 20835:
                                        inteN+=" "+this.obtenerValor(apc);
                                    break;
                                    case 20836:
                                        inteN+=" "+this.obtenerValor(apc);
                                    break;
                                    case 843:
                                        datosDelSolicitante+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 844:
                                        datosDelSolicitante+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 845:
                                        datosDelSolicitante+=" "+this.obtenerValor(apc)+" ";
                                    break;
                                    case 431:
                                        obs=" "+this.obtenerValor(apc);
                                    break;

                                }
                            }
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                Modulo modulo=moduloCampo.getModulo();
                                switch(modulo.getId().intValue()){
                                
                                    case 5017:
                                        datosDelaResolucion+="  "+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc)+",";
                                    break;
                                    case 308:
                                        puntoResolutivos+=""+moduloCampo.getEtiqueta()+": "+this.obtenerValor(apc)+",";
                                    break;
                                	case 1501:
                    					autoridad+=" "+this.obtenerValor(apc)+",";
                    				break;
                    				case 501:
                    					tipoDeDocumento+=" "+apc.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(apc)+",";
                    				break;
                                }
                            }
                            pvo.setTextoRegistro(this.quitarUltimoCaracter(tipoDeDocumento)+"\n\nAUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                            ap = actoPublicitarioRepository.findByLastNumero(acto2.getId());
                            if(null!=ap) {
                                pvo.setInscritoEn(""+ap.getNumero());
                            }else {
                                pvo.setInscritoEn("");
                            }
                           
                            pvo.setAFavorDe("DATOS DEL SOLICITANTE: \n\n"+datosDelSolicitante+
                            "\n\n NUMERO DE EXPEDIENTE \n\n     "+nExpediente+
                            "\n\n "+this.quitarUltimoCaracter(datosDelaResolucion)
                            +"\n\n INTERVINIENTES \n"+inteN+" "+inteAp+" "+inteAm+
                            "\n\n"+this.quitarUltimoCaracter(puntoResolutivos)
                            );
                            am = new ActoModel();
                            am.setNombre(acto2.getTipoActo().getNombre());
                            am.setActo(acto2);
                            am.setAsiento(buildAsiento(acto2.getFechaRegistro()));
                            actoModels = new ArrayList();
                            actoModels.add(am);
                            pvo.setActos(actoModels);
                            if(obs!=null && !obs.isEmpty()){
                                pvo.setObservaciones("OBSERVACIONES:\n\n"+obs);
                            }
                            
                        break;
                        case 7://REGISTRO, CANCELACIÓN, FIRMA PATENTE O HABILITACION Y SELLO DE FEDATARIOS PUBLICOS
                            String notarioTitular="",fechaOficio="",quienExpide="",notarioAds="",fundLegal="";
                            obs="";
                            actoPredioCampos=getActoPredioCampo(acto2);
                                
                            for(ActoPredioCampo apc:actoPredioCampos){
                                ModuloCampo moduloCampo = apc.getModuloCampo();
                                int id_modulo_campo =  moduloCampo.getId().intValue();
                                switch(id_modulo_campo){
                                    case 66: 
                                        notarioTitular=this.obtenerValor(apc);
                                    break;
                                    case 67: 
                                        fechaOficio=this.obtenerValor(apc);
                                    break;
                                    case 68: 
                                        quienExpide=this.obtenerValor(apc);
                                    break;
                                    case 69: 
                                        notarioAds=this.obtenerValor(apc);
                                    break;
                                    case 70: 
                                        fundLegal=this.obtenerValor(apc);
                                    break;
                                    case 431:
                                        obs=this.obtenerValor(apc);
                                    break;
                                }
                            }
                            
                            tipoDeDocumento="";autoridad="";
                          //doc fundatorio
                          for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                          	int moduloId=actoPredioCampo.getModuloCampo().getModulo().getId().intValue();
                          	switch(moduloId){
                          		case 1501:
                          			autoridad+=" "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          		case 501:
                          			tipoDeDocumento+=" "+actoPredioCampo.getModuloCampo().getEtiqueta()+": "+this.obtenerValor(actoPredioCampo)+",";
                          		break;
                          	}        
                          }
                                                       
                            ap = actoPublicitarioRepository.findByLastNumero(acto2.getId());
                            if(null!=ap) {
                                pvo.setInscritoEn(""+ap.getNumero());
                            }else {
                                pvo.setInscritoEn("");
                            }
                           
                            pvo.setAFavorDe("NOMBRE DEL NOTARIO TITULAR: \n\n"+notarioTitular+
                            "\n\n FECHA DE OFICIO/NOMBRAMIENTO \n\n     "+fechaOficio+
                            "\n\n QUIEN EXPIDE LA PATENTE \n\n   "+quienExpide
                            +"\n\n NOTARIO ADSCRITO \n\n  "+notarioAds+
                            "\n\n FUNDAMENTO LEGAL/NOMBRAMIENTO \n\n   "+fundLegal+                           
                            "\n\n"+this.quitarUltimoCaracter(tipoDeDocumento)
                            +"\n\n AUTORIDAD:\n\n"+this.quitarUltimoCaracter(autoridad));
                            
                            am = new ActoModel();
                            am.setNombre(acto2.getTipoActo().getNombre());
                            am.setActo(acto2);
                            am.setAsiento(buildAsiento(acto2.getFechaRegistro()));
                            actoModels = new ArrayList();
                            actoModels.add(am);
                            pvo.setActos(actoModels);
                            if(obs!=null && !obs.isEmpty()){
                                pvo.setObservaciones("OBSERVACIONES:\n\n"+obs);
                            }
                        break;
                        }
                       
                        pvo.setTextoConsta(this.getTextoConsta(prelacion.getId(), tipo) );
                        List<ActoFirma> aFi = actoFirmaRepository.findAllByActoIdAndEsActoOrderByIdDesc(acto2.getId(),true);
                        if( aFi != null && aFi.size()>0) {
                        	pvo.setRegistrador(this.buildUsuario( aFi.get(0).getUsuario() ));
                        }else {
                        	pvo.setRegistrador(this.buildUsuario( prelacion.getUsuarioAnalista()));
                        }
                        pvo.setCoordinador(this.buildUsuario( prelacion.getUsuarioCoordinador()));
                        // FIRMA ELECTRONICA REGISTRADOR
                        pvo.setFirmaRegistrador(this.buildStringFirmaActos(prelacionId));
                        // FIRMA ELECTRONICA COORDINADOR
                        String firmaCoord = "-SIN FIRMA -";
                        if (prelacion.getFirma() != null )
                            firmaCoord = prelacion.getFirma() + "\r\n";           
                        pvo.setFirmaCoordinador(firmaCoord);
                        if(pvos.isEmpty()) {
                        	pvos.add(pvo);
                        }
                        
                    }
                }
            
        }
        if(save && pvos!=null && pvos.size()>0)
            this.save(prelacion, pvos,pagina);
      
        return pvos;
    }
    
    
    public void empaquetarActo(Long actoId,PrelacionBoletaRegistralVO prelacion, String consecutivo,Integer pagina) throws Exception {	//CV
		byte[] boletaRegistral = this.generPdfBoletaRegistralPorActoJSON(prelacion, pagina);
		Acto acto = actoRepository.findOne(actoId);
		//SI ACTO ES DIFERENTE DE REACHAZO Y NO ES CLONADO
		if (!acto.getStatusActo().getId().equals(Constantes.StatusActo.RECHAZADO.getIdStatusActo()) && (acto.getClon()== null || !acto.getClon()) && boletaRegistral!=null) {
			List<String> archivos = new ArrayList<String>();
			ActoDocumento adoc = null; 
			Optional<ActoDocumento> ap = actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
			if(ap.isPresent())
				adoc =  ap.get();
			
			System.out.println("\n ActoDocumento: "+adoc);
			
			//SI ACTO-DOC 'NO' ES NULO Y ARCHIVO 'NO' ES NULO 
			if (adoc != null && adoc.getDocumento() != null && adoc.getDocumento().getArchivo() != null) {
				DocumentoArchivo archivoReingreso = documentoArchivoRepository.findFirstByDocumentoIdOrderByIdDesc(adoc.getDocumento().getId());
				Archivo archivo = null;
				if(archivoReingreso != null)
					 archivo =  archivoReingreso.getArchivo();
				else
					archivo  = adoc.getDocumento().getArchivo();	
				
				if(archivo!=null)
					System.out.println("\n archivos Agregado: "+archivo.getRuta() + archivo.getNombre()+".pdf");
					archivos.add(archivo.getRuta() + archivo.getNombre());
			}

		
			
			List<PdfReader> readerList = new ArrayList<PdfReader>();
			List<InputStream> inputPdfList = new ArrayList<InputStream>();
			Document document = new Document();

			if (archivos.size() > 0) {
			
				archivos.forEach(ar -> {//leer archivos
					try{
						inputPdfList.add(new FileInputStream(ar));
					}catch( FileNotFoundException e){
						e.printStackTrace();
					}
					
				  });

				for(InputStream input:inputPdfList){//convertir a Obj PDF
					PdfReader read= new PdfReader(input);
					readerList.add(read);//agregar
				}
				///////////////////////////////////////////////////////////////////////////
				for(String fileOut:archivos){								
					PdfCopy copy = new PdfCopy(document, new FileOutputStream(fileOut));//RUTA ARCHIVO INICIAL
					document.open();
					for(PdfReader read:readerList){
						System.out.println("\n DocFunadotrio npag: "+read.getNumberOfPages());
						copy.addDocument(read);//AGREGAR ARCHIVO INICIAL
						copy.freeReader(read);
						read.close();
					}
					// FIN DOCUMENTO CON BOLETA REGISTRAL
					PdfReader reader = new PdfReader(boletaRegistral);
					System.out.println("\n BolReg npag: "+reader.getNumberOfPages());
					copy.addDocument(reader);
					copy.freeReader(reader);
					reader.close();
			
					document.close();
					acto.setPathArchivado(fileOut);
					acto.setArchivado(true);
					actoRepository.saveAndFlush(acto);
				}
			}else {
				String pathDoc =  parametroRepository.findByCve("RUTA_DOCTO").getValor();
				String nameFile = "ASIENTO_"+actoId+"_"+consecutivo+".pdf";
				pathDoc += nameFile;
				try (FileOutputStream fos = new FileOutputStream(pathDoc)) {
					   fos.write(boletaRegistral);
						acto.setPathArchivado(pathDoc);
						acto.setArchivado(true);
						actoRepository.saveAndFlush(acto);
					   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
				 }
			}
		 }
				
    }
    
    
	public void save(Prelacion prelacion,List<PrelacionBoletaRegistralVO> list,Integer pagina){
		PrelacionBoleta pb = new PrelacionBoleta();
		pb.setTipoBoleta(tipoBoletaRepository.findOne(Constantes.tipoBoleta.BOLETA_REGISTRAL.getId()));
		pb.setPrelacion(prelacion);
		pb.setDatos(classToJson(list));
		pb.setPagina(pagina);
		prelacionBoletaRepository.save(pb);
	}
	
	private String classToJson(List<PrelacionBoletaRegistralVO> object) {
		//System.out.println("classToJson 116");
		return writeJson(object);
	}
	
	private String writeJson(Object object) {
		//ObjectMapper mapper = new ObjectMapper();
		try {
			//System.out.println("writeJson "+mapper.writeValueAsString(object));
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}

		//return "";
	}


    private String construlleActo(List<ActoPredioCampo> apcs, Set<Integer>order){
        int orden=-1;
        StringBuilder reconstruyeActo=new StringBuilder();
        String etiquetaTipoActo="",motivo="";
        String check="";
                   
        for(int o:order){
            System.out.println("orden   "+o);
            for(ActoPredioCampo apc:apcs){
                Long moduloCampoId=apc.getModuloCampo().getId();
                orden=apc.getOrden();
                System.out.println("moduloCampoId   "+moduloCampoId+" ordenAPC "+orden);
                if(moduloCampoId==20761L ||moduloCampoId==460L && orden==o){//recuperar acto
                    Acto actoRecuperado = actoRepository.findOne(Long.parseLong(apc.getValor()));
                    etiquetaTipoActo=actoRecuperado.getTipoActo().getNombre();
                    System.out.println("    "+etiquetaTipoActo);
                }
                if(moduloCampoId==20762L ||moduloCampoId==461L && orden==o){//CHECK ACTO
                    check=apc.getValor();
                }
                if(moduloCampoId==20758L && orden==o){//MOTIVO CANCELACIÓN
                    CampoValores cv=campoValoresRepository.findOne(Long.parseLong(apc.getValor()));
                    motivo=", MOTIVO: "+cv.getNombre();
                    System.out.println("    "+motivo);
                }
                if(moduloCampoId==471L && orden==o){//MOTIVO CANCELACIÓN
                  
                    motivo=", PORCENTAJE A CANCELAR "+apc.getValor()+"%";
                    System.out.println("    "+motivo);
                }
                
            } 
            if(!motivo.isEmpty()&&!etiquetaTipoActo.isEmpty()&&!check.isEmpty()){
                reconstruyeActo.append(" "+o+") "+etiquetaTipoActo+motivo+"\n");
                motivo="";etiquetaTipoActo="";
            }                    
        }
        
            return reconstruyeActo.toString();
        }
  
    private String stringToDateFormater(String s){
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaobtenida=null;
        if(s!=null){
            try{
                fechaobtenida = new SimpleDateFormat("yyyy-MM-dd").parse(s); 
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return(fechaobtenida != null ? sdf.format(fechaobtenida) : "");
    }
    private String moduloCampoFor(List<ActoPredioCampo> actoPredioCampos) {
    	
    	List<ModuloCampo> moduloCampo = new ArrayList<ModuloCampo>();
    	String cdnObs = "";
    	int index = 0;
    	
    	 //Campos no incluidos por ID MODULO_CAMPO_ID
    	List<Long> camposNoIncluidos = new ArrayList<Long>();
        camposNoIncluidos.add(Constantes.MODULO_CAMPO_CHECK_ACTO);
        camposNoIncluidos.add(Constantes.MODULO_CAMPO_ACTO);
        
       
        //camposNoIncluidos.add(1402L);
        //camposNoIncluidos.add(942L);
        //camposNoIncluidos.add(20103L);
        //camposNoIncluidos.add(20104L);
        //camposNoIncluidos.add(20105L);
        //camposNoIncluidos.add(843L);
        //camposNoIncluidos.add(844L);
        //camposNoIncluidos.add(845L);
        //camposNoIncluidos.add(20113L);
        
        camposNoIncluidos.add(431L);
        camposNoIncluidos.add(89L);
        camposNoIncluidos.add(281L);
        camposNoIncluidos.add(282L);
        camposNoIncluidos.add(2010L);
        camposNoIncluidos.add(842L);

        camposNoIncluidos.add(851L);
       
        
        
        camposNoIncluidos.add(951L);
        camposNoIncluidos.add(20722L);
        camposNoIncluidos.add(20720L);
        camposNoIncluidos.add(20721L);
        camposNoIncluidos.add(20144L);
        camposNoIncluidos.add(20142L);
        camposNoIncluidos.add(20141L);
        camposNoIncluidos.add(20143L);
        camposNoIncluidos.add(20145L);
        camposNoIncluidos.add(20146L);
        camposNoIncluidos.add(20111L);
        camposNoIncluidos.add(20112L);

        camposNoIncluidos.add(20762L);
        
        camposNoIncluidos.add(20102L);
        

        
        camposNoIncluidos.add(20058L);
        camposNoIncluidos.add(20054L);
        camposNoIncluidos.add(20055L);
        
        //Campos no incluidos por Etiqueta
        List<String> camposNoIncluidosPer = new ArrayList<String>();
        camposNoIncluidosPer.add("TIPO DE PERSONA");
        camposNoIncluidosPer.add("NACIONALIDAD");
        
        
        //Etiquetas No Incluidas
        List<String> camposNoIncluidosPerEtiqueta = new ArrayList<String>();
        //camposNoIncluidosPer.add("TIPO DE PERSONA");
        camposNoIncluidosPerEtiqueta.add("NACIONALIDAD");
        camposNoIncluidosPerEtiqueta.add("NOMBRE(S) / DENOMINACIÓN");
        camposNoIncluidosPerEtiqueta.add("PRIMER APELLIDO");
        camposNoIncluidosPerEtiqueta.add("SEGUNDO APELLIDO");
        camposNoIncluidosPerEtiqueta.add("NOMBRE(S)/DENOMINACIÓN DESIGNANTE");
        camposNoIncluidosPerEtiqueta.add("DESCRIPCION SERVIDUMBRE");
        camposNoIncluidosPerEtiqueta.add("MOTIVO CANCELACIÓN");
        
    	  actoPredioCampos = actoPredioCampos.stream()       
                  .filter(x -> !camposNoIncluidos.contains(x.getModuloCampo().getId()) && !camposNoIncluidosPer.contains(x.getModuloCampo().getEtiqueta()))
                  .collect(Collectors.toList()); 
        
    	for (ActoPredioCampo actoPredioCampo : actoPredioCampos) 
        {
    		moduloCampo.add(actoPredioCampo.getModuloCampo());
        }
    	
    	Long idMod = 0L;
    	//for(ModuloCampo mod : moduloCampo) {
    		
    		for (ActoPredioCampo apc : actoPredioCampos) 
            {
    			
    			log.debug("===> APC ID :s" + apc.getActoPredio().getId());
    			
    			ModuloCampo mod  = apc.getModuloCampo();
    			
        		if(mod.getId() == apc.getModuloCampo().getId()) {
        			
        			if(idMod != mod.getModulo().getId()) {
        				
        				List<ModuloTipoActo> mta = moduloTipoActoRepository.findAllByTipoActoIdAndModuloId(apc.getActoPredio().getActo().getTipoActo().getId(),mod.getModulo().getId());
            			
        				cdnObs += "" + mta.get(0).getEtiqueta()+" // ";
        				//cdnObs += "" + mod.getModulo().getNombre()+" // ";
            		}
        			
//        			log.debug("===> MODULO " + mod.getModulo().getNombre() + " ---- ID : " + mod.getModulo().getId() );	
//            		log.debug("===> MODULO_CAMPO " + mod.getEtiqueta() + " ----  ID : " + mod.getId() );
//            		log.debug("===> VALOR " + obtenerValor(apc));
            		
            		cdnObs += "" + apc.getModuloCampo().getEtiqueta() + ": " ;	
            		cdnObs += obtenerValor(apc) + ", " ;
            		
            		
//            		if(!camposNoIncluidosPerEtiqueta.contains(mod.getEtiqueta())) {
//            			
//            			if(!mod.getModulo().getNombre().equals(mod.getEtiqueta())){
//            				cdnObs += "" + mod.getEtiqueta() + ": " ;	
//            			}
//            			cdnObs += obtenerValor(apc) + ", " ;
//            		}else if(camposNoIncluidosPerEtiqueta.contains(mod.getEtiqueta())) {
//            			cdnObs += obtenerValor(apc) + " " ;
//            		}else {
//            			cdnObs += ","+obtenerValor(apc) + "," ;
//            		}
        			
            		idMod = mod.getModulo().getId();
        			
        		}
    
            }
    		
   	
//    		if(idMod != mod.getModulo().getId()) {
//    			cdnObs += "\n";
//    		}
    		
    	//}
    	
    	
    	return cdnObs;
    }
    
    private String getActoPredioCampoValores(List<ActoPredioCampo> actoPredioCampos) {
    	
    	String cdnObs = "";
    	
    	 //Campos no incluidos por ID MODULO_CAMPO_ID
    	List<Long> camposNoIncluidos = new ArrayList<Long>();
        camposNoIncluidos.add(Constantes.MODULO_CAMPO_CHECK_ACTO);
    
        
  	  actoPredioCampos = actoPredioCampos.stream()       
              .filter(x -> !camposNoIncluidos.contains(x.getModuloCampo().getId()))
              .collect(Collectors.toList()); 
    	
    	for (ActoPredioCampo actoPredioCampo : actoPredioCampos) 
        {
    		 
    		cdnObs += "" + actoPredioCampo.getModuloCampo().getEtiqueta() + ": " ;	
    		cdnObs += obtenerValor(actoPredioCampo) + ", \n" ;
    		
        }
    	
    	return cdnObs;
    }
    
    
    private String getObservacionesActoACancelar(List<ActoPredioCampo> actoPredioCampos) {
    	
    	Acto acto = null;
    	String cdnObs = "";
        
        List<Long> ordenActoCancelar = new ArrayList<Long>();
        
        for (ActoPredioCampo actoPredioCampo : actoPredioCampos) 
        {
            ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
            int id_modulo_campo =  moduloCampo.getId().intValue();
        	
        	if (id_modulo_campo == Constantes.MODULO_CAMPO_CHECK_ACTO && obtenerValor(actoPredioCampo).equals("SI")) { 
        		ordenActoCancelar.add(actoPredioCampo.getOrden().longValue());
        	}
        	
        }
        
        
        for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
        	ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
            int id_modulo_campo =  moduloCampo.getId().intValue();
            
            
        	if(id_modulo_campo ==  Constantes.MODULO_CAMPO_ACTO.intValue()) {
        		for(int i=0; i<ordenActoCancelar.size();i++) {
        			if(actoPredioCampo.getOrden().equals(ordenActoCancelar.get(i).intValue())) {
        				ActoPredioCampo  apc =  actoPredioCampoRepository.findAllByActoPredioIdAndModuloCampoIdAndOrden(
        						actoPredioCampo.getActoPredio().getId(),
        						Constantes.MODULO_CAMPO_ACTO.longValue(),
        						ordenActoCancelar.get(i).intValue());
        				
        				if(apc != null) {
        					acto = actoRepository.getOne(Long.parseLong(apc.getValor()));
        					cdnObs += "\n\n" + "ACTO CANCELADO" + ": " + acto.getTipoActo().getNombre() + " , FECHA DE INSCRIPCION: " + UtilFecha.formatToDatePattern(prelacionService.buscaFechaInscripcion(acto.getId())) + "\n\n";
        					cdnObs += moduloCampoFor(getActoPredioCampo(acto));
        				}
            		}
        		}
        		
        		

        	}
        }
        
        
    	return cdnObs;
    }
    
    
    
    private List<TitularModel> buildTitularesPrelacionContratante(Prelacion prelacion, Predio predio) {
		List<TitularModel> titularesList = new ArrayList<TitularModel>();
    	
    	log.debug("===> buildTitularesPrelacionContratante - (prelacion,predio)="+prelacion.getId()+","+predio.getId() );

    	List<PrelacionContratante> contratantesList = prelacionContratanteRepository
    			.findAllByPrelacionAndPredioOrderByOrdenAsc(prelacion,predio);
    	log.debug( "===> 1 contratantesList = "+contratantesList.size() );
    	
    	if( contratantesList == null || contratantesList.size() == 0 ) {
    		log.debug( "===> aun no hay predioId" );
            Set<Acto> actos = prelacion.getActosParaPrelacions();
   
            for(Acto a:actos){
                if(a.getVf() != null){
                    if(a.getVf()!=false ){
                        actos.remove(a);
                    }
                }
                else{
                    actos.remove(a);
                }
            }
            
    		contratantesList = prelacionContratanteRepository
    				.findAllByPrelacionAndActoInOrderByOrdenAsc(prelacion,actos);
    		log.debug( "===> 2 contratantesList = "+contratantesList.size() );
    	}
		
    	double dd=0,uv=0;
		TitularModel titularModel = null;
		for( PrelacionContratante contratante : contratantesList ) {
         
            if(contratante.getActo().getVf()!=null){
                if(contratante.getActo().getVf() == false){
                    if( contratante.getDd() == null && contratante.getUv() == null ) {
                        continue;
                    }
                    if( contratante.getDd() != null ) {
                        try {
                            dd = Double.parseDouble( contratante.getDd() );
                        } catch( NumberFormatException nfe ) {
                        }
                    }
                    if( contratante.getUv() != null ) {
                        try {
                            uv = Double.parseDouble( contratante.getUv() );
                        } catch( NumberFormatException nfe ) {
                        }
                    }
                    titularModel = new TitularModel();
                    titularModel.setNombre( contratante.getNombreCompleto() );
                    titularModel.setDd( dd );
                    titularModel.setUv( uv );
                    //titularModel.setTipo( "PROPIETARIO" );	
                    log.debug("===> titularModel ===> "+titularModel.getNombre() );
                    titularesList.add( titularModel );
                }
             }
		}

		return titularesList;
	}

	private boolean todosActosRechazados(PrelacionPredio predio) {
		
		Long prelacionId = -1L;
		if( predio.getPrelacion() != null ) {
			prelacionId = predio.getPrelacion().getId();
		} else {
			return false;
		}
		Long predioId = -1L;
		if( predio.getPredio() != null ) {
			predioId = predio.getPredio().getId();
		} else {
			return false;
		}

        List<ActoPredio>  actoPredios=  actoPredioRepository.findAllByPrelacionAndPredio(prelacionId,predioId);

        if (actoPredios == null || (actoPredios != null && actoPredios.size() == 0)) {
            return false;  // Permite imprimir predio sin Datos de Acto
        }

        for (ActoPredio apre :actoPredios ){

            if ( apre.getActo().getStatusActo().getId() == Constantes.StatusActo.RECHAZADO.getIdStatusActo() ) continue;

            return false;
        }

        return true;
    }

    /*private List<AcreedoresModel> buildAcreedores(PrelacionPredio predio, Constantes.ETipoFolio tipo) {
        List<AcreedoresModel> titulares = new ArrayList<>();

        if (tipo != Constantes.ETipoFolio.PREDIO)
            return null;

        if (predio == null || (predio != null && predio.size() == 0)) {
            TitularModel tit = new TitularModel();
            tit.setNombre("- -");
            tit.setDd(0.00);
            tit.setUv(0.00);
            titulares.add(tit);
            return titulares;
        }

        for (PrelacionPredio prel :predios){

            Set<PredioPersona> list = actoPredioService.findPersonasbyPredioId(prel.getPredio().getId());
            if (list == null)
                break;

            list.forEach(preper -> {
                titulares.add( new TitularModel() {{
                    setNombre( preper.getPersona().getNombre());
                    setTipo(preper.getPersona().getTipoPersona().getNombre());
                    setDd(preper.getPorcentajeDd().doubleValue());
                    setUv(preper.getPorcentajeUv().doubleValue());
                }});
            });

        }

        return titulares;

    }*/

    private PrelacionBoletaRegistralVO getBRHeader(Prelacion prelacion) {
        return new PrelacionBoletaRegistralVO(){{
            //prelacion.consecutivo - prelacion.año (del ingreso) - prelacion.num_capturas
            if(prelacion.getSubindice()==null){
                setConsecutivo(""+prelacion.getConsecutivo()+"-"+prelacion.getAnio()+"-"+0);
            }
            else{
                setConsecutivo(""+prelacion.getConsecutivo()+"-"+prelacion.getAnio()+"-"+prelacion.getSubindice());
            }
            
            setFechaIngreso(prelacion.getFechaIngreso()); // FECHA INGRESO
            setOficina(prelacion.getOficina().getNombre()); // LUGAR / OFICINA
             setObservaciones(CommonUtil.decodeValue(prelacion.getObservaciones()));
            setLeyendaRegistro(buildLeyendaRegistro(prelacion.getFechaIngreso()));
            setRecibos(buildRecibos(prelacion.getId()));
        }};
    }

    public PrelacionBoletaRegistralVO findOneVOPredios(Long prelacionId) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Prelacion prelacion = prelacionRepository.findOne(prelacionId);

        PrelacionBoletaRegistralVO pvo = new PrelacionBoletaRegistralVO(){{
            setConsecutivo(""+prelacion.getConsecutivo()); // NO PRELACION
            setFechaIngreso(prelacion.getFechaIngreso()); // FECHA INGRESO
            setOficina(prelacion.getOficina().getNombre()); // LUGAR / OFICINA
            setObservaciones(CommonUtil.decodeValue(prelacion.getObservaciones()));


        }};

        Constantes.ETipoFolio tipo = prelacionService.getTipoFolio(prelacion);
       

        List<PrelacionPredio> predios  = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacion.getId());


        pvo.setPredios(this.buildPredios (predios, tipo) );

        pvo.setLeyendaRegistro(this.buildLeyendaRegistro(pvo.getFechaIngreso()));
        pvo.setAntecedentes(this.buildAntecedentes(predios, tipo));
        pvo.setTitulares(this.buildTitulares(predios, tipo) );  // Titulares solo aplica para Predios (Pendiente Muebles y jurídico)

        pvo.setRecibos(this.buildRecibos(prelacion.getId()));

        pvo.setActos(this.buildActos(prelacion.getId()));

        pvo.setTextoRegistro (this.buildTextoRegistro(prelacion.getId() , tipo)); // Busqueda de persona Notario y Acto

        pvo.setTextoConsta(this.getTextoConsta(prelacion.getId(), tipo) );

        pvo.setRegistrador(this.buildUsuario( prelacion.getUsuarioVentanilla() ));

        pvo.setCoordinador(this.buildUsuario( prelacion.getUsuarioCoordinador()) );



        // FIRMA ELECTRONICA REGISTRADOR
        pvo.setFirmaRegistrador(this.buildStringFirmaActos(prelacionId));

        // FIRMA ELECTRONICA COORDINADOR
        String firmaCoord = "-SIN FIRMA -";
        if (prelacion.getPkcs7() != null )
            firmaCoord = prelacion.getPkcs7() + "\r\nCODIGO DE AUTENTICIDAD: "+prelacion.getSecuencia();
        pvo.setFirmaCoordinador(firmaCoord);


        return pvo;

    }

    public BoletaSuspensionVO findOneVOBoletaSuspension(Prelacion prelacion) {

		SimpleDateFormat formatter = new SimpleDateFormat("DD/MM/YYYY");
		SuspensionFirma suspensionFirma = null;
		BoletaSuspensionVO pvo = new BoletaSuspensionVO();
        Suspension suspension = suspencionRepository.findFirstByPrelacionIdOrderByIdDesc(prelacion.getId());
        if(suspension!=null) {
        	suspensionFirma = suspensionFirmaRepository.findFirstBySuspensionIdOrderByIdDesc(suspension.getId()); //Validar que cuando registrador no confire la suspension
        }
        
		Bitacora bitacora = bitacoraRepository.findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(prelacion.getId(),
				prelacion.getStatus().getId());  //Corregir estatus para pasar la de la entra

		// prelacion.

		pvo.setPrelacionId(new Long(prelacion.getConsecutivo())); // NO PRELACION
		pvo.setFechaRegistro(prelacion.getFechaIngreso()); // FECHA INGRESO
		pvo.setSolicitante(prelacion.getUsuarioSolicitan().getNombreCompleto());
		pvo.setOficina(prelacion.getOficina().getNombre());
	
		if(bitacora != null){
			if(bitacora.getMotivo()!=null) {
				pvo.setRazonSuspension(bitacora.getMotivo().getNombre());
			}
			if(bitacora.getTipoRechazo()!=null) {
				pvo.setFundamento(bitacora.getTipoRechazo().getNombre());
			}
			pvo.setComentarios(bitacora.getObservaciones());
		  }
		pvo.setRevisoElaboro(this.buildUsuario(prelacion.getUsuarioAnalista()));

        if(suspensionFirma != null){   // FIRMA ELECTRONICA COORDINADOR
            pvo.setUsuarioAutorizo(this.buildUsuario(suspensionFirma.getUsuario()));            
            pvo.setFechaFirma(suspensionFirma.getFechaFirma());            
            pvo.setFirma(suspensionFirma.getFirma());
        }

		int dias = Integer.valueOf(parametroRepository.findByCve("DIAS_SUSPENSION").getValor());
        pvo.setDias(dias);
        if(suspension!=null && suspension.getFechaSuspension()!=null) {
            pvo.setFechaSuspension(suspension.getFechaSuspension());
            pvo.setFechaVencimiento(turnadorService.addDiasHabiles(suspension.getFechaSuspension(), dias-1));
		}
     
        
        Constantes.ETipoFolio tipo = prelacionService.getTipoFolio(prelacion);
		List<PredioActoVO> listPredios = new ArrayList<PredioActoVO>();

        Set<Acto> actos = prelacion.getActosParaPrelacions();
        

		for (Acto acto : actos) {
            if(acto.getVf() == null || acto.getVf()==false) {
                PredioActoVO predioActoVO = new PredioActoVO();
                predioActoVO.setActo(acto.getTipoActo().getNombre());

                Set<ActoPredio> listActoPredio = acto.getActoPrediosParaActos();

                for (ActoPredio actoPredio : listActoPredio) {  
                    if(tipo!=null){
                        switch(tipo){                            
                            case PREDIO:
                                if(actoPredio.getPredio()!=null){
                                    predioActoVO.setFolio(actoPredio.getPredio().getNoFolio());
                                }
                                break;
                            case MUEBLE:
                                if(actoPredio.getMueble()!=null){
                                    predioActoVO.setFolio(actoPredio.getMueble().getNoFolio());
                                }
                                break;                            
                            case PERSONAS_JURIDICAS:
                                if(actoPredio.getPersonaJuridica()!=null){
                                    predioActoVO.setFolio(actoPredio.getPersonaJuridica().getNoFolio());
                                }
                                break;
                        }
                    }
                }
                listPredios.add(predioActoVO);
            }
		}

		pvo.setPredioActo(listPredios);
		
		return pvo;

	}

    @SuppressWarnings("unchecked")
	public List<byte[]> getPdfBoletaRechazo(Prelacion prelacion) throws JRException {

		Set<Acto> acto = prelacion.getActosParaPrelacions();
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		InputStream jasperStream = null;
		JRDataSource ds = null;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		byte[] array = null;
		List<byte[]> listadocs = new ArrayList<byte[]>();
		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			for (Acto item : acto) {

				if (item.getStatusActo().getId().equals(5L)) {

					BoletaRechazoVO boleta = findOneVOBoletaRechazo(item.getId());

					List<BoletaRechazoVO> prelacions = new ArrayList<BoletaRechazoVO>();
					prelacions.add(boleta);

					ds = new JRBeanCollectionDataSource(prelacions);

					String qrUri = parametroRepository.findByCve("QR_BASE_URI").getValor()
							+ "#/resultado-tramite/rechazo";

					parameterMap.put("datasource", ds);
					parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
					parameterMap.put("imgPath", Constantes.IMG_PATH);
					parameterMap.put(JRParameter.REPORT_LOCALE, Constantes.locale);
                    parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
                    String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+prelacion.getId();
                    parameterMap.put("QR_BASE_URI", qR);
					jasperStream = this.getClass().getResourceAsStream("/reports/pdfBoletaRechazo.jasper");

					if (jasperStream != null) {
						jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
						jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
						byte[] doc = JasperExportManager.exportReportToPdf(jasperPrint);
						outputStream.write(doc);
						listadocs.add(outputStream.toByteArray());
						outputStream.flush();
					}

				}

			}

			return listadocs;// outputStream.toByteArray();
		} catch (Exception ex) {

			log.debug("Excepcion de al generar boleta rechazo " + ex.getMessage());
			return null;

		}

	}

    public BoletaRechazoVO findOneVOBoletaRechazo(Long actoId) {
        
        Long prelacionId = 0L;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Acto  acto=   actoRepository.findOneById(actoId);
        Prelacion prelacion = prelacionRepository.findOne(acto.getPrelacion().getId());

        BoletaRechazoVO pvo = new BoletaRechazoVO();

        pvo.setPrelacionId(new Long(prelacion.getConsecutivo())); // NO PRELACION
        pvo.setFechaRegistro(new SimpleDateFormat("dd-MM-yyyy").format(prelacion.getFechaIngreso())); // FECHA INGRESO
        pvo.setOficina(prelacion.getOficina().getNombre());
        Constantes.ETipoFolio tipo = prelacionService.getTipoFolio(prelacion);
        Set<ActoPredio> aps = acto.getActoPrediosParaActos();
        Predio predio=null;
        PersonaJuridica personaJuridica=null;

        for(ActoPredio ap: aps ){
            if(ap.getTipoFolio()!=null){

                Integer tipoId = (int) (long) ap.getTipoFolio().getId();
                switch (tipoId) {
                    case 4:
                    	predio = ap.getPredio();
                    	if( predio != null ) {
                            pvo.setNoFolio(predio.getNoFolio());
                            pvo.setUbicacion(predio.getDireccionComplete());
                    	}
                        pvo.setAreaFolio(ap.getTipoFolio().getNombre());
                    break;
                    case 1:
                        if(ap.getPersonaJuridica()!=null){
                            personaJuridica = ap.getPersonaJuridica();
                            pvo.setNoFolio(personaJuridica.getNoFolio());
                            pvo.setUbicacion(personaJuridica.getDenominacionNombre());
                        }
                    break;
                    
                }
            }else{
                pvo.setNoFolio(0);
                pvo.setAreaFolio("NO SE ESTABLECE TIPO DE FOLIO");
                pvo.setUbicacion("NO SE ESTABLECE TIPO DE FOLIO");    

            }
           
            pvo.setActo(ap.getActo().getTipoActo().getNombre());
           
        }
        
        if(prelacion.getArea().getId().equals(Constantes.Area.PROPIEDAD.getIdArea())) {
        	pvo.setAreaFolio("INMOBILIARIO");
        }else {
        	pvo.setAreaFolio(prelacion.getArea().getNombre());
        }
        String personalNotario="",datosNotario="";
        List<ActoDocumento> docus = actoDocumentoService.getAllActoDocumentoByActoId(acto.getId());
        for(ActoDocumento aDoc: docus) {
            Documento doc = aDoc.getDocumento();
            Persona not = doc.getNotario()==null ? null: doc.getNotario().getPersona();

            //String fechaFormat = formatter.format(doc.getFecha());

            String folio =  "FOLIO [   ] ";
            String escritura = " ESCRITURA NO. " + doc.getNumero() ;

             personalNotario = (not == null) ? " - NO HAY INFORMACION DE NOTARIO - " : ("  FEDATARIO LIC. " + not.getNombre() + " " + not.getPaterno() + " " + not.getMaterno());
             datosNotario = (not == null) ? "": " CON SEDE EN " + doc.getNotario().getMunicipio().getNombre() + " " + doc.getNotario().getMunicipio().getEstado().getNombre().trim() + ".\r\n";

            
        }
        
        pvo.setFechaRechazo(new SimpleDateFormat("dd-MM-yyyy-HH:mm").format(acto.getFechaRechazo())) ;

        Motivo motivo = acto.getMotivo();
        if( motivo != null ) {
        	pvo.setRazonRechazo(motivo.getNombre());
        }
        TipoRechazo tipoRechazo = acto.getTipoRechazo();
        if( tipoRechazo != null ) {
        	pvo.setFundamento(tipoRechazo.getNombre());
        }
        pvo.setMontoOperacion(new BigDecimal(0)) ;
        pvo.setMontoDerechos(new BigDecimal(0)) ;
        
     	if(acto.getTipoActo().getId()==207L || acto.getTipoActo().getId()==209L) {

            StringBuilder sol = new StringBuilder();
            HashMap<Integer, Persona> solicitantes = new HashMap<Integer, Persona>();
            List<ActoPredioCampo> actoPredioCampos = getActoPredioCampo(acto);
            
		  for(ActoPredioCampo apc : actoPredioCampos) {
            long moduloCampoId = apc.getModuloCampo().getId();
            int index = apc.getOrden();
             if (moduloCampoId == 106 || moduloCampoId == 107 || moduloCampoId == 108) {
					Persona persona = solicitantes.get(index);
					if (persona == null) {
						persona = new Persona();
						solicitantes.put(index, persona);
					}

					if (moduloCampoId == 106) {
						persona.setNombre(" "+obtenerValor(apc));
					}

					if (moduloCampoId == 107) {
						persona.setPaterno(obtenerValor(apc));
					}

					if (moduloCampoId == 108) {
						persona.setMaterno(obtenerValor(apc));
					}
				}
          }
          for (Map.Entry<Integer, Persona> entry : solicitantes.entrySet()) {
            Persona pp = (Persona) entry.getValue();
            if (pp != null) {

                sol.append(pp.getNombre() != null ? pp.getNombre() + " " : "");
                sol.append(pp.getPaterno() != null ? pp.getPaterno() + " " : "");
                sol.append(pp.getMaterno() != null ? pp.getMaterno() : "");
                sol.append("\n");

            }
        }
		  pvo.setSolicitante(sol.toString());
		
		}else {
			pvo.setSolicitante(this.getUsuarioSolicitan( prelacion.getUsuarioSolicitan(), prelacion.getId()));
		}

          
        pvo.setComentarios(CommonUtil.decodeValue(acto.getObservacionesMotivo())) ;

        pvo.setNoFedatario(personalNotario +"  "+datosNotario);
        

        pvo.setRevisoElaboro(this.buildUsuario( prelacion.getUsuarioAnalista()));

        pvo.setUsuarioAutorizo(this.buildUsuario( prelacion.getUsuarioCoordinador()) );

       

        pvo.setDias(10);

          //FIRMA ELECTRONICA DEL REGISTRADOR
          pvo.setFirmaRegistrador(this.buildStringFirmaActosByActo(acto));
        
        // FIRMA ELECTRONICA COORDINADOR
        String firmaCoord = "-SIN FIRMA -";
        if (prelacion.getFirma() != null )
            firmaCoord = prelacion.getFirma();
        pvo.setFirmaCoordinador(firmaCoord);
        return pvo;

    }


	private String getUsuarioSolicitan(Usuario usuarioSolicitan, Long prelacionId) {
		if (usuarioSolicitan != null) {

			if (usuarioSolicitan.getId() != 0)
				return usuarioSolicitan.getNombreCompleto();

			if (usuarioSolicitan.getId() == 0) {
				PrelacionUsuarioDef def = prelacionUsuarioDefRepository.findOneByPrelacionId(prelacionId);
				if (def != null)
					return def.getNombre() + " " + def.getPaterno() + " " + def.getMaterno();
			}
		}
		return "NO DEFINIDO";
	}


    private String getTextoConsta(Long id, Constantes.ETipoFolio tipo) {
        return "<b>CONSTA QUE: </b> <br>" +
                "Consta que...";
    }


    private String joinPredios(List<PrelacionPredio> predios) {
        String strPredios = "";
        if (predios != null)
            strPredios =
                predios.stream()
                .map(predio -> predio.getPredio().getNoFolio().toString() )
                .collect(Collectors.joining(", "));

        return strPredios;
    }

    private List<PredioModel> buildPredios(List<PrelacionPredio> predios, Constantes.ETipoFolio tipo) {
        List<PredioModel> prediosModel = new ArrayList<>();
        if (predios == null ) {
            System.out.println("Predios vacios");
            prediosModel.add(new PredioModel() {{setFolio(0);}});
            return prediosModel;
        }

        if (predios != null && predios.size() == 0) {
            System.out.println("Predios tamano 0");
            prediosModel.add(new PredioModel() {{setFolio(0);}});
            return prediosModel;
        }

        System.out.println("=== Agregando predios ===");

        for (PrelacionPredio predio : predios ) {
            PredioModel pm = new PredioModel();
            switch(tipo) {
                case PREDIO:
                	if( predio.getPredio().getNoFolio() != null ) {                        
                        pm.setFolio(predio.getPredio().getNoFolio().intValue());
                        System.out.println("Folio:"+pm.getFolio());
                    }
                    if(predio.getPredio().getAuxiliar()!=null){
                        pm.setNoFolioAux(""+predio.getPredio().getAuxiliar());
                    }
                    else{
                        pm.setNoFolioAux("");
                    }
                    if(predio.getPredio().getNumeroFolioReal()!=null){
                        pm.setNoFolioFutuReg(""+predio.getPredio().getNumeroFolioReal());
                        pm.setLeyendaFolio("IDENTIFICADOR EN SISTEMA");
                    }else{
                        pm.setLeyendaFolio("FOLIO UNICO REAL ELECTRONICO");
                        pm.setNoFolioFutuReg(null);
                    }
            
                    /*
                    	
			clg.setNoFolioAux(""+predio.getAuxiliar());
		}else{
			clg.setNoFolioAux(null);
		}

		if(predio.getNumeroFolioReal()!=null){
			clg.setNoFolioFutuReg(""+predio.getNumeroFolioReal());
			clg.setLeyendaFolio("IDENTIFICADOR EN SISTEMA");
		}else{
			clg.setLeyendaFolio("FOLIO UNICO REAL ELECTRONICO");
			clg.setNoFolioFutuReg(null);
		}

                    */
                break;
                case MUEBLE:
                    pm.setFolio(predio.getMueble().getNoFolio().intValue());
                    break;
                case PERSONAS_AUXILIARES:
                    pm.setFolio(predio.getFolioSeccionAuxiliar().getNoFolio().intValue());
                    break;
                case PERSONAS_JURIDICAS:
                	pm.setLeyendaFolio("FOLIO UNICO REAL ELECTRONICO");
                    pm.setFolio(predio.getPersonaJuridica().getNoFolio().intValue());
                    break;
            }
            pm.setFolioFusion(null);
            prediosModel.add(pm);
        }

        return prediosModel;
    }



    private String buildTextoRegistro(long id, Constantes.ETipoFolio tipo) {
        String fundatorio= "";
        List<Acto> actos1=   actoService.findByPrelacionId(id);
        for (Acto acto:actos1 ) {
       
                List<ActoDocumento> docus = actoDocumentoService.getAllActoDocumentoByActoId(acto.getId());
                for(ActoDocumento aDoc: docus) {
                    Documento doc = aDoc.getDocumento();
                    Persona not = doc.getNotario()==null ? null: doc.getNotario().getPersona();

                    //String fechaFormat = formatter.format(doc.getFecha());

                    String folio =  "FOLIO [   ] ";
                    String escritura = "";
                    if(doc!=null){

                        
                        String personalNotario = (not == null) ? " - NO HAY INFORMACION DE NOTARIO - " : ("  FEDATARIO LIC. " + not.getNombre() + " " + not.getPaterno() + " " + not.getMaterno());
                        String datosNotario = (not == null) ? "": " CON SEDE EN " + doc.getNotario().getMunicipio().getNombre() + " " + doc.getNotario().getMunicipio().getEstado().getNombre().trim() + ".\r\n";
                        escritura= prelacionService.getCaratulaDocumento(doc);
                    }
                
                fundatorio = folio + "  "+ escritura;
             };
            
        }

        return fundatorio;
    }

    private String buildTextoRegistro(PrelacionPredio pPredio,  ArrayList<Acto> actos1, Constantes.ETipoFolio tipo) {
    	
    	//log.debug( "===> buildTextoRegistro !!! - (pPredio,prelacion) = "+pPredio.getPredio().getId() +"-"+ pPredio.getPrelacion().getId() );
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fundatorio = "";
        String fechaFormat =""; 
       ;


       /* if( pPredio.getPredio() != null ) {
            actos1 = actoService.findByActoPrediosParaActosPredioIdAndPrelacionId(pPredio.getPredio().getId(), pPredio.getPrelacion().getId());
        } else {
            actos1 = actoService.findByActoPrediosParaActosPredioIdAndPrelacionId(null, pPredio.getPrelacion().getId());
        }
        */
        Integer noFolio = null;
        if( tipo == Constantes.ETipoFolio.PREDIO ) {
        	noFolio = pPredio.getPredio().getNoFolio();
        } else if( tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS ) {
        	noFolio = pPredio.getPersonaJuridica().getNoFolio();
        } else if( tipo == Constantes.ETipoFolio.MUEBLE ) {
        	noFolio = pPredio.getMueble().getNoFolio();
        } else if( tipo == Constantes.ETipoFolio.PERSONAS_AUXILIARES ) {
        	noFolio = pPredio.getFolioSeccionAuxiliar().getNoFolio();
        }
       
        log.debug( "===> actos1 = "+ actos1.size() );
        
        
        for (Acto acto:actos1 ) {
            log.debug( "===> actoId = "+acto.getId() ); 
            if(acto.getVf()==null || acto.getVf()==false){            
                
                  
                     if(acto.getTipoActo().getId() == 210 ){
                        String texto1 = "FOLIO " + (noFolio == null ? "-" : noFolio) + " ";
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
                        List<ActoPredioCampo> actoPredioCampos = actoPredioCampoRepository.findByActoPredioId(actoPredio.getId());
                        for(ActoPredioCampo actoPredioCampo :actoPredioCampos){
                            if(actoPredioCampo.getModuloCampo().getId() == 1311){
                                Date fechaobtenida= null;
                                try {
                                    fechaobtenida = new SimpleDateFormat("yyyy-MM-dd").parse(actoPredioCampo.getValor()) ;
                                } catch (java.text.ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                               // texto1 += " AVISO CAUTELAR DE FECHA "+ (fechaobtenida != null ? sdf.format(fechaobtenida) : "-");
                            }else if(actoPredioCampo.getModuloCampo().getId() == 1310){
                            Notario notario = notarioService.findOne(Long.parseLong(actoPredioCampo.getValor()));
                            texto1 +=" NOTARIO "+ notario.getNoNotario()+" "+ notario.getPersona().getNombre() +" "+ notario.getPersona().getPaterno()+" "+ notario.getPersona().getMaterno() +
                            " " + notario.getMunicipio().getNombre() + ", HIDALGO";
                            }
                        }
                        fundatorio += texto1;
                    }
                    else{
                        List<ActoDocumento> docus = actoDocumentoService.getAllActoDocumentoByActoId(acto.getId());
                        int tipoDocumentoId;
                        for(ActoDocumento aDoc: docus) {
                            SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" ); 
                            Documento doc = aDoc.getDocumento();
                            
                            String texto1 = "FOLIO "+ (noFolio==null? "-": noFolio) + " ";
                            //PARA FUSION  MUESTRA FOLIOS MATRIZ
                            if(acto.getTipoActo().getId().equals(41L)) {
                                List<PredioRel> pr =  predioRelRepository.findByActoId(acto.getId());
                                if(pr!=null && pr.size()>0)
                                {	
                                	 texto1= "FOLIO(S) ";
                                	for(PredioRel prel  : pr) {
                                		texto1 += prel.getPredio().getNoFolio() +", ";
                                	}
                                }
                            }
                            
                           
                            String texto2 = "";
                            String label = "ESCRITO ";
                            Municipio municipio = null;
                            TipoNotario tipoDeNotario=null;
                            String municipioNombre = "-";
                            String estadoNombre = "-";
                            
                            Integer numero = doc.getNumero();
                            String numerox=doc.getNumero2();
                            Boolean bis = doc.isBis();
                            Date fecha = doc.getFecha();
                            Notario notario = doc.getNotario();
                            Integer noNotario = null;
                            Juez juez = doc.getJuez();
                            Integer noJuzgado = null;
                            Boolean exhorto = doc.isExhorto();
                            String autoridadExhortante = doc.getAutoridadExhortante();
                            String derivadoDe = doc.getDerivadoDe();
                            String numero2 = doc.getNumero2();
                            String cargoFirmante = doc.getCargoFirmante();
                            String autoridadLocal = doc.getAutoridadLocal();
                            String objeto = doc.getObjeto();
                            String causa = doc.getCausaUtilidad();
                            String requeridoPor = doc.getRequeridoPor();
                            String expropiante = doc.getExpropiante();
                            String enCalidadDe = doc.getEnCalidadDe();

                            String nombreCompleto = "";
                            String nombre = doc.getNombre();
                            String paterno = doc.getPaterno();
                            String materno = doc.getMaterno();
                            String tipoDeNotarioString="";
                            
                            if( nombre != null ) {
                                nombreCompleto += nombre;
                            }
                            if( paterno != null ) {
                                nombreCompleto += " "+paterno;
                            }
                            if( materno != null ) {
                                nombreCompleto += " "+materno;
                            }
                            nombreCompleto = nombreCompleto.trim().toUpperCase();
                            if(doc.getFecha()!=null) {
                            	fechaFormat=formatter.format(doc.getFecha());
                            }else {
                            	fechaFormat="";
                            }
                            
                            tipoDocumentoId = doc.getTipoDocumento().getId().intValue();
                            
                            log.debug("Tipo de Documento  "+tipoDocumentoId );
                            
                            if( notario != null ) 
                            {
                            	int num;
                                tipoDeNotario=notario.getTipoNotario();
                             num=   tipoDeNotario.getId().intValue();
                             switch (num) {
							case 4:
								tipoDeNotarioString=" "+tipoDeNotario.getNombre()+" ";
								break;
							case 5:
								tipoDeNotarioString=" "+tipoDeNotario.getNombre()+" ";
								break;
							case 7:
								tipoDeNotarioString=" "+tipoDeNotario.getNombre()+" ";
								break;
							case 8:
								tipoDeNotarioString=" "+tipoDeNotario.getNombre()+" ";
								break;
							default:
								tipoDeNotarioString=" NOTARIO ";
								break;
							}
                                }
                            switch(tipoDocumentoId) {
                            
                            case 1: // ESCRITURA
                                texto1 += "POR ESCRITURA PUBLICA NÚMERO "+ (numerox!=null? numerox: "-") +
                                        (bis!=null && bis==true? " BIS": "") +
                                        " DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
                                if( notario != null ) {
                                    municipio = notario.getMunicipio();
                                    tipoDeNotario=notario.getTipoNotario();
                                    tipoDeNotario.getId();
                                    log.debug("tipo123 "+tipoDeNotario);
                                    if( municipio != null ) {
                                        municipioNombre = municipio.getNombre().trim();
                                        estadoNombre = municipio.getEstado().getNombre().trim();
                                    }
                                    noNotario = notario.getNoNotario();
                                    texto2 = " OTORGADO ANTE LA FE DE " + notario.getNombreCompleto() + tipoDeNotarioString
                                    		+ (noNotario!=null? noNotario: "-") +
                                        " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto2 = " - NO HAY INFORMACION DE NOTARIO - ";
                                }
                                
                                
                                fundatorio += texto1 + texto2 + ".\r\n\r\n";
                                break;
                                

                            case 2:  // CONTRATO PRIVADO RATIFICADO POR JUEZ
                            case 20: //ESCRITO RATIFICADO POR JUEZ    
                                //texto1 += "CONTRATO PRIVADO DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");

                                if( juez != null ) {
                                    municipio = juez.getMunicipio();
                                    if( municipio != null ) {
                                        municipioNombre = municipio.getNombre().trim();
                                        estadoNombre = municipio.getEstado().getNombre().trim();
                                    }
                                    noJuzgado = juez.getNoJuzgado();
                                    texto2 = " RATIFICADO POR EL JUEZ " + juez.getNombreCompleto() +
                                        " DEL "+ (noJuzgado!=null? noJuzgado: "-") +
                                        " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto2 = " - NO HAY INFORMACION DE JUEZ - ";
                                }	
                    
                                fundatorio += texto1 + texto2 + ".\r\n\r\n";
                                
                                System.out.println("aqui "+texto1+" aqui\n "+texto2); 
                                log.info( "aqui "+texto1+" aqui\n "+texto2 );
                                break;
                                
                            /*case 3: // TÍTULO
                                texto1 += "TÍTULO NUMERO "+ (numero!=null? numero: "-") +
                                        " DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
                    
                                fundatorio += texto1 + ".\r\n\r\n";
                                break;*/
                             // RESOLUCIÓN
                            case 5: // OFICIO CON FUNDAMENTO EN EL ARTICULO 77 DE LA LEY DEL RPP
                                texto1 += "OFICIO DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                                " EMITIDO POR AUTORIDAD "+ (autoridadLocal.length()>0? autoridadLocal: "-" )+
                                " CON NUMERO DE DOCUMENTO: "+ (numero2!=null? numero2: "-" );                                
                                fundatorio += texto1 + ".\r\n\r\n";
                                break;
                            case 4:
                            case 6: // OFICIO JURIDICO
                            //case 9: // OFICIO RATIFICADO POR LA COMISION NACIONAL DE SEGUROS Y FIANZAS
                            //case 19: // ORDENAMIENTO DE AUTORIDAD

                            if(acto.getTipoActo().getId()==121){
                             
                                /*
                                    DERIVADO DE - EXPEDIENTE - A CARGO DE - - - DE 
                                */
                               List<ActoPredioCampo> actoPredioCampos=getActoPredioCampo(acto);                                
                                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                                    int id_modulo_campo =  moduloCampo.getId().intValue();

                                    switch (id_modulo_campo) {

                                        case 192: // derivado de
                                            derivadoDe=actoPredioCampo.getValor();
                                        break;
                                        
                                        case 193: // expediente
                                            numero2=actoPredioCampo.getValor();
                                        break;
                                        
                                        case 194: // cargo de
                                            cargoFirmante=actoPredioCampo.getValor();
                                        break;
                                        case 195: // datos del firmante
                                            nombreCompleto=actoPredioCampo.getValor();
                                        break;
                                    
                                        case 197: // municipio
                                             municipioNombre=municipioService.findOne(Long.parseLong(actoPredioCampo.getValor())).getNombre();
                                        break;
                                        
                                        case 196: // estado
                                            estadoNombre=estadoService.findOne(Long.parseLong(actoPredioCampo.getValor())).getNombre();
                                        break;
                                    }                                
                                } 
                                texto1 += "OFICIO NUMERO "+ (numero!=null? numero: "-") +
                                " DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                                 " DERIVADO DE "+ (derivadoDe!=null? derivadoDe: "-") +
                                " EXPEDIENTE "+ (numero2!=null? numero2: "-") +
                                " A CARGO DE "+ (cargoFirmante!=null? cargoFirmante: "-") +
                                " "+(nombreCompleto.length()>0? nombreCompleto: "" ) +
                                " DE " + municipioNombre + ", " + estadoNombre ;
                            }else{
                                municipio = doc.getMunicipio();
                                if( municipio != null ) {
                                    municipioNombre = municipio.getNombre().trim();
                                    estadoNombre = municipio.getEstado().getNombre().trim();
                                }
                                if( exhorto != null && exhorto == true ) {
                                    texto1 += "OFICIO NUMERO "+ (numero!=null? numero: "-") +
                                            " RELATIVO AL EXHORTO"+
                                            " GIRADO POR "+ (autoridadExhortante!=null? autoridadExhortante: "-") +
                                            " DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                                            " DERIVADO DE "+ (derivadoDe!=null? derivadoDe: "-") +
                                            " EXPEDIENTE "+ (numero2!=null? numero2: "-") +
                                            " A CARGO DE "+ (cargoFirmante!=null? cargoFirmante: "-") +
                                            " "+ (nombreCompleto.length()>0? nombreCompleto: "-" ) +
                                            " "+ (autoridadLocal!=null? autoridadLocal: "-") +
                                            " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto1 += "OFICIO NUMERO "+ (numero!=null? numero: "-") +
                                            " DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                                            " DERIVADO DE "+ (derivadoDe!=null? derivadoDe: "-") +
                                            " EXPEDIENTE "+ (numero2!=null? numero2: "-") +
                                            " A CARGO DE "+ (cargoFirmante!=null? cargoFirmante: "-") +
                                            " "+ (nombreCompleto.length()>0? nombreCompleto: "-" ) +
                                            " "+ (autoridadLocal!=null? autoridadLocal: "-") +
                                            " DE " + municipioNombre + ", " + estadoNombre;
                                }  
                            }
                                 fundatorio += texto1 + ".\r\n\r\n";
                            break;
                            /*case 7: // ESCRITO NOTARIAL
                                texto1 += "ESCRITO DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
                                if( notario != null ) {
                                    municipio = notario.getMunicipio();
                                    if( municipio != null ) {
                                        municipioNombre = municipio.getNombre().trim();
                                        estadoNombre = municipio.getEstado().getNombre().trim();
                                    }
                                    noNotario = notario.getNoNotario();
                                    texto2 = " PRESENTADO POR EL LIC. "+ notario.getNombreCompleto() +
                                        " NOTARIO PUBLICO "+ (noNotario!=null? noNotario: "-") +
                                        " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto2 = " - NO HAY INFORMACION DE NOTARIO - ";
                                }
                                fundatorio += texto1 + texto2 + ".\r\n\r\n";
                                break;*/
                            /*case 8: // SOLICITUD DE PARTICULAR RATIFICADA ANTE NOTARIO
                                label = "SOLICITUD "; */                
                            /*case 10: // SENTENCIA
                                texto1 += "SENTENCIA DICTADA EN EL EXPEDIENTE NUMERO "+ (numero!=null? numero: "-") +
                                        (bis!=null && bis==true? " BIS": "");
                                if( juez != null ) {
                                    municipio = juez.getMunicipio();
                                    if( municipio != null ) {
                                        municipioNombre = municipio.getNombre().trim();
                                        estadoNombre = municipio.getEstado().getNombre().trim();
                                    }
                                    noJuzgado = juez.getNoJuzgado();
                                    texto2 = " DEL "+ (noJuzgado!=null? noJuzgado: "-") +
                                        " DICTADA POR "+ juez.getNombreCompleto() +
                                        " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto2 = " - NO HAY INFORMACION DE JUEZ - ";
                                }
                                fundatorio += texto1 + texto2 + ".\r\n\r\n";
                                break;*/
                            case 11: // ESCRITO
                                texto1 += "ESCRITO DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                                        " PRESENTADO POR "+ (nombreCompleto.length()>0? nombreCompleto: "-" );
                                fundatorio += texto1 + ".\r\n\r\n";
                                break;
                            case 12: // OFICIO PROPIEDAD
                            	texto1 += "OFICIO NUMERO "+ (numero2!=null? numero2: "-") +
                            			" DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                            			" AUTORIDAD "+ (autoridadLocal!=null? autoridadLocal: "-");
                            	fundatorio += texto1 + ".\r\n\r\n";
                            	break;
                            case 13: // POLIZA
                                texto1 += "POLIZA NUMERO "+ (numero2!=null? numero2: "-") +
                                        " DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                                        " "+ (objeto!=null? objeto: "-");
                                fundatorio += texto1 + ".\r\n\r\n";
                                break;
                            case 14: // ESCRITO RATIFICADO POR NOTARIO
                                texto1 += label + "DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
                                if( notario != null ) {
                                    municipio = notario.getMunicipio();
                                    if( municipio != null ) {
                                        municipioNombre = municipio.getNombre().trim();
                                        estadoNombre = municipio.getEstado().getNombre().trim();
                                    }
                                    noNotario = notario.getNoNotario();
                                    texto2 = " RATIFICADO ANTE LA FE DE "+tipoDeNotarioString+ notario.getNombreCompleto() +
                                        " NUMERO "+ (noNotario!=null? noNotario: "-") +
                                        " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto2 = " - NO HAY INFORMACION DE NOTARIO - ";
                                }
                                fundatorio += texto1 + texto2 + ".\r\n\r\n";
                                break;
                            case 15: // DECRETO
                                texto1 += "DECRETO NUMERO "+ (numero!=null? numero: "-") +
                                        " DE FECHA "+ (fecha!=null? sdf.format(fecha): "-") +
                                        " POR CAUSA DE UTILIDAD PUBLICA "+ (causa!=null? causa: "-") +
                                        " REQUERIDO POR "+ (requeridoPor!=null? requeridoPor: "-") +
                                        " ORDENADO POR "+ (expropiante!=null? expropiante: "-") +
                                        " REPRESENTADO POR "+ (nombreCompleto.length()>0? nombreCompleto: "-" ) +
                                        " EN CALIDAD DE "+ (enCalidadDe!=null? enCalidadDe: "-");
                                fundatorio += texto1 + ".\r\n\r\n";
                                break;
                            case 16: // CONTRATO PRIVADO RATIFICADO POR NOTARIO PUBLICO
                                texto1 += "CONTRATO PRIVADO DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
                                if( notario != null ) {
                                    municipio = notario.getMunicipio();
                                    if( municipio != null ) {
                                        municipioNombre = municipio.getNombre().trim();
                                        estadoNombre = municipio.getEstado().getNombre().trim();
                                    }
                                    noNotario = notario.getNoNotario();
                                    texto2 = " RATIFICADO ANTE LA FE DE "+ notario.getNombreCompleto() +tipoDeNotarioString+
                                    		(noNotario!=null? noNotario: "-") +
                                        " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto2 = " - NO HAY INFORMACION DE NOTARIO - ";
                                }
                                fundatorio += texto1 + texto2 + ".\r\n\r\n";
                                break;
                            case 17: // SOLICITUD DE NOTARIO
                                texto1 += "ESCRITURA "+(numerox!=null ? numerox : "")+" DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
                                if( notario != null ) {
                                    municipio = notario.getMunicipio();
                                    if( municipio != null ) {
                                        municipioNombre = municipio.getNombre().trim();
                                        estadoNombre = municipio.getEstado().getNombre().trim();
                                    }
                                    noNotario = notario.getNoNotario();
                                    texto2 = " REALIZADA POR "+ notario.getNombreCompleto() +tipoDeNotarioString
                                        + (noNotario!=null? noNotario: "-") +
                                        " DE " + municipioNombre + ", " + estadoNombre;
                                } else {
                                    texto2 = " - NO HAY INFORMACION DE NOTARIO - ";
                                }
                               
                                fundatorio += texto1 + texto2 + ".\r\n\r\n";
                                break;
                            /*case 18: // CONTRATO PRIVADO DE INFONAVIT
                                texto1 += "CONTRATO PRIVADO DE INFONAVIT DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
                                fundatorio += texto1 + ".\r\n\r\n";
                                break; */
                            
                            case 21:  // RESOLUCION JUDICIAL
                                fundatorio =texto1+"RESOLUCION JUDICIAL CON NO. "+doc.getNumero2()+ " DE FECHA  "+fechaFormat+" SOLICITADO POR   "+doc.getAutoridadLocal();
                                break;
                            case 22:  // RESOLUCION ADMINISTRATIVA  
                                fundatorio =texto1+"RESOLUCION ADMINISTRATIVA CON NO. "+doc.getNumero2()+ " DE FECHA  "+fechaFormat+" SOLICITADO POR  "+doc.getAutoridadLocal();
                                break;
                            case 23:  // TITULO DE PROPIEDAD
                            	fundatorio=texto1+"TITULO DE PROPIEDAD AUTORIZADO POR "+autoridadLocal+" Y REPRESENTADO POR "+enCalidadDe+" CON NUMERO "+ numero2+" CON FECHA DE "+fechaFormat;
                                break;
                            case 24:  // DOCUMENTO PRIVADO
                            	fundatorio=texto1+"DOCUMENTO PRIVADO CON NUMERO "+numero2+" CON FECHA "+fechaFormat+" CELEBRADO EN "+objeto;
                                break;

                            case 27: 
                                return fundatorio ="";
                            
                            default:                
                                texto1 = " - TIPO DE DOCUMENTO INVALIDO - SIN TEXTO DE LEYENDA - ";
                                fundatorio += texto1 + ".\r\n\r\n";
                                break;
                            }
                        
                    }  
                }
            }   
        }

        return fundatorio.length()>0? fundatorio.trim(): "No hay información de documento fundatorio";
    }

    public String buildUsuario(Usuario usuario) {
        if (usuario!= null)
            return usuario.getNombreCompleto();
        return " - " ;
    }

    private List<ActoModel> buildActos(Long id) {
        List<Acto> acts = actoService.findByPrelacionId(id);
        List<ActoModel> actosModel = new ArrayList<>();
        int index = 1;
        for (Acto acto :acts ){
            if(acto.getTipoActo().getId() != 210){
                actosModel.add(new ActoModel() {{
                    setNombre(acto.getTipoActo().getNombre());
                    setAsiento( buildAsiento( acto.getFechaRegistro() ) );
                }});
            }
        }

        return actosModel;

    }

    private List<ActoModel> buildActos(PrelacionPredio pp,ActoPredio ap, PrelacionBoletaRegistralVO pvo,Constantes.ETipoFolio tipo,Prelacion prelacion) {
        System.out.println("===  Buscando Actos para predio ===");

        //log.debug( "===> buildActos (prelacion,predio)="+predio.getPrelacion().getId()+","+predio.getPredio().getId() );
        
      
        List<ActoModel> actosModel = new ArrayList<>();
        if (ap == null) {
            ActoModel act = new ActoModel();
            act.setNombre("- No hay información de actos -");
            act.setAsiento(null);
            actosModel.add(act);
            return actosModel;
        }
        
        TreeMap<Date, ActoModel> treeMap = new TreeMap<Date, ActoModel>();
   
        Long clasifActoId;
        int s=1;
     
           
        	if(ap.getActo().getVf()!=null) 
        	{
        		if(!ap.getActo().getVf()) 
        		{
        			if(ap.getActo().getFechaRegistro()!=null) 
        			{ 
                    Date d =ap.getActo().getFechaRegistro();
                    d.setSeconds(d.getSeconds()+s);
                    ap.getActo().setFechaRegistro(d);
                    s+=1;        		
        			}else log.debug("getFechaRegistro es null");
        		}
        	}
        


            
        	
        	log.debug( "===> ActoPredioId = "+ap.getId() );
     
            //if ( ap.getActo().getStatusActo().getId() == Constantes.StatusActo.RECHAZADO.getIdStatusActo() ) continue;
            
            //if ( acto.getActo().getStatusActo().getId() == Constantes.StatusActo.INVALIDO.getIdStatusActo() ) continue;
            
            // if( ap.getActo().getVf() != null && ap.getActo().getVf() ) continue;

            //if( ap.getActo().getVf() != null && ap.getActo().getVf() == true ) continue;

            /*
            if ( pvo.getTitulares().size() == 0  ) {

                HashMap<Integer, PredioPersona> titulares = buscarTitutlaresFromActo(acto.getActo());
                List<TitularModel> tempModel =  construirTitulares (titulares);

                pvo.getTitulares().addAll(tempModel);

            }
            */
            log.debug("Este es el tipo de acto ==========>" + ap.getActo().getTipoActo().getId());
            ArrayList<Acto> actosB=new  ArrayList<Acto>();
            actosB.add(ap.getActo());
            treeMap.put(ap.getActo().getFechaRegistro(),new ActoModel() {{
                String docFun=buildTextoRegistro(pp,actosB,tipo);
                if(docFun!=null && docFun.length()>0 ){
                	if(!actosB.get(0).getTipoActo().getId().equals(70L) && !actosB.get(0).getTipoActo().getId().equals(249L))//DISCT RECURSO DE INCONFORMIDAD
                		docFun="\n\n QUEDANDO INSCRITO EN: \n"+docFun;
                	else
                		docFun="\n\n"+docFun;
                		
                }else{ docFun="";}
                setNombre(actoService.nombreTipoActo(ap.getActo())+docFun);
                System.out.println("actomodel Nombre= "+getNombre());
                setAsiento(buildAsiento( ap.getActo().getTipoActo().getId().equals(210l) || ap.getActo().getTipoActo().getId().equals(50l)?  ap.getActo().getPrelacion().getFechaIngreso() : ap.getActo().getFechaRegistro()  )); // PRIMER O SEG AVISO FECHA INGRESO PRELACION
                setActo(ap.getActo());
           }});

            clasifActoId = ap.getActo().getTipoActo().getClasifActo().getId();
            
            if( clasifActoId == Constantes.ClasifActo.TRASLATIVOS.getIdClasifActo() ) {
            	agregaLeyendaBeneficiario(ap,pvo);
            } else {
            	if( pvo.getTitulares().size() == 0 && ap.getPredio() != null ) {
            		pvo.setTitulares( this.buildTitularesPrelacionContratante(ap.getPredio()) );
            	}//g
            }

            if( clasifActoId == Constantes.ClasifActo.GRAMAVAMES_LIMITATANES.getIdClasifActo() ) {
                pvo.setAFavorDe(buildClasificacionGravamen(ap.getActo(),prelacion));
                /*
                switch (acto.getActo().getTipoActo().getId().intValue()){
                    case 37: // FIANZA
                        pvo.setAFavorDe("\n"+ buildFiador(acto) );
                    break;
                    case 219: // USUFRUCTO
                        pvo.setAFavorDe("\n"+ buildUsufructuario(acto) );
                    break;
                    case 228:  // ANOTACIÓN PREVENTIVA DE DEMANDA
                        pvo.setAFavorDe("PROMOVENTE: \n\n"+buildAcreditantes(acto));
                    break;
                    case 234: // ARRENDAMIENTO
                        pvo.setAFavorDe("\n"+buildArrendatario(acto));
                    break;
                    default:
                        pvo.setAFavorDe("\n"+buildAcreditantes(acto));
                    break;
                }*/

                //if (pvo.getAFavorDe()!= null) {
                    //pvo.setAFavorDe("\n"+pvo.getAFavorDe()+"\n\n"+ buildAcreditantes(acto));                    
                //}
            }
        
        
        actosModel.addAll(treeMap.values());
        log.debug( "===> treMap = "+treeMap.size());
        log.debug( "===> ActosModelSize = "+actosModel.size());
        return actosModel.size() > 0? actosModel: null;

    }

    private String buildClasificacionGravamen(Acto acto,Prelacion _prelacion) {
    	
    	String montoG="",tipoG="",montoG3=" ",tipoMonto="";
    	HashMap<Integer,String> nombreG =new HashMap<Integer,String>(); 
		HashMap<Integer,String> paternoG =new HashMap<Integer,String>(); 
		HashMap<Integer,String> maternoG =new HashMap<Integer,String>(); 
		String DIA="",MES="",ANIO="";
		String fechaG="";
		String DocumentoG="",BisG="",OficinaG="",AnioG="",SeccionG="",LibroG="",TomoG="",VolumenG="",antecedentesCompleto="";
		DecimalFormat formatoPesos = new DecimalFormat("###,###.##");
		Boolean montoG_Aux=false;
		Double montoG2=0.0;
		NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
		boolean vf=false,auxVf=false;
		DecimalFormat formateador = new DecimalFormat("###,###.##");
		int numActo=0;
		numActo=acto.getTipoActo().getId().intValue();
		ActoPredio actoPredio1 =null;
        actoPredio1=acto.getActoPrediosParaActosOrderedByVersion().first();
        List<ActoPredioCampo> actoPredioCampos1=getActoPredioCampo(acto);
        String tipoDeMoneda = "";
    	for (ActoPredioCampo actoPredioCampo:actoPredioCampos1) 			
			
		{	try {
					
			int campoId = actoPredioCampo.getModuloCampo().getId().intValue();				
			int index  = actoPredioCampo.getOrden();
			
			if(campoId==492) {tipoMonto=actoPredioCampo.getValor(); CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));tipoG=cv.getNombre();}				
			if(actoPredioCampo.getModuloCampo().getCampo().getId()==265L){
			   	tipoDeMoneda=this.obtenerValor(actoPredioCampo);
			}
			switch (numActo) {
            case 28:
                switch (campoId) 
                {
                case 479:nombreG.put(index,actoPredioCampo.getValor()); break;
                case 723:paternoG.put(index,actoPredioCampo.getValor()); break;
                case 724:maternoG.put(index,actoPredioCampo.getValor()); break;							
                }
                if(campoId==89) {
                    montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
                    //montoG2=new Double(montoG);
                    montoG="POR LA CANTIDAD DE "+ montoG;
                }
                if(actoPredioCampo.getModuloCampo().getCampo().getId() == 265L &&(actoPredioCampo.getModuloCampo().getId()==2010L)){
                    tipoDeMoneda = obtenerValor(actoPredioCampo);
                }
            break;
			case 25:
			case 27:
			
				switch (campoId) 
				{
				case 537:nombreG.put(index,actoPredioCampo.getValor()); break;
				case 538:paternoG.put(index,actoPredioCampo.getValor()); break;
				case 539:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}
				if(campoId==89) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
                    //montoG2=new Double(montoG);
                    montoG="POR LA CANTIDAD DE "+ montoG;
                }
                if(actoPredioCampo.getModuloCampo().getCampo().getId() == 265L &&(actoPredioCampo.getModuloCampo().getId()==2010L)){
                    tipoDeMoneda = obtenerValor(actoPredioCampo);
                }
			break;
			
			
			case 66:// DEMANDAS Y/O NOTIFICACIONES DE JUICIOS DE AMPARO
				switch (campoId) 
					{
					case 252:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 253:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 775:maternoG.put(index,actoPredioCampo.getValor()); break;							
					}
			break;
			case 234://ARRENDAMIENTO 
				if(campoId==89) {
					montoG= CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG="POR LA CANTIDAD DE "+montoG;
				}
				switch (campoId) 
					{
					case 106:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 107:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 108:maternoG.put(index,actoPredioCampo.getValor()); break;							
					}
				
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//montoG="POR LA CANTIDAD DE "+montoG; //(formatoImporte.format(montoG2));
			break;	

			case 34://ARRENDAMIENTO FINANCIERO
				
				switch (campoId) 
					{
					case 106:nombreG.put(index,actoPredioCampo.getValor());break;
					case 107:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 108:maternoG.put(index,actoPredioCampo.getValor()); break;							
					}
					if(campoId==89) {
						montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
                        //montoG2=new Double(montoG);
                        montoG="POR LA CANTIDAD DE "+ montoG;
					}
					//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
					//(formatoImporte.format(montoG2));
			break;

			case 31://CÉDULA HIPOTECARIA
				if(campoId==187) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE "+montoG ;
				}
				switch (campoId) 
					{
					case 843:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 844:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 845:maternoG.put(index,actoPredioCampo.getValor()); break;							
					}
				
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
		 	break;
		
			case 32://embargo
				if(campoId==20792) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE "+ montoG;
				}
				switch (campoId) 
					{
					case 843:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 844:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 845:maternoG.put(index,actoPredioCampo.getValor()); break;							
					}
				
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
			break;
			case 115://embargo CON PRORROGA
				if(campoId==20792) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE "+montoG ;
				}
				switch (campoId) 
					{
					case 273:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 186:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 794:maternoG.put(index,actoPredioCampo.getValor()); break;							
					}
				
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
			break;
			case 37://FIANZA
				if(campoId==89) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG="POR LA CANTIDAD DE "+montoG;
				}
				switch (campoId) 
					{
					case 106:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 107:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 108:maternoG.put(index,actoPredioCampo.getValor()); break;						
					}
				
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				 //(formatoImporte.format(montoG2));
			break;
			case 243://ANOTACIÓN PREVENTIVA DE EMBARGO
				if(campoId==20792) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE  "+montoG ;
				}
				switch (campoId) 
					{
						case 843:nombreG.put(index,actoPredioCampo.getValor()); break;
						case 844:paternoG.put(index,actoPredioCampo.getValor()); break;
						case 845:maternoG.put(index,actoPredioCampo.getValor()); break;						
					}
				
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
			case 217://NOVACIÓN DE CONTRATO
				
				switch (campoId) 
					{
					case 925:nombreG.put(index,actoPredioCampo.getValor());break;						
					}
					if(campoId==20792) {
						montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
						//montoG2=new Double(montoG);
						montoG=" POR LA CANTIDAD DE "+ montoG;
					}
					//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
					//(formatoImporte.format(montoG2));
			break;
		
		/* 	case 228://ANOTACIÓN PREVENTIVA DE DEMANDA
				switch (campoId) 
				{
				case 843:nombreG=actoPredioCampo.getValor(); break;
				case 844:paternoG=actoPredioCampo.getValor(); break;
				case 845:maternoG=actoPredioCampo.getValor(); break;							
				}
			break; */

			case 55://COMODATO
				switch (campoId) 
				{
				case 925:nombreG.put(index,actoPredioCampo.getValor()); break;
				case 926:paternoG.put(index,actoPredioCampo.getValor()); break;
				case 927:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}
			break;

			case 227://CESION DE DERECHOS CREDITICIOS 1013 ,1014, 1015
				switch (campoId) 
				{
					case 925:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 926:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 927:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}
				break;
			case 38://ASEGURAMIENTO DE BIENES 925, 926, 927

				if(campoId==20792) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE "+ montoG;
				}
				switch (campoId) 
				{
				case 273:nombreG.put(index,actoPredioCampo.getValor()); break;
				case 186:paternoG.put(index,actoPredioCampo.getValor()); break;
				case 794:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}
				
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
				break;
			case 35://CONSTITUCION DE PATRIMONIO FAMILIAR 252 ,253 ,775
				switch (campoId) 
				{
				case 106:nombreG.put(index,actoPredioCampo.getValor()); break;
				case 107:paternoG.put(index,actoPredioCampo.getValor()); break;
				case 108:maternoG.put(index,actoPredioCampo.getValor());break;							
				}
				break;
			case 230://CONTRATO DE OCUPACIÓN SUPERFICIAL 106 ,107 ,108
				switch (campoId) 
				{
					case 106:nombreG.put(index,actoPredioCampo.getValor());break;
					case 107:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 108:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}
				if(campoId==124) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG=" POR LA CANTIDAD DE "+ montoG;
				}
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
				break;
			/* case 118://ANOTACION DE  DEMANDA
				switch (campoId) 
				{
					case 273:nombreG=actoPredioCampo.getValor(); break;
					case 186:paternoG=actoPredioCampo.getValor(); break;
					case 794:maternoG=actoPredioCampo.getValor(); break;						
				} 
			break;*/
			case 98://PRENDA
				switch (campoId) 
				{
					case 843:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 844:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 845:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}	
				if(campoId==124) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG="POR LA CANTIDAD DE "+montoG;
				}
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				
			break;
			case 14://FIDECOMISO 479, 723, 724
				switch (campoId) 
				{
				case 479:nombreG.put(index,actoPredioCampo.getValor()); break;
				case 723:paternoG.put(index,actoPredioCampo.getValor());break;
				case 724:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}
				if(campoId==20792) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());;
                    //montoG2=new Double(montoG);
                    montoG="POR LA CANTIDAD DE "+ montoG;
				}
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
			break;
			case 64:// CONVENIO MODIFICATORIO
				switch (campoId) 
				{
					case 843:nombreG.put(index,actoPredioCampo.getValor()); break;
					case 844:paternoG.put(index,actoPredioCampo.getValor()); break;
					case 845:maternoG.put(index,actoPredioCampo.getValor()); break;						
				}
				if(campoId==20792) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
                    //montoG2=new Double(montoG);
                    montoG="POR LA CANTIDAD DE "+ montoG;
				}
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
				break;
			case 48://SERVIDUMBRE 942 ,943, 944
				switch (campoId) 
				{
				case 942:nombreG.put(index,actoPredioCampo.getValor());break;
				case 943:paternoG.put(index,actoPredioCampo.getValor()); break;
				case 944:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}
				break;
			
			case 121://retencion
				switch (campoId) {
					case 273: // nombre
						nombreG.put(index,actoPredioCampo.getValor());
					break;
					case 186: // paterno
						paternoG.put(index,actoPredioCampo.getValor());
					break;
					case 794: // materno
						maternoG.put(index,actoPredioCampo.getValor());
					break;
				}
				if(campoId==187) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE "+ montoG;
				}
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
				//(formatoImporte.format(montoG2));
			break;

			case 219://USUFRUTO 20180 ,20181 ,20182, 20733
				switch (campoId) 
				{
				case 537:
				case 20733:
				case 20180:
					nombreG.put(index,actoPredioCampo.getValor());
					break;
				case 20181:
				case 538:
					paternoG.put(index,actoPredioCampo.getValor());
					break;
				case 20182:
				case 539:
					maternoG.put(index,actoPredioCampo.getValor());
					break;
				}
				break;
			case 223://RESERVA DOMINIO
				switch (campoId) 
				{
				case 537:
				case 1009:
				case 20180:
					nombreG.put(index,actoPredioCampo.getValor());
					break;
				case 1010:
				case 538:
					paternoG.put(index,actoPredioCampo.getValor());
					break;
				case 1011:
				case 539:
					maternoG.put(index,actoPredioCampo.getValor());
					break;
				}
				if(campoId==89 && actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().trim().isEmpty()) {
					montoG=CommonUtil.formatMoney(actoPredioCampo.getValor());
					//montoG2=new Double(montoG);
					//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
					montoG="POR LA CANTIDAD DE "+ montoG;//(formatoImporte.format(montoG2));
				}
				
				break;
			case 9://CRÉDITO O HIPOTECA
				switch (campoId) 
				{
				case 843:nombreG.put(index,actoPredioCampo.getValor()); break;
				case 844:paternoG.put(index,actoPredioCampo.getValor()); break;
				case 845:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}	
				if(campoId==124) {
					montoG= CommonUtil.formatMoney(actoPredioCampo.getValor());
                    //montoG2=new Double(montoG);
                    montoG="POR LA CANTIDAD DE "+ montoG;
				}
				//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
				 //(formatoImporte.format(montoG2));
				break;
			default://843, 844, 845
			
				int auxMonto=0;
				//360,361,376,689
				if(!montoG.isEmpty())
				{
			
					if(tipoMonto.contains("360")||tipoMonto.contains("361")||tipoMonto.contains("389")||tipoMonto.contains("376")) 
					{						
						montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE "+(montoG);
						//montoG_Aux=true;

					}else 
					{		
						//formatoImporte = NumberFormat.getCurrencyInstance();
						//formateador = new DecimalFormat("###,###.##");
						//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","MX"));						
						montoG=" EN GARANTIA DE PAGO POR LA CANTIDAD DE "+CommonUtil.formatMoney(montoG);
						//montoG_Aux=true;														
					}	
				}
				switch (campoId) 
				{
				case 843:nombreG.put(index,actoPredioCampo.getValor()); break;
				case 844:paternoG.put(index,actoPredioCampo.getValor()); break;
				case 845:maternoG.put(index,actoPredioCampo.getValor()); break;							
				}					
				break;
			}//actos
			if(vf==true){
				fechaG="";
				/* 20720	DIA, 20721	MES, 20722	AÑO				 */
				switch (campoId){
				case 20720:DIA=actoPredioCampo.getValor();  break;
				case 20721:CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor())); MES=cv.getNombre(); break;
				case 20722:ANIO=actoPredioCampo.getValor(); break;///
				case 20146: DocumentoG=actoPredioCampo.getValor(); break;
				case 20144: AnioG=actoPredioCampo.getValor(); break;
				case 20143: CampoValores cv2=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor())); SeccionG=cv2.getNombre(); break;
				case 20142: LibroG=actoPredioCampo.getValor(); break;
				case 20141: TomoG=actoPredioCampo.getValor(); break;
				case 20145: VolumenG=actoPredioCampo.getValor(); break;
				case 20160: BisG=actoPredioCampo.getValor(); break;					
				}	
				fechaG=DIA+"/"+MES+"/"+ANIO;
				auxVf=true;
				
				
				antecedentesCompleto=" INSCRITO BAJO EL NUMERO "+DocumentoG+" "+(BisG.contains("true")?"BIS":"")+" AÑO "+AnioG+" SECCION "+SeccionG+" LIBRO "+LibroG+" TOMO "+TomoG+" VOLUMEN "+VolumenG+" ";
				//Documento="",Bis="",Anio="",Seccion="",Libro="",Tomo="",Volumen="";
				
			}//vf 2	
			else{
				if(acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO) ||
						acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO2)) {
					fechaG=new SimpleDateFormat("dd/MM/YYYY").format(_prelacion.getFechaIngreso());
				}else if(acto.getFechaRegistro()!=null){
                    fechaG=new SimpleDateFormat("dd/MM/YYYY").format(acto.getFechaRegistro()); 
                }
               
            }

		} catch (NullPointerException e) {
			// TODO: handle exception
			log.debug("ACTO_PREDIO_CAMPO NULL REVISA");
		}		
		}
		
		try {
			if(!auxVf) {
			
				//actoPredio1.getActo().getPrelacion().getConsecutivo();
				antecedentesCompleto=" INSCRITO BAJO LA ENTRADA "+actoPredio1.getActo().getPrelacion().getConsecutivo()+" ";
			
				}
		} catch (NullPointerException e) {
			log.debug("ANTECEDENTES NULL REVISA");
			// TODO: handle exception
		}
		
		StringBuilder afavorDe = new StringBuilder();
		nombreG.forEach((index,name)->{
			if(name!=null && !name.isEmpty())
				afavorDe.append(name);
			
			String apaterno = paternoG.containsKey(index) ? paternoG.get(index) : "";
			String amaterno = maternoG.containsKey(index) ? maternoG.get(index) : "";
			if(apaterno!=null && !apaterno.isEmpty())
				 afavorDe.append(" ").append(apaterno);
			if(amaterno!=null && !amaterno.isEmpty())
				 afavorDe.append(" ").append(amaterno);
			
			afavorDe.append(", ");
			
		});
		
    return " EN FAVOR DE "+ afavorDe.toString().toUpperCase()	+" "+montoG + (tipoDeMoneda!=null && !tipoDeMoneda.isEmpty() ? " "+tipoDeMoneda :"") + antecedentesCompleto+" DE FECHA "+fechaG+"\n";
    }



    
    private List<TitularModel> buildTitularesPrelacionContratante(Predio predio) {
    	log.debug( "====> buildTitularesPrelacionContratante - predio = "+predio.getId() );
    	
    	List<TitularModel> titularesList = new ArrayList<TitularModel>();
		
    	List<PrelacionContratante> contratantesList = prelacionContratanteRepository
    			.findAllByPredioOrderByOrdenAsc(predio);

    	TitularModel titularModel;
    	double dd = 0;
    	double uv = 0;
    	for( PrelacionContratante contratante : contratantesList ) {
    		if( contratante.getActo().getStatusActo().getId() == 1 ) {
    			if( contratante.getDd() == null && contratante.getUv() == null ) {
    				continue;
    			}
    			if( contratante.getDd() != null ) {
					try {
	    				dd = Double.parseDouble( contratante.getDd() );
	    			} catch( NumberFormatException nfe ) {
	    			}
    			}
    			if( contratante.getUv() != null ) {
	    			try {
	        			uv = Double.parseDouble( contratante.getUv() );
	    			} catch( NumberFormatException nfe ) {
	    			}
    			}
    			titularModel = new TitularModel();
				titularModel.setNombre( contratante.getNombreCompleto() );
				//titularModel.setTipo( "PROPIETARIO" );
				titularModel.setDd( dd );
				titularModel.setUv( uv );
				titularesList.add( titularModel );
    		}
    	}
    	
		return titularesList;
	}

	private String buildFiador(ActoPredio actoPredio) {
    	log.debug( "====> buildFiador() = "+actoPredio.getId() );
    	
    	String leyenda = null;
    	
    	Hashtable<Integer,AcreditanteVO> hashTable = new Hashtable<Integer,AcreditanteVO>();
    	
    	boolean cazo = false;
    	String valor;
    	Integer orden;
    	int maxOrden = -1;
    	AcreditanteVO acreditanteVO;
    	int id;
    	Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
    	for( ActoPredioCampo apc : actoPredioCampos ) {
    		valor = apc.getValor();
    		if( valor == null ) 
    			continue;
    		orden = apc.getOrden();
    		if( orden == null ) {
    			continue;
    		}
    		if( maxOrden < orden ) {
    			maxOrden = orden;
    		}
    		acreditanteVO = hashTable.get( orden );
    		id = apc.getModuloCampo().getId().intValue();
    		if( id == 214 || id == 773 || id == 774 ) {
				cazo = true;
	    		if( acreditanteVO == null ) {
	    			acreditanteVO = new AcreditanteVO();
	    			hashTable.put(orden, acreditanteVO);
	    		}
    		}
    		switch( id ) {
    			case 214:
    				acreditanteVO.setNombre(valor);
    				break;
    			case 773:
    				acreditanteVO.setPaterno(valor);
    				break;
    			case 774:
    				acreditanteVO.setMaterno(valor);
    				break;
    		}
    	}

    	if( cazo ) {
    		leyenda = "";
	    	for( int i=1; i<=maxOrden; i++ ) {
	    		acreditanteVO = hashTable.get(i);
	    		if( acreditanteVO == null ) {
	    			continue;
	    		}
	    		leyenda += (i==1? "": ", ") +acreditanteVO.getNombreCompleto();
	    	}
    	}
    	
    	return leyenda!=null? leyenda.trim(): null;
	}

	private void agregaLeyendaBeneficiario(ActoPredio actoPredio, PrelacionBoletaRegistralVO pvo) {
    	
    	log.debug( "====> agregaBeneficiario - actoPredio = "+actoPredio.getId() );
		
    	Hashtable<Integer,Object[]> hashTable = new Hashtable<Integer,Object[]>();
    	
    	String valor;
    	Integer orden;
    	int maxOrden = -1;
    	int id;
    	Object[] nombreCampos;
    	boolean cazo = false;
    	
    	Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		for( ActoPredioCampo apc : actoPredioCampos ) {
			valor = apc.getValor();
			if( valor == null ) {
				continue;
			}
    		orden = apc.getOrden();
    		if( orden == null ) {
    			continue;
    		}
    		if( maxOrden < orden ) {
    			maxOrden = orden;
    		}
    		nombreCampos = hashTable.get( orden );
			id = apc.getModuloCampo().getId().intValue();
    		if( id == 87 || id == 88 || id == 143 || id == 741 || id == 742 ) {
				cazo = true;
	    		if( nombreCampos == null ) {
	    			nombreCampos = new Object[] { "","","",0d,0d };
	    			hashTable.put(orden, nombreCampos);
	    		}
    		}
			switch( id ) {
			case 87:
				try {
					nombreCampos[3] = Double.parseDouble( valor );
				} catch( NumberFormatException nfe ) {					
				}
				break;
			case 88:
				try {
					nombreCampos[4] = Double.parseDouble( valor );
				} catch( NumberFormatException nfe ) {					
				}
				break;
			case 143:
				nombreCampos[0] = valor;
				break;
			case 741:
				nombreCampos[1] = valor;
				break;
			case 742:
				nombreCampos[2] = valor;
				break;
			}
		}
		
		TitularModel titularModel;
		if( cazo ) {
			for( int i=1; i<=maxOrden; i++ ) {
				nombreCampos = hashTable.get(i);
				if( nombreCampos == null ) {
					continue;
				}
				/**
				titularModel = new TitularModel();
				titularModel.setNombre( (nombreCampos[0]+" "+nombreCampos[1]+" "+nombreCampos[2]).trim() );
				titularModel.setTipo( "CON CLAUSULA DE BENEFICIARIO" );
				titularModel.setDd( (double)nombreCampos[3] );
				titularModel.setUv( (double)nombreCampos[4] );
				pvo.getTitulares().add( titularModel );
				log.debug("===> Beneficiario ===> "+titularModel.getNombre() );				
				**/
				List<TitularModel> titulares = pvo.getTitulares();
				if( titulares != null && titulares.size() > 0 ) {
					titulares.get(titulares.size()-1).setTipo( "CON CLAUSULA DE BENEFICIARIO" );
				}
			}
		}

    }

    private String buildArrendatario(ActoPredio actoPredio) {    	
    	log.debug( "====> buildAcreditantes() = "+actoPredio.getId() );
    	
    	String leyenda = null;
    	
    	Hashtable<Integer,AcreditanteVO> hashTable = new Hashtable<Integer,AcreditanteVO>();
    	
    	boolean cazo = false;
    	String valor;
    	Integer orden;
    	int maxOrden = -1;
    	AcreditanteVO acreditanteVO;
    	int id;
    	Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
    	for( ActoPredioCampo apc : actoPredioCampos ) {
    		valor = apc.getValor();
    		if( valor == null ) 
    			continue;
    		orden = apc.getOrden();
    		if( orden == null ) {
    			continue;
    		}
    		if( maxOrden < orden ) {
    			maxOrden = orden;
    		}
    		acreditanteVO = hashTable.get( orden );
    		id = apc.getModuloCampo().getId().intValue();
    		if( id == 106 || id == 107 || id == 108) {
				cazo = true;
	    		if( acreditanteVO == null ) {
	    			acreditanteVO = new AcreditanteVO();
	    			hashTable.put(orden, acreditanteVO);
	    		}
    		}
    		switch( id ) { 
                case 106:
                    acreditanteVO.setNombre(valor);
                break;
                case 107:
                    acreditanteVO.setPaterno(valor);
                break;   			
    			case 108:
                    acreditanteVO.setMaterno(valor);
                break;
    		}
    	}

    	if( cazo ) {
    		leyenda = "";
	    	for( int i=1; i<=maxOrden; i++ ) {
	    		acreditanteVO = hashTable.get(i);
	    		if( acreditanteVO == null ) {
	    			continue;
	    		}
	    		leyenda += "  "+ acreditanteVO.getNombreCompleto();
	    	}
    	}
    	
    	return leyenda!=null? leyenda.trim(): null;
    }
    
	private String buildUsufructuario(ActoPredio actoPredio) {    	
    	log.debug( "====> buildAcreditantes() = "+actoPredio.getId() );
    	
    	String leyenda = null;
    	
    	Hashtable<Integer,AcreditanteVO> hashTable = new Hashtable<Integer,AcreditanteVO>();
    	
    	boolean cazo = false;
    	String valor;
    	Integer orden;
    	int maxOrden = -1;
    	AcreditanteVO acreditanteVO;
    	int id;
    	Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
    	for( ActoPredioCampo apc : actoPredioCampos ) {
    		valor = apc.getValor();
    		if( valor == null ) 
    			continue;
    		orden = apc.getOrden();
    		if( orden == null ) {
    			continue;
    		}
    		if( maxOrden < orden ) {
    			maxOrden = orden;
    		}
    		acreditanteVO = hashTable.get( orden );
    		id = apc.getModuloCampo().getId().intValue();
    		if( id == 20733 ) {
				cazo = true;
	    		if( acreditanteVO == null ) {
	    			acreditanteVO = new AcreditanteVO();
	    			hashTable.put(orden, acreditanteVO);
	    		}
    		}
    		switch( id ) {
    			case 127:
    				try {
    					int graduacionCreditoId = Integer.parseInt( valor );
    					CampoValores campoValores = campoValoresRepository.findOne( (long)graduacionCreditoId );
    					acreditanteVO.setGraduacionCredito(campoValores.getNombre().trim());
    				} catch( NumberFormatException nfe ) {
    					nfe.printStackTrace();
    				}
    				break;
    			case 20733:
    				acreditanteVO.setNombre(valor);
    				break;
    		}
    	}

    	if( cazo ) {
    		leyenda = "";
	    	for( int i=1; i<=maxOrden; i++ ) {
	    		acreditanteVO = hashTable.get(i);
	    		if( acreditanteVO == null ) {
	    			continue;
	    		}
	    		leyenda += "  " + acreditanteVO.getGraduacionCredito()+ 
	    				   " "+ acreditanteVO.getNombreCompleto();
	    	}
    	}
    	
    	return leyenda!=null? leyenda.trim(): null;
    }
    
	private String buildAcreditantes(ActoPredio actoPredio) {
    	
    	log.debug( "====> buildAcreditantes() = "+actoPredio.getId() );
    	
    	String leyenda = null;
    	
    	Hashtable<Integer,AcreditanteVO> hashTable = new Hashtable<Integer,AcreditanteVO>();
    	
    	boolean cazo = false;
    	String valor;
    	Integer orden;
    	int maxOrden = -1;
    	AcreditanteVO acreditanteVO;
    	int id;
    	Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
    	for( ActoPredioCampo apc : actoPredioCampos ) {
    		valor = apc.getValor();
    		if( valor == null ) 
    			continue;
    		orden = apc.getOrden();
    		if( orden == null ) {
    			continue;
    		}
    		if( maxOrden < orden ) {
    			maxOrden = orden;
    		}
    		acreditanteVO = hashTable.get( orden );
    		id = apc.getModuloCampo().getId().intValue();
    		if( id == 127 || id == 843 || id == 844 || id == 845 ) {
				cazo = true;
	    		if( acreditanteVO == null ) {
	    			acreditanteVO = new AcreditanteVO();
	    			hashTable.put(orden, acreditanteVO);
	    		}
    		}
    		switch( id ) {
    			case 127:
    				try {
    					int graduacionCreditoId = Integer.parseInt( valor );
    					CampoValores campoValores = campoValoresRepository.findOne( (long)graduacionCreditoId );
    					acreditanteVO.setGraduacionCredito(campoValores.getNombre().trim());
    				} catch( NumberFormatException nfe ) {
    					nfe.printStackTrace();
    				}
    				break;
    			case 843:
    				acreditanteVO.setNombre(valor);
    				break;
    			case 844:
    				acreditanteVO.setPaterno(valor);
    				break;
    			case 845:
    				acreditanteVO.setMaterno(valor);
    				break;
    		}
    	}

    	if( cazo ) {
    		leyenda = "";
	    	for( int i=1; i<=maxOrden; i++ ) {
	    		acreditanteVO = hashTable.get(i);
	    		if( acreditanteVO == null ) {
	    			continue;
	    		}
	    		leyenda += "  " + acreditanteVO.getGraduacionCredito()+ 
	    				   " "+ acreditanteVO.getNombreCompleto();
	    	}
    	}
    	
    	return leyenda!=null? leyenda.trim(): null;
	}

	public List<ReciboModel> buildRecibos(Long id) {

        List<Recibo> recibos = reciboService.findByPrelacionId(id);
        List <ReciboModel> recibosr =new ArrayList<ReciboModel>();
        if (recibos == null) {
            recibosr.add(new ReciboModel() {{
                setImporte(0.0);
                setFolio("");
            }});
            return recibosr;
        }
        Double total = recibos.stream()
                .collect(Collectors.summingDouble(
                        recibo -> (recibo.getMonto()!= null ? recibo.getMonto().doubleValue() : 0)
                ));
     int cont=1; String foliosPago ="";
     for(Recibo r:recibos ){
         if(cont==3){
             foliosPago=foliosPago+", "+r.getReferencia()+"\n";
             cont=1;
         }
         else{
            foliosPago=foliosPago+r.getReferencia()+", ";
         }
         cont++;
     }
     ReciboModel rm =new ReciboModel ();
     rm.setImporte(total);
     rm.setFolio(foliosPago);

        recibosr.add(rm);

        return recibosr;

    }

    private List<TitularModel> construirTitulares(HashMap<Integer, PredioPersona> titulares) {
        List<TitularModel> titularesModel = new ArrayList<>();
        if (titulares != null) {

            titulares.forEach( (index, value) -> {
                titularesModel.add(new TitularModel() {{
                    setNombre(getNombreCompletoTitular(value.getPersona()));
                    //setTipo(value.getTipoRelPersona().getNombre());
                    setTipo("TITULAR");

                    if  (value.getPorcentajeDd() != null )
                        setDd(value.getPorcentajeDd().doubleValue());
                    else
                        setDd(0.0);

                    if (value.getPorcentajeUv() != null)
                        setUv(value.getPorcentajeUv().doubleValue());
                    else
                        setUv(0.0);
                }});
            });

        }
        return titularesModel;
    }

    private List<TitularModel> buildTitularesFusion(Predio predio) {
        List<TitularModel> titulares = new ArrayList<>();
        System.out.println ( "###               ### ");
        System.out.println ( "###  Buscando Titulares ### ");       
        //Set<PredioPersona> list = actoPredioService.findPersonasbyPredioId(prel.getPredio().getId(), prel.getPrelacion().getid);
    	log.debug("Inicia findPropietariosActuales folio:"+predio.getNoFolio());    
        Set<PredioPersona> list = actoPredioRepository.findPersonasbyPredioId(predio.getId() );      
        System.out.println ( "predioPersona{} "+list);
        log.debug("Termina findPropietariosActuales ");
      
                list.forEach(prePer -> {
                	Double dd = prePer.getPorcentajeDd()!=null? prePer.getPorcentajeDd(): 0d;
                	Double uv = prePer.getPorcentajeUv()!=null? prePer.getPorcentajeUv(): 0d;
                    titulares.add( new TitularModel() {{                    	
                        setNombre( getNombreCompletoTitular(prePer.getPersona()));                        
                        //setTipo(prePer.getTipoRelPersona().getNombre() );
                        setDd(dd);
                        setUv(uv);
                    }});
                });
                log.debug("Titulares.size: "+titulares.size());
                log.debug("Titulares: "+titulares);
            
            return titulares;
        }
    private List<TitularModel> buildTitulares(List<PrelacionPredio> predios, Constantes.ETipoFolio tipo) {
        List<TitularModel> titulares = new ArrayList<>();

        /*
        if (tipo != Constantes.ETipoFolio.PREDIO)
            return null;
        */

        if (predios == null || (predios != null && predios.size() == 0)) {
            return titulares;
        }
        System.out.println ( "###               ### ");
        System.out.println ( "###  Buscando Titulares ### ");
        System.out.println ( "###               ### ");

        
        
        for (PrelacionPredio prel :predios){
            //Set<PredioPersona> list = actoPredioService.findPersonasbyPredioId(prel.getPredio().getId(), prel.getPrelacion().getid);

        	if (tipo == Constantes.ETipoFolio.PREDIO) {
        		log.debug("Inicia findPropietariosActuales folio:"+prel.getPredio().getNoFolio());
        		List<PredioPersona> list =null;
        		//AVM BOLETA FUSION
        		//List<ActoPredio> actoPredioModificatorio=actoPredioRepository.findAllByPredio(prel.getPredio().getId(), Constantes.StatusActo.ACTIVO.getIdStatusActo());
        		//if(actoPredioModificatorio!=null){
                  //list = predioPersonaService.findPropietariosActuales(prel.getPredio().getId(),false,actoPredioModificatorio);
                //}
        	
        		list = predioPersonaService.findPropietariosActuales(prel.getPredio().getId(),false);
        		log.debug("Termina findPropietariosActuales ");
                if (list == null)
                    continue;
                list.forEach(prePer -> {                   
                    Double dd = prePer.getPorcentajeDd()!=null? Double.valueOf(prePer.getPorcentajeDd().toString()): 0d;                    
                    Double uv = prePer.getPorcentajeUv()!=null? Double.valueOf(prePer.getPorcentajeUv().toString()): 0d;
                    titulares.add( new TitularModel() {{                    	
                        setNombre( getNombreCompletoTitular(prePer.getPersona()));                        
                        //setTipo(prePer.getTipoRelPersona().getNombre() );                        
                        setDd(dd);
                        setUv(uv);
                    }});
                });
                log.debug("Titulares.size: "+titulares.size());
                log.debug("Titulares: "+titulares);
        	} else if (tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS) {
        		log.debug( "====> ENTRO =====> " );
        		Set<PjPersona> list = pjPersonaRepository.findAllByPrelacionId(prel.getPrelacion().getId());
                if (list == null)
                    continue;
                list.forEach(prePer -> {
                    titulares.add( new TitularModel() {{
                        setNombre( getNombreCompletoTitular(prePer.getPersona()));
                    }});
                });
        	}



        } 
        return titulares;

    }
    
    private List<TitularModel> buildTitulares(Predio predio, boolean showFolio) {
        List<TitularModel> titulares = new ArrayList<>();
        List<PredioPersona>	list = predioPersonaService.findPropietariosActuales(predio.getId(),false);
        list.forEach(prePer -> {                   
                    Double dd = prePer.getPorcentajeDd()!=null? Double.valueOf(prePer.getPorcentajeDd().toString()): 0d;                    
                    Double uv = prePer.getPorcentajeUv()!=null? Double.valueOf(prePer.getPorcentajeUv().toString()): 0d;
                    titulares.add( new TitularModel() {{                    	
                        setNombre(showFolio ? "Folio: " + predio.getNoFolio()+" - "+ getNombreCompletoTitular(prePer.getPersona()) :getNombreCompletoTitular(prePer.getPersona()));                        
                        //setTipo(prePer.getTipoRelPersona().getNombre() );                        
                        setDd(dd);
                        setUv(uv);
                    }});
                });
        return titulares;

    }

    private String getNombreCompletoTitular (Persona persona ) {
        if(persona.getPaterno()==null){ persona.setPaterno("");} 
        if(persona.getMaterno()==null){ persona.setMaterno("");} 
        return persona.getNombre() + " " + persona.getPaterno() + " " + persona.getMaterno();
    }

    private List<AntecedenteModel> buildAntecedentes(List<PrelacionPredio> prelPred, Constantes.ETipoFolio tipo) {
        List<AntecedenteModel> antecedentes = new ArrayList<>();
        if (prelPred == null)
            return antecedentes;

        for (PrelacionPredio prel :prelPred){

            Antecedente ante= this.getAntecedenteByTipo(prel, tipo);

            if (ante == null)
                break;
            
            AntecedenteModel ant = null;
            if(ante.getLibro() != null) {
	            ant = new AntecedenteModel() {{
	                setLibro(ante.getLibro().getLibroBis());
	                setSeccion(ante.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
	                setOficina(ante.getLibro().getSeccionesPorOficina().getOficina().getNombre());
	                setVolumen(ante.getLibro().getVolumen());
	                setAnio(ante.getLibro().getAnio());
	                setTomo(ante.getLibro().getNumLibro());
	                setDocumento(ante.getDocumento());
	            }};
            }else {
            	ant = new AntecedenteModel() {{
	                setDocumento(ante.getDocumento());
	            }};
            }

            antecedentes.add(ant);
        }
        log.debug( "===> antecedentes = "+antecedentes.size() );
        
        return antecedentes.size() > 0? antecedentes: null;
    }

    private List<AntecedenteModel> buildAntecedentesFromPredio(List<PrelacionPredio> predios) {
        List<AntecedenteModel> antecedentes = new ArrayList<>();
        for(PrelacionPredio pp:predios){
            Predio pred=pp.getPredio();
            Libro lib= pred.getLibro();

            AntecedenteModel ant = new AntecedenteModel() {{
                setLibro(lib.getLibroBis());
                setSeccion(lib.getSeccionesPorOficina().getSeccion().getNombre());
                setOficina(lib.getSeccionesPorOficina().getOficina().getNumOficina());
                setVolumen(lib.getVolumen());
                setAnio(lib.getAnio());
                setTomo(lib.getNumLibro());
                setDocumento(pred.getDocumento());
            }};

            antecedentes.add(ant);
        }
        log.debug( "===> antecedentes from predio = "+antecedentes.size() );
        
        return antecedentes.size() > 0? antecedentes: null;
    }

    private Antecedente getAntecedenteByTipo(PrelacionPredio prel, Constantes.ETipoFolio tipo) {
        Antecedente ante = null;

        switch (tipo) {
            case PREDIO:
                ante =	antecedenteService.findAntecedenteByPredioId(prel.getPredio().getId());
                break;
            case MUEBLE:
                ante =	antecedenteService.findAntecedenteByMuebleId(prel.getMueble().getId());
                break;
            case PERSONAS_AUXILIARES:
                ante =	antecedenteService.findAntecedenteByFolioSeccionAuxiliarId(prel.getFolioSeccionAuxiliar().getId());
                break;
            case PERSONAS_JURIDICAS:
                ante = antecedenteService.findAntecedenteByPersonaJuridicaId(prel.getPersonaJuridica().getId());
                break;
            default:
                break;

        }

        return ante;
    }
    public String buildStringFirmaActosByActo(Acto a) {

        if (a == null)
            return "- SIN ESPECIFICAR -";

        StringBuilder sb = new StringBuilder();
        boolean tieneFirma = false;

            List<ActoFirma> firmas = actoService.getActosFirma((Long)a.getId());

            if (firmas == null )
            return "- SIN ESPECIFICAR -";

            for (ActoFirma firma : firmas) {
            	if( firma.getActo().getVf() != null && firma.getActo().getVf() ) {
            		continue;
            	}
            	if( firma.getFirma() != null ) {
            		sb.append( firma.getFirma() )
	            	  .append( "\r\n" );
            		tieneFirma = true;
            	}
            	if( tieneFirma ) {
            		break;
            	}
            }       
        

        if (sb.toString().length() == 0 )
        	sb.append( "- SIN FIRMAS - " );

        return sb.toString();
    }



    public String buildStringFirmaActos(long prelacionId) {

        List<Acto> actos = actoService.findByPrelacionId(prelacionId);
        if (actos == null || actos.size() == 0)
            return "- SIN ESPECIFICAR -";

        StringBuilder sb = new StringBuilder();
        boolean tieneFirma = false;

        for (Acto acto : actos) {

            List<ActoFirma> firmas = actoService.getActosFirma((Long)acto.getId());

            if (firmas == null )
                break;

            for (ActoFirma firma : firmas) {
            	if( firma.getActo().getVf() != null && firma.getActo().getVf() ) {
            		continue;
            	}
            	if( firma.getFirma() != null ) {
            		sb.append( firma.getFirma() )
	            	  .append( "\r\n" );
            		tieneFirma = true;
            	}
            	if( tieneFirma ) {
            		break;
            	}
            }
        	if( tieneFirma ) {
        		break;
        	}
        }

        if (sb.toString().length() == 0 )
        	sb.append( "- SIN FIRMAS - " );

        return sb.toString();
    }

    private String buildLeyendaRegistro (Date fecha) {

        if (fecha == null)
            return "FECHA NO VÁLIDA";

        Calendar date = new GregorianCalendar();
        date.setTime(fecha);

        String dia = String.valueOf(date.get(Calendar.DAY_OF_MONTH) );
        String[] meses = new String[] {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};
        String mes = meses[date.get(Calendar.MONTH)];
        String anio = String.valueOf(date.get(Calendar.YEAR) );
        String hora = String.format("%1$tl:%1$tM %1$tp",fecha);

        return "SE PRESENTÓ PARA SU INGRESO EL " + dia + " DE " + mes + " DE " + anio  + " A LAS " + hora;

    }
    
    public String buildAsiento (Date fecha) {

        if (fecha == null)
            return "FECHA NO VÁLIDA";

        Calendar date = new GregorianCalendar();
        date.setTime(fecha);

        String dia = String.valueOf(date.get(Calendar.DAY_OF_MONTH) );
        String[] meses = new String[] {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};
        String mes = meses[date.get(Calendar.MONTH)];
        String anio = String.valueOf(date.get(Calendar.YEAR) );
        String hora = String.format("%1$tl:%1$tM %1$tp",fecha);

        return dia + "/" + mes + "/" + anio ;

    }
    
    public String buildAsientoText(Date fecha) {

        if (fecha == null)
            return "FECHA NO VÁLIDA";

        Calendar date = new GregorianCalendar();
        date.setTime(fecha);

        String dia = String.valueOf(date.get(Calendar.DAY_OF_MONTH) );
        String[] meses = new String[] {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};
        String mes = meses[date.get(Calendar.MONTH)];
        String anio = String.valueOf(date.get(Calendar.YEAR) );
        String hora = String.format("%1$tl:%1$tM %1$tp",fecha);

        return dia + " DE " + mes + " DE " + anio ;

    }

    public byte[] generPdfRespuesta(Long idPrelacion, boolean conRespuestaAutomatica, boolean boletaRegistral,boolean qr,Long predioId) throws Exception{
        
        Prelacion prelacion = prelacionRepository.findOne(idPrelacion);
    	Set<PrelacionServicio> servicios = prelacion.getPrelacionServiciosParaPrelacions();
    	byte[] result = null;
		Servicio s= null;
		boolean withRechazo = true;
		byte[] pdfRechazos = prelacionService.generaPdfBoletaRechazos(idPrelacion,qr);
		boolean existRechazo = pdfRechazos!=null && pdfRechazos.length>0;
        if(servicios!=null && servicios.size()>0 ){
                PrelacionServicio ps=servicios.iterator().next();
                s=ps.getServicio();
                
          if(s.getId().equals(Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_CAUTELAR_ID.getIdServicio())) {
        	  if(!existRechazo) {
        	   if(predioId==null)
        		  result = certificadoService.genBoletaCertificadoInserto(idPrelacion,qr);
        	   else
        		  result = certificadoService.genBoletaCertificadoInsertoConPredio(idPrelacion, qr, predioId);
        	   withRechazo=false;
        	  }
          }  
          
          if(s.getId().equals(Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_ID.getIdServicio()) ) {
        	  if(!existRechazo) {
	            if(predioId==null)
	                result = certificadoService.getPdfCertificadoLg(prelacion,qr);
	            else
	                result = certificadoService.getPdfCertificadoLgConPredio(prelacion,qr,predioId);
        	  }
          }
          
          if(s.getId().equals(Constantes.Servicio.CERTIFICADO_LIBERTAD_NO_INSCRIPCION.getIdServicio())) {
        	  if(!existRechazo) {
        		  result = certificadoService.getPdfCertificadoInscripcionNoInscripcion(idPrelacion,qr);
        	  } 
          }
          
          if(s.getId().equals(Constantes.Servicio.CERTIFICADO_LIBERTAD_NO_PROPIEDAD.getIdServicio())) {
        	  if(!existRechazo) {
        		  result = certificadoService.getPdfCertificadoPropiedadNoPropiedad(idPrelacion,qr);
        	  } 
          }
          
          if(s.getId().equals(Constantes.Servicio.IMPRESION_FOLIOS.getIdServicio())) {
        	  if(!existRechazo) {
        	    result = caratulaService.genBoletaImpresionFolios(idPrelacion,qr);
        	  }
          }
          
          if(s.getArea()!=null && s.getArea().getId().equals(Constantes.Area.ACTOS_PUBLICITARIOS.getIdArea())) {
        	  if(!s.getId().equals(Constantes.Servicio.COPIA_CERTIFICADA_DE_ACTOS_PUBLICITARIOS.getIdServicio())) {
        		  
        		  if(!qr) {
        		  result = prelacionService.getPdfBoletaRegistralPorActo(idPrelacion,false,null);  
        		  }else 
        		  {
        				PrelacionBoleta prelacionBoletas= prelacionBoletaService.findFirstByPrelacion(idPrelacion,0);
        				if(prelacionBoletas!=null){			
        						if(prelacionBoletas.getDatos()!=null && !prelacionBoletas.getDatos().isEmpty()){
        							List<PrelacionBoletaRegistralVO> list=prelacionBoletaService.jsonTOBoletaRegistral(prelacionBoletas.getDatos());//JSON
        							result=prelacionService.getPdfBoletaRegistralPorActoJSON(list,0,qr);	
        						}
        				}
        		  }
        		  
        	  }else {
        		  if(!existRechazo) {
        		    result  = certificadoService.printActoPublicitarioCertificadoByActoPublicitarioId(idPrelacion,qr);
        		  }
        	  }
          }
          
          if(s.getId().equals(Constantes.Servicio.BUSQUEDA_HISTORIAL.getIdServicio())) {
        	  if(!existRechazo) {
        		  result = busquedaService.getPdfBusqueda(idPrelacion,qr);
        	  }
          }
          
          if(s.getId().equals(Constantes.Servicio.COPIAS_CERTIFICADA.getIdServicio())) {
        	  if(!existRechazo) {
           	    result = copiasService.getImagesBytesSeleccionadasByPrelacion(idPrelacion);
           	    byte[] certificacion = certificadoService.getPdfCertificadoCopia(idPrelacion,qr);
           	    result = pdfService.appendPDF(certificacion, result);
        	  }
          }
          
       }
       else
       {    
    	   
    	   if(!qr) 
    	   {
    	   Page<Acto> actos = actoService.getActosPageReport(idPrelacion,100);
    	   List<Integer> paginas =  new ArrayList<>();
    	   for(int i=0;i<actos.getTotalPages();i++) {
				paginas.add(i);
    	   }
    	   for(Integer pagina:paginas) {
    		   byte[] x = prelacionService.getPdfBoletaRegistralPorActo(idPrelacion,true,pagina);
    		   result = result!= null && result!=null ? pdfService.appendPDF(result,x) : 
    			   		result==null && x!=null ? x : result ;
    	   }
    	   }else {
    		   
    		  Optional<PrelacionBoleta> prelacionBoleta = prelacionBoletaService.findFirstByPrelacionIdOrderByPaginaDesc(idPrelacion);
    		  
    		  if(prelacionBoleta.isPresent()) 
    		  {
    			  Integer paginas = prelacionBoleta.get().getPagina()+1; 
    			  for(int pagina = 0;pagina < paginas;pagina++) 
    			  {
    				  List<PrelacionBoleta> prelacionBoletas= prelacionBoletaService.findAllByPrelacion(idPrelacion,pagina);	 
    	  	   			if(prelacionBoletas!=null && prelacionBoletas.size()>0){
    	  	   				for(PrelacionBoleta pb: prelacionBoletas){
    	  	   					log.info("prelacionBoletaId encontrado: "+pb.getId());
    	  	   					if(pb.getDatos()!=null && !pb.getDatos().isEmpty()){
    	  	   						List<PrelacionBoletaRegistralVO> list=prelacionBoletaService.jsonTOBoletaRegistral(pb.getDatos());//JSON
    	  	   					   byte[] x =prelacionService.getPdfBoletaRegistralPorActoJSON(list,pagina,qr);
    	  	   					   result = result!= null && result!=null ? pdfService.appendPDF(result,x) : 
    	         			   		result==null && x!=null ? x : result ;		
    	  	   					}
    	  	   				}
    	  	   	
    	  	   			} 
    			  } 
    		  }
    		  
    		  
    	   }
    	   
       }
        
        
       if(withRechazo)
    	   result = result!=null && pdfRechazos!=null ? pdfService.appendPDF(result,pdfRechazos) : 
		       result==null && pdfRechazos!=null ? pdfRechazos : result ;
    	   
      return result;

    }




    public byte[] generPdfBoletaRegistral(Long idPrelacion) throws JRException{
   
    	System.out.println("metodo: generPdfBoletaRegistral");
    			
    	//PrelacionBoletaRegistralVO prelacion = prelacionService.findOneVOSI(idPrelacion);

        List<PrelacionBoletaRegistralVO> prelacions = prelacionService.getBoletasRegistral(idPrelacion,false,null);
        String uri= parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/prelacion/boletaregistral/{tipoId}/{prelacionId}";
		JRDataSource ds = new JRBeanCollectionDataSource(prelacions);

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+idPrelacion;
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
        parameterMap.put("uri", uri);
    	parameterMap.put("QR_BASE_URI", qR);
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfBoletaRegistralSI.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public byte[] generPdfBoletaIngreso(Long idPrelacion) throws JRException{
   
    	
    			
    	
		PrelacionIngresoVO prelacion = prelacionService.findOneVO(idPrelacion);

		List<PrelacionIngresoVO> prelacions = new ArrayList<PrelacionIngresoVO>();
		prelacions.add(prelacion);
		JRDataSource ds = new JRBeanCollectionDataSource(prelacions);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfIngresoPrelacion.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    
    
    public byte[] generPdfBoletaRegistral(Map<String, Object> parameterMap ,  JRDataSource ds) throws JRException{
    	   
        
        
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfBoletaRegistralSI.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    
    public byte[] generPdfBoletaIngreso(Map<String, Object> parameterMap ,  JRDataSource ds) throws JRException{
    	   
		
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfIngresoPrelacion.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    
	
	@Transactional
    public void enviaBoleta(Prelacion prelacion , int tipo){
		try{
			
			
			//TODO llamar a prelacionService para avanzar prelacion al status correspondiente
			
	
			String to = prelacion.getUsuarioSolicitan().getEmail();
			String body = "Test";									
				
			if (to==null || to.length()==0){
				log.debug("No se envio correo");
			} else {
				log.debug("tratando de enviar email a " + to);
				
		        Context context = new Context();
		        context.setVariable("nombre", prelacion.getUsuarioSolicitan().getNombreCompleto());
		        context.setVariable("consecutivo", prelacion.getConsecutivo());

		        switch (tipo) {
				case 1:
					
                    mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor() , to, "Ingreso de la Entrada "+ prelacion.getConsecutivo(), "boletaRegistralTemplate", context, generPdfBoletaIngreso(prelacion.getId()));
                       
                    break;
				
				case 2:
				
					mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor() , to, "Respuesta de la Entrada "+ prelacion.getConsecutivo(), "boletaRegistralTemplate", context, generPdfRespuesta(prelacion.getId(),false,true,false,null));
					prelacion.setStatus(statusRepository.findOne(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()));
                    prelacionRepository.save(prelacion); 
                    bitacoraRepository.save(prelacionService.createBitacora(prelacion));
					break;
				
                }
                

                
			}
			
		} catch(IllegalArgumentException | JRException | MessagingException e){
			
			//prelacionService.enviarATurnador(prelacion);
			//TODO enviar a TURNADOR, dala implementar cambios a la prelacion, para que no se cicle, envio y recepcion
		} catch (Exception e) {
			e.printStackTrace();
			//prelacionService.enviarATurnador(prelacion);
		}
    	
    }

    @Transactional
	public void enviaBoletaOnlyActo(Prelacion prelacion, int tipo, Long actoId) {
		System.out.println("%%% enviaBoletaOnlyActo() %%%");
		try {

			// TODO llamar a prelacionService para avanzar prelacion al status
			// correspondiente

			String to = prelacion.getUsuarioSolicitan().getEmail();

			String body = "Test";

			if (to == null || to.length() == 0) {
				log.debug("No se envio correo");
			} else {
				log.debug("tratando de enviar email a " + to);

                SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
                //IHM 21-03-2019
                /*
				Context context = new Context();
				context.setVariable("nombre", prelacion.getUsuarioSolicitan().getPersona().getNombre());
				context.setVariable("paterno", prelacion.getUsuarioSolicitan().getPersona().getPaterno());
				context.setVariable("consecutivo", prelacion.getConsecutivo());
				context.setVariable("fecha", formatterFecha.format(prelacion.getFechaIngreso()));
                */
				switch (tipo) {
				case 1:
                    //IHM 21-03-2019
                    /*
					mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor(),
							to, "Ingreso de la Prelación " + prelacion.getConsecutivo(), "boletaIngresoTemplate",
							context, generPdfBoletaIngreso(prelacion.getId()));
                    */
					break;

				case 2:
					// JADV-SUSPENSION
					boolean esRechazoSuspension = false;

					Suspension suspension = suspencionRepository.findOneByestatusSuspensionAndPrelacion(2L, prelacion);

					if (suspension != null) {
						esRechazoSuspension = true;
						/*
						 * for (Acto acto : prelacion.getActosParaPrelacions()) { StatusActo statusActo
						 * = statusActoRepository.findOne(5L); acto.setStatusActo(statusActo);
						 * actoRepository.save(acto);
						 * 
						 * }
						 */
					}

					boolean esRechazo = false;
					for (Acto acto : prelacion.getActosParaPrelacions()) {
						if (acto.getStatusActo().getId().equals(5L)) {
							esRechazo = true;
						}
					}

					boolean esCertificado = false;
					boolean esCertificadoGravamen = false;
					for (Acto acto : prelacion.getActosParaPrelacions()) {
						if (acto.getTipoActo().getClasifActo().getId() == 9) {
							esCertificado = true;
							if (acto.getTipoActo().getId() == 203) {
								esCertificadoGravamen = true;
							}
						}
					}
					
					// JADV-SUSPENSION
					if (esRechazo || esRechazoSuspension) {

						List<byte[]> restRechazo = getPdfBoletaRechazo(prelacion);
                        //IHM 21-03-2019
						/*if (restRechazo != null) {
						 *	System.out.println("*** BOLETA DE RECHAZO NOT NULL");
                         *  
						 *	mailSenderService.sendMailWithListAttachment(
						 *			parametroRepository.findByCve("MAIL_USERNAME").getValor(), to,
						 *			"Rechazo de la Prelación " + prelacion.getConsecutivo(), "boletaRegistralTemplate",
						 *			context, restRechazo);
                        }*/

						prelacion.setStatus(statusRepository.findOne(10L));
						prelacionRepository.save(prelacion);
						bitacoraRepository.save(prelacionService.createBitacora(prelacion));

						prelacion.setStatus(
								statusRepository.findOne(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()));
						prelacionRepository.save(prelacion);
						bitacoraRepository.save(prelacionService.createBitacora(prelacion));

					} else if (esCertificado) {
						if (esCertificadoGravamen) {
                            //IHM 21-03-2019
							//List<byte[]> rest = GeneraCertificadoGravamenMultiple(prelacion);
                            //if (rest != null)                                
                                /*
								mailSenderService.sendMailWithListAttachment(
										parametroRepository.findByCve("MAIL_USERNAME").getValor(), to,
										"Certificado de la Prelación " + prelacion.getConsecutivo(),
                                        "boletaRegistralTemplate", context, rest);
                                        */
							prelacion.setStatus(
									statusRepository.findOne(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()));
							prelacionRepository.save(prelacion);
							bitacoraRepository.save(prelacionService.createBitacora(prelacion));
						} else {
                            //IHM 21-03.2019
                            /*
							byte[] rest = getPdfCertificado(prelacion);// generPdfRespuesta(prelacion.getId(),false,false);
															
							prelacion.setStatus(
									statusRepository.findOne(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()));
							prelacionRepository.save(prelacion);
							bitacoraRepository.save(prelacionService.createBitacora(prelacion));

							if (rest != null) {                                
								mailSenderService.sendMailWithAttachment(
										parametroRepository.findByCve("MAIL_USERNAME").getValor(), to,
										"Certificado de la Prelación " + prelacion.getConsecutivo(),
										"boletaRegistralTemplate", context, rest);
                            }
                            */
						}

					} else {
                         //IHM 21-03-2019
                         /*
						  *mailSenderService.sendMailWithAttachment(
						  *	parametroRepository.findByCve("MAIL_USERNAME").getValor(), to,
						  *	"Respuesta de la Prelación " + prelacion.getConsecutivo(), "boletaRegistralTemplate",
                          * context, generPdfRespuestaOnlyActo(prelacion.getId(), false, true, actoId));
                          */        
						prelacion.setStatus(
								statusRepository.findOne(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()));
						prelacionRepository.save(prelacion);
						bitacoraRepository.save(prelacionService.createBitacora(prelacion));
					}
					break;
				}
			}
		} catch (IllegalArgumentException | JRException e) {
			System.out.println(e.getStackTrace());				
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @SuppressWarnings("unchecked")
	public byte[] getPdfBoletaSuspencion(Prelacion prelacion) throws JRException {

		List<Acto> listActos = new ArrayList<Acto>();
		Set<Acto> acto = prelacion.getActosParaPrelacions();
		listActos.addAll(acto);

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		InputStream jasperStream = null;
		JRDataSource ds = null;
		Map<String, Object> parameterMap = new HashMap<String, Object>();

		byte[] suspencionDoc = null;
		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			BoletaSuspensionVO boleta = findOneVOBoletaSuspension(prelacion);

			List<BoletaSuspensionVO> prelacions = new ArrayList<BoletaSuspensionVO>();
			prelacions.add(boleta);

			ds = new JRBeanCollectionDataSource(prelacions);

			parameterMap.put("datasource", ds);
			parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
			parameterMap.put("imgPath", Constantes.IMG_PATH);
			parameterMap.put(JRParameter.REPORT_LOCALE, Constantes.locale);
			parameterMap.put("QR_BASE_URI", parametroRepository.findByCve("QR_BASE_URI").getValor());
			jasperStream = this.getClass().getResourceAsStream("/reports//pdfBoletaSuspension.jasper");

			if (jasperStream != null) {
				jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
				byte[] doc = JasperExportManager.exportReportToPdf(jasperPrint);
				outputStream.write(doc);
				suspencionDoc = outputStream.toByteArray();
				outputStream.flush();
			}

			return suspencionDoc;// outputStream.toByteArray();
		} catch (Exception ex) {

			System.out.println("Excepcion de al generar boleta suspension " + ex.getMessage());
			return null;

		}

	}
	
	@Transactional
    public void enviaNotificacion(Prelacion prelacion ){
		try{
			
			
			//TODO llamar a prelacionService para avanzar prelacion al status correspondiente
			
	
			String to = prelacion.getUsuarioSolicitan().getEmail();
			String body = "Test";									
				
			if (to==null || to.length()==0){
				log.debug("No se envio correo");
			} else {
				log.debug("tratando de enviar email a " + to);
				
		        Context context = new Context();
		        context.setVariable("nombre", prelacion.getUsuarioSolicitan().getNombreCompleto());
		        context.setVariable("consecutivo", prelacion.getConsecutivo());
		        context.setVariable("estado", prelacion.getStatus().getNombre());

		    
				mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor() , to, "Cambio de estado  de la Entrada "+ prelacion.getConsecutivo(), "notificacionTemplate", context);
				
				
			
			}
			
		} catch(IllegalArgumentException  | MessagingException e){
			
			//prelacionService.enviarATurnador(prelacion);
			//TODO enviar a TURNADOR, dala implementar cambios a la prelacion, para que no se cicle, envio y recepcion
		} catch (Exception e) {
			e.printStackTrace();
			//prelacionService.enviarATurnador(prelacion);
		}
    	
    }
    
    public void enviaNotificacionSuspencion(Prelacion prelacion) {
		try {

			// TODO llamar a prelacionService para avanzar prelacion al status
			// correspondiente

			byte[] boletaSuspencion = getPdfBoletaSuspencion(prelacion);

			String to = prelacion.getUsuarioSolicitan().getEmail();
			String body = "Test";

			if (to == null || to.length() == 0) {
				log.debug("No se envio correo");
			} else {
				log.debug("tratando de enviar email a " + to);

				SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");

				Context context = new Context();
				context.setVariable("nombre", prelacion.getUsuarioSolicitan().getPersona().getNombre());
				context.setVariable("paterno", prelacion.getUsuarioSolicitan().getPersona().getPaterno());
				context.setVariable("consecutivo", prelacion.getConsecutivo());
				context.setVariable("fecha", formatterFecha.format(prelacion.getFechaIngreso()));
				context.setVariable("fechaSuspension", formatterFecha.format(new Date()));

				mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor(), to,
						"Suspension de la Prelación " + prelacion.getConsecutivo(), "boletaSuspensionTemplate", context,
						boletaSuspencion);

			}

		} catch (IllegalArgumentException | MessagingException e) {

			// prelacionService.enviarATurnador(prelacion);
			// TODO enviar a TURNADOR, dala implementar cambios a la prelacion, para que no
			// se cicle, envio y recepcion
		} catch (Exception e) {
			e.printStackTrace();
			// prelacionService.enviarATurnador(prelacion);
		}

	}
	
	@Transactional
    public void enviaBoleta(Long prelacionId , int tipo , byte[] report, Constantes.ETipoFolio tipoFolio){
		try{
			Prelacion prelacion = prelacionRepository.findOne(prelacionId);
			
			
			//byte[] report = generPdfBoletaRegistral(prelacion.getId());
			
			//TODO llamar a prelacionService para avanzar prelacion al status correspondiente
			prelacion.setStatus(statusRepository.findOne(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()));
			prelacionRepository.save(prelacion);
	
			String to = prelacion.getUsuarioSolicitan().getEmail();
			String body = "Test";									
				
			if (to==null || to.length()==0){
				log.debug("No se envio correo");
			} else {
				log.debug("tratando de enviar email a " + to);
				
		        Context context = new Context();
		        context.setVariable("nombre", prelacion.getUsuarioSolicitan().getNombreCompleto());
		        context.setVariable("consecutivo", prelacion.getConsecutivo());
		        
		        switch (tipo) {
				case 1:
					mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor() , to, "Ingreso de la Entrada "+ prelacion.getConsecutivo(), "boletaRegistralTemplate", context,report);
					break;
				
				case 2:
					mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor() , to, "Bolera registral de la Entrada "+ prelacion.getConsecutivo(), "boletaRegistralTemplate", context,report);
					break;
				
				}
				
			}
			
		} catch(IllegalArgumentException  | MessagingException e){
			
			//prelacionService.enviarATurnador(prelacion);
			//TODO enviar a TURNADOR, dala implementar cambios a la prelacion, para que no se cicle, envio y recepcion
		} catch (Exception e) {
			e.printStackTrace();
			//prelacionService.enviarATurnador(prelacion);
		}
    	
    }

    public byte[] getBoletaRechazo(Long actoId,boolean marcaAgua) throws JRException {
        JasperPrint jasperPrint = null;
        JasperReport jasperReport = null;
        InputStream jasperStream = null;
        JRDataSource ds = null;
        BoletaRechazoVO boleta = findOneVOBoletaRechazo(actoId);
        Acto act = actoRepository.findOne(actoId);
        List<BoletaRechazoVO> prelacions = new ArrayList<BoletaRechazoVO>();
        prelacions.add(boleta);

        ds = new JRBeanCollectionDataSource(prelacions);

        Map<String, Object> parameterMap = new HashMap<String, Object>();

        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
        if(marcaAgua)
        	parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
        
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+act.getPrelacion().getId();
		parameterMap.put("QR_BASE_URI", qR);
        jasperStream = this.getClass().getResourceAsStream("/reports/pdfBoletaRechazo.jasper");
        jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

        return JasperExportManager.exportReportToPdf(jasperPrint);

    }

    private HashMap<Integer, PredioPersona> buscarTitutlaresFromActo (Acto acto) {
    	
    	log.debug( "===> buscarTitutlaresFromActo - actoId = "+acto.getId() );
    	
        ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
        
        log.debug( "===> ActoPredio ID="+actoPredio.getId() );

        if (actoPredio.getPredio() == null) {
            throw new IllegalArgumentException("El acto no se le asociado un predio");
        }

        // buscar ultimos traslativos para cambiarlo a no vigente
        /*List<Acto> actosTraslativo = actoService.getActosVigentesTraslativosParaPredio(actoPredio.getPredio().getId());

        log.debug("total de actos_traslativos_Activos={}", actosTraslativo.size());
        StatusActo noVigente = statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo());
        for (Acto actoTraslativo : actosTraslativo) {
            log.debug("acto_traslativo {}", actosTraslativo);
            actoTraslativo.statusActo(noVigente);
            //actoRepository.save(actoTraslativo);
        }
        */
        log.debug("actoPredio= {}", actoPredio);

        Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

        HashMap<Integer, PredioPersona> adquitientes = new HashMap<Integer, PredioPersona>();
        HashMap<Integer, PredioPersona> titulares = new HashMap<Integer, PredioPersona>();

        actoPredioCampos.forEach((actoPredioCampo) -> {
            long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();

            // actoPredioCampo.getValor());
            int index = actoPredioCampo.getOrden();

            //log.debug("{} = {}", campoId, actoPredioCampo.getValor());
            
            if( actoPredioCampo.getValor() != null ) {

		        if (campoId == NOMBRE || campoId == PATERNO || campoId == MATERNO || campoId == FECHA_NACIMIENTO || campoId == RFC || campoId == CURP || campoId == ESTADO_CIVIL || campoId == REGIMEN || campoId == NACIONALIDAD || campoId == TIPO_DE_PERSONA || campoId == DD || campoId == UV) {
		            PredioPersona predioPersona = adquitientes.get(index);
		            if (predioPersona == null) {
		                predioPersona = new PredioPersona();
		                predioPersona.setPersona(new Persona());
		                adquitientes.put(index, predioPersona);
		            }
		
		            if (campoId == NOMBRE) {
		                predioPersona.getPersona().setNombre(actoPredioCampo.getValor());
		            } else if (campoId == PATERNO) {
		                predioPersona.getPersona().setPaterno(actoPredioCampo.getValor());
		            } else if (campoId == MATERNO) {
		                predioPersona.getPersona().setMaterno(actoPredioCampo.getValor());
		            } else if (campoId == FECHA_NACIMIENTO) {
		                predioPersona.getPersona().setFechaNac(UtilFecha.toDate(actoPredioCampo.getValor(), FECHA_FORMATO_CAMPO_VALOR));
		            } else if (campoId == RFC) {
		                predioPersona.getPersona().setRfc(actoPredioCampo.getValor());
		            } else if (campoId == CURP) {
		                predioPersona.getPersona().setCurp(actoPredioCampo.getValor());
		            } else if (campoId == ESTADO_CIVIL) {
		                // TODO : el estado civil es un tipo??
		                // predioPersona.setEstadoCivil(estadoCivilRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
		            } else if (campoId == REGIMEN) {
		                predioPersona.setRegimen(regimenRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
		            } else if (campoId == NACIONALIDAD) {
		                Nacionalidad nacionalidad = nacionalidadRepository.findOne(Long.valueOf(actoPredioCampo.getValor()));
		                predioPersona.getPersona().setNacionalidad(nacionalidad);
		                predioPersona.setNacionalidad(nacionalidad);
		            } else if (campoId == TIPO_DE_PERSONA) {
		                predioPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Long.valueOf(actoPredioCampo.getValor())));
		            } else if (campoId == DD) {
		                predioPersona.setPorcentajeDd(Float.valueOf(actoPredioCampo.getValor()));
		            } else if (campoId == UV) {
		                predioPersona.setPorcentajeUv(Float.valueOf(actoPredioCampo.getValor()));
		            }
		
		        }
		
		        Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
		        if (tipoCampo == null) {
		            log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}", actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
		
		        } else {
		            if (tipoCampo == tipoCampo.PERSONA_PREDIO_TITULAR || tipoCampo == tipoCampo.DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR || tipoCampo == tipoCampo.USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR) {
		                PredioPersona predioPersona = titulares.get(index);
		                if (predioPersona == null) {
		                    predioPersona = new PredioPersona();
		                    titulares.put(index, predioPersona);
		                }
		
		                switch (tipoCampo) {
		                    case PERSONA_PREDIO_TITULAR:
		                        log.debug (" ### PERSONA_PREDIO_TITULAR : {}",actoPredioCampo.getValor() );
		                        predioPersona.setPersona(personaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
		                        break;
		                    case DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR:
		                        log.debug (" ### DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR : {}",actoPredioCampo.getValor() );
		                        predioPersona.setPorcentajeDd(Float.valueOf(actoPredioCampo.getValor() != null ? actoPredioCampo.getValor() : "0"));
		                        break;
		                    case USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR:
		                        log.debug (" ### USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR : {}",actoPredioCampo.getValor() );
		                        predioPersona.setPorcentajeUv(Float.valueOf(actoPredioCampo.getValor() != null ? actoPredioCampo.getValor() : "0"));
		                        break;
		                }
		            }
		        }
		    }

        });

        return adquitientes;
    }
    private  ArrayList<ActoPredioCampo> getActoPredioCampo(Acto acto){
        ArrayList<ActoPredioCampo> actoPredioCampos = null;
        if(acto!=null){    
            actoPredioCampos =new ArrayList();
            ActoPredio actoPredio = actoPredioRepository.findActoPredioByLastVersion(acto.getId());
         //   log.debug("BolRegSer:356 {}", actoPredio);  
                for(ActoPredioCampo apc:actoPredioCampoRepository.findAllByActoPredioOrderById(actoPredio)){
                    actoPredioCampos.add(apc);
                 //   log.debug("BolRegSer:360{}", apc);s
                    }
                }
    return actoPredioCampos;
    }
    private String getFundatorioActoSubdivision( Acto acto){
        List<ActoDocumento> docus = actoDocumentoService.getAllActoDocumentoByActoId(acto.getId());
        String fundatorio = "\n";
        String texto1 = "";
        String texto2 = "";
        Municipio municipio = null;        
        Integer noNotario = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");        
        String estadoNombre = "-";
        String municipioNombre = "-";
        int tipoDocumentoId;
        System.out.println("docus------"+docus);
        for(ActoDocumento aDoc: docus) {
            Documento doc = aDoc.getDocumento();
            String numerox=doc.getNumero2();
            Date fecha = doc.getFecha();
            Boolean bis = doc.isBis();
            Notario notario = doc.getNotario();
            texto1 += "POR ESCRITURA PUBLICA NÚMERO "+ (numerox!=null? numerox: "-") +
            (bis!=null && bis==true? " BIS": "") +
            ", DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
            if( notario != null ) {
                municipio = notario.getMunicipio();
                if( municipio != null ) {
                    municipioNombre = municipio.getNombre().trim();
                    estadoNombre = municipio.getEstado().getNombre().trim();
                }
                noNotario = notario.getNoNotario();
                texto2 = ", OTORGADO ANTE LA FE DEL LIC. " + notario.getNombreCompleto() +
                    ", NOTARIO PUBLICO "+ (noNotario!=null? noNotario: "-") +
                    ", DE " + municipioNombre + ", " + estadoNombre;
            } else {
                 texto2 = " - NO HAY INFORMACION DE NOTARIO - ";
            }       
         }
               

        return fundatorio += texto1 + texto2 + ".\r\n\r\n";
    }
    private String getFundatorioActoFusion(String noFolio, Acto acto){
        List<ActoDocumento> docus = actoDocumentoService.getAllActoDocumentoByActoId(acto.getId());
        String fundatorio = "\n";
        String texto1 = "FOLIO :"+ noFolio + " ";
        String texto2 = "";
        Municipio municipio = null;        
        Integer noNotario = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");        
        String estadoNombre = "-";
        String municipioNombre = "-";
        int tipoDocumentoId;
        System.out.println("docus------"+docus);
        for(ActoDocumento aDoc: docus) {
            Documento doc = aDoc.getDocumento();
            String numerox=doc.getNumero2();
            Date fecha = doc.getFecha();
            Boolean bis = doc.isBis();
            Notario notario = doc.getNotario();
            texto1 += "POR ESCRITURA PUBLICA NÚMERO "+ (numerox!=null? numerox: "-") +
            (bis!=null && bis==true? " BIS": "") +
            ", DE FECHA "+ (fecha!=null? sdf.format(fecha): "-");
            if( notario != null ) {
                municipio = notario.getMunicipio();
                if( municipio != null ) {
                    municipioNombre = municipio.getNombre().trim();
                    estadoNombre = municipio.getEstado().getNombre().trim();
                }
                noNotario = notario.getNoNotario();
                texto2 = ", OTORGADO ANTE LA FE DEL LIC. " + notario.getNombreCompleto() +
                    ", NOTARIO PUBLICO "+ (noNotario!=null? noNotario: "-") +
                    ", DE " + municipioNombre + ", " + estadoNombre;
            } else {
                 texto2 = " - NO HAY INFORMACION DE NOTARIO - ";
            }       
         }
               

        return fundatorio += texto1 + texto2 + ".\r\n\r\n";
    }
    
    private String buildDocFunService(Set<ActoPredioCampo> actoPredioCampos,String tipo){
        String documento="";String notario="";String nEscritura="";String fecha=""; String docf="NO HAY DOCUMENTO FUNDATORIO";
        String nExpediente="";
        System.out.println("******CONSTRUYENDO DOC FUND****"+tipo);
        switch(tipo){
            case "676":
                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                    int id_modulo_campo =  moduloCampo.getId().intValue();
                    if (id_modulo_campo == 5004 && actoPredioCampo.getValor()!=null ) {// 
                        CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));
                        documento=(""+cv.getNombre() + " ");
                    }
                    if (id_modulo_campo == 1001 && actoPredioCampo.getValor()!=null ) {// 
                    nEscritura=("NUMERO DE ESCRITURA: "+actoPredioCampo.getValor());
                    }
                    if (id_modulo_campo == 5001 && actoPredioCampo.getValor()!=null ) {// 
                        Notario n =notarioService.findOne(Long.parseLong(actoPredioCampo.getValor().toString()));
                    notario=("NOTARIO: "+n.getNombreCompleto() + " ");
                    }
                    if (id_modulo_campo == 5002 && actoPredioCampo.getValor()!=null ) {// fecha
                        try{
                            SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd");
                            Date d=s.parse(actoPredioCampo.getValor());
                            fecha=",  DE FECHA "+buildAsiento(d)+ " ";
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    //  ant.append("FECHA "+new SimpleDateFormat("dd/MM/yyyy").format(new Date(actoPredioCampo.getValor().toString())));
                    }
                }
                docf= documento+"\n\n"+notario+"\n"+nEscritura+fecha;
            break;
            case "679":
            case "680":
            case "685":
            case "677":
            for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                int id_modulo_campo =  moduloCampo.getId().intValue();
                if (id_modulo_campo == 5004 && actoPredioCampo.getValor()!=null ) {// TIPO DOC
                    CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));
                    documento=(""+cv.getNombre() + " ");
                }
                if (id_modulo_campo == 1001 && actoPredioCampo.getValor()!=null ) {// 
                    nEscritura=("NUMERO DE OFICIO: "+actoPredioCampo.getValor() + ", ");
                }
                if (id_modulo_campo == 20137 && actoPredioCampo.getValor()!=null ) {// 
                    nExpediente=(" NUMERO DE EXPEDIENTE: "+actoPredioCampo.getValor() + ", ");
                }
                if (id_modulo_campo == 20154 && actoPredioCampo.getValor()!=null ) {// AUTORIDAD                   
                notario=("AUTORIDAD: "+actoPredioCampo.getValor() + " ");
                }                
                if (id_modulo_campo == 5002 && actoPredioCampo.getValor()!=null ) {// fecha
                    try{
                        SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd");
                        Date d=s.parse(actoPredioCampo.getValor());
                        fecha=",  DE FECHA "+buildAsiento(d)+ " ";
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                //  ant.append("FECHA "+new SimpleDateFormat("dd/MM/yyyy").format(new Date(actoPredioCampo.getValor().toString())));
                }
            }
                                 //(AUTORIDAD)                           
            docf= documento+"\n\n"+notario+"\n"+nEscritura+nExpediente+fecha;
            break;
            default:
                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                    int id_modulo_campo =  moduloCampo.getId().intValue();
                    if (id_modulo_campo == 5004 && actoPredioCampo.getValor()!=null ) {// TIPO
                        CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));
                        documento=(""+cv.getNombre() + " ");
                    }
                    if (id_modulo_campo == 1001 && actoPredioCampo.getValor()!=null ) {// NUMERO
                         nEscritura=("NUMERO DE ESCRITURA: "+actoPredioCampo.getValor());
                    }
                    if (id_modulo_campo == 5002 && actoPredioCampo.getValor()!=null ) {// fecha
                        try{
                            SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd");
                            Date d=s.parse(actoPredioCampo.getValor());
                            fecha=",  DE FECHA "+buildAsiento(d)+ " ";
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    //  ant.append("FECHA "+new SimpleDateFormat("dd/MM/yyyy").format(new Date(actoPredioCampo.getValor().toString())));
                    }
                }
                docf= documento+"\n\n"+nEscritura+fecha;
            break;
        }
        return docf;
    }
    
    private String buildDocFunService(ArrayList<ActoPredioCampo> actoPredioCampos,String tipo){
        String documento="";String notario="";String nEscritura="";String fecha=""; String docf="NO HAY DOCUMENTO FUNDATORIO";
        String nExpediente="";
        System.out.println("******CONSTRUYENDO DOC FUND****"+tipo);
        switch(tipo){
            case "676":
                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                    int id_modulo_campo =  moduloCampo.getId().intValue();
                    if (id_modulo_campo == 5004 && actoPredioCampo.getValor()!=null ) {// 
                        CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));
                        documento=(""+cv.getNombre() + " ");
                    }
                    if (id_modulo_campo == 1001 && actoPredioCampo.getValor()!=null ) {// 
                    nEscritura=("NUMERO DE ESCRITURA: "+actoPredioCampo.getValor());
                    }
                    if (id_modulo_campo == 5001 && actoPredioCampo.getValor()!=null ) {// 
                        Notario n =notarioService.findOne(Long.parseLong(actoPredioCampo.getValor().toString()));
                    notario=("NOTARIO: "+n.getNombreCompleto() + " ");
                    }
                    if (id_modulo_campo == 5002 && actoPredioCampo.getValor()!=null ) {// fecha
                        try{
                            SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd");
                            Date d=s.parse(actoPredioCampo.getValor());
                            fecha=",  DE FECHA "+buildAsiento(d)+ " ";
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    //  ant.append("FECHA "+new SimpleDateFormat("dd/MM/yyyy").format(new Date(actoPredioCampo.getValor().toString())));
                    }
                }
                docf= documento+"\n\n"+notario+"\n"+nEscritura+fecha;
            break;
            case "679":
            case "680":
            case "685":
            case "677":
            for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                int id_modulo_campo =  moduloCampo.getId().intValue();
                if (id_modulo_campo == 5004 && actoPredioCampo.getValor()!=null ) {// TIPO DOC
                    CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));
                    documento=(""+cv.getNombre() + " ");
                }
                if (id_modulo_campo == 1001 && actoPredioCampo.getValor()!=null ) {// 
                    nEscritura=("NUMERO DE OFICIO: "+actoPredioCampo.getValor() + ", ");
                }
                if (id_modulo_campo == 20137 && actoPredioCampo.getValor()!=null ) {// 
                    nExpediente=(" NUMERO DE EXPEDIENTE: "+actoPredioCampo.getValor() + ", ");
                }
                if (id_modulo_campo == 20154 && actoPredioCampo.getValor()!=null ) {// AUTORIDAD                   
                notario=("AUTORIDAD: "+actoPredioCampo.getValor() + " ");
                }                
                if (id_modulo_campo == 5002 && actoPredioCampo.getValor()!=null ) {// fecha
                    try{
                        SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd");
                        Date d=s.parse(actoPredioCampo.getValor());
                        fecha=",  DE FECHA "+buildAsiento(d)+ " ";
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                //  ant.append("FECHA "+new SimpleDateFormat("dd/MM/yyyy").format(new Date(actoPredioCampo.getValor().toString())));
                }
            }
                                 //(AUTORIDAD)                           
            docf= documento+"\n\n"+notario+"\n"+nEscritura+nExpediente+fecha;
            break;
            default:
                for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                    ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                    int id_modulo_campo =  moduloCampo.getId().intValue();
                    if (id_modulo_campo == 5004 && actoPredioCampo.getValor()!=null ) {// TIPO
                        CampoValores cv=campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));
                        documento=(""+cv.getNombre() + " ");
                    }
                    if (id_modulo_campo == 1001 && actoPredioCampo.getValor()!=null ) {// NUMERO
                         nEscritura=("NUMERO DE ESCRITURA: "+actoPredioCampo.getValor());
                    }
                    if (id_modulo_campo == 5002 && actoPredioCampo.getValor()!=null ) {// fecha
                        try{
                            SimpleDateFormat s= new SimpleDateFormat("yyyy-MM-dd");
                            Date d=s.parse(actoPredioCampo.getValor());
                            fecha=",  DE FECHA "+buildAsiento(d)+ " ";
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    //  ant.append("FECHA "+new SimpleDateFormat("dd/MM/yyyy").format(new Date(actoPredioCampo.getValor().toString())));
                    }
                }
                docf= documento+"\n\n"+nEscritura+fecha;
            break;
        }
        return docf;
    }
    
	public String obtenerValor(ActoPredioCampo apc) {
		if (apc == null || apc.getValor() == null) {
			return null;
		}

		try {
			int tipo = apc.getModuloCampo().getCampo().getTipoCampo().getId().intValue();
			switch (tipo) {
            case 5:
               return  this.formatoFecha(apc.getValor());      
			case 8:
			case 11:
			case 23:
			case 78:
				return "true".equals(apc.getValor()) ? "SI" : "NO";

			case 9:
				return estadoService.findOne(Long.parseLong(apc.getValor())).getNombre();

			case 10:
				return municipioService.findOne(Long.parseLong(apc.getValor())).getNombre();

			case 15:
			case 24:
			case 26:
			case 46:
				return listaCampoValor(apc.getModuloCampo().getCampo().getId(), Long.parseLong(apc.getValor()));

			case 16:
			case 18:
			case 19:
			case 29:
			case 30:
			case 31:
			case 32:
				return construirPersona(personaService.findOneById(Long.parseLong(apc.getValor())));

			case 20:
			case 84:
			case 88:
			case 92:
			case 96:
			case 100:
			case 104:
				return construirDesignanteAdquiriente(apc.getValor());

			case 25:
				return tipoPersonaService.findOne(Long.parseLong(apc.getValor())).getNombre();

			case 28:
				return parentescoService.findOne(Long.parseLong(apc.getValor())).getNombre();

			case 37:
				return construirPersona(notarioService.findOne(Long.parseLong(apc.getValor())).getPersona());

			case 41:
				return nacionalidadService.findOne(Long.parseLong(apc.getValor())).getNombre();

			case 45:
				return campoActo(Long.parseLong(apc.getValor()));

			case 47:
			case 55:
				return predioFusion(Long.parseLong(apc.getValor()));

			case 53:
				return orientacionService.findAll().stream().filter(o -> o.getId().equals(Long.parseLong(apc.getValor()))).findFirst().get().getNombre();

			case 54:
				return tipoColinService.findOne(Long.parseLong(apc.getValor())).getNombre();

			case 59:
			case 60:
			case 80:
				return valorRFCCURP(apc);
			default:
				return apc.getValor();
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	private String valorRFCCURP(ActoPredioCampo apc) {
		if (apc.getValor() == null || apc.getValor().contains("__")) {
			return null;
		}

		return apc.getValor();
	}

	private String listaCampoValor(long campoId, long id) {
		return campoValorService.findByTipoCampoId(campoId)
				.stream()
				.filter(cv -> cv.getId().equals(id))
				.findFirst().get()
				.getNombre();
	}

	private String predioFusion(long id) {
		Predio p = predioService.findOne(id);
		if (p == null) {
			return "No se encuentra el predio";
		}

		return "Predio: " + p.getNoFolio();
	}

	private String campoActo(long id) {
		Acto a = tramiteService.findOne(id);
//		StringBuilder v = new StringBuilder("Prelacion: ");
//		v.append(a.getPrelacion().getConsecutivo());
		StringBuilder v = new StringBuilder(" ");
		//v.append(" Acto: ");
		v.append(a.getTipoActo().getNombre());
		v.append(" - Fecha ");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		v.append(simpleDateFormat.format(prelacionService.buscaFechaInscripcion(a.getId())));
//		v.append(" - Orden: ");
//		v.append(a.getOrden());
		return v.toString();
	}

	private String construirDesignanteAdquiriente(String valor) {
		switch (valor) {
		case "20":
			return "ADQUIRIENTE";
		case "84":
			return "ALBACEA";
		case "88":
			return "GARANTE";
		case "92":
			return "DEUDOR";
		case "96":
			return "FIDUCIARIO";
		case "100":
			return "FIDEICOMISARIO";
		case "104":
			return "PERMUTANTE";
		}

		return null;
	}

	private String construirPersona(Persona p) {
		String nombre = p.getNombre();
		nombre += p.getPaterno() != null ? " " + p.getPaterno() : "";
		nombre += p.getMaterno() != null ? " " + p.getMaterno() : "";
		return nombre;
    }
    
    public String modoDeImpresion(ActoPredioCampo apc) {
        String cadena="";

        Integer ind_impresion=apc.getModuloCampo().getInd_impresion();
        
        String val=obtenerValor(apc);
        String etiquetaCampo = apc.getModuloCampo().getEtiqueta();

        if(ind_impresion==null){
            cadena="  "+etiquetaCampo+": "+val;
            return cadena;            
        }
        if(ind_impresion==0){
            cadena=" ";
            return cadena;
        }
        if(ind_impresion==1){
            cadena="  "+etiquetaCampo+": "+val;
            return cadena;
        }
        if(ind_impresion==2){
            cadena="  "+val;  
            return cadena;
        }
        return cadena;
    }

    public byte[] generPdfBoletaRegistralPorActo(Long idPrelacion,boolean pageable,Integer pagina,boolean marcaAgua) throws Exception{
   
    	System.out.println("metodo: generPdfBoletaRegistralPorActo");
                
        String nombreBoleta = "pdfBoletaRegistralSI";
     
    	//PrelacionBoletaRegistralVO prelacion = prelacionService.findOneVOSI(idPrelacion);

        List<PrelacionBoletaRegistralVO> boletas = prelacionService.getBoletasRegistral(idPrelacion,pageable,pagina);
        byte[] boletasPorActo=null;
        int cont=0;
        
        if(boletas!=null) {
       	 System.out.println("boletas "+boletas.size());
       }
      
        for (PrelacionBoletaRegistralVO boletaRegistralVO : boletas) {
			
        	
        	if(boletaRegistralVO.getActos()!=null) {
				
			
                for (ActoModel actoModel : boletaRegistralVO.getActos()) {
				Acto acto = actoModel.getActo();
					if(acto!=null){
					int tipoActo = acto.getTipoActo().getId().intValue();
					log.debug("tipo7472 "+tipoActo);
					switch (tipoActo) {
						
					case 41: //FUSION
					 	nombreBoleta="pdfBoletaRegistralSIActo41";
					break;
					 case 39: //CONDOMINIO					 
					 case 40: //FRACCIONAMIENTO
					 case 42: //SUBDIVISION
					 case 245: //LOTIFICACION
					 case 246: //RELOTIFICACION
						nombreBoleta="pdfBoletaRegistralSIActo42";
					break; 
					case 54://DECLARACION DE HEREDEROS
					case 57://NOMBRAMIENTO DE ALBACEA
					case 242://LECTURA DE TESTAMENTO
						nombreBoleta = "pdfBoletaRegistralSIActo54";//(SOLO CAMBIA TITULO)
					break;
					case 105:
					case 106:
					case 107://poderes
						nombreBoleta = "pdfBoletaRegistralSIActo105";//(SOLO CAMBIA TITULO)
                    break;
                    case 1://repudio de herencia
                        nombreBoleta = "pdfBoletaRegistralSIActo1";
                    break;
                    case 2://PROTOCOLIZACIÓN DE AUTORIZACIÓN DE VENTA
                        nombreBoleta = "pdfBoletaRegistralSIActo2";
                    break;
                    case 3://RECTIFICACIÓN
                        nombreBoleta = "pdfBoletaRegistralSIActo3";
                    break;
                    case 5://PROTOCOLIZACIÓN DE PRORROGA DE VIGENCIA PARA ACTO MODIFICATORIO
                        nombreBoleta = "pdfBoletaRegistralSIActo5";
                    break;
                    case 6://ACTO PUBLICITARIO GENERAL
                	case 132:
                	case 133:
                        nombreBoleta = "pdfBoletaRegistralSIActo6";
                    break;
                    case 7://REGISTRO, CANCELACIÓN, FIRMA PATENTE O HABILITACION Y SELLO DE FEDATARIOS PUBLICOS
                        nombreBoleta = "pdfBoletaRegistralSIActo7";
                    break;
      
					case 244://RESOLUCION ADMINISTRATIVA PARA AUTORIZACION DE FRACCIONAMIENTO 
						nombreBoleta = "pdfBoletaRegistralSIActo244";
						break;
				
					case 134:
						nombreBoleta = "pdfBoletaRegistralSIActo134";//DEPOSITO DE TESTAMENTO
						break;
					case 66:
						nombreBoleta = "pdfBoletaRegistralSIActo66";  //DEMANDAS Y/O NOTIFICACIONES DE JUICIOS DE AMPARO
						break;
					case 76:
						nombreBoleta = "pdfBoletaRegistralSIActo76";  //INFORMES DE DISPOSICIÓN TESTAMENTARIA
						break;
					case 56:
						nombreBoleta = "pdfBoletaRegistralSIActo56";  //CESION DE DERECHOS HEREDITARIOS
						break;
					case 70:
					case 249:
						nombreBoleta = "pdfBoletaRegistralSIActo70";  //RECURSO DE INCONFORMIDAD
						break;
					case 4:
						nombreBoleta = "pdfBoletaRegistralSIActo4"; //CANCELACION ACTO PUBLICITARIO
						break;
					default:
						nombreBoleta = "pdfBoletaRegistralSI";//DEFAULT
					break;
					}
				}
					
                }
            }
            
            System.out.println("nombreBoleta "+nombreBoleta);
            String uri= parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/prelacion/boletaregistral/{tipoId}/{prelacionId}";

            List<PrelacionBoletaRegistralVO> dts=new ArrayList<>();
            dts.add(boletaRegistralVO);
            JRDataSource ds = new JRBeanCollectionDataSource(dts);

            Map<String, Object> parameterMap = new HashMap<String, Object>();

            String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+idPrelacion;
            parameterMap.put("datasource", ds);
            parameterMap.put("path", Constantes.IMG_PATH);
            parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
            if(marcaAgua)
            	parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
            parameterMap.put("uri", uri);
            parameterMap.put("QR_BASE_URI", qR);
            parameterMap.put("imgPath", Constantes.IMG_PATH);
            InputStream jasperStream = this.getClass().getResourceAsStream("/reports/"+nombreBoleta+".jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
            if(cont==0){
                boletasPorActo=JasperExportManager.exportReportToPdf(jasperPrint);
            }
            if(cont>0){
                byte[] aux =JasperExportManager.exportReportToPdf(jasperPrint);
                boletasPorActo = pdfService.appendPDF(boletasPorActo,aux);
            }
            cont+=1;
		}

		return boletasPorActo;
    }

    public byte[] generPdfBoletaRegistralPorActoJSON(PrelacionBoletaRegistralVO prelacion,Integer pagina) {
    	 byte[] boleta = null;
    	try {
    		List<PrelacionBoletaRegistralVO> prelaciones =  new ArrayList<>();
        	prelaciones.add(prelacion);
        	boleta = this.generPdfBoletaRegistralPorActoJSON(prelaciones, pagina,false);
		} catch (Exception e) {
			
		}
    	return boleta;
    }


    public byte[] generPdfBoletaRegistralPorActoJSON(List<PrelacionBoletaRegistralVO> prelacions,Integer pagina,boolean marcaAgua) throws Exception{
   
        System.out.println("metodo: generPdfBoletaRegistralJSON");
                
        String nombreBoleta = "pdfBoletaRegistralSI";
        
        Long idPrelacion=0L;
     
    	//PrelacionBoletaRegistralVO prelacion = prelacionService.findOneVOSI(idPrelacion);

        List<PrelacionBoletaRegistralVO> boletas = prelacions;
        byte[] boletasPorActo=null;
        int cont=0;
        if(boletas!=null) {
        	 System.out.println("boletas "+boletas.size());
        }
       
        for (PrelacionBoletaRegistralVO boletaRegistralVO : boletas) {
			if(boletaRegistralVO.getActos()!=null) {
			for (ActoModel actoModel : boletaRegistralVO.getActos()) {
				Acto acto = actoModel.getActo();
					if(acto!=null){
					int tipoActo = acto.getTipoActo().getId().intValue();
					log.debug("tipo7472 "+tipoActo);
					switch (tipoActo) {
                    case 1://REPUDIO DE HERENCIA
                        nombreBoleta = "pdfBoletaRegistralSIActo1";
                    break;
                    case 2://PROTOCOLIZACIÓN DE AUTORIZACIÓN DE VENTA
                        nombreBoleta = "pdfBoletaRegistralSIActo2";
                    break;
                    case 3://RECTIFICACIÓN
                        nombreBoleta = "pdfBoletaRegistralSIActo3";
                    break;
                    case 5://PROTOCOLIZACIÓN DE PRORROGA DE VIGENCIA PARA ACTO MODIFICATORIO
                        nombreBoleta = "pdfBoletaRegistralSIActo5";
                    break;
                	case 132:
                	case 133:	
                    case 6://ACTO PUBLICITARIO GENERAL
                        nombreBoleta = "pdfBoletaRegistralSIActo6";
                    break;
                    case 7://REGISTRO, CANCELACIÓN, FIRMA PATENTE O HABILITACION Y SELLO DE FEDATARIOS PUBLICOS
                        nombreBoleta = "pdfBoletaRegistralSIActo7";
                    break;
					case 41: //FUSION
					 	nombreBoleta="pdfBoletaRegistralSIActo41";
					break;
					 case 39: //CONDOMINIO					 
					 case 40: //FRACCIONAMIENTO
					 case 42: //SUBDIVISION
					 case 245: //LOTIFICACION
					 case 246: //RELOTIFICACION
						nombreBoleta="pdfBoletaRegistralSIActo42";
					break; 
					case 54://DECLARACION DE HEREDEROS
					case 57://NOMBRAMIENTO DE ALBACEA
					case 242://LECTURA DE TESTAMENTO
						nombreBoleta = "pdfBoletaRegistralSIActo54";//(SOLO CAMBIA TITULO)
					break;
					case 105:
					case 106:
					case 107://poderes
						nombreBoleta = "pdfBoletaRegistralSIActo105";//(SOLO CAMBIA TITULO)
					break;
					case 244://RESOLUCION ADMINISTRATIVA PARA AUTORIZACION DE FRACCIONAMIENTO 
						nombreBoleta = "pdfBoletaRegistralSIActo244";
						break;
				
					case 134:
						nombreBoleta = "pdfBoletaRegistralSIActo134";//DEPOSITO DE TESTAMENTO
						break;
					case 66:
						nombreBoleta = "pdfBoletaRegistralSIActo66";  //DEMANDAS Y/O NOTIFICACIONES DE JUICIOS DE AMPARO
						break;
					case 76:
						nombreBoleta = "pdfBoletaRegistralSIActo76";  //INFORMES DE DISPOSICIÓN TESTAMENTARIA
						break;
					case 56:
						nombreBoleta = "pdfBoletaRegistralSIActo56";  //CESION DE DERECHOS HEREDITARIOS
						break;
					case 70://RECURSO DE INCONFORMIDAD
					case 249:
						nombreBoleta = "pdfBoletaRegistralSIActo70";
						break;
					case 4:
						nombreBoleta = "pdfBoletaRegistralSIActo4"; //CANCELACION ACTO PUBLICITARIO
						break;
					default:
						nombreBoleta = "pdfBoletaRegistralSI";//DEFAULT
					break;
					}
				}
					idPrelacion=acto.getPrelacion().getId();
            }
			}
            
            
            String uri= parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/prelacion/boletaregistral/4/"+idPrelacion+"/"+pagina;

            List<PrelacionBoletaRegistralVO> dts=new ArrayList<>();
            dts.add(boletaRegistralVO);
            JRDataSource ds = new JRBeanCollectionDataSource(dts);

            Map<String, Object> parameterMap = new HashMap<String, Object>();

            String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+idPrelacion;
            parameterMap.put("datasource", ds);
            parameterMap.put("path", Constantes.IMG_PATH);
            parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
           
            if(marcaAgua)
             parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
           
            parameterMap.put("uri", uri);
            parameterMap.put("QR_BASE_URI", qR);
            InputStream jasperStream = this.getClass().getResourceAsStream("/reports/"+nombreBoleta+".jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
            if(cont==0){
                boletasPorActo=JasperExportManager.exportReportToPdf(jasperPrint);
            }
            if(cont>0){
                byte[] aux =JasperExportManager.exportReportToPdf(jasperPrint);
                boletasPorActo = pdfService.appendPDF(boletasPorActo,aux);
            }
            cont+=1;
		}

		return boletasPorActo;
    }



    public String buildStringFirmaActos(Acto acto) {

        if (acto == null)
            return "- SIN ESPECIFICAR -";

        StringBuilder sb = new StringBuilder();
        boolean tieneFirma = false;

            List<ActoFirma> firmas = actoService.getActosFirma((Long)acto.getId());

            if (firmas != null )
            {
                for (ActoFirma firma : firmas) {
                    if( firma.getActo().getVf() != null && firma.getActo().getVf() ) {
                        continue;
                    }
                    if( firma.getFirma() != null ) {
                        sb.append( firma.getFirma() )
                          .append( "\r\n" );
                        tieneFirma = true;
                    }
                    if( tieneFirma ) {
                        break;
                    }
                }
            }

        if (sb.toString().length() == 0 )
        	sb.append( "- SIN FIRMAS - " );

        return sb.toString();
    }
    
    public byte[] generaPdfBoletaRechazos(Long prelacionId,boolean marcaAgua) {
    	byte[] boletas = null;
    	List<Acto> actosRechazados =  this.actoRepository.findActosByPrelacionIdAndRechazados(prelacionId);
    	if(actosRechazados!=null && actosRechazados.size()>0) {
    		for(Acto x :  actosRechazados) {
    			try {
					boletas  = boletas == null ? this.getBoletaRechazo(x.getId(),marcaAgua)  : 
								pdfService.appendPDF(boletas,this.getBoletaRechazo(x.getId(),marcaAgua));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
    	return boletas;
    }

    public String quitarUltimoCaracter(String cadena){
        if(cadena!=null && !cadena.isEmpty()){
            cadena=cadena.substring(0, cadena.length()-1);
            return cadena;
        }else{
            return cadena;
        }
    }

    public String formatoFecha(String fecha){
        SimpleDateFormat modificado = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd");
        String reformattedStr = "";
        try {
            reformattedStr = modificado.format(original.parse(fecha));
            return reformattedStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

}
