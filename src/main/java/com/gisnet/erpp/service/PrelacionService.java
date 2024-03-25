package com.gisnet.erpp.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;

import javax.mail.MessagingException;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.autoconfigure.RemoteDevToolsProperties.Debug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.dao.CaratulaDAO;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.excepciones.PrelacionException;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.HtmlToPDF;
import com.gisnet.erpp.util.SHACheckSum;
import com.gisnet.erpp.util.SignerUtilBouncyCastle;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.util.Constantes.ETipoFolio;
import com.gisnet.erpp.vo.ArchivoFirmaVO;
import com.gisnet.erpp.vo.BandejaSuspensionVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetalleAtendioVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetalleFoliosVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetallePagoVO;
import com.gisnet.erpp.vo.ConsultaPrelacionDetalleTramiteVO;
import com.gisnet.erpp.vo.ConsultaPrelacionVO;
import com.gisnet.erpp.vo.FolioVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.ActoVO;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.PrelacionBitacoraImpresionVO;
import com.gisnet.erpp.vo.PrelacionCopiaCertificadaFolioVO;
import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.vo.PrelacionVO;
import com.gisnet.erpp.vo.PublicVO;
import com.gisnet.erpp.vo.RequisitoVO;
import com.gisnet.erpp.vo.ResultPrelacionVO;
import com.gisnet.erpp.vo.ServicioAndSubVO;
import com.gisnet.erpp.vo.actopublicitario.*;
import com.gisnet.erpp.vo.juridico.ActaDeRetiroVO;
import com.gisnet.erpp.vo.juridico.ActaDeRevocacionVO;
import com.gisnet.erpp.vo.juridico.DepositoTestamentoVO;
import com.gisnet.erpp.vo.prelacion.ActoPublictarioModel;
import com.gisnet.erpp.vo.prelacion.ModificarPrelacionVO;
import com.gisnet.erpp.vo.prelacion.PrelacionBoletaRegistralVO;
import com.gisnet.erpp.vo.prelacion.PrelacionModuloVO;
import com.gisnet.erpp.vo.PrelacionActosVO;
import com.gisnet.erpp.vo.PrelacionDocumentosVO;
import com.gisnet.erpp.vo.BitacoraDocumentoEntradaVO;
import com.gisnet.erpp.vo.boletin.*;
import com.itextpdf.tool.xml.html.Break;
import com.lowagie.text.DocumentException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import com.gisnet.erpp.vo.ReciboVO;
import com.gisnet.erpp.vo.ReingresoVO;

import com.gisnet.erpp.vo.BuscarFolioVO;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;

//CV
import java.util.concurrent.Executors;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.codec.Base64.OutputStream;
import java.io.OutputStream;

@Service
public class PrelacionService{
	@Autowired
    MailSenderService mailSenderService;
   
	@Autowired
	ActoPublicitarioRepository actoPublicitarioRepository;

    @Autowired
    ParametroRepository parametroRepository;

	@Autowired
	PrelacionRepository prelacionRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	StatusRepository statusRepository;
	
	@Autowired
	StatusExternoRepository statusExternoRepository;
	
	@Autowired
	StatusVisRepository statusVisRepository;

	@Autowired
	StatusActoRepository statusActoRepository;


	@Autowired
	PrioridadRepository prioridadRepository;

	@Autowired
	SuspensionRepository suspencionRepository;

	@Autowired
	DiasSolventacionSuspensionRepository diasSolventacionSuspensionRepository;

	@Autowired
	AreaRepository areaRepository;

	@Autowired
	OficinaRepository oficinaRepository;

	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;

	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;


	@Autowired
	ActoRequisitoRepository actoRequisitoRepository;

	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	BitacoraRepository bitacoraRepository;

	@Autowired
	BitacoraReingresoRepository bitacoraReingresoRepository;
	
	@Autowired
	ServicioRepository servicioRepository;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	NacionalidadService nacionalidadService;
	
	@Autowired
	OficinaService oficinaService;


	@Autowired
	ActoService actoService;

	@Autowired
	TipoActoService tipoActoService;

	@Autowired
	ArchivoRepository archivoRepository;

	@Autowired
	DocumentoRepository documentoRepository;

	@Autowired
	ActoDocumentoService actoDocumentoService;

	@Autowired
	ActoPredioService actoPredioService;

	@Autowired
	ActoRequisitoService actoRequisitoService;

	@Autowired
	PrelacionUsuarioDefRepository prelacionUsuarioDefRepository;
	
	@Autowired
	TipoEntradaRepository tipoEntradaRepository;

	@Autowired
	SuspensionService suspensionService;
	
	@Autowired
	SuspensionRepository suspensionRepository;

	@Autowired
	SuspensionFirmaRepository suspensionFirmaRepository;

	@Autowired
	ReciboService reciboService;

	@Autowired
	BoletaRegistralService boletaRegistralService;

	@Autowired
	AntecedenteService antecedenteService;
	@Autowired
	private TurnadorService turnadorService;


	@Autowired
	private MaterializacionActoService materializacionActoService;
	

	@Autowired
	private PersonaJuridicaRepository personaJuridicaRepository;

	@Autowired
	private FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;

	@Autowired
	private MuebleRepository muebleRepository;

	@Autowired
	private CampoValorService campoValorService;

	@Autowired
	private PredioRepository predioRepository;

	@Autowired
	private TipoFolioRepository tipoFolioRepository;

	@Autowired
	private PrelacionOficioRepository prelacionOficioRepository;


	@Autowired
	private DireccionAreaRepository direccionAreaRepository;
	
	@Autowired
	private PrelacionAnteRepository prelacionAnteRepository;
	
	@Autowired
	private PredioService predioService;
	
	@Autowired
	private MuebleService muebleService;
	
	@Autowired
	private PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	private FolioSeccionAuxiliarService folioSeccionAuxiliarService;
	
	@Autowired
	private EstructuraOrgService estructuraOrgService;

	@Autowired
	private BusquedaService busquedaService;

	@Autowired
	private MotivoRepository motivoRepository;
	
	@Autowired
	private NotificacionRepository notificacionRepository;

	@Autowired
	private TipoAclaracionService tipoAclaracionService;

	@Autowired
	private NotarioRepository notarioRepository;

	@Autowired
	private ReciboRepository reciboRepository;
		

	@Autowired
	private InhabilLocalRepository inhabilLocalRepository;
	
	@Autowired
	private PredioAnteRepository predioAnteRepository;
	
	@Autowired
	private TipoMotivoRepository tipoMotivoRepository;

	@Autowired
	private DocumentoArchivoRepository documentoArchivoRepository;

	@Autowired
	TipoRechazoRepository tipoRechazoRepository;
	
	@Autowired
	private BitacoraImpresionRepository bitacoraImpresionRepository;
	
	@Autowired
	private PrelacionServicioRepository prelacionServicioRepository;

	@Autowired
	private CertificadoRepository certificadoRepository;

	@Autowired
	private ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired//CV
	private PrelacionBoletaService prelacionBoletaService;

	@Autowired//CV
	private BusquedaResultadoService busquedaResultadoService;
	
	@Autowired
	private ResultadoRepository resultadoRepository;

	@Autowired
	PrelacionService prelacionService;
	
	@Autowired
	PrelacionAnteService prelacionAnteService;
	
	@Autowired
	PdfService pdfService;
	
	@Autowired
	ActoPublicitarioService actoPublicitarioService;
	
	@Autowired
	BitacoraDigitalizadorService bitacoraDigitalizadorService;
	
	@Autowired
	TipoActoRepository tipoActoRepository;
	
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	private PrelacionBoletaRepository prelacionBoletaRepository;
	
	@Autowired
	private BitacoraPrelacionRepository bitacoraPrelacionRepository;
	
	@Autowired
	private CaratulaDAO caratulaDAO;
	
	@Autowired
	private RecursoInconformidadRepository recursoInconformidadRepository;
	
	@Autowired
	BitacoraEntregaService bitacoraEntregaService;
	
	@Autowired
	BitacoraService bitacoraService;
	
	@Autowired
	SeccionRepository seccionRepository;
	
	@Autowired
	BitacoraDocumentoEntradaService bitacoraDocumentoEntradaService;
	
	@Autowired
	private BusquedaResultadoRepository busquedaResultadoRepository;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private SimpleDateFormat formatYMDS = new SimpleDateFormat( "yyyy-MM-dd" );
	@Autowired
	private PredioExtintoRepository predioExtintoRepository;

	public Prelacion findOne(Long id) {
		return prelacionRepository.findOne(id);
	}

	@Transactional
	public Long create(PrelacionVO prelacionIngreso){
		Prelacion prelacion = new Prelacion();
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Status status = statusRepository.findOne(Constantes.Status.INGRESO_POR_VENTANILLA.getIdStatus());
		Prioridad prioridad = prioridadRepository.findOne(Constantes.Prioridad.NORMAL.getIdPrioridad());



		Oficina ofi= oficinaRepository.findOne(1l);

		if (prelacionIngreso.getSolicitante() != null)
			prelacion.setUsuarioSolicitan(prelacionIngreso.getSolicitante());
		else
			prelacion.setUsuarioSolicitan(usuario);

		prelacion.setUsuarioVentanilla(usuario);
		prelacion.setStatus(status);
		prelacion.setOficina(ofi);
		prelacion.setPrioridad(prioridad);

		prelacionRepository.save(prelacion);


		return prelacion.getId();

	}

	public List<Prelacion> findAll(){
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Status status = new Status();
		status.setId(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus());
		return prelacionRepository.findAllByStatusAndUsuarioAnalistaAndConsecutivoIsNotNullOrderByConsecutivoAsc(status,usuario);
	}

	@Transactional(readOnly = true)
	public Page<Prelacion> findAllByStatusAndUsuario(Long folio, String fechaIngreso,
			String fechaEnvioFirma, String fechaBaja,
			Long usSolicitanteId, Pageable pageable,
			Integer estado, Integer tipoUsuarioSeleccionado
			) {

		Date fechaI=null, fechaEF=null, fechaB= null;

		try {
			if (fechaIngreso != null && fechaIngreso != "")
				fechaI = DateTime.parse(fechaIngreso).toDate();
			if (fechaEnvioFirma != null && fechaEnvioFirma != "")
				fechaEF = DateTime.parse(fechaEnvioFirma).toDate();
			if (fechaBaja != null && fechaBaja != "")
				fechaB = DateTime.parse(fechaBaja).toDate();
		}
		catch (Exception except) {
			return null;
		}

		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		System.out.println(" ==== USUARIO: ");
		
		System.out.println(usuario.getId());
		
		if (tipoUsuarioSeleccionado != 3)
			usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		Usuario solicitante = usSolicitanteId == null ? null: usuarioService.findOne(usSolicitanteId);

		List<Constantes.Status> maxStatus = new ArrayList<Constantes.Status>();

		maxStatus = this.getMaxStatusPrelacionSusp(tipoUsuarioSeleccionado);

		return prelacionRepository.findAllByStatusAndUsuario (usuario,tipoUsuarioSeleccionado, estado, maxStatus, folio, fechaI, fechaEF, fechaB, solicitante, pageable);
	}

	@Transactional(readOnly = true)
	public List<Prelacion> findAllByStatusAndUsuarioCoordinador(
			Long tipo,
			Long folio,
			String fechaIngreso,
			String fechaEnvioFirma,
			Long usSolicitanteId,
			Long notarioId,
			Long calificadorId,
			Pageable pageable,
			Integer estado,
			Integer tipoUsuarioSeleccionado
	) {
 
		Date fechaI=null, fechaEF=null, fechaB= null;
		Long oficinaId =0L;
		try {
			if (fechaIngreso != null && fechaIngreso != "")
				fechaI = DateTime.parse(fechaIngreso).toDate();
			if (fechaEnvioFirma != null && fechaEnvioFirma != "")
				fechaEF = DateTime.parse(fechaEnvioFirma).toDate();
		}
		catch (Exception except) {
			return null;
		}

		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		System.out.println("Usuario Id -------------> " + usuario);
		List<Usuario> subordinados = estructuraOrgService.getSubordinadosByUsuario(usuario);

		Usuario solicitante = usSolicitanteId == null ? null: usuarioService.findOne(usSolicitanteId);
		Usuario notario = notarioId == null ? null: usuarioService.findOne(notarioId);
		Usuario calificador= calificadorId == null ? null: usuarioService.findOne(calificadorId);
		
		tipoUsuarioSeleccionado = 3;
		List<Constantes.Status> status= this.getMaxStatusPrelacionSusp(tipoUsuarioSeleccionado);
		oficinaId = usuario.getOficina().getId();
		log.debug(status.toString() + " " + estado.toString());
		
		return prelacionRepository.findAllByStatusAndUsuarioCoordinador (subordinados,status, tipo, folio, fechaI, fechaEF, solicitante, notario, calificador, estado, oficinaId, pageable);
	}

	private Constantes.Status getMaxStatusPrelacion (int tipoUsuario) {
		switch (tipoUsuario ) {
			case 1:
				return Constantes.Status.ASIGNADO_A_ANALISTA;
			case 2:
				return Constantes.Status.ASIGNADO_A_CALIFICADOR;
			case 3:
				return Constantes.Status.ASIGNADO_A_COORDINADOR;
		}
		return null;

	}
	//JADV-SUSPENSION
	private List<Constantes.Status> getMaxStatusPrelacionSusp (int tipoUsuario) {
		
		List<Constantes.Status> status = new ArrayList<Constantes.Status>();
		
		switch (tipoUsuario ) {
		case 1:
			status.add(Constantes.Status.ASIGNADO_A_ANALISTA);
			return status;
		case 2:
			status.add(Constantes.Status.ASIGNADO_A_CALIFICADOR);
			status.add(Constantes.Status.SUSPENDIDA_ANALISTA);
			return status;
		case 3:
			status.add(Constantes.Status.ASIGNADO_A_COORDINADOR);
			status.add(Constantes.Status.SUSPENDIDA_CALIFICADOR);
			status.add(Constantes.Status.PENDIENTE_DEVOLUCION_DOCUMENTO);
			return status;
		}
		return null;

	}
	
	public List<Prelacion> findAllByUsuario(int tipoUsuario){

		log.debug("Datos de envio  : "+ tipoUsuario);
		List<Prelacion> prelacione=null;
		Oficina oficina=null;
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		//IHM Validar el tipo de usuario para obtener la oficina
		//long ofi=usuario.getOficina().getId();
		if(usuario.getTipoUsuario().getId()!=2){
			oficina=(Oficina) usuario.getOficina();
		}
		//TipoPrelacion tipopre = tipoPrelacionRepository.findOne(idTipoPrelacion);
		Status status = new Status();
		List<Status> listStatus = new ArrayList<Status>();
		switch (tipoUsuario) { 
		case 1:
			status.setId(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus());
			listStatus.add(status);
			status = new Status();
			//status.setId(Constantes.Status.ESTATUS_SUSPENSIVO.getIdStatus());
			//listStatus.add(status);

			List<Prelacion> prelacionesConObservaciones = prelacionRepository.findAllByBandejaByUsuarioAndStatusAndObservacion(listStatus, usuario, oficina);

			if (!CollectionUtils.isEmpty(prelacionesConObservaciones)) {
				prelacione = prelacionesConObservaciones;
			}
			else {
				prelacione = prelacionRepository.findAllByBandejaByUsuarioAndStatus(listStatus,usuario,oficina);
			}
			break;

		case 2:
			//JADV-SUSPENSION
			status.setId(Constantes.Status.ASIGNADO_A_CALIFICADOR.getIdStatus());
			listStatus.add(status);
			status = new Status();
			status.setId(Constantes.Status.SUSPENDIDA_ANALISTA.getIdStatus());
			listStatus.add(status);
			status = new Status();
			//status.setId(Constantes.Status.ESTATUS_SUSPENSIVO.getIdStatus());
			//listStatus.add(status);
			prelacione=prelacionRepository.findAllByBandejaByUsuarioAndStatusCalificador(listStatus,usuario,oficina);
			break;

		case 3:
			log.debug("Coordinador: " + usuario.getId().toString());
			status.setId(Constantes.Status.ASIGNADO_A_COORDINADOR.getIdStatus());
			listStatus.add(status);
			status = new Status();
			status.setId(Constantes.Status.SUSPENDIDA_CALIFICADOR.getIdStatus());
			listStatus.add(status);
			status = new Status();			
			prelacione= prelacionRepository.ffindAllByBandejaByStatusAndOficina(listStatus, usuario.getOficina());
			break;
		case 4:
			status.setId(Constantes.Status.ASIGNADOR_A_VALIDADOR_DE_PREDIOS.getIdStatus());
			//prelacione=prelacionRepository.findAllByStatusAndUsuarioCapValAndTipoPrelacionAndConsecutivoIsNotNullOrderByConsecutivoAsc(status,usuario, tipopre);
			prelacione =  prelacionRepository.findAllByBandejaByUsuarioCYVFAndStatus(status,usuario);
			break;
		case 5: // BANDEJA DE DIGITALIZACION
			prelacione=prelacionRepository.findAllByBandejaDigitalizaByOficina(oficina);
			break;
		case 6: // BANDEJA DE ENTREGA DIGITALIZACION
			if(oficina.getReq_digitalizar()){
				prelacione = prelacionRepository.findAllByBandejaByOficinaIdAndStatusIdAndDigitalizadoAndIndEntrega(oficina.getId(),4L,false,false);
			}
			
		break;
		case 21://Bandeja de Notario
			status.setId(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus());
			listStatus.add(status);
			status = new Status();
			status.setId(Constantes.Status.ASIGNADO_A_CALIFICADOR.getIdStatus());
			listStatus.add(status);
			status = new Status();			
			status.setId(Constantes.Status.ASIGNADO_A_COORDINADOR.getIdStatus());
			listStatus.add(status);
			status = new Status();
			status.setId(Constantes.Status.SUSPENDIDA_ANALISTA.getIdStatus());
			listStatus.add(status);
			status = new Status();
			status.setId(Constantes.Status.SUSPENDIDA_CALIFICADOR.getIdStatus());
			listStatus.add(status);
			status = new Status();
			status.setId(Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus());
			listStatus.add(status);
			status = new Status();
			status.setId(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus());
			listStatus.add(status);
			status = new Status();
			prelacione=prelacionRepository.findAllByStatusAndUsuarioSolicitanAndConsecutivoIsNotNullOrderByConsecutivoAsc(listStatus,usuario);
			System.out.println("Prelaciones -------> " + prelacione.size());
			break;
		case 22:// Bandeja de Notario no Asignado

			List<Documento> docs = documentoRepository.findAllByNotarioId((long) 12500);
			List<Long> idDocs = new ArrayList<Long>();
			List<Long> Idactos = new ArrayList<Long>();
			List<Long> idPrelaciones = new ArrayList<Long>();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date fecha = new Date();

			for (Documento doc : docs) {
				idDocs.add(doc.getId());
			}
			List<ActoDocumento> actoDoc = actoDocumentoRepository.getAllByDocumentoId(idDocs);
			for (ActoDocumento actoDo : actoDoc) {
				Idactos.add(actoDo.getActo().getId());
			}

			List<Acto> actos = actoRepository.findAll(Idactos);

			for (Acto prela : actos) {
				idPrelaciones.add(prela.getPrelacion().getId());
			}
			try {

				fecha = format.parse("2018-01-15");

			} catch (ParseException e) {
				e.printStackTrace();
			}

			prelacione = prelacionRepository.findAllById(idPrelaciones, fecha);

			break;
		

		}


		return prelacione;
	}
	//IHM 26-03-2019
	/*
	public List<Prelacion> findAllByUsuario(int tipoUsuario){

		log.debug("Datos de envio  : "+ tipoUsuario);
		List<Prelacion> prelacione=null;
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Status status = new Status();

		switch (tipoUsuario) { 
			case 1:
				status.setId(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus());
				prelacione=prelacionRepository.findAllByBandejaByUsuarioAndStatus(status,usuario);
			break;

			case 2:
				status.setId(Constantes.Status.ASIGNADO_A_CALIFICADOR.getIdStatus());
				prelacione=prelacionRepository.findAllByBandejaByUsuarioCalAndStatus(status,usuario);
			break;

			case 3:
				log.debug("Coordinador: " + usuario.getId().toString());
				status.setId(Constantes.Status.ASIGNADO_A_COORDINADOR.getIdStatus());
				prelacione= prelacionRepository.ffindAllByBandejaByStatus(status);
			break;
			
			case 4:
				status.setId(Constantes.Status.ASIGNADOR_A_VALIDADOR_DE_PREDIOS.getIdStatus());
				prelacione=prelacionRepository.findAllByStatusAndUsuarioCapValAndConsecutivoIsNotNullOrderByConsecutivoAsc(status,usuario);
			break;case 21://Bandeja de Notario
				status.setId(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus());
				prelacione=prelacionRepository.findAllByStatusAndUsuarioSolicitanAndConsecutivoIsNotNullOrderByConsecutivoAsc(status,usuario);
			break;
			case 22:// Bandeja de Notario no Asignado

				List<Documento> docs = documentoRepository.findAllByNotarioId((long) 12500);
				List<Long> idDocs = new ArrayList<Long>();
				List<Long> Idactos = new ArrayList<Long>();
				List<Long> idPrelaciones = new ArrayList<Long>();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date fecha = new Date();

				for (Documento doc : docs) {
					idDocs.add(doc.getId());
				}
				List<ActoDocumento> actoDoc = actoDocumentoRepository.getAllByDocumentoId(idDocs);
				for (ActoDocumento actoDo : actoDoc) {
					Idactos.add(actoDo.getActo().getId());
				}

				List<Acto> actos = actoRepository.findAll(Idactos);

				for (Acto prela : actos) {
					idPrelaciones.add(prela.getPrelacion().getId());
				}
				try {

					fecha = format.parse("2018-01-15");

				} catch (ParseException e) {
					e.printStackTrace();
				}

				prelacione = prelacionRepository.findAllById(idPrelaciones, fecha);

				break;


		}


		return prelacione;
	}
	*/

	public List<Prelacion> findNextPrelacionAsignadaAnalista(String userName){
		List<Long> status = new ArrayList<>(2);
		status.add(Constantes.Status.ASIGNADOR_A_VALIDADOR_DE_PREDIOS.getIdStatus());
		status.add(Constantes.Status.DEVUELTO_A_ANALISTA_CYVF.getIdStatus());
		/*Prelacion prelacion = null;
		List<Prelacion> prelaciones = prelacionRepository.findAllByAnalista(userName,status);
		if(prelaciones != null && !prelaciones.isEmpty()){
			prelacion = prelaciones.stream().findFirst().get();
		}*/
		return prelacionRepository.findAllByAnalista(userName,status);
	}

	@Transactional
	public String finalizaPrelacionCyvf(Long idPrelacion) throws Exception{
		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);
		List<Long> foliosCreados = new ArrayList<Long>();
		
		if(prelacion != null){
			prelacion.setNumCapturas(prelacion.getNumCapturas() + 1 );
			prelacion.setUsuarioCapVal(null);
			Set<PrelacionAnte> prelacionAntes = prelacion.getPrelacionAntesParaPrelacions();
			System.out.println("prelacionAntes size: "+prelacionAntes.size());
			for (Iterator iterator = prelacionAntes.iterator(); iterator.hasNext();) {
				PrelacionAnte prelacionAnte = (PrelacionAnte) iterator.next();
				
				Constantes.ETipoFolio tipoFolio = Constantes.ETipoFolio.fromLong(prelacionAnte.getTipoFolio().getId());
				
				PrelacionPredio prelacionPredio = new PrelacionPredio();
				
				boolean create=true;
				
				//TODO: Cambiar como se generan los folios porque no deben ser de sequences
				
				switch (tipoFolio) {
					case PERSONAS_JURIDICAS:
					System.out.println("prelacionAnte.getPersonaJuridica(): "+prelacionAnte.getPersonaJuridica());
						if (prelacionAnte.getPersonaJuridica()!= null && prelacionAnte.getPersonaJuridica().getNoFolio()==null){
							foliosCreados.add(personaJuridicaService.createFolioPj(prelacionAnte.getPersonaJuridica().getId(), prelacionAnte.getOficina().getId()));
							prelacionPredio.setPersonaJuridica(prelacionAnte.getPersonaJuridica());
						} else {							
							create=false;
						}						
						break;
					case PERSONAS_AUXILIARES :
						if (prelacionAnte.getFolioSeccionAuxiliar().getNoFolio()==null){
							foliosCreados.add(folioSeccionAuxiliarService.createFolioSeccionAux(prelacionAnte.getFolioSeccionAuxiliar().getId(), prelacionAnte.getOficina().getId()));
							prelacionPredio.setFolioSeccionAuxiliar(prelacionAnte.getFolioSeccionAuxiliar());
						} else {
							create=false;							
						}
						break;
					case MUEBLE:
						if (prelacionAnte.getMueble().getNoFolio()==null){
							foliosCreados.add(muebleService.createFolioMueble(prelacionAnte.getMueble().getId(), prelacionAnte.getOficina().getId()));
							prelacionPredio.setMueble(prelacionAnte.getMueble());
						} else {
							create=false;							
						}
						break;
					case PREDIO :
						if (prelacionAnte.getPredio() != null  && prelacionAnte.getPredio().getNoFolio()==null){
							foliosCreados.add(predioService.createFolioPredio(prelacionAnte.getPredio().getId(), prelacionAnte.getOficina().getId(),true));
							prelacionPredio.setPredio(prelacionAnte.getPredio());														
						} else {
							create=false;												
						}					
						break;
				}
				
				
				
				// tiene folio y por lo tanto ya fue previamente relacionado a la prelacion
				if (create){
					prelacionPredio.setPrelacion(prelacion);
					prelacionPredio.setTipoFolio(prelacionAnte.getTipoFolio());
					prelacionPredio.setVersion(1);
					prelacionPredio.setEstatus(1);
					
					prelacionPredioRepository.saveAndFlush(prelacionPredio);
					
					prelacionPredio.setIdVersion(prelacionPredio.getId().toString()+prelacionPredio.getPrelacion().getId().toString());
					prelacionPredioRepository.saveAndFlush(prelacionPredio);

				}
				
			}
			
			if(prelacion.getVf()!=null && prelacion.getVf())
				prelacion.setStatus(statusRepository.findOne(Constantes.Status.SOPORTE_OPERACION_MIGRACION.getIdStatus()));
			prelacionRepository.save(prelacion);
			
			//enviarATurnador(prelacion);

		}
		
		if (foliosCreados.size()>0) {
			StringBuilder result = new StringBuilder();
			result.append("Se creo el folio ");
			  for(Long folio : foliosCreados){
			    result.append(folio);
			    result.append(", ");
			  }
			  //AVM 11-08-2020 NO APLICA PARA ENTRADA NEGATIVAS DE CRACIÓN DE FOLIOS
			  if(prelacion.getVf()==null ||  !prelacion.getVf())
					  result.append(" al pasar a  analsita");
			return result.toString() ;
		} else {
			return "No se creo ningún Folio";
		}		
	}


	public Prelacion updatePrelacionEstado(Long idPrelacion, Constantes.Status estado,String observaciones, TipoAclaracion tipoAclaracion, Boolean reproceso) {

		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);		
		Status status = new Status();	
		Suspension suspension=null;
		
		if(prelacion.getStatus().getId()==16){ //SUSPENDIDO POR CALIFICADOR
			suspensionService.quitarSuspension(prelacion);
		}
		
		if(reproceso == null) {
			reproceso = false;
		}
		
		if(prelacion.getStatus().getId().equals(Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus())
			&& estado == Constantes.Status.ASIGNADO_A_COORDINADOR) {
			estado = Constantes.Status.SUSPENDIDA_CALIFICADOR;
		}
		
		status.setId(estado.getIdStatus());		
		prelacion.setStatus(status);
	
		prelacionRepository.save(prelacion);
		switch (estado) {
			case ASIGNADO_A_ANALISTA:
					if(observaciones!=null) {
						prelacion.setObservacionesMotivo(tipoAclaracion.getNombre()+" - "+observaciones);
						prelacionRepository.save(prelacion);
						if(tipoAclaracion!=null)
							bitacoraRepository.save(createBitacoraCompleta(prelacion,null/*Motivo*/,null/*TipoRezhaco*/,null/*TipoMotivo*/,tipoAclaracion,observaciones/*Aclaracion*/));
					}else{
						bitacoraRepository.save(createBitacora(prelacion));
					}

				break;
			case ASIGNADO_A_CALIFICADOR:
					if(observaciones!=null) {
						prelacion.setMotivoReasignacion(tipoAclaracion.getNombre()+" - "+observaciones);	
						prelacionRepository.save(prelacion); 
						if(tipoAclaracion!=null)
							bitacoraRepository.save(createBitacoraCompleta(prelacion,null,null,null,tipoAclaracion,observaciones));
					}else{
						bitacoraRepository.save(createBitacora(prelacion));
					}
					prelacion.setUsuarioCalificador(prelacion.getUsuarioAnalista());
					prelacionRepository.save(prelacion);

				break;
			case ASIGNADO_A_COORDINADOR:
				if(reproceso) {
					bitacoraRepository.save(createBitacoraReproceso(prelacion, observaciones));
				}else {
					bitacoraRepository.save(createBitacora(prelacion));
				}
					prelacion.setFechaEnvioFirma(new Date());		
				break;
			case LISTO_PARA_ENTREGARSE:
			case ENTREGADO_AL_USUARIO:
			case SOPORTE_A_OPERACION:
			case SUSPENDIDA_CALIFICADOR:
				if(reproceso) {
					bitacoraRepository.save(createBitacoraReproceso(prelacion, observaciones));
				}else {
					bitacoraRepository.save(createBitacora(prelacion));
				}
				
				break;
			case PENDIENTE_DEVOLUCION_DOCUMENTO:
				createBitacora(prelacion, observaciones,null);
				break;
			case DIGITALIZA_DEVOLUCION_DOCUMENTO:
				prelacion.setEs_digitalizado(false);
				createBitacora(prelacion, observaciones,null);
				
				prelacionRepository.save(prelacion);
				break;
		
		}
		//IHM Se quita el envio de Correo por cambio de estado
		//boletaRegistralService.enviaNotificacion(prelacion);

		return prelacion;
	}


	/**
	 *
	 * @param prealacion
	 */
	public Prelacion saveAndUpdate(Prelacion prelacion) {
		prelacionRepository.saveAndFlush(prelacion);
		return prelacion;
	}

	/**
	 *
	 * @param prelacion
	 */
	public Integer delete(Long idPrelacion) {
		// TODO - implement PrelacionService.delete
		throw new UnsupportedOperationException();
	}


	public List<Prelacion> findAllWithServiciosByAnalistaAndStatus(Long usuarioId, Long statusId){
		return prelacionRepository.findAllWithServiciosByAnalistaAndStatus(usuarioId,statusId);
	}



	@Transactional
	public Prelacion finalizaPrelacionVentanilla(
			Long idPrelacion,
			Observacion observacion,
			String observaciones,
			Boolean tipoEntrega,
			Usuario solicitante,
			Boolean tieneTermino,
			Integer dias,
			Integer horas,
			Boolean esExentoPago,
			Boolean primerRegistro,
			Area area,
			Boolean urgente,
			Prelacion preEnvio
	) throws PrelacionException {

		String nombreSecuenciaConsecutivo= null;
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		Prelacion prelacion=prelacionRepository.findOne(idPrelacion);
		
		System.out.println("prelacion Recuperada "+prelacion);

		try {

			Status status = null;
			if(prelacion != null){
				prelacion.setArea(area);	
				status = new Status();
				status.setId(Constantes.Status.RECIBIDO_POR_VENTANILLA.getIdStatus());

				prelacion.setObservacion(observacion);
				if(observaciones!=null)
				  prelacion.setObservaciones(CommonUtil.encodeValue(observaciones)); 

				prelacion.setTipoEntrega(tipoEntrega == true ? 2 : 1);

				prelacion.setTieneTermino(tieneTermino);
				prelacion.setDias(dias);
				prelacion.setHoras(horas);

				prelacion.setEmailEnviado(false);

				prelacion.setUsuarioSolicitan( (solicitante == null) ? usuario : solicitante);
				prelacion.setExentoPago(esExentoPago);

				Oficina ofi = this.getOficinaPrelacion(prelacion,usuario);
				if (ofi != null ) {
					prelacion.setOficina(ofi);
				}else {
					// Validar cuando trae oficina - USUARIO RPP
					if(usuario.getOficina()!=null) {
					ofi = usuario.getOficina();
					prelacion.setOficina(ofi);
					}else {
						if(prelacion.getOficina()!=null) {
							ofi = prelacion.getOficina();
						}
					}
				}

				if(prelacion.getOficina().getReq_digitalizar()==true){
					prelacion.setEs_digitalizado(false);
					prelacion.setIndEntrega(true);
				}

				//PARA INGRESOS EXTERNOS (USUARIOS NO RPP) NO DEBE PASAR A DIGITALIZACION
				if(!usuario.getTipoUsuario().getId().equals(Constantes.TipoUsuario.ERPP.getIdTipoUsuario())) {
					prelacion.setEs_digitalizado(true);
					prelacion.setIndEntrega(true);
				}

				Notario notario = notarioRepository.findByPersona(usuario.getPersona());
				if( notario != null ) {
					prelacion.setNotario( notario );
					prelacion.setUsuarioNotario( usuario );
				}

				prelacion.setStatus(status);
				prelacion.setFechaIngreso( new Date() );
				prelacion.setFechaProgramadaEntrega( new Date() );

				prelacion.setSubindice(0L);
								
				nombreSecuenciaConsecutivo = "PRELACION_CONSECUTIVO";
				Long idOficina = 0L;
				if(preEnvio!=null){
					nombreSecuenciaConsecutivo= nombreSecuenciaConsecutivo+"_"+preEnvio.getOficina().getNumOficina();
					idOficina =  preEnvio.getOficina().getId();
				}
				else {
					if(ofi.getNumOficina()!=null || !ofi.getNumOficina().isEmpty()){
						nombreSecuenciaConsecutivo= nombreSecuenciaConsecutivo+"_"+ofi.getNumOficina();
						idOficina = ofi.getId();
					}
				}
				nombreSecuenciaConsecutivo= nombreSecuenciaConsecutivo+"_SEQUENCE";				
				Integer consecutivo = prelacionRepository.getConsecutivo(nombreSecuenciaConsecutivo);
			
				prelacion.setPrimerRegistro( primerRegistro );
				try{
					Integer anio=Integer.parseInt( new SimpleDateFormat("YYYY").format(prelacion.getFechaIngreso()));
					prelacion.setAnio(anio);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				Boolean existe = prelacionRepository.existPrelacion(consecutivo, prelacion.getAnio(), idOficina);
				if(!existe) {
					prelacion.setConsecutivo(consecutivo);
				}
				else {
					prelacionRepository.updateSecuencia(nombreSecuenciaConsecutivo, prelacion.getAnio(),idOficina);
					consecutivo = prelacionRepository.getConsecutivo(nombreSecuenciaConsecutivo);
					prelacion.setConsecutivo(consecutivo);
				}
					
				
				String claveConsulta = this.getCodigo();
				prelacion.setClaveConsulta(claveConsulta);

				if(urgente == true){
					Prioridad prioridad = prioridadRepository.findOne(Constantes.Prioridad.ALTA.getIdPrioridad());
					prelacion.setPrioridad(prioridad);
				}
				if (tieneTermino == null? false : tieneTermino ) {
					final Prioridad prioridad = prioridadRepository.findOne(Constantes.Prioridad.CRITICA.getIdPrioridad());
					prelacion.setPrioridad(prioridad);
				}

				prelacionRepository.save(prelacion);
			}else{

				throw new PrelacionException("No se pudo recuperar la prelacion o la prelacion no existe.", prelacion);

			}

			if(preEnvio==null){
			//Se manda al turnador la prelacion
				preEnvio= new Prelacion();
			preEnvio.setId(prelacion.getId());
			preEnvio.setOficina(new Oficina());
			preEnvio.getOficina().setId(prelacion.getOficina().getId());
			}

			log.debug("Datos de envio "+preEnvio.getId()+"  -  oficina : "+preEnvio.getOficina().getId());



			if(prelacion.getTipoEntrega()==2) {

				boletaRegistralService.enviaBoleta(prelacion, 1 );

			}

			if (esClasificacionActoAclaracion (prelacion)) {

				TipoAclaracion tipoAclaracion = tipoAclaracionService.findOne( getIdTipoAclaracion(prelacion) );
				String observacionesAclaracion =  getObservacionDeAclaracion(prelacion);
			    guardarBitacoraObservacionAclaracion (prelacion,tipoAclaracion ,  observacionesAclaracion);
            }


			bitacoraRepository.save(createBitacora(prelacion));

			if (esBusquedaSimple(prelacion)) {
				this.asignarStatus (prelacion, Constantes.Status.ENTREGADO_AL_USUARIO);
			}	

			if (necesitaTurnador (prelacion))
			System.out.println("EnviarATurnador "+preEnvio.getOficina()+"/n/n");
				this.enviarATurnador(preEnvio); 

			return prelacion;

		}
		catch (Exception err) {
			err.printStackTrace();
			System.out.println("Error al finalizar la prelacion "+err+"/n/n");
			throw new PrelacionException(err.getMessage(), prelacion);
		}


	}

    private void guardarBitacoraObservacionAclaracion(Prelacion prelacion, TipoAclaracion tipoAclaracion, String observacionAclaracion) {
        // buscar la observacion String - Id de tipo Aclaracion -> Generar la bitacora

        bitacoraRepository.save( createBitacoraCompleta(prelacion, null, null, null, tipoAclaracion, observacionAclaracion) );
    }

    private boolean esClasificacionActoAclaracion(Prelacion prelacion) {
        Set<Acto> actos = prelacion.getActosParaPrelacions();
       if (! isEmpty( actos ) ) {
           Set<Acto> aclaraciones =  actos.stream().filter(item -> item.getTipoActo().getClasifActo().getNombre().contains("ACLARACIONES"))
                   .collect(Collectors.toSet());
           if (aclaraciones.size() > 0)
               return true;

       }
       return false;

    }

    private Long getIdTipoAclaracion(Prelacion prelacion) {
        return Long.valueOf( getDatosCampo(prelacion , Constantes.ID_TIPO_ACLARACION) );
    }

	private String getObservacionDeAclaracion(Prelacion prelacion) {
		return getDatosCampo(prelacion , Constantes.OBSERVACION_ACLARACION);
	}

	private String getDatosCampo(Prelacion prelacion, long campoId) {
		Set<Acto> actos= prelacion.getActosParaPrelacions();

		if(actos!=null) {
			for(Acto a :actos) {

				for(ActoPredio ap : a.getActoPrediosParaActos()) {
					Set<ActoPredioCampo> actoPredioCampos = ap.getActoPredioCamposParaActoPredios();
					for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
						long tempCampoId = actoPredioCampo.getModuloCampo().getCampo().getId();
						if (tempCampoId  == campoId)
							return actoPredioCampo.getValor();
					}
				}
			}
		}

		return null;

	}

    private boolean necesitaTurnador(Prelacion prelacion) {
		if (esBusquedaSimple(prelacion))
			return false;
		// TODO: Agregar mas opciones de servicios / prelación que no necesiten turnado

		return true;  // Ninguna de las condiciones anteriores asi que prelación necesita de Turnador
	}

	private boolean esBusquedaSimple(Prelacion prelacion) {
		Set<PrelacionServicio> servicios = prelacion.getPrelacionServiciosParaPrelacions();
		if(servicios!=null && servicios.size()>0 ){
			PrelacionServicio ps=servicios.iterator().next();
			if(ps.getServicio().getId()==Constantes.Servicio.BUSQUEDA_SIMPLE.getIdServicio()){
				return true;
			}
		}
		return false;
	}

	private void asignarStatus(Prelacion prelacion, Constantes.Status cStatus) {
		Status status = new Status();
		status.setId(cStatus.getIdStatus());
		prelacion.setStatus(status);
		prelacionRepository.save(prelacion);
		bitacoraRepository.save(createBitacora(prelacion));

	}


	private Oficina getOficinaPrelacion(Prelacion prelacion,Usuario user) {

		try {
			/*List<PrelacionAnte> preAnte = prelacionAnteRepository.findByPrelacionId(prelacion.getId());

			if (preAnte != null) {
				PrelacionAnte pre = preAnte.get(0);
				return pre.getOficina();
			}
			*/

			Oficina oficina = null;
			List<PrelacionPredio> prelacionPredio = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacion.getId());
			if (prelacionPredio != null && prelacionPredio.size() > 0 ) {

				PrelacionPredio pre = prelacionPredio.get(0);
				Constantes.ETipoFolio tipo = this.getTipoFolio(prelacion);

				switch (tipo) {
					case PERSONAS_JURIDICAS:
						oficina = pre.getPersonaJuridica().getOficina();
						break;
					case PERSONAS_AUXILIARES:
						oficina = pre.getFolioSeccionAuxiliar().getOficina();
						break;
					case MUEBLE:
						oficina = pre.getMueble().getOficina();
						break;
					case PREDIO:
						oficina = pre.getPredio().getOficina();

				}

				log.info("=== Oficina === " + oficina.getId() + " - " + oficina.getNombre());


			}else{
				
				List<PrelacionAnte> pAntecedentes = prelacionAnteRepository.findByPrelacionId(prelacion.getId());
				if(pAntecedentes!=null && pAntecedentes.size() > 0){
					for(PrelacionAnte a:pAntecedentes){
						oficina=a.getOficina();
					}
				}				
			}

			//SI LA PRELACION NO TIENE FOLIO NI ANTECEDENTE, SE TOMA LA OFICINA DEL USUARIO SI ES RPP
			if(oficina  == null && user.getTipoUsuario().getId().equals(Constantes.TipoUsuario.ERPP.getIdTipoUsuario()))
				 oficina =  user.getOficina();

			return oficina;
		}
		catch (Exception except ) {
			except.printStackTrace();
			log.error("No se pudo obtener oficina de la prelación");
		}

		return null;

	}



	public PrelacionPredio deletePredioPrelacion(long prelacionId, long predioId) {
		PrelacionPredio pred = null;

		log.info("Buscando para eliminar " + prelacionId);
		if (predioId == -1) {
			this.deleteAllPrelacionPredioByPrelacionId (prelacionId);
			return null;

		}

		List<PrelacionPredio> preTemp= prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionId);
		if (!preTemp.isEmpty()) {
			pred = preTemp.get(0);

			switch (pred.getTipoFolio().getId().intValue()) {
				case 1:
					pred = prelacionPredioRepository.findByPrelacionIdAndPersonaJuridicaId(prelacionId, predioId);
					break;
				case 2:
					pred = prelacionPredioRepository.findByPrelacionIdAndFolioSeccionAuxiliarId(prelacionId, predioId);
					break;

				case 3:
					pred = prelacionPredioRepository.findByPrelacionIdAndMuebleId(prelacionId, predioId);
					break;

				case 4:
					pred = prelacionPredioRepository.findOneByPrelacionIdAndPredioId(prelacionId, predioId);
					break;

			}

			if (pred != null) {
				prelacionPredioRepository.delete(pred);
			}
			return pred;
		}

		return null;

	}

	private void deleteAllPrelacionPredioByPrelacionId(Long prelacionId) {
		List<PrelacionPredio> preTemp= prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionId);
		if (preTemp != null) {
			for (PrelacionPredio pre: preTemp )
			{
				prelacionPredioRepository.delete(pre);

			}
		}

	}

	public PrelacionPredio deletePredioPrelacionById(long prelacionId, long prelacionPredioId) {
		PrelacionPredio pred = null;
		//List<PrelacionPredio> prelaciones=new ArrayList<PrelacionPredio>();
		PrelacionPredio prelacionPredio = prelacionPredioRepository.findByPrelacionIdAndId(prelacionId, prelacionPredioId);
		if (prelacionPredio != null) {
			//prelaciones.add(pred);			
			//for(PrelacionPredio prelacionPredio: prelaciones) {
				prelacionPredio.setEstatus(0);
			//prelacionPredioRepository.saveAndFlush(prelacionPredio);

				int tipoFolioId = (int)(long)prelacionPredio.getTipoFolio().getId();
					Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromInt(tipoFolioId);
					List<ActoPredio> listAP =null;
					switch (tipo) {
						case PERSONAS_JURIDICAS:
						listAP	=actoPredioRepository.findAllByActoPrelacionIdAndPersonaJuridicaIdAndActoVf(prelacionId,prelacionPredioId,false);	
						if(listAP!=null){
							for(ActoPredio ap: listAP){
									ap.setPersonaJuridica(null);
									ap.setTipoFolio(null);	
									actoPredioRepository.saveAndFlush(ap);
							}
						}
					
						break;
						case PERSONAS_AUXILIARES :
						listAP=	actoPredioRepository.findAllByActoPrelacionIdAndFolioSeccionAuxiliarIdAndActoVf(prelacionId,prelacionPredioId,false);
						if(listAP!=null){
							for(ActoPredio ap: listAP){
									ap.setFolioSeccionAuxiliar(null);
									ap.setTipoFolio(null);	
									actoPredioRepository.saveAndFlush(ap);
							}
						}
						break;
						case MUEBLE:
						listAP	=actoPredioRepository.findAllByActoPrelacionIdAndMuebleIdAndActoVf(prelacionId,prelacionPredioId,false);
						if(listAP!=null){
							for(ActoPredio ap: listAP){
									ap.setMueble(null);
									ap.setTipoFolio(null);	
									actoPredioRepository.saveAndFlush(ap);
							}
						}
						break;
						case PREDIO :
						listAP	=actoPredioRepository.findAllByActoPrelacionIdAndPredioIdAndActoVf(prelacionId,prelacionPredioId,false);
						if(listAP!=null){
							for(ActoPredio ap: listAP){
									ap.setPredio(null);
									ap.setTipoFolio(null);	
									actoPredioRepository.saveAndFlush(ap);
							}
						}
						break;
					}
			prelacionPredioRepository.delete(prelacionPredio);
		}

		return pred;
	}


	public PrelacionAnte deleteAntecedentePrelacion(long prelacionId, long anteId) {
		PrelacionAnte ante = null;

		
		return ante;
	}
	/* Función que obtiene solo el acto, tipo de acto y usuario solicitante.
	 *  27/02/2024
	 *  JLCI
	*/

	public PrelacionIngresoVO findOneVOv2 (Long prelacionId) {
		PrelacionIngresoVO pvo = new PrelacionIngresoVO();
		Prelacion pre = prelacionRepository.findOne(prelacionId);//primer registro
		pvo.setNombreSolicita(
				this.getUsuarioSolicitan( pre.getUsuarioSolicitan(), prelacionId )
		);
		List<Acto> acts = actoService.findAllByPrelacionOrderByOrdenAsc(prelacionId);
		String actos = "NO DEFINIDO";
		String tiposActo = "NO IDENTIFICADO";

		PrelacionServicio prelacionServicio = new PrelacionServicio();
		prelacionServicio = prelacionService.findByPrelacionId(prelacionId);
		if (acts != null && acts.size() > 0) {
			actos = "";
			int index = 1;
			for (String acto :acts.stream().map(acto -> acto.getTipoActo().getNombre()).collect(Collectors.toList()) ){
				actos += "1."+String.valueOf(index++) + " " + acto + "\r\n";

			}
			//actos.replaceFirst("\r\n$","");
			StringUtils.removeEnd(actos, "\r\n");
			tiposActo = "";
			index = 1;
			for (String tipo: acts.stream().map(acto -> acto.getTipoActo().getClasifActo().getNombre()).collect(Collectors.toList()) ) {
				tiposActo += String.valueOf(index++) + ". " +tipo + "\r\n";
			}
			StringUtils.removeEnd(tiposActo, "\r\n");
		}
		if(prelacionServicio!=null) {
			if(prelacionServicio.getServicio().getId()==46) {
				actos="1.1 "+prelacionServicio.getServicio().getNombre();
			}
		}
		pvo.setActos(actos);
		pvo.setTiposActo(tiposActo);
		return pvo;
	}

	public PrelacionIngresoVO findOneVO (Long prelacionId) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Prelacion pre = prelacionRepository.findOne(prelacionId);//primer registro

		List<PrelacionAnte> pre_ante = prelacionAnteRepository.findByPrelacionId(prelacionId);//antecedente

		PrelacionServicio ps =  findByPrelacionId(pre.getId() ); // Serivicio		
		HashSet <BusquedaResultado> brHash = busquedaResultadoRepository.findAllByPrelacionId (prelacionId); // Detalle de Servico

		PrelacionIngresoVO pvo = new PrelacionIngresoVO();
		
		pvo.setPrioridad(pre.getPrioridad().getNombre());
		
		  //prelacion.consecutivo - prelacion.año (del ingreso) - prelacion.num_capturas

		if(pre.getSubindice()==null){
			pvo.setConsecutivo(""+pre.getConsecutivo()+"-"+pre.getAnio()+"-"+0); 
		}else{
			pvo.setConsecutivo(""+pre.getConsecutivo()+"-"+pre.getAnio()+"-"+pre.getSubindice()); 
		}

		pvo.setFechaIngreso(pre.getFechaIngreso());
		pvo.setFechaReingreso(pre.getFechaReingreso());
		pvo.setOficina(pre.getOficina().getNombre());
		if(pre.getClaveConsulta()!=null){
			pvo.setClave(pre.getClaveConsulta());
		}else{
			pvo.setClave(null);
		}
			

		ArrayList<PrelacionPredio> predios  = (ArrayList) prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionId);
		List<PrelacionPredio> pred=new ArrayList<PrelacionPredio>();
		ArrayList<FolioVO> folioVOs=new ArrayList<>();

		System.out.println("predios.size "+predios.size());
		if(pre.getPrimerRegistro()!=null) {
			if(pre.getPrimerRegistro()==true){
				FolioVO folio= new FolioVO();
				folio.setFolio("ANTECEDENTE: ");
				folio.setUbicacion("PRIMER REGISTRO");
				folioVOs.add(folio);
			}
		}
		if(ps!=null) {
			for(BusquedaResultado br:brHash) {
				FolioVO folio= new FolioVO();
				// DE ANTECENDETE
				if(br.getLibro()!=null && br.getDocumento()!=null) {
					folio.setFolio("ANTECEDENTES");
					folio.setUbicacion("   LIBRO: "+br.getLibro().getLibroBis()+"   VOLUMEN: "+br.getLibro().getVolumen()+"   TOMO: "+br.getLibro().getNumLibro()+"   AÑO: "+br.getLibro().getAnio()+" \n\n "+"SECCION: "+br.getLibro().getSeccionesPorOficina().getSeccion().getId()+"   OFICINA: "+br.getLibro().getSeccionesPorOficina().getOficina().getNombre()+"   INSCRIPCION: "+br.getDocumento());
				}
				// DE FOLIO INSCRIPCION
				if(br.getPredio()!=null) {
					folio.setFolio(""+br.getPredio().getNoFolio());
					folio.setUbicacion(br.getPredio().getDireccionComplete());
				}
				
				// DE FOLIO PERSONA MORAL CIVIL
				if(br.getPersonaJuridica()!=null) {
					folio.setFolio(" "+br.getPersonaJuridica().getNoFolio());
					folio.setUbicacion(br.getPersonaJuridica().getDenominacionNombre());
				}				
				
				folioVOs.add(folio);
			}
		}
		if (pre_ante!=null)
		{	
			for(PrelacionAnte pre_ant: pre_ante)
			{	
				FolioVO folio= new FolioVO();
				folio.setFolio("ANTECEDENTES");
				folio.setUbicacion("   LIBRO: "+pre_ant.getLibroBis()+"   VOLUMEN: "+pre_ant.getVolumen()+"   TOMO: "+pre_ant.getLibro()+"   AÑO: "+pre_ant.getAnio()+" \n\n "+"SECCION: "+pre_ant.getSeccion().getId()+"   OFICINA: "+pre_ant.getOficina().getNombre()+"   INSCRIPCION: "+pre_ant.getDocumento());
				folioVOs.add(folio);
			}
		}
			
		if(predios!=null){ 				
			System.out.println("predios: "+predios.size());				
			for (PrelacionPredio predio: predios ) {                
				FolioVO folio= new FolioVO();
				if(predio.getPredio()!=null) {
					folio.setFolio(""+predio.getPredio().getNoFolio());
					folio.setUbicacion(predio.getPredio().getDireccionComplete());
				}
				if(predio.getPersonaJuridica()!=null) {
					folio.setFolio(""+predio.getPersonaJuridica().getNoFolio());
				}
				folioVOs.add(folio);
			}				
		}
		

		pvo.setFolios(folioVOs);
		System.out.println("folioVOs.size "+folioVOs.size());

		String observaciones="";
		for(String s: getObservacionesPrelacion(pre)) {
			observaciones+="* "+s+"\n";
		}

		if(pre.getObservaciones()!=null){
			observaciones+="* "+CommonUtil.decodeValue(pre.getObservaciones());
		}
		if( !(observaciones.length()>0) ) {
			observaciones=null;
		}
		pvo.setObservaciones(observaciones);

		pvo.setNombreSolicita(
				this.getUsuarioSolicitan( pre.getUsuarioSolicitan(), prelacionId )
		);


		pvo.setEmailSolicita(this.getEmailSolicita(pre));

		pvo.setTipoRecepcion(this.getTipoRecepcion(pre) );

		switch (pre.getTipoEntrega()) {
			case 1: pvo.setTipoEntrega("FÍSICO");
				break;
			case 2: pvo.setTipoEntrega("ELECTRONICO");
				break;
			default: pvo.setTipoEntrega("NO DEFINIDO"); 
		}


		pvo.setNombreRegistrador(pre.getUsuarioVentanilla()==null ? "NO DEFINIDO": pre.getUsuarioVentanilla().getNombreCompleto());
		List<Acto> acts = actoService.findAllByPrelacionOrderByOrdenAsc(prelacionId);
		String actos = "NO DEFINIDO";
		String tiposActo = "NO IDENTIFICADO";

		PrelacionServicio prelacionServicio = new PrelacionServicio();
		prelacionServicio = prelacionService.findByPrelacionId(prelacionId);
		if (acts != null && acts.size() > 0) {
			actos = "";
			int index = 1;
			for (String acto :acts.stream().map(acto -> acto.getTipoActo().getNombre()).collect(Collectors.toList()) ){
				actos += "1."+String.valueOf(index++) + " " + acto + "\r\n";

			}
			//actos.replaceFirst("\r\n$","");
			StringUtils.removeEnd(actos, "\r\n");
			tiposActo = "";
			index = 1;
			for (String tipo: acts.stream().map(acto -> acto.getTipoActo().getClasifActo().getNombre()).collect(Collectors.toList()) ) {
				tiposActo += String.valueOf(index++) + ". " +tipo + "\r\n";
			}
			StringUtils.removeEnd(tiposActo, "\r\n");
		}

		String fundatorio= "";
		List<Acto> actos1=   actoRepository.findAllByPrelacion(pre);
		List<String> fundarios = new ArrayList<String>();
		
		if (actos1 != null && actos1.size() >0)
			for (Acto acto:actos1 ) {

				List<ActoDocumento> docus = actoDocumentoService.getAllActoDocumentoByActoId(acto.getId());
				if (docus != null && docus.size() > 0)
					for(ActoDocumento aDoc: docus) {
						//fundatorio+=getCaratulaDocumento(aDoc.getDocumento())+"\n";
						fundarios.add(getCaratulaDocumento(aDoc.getDocumento()));
					};

			}
		List<String> distinctElements = fundarios.stream().distinct().collect(Collectors.toList());
		if(distinctElements.size()>0) {
			for(String fu:distinctElements) {
				fundatorio+=fu+"\n";
			}
		}

		if(prelacionServicio!=null) {
			if(prelacionServicio.getServicio().getId()==46) {
				actos="1.1 "+prelacionServicio.getServicio().getNombre();
			}
		}
		pvo.setActos(actos);
		pvo.setTiposActo(tiposActo);
		pvo.setDatosDocumentoFundatorio(fundatorio);	

		pvo.setRecibos(reciboService.findVOByPrelacionId(prelacionId));


		return pvo;

	}


	public String getCaratulaDocumento(Documento  doc){
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String fundatorio="";
		if(doc!=null) {

			String fechaFormat =""; 
			Boolean bis=doc.isBis();
			
			

			if(doc.getFecha()!=null)
				fechaFormat=formatter.format(doc.getFecha());
	
 	System.out.println("El tipo de doc es ="+doc.getTipoDocumento().getId().intValue());
/*	System.out.println(doc.getNumero()+"\n"+doc.getNumero2()+"\n"+doc.getNumero3()+"\n"+doc.getNombre()+"\n"+doc.getPaterno()+"\n"+doc.getJuzgado()+"\n");
	System.out.println(doc.getNumero2()+"\n");		
	System.out.println(doc.getNumero3()+"\n");
	System.out.println(doc.getNombre()+"\n");
	System.out.println(doc.getNombre()+"\n");
	System.out.println(doc.getPaterno()+"\n");
	System.out.println(doc.getMaterno()+"\n");
	System.out.println(doc.getJuzgado()+"\n");
	System.out.println(doc.getCargoFirmante()+"\n");
	System.out.println(doc.getAutoridadLocal()+"\n");
	System.out.println(doc.getRequeridoPor()+"\n"); */
	//*/
	
			switch(doc.getTipoDocumento().getId().intValue()) {

					case 1 : 
							fundatorio ="POR ESCRITURA PUBLICA NÚMERO "+doc.getNumero2()+"-"+(bis!=null && bis==true? " BIS": "")+" DE FECHA "+fechaFormat+" OTORGADO ANTE LA FE DEL  "+this.getDatosNotario(doc.getNotario());
					break;
					
					case 2:
					case 20:
							fundatorio=" CONTRATO PRIVADO RATIFICADO POR EL JUEZ  " + doc.getNombre() + " " + doc.getPaterno() + " " + doc.getMaterno() + ", DEL JUZGADO " + doc.getJuzgado()  +" No "+doc.getNumero()+".";	
					break;						
					case 3:
							fundatorio =" TÍTULO NUMERO  "+doc.getNumero()+"  DE FECHA  "+fechaFormat;
					break; 
					case 4 :
							fundatorio =" RESOLUCION CON NO. "+doc.getNumero()+" DE FECHA "+fechaFormat+" EMITIDO POR "+ doc.getRequeridoPor();
					break; 
					case 5 :
							fundatorio =" OFICIO CON NO. "+doc.getNumero2()+" EMITIDO POR LA AUTORIDAD "+doc.getAutoridadLocal()+" DE FECHA "+fechaFormat;
					break; 
					case 6 :
							fundatorio ="OFICIO NO. "+doc.getNumero()+" CON EXPEDIENTE NO. "+doc.getNumero2()+"  DE FECHA   "+fechaFormat+"  FIRMADO POR "+doc.getCargoFirmante()+".";
					break;  
					case 7 :
							fundatorio =" NO EXISTE PLANTILLA DE DOCUMENTO";
					break;
					case 8:
							fundatorio =" NO EXISTE PLANTILLA DE DOCUMENTO";
					break; 
					case 9 :
							fundatorio =" NO EXISTE PLANTILLA DE DOCUMENTO";
					break; 
					case 10 :
							fundatorio ="SENTENCIA DICTADA EN EL EXPEDIENTE NUMERO  "+doc.getNumero()+"  DICTADA POR  "+this.getDatosJuez(doc.getJuez());;
					break; 
					case 11://OFICIO DE AUTORIDAD
							fundatorio =(fechaFormat!= null?"ESCRITO  FECHA   "+fechaFormat:"")+" SOLICITADO POR  "+doc.getNombre()+ "  "+(doc.getPaterno()!=null ? doc.getPaterno() : "" )+"  "+(doc.getMaterno()!=null ? doc.getMaterno() : "")+" EN CALIDAD DE PERSONA "+ doc.getObjeto() ;
					break;
					
					case 12 ://OFICIO DE AUTORIDAD 
						fundatorio ="OFICIO CON NO.  "+doc.getNumero()+ " DE FECHA  "+fechaFormat+" SOLICITADO POR  "+doc.getAutoridadLocal();
					break;
					case 13://POLIZA
							fundatorio ="PÓLIZA NÚMERO  "+doc.getNumero2()+"  DE FECHA  "+fechaFormat+" "+ doc.getObjeto()+" EMITIDO POR "+doc.getRequeridoPor()+".";
					break; 
					case 14 ://ESCRITO RATIFICADO POR NOTARIO
							fundatorio ="ESCRITO CON NO. "+doc.getNumero()+" DE FECHA   "+fechaFormat+this.getDatosNotario(doc.getNotario());
					break; 
					case 15 ://DECRETO
							fundatorio ="DECRETO NÚMERO  "+doc.getNumero()+" POR CAUSA DE UTILIDAD PUBLICA  "+doc.getCausaUtilidad()+"  REQUERIDO POR  "+doc.getRequeridoPor()+"  ORDENADO POR "+ doc.getAutoridadExhortante() + "  EXPROPIADO POR "+doc.getExpropiante()+" CON NOMBRE"
							+doc.getNombre()+" "+doc.getPaterno()+" "+ doc.getMaterno()+" EN CALIDAD DE "+doc.getEnCalidadDe()+".";
					break; 
					case 16 ://CONTRATO PRIVADO RATIFICADO POR NOTARIO
							fundatorio =" CONTRATO CON NO. "+doc.getNumero2()+ " DE FECHA "+fechaFormat+" RATIFICADO ANTE LA FE DEL  "+this.getDatosNotario(doc.getNotario());
					break;
					case 17 :
							fundatorio ="SOLICITUD DE FECHA    "+fechaFormat+" REALIZADA POR EL  "+this.getDatosNotario(doc.getNotario());
					break;  
					case 18 :
							fundatorio =" NO EXISTE PLANTILLA DE DOCUMENTO-18";
					break;
					case 21://RESOLUCION JUDICIAL
					case 22://RESOLUCION ADMINISTRATIVA
							fundatorio ="RESOLUCION CON NO. "+doc.getNumero2()+ " DE FECHA  "+fechaFormat+" SOLICITADO POR "+doc.getAutoridadLocal();
					break;  
					case 23://TITULO DE PROPIEDAD
						fundatorio =" TITULO CON NO. "+doc.getNumero2()+ " DE FECHA "+fechaFormat+ " CON AUTORIZACION DE "+doc.getAutoridadLocal()+" REPRESENTADO POR "+doc.getEnCalidadDe();
					break;
					case 24://DOCUMENTO PRIVADO
						fundatorio =" DOCUMENTO CON NO. "+doc.getNumero2()+ " DE FECHA "+fechaFormat+ " CELEBRADO EN "+doc.getObjeto();
					break;
					 
					



			}



			
			fundatorio += "\r\n";
		}
		

		return fundatorio;
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

	private String getEmailSolicita(Prelacion pre) {
		String emailSolicita = "";


		if (pre.getUsuarioSolicitan() == null)
			emailSolicita = "- NO ESPECIFICADO - ";
		else  {

			emailSolicita = pre.getUsuarioSolicitan().getEmail();
			if (emailSolicita.length()==0 || pre.getUsuarioSolicitan().getId() == 0) {
				emailSolicita = "- NO ESPECIFICADO - ";
			}
		}

		return emailSolicita;

	}

	private String getTipoRecepcion(Prelacion pre) {
		String tipoRecepcion = "";
		if (pre.getUsuarioVentanilla() == null)
			tipoRecepcion = "ELECTRONICO";

		else  {

			if (pre.getUsuarioVentanilla() == pre.getUsuarioSolicitan())
				tipoRecepcion = "ELECTRONICO";
			else
				tipoRecepcion = "PRESENCIAL";
		}

		return tipoRecepcion;
	}

	private String getDatosNotario (Notario pNotario)
	{
		String txtNotario = "";

		if (pNotario == null)
			return " - SIN DATOS DE NOTARIA - ";

		txtNotario = " NOTARIO PUBLICO " + pNotario.getNoNotario() + " DE " + pNotario.getMunicipio().getNombre() + ", " +
				pNotario.getMunicipio().getEstado().getNombre().trim() + ".\r\n";

		if (pNotario.getPersona() == null)
			return " SIN DATOS PERSONALES DE NOTARIO - " + txtNotario;

		txtNotario = " LIC. " + pNotario.getPersona().getNombre() + " " + pNotario.getPersona().getPaterno() + " " + pNotario.getPersona().getMaterno() + ", " + txtNotario;

		return txtNotario;
	}


	private String getDatosJuez (Juez pJuez)
	{
		String txtJuez = "";

		if (pJuez == null)
			return " - SIN DATOS DE JUEZ - ";



		txtJuez = " JUEZ  " + pJuez.getNombre() + " " + pJuez.getPaterno() + " " + pJuez.getMaterno() + ", DEL  " + pJuez.getJuzgado()  +" No "+pJuez.getNoJuzgado() + "  DE "+pJuez.getMunicipio().getNombre()+"  DEL  "+pJuez.getMunicipio().getEstado().getNombre() ;

		return txtJuez;
	}
	/*
	  private String getTipoCert (TipoCer pTipoCer)
	{
		String txtJuez = "";

		if (pJuez == null)
			return " - SIN DATOS DE JUEZ - ";
		txtJuez = " JUEZ  " + pJuez.getNombre() + " " + pJuez.getPaterno() + " " + pJuez.getMaterno() + ", DEL  " + pJuez.getJuzgado()  +" No "+pJuez.getNoJuzgado() + "  DE "+pJuez.getMunicipio().getNombre()+"  DEL  "+pJuez.getMunicipio().getEstado().getNombre() ;

		return txtJuez;
	}
	*/


	public PrelacionBoletaRegistralVO findOneVOSI (Long prelacionId) {
		return boletaRegistralService.findOneVOPredios(prelacionId);
	}

	/*public List<PrelacionBoletaRegistralVO> getBoletasRegistral(Long prelacionId) {
		return boletaRegistralService.findVOPredios(prelacionId,false,null);
	}*/
	
	public List<PrelacionBoletaRegistralVO> getBoletasRegistral(Long prelacionId,boolean pageable,Integer pagina) {
		return boletaRegistralService.findVOPredios(prelacionId,pageable,pagina,false,null);
	}

	public void enviarATurnador(Prelacion prelacion){
		turnadorService.asignaPrelacionAnalista(prelacion);
	}
	
	public Prelacion updatePrelacionEstadoAclaracion(Long idPrelacion, Constantes.Status estado, String observaciones , Long tipoAclaracion) {

		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);
		TipoAclaracion ta = new TipoAclaracion();
		ta.setId(tipoAclaracion);
		Status status = new Status();
		status.setId(estado.getIdStatus());
		prelacion.setStatus(status);
		prelacion.setObservacionesAclaracion(observaciones);
		prelacion.setTipoAclaracion(ta);
		prelacionRepository.save(prelacion);
		bitacoraRepository.save(createBitacoraCompleta(prelacion,null,null,null,ta,observaciones));

		return prelacion;
	}



	public String getCadenaFirmado(Long idPrelacion) {

		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);


		return  prelacion.getCadenaHash();
	}

	public String getCadenaSuspensionFirmado(Long idPrelacion) {
		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);
		Suspension suspension = suspencionRepository.findOneByestatusSuspensionAndPrelacion(2L, prelacion);
		if(suspension!=null) {
			return  suspension.getCadenaHash();
		}else {
			return  null;
		}
	}


	public String getHashPrelacion(Long idPrelacion) {

		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);
		String hash=null;
		try{
			hash= SHACheckSum.getHashSHA256(prelacion.getCadenaHash());
		}catch(Exception e){
			e.printStackTrace();
		}
		return hash;
	}

	@Transactional
	public Prelacion firmaPrelacion(ArchivoFirmaVO  firma ) {

		Prelacion prelacion =null ;
		try {
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		 prelacion = prelacionRepository.findOne(firma.getId());
		//CV
		Boolean isCopias = false;
		Boolean existActoPreventivo = false;

		if(firma!=null){
		 log.debug("ENTRO A FIRMAR PRELACION :::: "+prelacion.getId());
					//Calendar calendar = Calendar.getInstance();
				//	calendar.setTimeInMillis(firma.getEstampilla());
					prelacion.setPkcs7( firma.getPkcs7());
					prelacion.setFirma(SignerUtilBouncyCastle.getEncryptedDigest(firma.getPkcs7()));
					prelacion.setSecuencia( firma.getSecuencia());
					prelacion.setPolitica( firma.getPolitica());
					prelacion.setDigestion( firma.getDigestion()) ;
					prelacion.setSecuenciaTS( firma.getSecuenciaTS());
					prelacion.setEstampilla( firma.getEstampilla());
					prelacion.setUsuarioCoordinador(user);
					prelacion.setFechaEnvioFirma(UtilFecha.getCurrentDate());

					Status status = new Status();
					//JADV-SUSPENSION
					List<Suspension> suspension = suspencionRepository.findAllByestatusSuspensionAndPrelacion(2L, prelacion);

					status.setId(Constantes.Status.LISTO_PARA_ENTREGARSE.getIdStatus());
					prelacion.setStatus(status);
					prelacionRepository.saveAndFlush(prelacion);					
					bitacoraRepository.save(createBitacora(prelacion));

					boolean isRechazada = false;
					boolean isSuspendida =  false;

					if (suspension == null || suspension.isEmpty()) {
						
						List<ActoPredio> actos = this.findAllActoPredioByPrelacionAndVfFalse(prelacion.getId());

						materializacionActoService.updateStatusFinalPrelacion(prelacion);
						
						updateResultado(prelacion, actos);	

						for (Acto acto : prelacion.getActosParaPrelacions()) {
							if(acto.getStatusActo().getId() == 5){
								acto.setFechaRechazo(new Date());
								isRechazada = true;
							}else{
								if((acto.getVf() == null || acto.getVf() == false) && acto.getClon() == null)
									 acto.setFechaRegistro(new Date());
								//CV
								/* if(acto.getStatusActo().getId().equals(Constantes.StatusActo.PREVENTIVO.getIdStatusActo())){
									existActoPreventivo = true;
							   } */
							}
							//IHM Revisar VF
							/*if (acto.getVf() == null || !acto.getVf()) {
								List<ActoRel> actosRel = actoRelRepository.findByActoId(acto.getId());
								for (ActoRel actoRel: actosRel) {
									if (actoRel.getActoSig()!=null && actoRel.getActo() != actoRel.getActoSig()) {
										log.debug("Generando firma para acto = {} ", actoRel.getActoSig());
										log.debug("AQUI va00 {} ",firma);
										ActoFirma actoFirma = new ActoFirma();
										actoFirma.setActo(actoRel.getActoSig());
										actoFirma.setPkcs7(firma.getPkcs7());
										actoFirma.setFirma(firma.getPkcs7());
										actoFirma.setSecuencia(firma.getSecuencia());
										//actoFirma.setPolitica(firma.getPolitica());
										//actoFirma.setDigestion(firma.getDigestion());
										actoFirma.setSecuenciaTS(firma.getSecuenciaTS());
										//actoFirma.setEstampilla(firma.getEstampilla());
										actoFirma.setUsuario(user);
										actoFirma.setEsActo(true);
										log.debug("AQUI va1 {} ",actoFirma);
										actoFirmaRepository.save(actoFirma);
										log.debug("AQUI va2 {} ",actoFirma);
									}
								}
							}*/
						}
						
					} else {
						isRechazada = true;
						isSuspendida= true;
						for(Suspension item : suspension) 
						{
							item.setEstatusSuspension(1L);
							suspencionRepository.save(suspension);
						}
					}

					Bitacora bitacora = bitacoraRepository.findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(prelacion.getId(), 16L);

					if (bitacora != null) {

						System.out.println("*** TIPO ENTREGA: 21***");
						RevertirEstatusSuspensivos(prelacion);

						System.out.println("*** TIPO ENTREGA: 22 ***");
				   	}

					if(prelacion.getTipoEntrega()==2) {
						System.out.println("*** TIPO ENTREGA: 2 ***");
						//if (firma.getActoId() != null) {
						//	boletaRegistralService.enviaBoletaOnlyActo(prelacion, 2, firma.getActoId());
						//} else {
							boletaRegistralService.enviaBoleta(prelacion, 2);
						//}
					} else{
						System.out.println("*** TIPO ENTREGA: 1 ***");
						//IHM 22-03-2019
						//boletaRegistralService.guardarResultadoTramite(prelacion, 2);						
					}
					

				// GUARDA BOLETA PRELACIÓN
				Prelacion prelacionCurrent = this.findOne(prelacion.getId());
				this.updateStatusPredios(prelacion.getId());
				/* if (prelacion.getPrelacionServiciosParaPrelacions().size() > 0) {
					PrelacionServicio ps = prelacion.getPrelacionServiciosParaPrelacions().iterator().next();
					// PARA COPIAS VUELVE A GENERAR EL ARCHIVO
					if (ps != null
							&& (ps.getServicio().getId() == Constantes.Servicio.COPIAS_CERTIFICADA.getIdServicio()
									|| ps.getServicio().getId() == Constantes.Servicio.IMPRESION_FOLIOS.getIdServicio())
							&& !prelacionCurrent.getStatus().getId()
									.equals(Constantes.Status.RECHAZADO.getIdStatus())) {
						isCopias = true;
						Certificado cert = ps.getCertificadosParaPrelacionServicios().iterator().next();
						this.certificadoService.generaArchivoCopia(prelacion.getId(), cert,prelacion.getConsecutivo().toString());
					}
				} */
				System.out.println("isCopias"+isCopias);
			/* 	if (prelacionCurrent.getStatus().getId().equals(Constantes.Status.RECHAZADO.getIdStatus())) {
					prelacionBoletaService.save(prelacionCurrent.getId(), Constantes.tipoBoleta.BOLETA_RECHAZO.getId());
					this.clearPrelacionRechazada(prelacionCurrent);// CLEAR PRELACION

				}  */
				if (!this.esCertificado(prelacionCurrent) && !isCopias && !isSuspendida) {
					System.out.println("	prelacionBoletaService.save ");
					prelacionBoletaService.save(prelacionCurrent.getId(),
							Constantes.tipoBoleta.BOLETA_REGISTRAL.getId(),prelacion);
					
				}

			/* 	if (!prelacionCurrent.getStatus().getId().equals(Constantes.Status.RECHAZADO.getIdStatus())
						&& !prelacionCurrent.isExentoPago() && prelacionCurrent.getNotificaFinanzas()) {
					this.reportarPagos(prelacionCurrent.getId(), prelacionCurrent.getConsecutivo().toString());
				}*/

			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("\n\n\n /********+*** RESULTADO FALSO *************/");

			return null;
		}

		log.debug("AQUI va1 {} ");
		return prelacion;
		//log.debug("AQUI va2 {} ");
	}

	public Constantes.ETipoFolio getTipoFolio(Prelacion prelacion) {
		List<PrelacionPredio> pps=prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacion.getId());
		
		if (pps!= null && pps.size() > 0 ) {
			Long tipoI = pps.iterator().next().getTipoFolio().getId();

			Integer tipoId = (int) (long) tipoI;
			switch (tipoId) {
				case 4:
					return Constantes.ETipoFolio.PREDIO;
				case 3:
					return Constantes.ETipoFolio.MUEBLE;
				case 2:
					return Constantes.ETipoFolio.PERSONAS_AUXILIARES;
				case 1:
					return Constantes.ETipoFolio.PERSONAS_JURIDICAS;
				default:
					return null;
			}

		}
		return null;
	}


	public List<RequisitoVO>  findAllDocAdjuntosByPrelacionId(Long id){
			List<RequisitoVO> reqs  = new ArrayList<RequisitoVO>();
			List<ActoDocumento> docs=	actoDocumentoRepository.getAllActoDocumentoByActoPrelacionId(id);
			List<ActoRequisito> reqpPrelacion = actoRequisitoRepository.findAllByPrelacion(id);
			Prelacion prelacion  =  this.findOne(id);
			Boolean isServicios = this.isServicios(prelacion);
			Boolean isVE  =  this.isVE(prelacion);
			Set<Documento> setDoc = new HashSet<Documento>();
			
			List<String> listActos = this.getActosFromRequisitos(docs, reqpPrelacion);
			/*
			for (String x : listActos) {
				System.out.println("IHM x:"+x);
				if(isVE && isServicios) {//MUESTRA REQUISITOS SOLO PARA PRELACIONES QUE INGRESARON POR VE Y QUE SEAN SERVICIOS
				  for(ActoRequisito ar: reqpPrelacion ){
	
						if (ar.getActo().getTipoActo().getNombre().equals(x)) {
							System.out.println("IHM Req:"+ar.getId()+" actoId:"+ar.getActo().getId()+ " Acto:"+ar.getActo().getTipoActo().getNombre());
						RequisitoVO temReqVO= new RequisitoVO();
						temReqVO.setRequisito(ar.getRequisito());
						temReqVO.setActo(ar.getActo());
						temReqVO.setFundatorio(false);
						if(ar!=null && ar.getArchivo() != null){
							temReqVO.getArchivos().add(ar.getArchivo());
						}
						reqs.add(temReqVO);
					}
				   }
				
				}
		    
			

			if(docs!=null){
					for(ActoDocumento ad: docs){
						
						if (ad.getActo().getTipoActo().getNombre().equals(x)) {
							System.out.println("IHM ad.ID:"+ad.getId()+" actoId:"+ad.getActo().getId()+ " Acto:"+ad.getActo().getTipoActo().getNombre());
						RequisitoVO temReqVO= new RequisitoVO();
						temReqVO.setTipoDocumento(ad.getDocumento().getTipoDocumento());
							List<DocumentoArchivo> documentosArchivo = new ArrayList<DocumentoArchivo>();
							documentosArchivo = documentoArchivoRepository.findByDocumentoId(ad.getDocumento().getId());
						temReqVO.setIdDocumento(ad.getDocumento().getId());
							temReqVO.setDocumentoArchivo(documentosArchivo);

						if( ad.getDocumento().getArchivo() != null ) {
							temReqVO.getArchivos().add(ad.getDocumento().getArchivo());
						}
						temReqVO.setActo(ad.getActo());
						temReqVO.setFundatorio(true);
						temReqVO.setObservaciones(ad.getDocumento().getObservaciones());
						reqs.add(temReqVO);
					}
			}
				}
			}
			*/
			// IHM
			if(isVE && isServicios) {
				System.out.println("IHM Entrada de SERVICIO");
				for(ActoRequisito ar: reqpPrelacion ){						
						//System.out.println("IHM Req:"+ar.getId()+" actoId:"+ar.getActo().getId()+ " Acto:"+ar.getActo().getTipoActo().getNombre());
						RequisitoVO temReqVO= new RequisitoVO();
						temReqVO.setRequisito(ar.getRequisito());
						//temReqVO.setActo(ar.getActo());
						temReqVO.setFundatorio(false);
						if(ar!=null && ar.getArchivo() != null){
							temReqVO.getArchivos().add(ar.getArchivo());
						}
						reqs.add(temReqVO);
			   }
				if(docs!=null){
					for(ActoDocumento ad: docs){
						if (ad.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_SEGUNDO_AVISO)) {
							setDoc.add(ad.getDocumento());
						}
					}
					for(Documento d:setDoc){
						RequisitoVO temReqVO= new RequisitoVO();
							temReqVO.setTipoDocumento(d.getTipoDocumento());
							List<DocumentoArchivo> documentosArchivo = new ArrayList<DocumentoArchivo>();
							List<DocumentoArchivo> tmpDocsArchivo = new ArrayList<DocumentoArchivo>();
							documentosArchivo = documentoArchivoRepository.findByDocumentoId(d.getId());
							if(documentosArchivo!=null){
								if(documentosArchivo.size()>0){
									tmpDocsArchivo.add(documentosArchivo.get(0));
								}
							}
							temReqVO.setIdDocumento(d.getId());
							temReqVO.setDocumentoArchivo(tmpDocsArchivo);
							if( d.getArchivo() != null ) {
								temReqVO.getArchivos().add(d.getArchivo());
							}
							//temReqVO.setActo(ad.getActo());
							temReqVO.setFundatorio(true);
							temReqVO.setObservaciones(d.getObservaciones());
							reqs.add(temReqVO);
					}
				}

			} else {
				System.out.println("IHM Entrada No de Servicio");
				for (ActoDocumento ad:docs){
					setDoc.add(ad.getDocumento());
				}

				for(Documento d:setDoc){
					RequisitoVO temReqVO= new RequisitoVO();
						temReqVO.setTipoDocumento(d.getTipoDocumento());
						List<DocumentoArchivo> documentosArchivo = new ArrayList<DocumentoArchivo>();
						List<DocumentoArchivo> tmpDocsArchivo = new ArrayList<DocumentoArchivo>();
						documentosArchivo = documentoArchivoRepository.findByDocumentoId(d.getId());
						if(documentosArchivo!=null){
							if(documentosArchivo.size()>0){
								tmpDocsArchivo.add(documentosArchivo.get(0));
							}
						}
						temReqVO.setIdDocumento(d.getId());
						temReqVO.setDocumentoArchivo(tmpDocsArchivo);
						if( d.getArchivo() != null ) {
							temReqVO.getArchivos().add(d.getArchivo());
						}
						//temReqVO.setActo(ad.getActo());
						temReqVO.setFundatorio(true);
						temReqVO.setObservaciones(d.getObservaciones());
						reqs.add(temReqVO);
				}
			}

		return reqs;
	}

	private List<String> getActosFromRequisitos(List<ActoDocumento> documentos, List<ActoRequisito> requisitos) {

		List<String> listActos = new ArrayList<String>();

		documentos.forEach((x) -> {
			if (!listActos.contains(x.getActo().getTipoActo().getNombre()))
				listActos.add(x.getActo().getTipoActo().getNombre());
		});

		requisitos.forEach((x) -> {
			if (!listActos.contains(x.getActo().getTipoActo().getNombre()))
				listActos.add(x.getActo().getTipoActo().getNombre());
		});

		return listActos;
	}


	public List<ActoPredio> findAllActoPredioByPrelacionAndVfFalse(Long idPrelacion) {
			return actoPredioRepository.findAllByPrelacionAndVfFalse(idPrelacion, false);
	}
	
	public List<ActoPredio> findAllActosFusionByPrelacionAndVfFalse(Long idPrelacion){
		List<ActoPredio> actos = findAllActoPredioByPrelacionAndVfFalse(idPrelacion);
		actos =  actos.stream().filter(x->x.getActo().getTipoActo().getId().equals(Constantes.FUSION_TIPO_ACTO_ID)).collect(Collectors.toList());
		return actos;
	}

	public PrelacionPredio  savePrelacionPredio( PrelacionPredio  prelacionPredio, Prelacion prelacion ){
				PrelacionPredio prelaPredio=new PrelacionPredio();
				try{
					System.out.println("TipoFolio  : "+prelacionPredio.getTipoFolio().getId());
					System.out.println("PRELACION  : "+prelacion.getId());

					PersonaJuridica personaJuridica=null;
					FolioSeccionAuxiliar folioSeccionAuxiliar=null;
					Mueble mueble=null;
					Predio predio=null;

					int tipoFolioId = (int)(long)prelacionPredio.getTipoFolio().getId();
					Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromInt(tipoFolioId);

					switch (tipo) {

						case PERSONAS_JURIDICAS:
							personaJuridica= personaJuridicaRepository.findOne(prelacionPredio.getPersonaJuridica().getId());
							prelaPredio.setPersonaJuridica(personaJuridica);
							break;
						case PERSONAS_AUXILIARES :
							folioSeccionAuxiliar= folioSeccionAuxiliarRepository.findOne(prelacionPredio.getFolioSeccionAuxiliar().getId());
							prelaPredio.setFolioSeccionAuxiliar(folioSeccionAuxiliar);
							break;
						case MUEBLE:
							mueble= muebleRepository.findOne(prelacionPredio.getMueble().getId());
							prelaPredio.setMueble(mueble);
							break;
						case PREDIO :
							predio= predioRepository.findOne(prelacionPredio.getPredio().getId());
							prelaPredio.setPredio(predio);
							break;
					}


					TipoFolio tipoFolio = tipoFolioRepository.findOne((long)tipoFolioId);

					prelaPredio.setPrelacion(prelacion);
					prelaPredio.setTipoFolio(tipoFolio);
					prelaPredio.setVersion(1);
					prelaPredio.setEstatus(1);
					//prelaPredio.set(1);
					prelacionPredioRepository.saveAndFlush(prelaPredio);

					prelaPredio.setIdVersion(prelaPredio.getId().toString()+prelaPredio.getPrelacion().getId().toString());
					prelacionPredioRepository.saveAndFlush(prelaPredio);
				}catch(Exception e)
				{
						System.out.println("ocurrio un error");
						e.printStackTrace();
				}
				return prelaPredio;
	}

	public PrelacionPredio savePrelacionPredioAnalisis( Long prelacionId, Long predioId, boolean primerRegistro ){
		PrelacionPredio prelacionPredio = new PrelacionPredio();
		try {

			int tipoFolioId = (int)(long)4;
			TipoFolio tipoFolio = tipoFolioRepository.findOne((long)tipoFolioId);
			
			Prelacion prelacion = findOne(prelacionId);
			Predio predio = predioRepository.findOne(predioId);
			
			if( primerRegistro ) {
				predio.setPrimerRegistro( 1 );
				predioRepository.saveAndFlush( predio );
			}
			
			if( predio.getNoFolio() == null ) {
				Long folio = predioRepository.getFolioFromPredioSequence();
				predio.setNoFolio(Integer.valueOf(folio.intValue()));
				predio.setOficina(prelacion.getOficina());
			}

			prelacionPredio.setPrelacion(prelacion);
			prelacionPredio.setPredio(predio);
			prelacionPredio.setTipoFolio(tipoFolio);
			prelacionPredio.setVersion(1);
			prelacionPredio.setEstatus(1);
			prelacionPredioRepository.saveAndFlush(prelacionPredio);

			prelacionPredio.setIdVersion(prelacionPredio.getId().toString()+prelacionPredio.getPrelacion().getId().toString());
			prelacionPredioRepository.saveAndFlush(prelacionPredio);
		} catch(Exception e) {
				System.out.println("ocurrio un error");
				e.printStackTrace();
		}
		return prelacionPredio;
	}


	public PrelacionOficio getPrelacionOficio(Long idPrelacion) {
		PrelacionOficio prelacionOfi =null;
		Integer max= prelacionOficioRepository.maxByPrelacion(idPrelacion);
		if(max!=null){
				 prelacionOfi = prelacionOficioRepository.findByPrelacionIdAndVersion(idPrelacion,max);

		}

		return  prelacionOfi;
	}



    public PrelacionOficio savePrelacionOficio(PrelacionOficio prelacionOficio){
        	Integer max= prelacionOficioRepository.maxByPrelacion(prelacionOficio.getPrelacion().getId());	

			if(max!=null){
					max=max+1;
			}else{
				max=1;
			}
			prelacionOficio.setId(null);
			prelacionOficio.setVersion(max);

		prelacionOficioRepository.saveAndFlush(prelacionOficio);
		return prelacionOficio;
	}

	public PrelacionOficio endPrelacionOficio (PrelacionOficio prelacionOficio){
        	Integer max= prelacionOficioRepository.maxByPrelacion(prelacionOficio.getPrelacion().getId());
			if(max!=null){
					max=max+1;
			}else{
				max=1;
			}
			Integer numO =prelacionOficioRepository.getConsecutivo();
			prelacionOficio.setId(null);
			prelacionOficio.setVersion(max);
			prelacionOficio.setNumOficio(prelacionOficioRepository.getConsecutivo());
			String plantilla = prelacionOficio.getPlantilla();
			String plantillaNum = "<p style='text-align: right;'><span style='font-size: 14px; font-family: Arial, Helvetica, sans-serif;'>DJ-"+numO+"/2007</span></p><br>"+plantilla;

			prelacionOficio.setPlantilla(plantillaNum);

			

		prelacionOficioRepository.saveAndFlush(prelacionOficio);
		return prelacionOficio;
	}

	public PrelacionPredio findByVersionId(String idVersion){
		List<PrelacionPredio> prelacionesPredio = prelacionPredioRepository.findByVersionId(idVersion);
		return prelacionesPredio.get(0);
	}

	public List<PrelacionPredio> findPrelacionPredio(Long idPrelacion){
		List<PrelacionPredio> nuevo =  new ArrayList<PrelacionPredio>();
		//List<PrelacionPredio> result = prelacionPredioRepository.findByPrelacionIdAndPredioStatusActoId(idPrelacion, Constantes.StatusActo.ACTIVO.getIdStatusActo()); 
		List<PrelacionPredio> result = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(idPrelacion);
		for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			PrelacionPredio prelacionPredio = (PrelacionPredio) iterator.next();
			//if (!turnadorService.isCaratulaActualizada(prelacionPredio)){
				nuevo.add(prelacionPredio);
			//}
			
		}
		return nuevo;		 
	}

	public List<PrelacionPredio>findPrelPredio(Long idPrelacion){
		List<PrelacionPredio> prelPredio =  new ArrayList<PrelacionPredio>();
		prelPredio= prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(idPrelacion);
		return prelPredio;
	}
	
	@Transactional
	public String turnarAAnalista(Long idPrelacion){
		String messageError = null;
		log.debug("Consultando la prelacion con id : {}",idPrelacion);
		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);

		log.debug("Consultando antecedentes para saber si se realiza validacion ...");
		//List<Antecedente> lstAntecedentes = antecedenteRepository.findByPrelacionAntesParaAntecedentesPrelacionId(idPrelacion);
		List<PrelacionAnte> prelacionesAnte = prelacionAnteRepository.findByPrelacionId(idPrelacion);
		if(prelacionesAnte  != null && !prelacionesAnte.isEmpty()){//Turnador de antecedente
			log.debug("Se encontraron antecedentes se realizaran validaciones ...");
			if(!validaFoliosAntecedentes(prelacionesAnte)){
				messageError = "La prelacion tiene antecedentes sin folios creados";
			}
		}

		if(messageError == null && !validaActosFirmados(prelacion)){
			messageError = "La prelacion tiene actos sin firmar";
		}

		log.debug("Error encontrado : [{}]",messageError);
		if(messageError == null){
			Status status = new Status();
			status.setId(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus());
			log.debug("Enviando a materializacion ...");
			materializacionActoService.materializarPrelacion(idPrelacion);
			log.debug("actualizando prelacion a estado 4");
			prelacion.setStatus(status);
			prelacionRepository.save(prelacion);
			log.debug("Enviando a turnador ...");
			enviarATurnador(prelacion);
			log.debug("Turnado exitoso!");
			messageError = "La prelacion "+prelacion.getConsecutivo()+" se envio al Analista";
		}
		return messageError;
	}

	
	public List<String> getObservacionesPrelacion(Prelacion prelacion){
		 

			Set<Acto> actos=	prelacion.getActosParaPrelacions();
			List<String> observaciones = new ArrayList<String>();
			if(actos!=null) {
					for(Acto a :actos) {

								List<RequisitoVO> reqs=	actoRequisitoService.getRequisitosByActo(a,true);
								for(RequisitoVO requisito: reqs) {
										if(requisito.getFundatorio()) {
												if(requisito.getArchivos().size() == 0 ) {
													observaciones.add("El documento fundatorio "+ requisito.getTipoDocumento().getNombre()+ "  del acto "+a.getTipoActo().getNombre()+ ",  no se agrego a la prelación ");

												}else {
														if(requisito.getObservaciones()!=null)
															observaciones.add("El documento fundatorio "+ requisito.getTipoDocumento().getNombre()+ "  del acto "+a.getTipoActo().getNombre()+ ", contiene las siguientes observacioens: "+requisito.getObservaciones() );

												}

										}else {
											/*if(requisito.getArchivos().size() == 0 ) {
												if(requisito.getRequisito().getId()!=2)
												{
												//observaciones.add("El requisito "+ requisito.getRequisito().getNombre()+ "  del acto "+a.getTipoActo().getNombre()+ ",  no se agrego a la prelación ");
												}
												else{
													if(!prelacion.isExentoPago() ) {
													//observaciones.add("El requisito "+ requisito.getRequisito().getNombre()+ "  del acto "+a.getTipoActo().getNombre()+ ",  no se agrego a la prelación ");
													}
												}
											}*/

										}

								}
					}
			}

			Set<PrelacionPredio>    predios=	prelacion.getPrelacionPrediosParaPrelacions();

			if(predios!=null) {
				for(PrelacionPredio p :predios) {
							if(!p.getValido()){

								int tipoFolioId = (int)(long)p.getTipoFolio().getId();
								Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromInt(tipoFolioId);

								switch (tipo) {

									case PERSONAS_JURIDICAS:
										if(p.getPersonaJuridica().getBloqueado()!=null){
											if(p.getPersonaJuridica().getBloqueado())
												observaciones.add("No fue posible validar el antecedente y sus titulares ");
											else
												observaciones.add("El folio  no fue verificado por el usuario ");
										}else{
											observaciones.add("El folio  no fue verificado por el usuario ");
										}
										break;
									case PERSONAS_AUXILIARES :
											if(p.getFolioSeccionAuxiliar().getBloqueado()!=null){
												if(p.getFolioSeccionAuxiliar().getBloqueado())
													observaciones.add("No fue posible validar el antecedente y sus titulares ");
												else
													observaciones.add("El folio  no fue verificado por el usuario ");
											}else{
												observaciones.add("El folio  no fue verificado por el usuario ");
											}
											break;
									case MUEBLE:
										if(p.getMueble().getBloqueado()!=null){
											if(p.getMueble().getBloqueado())
												observaciones.add("No fue posible validar el antecedente y sus titulares ");
											else
												observaciones.add("El folio  no fue verificado por el usuario ");
										}else{
											observaciones.add("El folio  no fue verificado por el usuario ");
										}
										break;
									case PREDIO :
											if(p.getPredio().getBloqueado()!=null){
												if(p.getPredio().getBloqueado())
													observaciones.add("No fue posible validar el antecedente y sus titulares ");
												
													//observaciones.add("El predio  no fue verificado por el usuario ");
											}else{
												//observaciones.add("El predio  no fue verificado por el usuario ");
											}
										break;
								}	



								
								
							}

				}
			}
			
			Set<PrelacionAnte> antecedentes=	prelacion.getPrelacionAntesParaPrelacions();

			if(antecedentes!=null) {
				for(PrelacionAnte pa :antecedentes) {
							//if(!pa.getValido())
							//observaciones.add("El antecedente no fue verificado por el usuario ");

				}
			}

		return observaciones;
	}

	private boolean validaFoliosAntecedentes(List<PrelacionAnte> prelacionesAnte){
		boolean isFolioValido = true;
		List<Predio> lstPredios =  null;
		List<PrelacionPredio> lstPrelacionesPredio = null;
		for(PrelacionAnte prelacionAnte: prelacionesAnte){//Los antecedentes deben ser uno a uno con los folios
			if(prelacionAnte.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PREDIO.idTipoFolio.intValue()){
				lstPredios =  predioRepository.findByPrelacionPrediosParaPrediosPrelacionId(prelacionAnte.getPrelacion().getId());
				if(lstPredios != null && !lstPredios.isEmpty() && lstPredios.get(0).getNoFolio() == null){
					isFolioValido = false;
					break;
				}
			}else if(prelacionAnte.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.MUEBLE.idTipoFolio.intValue()){
				lstPrelacionesPredio = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionAnte.getPrelacion().getId());
				if(lstPrelacionesPredio != null && !lstPrelacionesPredio.isEmpty() && lstPrelacionesPredio.get(0).getMueble().getNoFolio() == null){
					isFolioValido = false;
					break;
				}
			}else if(prelacionAnte.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio.intValue()){
				lstPrelacionesPredio = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionAnte.getPrelacion().getId());
				if(lstPrelacionesPredio != null && !lstPrelacionesPredio.isEmpty() &&  lstPrelacionesPredio.get(0).getPersonaJuridica().getNoFolio() == null){
					isFolioValido = false;
					break;
				}
			}else if(prelacionAnte.getTipoFolio().getId().intValue() == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio.intValue()){
				lstPrelacionesPredio = prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionAnte.getPrelacion().getId());
				if(lstPrelacionesPredio != null && !lstPrelacionesPredio.isEmpty() &&  lstPrelacionesPredio.get(0).getFolioSeccionAuxiliar().getNoFolio() == null){
					isFolioValido = false;
					break;
				}
			}
		}
		return isFolioValido;
	}



	public Bitacora createBitacora(Prelacion prelacion){
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Bitacora bitacora = new Bitacora();
		bitacora.setStatus(prelacion.getStatus());
		bitacora.setUsuario(usuario);
		bitacora.setPrelacion(prelacion);
		bitacora.setFecha(UtilFecha.getCurrentDate());

		return bitacora;
	}

	public void deleteBitacoraByActo(Long actoId) {
		bitacoraRepository.deleteByActoId(actoId);
	}

	
	public Bitacora createBitacora(Prelacion prelacion,String observaciones,Acto acto){
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Bitacora bitacora = new Bitacora();
		bitacora.setStatus(prelacion.getStatus());
		bitacora.setUsuario(usuario);
		bitacora.setPrelacion(prelacion);
		bitacora.setFecha(UtilFecha.getCurrentDate());
		bitacora.setObservaciones(observaciones);
		bitacora.setActo(acto);
		return bitacoraRepository.save(bitacora);
	}
	
	public Optional<Bitacora> findBitacoraByActoId(Long actoId) {
		return bitacoraRepository.findFirstByActoId(actoId);
	}


	public Bitacora createBitacoraCompleta(Prelacion prelacion, Motivo motivo, TipoRechazo tRechazo,TipoMotivo tMotivo, TipoAclaracion tAclaracion  ,String aclaracion){
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Bitacora bitacora = new Bitacora();
		bitacora.setStatus(prelacion.getStatus());
		bitacora.setUsuario(usuario);
		bitacora.setPrelacion(prelacion);


		if(prelacion.getStatus().getId()==17) {
			Bitacora bitacoraActual=bitacoraRepository.findTop1BitacoraByPrelacionIdOrderByIdDesc(prelacion.getId());
			if(bitacoraActual!=null && bitacoraActual.getStatus().getId()==16L){
				if(bitacoraActual.getObservaciones()!=null){
					bitacora.setObservaciones(bitacoraActual.getObservaciones());
				}
				if(bitacoraActual.getMotivo()!=null){
					motivo=bitacoraActual.getMotivo();
				}
				if(bitacoraActual.getTipoRechazo()!=null){
					tRechazo=bitacoraActual.getTipoRechazo();
				}
			}
		}


		if(motivo!=null){
			bitacora.setMotivo(motivo);
		}


		if(tRechazo!=null){
			bitacora.setTipoRechazo(tRechazo);
		}

		if(tMotivo!=null){
			bitacora.setTipoMotivo(tMotivo);
		}

		if(tAclaracion!=null){
			bitacora.setTipoAclaracion(tAclaracion);
		}

		if(tRechazo!=null){
			bitacora.setTipoRechazo(tRechazo);
		}

		if(aclaracion!=null && tAclaracion!=null){
			bitacora.setAclaraciones(aclaracion);
		}else{
			bitacora.setObservaciones(aclaracion);
		}

		bitacora.setFecha(UtilFecha.getCurrentDate());

		return bitacora;
	}
	
	
	public Bitacora createBitacoraReproceso(Prelacion prelacion, String aclaracion){
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Bitacora bitacora = new Bitacora();
		bitacora.setStatus(prelacion.getStatus());
		bitacora.setUsuario(usuario);
		bitacora.setPrelacion(prelacion);
		bitacora.setFecha(UtilFecha.getCurrentDate());
		bitacora.setObservaciones(aclaracion);
		bitacora.setReproceso(true);

		return bitacora;
	}
	
	public ResultPrelacionVO  validaPrelacion(Long idPrelacion,boolean contienePago, Boolean presencial) {
		log.info("PRELACION ID: "+idPrelacion);
		Prelacion prelacion=prelacionRepository.findOne(idPrelacion);
		
		ResultPrelacionVO result= new ResultPrelacionVO();
		result.setCodigos(new ArrayList<String>());
		Set<Acto> actos=	prelacion.getActosParaPrelacions();
		Boolean exDocumento,exLlenado,exArchivo;
		
		
		if(actos!=null) {
				for(Acto a :actos) {
					System.out.println("act-"+a);
					System.out.println("tipo-acto---"+a.getTipoActo());
							exDocumento=false;
							exLlenado=false;
							exArchivo=false;
							List<RequisitoVO> reqs=	actoRequisitoService.getRequisitosByActo(a,true);	
						
							for(RequisitoVO requisito: reqs) {								
								System.out.println("req---"+requisito.getFundatorio());
									if(requisito.getFundatorio()) {
										exDocumento=true;
									
										if(requisito.getArchivos().size() > 0 )
										exArchivo=true;

									}
									if(requisito.getCantidad()!=null && requisito.getCantidad()==1){
										exDocumento=true;
									
										if(requisito.getArchivos().size() > 0 )
										exArchivo=true;
									}
							}
							if(a.getTipoActo().getId()==210L||a.getTipoActo().getId()==123L){
								exDocumento=true;
								exArchivo=true;
							}	
							log.info("ACTO PREDIO ACTO : "+a.getActoPrediosParaActos().size());
							
							for(ActoPredio ap : a.getActoPrediosParaActos()) {
								
									Set<ActoPredioCampo> llenados=ap.getActoPredioCamposParaActoPredios();
									log.info("ACTO PREDIO CAMPOT : "+llenados.size());
									if(llenados!=null && llenados.size()>0) {
											exLlenado=true;
									}
							}
						

							if (a.getTipoActo() != null) {

								if (a.getTipoActo().isRequiereDocumento() && !exDocumento) {

									result.getCodigos().add("No existe captura de datos sobre el documento fundatorio  del acto " + a.getTipoActo().getNombre());

								}else{
									if(exDocumento &&  !exArchivo){
										result.getCodigos().add("El requisito fundatorio no se ha entregado  " + a.getTipoActo().getNombre());
									}
								}
							}
							
							if(a.getTipoActo().getId()!=123 && !a.getTipoActo().getId().equals(Constantes.TIPO_ACTO_CERTIFICADO_LG_CAUTELAR_INSERTO)) {
							if(!exLlenado && (a.getTipoActo().isRequiereCaptura() &&  !presencial))
							{
								
								result.getCodigos().add(" No existe datos de precaptura  del acto "+a.getTipoActo().getNombre());	
								System.out.println(a.getTipoActo()+"\n"+a.getId()+"\n\n\n "+a.getTipoActo().getId());
							}
							}
							
							
							
				}
			
		}



		Set<PrelacionServicio> servicios = prelacion.getPrelacionServiciosParaPrelacions();
		
		if(servicios!=null && servicios.size()>0 ){
			
			PrelacionServicio ps=servicios.iterator().next();

		    if(ps.getServicio().getId()==Constantes.Servicio.BUSQUEDA_HISTORIAL.getIdServicio() || ps.getServicio().getId()==Constantes.Servicio.BUSQUEDA_SIMPLE.getIdServicio()){

				HashMap<String, Integer> busquedas= busquedaService.getTipoBusquedasFromPrelacion(prelacion.getId());

				
					boolean tieneBusqueda=false;
					Map<String,Integer> entradas=new HashMap<String, Integer>(busquedas);
					for (Map.Entry<String,Integer> entry : entradas.entrySet()) {
							if(entry.getValue()>0){
								tieneBusqueda=true;
							}
					}
					if(!tieneBusqueda){
						result.getCodigos().add("No se encontraron busquedas guardadas para la prelacion");	
						result.setValida(false);

					}
				
			}

		}

		


		if(contienePago){
			Set<Recibo> recibos = prelacion.getRecibosParaPrelacions();
			
			if(recibos.size()==0) {
				result.getCodigos().add("No se encontraron datos del recibo de pago para la prelacion");	
				result.setValida(false);
			}
		}
		if(result.getCodigos().size()==0) {
			result.setValida(true);
		}
		
		return result;
	}

	private boolean validaActosFirmados(Prelacion prelacion){
		boolean isPrelacionValida = true;
		
		if(prelacion.getActosParaPrelacions() != null && !prelacion.getActosParaPrelacions().isEmpty()){
			for(Acto acto:prelacion.getActosParaPrelacions()){
				if(acto.getActoFirmasParaActos() == null || acto.getActoFirmasParaActos().isEmpty()){
					isPrelacionValida = false;
					break;
				}
			}
		}
		
		
		return isPrelacionValida;
	}



	public Boolean  validaActoPrecaptura(Prelacion prelacion) {
		Boolean valido= false;
		int contador =0; 
		List<Acto> actosPrelacion=null;
		actosPrelacion =actoService.findByPrelacionId(prelacion.getId());
		System.out.println("actos{}---"+actosPrelacion);
		int actosValidos=0;
		if(actosPrelacion!= null){
			for(Acto acto:actosPrelacion){

				Boolean actoValido= false;	
				if(acto!=null && acto.getStatusActo().getId()!= Constantes.StatusActo.TEMPORAL.getIdStatusActo()) {
					actosValidos++;
					for(ActoPredio ap : acto.getActoPrediosParaActos()) {
						System.out.println("id AC-PRE---"+ap.getId());
						List<ActoPredioCampo> llenados = actoPredioCampoRepository.findByActoPredioId(ap.getId());
						//	Set<ActoPredioCampo> llenados=ap.getActoPredioCamposParaActoPredios();
						log.info("ACTO PREDIO CAMPOT : "+llenados.size());
						if(llenados!=null && llenados.size()>0) {
							actoValido=true;
						}	
					}
					if(!acto.getTipoActo().isRequiereCaptura()){
						actoValido=true;
						}
				}//fin if acto
				else{
					contador++;
				}
				if(actoValido){
					contador++;
				}

			}//fin for actos

			if(actosPrelacion.size() == contador ){
				valido=true;
			}
		}
		return valido;
	}


    public PrelacionUsuarioDef setUsuarioDefault(PrelacionVO pre) {

		Persona temp = pre.getSolicitante().getPersona();

		log.info("Prelacion Id: " + Integer.toString(pre.getId().intValue() ));
		log.info(temp.getNombre());

		Prelacion prelacion = prelacionRepository.findOne(pre.getId());

		PrelacionUsuarioDef def = new PrelacionUsuarioDef();
		def.setPrelacion(prelacion);
		def.setPaterno(temp.getPaterno());
		def.setMaterno(temp.getMaterno());
		def.setNombre(temp.getNombre());
		def.setTelefono(getFirstTelefonoFromPersona (temp.getTelefonosParaPersonas()));


		Identificacion iden = pre.getIdentificacionPersona();

		def.setTipoIden(iden.getTipoIden());
		def.setValor (iden.getValor());


		prelacionUsuarioDefRepository.save(def);

		return def;

    }

	public PrelacionUsuarioDef updateUsuarioDefault(PrelacionVO pre, Long prelacionUsuarioDefId) {

		Persona temp = pre.getSolicitante().getPersona();

		log.info("Prelacion Id: {}", pre.getId());
		log.info(temp.getNombre());

		Prelacion prelacion = prelacionRepository.findOne(pre.getId());

		PrelacionUsuarioDef def = prelacionUsuarioDefRepository.findOne(prelacionUsuarioDefId);
		def.setPrelacion(prelacion);
		def.setPaterno(temp.getPaterno());
		def.setMaterno(temp.getMaterno());
		def.setNombre(temp.getNombre());
		def.setTelefono(getFirstTelefonoFromPersona (temp.getTelefonosParaPersonas()));

		Identificacion iden = pre.getIdentificacionPersona();
		def.setTipoIden(iden.getTipoIden());
		def.setValor(iden.getValor());

		prelacionUsuarioDefRepository.save(def);

		return def;
	}
    
    public PrelacionUsuarioDef getUsuarioDefault(Long prelacionId) {

		PrelacionUsuarioDef def = new PrelacionUsuarioDef();
		
		def = prelacionUsuarioDefRepository.findOneByPrelacionId(prelacionId);

		return def;

    }

	private String getFirstTelefonoFromPersona(Set<Telefono> telefonosParaPersonas) {
		if (telefonosParaPersonas == null)
			return "";
		if (telefonosParaPersonas.size() == 0)
			return "";

		final String[] tele = {""};

		Optional<Telefono> temp = telefonosParaPersonas.stream().findFirst();
		temp.map(tel -> tele[0] = tel.getNumTelefono() );

		return tele[0];

	}

	@Transactional
	public boolean  enviarCorreo(List<DireccionArea> direccion,long oficina,long prelacionId){
		
		
		  byte[] bytesArray =null;
		  FileInputStream fileInputStream=null;
		  
		  PrelacionOficio prelaOf = prelacionOficioRepository.findByMaxPrelacion(prelacionId);

		  try{
		  	HtmlToPDF.createPdf(HtmlToPDF.DEST,prelaOf.getPlantilla());

		    File fichero=new  File("C:/Users/USUARIO/Desktop/Test.pdf");
		    if(fichero.exists()){
		    	 bytesArray = new byte[(int) fichero.length()];
		    	   fileInputStream = new FileInputStream(fichero);
	               fileInputStream.read(bytesArray);
	               for (int i = 0; i < bytesArray.length; i++) {
	                           System.out.print((char)bytesArray[i]);
	                }

		    	System.out.println ("recuperado");
		    }else{
		    	System.out.println ("NOrecuperado");
		    }
		  }catch(Exception e){
		  	return false;
		  }	

		  DireccionArea direccionForanea = new DireccionArea();
		String to ="";
		Oficina ofi = oficinaRepository.findOne(oficina);
		

		try{						
			//validarsi la oficina es diferente a Guadalajara entonces
			//tambien se enviara a la direccion de oficinas FOREANEAS
			if(ofi.getNuRegion() !=1){// es diferente de 1 que es gudalajara
				//traer a la direccion de oficinas FORANEAS
				 direccionForanea = direccionAreaRepository.findByClave("DIRFO");
				 to +=direccionForanea.getCorreo()+",";
			}
			
			for( DireccionArea d :direccion ){
				to +=d.getCorreo()+",";
			}

			to = to.substring(0, to.length()-1);
			
			 
			String body = "Test";									

			if (to==null || to.length()==0){
				log.debug("No se envio correo");
			} else {
				log.debug("tratando de enviar email a " + to);
			
				Context context = new Context();
				/*context.setVariable("nombre", direccion.getNombre());
				context.setVariable("puesto", direccion.getPuesto());*/
				context.setVariable("oficina", ofi.getNombre());
				
				
				//mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor() , to, "Información solicitada ","notificacionOficioTemplate",context,bytesArray);
				
				
			}
			return true;
			
		//} catch(IllegalArgumentException  | MessagingException e){		
			//return true;
			
			//TODO enviar a TURNADOR, dala implementar cambios a la prelacion, para que no se cicle, envio y recepcion
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		}
			
		

	}


	public byte[] generarPDF(long idPrelacion){
		
		byte[] bytesArray =null;
		FileInputStream fileInputStream=null;		  
		PrelacionOficio prelaOf = prelacionOficioRepository.findByMaxPrelacion(idPrelacion);

		String url ="";
		 try{
		 	
		  	HtmlToPDF.createPdf(Constantes.IMG_PATH+"oficio-"+idPrelacion+".pdf",prelaOf.getPlantilla());

		    File fichero=new  File(Constantes.IMG_PATH+"oficio-"+idPrelacion+".pdf");
		    System.out.println ("URL"+Constantes.IMG_PATH+"oficio-"+idPrelacion+".pdf");
		    if(fichero.exists()){
		    	 bytesArray = new byte[(int) fichero.length()];
		    	   fileInputStream = new FileInputStream(fichero);
	               fileInputStream.read(bytesArray);
	               for (int i = 0; i < bytesArray.length; i++) {
	                          // System.out.print((char)bytesArray[i]);
	                }
	               
		    	System.out.println ("recuperado");
		    }else{

		    	System.out.println ("NOrecuperado");
		    }
		  }catch(Exception e){
		  	return bytesArray;
		  }	
		  
		  return bytesArray;
	}

	public byte[] generarPdfActaRevocacionTestamento() throws JRException{

		ActaDeRevocacionVO actaDeRevocacion= new ActaDeRevocacionVO();
		
		actaDeRevocacion.setNumeroActa(Long.valueOf("2"));
		actaDeRevocacion.setLibro(Long.valueOf("2"));	
		actaDeRevocacion.setNombreTesteador("Jesus Vasquez");	
		actaDeRevocacion.setCiudad("Oaxaca");
		actaDeRevocacion.setHoraNumero("12:30");
		actaDeRevocacion.setHoraLetra("Doce treinta");
		actaDeRevocacion.setDia("2");
		actaDeRevocacion.setMesLetra("Noviembre");
		actaDeRevocacion.setAnio("2017");
		actaDeRevocacion.setNombreDirector("yolanda vasquez");
		actaDeRevocacion.setDiaRecepcion("2");
		actaDeRevocacion.setMesRecepcion("agosto");
		actaDeRevocacion.setNoPrelacion("4");
		actaDeRevocacion.setNombreNotario("pedrito fernandez");
		actaDeRevocacion.setCiudadano("mexicano");
		actaDeRevocacion.setDiaTestamento("23");
		actaDeRevocacion.setMesLetraTestamento("enero");	
		actaDeRevocacion.setRegistradorAsignado("jesus vasquez");

		List<ActaDeRevocacionVO> revocacion = new ArrayList<ActaDeRevocacionVO>();
		revocacion.add(actaDeRevocacion);
		JRDataSource ds = new JRBeanCollectionDataSource(revocacion);

		Map<String, Object> parameterMap = new HashMap<String, Object>();	

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
   
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfActaRevocacionTestamento.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}


	@Transactional
	public Prelacion updateOficina(Long prelacionId, Long oficinaId) {
		Prelacion prelacion = prelacionRepository.findOne(prelacionId);
		if (prelacion != null ) {
			Oficina oficina = null;
			if (oficinaId != null && oficinaId != -1 )
				oficina = oficinaRepository.findOne(oficinaId);

			prelacion.setOficina(oficina);
			prelacionRepository.save(prelacion);
		}
		return prelacion;

	}
	
	public byte[] generPdfActaDepositoTestamento() throws JRException{

		DepositoTestamentoVO depositoTestamento = new DepositoTestamentoVO();
		depositoTestamento.setNumeroActa(Long.valueOf("3"));
		depositoTestamento.setLibro(Long.valueOf("2"));
		depositoTestamento.setNombreTesteador("Yolanda");
		depositoTestamento.setCiudad("Jalisco");
		depositoTestamento.setHoraNumero("12:20");
		depositoTestamento.setHoraLetra("Doce veinte");		
		depositoTestamento.setDia("04");
		depositoTestamento.setMesLetra("Noviembre");
		depositoTestamento.setAnio("2017");
		depositoTestamento.setNombreDirector("Jesus Vasquez Aquiono");
		depositoTestamento.setLugarNacimiento("Oaxaca");
		depositoTestamento.setFechaNacimiento("24 de enero de 2017");
		depositoTestamento.setEdad(Long.valueOf("12"));
		depositoTestamento.setEstadoCivil("Soltero");
		depositoTestamento.setOcupacion("estudiante");
		depositoTestamento.setDomicilio("conocido");
		depositoTestamento.setFolioCredencial("wwwwww");
		depositoTestamento.setCurp("000000000");
		depositoTestamento.setNombreTestigo("Victor alonso");
		depositoTestamento.setLugarNacimientoTestigo("ejemplo");
		depositoTestamento.setFechaNacTestigo("12/12/12");
		depositoTestamento.setEdoCivilTestigo("casado");
		depositoTestamento.setOcupacionTestigo("desarrollo de software");
		depositoTestamento.setDomicilioTestigo("conocido2");
		depositoTestamento.setAnioConocer("3");
		depositoTestamento.setFolioCredeTestigo("qwqwqw");
		depositoTestamento.setRegistradorAsignado("asasa");
	

		List<DepositoTestamentoVO> depositos = new ArrayList<DepositoTestamentoVO>();
		depositos.add(depositoTestamento);
		JRDataSource ds = new JRBeanCollectionDataSource(depositos);

		Map<String, Object> parameterMap = new HashMap<String, Object>();	

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
   
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfActaDepositoTestamento.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
    }
	
	
	public byte[] generarPdfActaRetiroTestamento() throws JRException{
	
		ActaDeRetiroVO actaDeRetiro = new ActaDeRetiroVO();
		actaDeRetiro.setHoraNumero("12:30");
		actaDeRetiro.setHoraLetra("Doce treinta ");
		actaDeRetiro.setDia("20");
		actaDeRetiro.setMesLetra("Noviembre");
		actaDeRetiro.setNombreDirector("Jesus eduardo Vasquez");
		actaDeRetiro.setCiudadano("Mexicano");
		actaDeRetiro.setFolioCredencial("1234567890");
		actaDeRetiro.setDiaDeposito("23");
		actaDeRetiro.setMesLetraDeposito("Enero");
		actaDeRetiro.setNumeroActa(Long.valueOf("2"));
		actaDeRetiro.setLibro(Long.valueOf("2"));
		actaDeRetiro.setRegistradorAsignado("Yolanda Vasquez");
	
		List<ActaDeRetiroVO> retiros = new ArrayList<ActaDeRetiroVO>();
		retiros.add(actaDeRetiro);
		JRDataSource ds = new JRBeanCollectionDataSource(retiros);
	
		Map<String, Object> parameterMap = new HashMap<String, Object>();
	
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
	
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfActaRetiroTestamento.jasper");
	
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
	
		return JasperExportManager.exportReportToPdf(jasperPrint);
	
	
	}


	public Prelacion adelantarPrelacion(ModificarPrelacionVO vo) {
		Prelacion prelacion = prelacionRepository.findOne(vo.getPrelacionId());
		Prioridad prioridadNueva = prioridadRepository.findOne(vo.getMarcaId());

		Bitacora bitacora  	= createBitacoraCompleta(prelacion, null ,null,null,null,"");
		Motivo motivo 		= motivoRepository.findOne(vo.getCausaId());

		bitacora.setPrioridadAnterior(prelacion.getPrioridad());
		bitacora.setPrioridadNuevo(prioridadNueva);
		bitacora.setMotivo(motivo);
		prelacion.setPrioridad(prioridadNueva);


		prelacionRepository.save(prelacion);
		bitacoraRepository.save(bitacora);


		return prelacion;
	}

	public Prelacion returnarPrelacion(ModificarPrelacionVO vo){
		Prelacion prelacion = prelacionRepository.findOne(vo.getPrelacionId());
		Usuario usuario = usuarioRepository.findOne(vo.getRegistradorId());	
		Motivo motivo = motivoRepository.findOne(vo.getCausaId());
		Usuario usuarioAnterior = this.getUsuarioAnterior(prelacion);
		
		System.out.println(" usuarioAnterior " +usuarioAnterior);
		System.out.println(" usuario " +usuario);
		
		
		prelacion = this.returnar(prelacion, usuario);
		System.out.println("prelacion " +prelacion);
		prelacionRepository.saveAndFlush(prelacion);
		this.fillBitacoraForReturnar(prelacion, motivo, null, usuarioAnterior, usuario);
		return prelacion;
	}
	
	private Prelacion returnar(Prelacion prelacion, Usuario usuario){
		
		switch (Constantes.Status.fromLong(prelacion.getStatus().getId())) {
			case ASIGNADOR_A_VALIDADOR_DE_PREDIOS:
				prelacion.setUsuarioCapVal(usuario);
			break;
			case EN_PROCESO_ASIGNACION:
				prelacion.setStatus(statusRepository.findOne(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()));
				prelacion.setUsuarioAnalista(usuario);
			break;
			case ASIGNADO_A_ANALISTA:
				prelacion.setUsuarioAnalista(usuario);
				break;
			case ASIGNADO_A_CALIFICADOR:
				prelacion.setUsuarioCalificador(usuario);
				break;
			case ANALISTA_VIRTUAL:
				prelacion.setStatus(prelacion.getStatusAnterior());
				prelacion.setStatusAnterior(this.statusRepository.findOne(14L));
				prelacion = returnar(prelacion, usuario);
				break;
			default: 
				break;
		}
		return prelacion;
	}
	
	private Usuario getUsuarioAnterior(Prelacion prelacion) {
		Usuario usuarioAnterior =  null;
		switch (Constantes.Status.fromLong(prelacion.getStatus().getId())) {
		case ASIGNADOR_A_VALIDADOR_DE_PREDIOS:
			usuarioAnterior =  prelacion.getUsuarioCapVal();
		break;
		case ASIGNADO_A_ANALISTA:
			usuarioAnterior =  prelacion.getUsuarioAnalista();
			break;
		case ASIGNADO_A_CALIFICADOR:
			usuarioAnterior =  prelacion.getUsuarioCalificador();
			break;
		case ANALISTA_VIRTUAL:
			prelacion.setStatus(prelacion.getStatusAnterior());
			usuarioAnterior = this.getUsuarioAnterior(prelacion);
			break;
		default:
			break;
		}
		return usuarioAnterior;
	}
	
	private void fillBitacoraForReturnar (Prelacion prelacion, Motivo motivo, String observaciones, Usuario anterior, Usuario usuario) {
		Bitacora bitacora  = createBitacoraCompleta(prelacion, motivo ,null,null,null, observaciones);
		bitacora.setRegistradorAnterior(anterior);
		bitacora.setRegistradorNuevo(usuario);
		bitacoraRepository.saveAndFlush(bitacora);
	}

	public Prelacion returnarAnalistaVirtual(ModificarPrelacionVO vo) throws Exception{
		Prelacion prelacion = prelacionRepository.findOne(vo.getPrelacionId());		
		if (prelacion.getStatus().getId().equals(14L)) {
			throw new Exception("La prelación ya pertenece al analista virtual");
		}
		StringBuilder observacionesBuilder = new StringBuilder();
		Motivo motivo = motivoRepository.findOne(vo.getCausaId());
		Usuario usuario = this.getUsuarioAnterior(prelacion);
		observacionesBuilder.append("Los siguientes números de prelación no permiten que la prelación en cuestión sea procesada. \n");
		List<Prelacion> blockingPrelacionees = this.isPrelacionAnalizable(prelacion);
		for (Prelacion x : blockingPrelacionees) {
			observacionesBuilder.append("\n Consecutivo: " + x.getConsecutivo().toString() + " Estado: " + x.getStatus().getNombre());
		}
		String observaciones = observacionesBuilder.toString();
		log.debug(observaciones);
		try {
			observaciones = observaciones.substring(0, 999);
		}
		catch(Exception e){}
		prelacion.setStatusAnterior(prelacion.getStatus());
		prelacion.setStatus(this.statusRepository.findOne(14L));
		prelacionRepository.saveAndFlush(prelacion);
		this.fillBitacoraForReturnar(prelacion, motivo, observaciones, usuario, usuario);
		return prelacion;
	}
	
	public List<Prelacion> findAllByStatus(List<Long> status){
		
		List<Prelacion> prelacione = null;
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		if ((usuario.getTipoUsuario().getId().equals((long) 8)) && status.contains((long) 7)) {
			prelacione = prelacionRepository.findAllByStatusIdAndConsecutivoIsNotNullOrderByConsecutivoAsc(status,
					usuario.getOficina().getId(), new PageRequest(0, 50));
		} else {
			prelacione = prelacionRepository.findAllByStatusIdAndConsecutivoIsNotNullOrderByConsecutivoAsc(status,
					usuario.getOficina().getId());
		}

		prelacione = usuarioDef(prelacione);

		System.out.println("Prelaciones.size()  : " + prelacione.size());

		return prelacione;
	} 
	
	public List<Prelacion> findAllByStatus(Status status){

		List<Prelacion> prelaciones = new ArrayList<Prelacion>();

		prelaciones = prelacionRepository.findAllByStatus(status);

		System.out.println("Prelaciones  : " + prelaciones.size());

		return prelaciones;
	} 


	public List<Prelacion> findAllByConsecutivoAndStatus(int consecutivo,Long status) {
		Status sts = new Status();
		sts.setId(status);
		return prelacionRepository.findAllByStatusAndConsecutivo(sts,consecutivo);
	}
	
	public Prelacion findByConsecutivo(int consecutivo,int anio) {
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Prelacion prelacion = new Prelacion();
		// TODO Auto-generated method stub
		try {
			log.debug("aqui pasa 7472B \n consecutivo "+consecutivo+"\n oficina"+usuario.getOficina().getId());
		} catch (Exception e) {
			log.debug("consecutivo u oficina null");
		}
		prelacion = prelacionRepository.findAllByConsecutivoAndOficinaIdAndAnio(consecutivo,usuario.getOficina().getId(),anio);
		if(prelacion != null) {
			if( prelacion.getStatus().getId().equals(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()) &&
					prelacion.getOficina().getReq_digitalizar() == true && 
					prelacion.getEs_digitalizado() == false ) {
				prelacion.getStatus().setNombre("ASIGNADO A DIGITALIZADOR");
			}
		}

		log.debug("Usuario solicitante: {}", prelacion.getUsuarioSolicitan().getNombreCompleto() + prelacion.getUsuarioSolicitan());

		return prelacion;
	}

	public Prelacion findByReferenciaBancaria(String referencia,int anio) {
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Prelacion prelacion = new Prelacion();
		// TODO Auto-generated method stub
		try {
			log.debug("aqui pasa 7472B \n referencia "+referencia+"\n oficina"+usuario.getOficina().getId());
		} catch (Exception e) {
			log.debug("consecutivo u oficina null");
		}
		prelacion = prelacionRepository.findAllByOficinaIdAndAnioAndRecibosParaPrelacions_Referencia(usuario.getOficina().getId(),anio,referencia);
		if(prelacion != null) {
			if( prelacion.getStatus().getId().equals(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()) &&
					prelacion.getOficina().getReq_digitalizar() == true &&
					prelacion.getEs_digitalizado() == false ) {
				prelacion.getStatus().setNombre("ASIGNADO A DIGITALIZADOR");
			}
		}
		return prelacion;
	}
	
public List<Prelacion> findAllUsuarioAndRangoFechasAndStatus(Long idUsuario,Long status,Date fechaIni,Date fechaFin){
		
	System.out.println("sOriginal-"+status);
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		List<StatusVis> statusVis = statusVisRepository.findStatusIdByStatusExternoId(status);
		List<Long> statusId = new ArrayList<Long>();
		System.out.println("s-"+statusVis);
	for(StatusVis sts :statusVis) {
		statusId.add(sts.getStatusInterno().getId());
	}
		
		if(status == null || status.equals(100L)) {
			if(fechaIni == null || fechaFin == null) {
				return prelacionRepository.findAllNotario(usuario);
			}else {
				return prelacionRepository.findAllNotario(usuario,fechaIni,fechaFin);
			}
		}else if(fechaIni == null || fechaFin == null) {
			return prelacionRepository.findAllNotario(statusId,usuario);
		}else {
			//statusId.add(4L);
			System.out.println("s-"+statusId+"-u-"+usuario+"-fi-"+fechaIni+"-ff-"+fechaFin);
			System.out.println("prelacionRepository{}-"+prelacionRepository.findAllNotario(statusId,usuario,fechaIni,fechaFin));
			return prelacionRepository.findAllNotario(statusId,usuario,fechaIni,fechaFin);	
		}
		
	}
	
	public List<StatusExterno> findAllStatus() {
		return statusExternoRepository.findAll();
	}
	
	public Page<ConsultaPrelacionVO> findAllByRechazada(
			Pageable pageable,
			Integer consecutivo, String desdeFecha, String hastaFecha, Integer escritura, 
			Long notarioId,
			Long contratanteId,
			Long tipoActoId,
			Long registradorId ) {
		Date fechaDesde = null;
		Date fechaHasta = null;
		
		try {
			if (desdeFecha != null && desdeFecha != "")
				fechaDesde = DateTime.parse(desdeFecha).toDate();
			if (hastaFecha != null && hastaFecha != "")
				fechaHasta = DateTime.parse(hastaFecha).toDate();
		} catch (Exception except) {
			return null;
		}
		
		return prelacionRepository.findAllByRechazadas(
				pageable, consecutivo, fechaDesde, fechaHasta, escritura,
				notarioId, contratanteId, tipoActoId, registradorId);
	}
	
	
	public Page<ConsultaPrelacionVO> findAllByStatus(Long folio, Integer folioPredio, String fechaIngreso, String fechaHasta, String fechaEnvioFirma, String fechaBaja,
			Long solicitante, Long registrador,Long calificador, Pageable pageable, Integer status, Integer tipoUsuarioSeleccionado,
			Long contratante,
			String referencia, Long region, Long area,
			Long coordinador,
			Long acto,
			Long notario,
			String escritura,
			Boolean pantallaCoordinador,Integer prioridad
			) {
		Date fechaI=null, fechaH=null, fechaEF=null, fechaB= null;
	
		try {
			if (fechaIngreso != null && fechaIngreso != "")
				fechaI = DateTime.parse(fechaIngreso).toDate();
			if (fechaHasta != null && fechaHasta != "")
				fechaH = DateTime.parse(fechaHasta).toDate();
			if (fechaEnvioFirma != null && fechaEnvioFirma != "")
				fechaEF = DateTime.parse(fechaEnvioFirma).toDate();
			if (fechaBaja != null && fechaBaja != "")
				fechaB = DateTime.parse(fechaBaja).toDate();
		}
		catch (Exception except) {
			return null;
		}
	
		Usuario usuario = null;
		if (tipoUsuarioSeleccionado != 3)
			usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
	
		solicitante = null;
	
		Constantes.Status maxStatus = this.getMaxStatusPrelacion(tipoUsuarioSeleccionado);
	
		return prelacionRepository.findAllByStatus (usuario,status, folio, folioPredio, fechaI, fechaH, fechaEF, fechaB, solicitante, registrador,calificador,
				pageable,
				contratante,
				referencia, region, area,
				coordinador,
				acto,
				notario,
				escritura,
				pantallaCoordinador,prioridad);
	}

	public Long getDocumentoByPrelacion(Long idPrelacion) {
		
		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);
		List<Acto> actos = actoRepository.findAllByPrelacion(prelacion);
		List<ActoDocumento> actoDocumento = actoDocumentoRepository.getAllByActo(actos);
		
		System.out.println("ID ARCHIVO-------------> " +  actoDocumento.get(0).getDocumento().getArchivo().getId());
		return actoDocumento.get(0).getDocumento().getArchivo().getId();
	}

	public ConsultaPrelacionDetalleTramiteVO findPrelacionFechas(Long prelacionId) {
		ConsultaPrelacionDetalleTramiteVO detalleVO = new ConsultaPrelacionDetalleTramiteVO();
		List<BitacoraDigitalizador> bitacoraDig = new ArrayList<BitacoraDigitalizador>();
		Prelacion prelacion = prelacionRepository.findOne(prelacionId);
		
		detalleVO.setId( prelacion.getId() );
		detalleVO.setConsecutivo( prelacion.getConsecutivo() );
		detalleVO.setFechaRecepcion( prelacion.getFechaIngreso() );
		detalleVO.setPrioridad(prelacion.getPrioridad().getNombre());
		//Bitacora bitacora = bitacoraRepository.findTop1BitacoraByPrelacionIdOrderByIdDesc(prelacion.getId());
		 List<Bitacora>listBitacora = bitacoraRepository.findAllBitacotoraByPrelacionId(prelacionId);
		 
		
		 for(Bitacora b:listBitacora) {
			 
			 if(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()==b.getStatus().getId()) {
				 detalleVO.setFechaEnvioAnalisis(b.getFecha());
			 }else if(Constantes.Status.ASIGNADO_A_CALIFICADOR.getIdStatus() == b.getStatus().getId() ) {
					detalleVO.setFechaEnvioCalificador( b.getFecha() );
				} else if(Constantes.Status.ASIGNADO_A_COORDINADOR.getIdStatus() == b.getStatus().getId() ) {
					detalleVO.setFechaEnvioCoordinador( b.getFecha() );
				} else if( Constantes.Status.LISTO_PARA_ENTREGARSE.getIdStatus() == b.getStatus().getId() ) {
					detalleVO.setFechaFirmaYEnvioUsuario( b.getFecha() );
				} else if(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()  == b.getStatus().getId() ) {
					detalleVO.setFechaImpresionUsuario( b.getFecha() );
				}
		 }
			
		 bitacoraDig = bitacoraDigitalizadorService.findBitacoraDByprelacion(prelacionId);
		 if(bitacoraDig != null) {
			 if(bitacoraDig.size()>0) {
				 BitacoraDigitalizador bdig= bitacoraDig.get(0);
				 System.out.println(bdig.getId()+" -- "+ bdig.getFecha());
				 detalleVO.setFechaEnvioAnalisis(bdig.getFecha());
			 }
		 }
		return detalleVO;
	}

	public ConsultaPrelacionDetalleAtendioVO findPrelacionAtendio(Long prelacionId) {
		ConsultaPrelacionDetalleAtendioVO detalleVO = new ConsultaPrelacionDetalleAtendioVO();

		Prelacion prelacion = prelacionRepository.findOne(prelacionId);

		detalleVO.setId( prelacion.getId() );
		detalleVO.setUsuarioAnalista( prelacion.getUsuarioAnalista() );
		//detalleVO.setUsuarioCalificador( prelacion.getUsuarioCalificador() );
		detalleVO.setUsuarioCoordinador( prelacion.getUsuarioCoordinador() );
		detalleVO.setUsuarioVentanilla(prelacion.getUsuarioVentanilla());

		return detalleVO;

	}

//Funcion para consulta detallefolio//
	public ConsultaPrelacionDetalleFoliosVO findPrelacionFolios(Long prelacionId,boolean reingreso) {
		ConsultaPrelacionDetalleFoliosVO detalleVO = new ConsultaPrelacionDetalleFoliosVO();

		Prelacion prelacion = prelacionRepository.findOne(prelacionId);

		detalleVO.setId( prelacion.getId() );

		StringBuilder sb = new StringBuilder();
		Set<PrelacionPredio> prelacionPredios = prelacion.getPrelacionPrediosParaPrelacions().stream().filter( x-> !x.getEstatus().equals(0)).collect(Collectors.toSet());
		Predio predio = null;
		PersonaJuridica personaJuridica = null;
		for( PrelacionPredio pp : prelacionPredios ) {

			if(pp.getTipoFolio()!=null){
				switch(ETipoFolio.fromInt(pp.getTipoFolio().getId().intValue())){
					case PERSONAS_JURIDICAS:
						personaJuridica = pp.getPersonaJuridica();
						if( personaJuridica != null ) {
							sb.append( personaJuridica.getNoFolio()+", " );
						}
					break;
					case PREDIO:  
						predio = pp.getPredio();
						if( predio != null ) {
							sb.append( predio.getNoFolio()+", " );
						}
					break;
				}
			}

			if(detalleVO.getEstatus()== "0") {
				System.out.println("No se puede mostrar  el folio"+predio.getNoFolio()+ "por que no existe");

			}
		}

	    if( sb.length() > 2 ) {
	    	sb.delete( sb.length()-2, sb.length() );
	    }

		detalleVO.setFolios( sb.toString() );

		sb = new StringBuilder();
		Set<Acto> actos = prelacion.getActosParaPrelacions();
		Set<ActoDocumento> actoDocumentos = null;
		Documento documento = null;
		TipoDocumento tipoDocumento = null;
		 List<Long> tiposDocumentosId = new ArrayList<Long>();
		for( Acto a : actos ) {
			actoDocumentos = a.getActoDocumentosParaActos();

			Optional<ActoDocumento> act = actoDocumentos.stream()
					.collect( Collectors.maxBy(Comparator.comparing(ActoDocumento::getVersion) ) );
			if( act.isPresent() ) {//OBTIENE DOCUMENTO FUNDATORIO POR ACTO
				//sb.append( act.get().getDocumento().getTipoDocumento().getNombre()+", " );
				tiposDocumentosId.add(act.get().getDocumento().getTipoDocumento().getId());
			}

			/*
			for( ActoDocumento ad : actoDocumentos ) {
				documento = ad.getDocumento();
				if( documento != null ) {
					tipoDocumento = documento.getTipoDocumento();
					if( tipoDocumento != null ) {
						sb.append( tipoDocumento.getNombre()+", " );
					}
				}
			}
			*/
		}
		sb.append(getTiposDocumento(tiposDocumentosId));
		
	    if( sb.length() > 2 ) {
	    	sb.delete( sb.length()-2, sb.length() );
	    }

	    detalleVO.setDocumentoFundatorio( sb.toString() );

	    //TODO
	    //detalleVO.setOrigenDocumentoFundatorio( "" );

	    sb = new StringBuilder();
	    TipoActo tipoActo = null;
	    List<Long> tiposActosId = new ArrayList<Long>();

	    	for( Acto a : actos ) {
	    		    // SI ES REINGRESO SOLO TOMA ACTOS RECHAZADOS
	    		    if ( reingreso && a.getStatusActo().getId() != Constantes.StatusActo.RECHAZADO.getIdStatusActo() ) continue;
		    		if (!reingreso && a.getStatusActo().getId() == Constantes.StatusActo.RECHAZADO.getIdStatusActo() ) continue;
		            if ( a.getStatusActo().getId() == Constantes.StatusActo.INVALIDO.getIdStatusActo() ) continue;
		            if ( a.getVf() != null && a.getVf()) continue;


		    	tipoActo = a.getTipoActo();
		    	tiposActosId.add(a.getTipoActo().getId());
//		    	if( tipoActo != null ) {
//		    		sb.append( tipoActo.getNombre()+", " );
//		    	}
		    }
	    	sb.append(getTiposActos(tiposActosId));
	    	//Traerme Lista de Actos 

	    if( sb.length() > 2 ) {
	    	sb.delete( sb.length()-2, sb.length() );
	    }

	    detalleVO.setActoServicioSolicitado( sb.toString() );

	    detalleVO.setEstatus( prelacion.getStatus().getNombre() );

		return detalleVO;
	}
	
	
	public String getTiposActos(List<Long> tiposIds) {
	   StringBuilder sb = new StringBuilder();
	   List<TipoActo> tiposActos = tipoActoRepository.findAllByIdIn(tiposIds);
	   
	   for(TipoActo tipoActo:tiposActos) {
		   	if( tipoActo != null ) {
				sb.append( tipoActo.getNombre()+", " );
			}
	   }
		return sb.toString();
	}

	public String getTiposDocumento(List<Long> tiposIds) {
		   StringBuilder sb = new StringBuilder();
		   List<TipoDocumento> tiposDocumentos = tipoDocumentoRepository.findAllByIdIn(tiposIds);
		   
		   for(TipoDocumento tipoDocumento:tiposDocumentos) {
			   	if( tipoDocumento != null ) {
					sb.append( tipoDocumento.getNombre()+", " );
				}
		   }
			return sb.toString();
		}
	
	public List<ConsultaPrelacionDetallePagoVO> findPrelacionPago(Long prelacionId) {
		List<ConsultaPrelacionDetallePagoVO> detalleVOs = new ArrayList<ConsultaPrelacionDetallePagoVO>();

		Prelacion prelacion = prelacionRepository.findOne(prelacionId);
		
		Set<Recibo> recibos = prelacion.getRecibosParaPrelacions();
		ConsultaPrelacionDetallePagoVO detalleVO = null;
		Set<ReciboConcepto> reciboConceptos = null;
		StringBuilder sb = null;
		ConceptoPago conceptoPago = null;
		for( Recibo r : recibos ) {
			detalleVO = new ConsultaPrelacionDetallePagoVO();
			detalleVO.setId( prelacion.getId() );
			detalleVO.setNumRecibo( r.getNoRecibo() );

			reciboConceptos = r.getReciboConceptosParaRecibos();
			sb = new StringBuilder();
			for( ReciboConcepto rb : reciboConceptos ) {
				conceptoPago = rb.getConceptoPago();
				if( conceptoPago != null ) {
					sb.append( conceptoPago.getNombre()+", " );
				}
			}
		    if( sb.length() > 2 ) {
		    	sb.delete( sb.length()-2, sb.length() );
		    }
		    detalleVO.setConcepto( sb.toString() );
		    detalleVO.setReferencia(r.getReferencia());
		    detalleVO.setCuenta( r.getCuenta() );
		    detalleVO.setMonto( r.getMonto() );
		    detalleVO.setFechaPago( r.getFecha() );
		    detalleVOs.add( detalleVO );
		}
		
		
		return detalleVOs;
	}
	
	
public void prelacionesAtrasadas() {
		
		List<Prelacion> atrasadas = new ArrayList<Prelacion>();
		List<Long> status = new ArrayList<Long>();
		
		status.add(6L);
		status.add(14L);
		atrasadas = prelacionRepository.findPrelacionesAtrasadas((double) 3,status);
		notificarAtrasos(atrasadas,(double) 3);
		atrasadas.clear();
		atrasadas = prelacionRepository.findPrelacionesAtrasadasPost((double) 9,status);
		notificarAtrasos(atrasadas,(double) 9);
		atrasadas.clear();
		
	}
	
	public void notificarAtrasos(List<Prelacion> atrasadas, Double dias) {

		System.out.println("Notificados");
		
		String msg = dias == 9 ? " dias o mas de inactividad favor de atenderla" : " dias de inactividad favor de atenderla"; 
		
		for (Prelacion prelacion : atrasadas) {

			Notificacion notificacion = new Notificacion();
			notificacion.setFechaEnvio(new Date());
			notificacion.setNotificacion("La prelacion " + prelacion.getConsecutivo() + "  lleva "+ dias.intValue() + msg);
			StatusNotificacion statusNotificacion = new StatusNotificacion();
			statusNotificacion.setId(1L);
			notificacion.setStatusNotificacion(statusNotificacion);
			TipoNotificacion tipoNotificacion = new TipoNotificacion();
			switch ( dias.intValue()) {
			case 3:
				tipoNotificacion.setId(1L);
				break;
            case 9:
            	tipoNotificacion.setId(3L);
				break;
			}
			notificacion.setTipoNotificacion(tipoNotificacion);
			notificacion.setUsuarioDestinatario(prelacion.getUsuarioCoordinador());

			notificacionRepository.save(notificacion);

		}

	}

	public Prelacion restaurarFromEspecial(Long vo) throws Exception{
		Prelacion prelacion = prelacionRepository.findOne(vo);
		prelacion.status(prelacion.getStatusAnterior());
		prelacionRepository.saveAndFlush(prelacion);
		return prelacion;
	}
	
	/* Busca todas las prelaciones que cumplan con los siguientes criterios:
	 * que tengan un consecutivo menor al de la prelacion otorgada
	 * que estén en medio de algún trámite (status de la prelacion)
	 * y que contengan uno o más predios iguales a la prelación otorgada
	 */
	public List<Prelacion> isPrelacionAnalizable(Prelacion prelacion) {
		List<Long> prediosIds = this.prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacion.getId())
				.stream()
				.map((x) -> {
					if (x.getPredio() != null) {
						return x.getPredio().getId();	
					}
					else{
						return -1L;
					}
				})
				.collect(Collectors.toList());
		List<Long> statusesIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 11L, 14L); 
		List<Prelacion> prelaciones =  this.prelacionPredioRepository
				.findAllByPrelacionAnioLessThanAndPrelacionStatusIdInAndPredioIdIn(prelacion.getAnio(), statusesIds, prediosIds)
				.stream()
				.map(x-> x.getPrelacion() )
				.collect(Collectors.toList());
		
		prelaciones.addAll(this.prelacionPredioRepository
				.findAllByPrelacionConsecutivoLessThanAndPrelacionAnioAndPrelacionStatusIdInAndPredioIdIn(prelacion.getConsecutivo(),prelacion.getAnio(), statusesIds, prediosIds)
				.stream()
				.map(x-> x.getPrelacion() )
				.collect(Collectors.toList())
				);
		
		//if(prelaciones!=null && prelaciones.size()>0)
			//prelaciones  = prelaciones.stream().filter(x->x.getConsecutivo()<prelacion.getConsecutivo()).collect(Collectors.toList());
		
		return prelaciones;
	}
	
	public List<Prelacion> isPrelacionCapturableAnte(Prelacion prelacion) {
		
		List<Prelacion> prelaciones = new ArrayList<Prelacion>();
		List<PrelacionAnte> antecendentes =  new ArrayList<PrelacionAnte>();
		List<PrelacionAnte> prelacionesAnte = prelacionAnteService.findPrelacionAnteByPrelacion(prelacion.getId());
		List<Long> statusesIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 11L, 14L); 
		
		System.out.println("Prelacion Ante -> " + prelacionesAnte.size());
		
		for(PrelacionAnte preAnte : prelacionesAnte) {
			antecendentes.addAll(prelacionAnteRepository.findByLibroAndLibroBisAndSeccionIdAndOficinaIdAndAnioAndVolumenAndDocumentoAndTipoFolioIdAndPrelacionStatusIdInAndPrelacionUsuarioCapValIsNotNullAndPrelacionAnioLessThan(
					preAnte.getLibro(), preAnte.getLibroBis(),preAnte.getSeccion()!=null ?  preAnte.getSeccion().getId() : null, preAnte.getOficina()!=null ?  preAnte.getOficina().getId() : null, preAnte.getAnio(), preAnte.getVolumen(), preAnte.getDocumento(),preAnte.getTipoFolio() != null ? preAnte.getTipoFolio().getId() : null,statusesIds,
							preAnte.getPrelacion().getAnio()
					));
			
			antecendentes.addAll(prelacionAnteRepository.findByLibroAndLibroBisAndSeccionIdAndOficinaIdAndAnioAndVolumenAndDocumentoAndTipoFolioIdAndPrelacionStatusIdInAndPrelacionUsuarioCapValIsNotNullAndPrelacionConsecutivoLessThanAndPrelacionAnio
					(preAnte.getLibro(), preAnte.getLibroBis(),preAnte.getSeccion()!=null ?  preAnte.getSeccion().getId() : null, preAnte.getOficina()!=null ?  preAnte.getOficina().getId() : null, preAnte.getAnio(), preAnte.getVolumen(), preAnte.getDocumento(),preAnte.getTipoFolio() != null ? preAnte.getTipoFolio().getId() : null,statusesIds,preAnte.getPrelacion().getConsecutivo(),
							preAnte.getPrelacion().getAnio()
							));
		}
		
		System.out.println("Prelacion Ante 2-> " + antecendentes.size());
		
		
		for(PrelacionAnte ante :antecendentes) {
			prelaciones.add(ante.getPrelacion());			
		}
	
		
		return prelaciones;
	}
	
	public List<Prelacion> isFolioOcupadoPrelacion(Integer folio, Integer tipoFolio){
		List<Prelacion> resultado = new ArrayList<Prelacion>();

		try {
			switch (tipoFolio) {
			case 1:
				PersonaJuridica persona = personaJuridicaService.findByNumFolio(folio);
				List<PrelacionPredio> prelacionPredio1 = prelacionPredioRepository
						.findByFolioLibrePersonaJuridica(persona.getId());

				if (prelacionPredio1.size() > 0) {
					resultado = this.prelacionPredioToPrelacion(prelacionPredio1);
				}
				break;
			case 2:
				FolioSeccionAuxiliar auxiliar = folioSeccionAuxiliarRepository.findOneByNoFolio(folio);
				List<PrelacionPredio> prelacionPredio2 = prelacionPredioRepository
						.findByFolioLibreAuxiliar(auxiliar.getId());

				if (prelacionPredio2.size() > 0) {
					resultado =  this.prelacionPredioToPrelacion(prelacionPredio2);
				}
				break;
			case 3:
				Mueble mueble = muebleRepository.findByNoFolio(folio);
				List<PrelacionPredio> prelacionPredio3 = prelacionPredioRepository
						.findByFolioLibreMueble(mueble.getId());

				if (prelacionPredio3.size() > 0) {
					resultado =  this.prelacionPredioToPrelacion(prelacionPredio3);
				}
				break;
			case 4:
				Predio predio = predioService.findPredioByNoFolio(folio);
				List<PrelacionPredio> prelacionPredio4 = prelacionPredioRepository
						.findByFolioLibrePredio(predio.getId());

				if (prelacionPredio4.size() > 0) {
					resultado =  this.prelacionPredioToPrelacion(prelacionPredio4);
				}

				break;

			}
		} catch (Exception e) {
			
		}

		return resultado;
	}
	
	private List<Prelacion> prelacionPredioToPrelacion(List<PrelacionPredio> prelacionPredio){
		List<Prelacion> prelaciones = new ArrayList<Prelacion>();
		prelacionPredio.forEach(x->{
			prelaciones.add(x.getPrelacion());
		});
		return prelaciones;
	}

	public boolean isFolioOcupado(Integer folio, Integer tipoFolio) {
		
		boolean resultado = false;
		
		try {
			switch (tipoFolio) {
			case 1:
				PersonaJuridica persona = personaJuridicaService.findByNumFolio(folio);
				List<PrelacionPredio> prelacionPredio1 =  prelacionPredioRepository.findByFolioLibrePersonaJuridica(persona.getId());
				
				if (prelacionPredio1.size() > 0) {
					resultado = true;
				}
			break;
			case 2:
				FolioSeccionAuxiliar auxiliar = folioSeccionAuxiliarRepository.findOneByNoFolio(folio);
				List<PrelacionPredio> prelacionPredio2 =  prelacionPredioRepository.findByFolioLibreAuxiliar(auxiliar.getId());
				
				if (prelacionPredio2.size() > 0) {
					resultado = true;
				}	
			break;
			case 3:
				Mueble mueble = muebleRepository.findByNoFolio(folio);
				List<PrelacionPredio> prelacionPredio3 =  prelacionPredioRepository.findByFolioLibreMueble(mueble.getId());
				
				if (prelacionPredio3.size() > 0) {
					resultado = true;
				}
			break;
			case 4:
				Predio predio = predioService.findPredioByNoFolio(folio);
				List<PrelacionPredio> prelacionPredio4 =  prelacionPredioRepository.findByFolioLibrePredio(predio.getId());
				
				if (prelacionPredio4.size() > 0) {
					resultado = true;
				}
				
				
			break;
			
			}
		} catch (Exception e) {
			resultado =  true;
		}
		
		
		
		

		return resultado;
	}

	
	public Boolean isPrelacionFirmado(Prelacion prelacion) {
		if (!isEmpty ( prelacion.getEstampilla()) && !isEmpty(prelacion.getPkcs7()) && !isEmpty(prelacion.getSecuencia()) )
			return true;

		return false;

	}

	public void actualizarAnteriorAbril(Prelacion prelacion, Boolean aa81) {
		if (!isEmpty(prelacion)) {
			prelacion.setAnteriorAbril81(aa81);
			prelacionRepository.saveAndFlush(prelacion);
		}
	}

	public void actualizarPrimerRegistro(Prelacion prelacion, Boolean pr) {
		if (!isEmpty(prelacion)) {
			prelacion.setPrimerRegistro(pr);
			prelacionRepository.saveAndFlush(prelacion);
		}
	}
	public void actualizarIndEntrega(Prelacion prelacion, Boolean pr) {
		System.out.println("actualizarIndEntrega");
		if (!isEmpty(prelacion)) {
			prelacion.setIndEntrega(pr);
			prelacionRepository.saveAndFlush(prelacion);
		}
	}
	
	
	public PrelacionVO findPrelacionVo(Long prelacionId, Long actoId) {
		Prelacion prelacion = prelacionRepository.findOne(prelacionId);
		PrelacionVO prelVo = new PrelacionVO();
		prelVo.setId(prelacionId);
		prelVo.setSolicitante(prelacion.getUsuarioSolicitan());
		prelVo.setTipoEntrega(prelacion.getTipoEntrega() == 1 ? false : true);
		Acto[] Actos;
		
		List<PrelacionPredio> prelacionPredios = new ArrayList<PrelacionPredio>();
		List<PredioVO> predios = new ArrayList<PredioVO>();
		List<Mueble> muebles  = new ArrayList<Mueble>();
		List<PersonaJuridica> personasJuricas = new ArrayList<PersonaJuridica>();
		
		
		List <ActoPredio> listActPre= new ArrayList<ActoPredio>();
		List <ActoFolioRecibo> listActFolioRecibo = new ArrayList<ActoFolioRecibo>();
		
		List <Recibo> Recibos = new ArrayList<Recibo>();
		List <ReciboVO> recibos = new ArrayList<ReciboVO>(); 
		ReciboVO rvo[] = new ReciboVO[Recibos.size()];
		
		Actos = actoRepository.findActosByPrelacionId(prelacionId);
		prelVo.setActos(Actos);
		List<Acto> listA = Arrays.asList(Actos);
		listActPre= actoPredioRepository.findAllByListActo(listA);
		
		prelacionPredios =  prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionId);
		
		prelacionPredios.forEach(x->{
			switch(x.getTipoFolio().getId().intValue())
			{
			   case 4:
				     PredioVO predioVo = new PredioVO();
				     predioVo.setIdPrelacionPredio(x.getId());
				     predioVo.setId(x.getPredio().getId());
				     predioVo.setNoLote(x.getPredio().getNoLote());
				     predioVo.setManzana(x.getPredio().getManzana());
				     if(x.getPredio().getSuperficieM2()!=null) {
				    	 predioVo.setSuperficie(x.getPredio().getSuperficieM2().floatValue());
				     }else if(x.getPredio().getSuperficie()!=null) {
				    	 predioVo.setSuperficie(Float.parseFloat(x.getPredio().getSuperficie()));
				     }				     
				     predioVo.setClaveCatastral(x.getPredio().getClaveCatastral());
				     predioVo.setClaveCatastralEstandard(x.getPredio().getClaveCatastralEstandard());
				     predioVo.setNivel(x.getPredio().getNivel());
				     predioVo.setEdificio(x.getPredio().getEdificio());
				  //   predioVo.setFraccion(Integer.parseInt(x.getPredio().getFraccion()));
				     Optional<PredioAnte> predioAnte = predioAnteRepository.findByPredioId(x.getPredio().getId());
				     if(predioAnte != null && predioAnte.isPresent())
				        predioVo.setPredioAntesParaPredio(predioAnte.get());
				     predioVo.setUsoSuelo(x.getPredio().getUsoSuelo());
				     predioVo.setUnidadMedida(x.getPredio().getUnidadMedida());
				     predioVo.setVialidad(x.getPredio().getVialidad());
				     predioVo.setAsentamiento(x.getPredio().getAsentamiento());
				     predioVo.setZona(x.getPredio().getZona());
				     predioVo.setNoFolio(x.getPredio().getNoFolio());
				     predioVo.setStatusActo(x.getPredio().getStatusActo());
				     predioVo.setCaratulaActualizada(x.getPredio().getCaratulaActualizada());
				     predioVo.setPrelacion(x.getPrelacion());
				     predioVo.setBloqueado(x.getPredio().getBloqueado());
				     predioVo.setOficina(x.getPredio().getOficina());
				     predioVo.setProcedeDeFolio(x.getPredio().getProcedeDeFolio());
				     predioVo.setPrimerRegistro(x.getPredio().getPrimerRegistro());
				     
				     predios.add(predioVo);
				break;
				
			   case 3:
				   muebles.add(x.getMueble());
			   break;
			   case 2:
				   personasJuricas.add(x.getPersonaJuridica());
			   break;
			   
			}
		});
		
		PredioVO[] prediosArray = new PredioVO[predios.size()];
		prediosArray =  predios.toArray(prediosArray);
		if(prediosArray.length > 0)
		prelVo.setPredios(prediosArray);
		
		Mueble[] mueblesArray = new Mueble[muebles.size()];
		mueblesArray =  muebles.toArray(mueblesArray);
		if(mueblesArray.length>0)
		prelVo.setMuebles(mueblesArray);
		
		PersonaJuridica[] personasJuridicasArray =  new PersonaJuridica[personasJuricas.size()];
		personasJuridicasArray =  personasJuricas.toArray(personasJuridicasArray);
		if(personasJuridicasArray.length > 0)
		prelVo.setPersonasJuridicas(personasJuridicasArray);
		
		
		if(listActPre.size()>0) {			
			Recibos = reciboRepository.findAllByListActoPredio(listActPre);			
			if(Recibos.size()>0) {
				for( Recibo recibo : Recibos){
					ReciboVO reciboVo = new ReciboVO();
					reciboVo.setId(recibo.getId());
					//reciboVo.setNoRecibo(recibo.getNoRecibo());
					reciboVo.setReferencia(recibo.getReferencia());
					reciboVo.setMonto(recibo.getMonto());
					reciboVo.setFecha(recibo.getFecha());
					reciboVo.setPrelacion(recibo.getPrelacion());			
					reciboVo.setActos(Actos);
					recibos.add(reciboVo);
				}
				if(Recibos.size()>0) {
					prelVo.setContienePago(true);
				}
				prelVo.setRecibos(recibos.toArray(rvo));
			}else {
				if(prelacion.getRecibosParaPrelacions().size()>0) {
					Iterator<Recibo> setRecibos = prelacion.getRecibosParaPrelacions().iterator();				
				
					while(setRecibos.hasNext()){
						Recibo recibo = setRecibos.next();
						ReciboVO reciboVo = new ReciboVO();
						reciboVo.setId(recibo.getId());
						reciboVo.setReferencia(recibo.getReferencia());
						reciboVo.setMonto(recibo.getMonto());
						reciboVo.setFecha(recibo.getFecha());
						reciboVo.setPrelacion(recibo.getPrelacion());			
						reciboVo.setActos(Actos);
						recibos.add(reciboVo);
					}
					prelVo.setContienePago(true);
					prelVo.setRecibos(recibos.toArray(rvo));
				}else {
					return prelVo;
				}
			}
			
		}else {
			return prelVo;
		}
		List <ActoRequisito> listActReq = new ArrayList<ActoRequisito>();
		
		listActReq= actoRequisitoRepository.findByActoId(actoId);
		
		List<RequisitoVO> listReqVO = new ArrayList<RequisitoVO>();
		RequisitoVO[] requisitoVO = new RequisitoVO[listActReq.size()];
		ArrayList <Archivo> listArchivos = new ArrayList<Archivo>();
		
	    ActoDocumento actoDocumento = new ActoDocumento();
		Optional<ActoDocumento> adoc = actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(actoId);
		actoDocumento = adoc.isPresent()  ? adoc.get() : null;
		
		for(ActoRequisito actReq: listActReq) {
			RequisitoVO reqVO = new RequisitoVO();
			listArchivos.add(actReq.getArchivo());
			reqVO.setActo(actReq.getActo());
			reqVO.setTipoDocumento(actoDocumento.getDocumento().getTipoDocumento());
			reqVO.setIdDocumento(actoDocumento.getDocumento().getId());
			reqVO.setRequisito(actReq.getRequisito());
			reqVO.setArchivos(listArchivos);
			reqVO.setIdActoRequisito(actReq.getId());
			reqVO.setIdActoDocumento(actoDocumento.getId());
			listArchivos.clear();
			listReqVO.add(reqVO);
		}
		prelVo.setRequisitos(listReqVO.toArray(requisitoVO));
		prelVo.setId(prelacionId);
				
		
	//	System.out.println("Longitud : N  " + Recibos.size());
	
		return prelVo;
	}
	
	
	//VALIDA  DIAS HABILES PARA EL REINGRESO
	public Boolean  validaReingreso(Long prelacionId) throws Exception
	{
		Prelacion prelacion =  prelacionRepository.findOne(prelacionId);
		List<Acto> actos =     actoRepository.findActosByPrelacionIdAndRechazados(prelacion.getId());
		List<Date> diasInhabil = new ArrayList<Date>();
		List<InhabilLocal> inhabil =  inhabilLocalRepository.findByOficinaId(prelacion.getOficina().getId());
		inhabil.forEach( x -> {
			diasInhabil.add(x.getDiaInhabil().getFechaDiaInhabil());
		});
		
		Date fechaRechazo = null;
		
		if(actos!= null &&  actos.size() > 0)
			 fechaRechazo = actos.get(0).getFechaRechazo();
		else 
			 throw new Exception("La prelación no tiene actos rechazados");
		
		
		int dias = UtilFecha.diasHabiles(fechaRechazo, new Date(), diasInhabil);
		log.info(" DIAS: " + dias);
		if(dias <= 10) 
		{
			return true;
		}else 
		{
			return  false;
		}
	}
	
	@Transactional
	public Boolean reingreso(ReingresoVO reingreso)
	{	   
	   Prelacion prelacion =  prelacionRepository.findOne(reingreso.getPrelacionId());
	   Suspension suspension = suspencionRepository.findOneByestatusSuspensionAndPrelacion(2L, prelacion);

	   BitacoraReingreso bitacoraReingreso= new BitacoraReingreso();
	   Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
	   Bitacora bitacora = bitacoraRepository.findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(prelacion.getId(),prelacion.getStatus().getId());

	   prelacion.setStatus(statusRepository.findOne(Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()));
	   
	   if(prelacion.getReingresado() != null) {
		   prelacion.setReingresado(prelacion.getReingresado() + 1);
	   }else {
		   prelacion.setReingresado(1);
	   }
	   if(prelacion.getSubindice() != null) {
		   int inum = prelacion.getSubindice().intValue();
		   inum= inum+1;
		   prelacion.setSubindice(Long.valueOf(inum));  
	   }else {
		   prelacion.setSubindice(1L);
	   }
	   prelacion.setFechaReingreso(new Date());
	   	   
	   prelacionRepository.saveAndFlush(prelacion);

	   if(bitacora!=null){
		bitacoraReingreso.setMotivo(bitacora.getMotivo());
		bitacoraReingreso.setTipoRechazo(bitacora.getTipoRechazo());
		bitacoraReingreso.setObservaciones(bitacora.getObservaciones()!=null?bitacora.getObservaciones():"");
	   } 
	   bitacoraReingreso.setFecha(new Date());
	   bitacoraReingreso.setUsuario(usuario);
	   bitacoraReingreso.setPrelacion(prelacion);
	   if(prelacion.getSubindice() != null) {
		   bitacoraReingreso.setSubindice(prelacion.getSubindice());
	   }
	   bitacoraReingresoRepository.save(bitacoraReingreso);

	   //Validar si es oficina migrada pasar a bandeja DIGITALIZACION
	   if(prelacion.getOficina().getReq_digitalizar()!=null){
		   if(prelacion.getOficina().getReq_digitalizar()==true){
			changePrelacionDigitalizado(prelacion.getId());
		   }
	   }

	   suspension.setEstatusSuspension(1L);
	   suspencionRepository.save(suspension);
	   
	   List<Acto> actos   =  actoRepository.findActosByPrelacionIdAndRechazados(prelacion.getId());
	   actos.forEach(x->{
		   x.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		   x.setModificable(true);
		   actoRepository.saveAndFlush(x);
		   List<ActoPredio> actoPredio = actoPredioRepository.findByActoId(x.getId());
			actoPredio.forEach(ap -> {
				ap.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
				actoPredioRepository.saveAndFlush(ap);
			});
	   });

	   if(reingreso.getMotivo() != null)
	       bitacoraRepository.saveAndFlush(createBitacoraCompleta(prelacion,reingreso.getMotivo(),null, reingreso.getMotivo().getTipoMotivo(),null, reingreso.getFundamento()));
	   else
		   bitacoraRepository.saveAndFlush(createBitacoraCompleta(prelacion,null,null,tipoMotivoRepository.findOneByNombre("REINGRESO"),null, reingreso.getFundamento()));

	  return true;
	}
	//JADV-SUSPENSION
	public Boolean SuspenderPrelacion(Long id, Long tipoUsuarioId, Long motivoId, Long fundamentoId ,String observaciones) {
		Prelacion prelacion = prelacionRepository.findOne(id);				
		Motivo motivo = null;
		TipoRechazo tRechazo = null;
		Status status = new Status();
		
		try {
		
			if(tipoUsuarioId == 1) {  // BANDEJA ANALISTA			
				status.setId((Constantes.Status.SUSPENDIDA_CALIFICADOR).getIdStatus());				
				prelacionRepository.save(prelacion);			
			} else if(tipoUsuarioId == 2) {  // BANDEJA CALIFICADOR
				status.setId((Constantes.Status.SUSPENDIDA_CALIFICADOR).getIdStatus());
			}
			
			motivo = motivoRepository.findOne(motivoId);
			if (motivo == null)
				return null;

			tRechazo=tipoRechazoRepository.findById(fundamentoId);
			if (tRechazo == null)
				return null;

			
			if(tipoUsuarioId != 3) { 
				prelacion.setStatus(status);			
				bitacoraRepository.save(createBitacoraCompleta(prelacion,motivo,tRechazo,null,null,observaciones));	
			}
			
			Suspension suspensionExistente =   suspencionRepository.findOneByestatusSuspensionAndPrelacion(2L, prelacion);

			
				
			if(tipoUsuarioId == 3 && suspensionExistente == null) {  // BANDEJA REGISTRADOR
				Suspension suspension = new Suspension();
				Calendar fechaActual=Calendar.getInstance();
				fechaActual.setTimeZone(TimeZone.getDefault());
				if(fechaActual.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
					fechaActual.add(Calendar.DATE, 3);	
				} else if (fechaActual.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
					fechaActual.add(Calendar.DATE, 2);
				} else {
					fechaActual.add(Calendar.DATE, 1);
				}				
				fechaActual.set(Calendar.HOUR_OF_DAY, 0);
				fechaActual.set(Calendar.MINUTE, 0);
				fechaActual.set(Calendar.SECOND, 0);
				fechaActual.set(Calendar.MILLISECOND, 0);				
				
				
				suspension.setPrelacion(prelacion);
				suspension.setEstatusSuspension(2L);
				suspension.setFechaSuspension(fechaActual.getTime());
				suspension.setFechaCalculo(fechaActual.getTime());				
				suspension.setPorAutoridad(false);
				suspension.setDiasAdicionales(0L);
				suspencionRepository.save(suspension);
				
				//24-JULIO-2019 IHM Se comenta el envio de correo de la suspension
				//boletaRegistralService.enviaNotificacionSuspencion(prelacion);
				//29-Julio-2019 IHM se comenta
				//AplicarEstatusSuspensivoPrelacionesExistentes(prelacion);
				
			} 
			
			if(tipoUsuarioId != 3) { 
				Prelacion resultado = prelacionRepository.saveAndFlush(prelacion);
			}
			
			
			
			Context context = new Context();
			/*context.setVariable("nombre", direccion.getNombre());
			context.setVariable("puesto", direccion.getPuesto());*/
			context.setVariable("oficina", prelacion.getOficina().getNombre());
			return true;
		}
		catch (Exception except) {			
			return false;
		}
	}
	
	public boolean AgregarDiasAdicionalesSuspension (Long prelacionId, String observaciones) {
		
		Calendar fechaActual=Calendar.getInstance();
		fechaActual.set(Calendar.HOUR_OF_DAY, 0);
		fechaActual.set(Calendar.MINUTE, 0);
		fechaActual.set(Calendar.SECOND, 0);
		fechaActual.set(Calendar.MILLISECOND, 0);
		
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		try {
			
			Prelacion prelacion = prelacionRepository.findOne(prelacionId);
			Suspension suspension =   suspencionRepository.findOneAllByPrelacionOrderByIdDesc(prelacion);
			
			Long dias_adicionales = Long.valueOf( parametroRepository.findByCve("DIAS_SUSPENSION").getValor());
			
			suspension.setFechaCalculo(fechaActual.getTime());
			
			suspencionRepository.save(suspension);
			
			DiasSolventacionSuspension bitacora = new DiasSolventacionSuspension();
			bitacora.setSuspension(suspension);
			bitacora.setFechaModificacion(fechaActual.getTime());
			bitacora.setDiasAdicionales(dias_adicionales);
			bitacora.setObservaciones(observaciones);
			bitacora.setUsuario(usuario);
			
			diasSolventacionSuspensionRepository.save(bitacora);
			
			return true;
			
		} catch (Exception e) {
			
			return false;
			
		}
		

	}
	
	/*
	public boolean AgregarDiasAdicionalesSuspension (Long prelacionId) {
		
		Calendar fechaActual=Calendar.getInstance();
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		try {
			
			Prelacion prelacion = prelacionRepository.findOne(prelacionId);
			
			Suspension suspension =   suspencionRepository.findOneAllByPrelacionOrderByIdDesc(prelacion);
			
			Long dias_adicionales = Long.valueOf( parametroRepository.findByCve("DIAS_SUSPENSION_ADICIONALES").getValor());
			
			Long diasTotales = suspension.getDiasAdicionales() + dias_adicionales;
			
			suspension.setDiasAdicionales(diasTotales);
			
			suspencionRepository.save(suspension);
			
			DiasSolventacionSuspension bitacora = new DiasSolventacionSuspension();
			bitacora.setSuspension(suspension);
			bitacora.setFechaModificacion(fechaActual.getTime());
			bitacora.setDiasAdicionales(dias_adicionales);
			bitacora.setUsuario(usuario);
			
			diasSolventacionSuspensionRepository.save(bitacora);
			
			return true;
			
		} catch (Exception e) {
			
			return false;
			
		}
		

	}
	*/
	/*
	public boolean IsSuspensionAutoridad(Prelacion prelacion  ) {
		
	  boolean aplicaAutoridad = false;
		
	   Set<Acto> actosPrel =  prelacion.getActosParaPrelacions();
		
	   for (Acto acto : actosPrel) {
		 
		 Set<ActoDocumento> actoDocumento =   acto.getActoDocumentosParaActos();
		 
		 for (ActoDocumento actoDocumento2 : actoDocumento) {
			
			Documento documento = actoDocumento2.getDocumento();
			  
			  if(documento.getTipoDocumento().getAplicaAutoridad()) {
				  aplicaAutoridad = true;
			  }
			
			
		 }
		 
	   }
	   
		return 	aplicaAutoridad;
		
	}
	*/
	
	public Date fechaActual(){

	      Calendar calendar = Calendar.getInstance();

	      return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

	 }

	public List<BandejaSuspensionVO> listaPrelacionesSuspendidas(Long oficinaId)
	{
	
		List<Suspension> suspenciones = new ArrayList<Suspension>();
		List<BandejaSuspensionVO> resultado = new ArrayList<BandejaSuspensionVO>();
		BandejaSuspensionVO obj; 
		
		try {
						
			suspenciones = suspencionRepository.findAllByestatusSuspensionAndOficinaId(2L,oficinaId);
						
			
			for (Suspension item : suspenciones) {
				
				obj = new BandejaSuspensionVO();
				
				
				if(item.getPrelacion().getStatus().getId() == Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus())
				{
					
					if(item.getSuspensionFirma()!=null) {						
						obj.setFechaFirma(item.getSuspensionFirma().getFechaFirma());
					} else {
						obj.setFechaFirma(null);
					}
					Bitacora bitacora = bitacoraRepository.findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(item.getPrelacion().getId(), Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus());                           
				
					Date fechaVencimiento = CalcularFechaVencimientoSolventacion(item);
					obj.setPrelacionId(item.getPrelacion().getId());
					if(item.getPrelacion().getConsecutivo()!=null) {
						obj.setConsecutivo(item.getPrelacion().getConsecutivo().toString());
						obj.setNoFolio(item.getPrelacion().getConsecutivo().longValue());	
					}
					if(item.getPrelacion().getActosParaPrelacions()!=null) {
						for (Acto acto:item.getPrelacion().getActosParaPrelacions()){
							obj.setActoId(acto.getId());
						}
					}
					
					obj.setFechaIngreso(item.getFechaSuspension());
					obj.setFechaVencimiento(fechaVencimiento);
					if(bitacora!=null){
						obj.setMotivo(bitacora.getMotivo()!=null ? bitacora.getMotivo().getNombre() : "");
						obj.setTipoRechazo(bitacora.getTipoRechazo()!=null ? bitacora.getTipoRechazo().getNombre(): "");
						obj.setObservaciones(bitacora.getObservaciones());					
					}
					//obj.setAcuerdo(ObtieneAcuerdoAudiencia(item));
					obj.setAcuerdo(" ");
					if(item.getPorAutoridad()!=null) {
						obj.setPorAutoridad(item.getPorAutoridad());
					} else {
						obj.setPorAutoridad(false);
					}
					Long dias = new Long( ObtieneDiasTramiteSuspendido(item.getFechaSuspension()));
					obj.setDiasSuspendido(dias);
					resultado.add(obj);
					
				}

			}
			log.info("Total de Resultado:"+resultado.size());
			
			return resultado;
		} catch (NullPointerException npe) {
			log.info("Error: "+npe.getLocalizedMessage());
			
			return null;
		} catch (Exception except) {
			log.info("Error: "+except.toString());
			return null;
		}

	}

	public Boolean esPrelacionSuspendida(Long prelacionId) {	
		Prelacion prelacion=prelacionRepository.findById(prelacionId);		
		Suspension suspension=suspencionRepository.findOneByestatusSuspensionAndPrelacion(2L,prelacion);
		
		if(suspension!=null){			
			return true;
		}else{
			return false;
		}
	}
	
	public Date CalcularFechaVencimientoSolventacion(Suspension suspension)
	{
		Date fechaVencimiento = null; 
		try {
			if (suspension.getPorAutoridad()) {
				
				int dias = Integer.valueOf(parametroRepository.findByCve("DIAS_SUSPENSION_AUTORIDAD").getValor());
				//int diasAdicionales = suspension.getDiasAdicionales().intValue();
				//System.out.println("CRON: Los dias de suspension son: " + dias);
				fechaVencimiento = CalcularFechaDiasNaturales(suspension.getFechaCalculo(), dias);
			
			}else{
				
				int dias = Integer.valueOf(parametroRepository.findByCve("DIAS_SUSPENSION").getValor());
				//int diasAdicionales = suspension.getDiasAdicionales().intValue();
				//System.out.println("CRON: Los dias de suspension son: " + dias);
				dias=dias-1;
				
				fechaVencimiento = turnadorService.addDiasHabiles(suspension.getFechaCalculo(), dias);
				
			}
		}
		catch(Exception e) {
			
		}
		
		return fechaVencimiento;
	}
	
	public Date CalcularFechaDiasNaturales(Date fechaSuspension, int dias) {
		if (dias==0) return fechaSuspension;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fechaSuspension); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      return calendar.getTime(); 
	}
	
	public String ObtieneAcuerdoAudiencia(Suspension suspension) {
		
	    DiasSolventacionSuspension item = 	diasSolventacionSuspensionRepository.findFirstBySuspensionOrderByIdDesc(suspension);
		if (item != null) {
			return item.getObservaciones();
		} else {
			return "";
		}
	 
	}
	
	public int ObtieneDiasTramiteSuspendido (Date fechaSuspension) {
		
		int dias = 0;
		try {
			dias = turnadorService.ObtieneDiasDesdeFecha(fechaSuspension);	
		}catch(Exception e) {
			
		}
		return dias;
	}
	
	public void CronDepurarPrelacionesSuspendidas() {

		List<Suspension> suspenciones = new ArrayList<Suspension>();
		Prelacion prelacion ;
		BandejaSuspensionVO obj; 
		Status status = statusRepository.findOne(6L);
		
		//System.out.println("CRON: Entro en cron Suspension " );
		
		try {
						
			suspenciones = suspencionRepository.findAllByestatusSuspension(2L);

			for (Suspension item : suspenciones) {
				
				prelacion = new Prelacion();
				
				//System.out.println("--------------------------------------------------------------------------------");
				
				
				if(item.getPrelacion().getStatus().getId() == Constantes.Status.SUSPENDIDA_CALIFICADOR.getIdStatus())
				{
					Date fechaVencimiento = CalcularFechaVencimientoSolventacion(item);
					//System.out.println("CRON: Revisando prelacion  " + item.getPrelacion().getConsecutivo() + " con fecha de suspension " + item.getFechaSuspension()   );
					//System.out.println("CRON: Revisando prelacion  " + item.getPrelacion().getConsecutivo() + " con status " + item.getPrelacion().getStatus().getId()   );

					if(fechaVencimiento.before(fechaActual())) {			
						//System.out.println("CRON: Prelacion "+ item.getPrelacion().getConsecutivo() + " Asignada a registrador para su rechazo");
						//System.out.println("CRON: fechaRegistro: - " + item.getPrelacion().getFechaIngreso());
						//System.out.println("CRON: fechaSuspension: - " + item.getFechaSuspension());
						//System.out.println("CRON: fechaVencimiento: - " + fechaVencimiento);
						Status statusAnterior = new Status();
						prelacion = prelacionRepository.findOne(item.getPrelacion().getId());
						statusAnterior = prelacion.getStatus();
						
						prelacion.setStatusAnterior(statusAnterior);
						prelacion.setStatus(status);
						
						prelacionRepository.save(prelacion);
						
						bitacoraRepository.save(createBitacora(prelacion));	

					}else {
						//System.out.println("CRON: La prelacion "+ item.getPrelacion().getConsecutivo() + " aun cuenta con tiempo para ser solventada");
						//System.out.println("CRON: fechaRegistro: - " + item.getPrelacion().getFechaIngreso());
						//System.out.println("CRON: fechaSuspension: - " + item.getFechaSuspension());
						//System.out.println("CRON: fechaVencimiento: - " + fechaVencimiento);
						
					}
				
					
				}
				
			}
			
			return;
		}
		catch (Exception except) {
			
			return;
		}
		
		
		
	}
	
	private boolean ValidaAplicaEstatusSuspensivo(Prelacion prelacion) {
		
		boolean aplica = false;
		
		Set<PrelacionPredio> prelacionPredioCreado = prelacion.getPrelacionPrediosParaPrelacions();
		
		List<Predio> listPrediosCreado = new ArrayList<Predio>();

	      Set<Acto> actos =	prelacion.getActosParaPrelacions();
	      boolean esCertificado = false;
	      
	      for(Acto acto : actos) {
	    	  
	    	 if( acto.getTipoActo().getClasifActo().getId() == 9L ) {
	    		 esCertificado = true;
	    	 }
	    	  
	      }
	      
	      if(esCertificado) {
	    	  return false;    	  
	      }
	      

		for (PrelacionPredio item : prelacionPredioCreado) {
		
			listPrediosCreado.add(item.getPredio());
			
		}
		
		
		List<BandejaSuspensionVO> prelacionesSuspendidas =  listaPrelacionesSuspendidas(prelacion.getOficina().getId());
		List<Predio> listPrediosSuspendidos = new ArrayList<Predio>();
		
		for (BandejaSuspensionVO item : prelacionesSuspendidas) {
			
		    Prelacion prelacion2 = prelacionRepository.findOne(item.getPrelacionId());	
		    
		    Set<PrelacionPredio> prelacionPredioSuspendido =  prelacion2.getPrelacionPrediosParaPrelacions();
		    
		    for (PrelacionPredio item2 : prelacionPredioSuspendido) {
		    	
		    	listPrediosSuspendidos.add(item2.getPredio()); 
			}
		    		    		
		}
		
		for (Predio item : listPrediosCreado) {
		
			for (Predio item2 : listPrediosSuspendidos) {
			
				if (item.getNoFolio() == item2.getNoFolio()) {
				   //Status status = statusRepository.findOne(17L);
				   //IHM 20-03-2019
				   prelacion.setMarcarSuspensivo(true);
				   prelacionRepository.save(prelacion);
				   bitacoraRepository.save(createBitacora(prelacion));	
				   aplica = true;
				   //IHM 20-03-2019
				   //icresonService.updateEstatusSolicitud(prelacion.getSolicitudPago().getNumeroSolicitudIcreson().toString(), String.valueOf(-3));
				}
				
			}
		}
		return aplica;
	}
	
	public void RevertirEstatusSuspensivos(Prelacion prelacion) {
		
		Usuario usuario = prelacion.getUsuarioAnalista(); 
		 
		Set<PrelacionPredio> prelacionPredioSolventado = prelacion.getPrelacionPrediosParaPrelacions();
		
		List<Predio> listPrediosSolventado = new ArrayList<Predio>();
		
		for (PrelacionPredio item : prelacionPredioSolventado) {
		
			listPrediosSolventado.add(item.getPredio());
			
		}
		
		//Status status = statusRepository.findOne(17L);
		List<Prelacion> listPrelacionesSuspensivas = prelacionRepository.findAllByMarcarSuspensivo(true);
		
		for (Prelacion item : listPrelacionesSuspensivas) {
		 
			for (PrelacionPredio item2 : item.getPrelacionPrediosParaPrelacions()) {
				
				for (Predio item3 : listPrediosSolventado) {
					
					if (item2.getPredio().getId() == item3.getId()) {
						
						Status status2 = statusRepository.findOne(4L);
						item.setStatus(status2);
						item.setUsuarioAnalista(usuario);

						//IHM 20-03-2019
						item.setMarcarSuspensivo(false);
						prelacionRepository.save(item);
						
					}
					
				}
				
			}
			
		}
		
		
	}
	
	public void AplicarEstatusSuspensivoPrelacionesExistentes(Prelacion prelacion) {
		
		List<Long> listStatus = new ArrayList<Long>();
		
		listStatus.add(2L);
		listStatus.add(4L);
		listStatus.add(5L);
		
		List<Long> listAreas = new ArrayList<Long>();
		listAreas.add(1L);
		listAreas.add(7L);
		listAreas.add(8L);
		listAreas.add(9L);

		Set<PrelacionPredio> prelacionPredioSuspendida = prelacion.getPrelacionPrediosParaPrelacions();
		
	    List<Prelacion> listPrelaciones =	prelacionRepository.findAllByListStatus(listStatus, listAreas,  prelacion.getOficina().getId());
		
	    List<Prelacion> listPrelacionAplicaSuspensivo = new ArrayList<Prelacion>();
	    
	    for (PrelacionPredio prelPredSuspendido : prelacionPredioSuspendida) {

	    	Integer folio = prelPredSuspendido.getPredio().getNoFolio();

	    	for(Prelacion prel : listPrelaciones) {
		    	
	    		Set<PrelacionPredio> prelPredRevisar = prel.getPrelacionPrediosParaPrelacions();
		    	
	    		for(PrelacionPredio revisar : prelPredRevisar) {
	    			
	    			if (revisar.getPredio().getNoFolio() == folio) {
						System.out.println("La prelacion " + prel.getConsecutivo() + " se cambio a status suspensivo");
	    				Status status = statusRepository.findOne(17L);
	    				prel.setStatus(status);
						
						//IHM 20-03-2019
						prel.setMarcarSuspensivo(true);
	    		    	prelacionRepository.save(prel);
	    		    	bitacoraRepository.save(createBitacora(prel));	
	    		    	
	    		    	//icresonService.updateEstatusSolicitud(prel.getSolicitudPago().getNumeroSolicitudIcreson().toString(), String.valueOf(-3));
	    				
					}
	    			
	    		}
		    	
		    }

		}
 
	}
	
 	public byte[] getPdfBoletaSuspencion(Prelacion prelacion) {

		 try {
			return	boletaRegistralService.getPdfBoletaSuspencion(prelacion);
		} catch (JRException e) {
			return null;
			
		}

	}
	
	public List<PrelacionBitacoraImpresionVO> llenadoPrelacionBitacoraImpresionVO(List<Prelacion> lisPrelacion,Usuario us) {
		List<Prelacion> listPrel = lisPrelacion;
		BitacoraImpresion bitacoraImpresion= new BitacoraImpresion();
		List<PrelacionBitacoraImpresionVO> listPrelBitImp = new ArrayList<PrelacionBitacoraImpresionVO>();

		for (Prelacion prel: listPrel ) {
			PrelacionBitacoraImpresionVO  prelBitImp = new  PrelacionBitacoraImpresionVO();
			prelBitImp.setPrelacion(prel);
			Long prelId= prel.getId();
			Usuario usuarioSol = prel.getUsuarioSolicitan();
			Usuario usuarioVent = prel.getUsuarioVentanilla();

			if(usuarioSol != usuarioVent) {
				prelBitImp.setVentanilla(true);
			}else {
				prelBitImp.setVentanilla(false);
			}
			bitacoraImpresion = bitacoraImpresionRepository.findBitacoraImpresionAndUserLogged(prelId, us);
			if(bitacoraImpresion==null) {
				prelBitImp.setBitacoraImpresion(null);
			}else if(bitacoraImpresion !=null) {
				prelBitImp.setBitacoraImpresion(bitacoraImpresion);
			}
			listPrelBitImp.add(prelBitImp);
		}
		return listPrelBitImp;
	}

	public Boolean findBitacoraImpresionAndUserLogged(Long prelacionId) {
		Boolean bnd = null ;
		Date fech = new Date();
		StatusImpresion stImp = new StatusImpresion();
		BitacoraImpresion bitacoraImp = new BitacoraImpresion();
		Usuario us = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		Prelacion prel = prelacionRepository.findOne(prelacionId);
		Long status = prel.getStatus().getId();
	//	System.out.println("Prelacion : " + prel + "ESTATUS :  " + status);
	    BitacoraImpresion bitacoraImpresion = bitacoraImpresionRepository.findBitacoraImpresionAndUserLogged(prelacionId, us);
	    int version;

	  //  System.out.println("BitacoraImp  :  " + bitacoraImpresion);
	    if(bitacoraImpresion== null) {

	    	bitacoraImp.setPrelacion(prel);
	    	bitacoraImp.setFechaImpresion(fech);
	    	bitacoraImp.setUsuarioLogeado(us);
	    	bitacoraImp.setVersion(1);
	    	bitacoraImp.setInactivo(true);
	    	if(status == 8) {
	    		stImp.setId(1L);
	    		stImp.setNombre("Impresion Terminado Registrado");
	    		bitacoraImp.setStatus_impresion(stImp);
	    	} else if (status==9) {
	    		stImp.setId(2L);
	    		stImp.setNombre("Impresion Terminado Registrado Parcialmente");
	    		bitacoraImp.setStatus_impresion(stImp);
	    	}else if(status == 10) {
	    		stImp.setId(3L);
	    		stImp.setNombre("Impresion Terminado Rechazado");
	    		bitacoraImp.setStatus_impresion(stImp);
	    	}

	    	bitacoraImpresionRepository.saveAndFlush(bitacoraImp);


	    }else if(bitacoraImpresion.getStatus_impresion().getId()==2 || bitacoraImpresion.getStatus_impresion().getId()==3 && bitacoraImpresion.getInactivo() !=true) {
	    	 version = (bitacoraImpresion.getVersion())+1;
	    	//System.out.println("Version : " + version + "Fecha  : " + fech);
	    //	status=9L;
	    	bitacoraImp.setPrelacion(prel);
	    	bitacoraImp.setFechaImpresion(fech);
	    	bitacoraImp.setUsuarioLogeado(us);
	    	bitacoraImp.setVersion(version);
	    	bitacoraImp.setInactivo(true);
	    	if(status == 9) {
	    		stImp.setId(2L);
	    		stImp.setNombre("Impresion Terminado Registrado Parcialmente");
	    		bitacoraImp.setStatus_impresion(stImp);
	    	} else if (status==10) {
	    		stImp.setId(3L);
	    		stImp.setNombre("Impresion Terminado Rechazado");
	    		bitacoraImp.setStatus_impresion(stImp);
	    	}

	    	bitacoraImpresionRepository.saveAndFlush(bitacoraImp);

	    }else {
	    	bnd= false;
	    	//System.out.println("BANDERA2 :  " + bnd);
	    }

	    bitacoraImpresion = bitacoraImpresionRepository.findBitacoraImpresionAndUserLogged(prelacionId, us);
	    bnd = bitacoraImpresion.getInactivo();
				//System.out.println("BANDERA :  " + bnd);
		return bnd;

	}

	public List<PrelacionBitacoraImpresionVO> findPrelBitacoraImpresionVO(int tipoUsuario) {
		Usuario us = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		List<Prelacion> listPrel = new ArrayList<Prelacion>();
		listPrel = findAllByUsuario(tipoUsuario);
		//System.out.println("Longitud de listPrel : " + listPrel.size());
		BitacoraImpresion bitacoraImpresion= new BitacoraImpresion();
		List<PrelacionBitacoraImpresionVO> listPrelBitImp = new ArrayList<PrelacionBitacoraImpresionVO>();

		return llenadoPrelacionBitacoraImpresionVO(listPrel, us);
	}

	public Date modificarFecha(Date fecha) {
		Date fechaFin= null;
		Calendar cal  = Calendar.getInstance();
		cal.setTime(fecha);
		cal.add(cal.DAY_OF_YEAR, 1);
		fechaFin = cal.getTime();
		System.out.println( "Fecha Final " + fechaFin );
		return fechaFin;
	}

	public PrelacionServicio findByPrelacionId(Long idPrel ) {
		return prelacionServicioRepository.findByIdPrelacion(idPrel);
	}

	public Certificado tipoCertificado(Long prelacionId) {
		Certificado tipoCertificado = null;
		List<Certificado> listPrelacionServicio = certificadoRepository.certificadoByPrelacionId(prelacionId);
		for(Certificado certi: listPrelacionServicio) {
			tipoCertificado = certi;
		}
		
		return tipoCertificado;
		
	}

	  public List<Prelacion>findFormValida (Long oficina, Integer consecutivo, String fechaEnvioFirma, String firma){
		Date fechaEnvioFirmaOK;
		String result = " ";
		List<Prelacion> prelaciones = new ArrayList<Prelacion>();
		List<Prelacion> prelacionesOut = new ArrayList<Prelacion>();
		try {

			//info Firma
			int f = firma.length();
			if(f>5){
					 result = firma.substring(f-5);
				}else{
			 System.out.println("La firma completa es "+ firma);
			 result = firma;
			 System.out.println("El resultado es " + result);
             
			}

			fechaEnvioFirmaOK = formatYMDS.parse(fechaEnvioFirma);
			String [] dateParts = fechaEnvioFirma.split("-");
			log.info("oficina: " + oficina + " consecutivo: " + consecutivo + " fe: " + fechaEnvioFirma );
			prelaciones = prelacionRepository.findBusquedaValida(oficina, consecutivo, Integer.parseInt(dateParts[0]));
			log.info("prels: " + prelaciones);


			//Validacion de firma manualmente
			log.info("A001");
			for( Prelacion p : prelaciones ) {
				log.info("A002 " + p);
				String firma2 = (""+p.getFirma());
				if(firma2.length()>5) {
					firma2 = firma2.substring(firma2.length()-5);
				}

		
			log.info("firma2: " + firma2);
				if( result.equals(firma2) ) {
					prelacionesOut.add(p);
				}
			} 

			// Termina validacion de Firma



		} catch (ParseException e) {
			log.error("Error al parsear fechaEnvioFirma: " + fechaEnvioFirma, e);
			e.printStackTrace();
		}
		return prelacionesOut;
	}



	public  List<ActoPublicitarioVO> getActoPublicitario(String nombre, String apellido_paterno, String apellido_materno, Long servicioId) {
	
	
		ActoPublicitarioVO resultado = null;
		Long  oficinaId = null;
		Servicio serv = this.servicioRepository.findOne(servicioId);
		List<ActoPublicitarioVO> resultados=new ArrayList();		
		
		String nom="";
		if(apellido_paterno.equalsIgnoreCase("_")) {
			apellido_paterno="";
		}
		if(apellido_materno.equalsIgnoreCase("_")) {
			apellido_materno="";
		}		
		nom = nombre.replace(" ", "").concat(apellido_paterno.replace(" ","")).concat(apellido_materno.replace(" ","")).toUpperCase();
		
		nom  =  nom.replaceAll("\\s", "");
		
	
		Long tipoActo = serv.getTipoActo().getId();
		Usuario u;
		try {
			u = usuarioService.getLoguedUser();
			 oficinaId =u.getOficina().getId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(oficinaId!=null) {
			List<ActoPublictarioModel> apms = caratulaDAO.findActoPublicitarioPorNombre(nom, tipoActo, oficinaId);
			Set<Long> aps=apms.stream().map(p -> p.getActoId()).collect(Collectors.toCollection(HashSet::new));
			if(apms!=null){
				
				System.out.println("actosOk: "+apms.size());	
				
				for(Long actoId:aps){
					
					Acto a = actoRepository.findOne(actoId);
					Prelacion p= a.getPrelacion();
					
					if(p!=null && p.getConsecutivo()!=null && p.getConsecutivo() > 1){	
						
						List<ActoPredio> actopredio = actoPredioRepository.findLastVersionByActo(a.getId());
		
						for(ActoPredio b:actopredio){
						
							List<ActoPredioCampo> actoPredioCampos = actoPredioCampoRepository.findAllByActoPredioOrderById(b);
							
							if(actoPredioCampos!=null && !actoPredioCampos.isEmpty()){
								switch (serv.getTipoActo().getId().intValue()){// busqueda acto seleccionado
									
								case 1://REPUDIO
									resultado = busquedaPorActo1(actoPredioCampos,b);
									if(resultado != null){
										
										resultados.add(resultado);		
									}
								break;
								case 2://PROTOCOLIZACIÓN DE AUTORIZACIÓN DE VENTA
									resultado = busquedaPorActo2(actoPredioCampos,b);
									if(resultado != null){
										resultados.add(resultado);		
									}
								break;
								case 3://RECTIFICACIÓN
									resultado = busquedaPorActo3(actoPredioCampos,b);
									if(resultado != null){								
										resultados.add(resultado);		
									}
								break;	
								case 4://CANCELACIONES
									resultado = busquedaPorActo4(actoPredioCampos, b);
									if(resultado != null){
										resultados.add(resultado);		
									}
								break;		
								case 5://PROTOCOLIZACIÓN DE PRORROGA DE VIGENCIA PARA ACTO MODIFICATORIO
									resultado = busquedaPorActo5(actoPredioCampos,b);
								if(resultado != null){								
									System.out.println("Acto Predio encontrado b: "+b.getId());
									resultados.add(resultado);		
								}
								break;
								case 6://ACTO PUBLICITARIO GENERAL
									resultado = busquedaPorActo6(actoPredioCampos,b);
									if(resultado != null){								
										resultados.add(resultado);		
									}
								break;
								case 7://REGISTRO, CANCELACIÓN, FIRMA PATENTE O HABILITACION Y SELLO DE FEDATARIOS PUBLICOS
									resultado = busquedaPorActo7(actoPredioCampos, b);
									if(resultado != null){
										resultados.add(resultado);		
									}
								break;
									case 54://DECLARACIÓN DE HEREDEROS
										resultado = busquedaPorActo54(actoPredioCampos, b);
										if(resultado != null){
											resultados.add(resultado);		
										}
									break;
									case 56://CESIÓN DE DERECHOS HEREDITARIOS
										resultado = busquedaPorActo56(actoPredioCampos, b);
										if(resultado != null){
											System.out.println("Acto Predio encontrado b: "+b.getId());
											resultados.add(resultado);		
										}
									break;
									case 57://nombramiento de albacea
										resultado = busquedaPorActo57(actoPredioCampos,b);
										if(resultado != null){
											
											resultados.add(resultado);		
										}
									break;
									case 76://INFORMES DE DISPOSICIÓN TESTAMENTARIA									
										resultado = busquedaPorActo76(actoPredioCampos, b);
										if(resultado != null){
											resultados.add(resultado);		
										}
									break;
									case 105://SECCIÓN AUXILIAR PODER(ES)
										resultado = busquedaPorActo105(actoPredioCampos,b);
										if(resultado != null){
									
											resultados.add(resultado);		
										}
									break;
									case 106://SECCIÓN AUXILIAR MODIFICACIÓN PODER(ES)
										resultado = busquedaPorActo106(actoPredioCampos, b);
										if(resultado != null){
									
											resultados.add(resultado);		
										}
									break;
									case 107://SECCIÓN AUXILIAR REVOCACIÓN  PODER(ES)
										resultado = busquedaPorActo107(actoPredioCampos,b);
										if(resultado != null){
											resultados.add(resultado);		
										}
									break;
									case 132://REVOCAION DE TESTAMENTO
										resultado = busquedaPorActo132(actoPredioCampos,b);
										if(resultado != null){
											resultados.add(resultado);		
										}
									break;
									case 133://RETIRO DE TESTAMENTO
										resultado = busquedaPorActo133(actoPredioCampos,b);
										if(resultado != null){
											resultados.add(resultado);		
										}
									break;
									case 134://DEPÓSITO DE TESTAMENTO
										resultado = busquedaPorActo134(actoPredioCampos, b);
										if(resultado != null){
											System.out.println("Acto Predio encontrado b: "+b.getId());
											resultados.add(resultado);		
										}
									break;
									case 242://LECTURA DE TESTAMENTO
										resultado = busquedaPorActo242(actoPredioCampos,b);
										if(resultado != null){

											resultados.add(resultado);		
										}
									break;
									case 244://RESOLUCIÓN ADMINISTRATIVA PARA AUTORIZACIÓN DE ACTO MODIFICATORIO DEL INMUEBLE
										resultado = busquedaPorActo244(actoPredioCampos,b);
										if(resultado != null){
											resultados.add(resultado);		
										}
									break;
									
								}//fin busqueda acto seleccionado						
							}
						}				
					}	
				}
			} 
			if(resultados.isEmpty()){
				resultado = new ActoPublicitarioVO();
				resultado.setOficina(usuarioService.getUsuario().getOficina().getNombre());
				System.out.println(usuarioService.getUsuario());
				resultado.setNoRegistrosEncontrados("NO SE ENCONTRARON \n REGISTROS");	
				resultados.add(resultado);
			} 
		}
	
		return resultados;   
	}

	private ActoPublicitarioVO busquedaPorActo1(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){
		String nAlbacea ="";	
		String repN="";
		String cujusN="",nSolicitante="";
		String tipoDeDocumento="",autoridad="";
		

		String expediente = "";
		String tipoDeSucesion="";
		String observaciones="";

		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			switch(id){
				
				// repudiante
				case 20086:
					repN +="\n"+ c.getValor().trim();
				break;
				case 20087:
					repN +=" "+ c.getValor().trim();
				break;
				case 20088:
					repN +=" "+ c.getValor().trim();
				break;
				//ALBACEA
				case 20742://ALBACEA NOMBRE
					nAlbacea +="\n"+ c.getValor().trim();
				break;
				case 20743://ALBACEA PRIMER APELLIDO
					nAlbacea +=" "+ c.getValor().trim();
				break;
				case 20744://SEGUNDO APELLIDO	
					nAlbacea +=" "+ c.getValor().trim();
				break;
				case 843://CUJUS NOMBRE
					cujusN +="\n"+c.getValor().trim();
				break;
				case 844://CUJUS AP
					cujusN +=" "+c.getValor().trim();
				break;
				case 845://CUJUS AM
					cujusN +=" "+c.getValor().trim();
				break;
				
				case 252:
						nSolicitante+="\n"+c.getValor().trim();
					break;
				case 253:
						nSolicitante+=" "+c.getValor().trim();
					break;
				case 775:
						nSolicitante+=" "+c.getValor().trim();
					break;
				
				case 20137: //EXPEDIENTE
					expediente = c.getValor().trim();
				break;
			
				case 996://TIPO DECLARACION
					CampoValores cv = campoValorService.findOne(Long.valueOf(c.getValor()));
					tipoDeSucesion=cv.getNombre();
				break;

				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;

				
			}
			switch(moduloId){
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
		}//fin busqueda
	
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}										
		
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			
			Acto1Model am1=new Acto1Model();	

			am1.setnSolicitante(nSolicitante);
			am1.setRepudiantes(repN);
			am1.setCujus(cujusN);
			am1.setAlbacea(nAlbacea);
			am1.setExpediente(expediente);
			am1.setTipoSucesion(tipoDeSucesion);
			am1.setTipoDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			am1.setObservaciones(observaciones);


			ArrayList<Acto1Model> a1s=new ArrayList<Acto1Model>();
			a1s.add(am1);
			resultado.setActo1Models(a1s);		
		}
	
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo2(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){

		String proN="";


		String tipoDeDocumento = "",observaciones="";

		String datosDelaResolucion="",autoridad="";
		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			ModuloCampo moduloCampo = c.getModuloCampo();
			switch(id){
				case 843:
					proN += "\n"+ c.getValor().trim();
				break;
				case 844:
					proN += "\n"+ c.getValor().trim();
				break;
				case 845:
					proN += "\n"+ c.getValor().trim();
				break;
				
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;				
			}
			switch(moduloId){
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 5017:
					datosDelaResolucion+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
			
			
			
		}//fin busqueda}
		
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}										
			
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			
			Acto2Model am2=new Acto2Model();		
		
			if(observaciones!=null && !observaciones.isEmpty()){
				observaciones="OBSERVACIONES:\n\n"+observaciones;
			}
			am2.setDetalle("DATOS DEL PROMOVENTE \n"+proN+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(datosDelaResolucion)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)+
			"\n\n AUTORIDAD:\n  "+this.boletaRegistralService.quitarUltimoCaracter(autoridad)+
			"\n\n "+observaciones
			);

			
			ArrayList<Acto2Model> a2s=new ArrayList<Acto2Model>();
			a2s.add(am2);
			resultado.setActo2Models(a2s);		
		}
	
		return resultado;
	}
	
	private ActoPublicitarioVO busquedaPorActo3(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){

		String solN="",autoridad="";


		Boolean nombreEncontrado = false;
		String tipoDeDocumento = "",observaciones="";
		
		String datosDelaAclaracion="";
		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			ModuloCampo moduloCampo = c.getModuloCampo();
			switch(id){
				case 843:
					solN +="\n"+ c.getValor().trim();
				break;
				case 844:
					solN +=" "+ c.getValor().trim();
				break;
				case 845:
					solN +=" "+ c.getValor().trim();
				break;
				
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;				
			}
			switch(moduloId){
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 115:
					datosDelaAclaracion+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
			
		}//fin busqueda
		
		if(nombreEncontrado || actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}										
		
				resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			
			Acto2Model am2=new Acto2Model();		
			
			if(observaciones!=null && !observaciones.isEmpty()){
				observaciones="OBSERVACIONES:\n\n"+observaciones;
			}
			am2.setDetalle("DATOS DEL SOLICITANTE: \n\n"+solN+
			"\n\n DATOS DE LA ACLARACIÓN \n\n"+this.boletaRegistralService.quitarUltimoCaracter(datosDelaAclaracion)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)+
			"\n\n AUTORIDAD:\n  "+this.boletaRegistralService.quitarUltimoCaracter(autoridad)+
			"\n\n "+observaciones
			);

			ArrayList<Acto2Model> a2s=new ArrayList<Acto2Model>();
			a2s.add(am2);
			resultado.setActo2Models(a2s);		
		}
	
		return resultado;
	}
	
	private ActoPublicitarioVO busquedaPorActo4(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){

		String solN="";

		String tipoDeDocumento="",autoridad="";

		ArrayList<String> acts = new ArrayList<String>();

		String observaciones="";

		String operaSobre="",actPubFolPer="",cancelacionPor="",cancelacionDe="",montoDelaOpercion="";
		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			ModuloCampo moduloCampo = c.getModuloCampo();
			switch(id){
				case 843:
					solN += "\n"+c.getValor().trim();
				break;
				case 844:
					solN += " "+c.getValor().trim();
				break;
				case 845:
					solN += " "+c.getValor().trim();
				break;
				case 3447:
					acts.add( this.boletaRegistralService.obtenerValor(c));
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;				
			}
			switch(moduloId){
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 307:
					operaSobre+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 5511:
					actPubFolPer+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 2026:
					cancelacionPor+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 2033:
					cancelacionDe+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 21:
					montoDelaOpercion+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			
			}
					
		}//fin busqueda

			
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}										
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			
			Acto2Model am2=new Acto2Model();		
			
			String act="";
			for(String a:acts){
				act+=""+a+"\n";
			}
			if(observaciones!=null && !observaciones.isEmpty()){
				observaciones="OBSERVACIONES:\n\n"+observaciones;
			}
			
			am2.setDetalle("DATOS DEL SOLICITANTE \n"+solN+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(operaSobre)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(actPubFolPer)+
			"\n\n ACTOS: \n\n"+act+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(cancelacionPor)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(cancelacionDe)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(montoDelaOpercion)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento+"\n AUTORIDAD:\n\n"+autoridad)+
			"\n\n "+observaciones
			);

			ArrayList<Acto2Model> a2s=new ArrayList<Acto2Model>();
			a2s.add(am2);
			resultado.setActo2Models(a2s);		
		}
	
		return resultado;
	}


	private ActoPublicitarioVO busquedaPorActo5(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){

		String proN="";
		String tipoDeDocumento = "",observaciones="",datosDelInmueble="",autoridad="";
	
	
		String datosDelaResolucion="";
		String tipoDeActoModificatorio="";
		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			ModuloCampo moduloCampo = c.getModuloCampo();
			switch(id){
				case 843:
					proN +="\n"+ c.getValor().trim();
				break;
				case 844:
					proN +=" "+ c.getValor().trim();
				break;
				case 845:
					proN +=" "+ c.getValor().trim();
				break;
				case 20789:
					tipoDeActoModificatorio=" "+this.boletaRegistralService.obtenerValor(c)+" ";
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;				
			}
			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 5017:
					datosDelaResolucion+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 5015:
					datosDelInmueble+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
						
		}//fin busqueda

		
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}										
			
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
				
			Acto2Model am2=new Acto2Model();		
			
			if(observaciones!=null && !observaciones.isEmpty()){
				observaciones="OBSERVACIONES:\n\n"+observaciones;
			}
			am2.setDetalle("TIPO DE ACTO MODIFICATORIO: \n\n   "+tipoDeActoModificatorio+
			"\n\n DATOS DEL PROMOVENTE: \n\n    "+proN+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(datosDelaResolucion)+
			"\n\n DATOS DEL INMUEBLE \n\n   "+this.boletaRegistralService.quitarUltimoCaracter(datosDelInmueble)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)+
			"\n\n AUTORIDAD:\n "+this.boletaRegistralService.quitarUltimoCaracter(autoridad)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(observaciones)
			);

			ArrayList<Acto2Model> a2s=new ArrayList<Acto2Model>();
			a2s.add(am2);
			resultado.setActo2Models(a2s);		
		}
	
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo6(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){


		String solN="";
		String puntoResolutivos="",nExpediente="",inteN="";	
		String tipoDeDocumento = "",autoridad="",observaciones="";


		String datosDelaResolucion="";

		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			ModuloCampo moduloCampo = c.getModuloCampo();
			switch(id){
				
				case 843:
					solN = "\n"+ c.getValor().trim();
				break;
				case 844:
					solN += " "+c.getValor().trim();
				break;
				case 845:
					solN += " "+c.getValor().trim();
				break;
				case 20834:
					inteN += "\n"+this.boletaRegistralService.obtenerValor(c);
				break;
				case 20835:
					inteN += " "+this.boletaRegistralService.obtenerValor(c);
				break;
				case 20836:
					inteN += " "+this.boletaRegistralService.obtenerValor(c);
				break;
				case 20045:
					nExpediente=this.boletaRegistralService.obtenerValor(c);
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;				
			}
			switch(moduloId){
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 5017:
					datosDelaResolucion+=" "+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 308:
					puntoResolutivos+=""+moduloCampo.getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
	
		}//fin busqueda

		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}										
		
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			Acto2Model am2=new Acto2Model();		
		
			if(observaciones!=null && !observaciones.isEmpty()){
				observaciones="OBSERVACIONES:\n\n"+observaciones;
			}
			am2.setDetalle("DATOS DEL SOLICITANTE: \n"+solN+
			"\n\n NUMERO DE EXPEDIENTE \n\n     "+nExpediente+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(datosDelaResolucion)+
			"\n\n INTERVINIENTES: \n  "+inteN+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(puntoResolutivos)+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)+
			"\n\n AUTORIDAD:\n"+this.boletaRegistralService.quitarUltimoCaracter(autoridad)+
			"\n\n "+observaciones
			);

			ArrayList<Acto2Model> a2s=new ArrayList<Acto2Model>();
			a2s.add(am2);
			resultado.setActo2Models(a2s);		
		}
	
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo7(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){

		String notarioTitular="",fechaOficio="",quienExpide="",notarioAds="",fundLegal="",autoridad="";
		String tipoDeDocumento = "",observaciones="";
		
		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			ModuloCampo moduloCampo = c.getModuloCampo();
			switch(id){
				case 66: 
				notarioTitular += "\n"+this.boletaRegistralService.obtenerValor(c);
				break;
				case 67: 
					fechaOficio=this.boletaRegistralService.obtenerValor(c);
				break;
				case 68: 
					quienExpide+= "\n"+this.boletaRegistralService.obtenerValor(c);
				break;
				case 69: 
					notarioAds+= "\n"+this.boletaRegistralService.obtenerValor(c);
				break;
				case 70: 
					fundLegal=this.boletaRegistralService.obtenerValor(c);
				break;
				case 431:
					observaciones=this.boletaRegistralService.obtenerValor(c);
				break;	
						
			}
			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}		
			
		}//fin busqueda

		
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}
			
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			
			Acto2Model am2=new Acto2Model();	
		
			if(observaciones!=null && !observaciones.isEmpty()){
				observaciones="OBSERVACIONES:\n\n"+observaciones;
			}
			am2.setDetalle("NOMBRE DEL NOTARIO TITULAR: \n\n"+notarioTitular+
			"\n\n FECHA DE OFICIO/NOMBRAMIENTO \n\n     "+fechaOficio+
			"\n\n QUIEN EXPIDE LA PATENTE \n\n   "+quienExpide+
			"\n\n NOTARIO ADSCRITO \n\n  "+notarioAds+
			"\n\n FUNDAMENTO LEGAL/NOMBRAMIENTO \n\n   "+fundLegal+
			"\n\n "+this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)+
			"\n\n AUTORIDAD:\n"+this.boletaRegistralService.quitarUltimoCaracter(autoridad)+
			"\n\n "+observaciones
			);

			ArrayList<Acto2Model> a2s=new ArrayList<Acto2Model>();
			a2s.add(am2);
			resultado.setActo2Models(a2s);		
		}
	
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo54(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){
		String nAlbacea ="";
	
		String expediente = "";
		String tipoDeSucesion="",tipoDeDocumento="",autoridad="";
		String hdoN="";
		String nSol="",aBienes="";
		Boolean nombreEncontrado = false;
		String observaciones="";



		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();
			switch(id){
				//solicitante
				case 252://252 nSol
					nSol+="\n"+c.getValor().trim();
				break;
				case 253://253 ap Sol
					nSol+=" "+c.getValor().trim();
				break;
				case 775://775 am sol
					nSol+=" "+c.getValor().trim();
				break;
				//HDO
				case 1300:// Hdos
					hdoN +="\n"+c.getValor().trim();
				break;
				case 476:
					hdoN +=" "+ c.getValor().trim();
				break;
				case 477:
					hdoN +=" "+ c.getValor().trim();
				break;
				case 843:
					aBienes +="\n"+c.getValor().trim();
					break;
				case 844:
					aBienes +=" "+c.getValor().trim();
					break;
				case 845:
					aBienes +=" "+c.getValor().trim();
					break;
				//ALBACEA
				case 20742://ALBACEA NOMBRE
					nAlbacea+="\n"+ c.getValor().trim();
				break;
				case 20743://ALBACEA PRIMER APELLIDO
					nAlbacea +=" "+ c.getValor().trim();
				break;
				case 20744://SEGUNDO APELLIDO	
					nAlbacea +=" "+ c.getValor().trim();
				break;
				case 20137: //EXPEDIENTE
					expediente = c.getValor().trim();
				break;
				case 996://TIPO DECLARACION
					CampoValores cv = campoValorService.findOne(Long.valueOf(c.getValor()));
					tipoDeSucesion=cv.getNombre();
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;
				
			}
			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
		
		}//fin busqueda
	
		
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
				
			}										
		
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
					
			Acto54Model a54=new Acto54Model();
	
			a54.setHdoNombre(hdoN);		
			a54.setnAlbacea(nAlbacea);	
			a54.setnSolicitante(nSol);		
			a54.setExpediente(expediente);
			a54.setTipoSucesion(tipoDeSucesion);
			a54.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			a54.setaBienes(aBienes);
			a54.setObservaciones(observaciones);
			ArrayList<Acto54Model> a54s=new ArrayList<Acto54Model>();
			a54s.add(a54);
			resultado.setActo54Models(a54s);		
		}
	
		return resultado;
	}
	private ActoPublicitarioVO busquedaPorActo56(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		String crioN="";
		String cedN="";
		String hdoN="";
		String cujusN="";

		String tipoDeDocumento="",autoridad="";
		String observaciones="";
		
		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(id){
				case 106:
					cedN+="\n"+c.getValor().trim();
				break;
				case 107:
					cedN+=" "+c.getValor().trim();
				break;
				case 108:
					cedN+=" "+c.getValor().trim();
				break;
				case 925://Cesionario Nombre
					crioN+="\n"+c.getValor().trim();
				break;
				case 926://Cesionario AP
					crioN+=" "+c.getValor().trim();
				break;
				case 927://Cesionario AM
					crioN+=" "+c.getValor().trim();	
				break;
				case 20086://HDO Nombre
					hdoN+="\n"+c.getValor().trim();
				break;
				case 20087://HDO AP
					hdoN+=" "+c.getValor().trim();
				break;
				case 20088://HDO AM
					hdoN+=" "+c.getValor().trim();
				break;
				case 843://CUJUS NOMBRE
					cujusN+="\n"+c.getValor().trim();
				break;
				case 844://CUJUS AP
					cujusN+=" "+c.getValor().trim();
				break;
				case 845://CUJUS AM
					cujusN+=" "+c.getValor().trim();
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;
			}
			
			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
	
		}
		
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
			}										
			
			
				resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
				Acto56Model a56=new Acto56Model();

			a56.setCrioN(crioN);
			a56.setCujusN(cujusN);
			a56.setHdoN(hdoN);
			a56.setCedente(cedN);
			a56.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			a56.setObservaciones(observaciones);
			
			ArrayList<Acto56Model> a56s=new ArrayList<Acto56Model>();
			a56s.add(a56);
			resultado.setActo56Models(a56s);		
		}
		return resultado;
	}
	private ActoPublicitarioVO busquedaPorActo57(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		
		String cujusN="";
		String nSol="";
		String nAlbacea ="";
	
		
		String expediente = "";
		String tipoDeSucesion="";
		String observaciones = "", nombraOCambio="";
		String tipoDeDocumento="",autoridad="";

		
		
		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(id){
				//solicitante
				case 252://252 nSol
					nSol+="\n"+c.getValor().trim();
				break;
				case 253://253 ap Sol
					nSol+=" "+c.getValor().trim();
				break;
				case 775://775 am sol
					nSol+=" "+c.getValor().trim();
				break;
				//A BIENES DE 
				case 843://CUJUS NOMBRE
					cujusN+="\n"+c.getValor().trim();
				break;
				case 844://CUJUS AP
					cujusN+=" "+c.getValor().trim();
				break;
				case 845://CUJUS AM
					cujusN+=" "+c.getValor().trim();
				break;
				//ALBACEA
					case 20742://ALBACEA NOMBRE
					nAlbacea +="\n"+ c.getValor().trim();
				break;
				case 20743://ALBACEA PRIMER APELLIDO
					nAlbacea +=" "+ c.getValor().trim();
				break;
				case 20744://SEGUNDO APELLIDO	
					nAlbacea +=" "+ c.getValor().trim();
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;
				case 20137: //EXPEDIENTE
					expediente = c.getValor();
				break;
				case 996://TIPO DECLARACION
					CampoValores cv = campoValorService.findOne(Long.valueOf(c.getValor()));
					tipoDeSucesion=cv.getNombre();
				break;
				case 20833: 
                    // NOMBRAMIENTO O CAMBIO ALBACEA
                    nombraOCambio=this.boletaRegistralService.obtenerValor(c);
                break;
			}
			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
			
		}
		if( actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}	
			}	
				resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			Acto57Model a57=new Acto57Model();
		
			a57.setCujusN(cujusN);
			a57.setnAlbacea(nAlbacea+"\n\n"+nombraOCambio);
			a57.setnSolicitante(nSol);
			a57.setTipoSucesion(tipoDeSucesion);
			a57.setObservaciones(observaciones);
			a57.setExpediente(expediente);
			a57.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			
	
			ArrayList<Acto57Model> a57s=new ArrayList<Acto57Model>();
			a57s.add(a57);
			resultado.setActo57Models(a57s);		
		}
		return resultado;
	}
	private ActoPublicitarioVO busquedaPorActo76(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		String respuesta = "",obs="",
		numeroOficio="",
		fecha76="",
		aBienes="",
		juicioS="",
		numExpediente="";
		Boolean testamento=false;
		String nSol="";
		String tipoDeDocumento="",autoridad="";

	
		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(id){
				case 20779:
					CampoValores cv=campoValorService.findOne(Long.parseLong(c.getValor()));
					juicioS=cv.getNombre();
				break;
				case 20725:
					numeroOficio=c.getValor().trim();
				break;
				case 206:
					fecha76=c.getValor().trim();
				break;								
				case 431:
					aBienes=c.getValor().trim();
				break;
				case 252:
					nSol+="\n"+c.getValor().trim();
				break;
				case 253:
					nSol+=" "+c.getValor().trim();
				break;
				case 775:
					nSol+=" "+c.getValor().trim();
				break;
				case 20137:
					numExpediente=c.getValor();
				break;
				case 20778:
					testamento=Boolean.valueOf(c.getValor());
				break;
				case 20194 :
					obs=c.getValor().trim();
				break;
			}
			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}

		
		}
	
		
		if( actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
			}										
			
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());

			String aux76="NO";
			if(testamento) {
				aux76="SI";
			}
			respuesta="EN ATENCIÓN A SU OFICIO "+numeroOficio+" DE FECHA "+fecha76+" DEDUCIDO DEL JUICIO SUCESORIO "+juicioS+" A BIENES DE "+aBienes+" PROMOVIDO POR "+nSol+", EXP. "+numExpediente
			+"\n\n\n"+"ME PERMITO INFORMAR A USTED QUE "+aux76+" SE ENCONTRÓ REGISTRADO EN ESTA INSTITUCIÓN TESTAMENTO A NOMBRE DE "+aBienes+".";
		
			Acto76Model a76 =new Acto76Model();
			a76.setResultado(respuesta);
			a76.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad+"\n\n OBSERVACIONES:\n\n"+obs);
	
			List<Acto76Model> a76s=new ArrayList<Acto76Model>();	
			a76s.add(a76);	
			resultado.setActo76Models(a76s);		
		}
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo133(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
	
		String nSol="";
		//DATOS DE RETIRO DE TESTAMENTO
		String cN="",fCredencial="",fechaDep="",nActa="",rN="",motivo="";

		String tipoDeDocumento="",autoridad="";

		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		String observaciones="";
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			switch(id){
				//solicitante
				case 252://252 nSol
					nSol+="\n"+c.getValor().trim();
				break;
				case 253://253 ap Sol
					nSol+=" "+c.getValor().trim();
				break;
				case 775://775 am sol
					nSol+=" "+c.getValor().trim();
				break;
				//datos del retiro de testamento 
				case 591:
					cN+="\n"+ c.getValor().trim();
				break;
				case 592:
					cN+=" "+ c.getValor().trim();
				break;
				case 593:
					cN+=" "+ c.getValor().trim();
				break;
				case 594:
					fCredencial= c.getValor();
				break;
				case 595:
					fCredencial = c.getValor();
				break;
				case 596:
					nActa = c.getValor();
				break;
				case 597:
					rN+="\n"+ c.getValor().trim();
				break;
				case 598:
					rN+=" "+ c.getValor().trim();
				break;
				case 599:
					rN+=" "+ c.getValor().trim();
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;
				case 655:
					motivo = c.getValor();
					break;
			}
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}

		}//fin busqueda
	
		if(actoPredio !=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
			}
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			Acto133Model a133=new Acto133Model();
			
			String datosDeRetiroDeSolicitante="";
			if(cN!=null && !cN.isEmpty()){
				datosDeRetiroDeSolicitante+="NOMBRE(S) TESTADOR: "+cN;
			}
			//="",fechaDep="",nActa="",rN="",rAP="",rAM=""
			if(fCredencial!=null && !fCredencial.isEmpty()){
				datosDeRetiroDeSolicitante+=", FOLIO CREDENCIAL INE "+fCredencial;
			}
			if(fechaDep!=null && !fechaDep.isEmpty()){
				datosDeRetiroDeSolicitante+=", FECHA DE DEPOSITO "+fechaDep;
			}
			if(nActa!=null && !nActa.isEmpty()){
				datosDeRetiroDeSolicitante+=", NÚMERO DE ACTA "+nActa;
			}
			if(rN!=null && !rN.isEmpty() ){
				datosDeRetiroDeSolicitante+=", NOMBRE DEL REGISTRADOR "+rN ;
			}
			a133.setSolicitante(nSol);
			a133.setDatosDeRetiroDeSolicitante(datosDeRetiroDeSolicitante+"\n MOTIVO: "+motivo);
			a133.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD: \n"+autoridad);
			a133.setObservaciones(observaciones);
			ArrayList<Acto133Model> a133s=new ArrayList<Acto133Model>();
			a133s.add(a133);
			resultado.setActo133Models(a133s);		
		}
	
		return resultado;
	}
	private ActoPublicitarioVO busquedaPorActo105(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
		
		
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;
	
		String tipoDeDocumento="",autoridad="";

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");

		String poderdanteN="";
		String apoderadoN="";

		String 
		vigencia="",tipoDePoder="",obs="",
		año="",mes="",dia="",folio="";

		Boolean afecta=false;





		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(id){
//---------------------DATOS DEL PODERDANTE------------------------------------------------------		
				case 252://poderdante NOMBRE
					poderdanteN="\n"+c.getValor().trim();
				break;
				case 253://poderdante AP
					poderdanteN+=" "+c.getValor().trim();
				break;
				case 775://poderdante AM
					poderdanteN+=" "+c.getValor().trim();
				break;

//---------------------DATOS DEL APODERADO------------------------------------------------------

				case 440://APODERADO
					apoderadoN="\n"+c.getValor().trim();
				break;
				case 441://APODERADO AP
					apoderadoN+=" "+c.getValor().trim();
				break;
				case 452://APODERADO AM
					apoderadoN+=" "+c.getValor().trim();
				break;
//--------------------- AFECTA FOLIO INMUEBLES EL PODER?------------------------------------------------------
				case 626:
					afecta=Boolean.parseBoolean(c.getValor());
				break;
			
//--------------------- TTIPO(S) DE PODER (ES)------------------------------------------------------
				case 448:
					tipoDePoder=boletaRegistralService.obtenerValor(c);
				break;
//--------------------- VIGENCIA------------------------------------------------------
				case 449: //año
					año = boletaRegistralService.obtenerValor(c);
				break;
				case 450: //mes
					mes = boletaRegistralService.obtenerValor(c);
				break;
				case 451://dia
					dia = boletaRegistralService.obtenerValor(c);
				break;
//--------------------- OBSERVACIONES------------------------------------------------------				
				case 431:
					obs=boletaRegistralService.obtenerValor(c);
				break;
				case 96:
					folio = boletaRegistralService.obtenerValor(c);
				break;			
			}
			
		switch(moduloId){
			case 1501:
				autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
			break;
			case 501:
				tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
			break;
		}

		}//fin busqueda
	
		if( actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}	
			}		
		
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			if(!año.isEmpty()){
				vigencia+=" AÑO(S): "+año;
			}
			if(!mes.isEmpty()){
				vigencia+=", MES: "+mes;
			}
			if(!dia.isEmpty()){
				vigencia+=", DIA(S): "+dia;
			}
			Acto105Model a105=new Acto105Model();
			a105.setNuevoPoderdante(actoPublicitarioService.mostrarDetalle(actoPredioCampos, 146));//146
			a105.setPoderdante(poderdanteN);
			a105.setObs(obs);
			if(!vigencia.isEmpty()){
				a105.setTipoDePoder(tipoDePoder+", VIGENCIA "+vigencia);
			}else{
				a105.setTipoDePoder(tipoDePoder);
			}
			
			a105.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			
			if(afecta){a105.setAfecta("AFECTA FOLIO INMUEBLES EL PODER? SI, FOLIOS QUE AFECTA EL PODER:"+folio);}
			else{a105.setAfecta("" );}
			ArrayList<Acto105Model> a105s=new ArrayList<Acto105Model>();
			a105s.add(a105);
			resultado.setActo105Models(a105s);		
		}
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo106(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");

		String poderdanteN="";

		String tipoDePoder="",obs="",folio="";	
				
		String tipoDeDocumento="",autoridad="";

		Boolean afecta=false;

	

		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			switch(id){
			
			//---------------------DATOS DEL PODERDANTE------------------------------------------------------		
			case 252://poderdante NOMBRE
				poderdanteN="\n"+c.getValor().trim();
			break;
			case 253://poderdante AP
				poderdanteN+=" "+c.getValor().trim();
			break;
			case 775://poderdante AM
				poderdanteN+=" "+c.getValor().trim();
			break;

//--------------------- AFECTA FOLIO INMUEBLES EL PODER?------------------------------------------------------
				case 626:
					afecta=Boolean.parseBoolean(c.getValor());
				break;
			
//--------------------- TTIPO(S) DE PODER (ES)------------------------------------------------------
				case 448:
					tipoDePoder=boletaRegistralService.obtenerValor(c);
				break;
//--------------------- OBSERVACIONES------------------------------------------------------				
				case 431:
					obs=boletaRegistralService.obtenerValor(c);
				break;
				case 96:
					folio = boletaRegistralService.obtenerValor(c);
				break;
}
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
		
		}

			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}	
			}		
			
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
		
			Acto106Model a106=new Acto106Model();
			a106.setNuevoPoderdante(actoPublicitarioService.mostrarDetalle(actoPredioCampos, 146));
			a106.setPoderdante(poderdanteN);
			a106.setObs(obs);
			a106.setTipoDePoder(tipoDePoder);
			a106.setTipoDeModificacion(actoPublicitarioService.mostrarDetalle(actoPredioCampos, 147));
			a106.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			
			if(afecta){a106.setAfecta("AFECTA FOLIO INMUEBLES EL PODER? SI, FOLIOS QUE AFECTA EL PODER:"+folio);}
			else{a106.setAfecta("" );}
			ArrayList<Acto106Model> a106s=new ArrayList<Acto106Model>();
			a106s.add(a106);
			resultado.setActo106Models(a106s);		
		
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo107(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
		
		
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;
		

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");

		String poderdanteN="";
		String apoderadoN="";
		String tipoDeDocumento="",autoridad="",folio="";

		String 
		tipoDeRevocacion="",tipoDePoder="",obs="",
		motivo="";

		Boolean afecta=false;
	
		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			switch(id){
//---------------------DATOS DEL PODERDANTE------------------------------------------------------		
				case 252://poderdante NOMBRE
					poderdanteN+= "\n"+c.getValor().trim();
				break;
				case 253://poderdante AP
					poderdanteN+= " "+c.getValor().trim();
				break;
				case 775://poderdante AM
					poderdanteN+= " "+c.getValor().trim();
				break;

//---------------------DATOS DEL PODERDANTE------------------------------------------------------

				case 440://APODERADO
					apoderadoN+= "\n"+c.getValor().trim();
				break;
				case 441://APODERADO AP
					apoderadoN+= " "+c.getValor().trim();
				break;
				case 452://APODERADO AM
					apoderadoN+= " "+c.getValor().trim();
				break;
//--------------------- AFECTA FOLIO INMUEBLES EL PODER?------------------------------------------------------
				case 626:
					afecta=Boolean.parseBoolean(c.getValor());
				break;
			
//--------------------- TTIPO(S) DE PODER (ES)------------------------------------------------------
				case 448:
					tipoDePoder=boletaRegistralService.obtenerValor(c);
				break;
//--------------------- TIPO DE REVOCACION PODER------------------------------------------------------				
				case 442:
					tipoDeRevocacion=boletaRegistralService.obtenerValor(c);
				break;
//--------------------- OBSERVACIONES------------------------------------------------------				
				case 431:
					obs=boletaRegistralService.obtenerValor(c);
				break;
//--------------------- MOTIVO------------------------------------------------------				
				case 20816:
					motivo=boletaRegistralService.obtenerValor(c);
				break;
				case 96:
					folio = boletaRegistralService.obtenerValor(c);
				break;
			}
			
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
			
		}
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}	
			}		
			
				resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			
			Acto107Model a107=new Acto107Model();
			a107.setNuevoPoderdante(apoderadoN);
			a107.setPoderdante(poderdanteN);
			a107.setObs(obs);
			a107.setTipoDePoder(tipoDePoder);
			a107.setTipoDeRevocacion(tipoDeRevocacion+", MOTIVO: "+motivo);
			a107.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			
			if(afecta){a107.setAfecta("AFECTA FOLIO INMUEBLES EL PODER? SI, FOLIOS QUE AFECTA EL PODER:"+folio);}
			else{a107.setAfecta("" );}
			ArrayList<Acto107Model> a107s=new ArrayList<Acto107Model>();
			a107s.add(a107);
			resultado.setActo107Models(a107s);		
		}
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo132(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
				
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");

		String nSol="";
		String testN="";
		String regN="";
		String obs="",
		datosDeRevocacion="";

		String tipoDeDocumento="",autoridad="";
	
		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			int moduloid =c.getModuloCampo().getModulo().getId().intValue();
			switch(id){
//---------------------TESTADOR------------------------------------------------------		

				case 601://
					testN+="\n"+c.getValor().trim();
				break;
				case 602:// AP
					testN+=" "+c.getValor().trim();
				break;
				case 603:// AM
					testN+=" "+c.getValor().trim();
				break;

//---------------------SOLICITANTE------------------------------------------------------
				case 252://252 nSol
					nSol+="\n"+c.getValor().trim();
				break;
				case 253://253 ap Sol
					nSol+=" "+c.getValor().trim();
				break;
				case 775://775 am sol
					nSol+=" "+c.getValor().trim();
				break;
//---------------------REGISTRADOR------------------------------------------------------
				case 618:
					regN+="\n"+c.getValor().trim();
				break;
				case 619:
					regN+=" "+c.getValor().trim();
				break;
				case 620:
					regN+=" "+c.getValor().trim();
				break;			

//--------------------- OBSERVACIONES------------------------------------------------------				
				case 431:
					obs=boletaRegistralService.obtenerValor(c);
				break;
			}
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}

			switch(moduloid){
				case 181:
				Integer modo =c.getModuloCampo().getInd_impresion();
				if(modo!=null){
					switch(modo){
						case 1:
							if(datosDeRevocacion.endsWith(" ")){
								datosDeRevocacion+=", "+c.getModuloCampo().getEtiqueta()+": "+boletaRegistralService.obtenerValor(c)+",";
							}else{
								datosDeRevocacion+=" "+c.getModuloCampo().getEtiqueta()+": "+boletaRegistralService.obtenerValor(c)+",";
							}
							
						break;
						case 2:
							if(datosDeRevocacion.endsWith(",")){
							datosDeRevocacion = boletaRegistralService.quitarUltimoCaracter(datosDeRevocacion);
							datosDeRevocacion+=" "+boletaRegistralService.obtenerValor(c)+",";
						}else{
							datosDeRevocacion+=" "+boletaRegistralService.obtenerValor(c)+", ";
						}
							
						break;

					}
				}else{
					datosDeRevocacion+=" "+c.getModuloCampo().getEtiqueta()+": "+boletaRegistralService.obtenerValor(c)+",";
				}					
				break;
			}

			
		}//fin busqueda
		if(actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}	
			}		
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
								
			Acto132Model a132=new Acto132Model();
			a132.setSolicitante(nSol);
			a132.setTestador(testN);
			a132.setDatosDeRevocacion(boletaRegistralService.quitarUltimoCaracter(datosDeRevocacion));
			a132.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			
			a132.setObs(obs);

			ArrayList<Acto132Model> a132s=new ArrayList<Acto132Model>();
			a132s.add(a132);
			resultado.setActo132Models(a132s);		
		}
		return resultado;
	}

	private ActoPublicitarioVO busquedaPorActo134(List<ActoPredioCampo> actoPredioCampos,ActoPredio actoPredio){
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;
		Boolean nombreEncontrado = false;

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");

		String cujusN="";
		String testigoN="";

		String tipoDeDocumento="",autoridad="";
		String observaciones="";

		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			switch(id){
				case 561://CUJUS NOMBRE
					cujusN+= "\n"+c.getValor().trim();
				break;
				case 562://CUJUS AP
					cujusN+= " "+c.getValor().trim();
				break;
				case 563://CUJUS AM
					cujusN+= " "+c.getValor().trim();
				break;
				case 579://testigo NOMBRE
					testigoN+= "\n"+c.getValor().trim();
				break;
				case 580://testigo AP
					testigoN+= " "+c.getValor().trim();
				break;
				case 581://testigo AM
					testigoN+= " "+c.getValor().trim();
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;
			}
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}

		}
		
		if(nombreEncontrado || actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}	
			}		
			
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
		
			Acto134Model a134=new Acto134Model();
			a134.setTestador(cujusN);
			a134.setTestigo(testigoN);
			a134.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			a134.setObservaciones(observaciones);
			ArrayList<Acto134Model> a134s=new ArrayList<Acto134Model>();
			a134s.add(a134);
			resultado.setActo134Models(a134s);		
		}
		return resultado;
	}
	private ActoPublicitarioVO busquedaPorActo242(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Acto a1 = null;
		Prelacion p = null;
		Boolean nombreEncontrado = false;

		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");

		String cujusN="";
		String tipoDeDocumento="",autoridad="";

		
		String hdoN="";
		String nAlbacea ="";
	
		String observaciones="";
	

		for(ActoPredioCampo c:actoPredioCampos){
			a1 = c.getActoPredio().getActo();									
			int id =c.getModuloCampo().getId().intValue();
			switch(id){
				case 561://CUJUS NOMBRE
					cujusN+= "\n"+c.getValor().trim();
				break;
				case 562://CUJUS AP
					cujusN+= " "+c.getValor().trim();
				break;
				case 563://CUJUS AM
					cujusN+= " "+c.getValor().trim();
				break;

				case 1300:// Hdos
					hdoN += "\n"+c.getValor().trim();
				break;
				case 476:
					hdoN += " "+ c.getValor().trim();
				break;
				case 477:
					hdoN += " "+ c.getValor().trim();
				break;

					//ALBACEA
				case 20742://ALBACEA NOMBRE
					nAlbacea += "\n"+ c.getValor().trim();
				break;
				case 20743://ALBACEA PRIMER APELLIDO
					nAlbacea += " "+ c.getValor().trim();
				break;
				case 20744://SEGUNDO APELLIDO	
					nAlbacea += " "+ c.getValor().trim();
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;
			}
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
		
		}

		if( actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}
			}		
		
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
		
			Acto242Model a242=new Acto242Model();
			a242.setTestador(cujusN);
			a242.setAlbacea(nAlbacea);
			a242.setHeredero(hdoN);	
			a242.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			a242.setObservaciones(observaciones);
			ArrayList<Acto242Model> a242s=new ArrayList<Acto242Model>();
			a242s.add(a242);
			resultado.setActo242Models(a242s);		
		}
		return resultado;
	}
	private ActoPublicitarioVO busquedaPorActo244(List<ActoPredioCampo> actoPredioCampos, ActoPredio actoPredio){

		String proN="",fechaNacimiento="",rfc="",curp="",eCivil="",nacionalidad="",edad="",actuaPor="";
		String denominacion="",lotes="",manzanas="",usoDeSuelo="",superficie="",unidadDeMedida="",datosDeResolucion="",tActoModificatorio="",
		nActuaPor="",apActuaPor="",amActuaPor="";
		CampoValores cv = null;
		String tipoDeDocumento="",autoridad="";
		String observaciones="";
	
		Acto a1 = null;
		SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
		ActoPublicitarioVO resultado = null; 
		ActoPublicitario aPublicitario = null;
		Prelacion p = null;
		
		for(ActoPredioCampo c:actoPredioCampos){
			//System.out.println("Procesando Acto Predio"+c.getActoPredio().getId());
			a1 = c.getActoPredio().getActo();
			int id =c.getModuloCampo().getId().intValue();
			switch(id){
				case 843:// PROMOVENTE
					proN += "\n"+c.getValor().trim();
				break;
				case 844:
					proN += " "+ c.getValor().trim();
				break;
				case 845:
					proN += " "+ c.getValor().trim();
				break;
				case 846:
					fechaNacimiento = c.getValor().trim();
				break;
				case 847:
					rfc = c.getValor().trim();
				break;
				case 848:
					curp = c.getValor().trim();
				break;
				case 849:
					cv=campoValorService.findOne(Long.valueOf(c.getValor()));
					eCivil=cv.getNombre();
				break;
				case 851:
					nacionalidad=nacionalidadService.findOne(Long.valueOf(c.getValor())).getNombre();
				break;
				case 852:
					cv=campoValorService.findOne(Long.valueOf(c.getValor()));
					edad=cv.getNombre();
				break;
				case 853:
					cv=campoValorService.findOne(Long.valueOf(c.getValor()));
					actuaPor=cv.getNombre();
				break;

				case 20789://TIPO DE ACTO MODIFICATORIO
					cv=campoValorService.findOne(Long.valueOf(c.getValor()));
					tActoModificatorio=cv.getNombre();
				break;
				case 20781://DENOMINACIÓN 
					denominacion = c.getValor();					
				break;
				case 20782://lotes
					lotes = c.getValor();					
				break;
				case 20783://manzanas
					manzanas = c.getValor();					
				break;
				case 20784://usoDeSuelo
					cv = campoValorService.findOne(Long.valueOf(c.getValor()));
					usoDeSuelo = cv.getNombre();				
				break;
				case 20785://superficie					
					superficie = c.getValor();		
				break;
				case 20786://unidadDeMedida
					cv = campoValorService.findOne(Long.valueOf(c.getValor()));
					unidadDeMedida = cv.getNombre();				
				break;
			
				case 20787:
					datosDeResolucion+= ", DATOS DE LA RESOLUCIÓN: "+c.getValor();                               
				break;

				case 5021:
					datosDeResolucion+=", NUMERO DE AUTORIZACION: "+c.getValor();
				break;

				case 5022:
					datosDeResolucion+=", FECHA DE AUTORIZACIÓN: "+this.boletaRegistralService.obtenerValor(c);
				break;

				case 5023:
					datosDeResolucion+=", CON LA COMPARECENCIA DE: "+c.getValor();
				break;

				case 5024:
					datosDeResolucion+="AUTORIDAD QUE OTORGA: "+c.getValor();
				break;
				case 75:
					nActuaPor=boletaRegistralService.obtenerValor(c);
				break;
				case 188:
					apActuaPor=boletaRegistralService.obtenerValor(c);
				break;
				case 141:
					amActuaPor=boletaRegistralService.obtenerValor(c);
				break;
				case 431: //OBSERRVACIONES
					observaciones = c.getValor();
				break;

			}
			int moduloId=c.getModuloCampo().getModulo().getId().intValue();

			switch(moduloId){
				case 1501:
					autoridad+=" "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
				case 501:
					tipoDeDocumento+=" "+c.getModuloCampo().getEtiqueta()+": "+this.boletaRegistralService.obtenerValor(c)+",";
				break;
			}
		}//fin busqueda
	
		if( actoPredio!=null){
			resultado = new ActoPublicitarioVO();
			if(a1 != null){
				p = a1.getPrelacion();
				aPublicitario=actoPublicitarioRepository.findByLastNumero(a1.getId());
				resultado.setEntrada(""+p.getConsecutivo());
				resultado.setOficina(p.getOficina().getNombre());
				resultado.setFechaIngreso(""+formateador.format(p.getFechaIngreso()));
				resultado.setFechaFirmaR(""+formateador.format(a1.getFechaRegistro())); 
				resultado.setnActoPublicitario(""+aPublicitario.getNumero());	
				if(aPublicitario.getNum_folio_real()!=null){
					resultado.setNoFolioReal(""+aPublicitario.getNum_folio_real());		
				}	
			}										
			
			resultado.setTipoActoPublicitario(actoPredio.getActo().getTipoActo().getNombre());
			
			String datosDelPromovente = "";
		
				datosDelPromovente +=proN;
		
		
			if(fechaNacimiento!=null && !fechaNacimiento.isEmpty()){
				datosDelPromovente+=", DE FECHA DE NACIMIENTO "+fechaNacimiento;
			}
			if(rfc!=null && !rfc.isEmpty()){
				datosDelPromovente+=", RFC "+rfc;
			}
			if(curp!=null && !curp.isEmpty()){
				datosDelPromovente+=", CURP "+curp;
			}
			if(eCivil!=null && !eCivil.isEmpty()){
				datosDelPromovente+=", ESTADO CIVIL "+eCivil;
			}
			if(nacionalidad!=null && !nacionalidad.isEmpty()){
				datosDelPromovente+=", NACIONALIDAD "+nacionalidad;
			}
			if(edad!=null && !edad.isEmpty()){
				datosDelPromovente+=", EDAD "+edad;
			}
			if(actuaPor!=null && !actuaPor.isEmpty()){
				datosDelPromovente+=", ACTUA  "+actuaPor+" "+nActuaPor+" "+apActuaPor+" "+amActuaPor;
			}
			
			
			String datosDelInmueble = "";
			if(denominacion!=null && !denominacion.isEmpty()){
				datosDelInmueble+="DENOMINACION "+denominacion;
			}
			if(lotes!=null && !lotes.isEmpty()){
				datosDelInmueble+=", LOTES "+lotes;
			}
			if(manzanas!=null && !manzanas.isEmpty()){
				datosDelInmueble+=", MANZANAS "+manzanas;
			}
			if(usoDeSuelo!=null && !usoDeSuelo.isEmpty()){
				datosDelInmueble+=", USO DE SUELO "+usoDeSuelo;
			}
			if(superficie!=null && !superficie.isEmpty()){
				datosDelInmueble+=", SUPERFICIE "+superficie;
			}
			if(unidadDeMedida!=null && !unidadDeMedida.isEmpty()){
				datosDelInmueble+=", UNIDAD DE MEDIDA "+unidadDeMedida;
			}

			Acto244Model a244=new Acto244Model();
			a244.setDatosDelPromovente(datosDelPromovente);
			a244.setDatosDelInmueble(datosDelInmueble);
			a244.setTipoDeActoModificatorio(tActoModificatorio);
			a244.setDatosDeResolucion(datosDeResolucion);
			a244.setTipoDeDocumento(this.boletaRegistralService.quitarUltimoCaracter(tipoDeDocumento)
					+"\n\n AUTORIDAD:\n"+autoridad);
			a244.setObservaciones(observaciones);
	
			ArrayList<Acto244Model> a244s=new ArrayList<Acto244Model>();
			a244s.add(a244);
			resultado.setActo244Models(a244s);		
		}
	
		return resultado;
	}



	public List<BoletinDenegadoVO> getBoletinDenegado(Date fecha, Oficina oficina) {
	//2018,12,27
		ArrayList<BoletinDenegadoVO> boletinDenegadoVOs= new ArrayList<>();
		BoletinDenegadoVO boletinD =new BoletinDenegadoVO();
	    SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy");
	    SimpleDateFormat anio =new SimpleDateFormat("yyyy");
	    SimpleDateFormat mes =new SimpleDateFormat("MM");
		SimpleDateFormat dia =new SimpleDateFormat("dd");
		Integer a = Integer.parseInt(anio.format(fecha));
		Integer m = Integer.parseInt(mes.format(fecha));
		Integer d = Integer.parseInt(dia.format(fecha));
		List<Acto> actosR=actoRepository.findAllByActoFechaRechazo(a,m,d,oficina.getId());
		List<RecursoInconformidad> recursosDenegados =  recursoInconformidadRepository.findAllByFechaAndOficinaDenegados(a, m, d, oficina.getId(),Constantes.TipoInconformidad.DENEGACION.getIdTipoInconformidad());
		
		if((actosR != null && !actosR.isEmpty()) || (recursosDenegados!=null && !recursosDenegados.isEmpty()) ) {
			ArrayList<BoletinDenegadoModel> boletinDenegadoModels=new ArrayList<>();
			if(actosR != null && !actosR.isEmpty()) 
			{
			for(Acto ac:actosR){				
				BoletinDenegadoModel bdm=new BoletinDenegadoModel();
				bdm.setMotivo(ac.getMotivo()!=null?ac.getMotivo().getNombre():"");								
				bdm.setObservacion(ac.getObservacionesMotivo()!=null ? CommonUtil.decodeValue(ac.getObservacionesMotivo()): "");				
				bdm.setFundamento(ac.getTipoRechazo()!=null ? ac.getTipoRechazo().getNombre() : "");
				bdm.setFechaDeNegacion(format.format(ac.getFechaRechazo()));
				bdm.setEntrada(""+ac.getPrelacion().getConsecutivo());
				List<ActoPredio> aps=actoPredioRepository.findAllByActo(ac);
				for(ActoPredio acp:aps ) {
					if(acp.getPredio()!=null) {						
						bdm.setFolio(""+acp.getPredio().getNoFolio());
					}else {
						bdm.setFolio(" ");
					}
				}	
				//bdm.setFechaTermino(buildFechaRechazo(fecha,"denegado"));
				boletinDenegadoModels.add(bdm);		
			}
			}
			if(recursosDenegados!=null && !recursosDenegados.isEmpty()) {
				for(RecursoInconformidad item: recursosDenegados) {
					BoletinDenegadoModel bdm=new BoletinDenegadoModel();
					bdm.setMotivo(item.getActo().getTipoActo().getNombre()+" ENTRADA: "+item.getPrelacion().getConsecutivo());
					bdm.setFundamento(item.getFundamento());
					bdm.setFechaDeNegacion(format.format(item.getActo().getPrelacion().getFechaEnvioFirma()));
					bdm.setEntrada(item.getActo().getPrelacion().getConsecutivo().toString());
					Optional<ActoPredio> actoPredio  = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(item.getActo().getId());
					if(actoPredio.isPresent()) {
						if(actoPredio.get().getPredio()!=null) {
							bdm.setFolio(actoPredio.get().getPredio().getNoFolio().toString());
						}
						if(actoPredio.get().getPersonaJuridica()!=null) {
							bdm.setFolio(actoPredio.get().getPersonaJuridica().getNoFolio().toString());
						}
					}
					
					boletinDenegadoModels.add(bdm);		
				}
			}
			
			boletinD.setFecha(format.format(fecha));
			boletinD.setOficina(oficina.getNombre());
			boletinD.setDenegados(boletinDenegadoModels);
			boletinDenegadoVOs.add(boletinD);
		}else {
			boletinD.setFecha(format.format(fecha));
			boletinD.setOficina(oficina.getNombre());
			boletinD.setNoRegistros("NO SE ENCONTRARÓN \n REGISTROS");
			boletinDenegadoVOs.add(boletinD);
		}
	
		
	/* 	for(int j=0; j<boletinDenegadoModels.size();j++){
			String consecutivo = boletinDenegadoModels.get(j).getEntrada();
			for(int i=0; i<boletinDenegadoModels.size();i++){				
				if(i>j){
					if(consecutivo.equals(boletinDenegadoModels.get(i).getEntrada())){
						boletinDenegadoModels.remove(i);
					}
				}
			}

		} */
		return boletinDenegadoVOs;
	}

	public List<BoletinDenegadoVO> getBoletinSuspendido(Date fecha,Oficina oficina) {
		Status status = statusRepository.findOne(Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus());

		ArrayList<BoletinDenegadoVO> boletinDenegadoList= new ArrayList<>();
		BoletinDenegadoVO boletinD =new BoletinDenegadoVO();

		ArrayList<BoletinSuspensionModel> boletinSuspensionList=new ArrayList<>();
		
		SimpleDateFormat format =new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat format1 =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    SimpleDateFormat anio =new SimpleDateFormat("yyyy");
	    SimpleDateFormat mes =new SimpleDateFormat("MM");
		SimpleDateFormat dia =new SimpleDateFormat("dd");
		Integer a = Integer.parseInt(anio.format(fecha));
		Integer m = Integer.parseInt(mes.format(fecha));
		Integer d = Integer.parseInt(dia.format(fecha));
		List<SuspensionFirma> supenSuspensionFirmas = suspensionFirmaRepository.findAllBySuspensionPrelacionOficinaAndSuspensionPrelacionStatus(oficina,status);
		List<RecursoInconformidad> recursosDenegados = recursoInconformidadRepository.findAllByFechaAndOficinaDenegados(a, m, d, oficina.getId(), Constantes.TipoInconformidad.SUSPENCION.getIdTipoInconformidad());
		
		String fechaBusqueda = format.format(fecha);

		boletinD.setFecha(format.format(fecha));
		boletinD.setOficina(oficina.getNombre());

		if((supenSuspensionFirmas!=null && !supenSuspensionFirmas.isEmpty()) || (recursosDenegados!=null && !recursosDenegados.isEmpty())){
		if(supenSuspensionFirmas!=null && !supenSuspensionFirmas.isEmpty()){
			for(SuspensionFirma susFirma : supenSuspensionFirmas){
				Suspension s = susFirma.getSuspension();
				Prelacion p = s.getPrelacion();
				String fechaSuspension=format.format(susFirma.getFechaFirma());
				if(s.getEstatusSuspension()==2L && fechaBusqueda.equals(fechaSuspension)){
					log.info("Entrada:"+p.getConsecutivo()+" Estatus:"+p.getStatus()+" Fecha Suspension:"+s.getFechaSuspension());
					log.info("Fecha Firma:"+susFirma.getFechaFirma());
					BoletinSuspensionModel bsm =new BoletinSuspensionModel();
					bsm.setEntrada(""+p.getConsecutivo());
					bsm.setFechaFirma(""+format1.format(susFirma.getFechaFirma()));
					bsm.setFechaDeSuspension(""+format.format(s.getFechaSuspension()));
					bsm.setFechaTermino(buildFechaRechazo(s.getFechaSuspension(),"suspension"));

					List<PrelacionPredio> pps=prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(p.getId());
					String folios= "";
					if(pps!=null && !pps.isEmpty()) {
						for(PrelacionPredio pp:pps) {
							if(pp.getTipoFolio()!=null) {
								
								switch(pp.getTipoFolio().getId().intValue()) {
									case 1: //PERSONAS MORALES CIVILES
										if(folios.length()<=0)
											folios += pp.getPrelacion().getArea().getNombre()+": "+pp.getPersonaJuridica().getNoFolio();
										else
											folios += ", "+pp.getPersonaJuridica().getNoFolio();
										
									break;
									case 4: //INMOBILIARIO
										if(folios.length()<=0)
											 folios += pp.getPrelacion().getArea().getNombre()+": " +pp.getPredio().getNoFolio();
										else
										     folios += ", "+pp.getPredio().getNoFolio();
										
									break;
								}
							}
						}						
						bsm.setFolio(folios);
					}
					else { bsm.setFolio(""); }
					
					Bitacora bitacora = bitacoraRepository.findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(p.getId(),
				p.getStatus().getId());
					if(bitacora!=null){
						if(bitacora.getMotivo()!=null) {
							bsm.setMotivo(bitacora.getMotivo().getNombre());
						}
						if(bitacora.getTipoRechazo()!=null) {
							bsm.setFundamento(bitacora.getTipoRechazo().getNombre());
						}
						
						bsm.setObservaciones(bitacora.getObservaciones()!=null?bitacora.getObservaciones():"");
					}
					else { bsm.setMotivo(""); 
					bsm.setFundamento("");
					bsm.setObservaciones("");
					}  						

					boletinSuspensionList.add(bsm);
				}
			}
				
			if(recursosDenegados!=null && !recursosDenegados.isEmpty()) {
					for(RecursoInconformidad item: recursosDenegados) {
						
						BoletinSuspensionModel bsm =new BoletinSuspensionModel();
						
						bsm.setEntrada(""+item.getActo().getPrelacion().getConsecutivo());
						bsm.setFechaFirma(""+format1.format(item.getActo().getPrelacion().getFechaEnvioFirma()));


						List<PrelacionPredio> pps=prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(item.getPrelacion().getId());
						String folios= "";
						if(pps!=null && !pps.isEmpty()) {
							for(PrelacionPredio pp:pps) {
								if(pp.getTipoFolio()!=null) {
									
									switch(pp.getTipoFolio().getId().intValue()) {
										case 1: //PERSONAS MORALES CIVILES
											folios=folios+pp.getPersonaJuridica().getNoFolio()+" ";
										break;
										case 4: //INMOBILIARIO										
											folios=folios+pp.getPredio().getNoFolio()+" ";
										break;
									}
								}
							}						
							bsm.setFolio(folios);
						}
						else { bsm.setFolio(""); }

						bsm.setMotivo(item.getActo().getTipoActo().getNombre()+" ENTRADA: "+item.getPrelacion().getConsecutivo());
						bsm.setFundamento(item.getFundamento());					
						
						boletinSuspensionList.add(bsm);
					}	
			}
				
			if (boletinSuspensionList!=null && !boletinSuspensionList.isEmpty()) {
				boletinD.setSuspendidos(boletinSuspensionList);
			} else { //NO SE ENCONTRAR SUSPENSIONES
				boletinD.setSuspendidos(null);						
				boletinD.setNoRegistros("NO SE ENCONTRARON \n REGISTROS");
			}
			boletinDenegadoList.add(boletinD);		
		  }
		
	

		} else {  //NO SE ENCONTRAR SUSPENSIONES
			boletinD.setSuspendidos(null);			
			boletinD.setNoRegistros("NO SE ENCONTRARON \n REGISTROS");
			boletinDenegadoList.add(boletinD);
		}	

	return boletinDenegadoList;
}
	
	private String buildFechaRechazo( Date d, String tipo) {
        try {
			int diaHabil =0;
			if(tipo.equals("denegado")){
				 diaHabil = Integer.parseInt(parametroRepository.findByCve("DIAS_RECHAZO").getValor());
			}
			if(tipo.equals("suspension")){
				diaHabil = Integer.parseInt(parametroRepository.findByCve("DIAS_SUSPENSION").getValor());
				diaHabil = diaHabil-1;
			}
			Calendar calendar= null;
			System.out.println(" dia habil "+diaHabil);

            for (int dia = 1; dia <= diaHabil; dia++) {
                //busacar sabados y domingos
                calendar = sumarFecha(d, dia);
                int nDia = getNumeroDia(calendar.getTime());
                switch (nDia) {
                    case 1:
                        diaHabil += 1;
                        break;
                    case 7:
                        diaHabil += 1;
                        break;
                    default:
                        if (esDiaOficial(calendar.getTime())) {
                            diaHabil += 1;
                        }
                    break;

                }
                
			}			
			return new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
			

        } catch (Exception ex) {
		  ex.printStackTrace();
		  return "";
        }
    }

	private Calendar sumarFecha(Date fecha, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); 
        calendar.add(Calendar.DAY_OF_YEAR, d);  
        return calendar;
    }

    private int getNumeroDia(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

	private Boolean esDiaOficial(Date fecha) {
        boolean esDiaOficial = false;
        String sFecha = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
       // System.out.println("* " + sFecha);
        int año = Integer.parseInt(new SimpleDateFormat("yyyy").format(fecha));
        String diasOficiales[] = {
            "01/01/" + año,//enero
            "06/01/" + año,
            "05/02/" + año,//febrero
            "14/02/" + año,
            "24/02/" + año,
            "18/03/" + año,//marzo
            "18/04/" + año,//abril
            "19/04/" + año,
            "21/04/" + año,
            "30/04/" + año,
            "01/05/" + año,//mayo
            "05/05/" + año,
            "10/05/" + año,
            "16/06/" + año,//junio
            "16/09/" + año,//septmiebre
            "02/11/" + año,//noviembre
            "18/11/" + año,
            "12/12/" + año,//diciembre
            "24/12/" + año,
            "25/12/" + año
        };
        for (String f : diasOficiales) {
            if (sFecha.equals(f)) {
                int nDia = getNumeroDia(fecha);
                if (nDia != 7 && nDia != 1) {                   
                    esDiaOficial = true;
                }
            }
        }

        return esDiaOficial;
	}
	
	public List<PrelacionPredio>findPrelacionByPredioProcedeDe(Long noFolio){
		List<Predio> predios = predioRepository.findPredioByProcedeDeFolio(""+noFolio);
		ArrayList<PrelacionPredio> pres= new ArrayList<>();
		if(null!=predios){
			for(Predio p :predios) {

				List<PredioExtinto> prediosExtintos = predioExtintoRepository.findByPredioId(p.getId());

				log.debug("Predios Extintos: {}", prediosExtintos.size());

				if (CollectionUtils.isEmpty(prediosExtintos)) {

					List<PrelacionPredio> pps;
					pps = prelacionPredioRepository.findPrelacionPredioByPredioId(p.getId());
					for (PrelacionPredio pp : pps) {
						Prelacion pre = pp.getPrelacion();
						Long statusId = pre.getStatus().getId();
						if (statusId == 4 || statusId == 5 || statusId == 6) {
							pres.add(pp);
						}

					}
				}
			}
		}
		System.out.println("prelaciones{} "+pres);
		return pres;
	}
	
	public Date buscaFechaInscripcion(Long idActo ) {
		
		System.out.println("ID ACTO : " + idActo);
		
		String anio = "";
		String mes  = "";
		String dia  = "";
		
		
		List<ActoPredioCampo> actoPredcamp = actoPredioCampoRepository.findAllByActoPredio(actoPredioRepository.findActoPredioByLastVersion(idActo));
		
	
		
		for(ActoPredioCampo x : actoPredcamp) {
			if(Constantes.MODULO_CAMPO_ANIO_INSCRIPCION.equals(x.getModuloCampo().getId())    ) {
				anio = x.getValor();
			}
			if(Constantes.MODULO_CAMPO_MES_INSCRIPCION.equals(x.getModuloCampo().getId())    ) {
				mes = x.getValor();
			}
			if(Constantes.MODULO_CAMPO_DIA_INSCRIPCION.equals(x.getModuloCampo().getId())    ) {
				dia = x.getValor();
			}
		}
		Date fechaInscripcion = null;
		try {
			fechaInscripcion = new SimpleDateFormat("dd/MM/yyyy").parse(dia+"/"+UtilFecha.monthNameOrIdtoNumerMonth(mes)+"/"+anio);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		if(fechaInscripcion == null) {
			fechaInscripcion = actoRepository.getOne(idActo).getFechaRegistro();
		}
		
		return fechaInscripcion;
	}


	private Boolean esCertificado(Prelacion prelacion) {	//CV
		boolean esCertificado = false;
		for (Acto acto : prelacion.getActosParaPrelacions()) {
			if (acto.getTipoActo().getClasifActo().getId() == 9) {
				esCertificado = true;
			}
		}

		System.out.println("esCertificado"+esCertificado);
		return esCertificado;
	}
	

		
/* 	private void clearPrelacionRechazada(Prelacion prelacion) {	//CV
		// CERTIFICADOS O COPIAS
		if (prelacion.getArea().getId().equals(3L) || prelacion.getArea().getId().equals(4L)) {
			List<Certificado> certs = this.certificadoRepository.certificadoByPrelacionId(prelacion.getId());
			if (certs != null && certs.size() > 0) {
				this.certificadoService.delete(certs);
				// ELIMINA ARCHIVO SI SON COPIAS
				if (prelacion.getArea().getId().equals(4L) && certs.get(0).getRutaArchivo() != null
						&& !certs.get(0).getRutaArchivo().isEmpty())
					CommonUtil.deleteFileDirect(certs.get(0).getRutaArchivo());
			}

			if (prelacion.getArea().getId().equals(4L)) {
				HashSet<BusquedaResultado> result = this.busquedaResultadoService
						.getPrediosFromPrelacionId(prelacion.getId());
				result.forEach(x -> {
					if (x.getRutaArchivo() != null && !x.getRutaArchivo().isEmpty())
						CommonUtil.deleteFileDirect(x.getRutaArchivo());
					this.busquedaResultadoService.guardar(x);
					this.copiasService.deleteHojasSeleccionadas(x.getId());

				});
			}
 
		}
	}
	*/
	
	/**
	 * crea nueva prelacion con informacion de vo y setea los nuevos ids al vo para su regreso
	 */
	public PrelacionVO clonePrelacionIngreso(PrelacionVO vo) {
		Prelacion prelacion = prelacionRepository.findOne(vo.getId());
		
		Prelacion newPrelacion = new Prelacion();
		newPrelacion.setOficina(prelacion.getOficina());
		newPrelacion.setPrioridad(prelacion.getPrioridad());
		newPrelacion.setUsuarioSolicitan(prelacion.getUsuarioSolicitan());
		newPrelacion.setUsuarioVentanilla(prelacion.getUsuarioVentanilla());
		newPrelacion.setPrimerRegistro(prelacion.getPrimerRegistro());
		newPrelacion.setMarcarSuspensivo(prelacion.getMarcarSuspensivo());
		newPrelacion.setStatus(statusRepository.findOne(Constantes.Status.INGRESO_POR_VENTANILLA.getIdStatus()));
			
		prelacionRepository.save(newPrelacion);
		//set newId
		vo.setId(newPrelacion.getId());
		
		if(prelacion.getUsuarioSolicitan().getId() == 0) {
			PrelacionUsuarioDef newPrelacionUsuario = new PrelacionUsuarioDef();
			PrelacionUsuarioDef def = prelacionUsuarioDefRepository.findOneByPrelacionId(prelacion.getId());
			
			newPrelacionUsuario.setPrelacion(newPrelacion);
			newPrelacionUsuario.setMaterno(def.getMaterno());
			newPrelacionUsuario.setPaterno(def.getPaterno());
			newPrelacionUsuario.setNombre(def.getNombre());
			newPrelacionUsuario.setTelefono(def.getTelefono());
			newPrelacionUsuario.setValor(def.getValor());
			newPrelacionUsuario.setTipoIden(def.getTipoIden());
			newPrelacionUsuario.setSolicitudDevolucion(def.getSolicitudDevolucion());
			
			prelacionUsuarioDefRepository.save(newPrelacionUsuario);
		}
		
		if (vo.getActos()!=null) {
			for (Acto actoDeVO : vo.getActos()) {
				Acto newActo = this.cloneActo(actoDeVO.getId(), newPrelacion);
				//set newId
				actoDeVO.setId(newActo.getId());
			}	
		} else {
			//si no vienen en el objeto, se cargan de la base de datos
			List<Acto> actos = actoService.findAllByPrelacionOrderByOrdenAsc(prelacion.getId());
			if (actos!=null) {
				Acto[] actosArreglo = new Acto[actos.size()];
				int index=0;
				for (Acto acto: actos) {
					Acto newActo = this.cloneActo(acto.getId(), newPrelacion);
					actosArreglo[index] = newActo;
					index++;
				}
				vo.setActos(actosArreglo);
			}
		}
		
		if (vo.getServicios()!=null) {
			List<PrelacionServicio> prelacionServicios = prelacionServicioRepository.findByPrelacionId(prelacion.getId());
			for (PrelacionServicio prelacionServicio: prelacionServicios) {
				for (ServicioAndSubVO servicioDeVO : vo.getServicios()) {
					if (prelacionServicio.getServicio().getId().equals(servicioDeVO.getIdServicio())) {					
					
						PrelacionServicio newPrelacionServicio = new PrelacionServicio();
						newPrelacionServicio.setPrelacion(newPrelacion);
						newPrelacionServicio.setServicio(prelacionServicio.getServicio());
						
						prelacionServicioRepository.save(newPrelacionServicio);
						
						//set newId
						servicioDeVO.setId(newPrelacionServicio.getId());
					}
				}
			}
		}
		
		return vo;
	}
	
	private Acto cloneActo(Long actoId, Prelacion newPrelacion) {
		Acto acto = actoRepository.findOne(actoId);
		Acto newActo = new Acto();	
		newActo.setFechaRegistro(acto.getFechaRegistro());
		newActo.setModificable(acto.getModificable());
		newActo.setOrden(acto.getOrden());
		newActo.setProcesado(acto.isProcesado());
		newActo.setVersion(acto.getVersion());
		newActo.setStatusActo(acto.getStatusActo());
		newActo.setTipoActo(acto.getTipoActo());
		newActo.setVf(acto.getVf());
		newActo.setPrelacion(newPrelacion);
		
		actoRepository.save(newActo);	
		
		for (ActoPredio actoPredio : acto.getActoPrediosParaActos()) {
			ActoPredio newActoPredio = new ActoPredio();
			newActoPredio.setTipoEntrada(actoPredio.getTipoEntrada());
			newActoPredio.setVersion(actoPredio.getVersion());
			newActoPredio.setActo(newActo);
			
			actoPredioRepository.save(newActoPredio);
		
		}
		
		for (ActoDocumento actoDocumento : acto.getActoDocumentosParaActos()) {
			
			Documento newDocumento = new Documento();
			BeanUtils.copyProperties(actoDocumento.getDocumento(), newDocumento);
			newDocumento.setId(null);
			newDocumento.setActoDocumentosParaDocumentos(null);
			
			documentoRepository.save(newDocumento);
			
			ActoDocumento newActoDocumento = new ActoDocumento();
			newActoDocumento.setActo(newActo);
			newActoDocumento.setDocumento(newDocumento);
			newActoDocumento.setVersion(actoDocumento.getVersion());
			
			actoDocumentoRepository.save(newActoDocumento);
		}		
		return newActo;
	}
	
	public List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnioAndSubindice(Long oficinaId, Integer consecutivo, Integer anio, Long subindice) {
		return prelacionRepository.findAllByOficinaIdAndConsecutivoAndAnioAndSubindice(oficinaId, consecutivo, anio, subindice);
	}

	public List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnioAndSubindiceAndClave(Long oficinaId, Integer consecutivo, Integer anio, Long subindice, String clave) {
		return prelacionRepository.findAllByOficinaIdAndConsecutivoAndAnioAndSubindiceAndClaveConsulta(oficinaId, consecutivo, anio, subindice, clave);
	}

	public PrelacionActosVO findOneByOficinaIdAndConsecutivoAndAnioAndSubindice(Long oficinaId, Integer consecutivo, Integer anio, Long subindice) {
		PrelacionActosVO prelacionActosVO = new PrelacionActosVO();
		Prelacion prelacion = null;
		
		prelacion= prelacionRepository.findOneByOficinaIdAndConsecutivoAndAnioAndSubindice(oficinaId, consecutivo, anio, subindice);
		if(prelacion!=null){
			prelacionActosVO.setPrelacion(prelacion);
			if(prelacion.getId_entrada()==null && (prelacion.getArea().getId()==1 || prelacion.getArea().getId()==2)){  //Es Entrada de Sistema (No migrada) y ES de PROPIEDAD
				switch(prelacion.getStatus().getId().intValue()){
					case 7:  // LISTO PARA ENTREGAR
					case 8:  // ENTREGADO AL USUARIO
					case 10: // RECHAZADO
						prelacionActosVO.setActosVO(findbyEntrada(prelacion.getId()));
					break;
				}
				
			}
		}
		return prelacionActosVO;
	}
	
	public PrelacionDocumentosVO findDocumentosEntradaByOficinaIdAndConsecutivoAndAnioAndSubindice(Long oficinaId, Integer consecutivo, Integer anio, Long subindice) {
		log.info("IHM-PrelacionService.findDocumentosEntradaByOficinaIdAndConsecutivoAndAnioAndSubindice:");		
		PrelacionDocumentosVO prelacionDocumentosVO = new PrelacionDocumentosVO();
		List<RequisitoVO> listDocs = new ArrayList<RequisitoVO>();
		Prelacion prelacion = null;
		List<BitacoraDocumentoEntradaVO> listBitacoraVO = new ArrayList<BitacoraDocumentoEntradaVO>();
		
		prelacion= prelacionRepository.findOneByOficinaIdAndConsecutivoAndAnioAndSubindice(oficinaId, consecutivo, anio, subindice);
		if(prelacion!=null){
			prelacionDocumentosVO.setPrelacion(prelacion);			
			listDocs = findAllDocAdjuntosByPrelacionId(prelacion.getId());
			prelacionDocumentosVO.setRequisitoVO(listDocs);
			listBitacoraVO = getBitacoraDocumentoEntrada(prelacion.getId());
			prelacionDocumentosVO.setBitacoraVO(listBitacoraVO);
		}
		return prelacionDocumentosVO;
	}

	List<BitacoraDocumentoEntradaVO> getBitacoraDocumentoEntrada(Long prelacionId) {
		List<BitacoraDocumentoEntrada> listBitacoraDocumentoEntrada = new ArrayList<BitacoraDocumentoEntrada>();
		List<BitacoraDocumentoEntradaVO> listBitacoraVO = new ArrayList<BitacoraDocumentoEntradaVO>();

		listBitacoraDocumentoEntrada = bitacoraDocumentoEntradaService.findAllByPrelacionId(prelacionId);

		for (BitacoraDocumentoEntrada bde:listBitacoraDocumentoEntrada) {
			BitacoraDocumentoEntradaVO bdeVO = new BitacoraDocumentoEntradaVO();
			String nombreCompleto = "";
			bdeVO.setFecha(bde.getFecha());
			bdeVO.setAccion(bde.getAccion());
			String n = bde.getUsuario().getPersona().getNombre()!=null? bde.getUsuario().getPersona().getNombre() : "";
			String ap = bde.getUsuario().getPersona().getPaterno()!=null? bde.getUsuario().getPersona().getPaterno() : "";
			String am = bde.getUsuario().getPersona().getMaterno()!=null? bde.getUsuario().getPersona().getMaterno() : "";
			nombreCompleto = n + " " + ap + " " + am;
			bdeVO.setNomUsuario(nombreCompleto);
			bdeVO.setObservaciones(bde.getObservaciones());
			bdeVO.setNomDocumento(bde.getDocumento().getTipoDocumento().getNombre());
			bdeVO.setArchivo(bde.getArchivo());
			bdeVO.setEsAnexo(bde.getEs_anexo());
			listBitacoraVO.add(bdeVO);
		}
		return listBitacoraVO;
	}
	
	public List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnio(Long oficinaId, Integer consecutivo, Integer anio) {
		return prelacionRepository.findAllByOficinaIdAndConsecutivoAndAnio(oficinaId, consecutivo, anio);
	}

	public List<Prelacion> findAllByOficinaIdAndConsecutivoAndAnioAndClave(Long oficinaId, Integer consecutivo, Integer anio, String clave) {
		return prelacionRepository.findAllByOficinaIdAndConsecutivoAndAnioAndClaveConsulta(oficinaId, consecutivo, anio, clave);
	}

	public List<PrelacionActosVO> findPrelacionActosByFolio(BuscarFolioVO folioBuscar){		
		log.info(" Oficina:"+folioBuscar.getOficinaId()+
		" Folio:"+folioBuscar.getFolio()+
		" FolioRealAnte:"+folioBuscar.getFolioRealAnterior()+
		" Auxiliar:"+folioBuscar.getAuxiliar()
		);
		List<PrelacionActosVO> listEntradasActosVO = new ArrayList<PrelacionActosVO>();
		Oficina oficina = oficinaService.findById(folioBuscar.getOficinaId());
		Predio predio = new Predio();
		Long[] statusActoId = {Constantes.StatusActo.ACTIVO.getIdStatusActo(),
			Constantes.StatusActo.INVALIDO.getIdStatusActo()};

		if(folioBuscar.getFolioRealAnterior()>0 && folioBuscar.getAuxiliar()>0) {			
			predio = predioService.findPredioByNoFolioRealAndAuxiliarAndOficina(folioBuscar.getFolioRealAnterior(),folioBuscar.getAuxiliar(), oficina);
		}else if(folioBuscar.getAuxiliar()==0 && folioBuscar.getFolioRealAnterior()>0 ){
			predio = predioService.findByNoFolioRealAndOficina(folioBuscar.getFolioRealAnterior(), oficina);
		}else {
			predio = predioService.findByNoFolioAndOficina(folioBuscar.getFolio(), oficina);
		}
		if(predio!=null){
			log.info(" PredioId:"+predio.getId()+
			" Bloqueado:"+predio.getBloqueado()+
			" StatusActo:"+predio.getStatusActo().getNombre()+
			" Entradas Asociadas:"+predio.getPrelacionPrediosParaPredios().size()
			);
			// Validar Bloqueado
			// Validar Status_acto	
			
			Set<Acto> listActos = actoPredioRepository.findActosPredioByPredioAndStatusQuery(predio.getId(),statusActoId);
			Set<Prelacion> setPrelaciones = new HashSet<Prelacion>();
			List<Prelacion> listPrelaciones = new ArrayList<Prelacion>();
			
			for(Acto acto:listActos){				
				setPrelaciones.add(acto.getPrelacion());				
			}			
			log.info("listPRelaciones.size:"+setPrelaciones.size());
			listPrelaciones=setPrelaciones.stream().sorted(Comparator.comparingInt(Prelacion::getAnio).reversed()).collect(Collectors.toList());
			
			for(Prelacion p:listPrelaciones) {
				PrelacionActosVO pActosVO = new PrelacionActosVO();
				pActosVO.setPrelacion(p);
				/*log.info(" Entrada:"+p.getConsecutivo()+
				" EstatusEntrada:"+p.getStatus().getId()+ //Solo estatus 7,8 y status migradas
				" Año:"+p.getAnio()+
				" Subindice:"+p.getSubindice()+				
				" Actos Asociados:"+p.getActosParaPrelacions().size()
				); */
				if(p.getId_entrada()==null && (p.getArea().getId()==1 || p.getArea().getId()==2)){  //Es Entrada de Sistema (No migrada) y ES de PROPIEDAD
					switch(p.getStatus().getId().intValue()){
						case 7:  // LISTO PARA ENTREGAR
						case 8:  // ENTREGADO AL USUARIO
						case 10: // RECHAZADO
							pActosVO.setActosVO(findbyEntrada(p.getId()));
						break;
					}					
				} else {
					pActosVO.setActosVO(findbyEntrada(p.getId()));
				}
				if(pActosVO.getActosVO()!=null) {
					log.info("pActosVO.setActosVO:"+pActosVO.getActosVO().size());
					listEntradasActosVO.add(pActosVO);
				}
			}
			log.info("listEntradasActosVO.size():"+listEntradasActosVO.size());
		} 

		return listEntradasActosVO;
	}

	public Prelacion findByEntradaAnioSubIndice(Integer entrada, String año, Long subIndice, Long prelacionActual){
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Integer anio= Integer.parseInt(año);
		Prelacion pActual= prelacionRepository.findById(prelacionActual);
		Prelacion p = prelacionRepository.findByConsecutivoAndAnioAndSubIndiceAndOficinaId(entrada,anio,subIndice,usuario.getOficina().getId());
		BusquedaResultado br = new BusquedaResultado(); 
		if(p!=null) {
			try {
				busquedaResultadoService.clearBusquedaFromPrelacion(prelacionActual);
			} catch (Exception e) {
				 System.out.println(e);
			}
			List<BusquedaResultado> brs= busquedaResultadoService.findAllBusquedResultadoByPrelacionIdAndPrelacionHistorica(pActual.getId(), p.getId());
			if(brs.size()==0) {
				br.setPrelacionHistorica(p);
				br.setPrelacion(pActual);
				busquedaResultadoService.guardar(br);
			}

		}
		return p;
	}
	public Prelacion findByEntradaAnioSubIndiceByOficina(Integer entrada, String año, Long subIndice, Long prelacionActual,Long oficinaId ){
		Oficina oficina = oficinaRepository.findById(oficinaId);
		if(oficina!=null){
			Integer anio= Integer.parseInt(año);
			Prelacion pActual= prelacionRepository.findById(prelacionActual);
			Prelacion p = prelacionRepository.findByConsecutivoAndAnioAndSubIndiceAndOficinaId(entrada,anio,subIndice,oficina.getId());
			BusquedaResultado br = new BusquedaResultado(); 
			if(p!=null) {
				try {
					busquedaResultadoService.clearBusquedaFromPrelacion(prelacionActual);
				} catch (Exception e) {
					 System.out.println(e);
				}
				List<BusquedaResultado> brs= busquedaResultadoService.findAllBusquedResultadoByPrelacionIdAndPrelacionHistorica(pActual.getId(), p.getId());
				if(brs.size()==0) {
					br.setPrelacionHistorica(p);
					br.setPrelacion(pActual);
					busquedaResultadoService.guardar(br);
				}
	
			}
			return p;
		}
	
		return null;
	}
	
	public List<PrelacionCopiaCertificadaFolioVO> findByFolio(Integer folio){
		List<Prelacion> listPrelacion = prelacionRepository.findByFolio(folio);
		List<PrelacionCopiaCertificadaFolioVO> listPrelacionVO = new ArrayList<PrelacionCopiaCertificadaFolioVO>();
		
		for(Prelacion pre:listPrelacion ) {
			int cont =0, sizeActos=0;
			PrelacionCopiaCertificadaFolioVO prelacionVo = new PrelacionCopiaCertificadaFolioVO();
			prelacionVo.setPrelacion(pre);
			prelacionVo.setNombreTipoActo("");
			sizeActos=pre.getActosParaPrelacions().size();
			for(Acto act : pre.getActosParaPrelacions()) {
				
			if(cont>0&&cont<sizeActos)
				prelacionVo.setNombreTipoActo(prelacionVo.getNombreTipoActo()+", ");
				
				prelacionVo.setNombreTipoActo(prelacionVo.getNombreTipoActo()+ act.getTipoActo().getNombre());
				cont++;
			}
			listPrelacionVO.add(prelacionVo);
		}
		
		return listPrelacionVO;
		}
	
	
	public List<PrelacionCopiaCertificadaFolioVO> findByFolio(Long  idPredio,Long busquedaResultadoId,Integer tipoFolio){
		
		Long[] statusActoId = {Constantes.StatusActo.ACTIVO.getIdStatusActo(),
								Constantes.StatusActo.INVALIDO.getIdStatusActo()};
		
		Set<Acto> listActos =  tipoFolio == Constantes.ETipoFolio.PREDIO.getTipoFolio() ?  actoPredioRepository.findActosPredioByPredioAndStatusQuery(idPredio,statusActoId) :
								tipoFolio == Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio() ?  actoPredioRepository.findActosPredioByPersonaJuridicaAndStatusQuery(idPredio,statusActoId) : null ;
		List<PrelacionCopiaCertificadaFolioVO> listPrelacionVO = new ArrayList<PrelacionCopiaCertificadaFolioVO>();
		Set<BusquedaResultadoActo> actosBusqueda =  busquedaResultadoService.findBusquedaResultadoActos(busquedaResultadoId);
		
		if(listActos!=null && listActos.size()>0) 
		{
		 for(Acto acto:listActos ) 
		 {
			if(acto.getTipoActo()!=null && (acto.getVf()==null || acto.getVf()==false) && (acto.getClon()==null || acto.getClon() == false))
			{	
				PrelacionCopiaCertificadaFolioVO prelacionVo = new PrelacionCopiaCertificadaFolioVO();
		
				    acto.getPrelacion().getId_entrada();
					prelacionVo.setPrelacion(acto.getPrelacion());
					prelacionVo.setNombreTipoActo(acto.getTipoActo().getNombre());
					prelacionVo.setActoId(acto.getId());
					prelacionVo.setSeleccionado(actosBusqueda!=null && actosBusqueda.size() > 0 ? 
								actosBusqueda.stream()
								.filter(x->x.getActo().getId().equals(acto.getId())).count()>0 : false 
								);
					listPrelacionVO.add(prelacionVo);		
		     }
		  }
		}
		
		  return listPrelacionVO;
	}
	
	public List<ActoVO> findbyEntrada(long prelacionId){
		
		Prelacion prelacion = prelacionRepository.findById(prelacionId);
		Set<Acto> listActos = prelacion.getActosParaPrelacions();
		List<ActoVO> listActoVO = new ArrayList<ActoVO>();

		for(Acto acto:listActos ) {
			if(acto.getTipoActo()!=null && (acto.getVf()==null || acto.getVf()==false) && (acto.getClon()==null || acto.getClon() == false))
			{	
				ActoVO actoVO = new ActoVO();				
				    actoVO.setId(acto.getId());
					actoVO.setPrelacion(acto.getPrelacion().getId());
					actoVO.setOrden(acto.getOrden());
					actoVO.setTipoActo(acto.getTipoActo());
					actoVO.setStatusActo(acto.getStatusActo());					
					listActoVO.add(actoVO);				
		     }
		  }	

		return listActoVO;
	}
	public int getObtenTotalHojasArchivoEntrada(Long prelacionId){
		String rutaArchivoEntrada=null;
		String fname=null;
	    int totalhojas = 0;
		InputStream in;
		
		Prelacion p = prelacionRepository.findById(prelacionId);
		if(p.getId_entrada()!=null) {
			Parametro parametro = parametroRepository.findByCve(Constantes.RUTA_PRELACION);
			rutaArchivoEntrada = parametro.getValor()+"MIG_"+p.getId_entrada()+"_ENTRADA.pdf";
		}else {
			
		List<Acto> actos = actoRepository.findAllByPrelacionIdOrderByOrdenAsc(prelacionId);

		rutaArchivoEntrada= actos.get(0).getPathArchivado();
		}

		fname =rutaArchivoEntrada;
		try{

			in = new FileInputStream(new File(fname));
			totalhojas = CommonUtil.getNumeroDeHojas(in);
			in.close();
	
		}catch(FileNotFoundException e){
			log.error(e.getMessage());
		}catch(IOException e){
			log.error(e.getMessage());						
		}
		
		return totalhojas;
	}
	
	public List<String> getPathOfImagesOfArchivoByEntrada(Long prelacionId) {
		String rutaArchivoEntrada=null;
		List<String> lstPaths = new ArrayList<>();
		
		Prelacion p = prelacionRepository.findById(prelacionId);
		if(p.getId_entrada()!=null) {
			
			Parametro parametro = parametroRepository.findByCve(Constantes.RUTA_PRELACION);
			rutaArchivoEntrada = parametro.getValor()+"MIG_"+p.getId_entrada()+"_ENTRADA.pdf";
		}else {
	
			rutaArchivoEntrada =  this.getPathFileEntrada(prelacionId);
		}
		
		lstPaths.add(rutaArchivoEntrada);
		return lstPaths;
		
	}
	
	public String getPathFileEntrada(Long prelacionId) {
			List<Acto> actos = actoRepository.findAllByPrelacionIdAndVfFalseOrderByOrdenAsc(prelacionId,false);
		String[] rutaArchivoEntrada = {null};
		if(actos!=null && actos.size()>0){
			actos.forEach(x->{
				Optional<ActoDocumento> adoc = actoDocumentoRepository.findFirstByActoIdOrderByIdDesc(x.getId());
				if(adoc.isPresent() && adoc.get().getDocumento()!=null && adoc.get().getDocumento().getArchivo()!=null) {
					Archivo archivo = adoc.get().getDocumento().getArchivo();
					if(!archivo.getId().equals(1L)) {
						rutaArchivoEntrada[0]=x.getPathArchivado();	
					}
					
				}
				
			});
		}
		return rutaArchivoEntrada[0];
	}
	
	public List<String> getPathOfImagesByActo(Long actoId){
		Acto acto = actoRepository.findOneById(actoId);
		List<String> lstPaths = new ArrayList<>();
		String rutaArchivo=null;
		if(acto.getIdAsiento()!=null) {			
			Parametro parametro = parametroRepository.findByCve(Constantes.RUTA_ASIENTOS_CVE);
			rutaArchivo = parametro.getValor()+"/MIG_"+acto.getIdAsiento()+"_ASIENTO.pdf";			
		}else 
		{
			//IHM Validar para servicios armar la ruta del documento
			if(acto.getPathArchivado()!=null) {				
				rutaArchivo = acto.getPathArchivado();
			}
		}		
		lstPaths.add(rutaArchivo);
		return lstPaths;
	}
	
	public  byte[] getImagesConsultaBySeleccionadasByEntradaOrFolio(BusquedaResultado  busqueda) {

		byte[] lstBytes = null;
		InputStream in;
		
		File folder = null;
		String ofname = null;
		String rutaArchivoEntrada=null;
		
		if(busqueda.getPrelacionHistorica().getId_entrada()!=null) {
			
			Parametro parametro = parametroRepository.findByCve(Constantes.RUTA_PRELACION);
			rutaArchivoEntrada = parametro.getValor()+"MIG_"+busqueda.getPrelacionHistorica().getId_entrada()+"_ENTRADA.pdf";
		}else {
	
		 rutaArchivoEntrada =  this.getPathFileEntrada(busqueda.getPrelacionHistorica().getId());

		}
	//	Parametro parametro = parametroRepository.findByCve(rutaLibro);		
		
		try {
			File file= new File(rutaArchivoEntrada);
			lstBytes = Files.readAllBytes(file.toPath());
			//in = new FileInputStream(new File(rutaArchivoEntrada));
           //  byte fileContent[] = new byte[(int)in.length()];
			//lstBytes=CommonUtil.doExtraeToByteArray(in,busqueda.getHojasSeleccionadas(),null)

		} catch (FileNotFoundException e) {
        	log.error(e.getMessage());
            e.printStackTrace();
		} catch (IOException e) {
        	log.error(e.getMessage());
            e.printStackTrace();
        }
		return lstBytes;
	}	
	
	
	public  byte[] getImagesConsultaBySeleccionadasByFolio(BusquedaResultado  busqueda) {
		byte[] lstBytes = null;
		Set<BusquedaResultadoActo> actos = busquedaResultadoService.findBusquedaResultadoActos(busqueda.getId());
			if(actos!=null && actos.size()>0) {
				for(BusquedaResultadoActo bActo : actos) {
					Acto acto = bActo.getActo();
					List<String> rutaArchivo  = this.getPathOfImagesByActo(acto.getId());
					System.out.println("IHM-Detalle-Copia ["+acto.getId()+"|"+acto.getTipoActo().getNombre()+"]");
					log.info("RutaArchivo:{}",rutaArchivo);
					if(rutaArchivo!=null && rutaArchivo.size()>0) 
					{
						try 
						{
							File file= new File(rutaArchivo.get(0));
							byte[] fileBytes = Files.readAllBytes(file.toPath());
							if(lstBytes!=null)
							   lstBytes =  pdfService.appendPDF(lstBytes, fileBytes);
							else 
								lstBytes = fileBytes;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		return lstBytes;
	}	
	
	/**
	 * 
	 * @param prelacion
	 * @param actos
	 */
	@Transactional
	private void updateResultado(Prelacion prelacion, List<ActoPredio> actos) {
		boolean rechazado = false;
		boolean terminado = false;
		
		for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
			ActoPredio actoPredio = (ActoPredio) iterator.next();
									
			if (actoPredio.getActo().getStatusActo().getId().equals(Constantes.StatusActo.RECHAZADO.getIdStatusActo())) {
				rechazado = true;
			} else if (actoPredio.getActo().getStatusActo().getId().equals(Constantes.StatusActo.ACTIVO.getIdStatusActo())) {
				terminado = true;
			}
			
		}
		
		if (rechazado && terminado) {
			prelacion.setResultado(resultadoRepository.findOne(Constantes.Resultado.PARCIALMENTE_RECHAZADO.getIdResultado()));
		} else {
			if (rechazado && !terminado) {
				prelacion.setResultado(resultadoRepository.findOne(Constantes.Resultado.RECHAZADO.getIdResultado()));
			} else {
				prelacion.setResultado(resultadoRepository.findOne(Constantes.Resultado.TERMINADO.getIdResultado()));
			}
		}
		
		log.debug("Guardando nuevo resultado {} ", prelacion.getResultado());
		
		prelacionRepository.save(prelacion); 		
	}
  
	public PrelacionServicio findPrelacionServioByPrelacionId(Long prelacionId){
		PrelacionServicio ps= new PrelacionServicio();
		ps= prelacionServicioRepository.findByIdPrelacion(prelacionId);
		return ps;
	}
	
	public ActoPublicitario findActoPublicitario(Long prelacionActual,Integer numeroActoPublicitario,Long idOficina){
		System.out.println("Acto Publicitario :  " );
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		Prelacion pActual= prelacionRepository.findById(prelacionActual);
		ActoPublicitario actoPublicitario = null;
		if(idOficina!=-1) {
			actoPublicitario = actoPublicitarioRepository.findActoPublicitarioByOficinaIdAndNumActoPublicitario(idOficina,numeroActoPublicitario);
		}else {
			actoPublicitario = actoPublicitarioRepository.findActoPublicitarioByOficinaIdAndNumActoPublicitario(usuario.getOficina().getId(),numeroActoPublicitario);
		}
		 
	//System.out.println("Publicitario : " + actoPublicitario.getId() + " Prelacion : "+ actoPublicitario.getActo().getPrelacion().getId());
		BusquedaResultado br = new BusquedaResultado(); 
		if(actoPublicitario!=null) {
			List<BusquedaResultado> brs= busquedaResultadoService.findAllByPrelacionIdAndPrelacionHistoricaAndActoPublicitarioId(pActual.getId(), actoPublicitario.getActo().getPrelacion().getId(),actoPublicitario.getId());
			if(brs.size()==0) {
				try {
					busquedaResultadoService.clearBusquedaFromPrelacion(pActual.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				br.setPrelacionHistorica(actoPublicitario.getActo().getPrelacion());
				br.setPrelacion(pActual);
				br.setActoPublicitario(actoPublicitario);
				busquedaResultadoService.guardar(br);
			}

		}
		return actoPublicitario;
	}
	
	public  List<ActoPublicitarioVO> getActoPublicitarioById(Long actoPublicitarioId) {
		
		
		ActoPublicitarioVO resultado = null;
		List<ActoPublicitarioVO> resultados=new ArrayList();	
		ActoPublicitario ap = actoPublicitarioRepository.findOne(actoPublicitarioId);
		ActoPredio actopredio = actoPredioRepository.findActoPredioByLastVersion(ap.getActo().getId());
		System.out.println("ACTO  iD : " + ap.getActo().getId() + "ACTO PUBLICITARIO "+ ap.getId());
		List<ActoPredioCampo> actoPredioCampos=actoPredioCampoRepository.findAllByActoPredioOrderById(actopredio);
		
						if(actoPredioCampos!=null && !actoPredioCampos.isEmpty()) {
							long valor =ap.getActo().getTipoActo().getId();  
							int tipoActoId=(int)valor; 
							switch (tipoActoId){// busqueda acto seleccionado
								case 1://repudio
								resultado = busquedaPorActo1(actoPredioCampos,actopredio);
								if(resultado != null){
									System.out.println("Acto Predio encontrado ap: "+ap.getId());
									resultados.add(resultado);		
								}
								break;

								case 2://PROTOCOLIZACIÓN DE AUTORIZACIÓN DE VENTA
								resultado = busquedaPorActo2(actoPredioCampos,actopredio);
								if(resultado != null){
									System.out.println("Acto Predio encontrado ap: "+ap.getId());
									resultados.add(resultado);		
								}
								break;
								case 3://RECTIFICACION
									resultado = busquedaPorActo3(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado ap: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 4://CANCELACIONES
									resultado = busquedaPorActo4(actoPredioCampos,actopredio);
									if(resultado != null){
										resultados.add(resultado);		
									}
								break;
								case 5://PROTOCOLIZACIÓN DE PRORROGA DE VIGENCIA PARA ACTO MODIFICATORIO
									resultado = busquedaPorActo5(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado ap: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 6://ACTO PUBLICITARIO GENERAL
									resultado = busquedaPorActo6(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado ap: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 7://REGISTRO, CANCELACIÓN, FIRMA PATENTE O HABILITACION Y SELLO DE FEDATARIOS PUBLICOS
									resultado = busquedaPorActo7(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado ap: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 54://DECLARACIÓN DE HEREDEROS
									resultado = busquedaPorActo54(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado ap: "+ap.getId());
										resultados.add(resultado);		
									}
									break;
									
								case 56://CESIÓN DE DERECHOS HEREDITARIOS
									resultado = busquedaPorActo56(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado ap: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 57://nombramiento de albacea
									resultado = busquedaPorActo57(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 76://INFORMES DE DISPOSICIÓN TESTAMENTARIA		
									System.out.println("ActoPredioCampo size : "+ actoPredioCampos.size());
									resultado = busquedaPorActo76(actoPredioCampos, actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 105://SECCIÓN AUXILIAR PODER(ES)
									resultado = busquedaPorActo105(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 106://SECCIÓN AUXILIAR MODIFICACIÓN PODER(ES)
									resultado = busquedaPorActo106(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 107://SECCIÓN AUXILIAR REVOCACIÓN  PODER(ES)
									resultado = busquedaPorActo107(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 132://REVOCAION DE TESTAMENTO
									resultado = busquedaPorActo132(actoPredioCampos,actopredio);
									if(resultado != null){
										resultados.add(resultado);		
									}
								break;
								case 133://RETIRO DE TESTAMENTO
									resultado = busquedaPorActo133(actoPredioCampos, actopredio);
									if(resultado != null){
										resultados.add(resultado);		
									}
								break;
								case 134://DEPÓSITO DE TESTAMENTO
									resultado = busquedaPorActo134(actoPredioCampos, actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 242://LECTURA DE TESTAMENTO
									resultado = busquedaPorActo242(actoPredioCampos, actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
								case 244://RESOLUCIÓN ADMINISTRATIVA PARA AUTORIZACIÓN DE ACTO MODIFICATORIO DEL INMUEBLE
									resultado = busquedaPorActo244(actoPredioCampos,actopredio);
									if(resultado != null){
										System.out.println("Acto Predio encontrado b: "+ap.getId());
										resultados.add(resultado);		
									}
								break;
							}
						}						
			
		if(resultados.isEmpty()){
			resultado = new ActoPublicitarioVO();
			resultado.setOficina(usuarioService.getUsuario().getOficina().getNombre());
			System.out.println(usuarioService.getUsuario());
			resultado.setNoRegistrosEncontrados("NO SE ENCONTRARON \n REGISTROS");	
			resultados.add(resultado);
		} 
		return resultados;   
	}

	public byte[] getPdfBoletaRegistralPorActo(Long prelacionId,boolean pageable,Integer pagina) {

		try {
		   return boletaRegistralService.generPdfBoletaRegistralPorActo(prelacionId,pageable,pagina,false);
	   } catch (Exception e) {
		   e.printStackTrace();
		   return null;
		   
	   }

   }
	

  
   public byte[] getPdfBoletaRegistralPorActoJSON(List<PrelacionBoletaRegistralVO> prelacions,Integer pagina,boolean marcaAgua) {

      try {
         return boletaRegistralService.generPdfBoletaRegistralPorActoJSON(prelacions,pagina,marcaAgua);
       } catch (Exception e) {
         e.printStackTrace();
         return null;

       }

   }
   
   public byte[] generaPdfBoletaRechazos(Long prelacionId,boolean marcaAgua) {
	   try {
	         return boletaRegistralService.generaPdfBoletaRechazos(prelacionId,marcaAgua);
	       } catch (Exception e) {
	         e.printStackTrace();
	         return null;

	       }
   }
   
   public Prelacion findEntradaByConsecutivoAndAnioAndSubindice(Integer entrada, String año, Long subIndice){
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Integer anio= Integer.parseInt(año);
		Prelacion p = prelacionRepository.findByConsecutivoAndAnioAndSubIndiceAndOficinaId(entrada,anio,subIndice,usuario.getOficina().getId());
		return p;
   }
   


   public List<ActoPredio> findAllActosFusionByPrelacionAndVfFalseSalida(Long idPrelacion){
		List<ActoPredio> actos = findAllActoPredioByPrelacionAndVfFalseSalida(idPrelacion);
		actos =  actos.stream().filter(x->x.getActo().getTipoActo().getId().equals(Constantes.FUSION_TIPO_ACTO_ID)).sorted(Comparator.comparingLong(ActoPredio::getId)).collect(Collectors.toList());
		return actos;
	}
   
	public List<ActoPredio> findAllActoPredioByPrelacionAndVfFalseSalida(Long idPrelacion) {
		return actoPredioRepository.findAllByPrelacionAndVf(idPrelacion, false, Constantes.TipoEntrada.SALIDA);
	}


   public List<Acto> updateTipoActo(PrelacionVO prelacionIngreso, Long tipoActoId, Long actoId ){
	   List<Acto> actos = new ArrayList<Acto>();
	   Prelacion prelacion = prelacionService.findOne(prelacionIngreso.getId());
		TipoActo tipoActo = tipoActoService.findOne(tipoActoId);
		for (Acto acto : prelacionIngreso.getActos()) {
			acto.setPrelacion(prelacion);
			if (acto.getId().equals(actoId)) {
				acto.setTipoActo(tipoActo);
				acto = actoService.update(acto);
			}
			if (acto != null) {
				actos.add(acto);
			}
		}
		return actos;
   }
   
   public Prelacion changePrelacionDigitalizado(Long id) {
	   Prelacion prelacion = null;
	   Prelacion prelacionNew = new Prelacion();
	   prelacion = prelacionService.findOne(id);
	   
	   if(prelacion != null) {
		   prelacion.setEs_digitalizado(false);
		   prelacionRepository.saveAndFlush(prelacion);
		   prelacionNew = prelacion;
	   }else {
		   return prelacion;
	   }
	   return prelacionNew;
   }
   
   private Boolean isServicios(Prelacion prelacion) {
	   return prelacion.getPrelacionServiciosParaPrelacions() != null && 
			   prelacion.getPrelacionServiciosParaPrelacions().size() > 0;
   }
   
   private Boolean isVE(Prelacion prelacion) {
	   return prelacion.getUsuarioVentanilla()!=null &&
			   	!prelacion.getUsuarioVentanilla().getTipoUsuario().getId().equals(Constantes.TipoUsuario.ERPP.getIdTipoUsuario());
   }
   
   @Transactional
   private void updateStatusPredios(Long prelacionId) {
	   List<PrelacionPredio> folios =  prelacionPredioRepository.findByPrelacionIdOrderByIdAsc(prelacionId);
	   folios.forEach(x->{
		   if(x.getPredio()!=null && 
				(x.getPredio().getStatusActo().getId().equals(Constantes.StatusActo.EN_PROCESO.getIdStatusActo()) 	|| 
						x.getPredio().getStatusActo().getId().equals(Constantes.StatusActo.TEMPORAL.getIdStatusActo())))
			{
			  Predio predio =  x.getPredio();
			  predio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			  predioRepository.save(predio);
		    }
		   
	   });
   }
   
   public List<ActoPredio> findAllActoPredioByPrelacion(Long idPrelacion){
		return actoPredioRepository.findAllByPrelacion(idPrelacion);
	}
   
   public Prelacion changePrelacionValidador(Long id) {
	   Prelacion prelacion = null;
	   Prelacion prelacionNew = new Prelacion();
	   prelacion = prelacionService.findOne(id);
	   
	   if(prelacion != null) {
		   
		   prelacion.setUsuarioCapVal(prelacion.getUsuarioAnalista());
		   bitacoraRepository.save(createBitacora(prelacion));
		   prelacionRepository.saveAndFlush(prelacion);
		   prelacionNew = prelacion;
	   }else {
		   return prelacion;
	   }
	   return prelacionNew;
   }
   
   public List<Acto> updateTipoActoCLG(PrelacionVO prelacionIngreso, Long tipoActoId, Long actoId ){
	   List<Acto> actos = new ArrayList<Acto>();
	   Acto actoPrimerAvisoN = new Acto();
	   ActoPredio actoPredioNew = new ActoPredio();
	   Servicio servicio = new Servicio();
	   TipoActo taPrimerAviso = tipoActoService.findOne(Constantes.TIPO_ACTO_PRIMER_AVISO);
	   Prelacion prelacion = prelacionService.findOne(prelacionIngreso.getId());
	   TipoActo tipoActo = tipoActoService.findOne(tipoActoId);
	   PrelacionServicio prelaServi= prelacionServicioRepository.findByIdPrelacion(prelacion.getId());

	   if(tipoActoId.equals(Constantes.TIPO_ACTO_CERTIFICADO_LG)) {

		   servicio = servicioRepository.findOneById(Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_ID.getIdServicio());

		   for (Acto acto : prelacionIngreso.getActos()) {
			   acto.setPrelacion(prelacion);
			   if(acto.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO)) {
				   actoPrimerAvisoN = acto;
			   }else {
				   acto.setTipoActo(tipoActo);
				   acto.setOrden(1);
				   acto = actoService.update(acto);
				   if (acto != null) {
					   actos.add(acto);
				   }
			   }
		   }
		   actoService.deleteActo(actoPrimerAvisoN.getId());
	   }
	   if(tipoActoId.equals(Constantes.TIPO_ACTO_CERTIFICADO_LG_PRIMER_AVISO)) {

		   servicio = servicioRepository.findOneById(Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_CAUTELAR_ID.getIdServicio());

		   for (Acto acto : prelacionIngreso.getActos()) {
			   acto.setPrelacion(prelacion);
			   acto.setOrden(2);
			   acto.setTipoActo(tipoActo);
			   acto = actoService.update(acto);

			   if (acto != null) {
				   actos.add(acto);
			   }
		   }
		   Acto actoN = new Acto();
		   actoN.setPrelacion(prelacion);
		   actoN.setFechaRegistro(new Date());
		   actoN.setTipoActo(taPrimerAviso);
		   actoN.setOrden(1);
		   actoN.setVersion(1);
		   actoN.setModificable(true);
		   actoN.setStatusActo( statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()) );
		   actoPrimerAvisoN =actoRepository.save(actoN);

		   
		   actoPredioNew.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
		   actoPredioNew.setVersion(1);
		   actoPredioNew.setActo(actoPrimerAvisoN);

		   actoPredioRepository.save(actoPredioNew);

		   actos.add(actoPrimerAvisoN);
	   }

	   prelaServi.setServicio(servicio);
	   prelacionServicioRepository.saveAndFlush(prelaServi);

	   return actos;
   }
   
   public Prelacion reproceso(PrelacionVO prelacionVo, Long tipoBoleta) {
	   List <PrelacionBoleta> boletaPre = new ArrayList<PrelacionBoleta>();
	   Prelacion result = new Prelacion();
	   
	   Prelacion prelacion = prelacionRepository.findOne(prelacionVo.getId());
	   boletaPre = prelacionBoletaService.findAllByPrelacion(prelacion.getId(),tipoBoleta);
	   if(boletaPre != null && boletaPre.size()>0) {
		   prelacionBoletaRepository.delete(boletaPre);
	   }
	   
	   result = updatePrelacionEstado(prelacion.getId(), Constantes.Status.ASIGNADO_A_COORDINADOR,prelacionVo.getObservaciones(),null,true);
		
	   
	   return result;
   }
   
   
   public Boolean folioOficina(Integer folio, Integer tipoFolio, Long oficinaId){
	   Boolean esDiferente = false;
		try {
			switch (tipoFolio) {
			case 1:
				PersonaJuridica persona = personaJuridicaService.findByNumFolio(folio);
				if (persona != null) {
					if(persona.getOficina().getId() != oficinaId){
						esDiferente=true;
					}
				}
				break;
			case 2:
				FolioSeccionAuxiliar auxiliar = folioSeccionAuxiliarRepository.findOneByNoFolio(folio);
				if (auxiliar != null) {
					if(auxiliar.getOficina().getId() != oficinaId){
						esDiferente=true;
					}
				}
				break;
			case 3:
				Mueble mueble = muebleRepository.findByNoFolio(folio);
				if (mueble != null) {
					if(mueble.getOficina().getId() != oficinaId){
						esDiferente=true;
					}
				}
				break;
			case 4:
				Predio predio = predioService.findPredioByNoFolio(folio);
				if (predio != null) {
					if(predio.getOficina().getId() != oficinaId){
						esDiferente=true;
					}
				}
				break;
			}
		} catch (Exception e) {
			
		}
		return esDiferente;
	}

   public Prelacion upkeepPrelacion (Prelacion pre) {
	   PrelacionUsuarioDef  prelacionUF = new PrelacionUsuarioDef();
	   Prelacion pOriginal = prelacionService.findOne(pre.getId());
	   BitacoraPrelacion bitacoraP = new BitacoraPrelacion();
	   List <PrelacionBoleta> boletaPre = new ArrayList<PrelacionBoleta>();
	   prelacionUF = prelacionUsuarioDefRepository.findOneByPrelacionId(pre.getId());

	   if(pre.getUsuarioSolicitan().getId() == 0) {
		   
		   if(prelacionUF!=null) {
			   prelacionUF.setNombre(pre.getUsuarioSolicitan().getPersona().getNombre());
			   prelacionUF.setPaterno(pre.getUsuarioSolicitan().getPersona().getPaterno());
			   prelacionUF.setMaterno(pre.getUsuarioSolicitan().getPersona().getMaterno());
			   prelacionUsuarioDefRepository.saveAndFlush(prelacionUF);
		   }else {
			   prelacionUF = new PrelacionUsuarioDef();
			   prelacionUF.setNombre(pre.getUsuarioSolicitan().getPersona().getNombre());
			   prelacionUF.setPaterno(pre.getUsuarioSolicitan().getPersona().getPaterno());
			   prelacionUF.setMaterno(pre.getUsuarioSolicitan().getPersona().getMaterno());
			   prelacionUF.setPrelacion(pre);
			   prelacionUsuarioDefRepository.saveAndFlush(prelacionUF);
		   } 

	   }else {
		   if(prelacionUF!=null) {
			   prelacionUF.setPrelacion(null);
			   prelacionUsuarioDefRepository.saveAndFlush(prelacionUF);
		   }
	   }

	   if(pOriginal.getStatus().getId() == Constantes.Status.RECHAZADO.getIdStatus() ||
			   pOriginal.getStatus().getId() == Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus() ||
			   pOriginal.getStatus().getId() == Constantes.Status.LISTO_PARA_ENTREGARSE.getIdStatus() ) {

		   if(pre.getStatus().getId() == Constantes.Status.ASIGNADO_A_COORDINADOR.getIdStatus() ||
				   pre.getStatus().getId() == Constantes.Status.ASIGNADO_A_ANALISTA.getIdStatus()   ) {

			   boletaPre = prelacionBoletaService.findAllByPrelacion(pre.getId(),2L);
			   if(boletaPre != null && boletaPre.size()>0) {
				   prelacionBoletaRepository.delete(boletaPre);
			   }
		   }

	   }
	   
	   //pOriginal.setFechaIngreso(pre.getFechaIngreso());
	   pOriginal.setSubindice(pre.getSubindice());
	   pOriginal.setPrioridad(pre.getPrioridad());
	   pOriginal.setStatus(pre.getStatus());
	   pOriginal.setArea(pre.getArea());
	   pOriginal.setEs_digitalizado(pre.getEs_digitalizado());
	   pOriginal.setUsuarioCapVal(pre.getUsuarioCapVal());
	   pOriginal.setUsuarioSolicitan(pre.getUsuarioSolicitan());

	   prelacionRepository.saveAndFlush(pOriginal);

	   bitacoraP = llenaBitacoraPrelacion(pre);
	   
	   if(pre.getUsuarioSolicitan().getId() == 0) {
	   bitacoraP.setPrelacionUsuarioDef(prelacionUF);
	   }
	   
	   bitacoraPrelacionRepository.save(bitacoraP);


	   return pOriginal;
   }

   public BitacoraPrelacion llenaBitacoraPrelacion(Prelacion pre) {
	   Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
	   BitacoraPrelacion bitacoraP = new BitacoraPrelacion();

	   bitacoraP.setFecha(new Date());
	   bitacoraP.setUsuario(usuario);
	   
	   bitacoraP.setPrelacion(pre);
	   bitacoraP.setFechaIngreso(pre.getFechaIngreso());
	   bitacoraP.setSubindice(pre.getSubindice());
	   bitacoraP.setPrioridad(pre.getPrioridad());
	   bitacoraP.setArea(pre.getArea());
	   bitacoraP.setStatus(pre.getStatus());
	   bitacoraP.setEs_digitalizado(pre.getEs_digitalizado());
	   bitacoraP.setUsuarioCapVal(pre.getUsuarioCapVal());
	   bitacoraP.setUsuarioSolicitan(pre.getUsuarioSolicitan());
	   bitacoraP.setSolicitante(pre.getUsuarioSolicitan().getUserName());
	   bitacoraP.setNombre(pre.getUsuarioSolicitan().getPersona().getNombre());
	   bitacoraP.setPaterno(pre.getUsuarioSolicitan().getPersona().getPaterno());
	   bitacoraP.setMaterno(pre.getUsuarioSolicitan().getPersona().getMaterno());

	   return bitacoraP;
   }
   
   public PrelacionModuloVO findPrelacionVO(Long prelacionId) {
	   Prelacion prelacion =  this.prelacionRepository.findById(prelacionId);
	   return new PrelacionModuloVO(prelacion.getId(),prelacion.getConsecutivo().toString(),prelacion.getAnio().toString());
   }
   
   public List<PrelacionModuloVO> findPrelacionesDenegadas(Long actoId) {
	   Optional<ActoPredio> oActoPredio =  this.actoPredioRepository.findFirstByActoIdOrderByVersionDesc(actoId);
	   ActoPredio actoPredio = null;
	   List<PrelacionModuloVO> rechazadas = new ArrayList<>();
	   List<PrelacionModuloVO> suspendidas = new ArrayList<>();
	   List<PrelacionModuloVO> list =  new ArrayList<>();
	   
	   if(oActoPredio.isPresent()) {
		   actoPredio =  oActoPredio.get();
		   if(actoPredio.getTipoFolio()!=null && actoPredio.getTipoFolio().getId().equals(4L) && actoPredio.getPredio()!=null) {
			   rechazadas =  caratulaDAO.findPrelacionesRechazadas(actoPredio.getPredio().getId(),null);
			   suspendidas =  caratulaDAO.findPrelacionesSuspendidas(actoPredio.getPredio().getId(), null);
		   }
		   if(actoPredio.getTipoFolio()!=null && actoPredio.getTipoFolio().getId().equals(1L) && actoPredio.getPersonaJuridica()!=null)
		   {
			   rechazadas =  caratulaDAO.findPrelacionesRechazadas(null,actoPredio.getPersonaJuridica().getId());
			   suspendidas =  caratulaDAO.findPrelacionesSuspendidas(null,actoPredio.getPersonaJuridica().getId());
		   }
		   
		   if(rechazadas!=null && rechazadas.size()>0)
			   list.addAll(rechazadas);
		   if(suspendidas!=null && suspendidas.size()>0) 
			   list.addAll(suspendidas);
	   }
	   return list;
	   
   }
   
   public Prelacion bitacoraEntrega (Long prelacionId, Persona persona) {
	   Prelacion prelacion =  this.prelacionRepository.findById(prelacionId);
	   Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
	   BitacoraEntrega bitacoraE = new BitacoraEntrega();
	   
	   bitacoraE = bitacoraEntregaService.saveBitacoraEntrega(prelacion,usuario,persona);
	   
	   return prelacion;
   }
   
   public Prelacion prelacionDevolucion (Long prelacionId, String observaciones) {
	   Status status = new Status();
	   Prelacion prelacion =  this.prelacionRepository.findById(prelacionId);
	   bitacoraRepository.save(prelacionService.createBitacoraCompleta(prelacion,null,null,null,null,observaciones));
	   status.setId((Constantes.Status.DEVOLUCION_DOCUMENTO_AUTORIZADO).getIdStatus());
	   prelacion.setStatus(status);
	   prelacionRepository.saveAndFlush(prelacion);

	   return prelacion;
   }
   
   
   @Transactional
   public void dematerializaActos(Long prelacionId) throws Exception {
	   List<Acto> actos =  this.actoRepository.findAllByPrelacionIdAndVfFalseOrderByOrdenAsc(prelacionId,false);
	   for(Acto acto : actos) {
		   if(acto.getStatusActo().getId().equals(Constantes.StatusActo.ACTIVO.getIdStatusActo())) {
			   actoService.desfirmarActo(acto.getId());
			   materializacionActoService.deMaterializarNew(acto);
		   }
	   }
   }
   
   public List<Prelacion> usuarioDef(List<Prelacion> prelaciones){
	   try {
		   prelaciones.forEach(x->{
			   if(x.getUsuarioSolicitan().getId() == 0) {
				   PrelacionUsuarioDef def = new PrelacionUsuarioDef();
				   def = prelacionUsuarioDefRepository.findOneByPrelacionId(x.getId());
					if (def != null) {
						Usuario usu = new Usuario();
						Persona per = new Persona();
		
						per.setNombre(def.getNombre());
						per.setPaterno(def.getPaterno());
						per.setMaterno(def.getMaterno());
						
						usu.setPersona(per);
						
						x.setUsuarioSolicitan(usu);
						
					}
			   }
			});
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	   return prelaciones;
   }
   
   public PublicVO datosPublic(Prelacion pre) {
	   PublicVO publico = new PublicVO();
	   PrelacionUsuarioDef usuarioDef = new PrelacionUsuarioDef();
	   StringBuilder nombre= new StringBuilder();
	   Bitacora resultado = null;

	   if(pre.getUsuarioSolicitan().getId() == 0) {
		   usuarioDef = this.getUsuarioDefault(pre.getId());
		   if(usuarioDef!= null) {
			   nombre.append(usuarioDef.getNombreCompleto()) ; 
		   }
	   }else {
		   nombre.append(pre.getUsuarioSolicitan().getNombreCompleto()) ;

	   }
	   if(pre.getStatus().getId() == Constantes.Status.LISTO_PARA_ENTREGARSE.getIdStatus()
			   || pre.getStatus().getId() == Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()
			   || pre.getStatus().getId() == Constantes.Status.RECHAZADO.getIdStatus()
			   || pre.getStatus().getId() == Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus()) {

		   if(pre.getStatus().getId() == Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus()) {
			   resultado = bitacoraService.findOneBitacoraByPrelacionIdAndStatusId(pre);
			   if(resultado != null) {
				   if(resultado.getMotivo() != null) {
					   publico.setMotivo(resultado.getMotivo().getNombre());
				   }
				   if(resultado.getTipoRechazo() != null) {
					   publico.setFundamento(resultado.getTipoRechazo().getNombre()); 
				   }
				   publico.setObservaciones(resultado.getObservaciones()); 
			   }
		   }
		   if(pre.getResultado() !=null ) {
			   publico.setId(pre.getResultado().getId());
			   publico.setMensaje(pre.getResultado().getNombre());
		   }
	   }else {
		   publico.setId(5L);
		   publico.setMensaje("EN PROCESO");
	   }

	   publico.setUsuarioSolicita(nombre.toString());
	   publico.setFechaIngreso(pre.getFechaIngreso());


	   return publico;
   }
   
   
   /*
    * INICIA PRELACIÓN NEGATIVA CON PRELACION_ANTE PARA CREAR FOLIO.
    */
   @Transactional
	public Prelacion iniciaPrelacionCyvf(AntecedenteVO antecedente) throws Exception {
		try {
			Prelacion newPrel = new Prelacion();
			Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			newPrel.setConsecutivo(prelacionRepository.getConsecutivoNeg());
			newPrel.setUsuarioCapVal(usuario);
			newPrel.setUsuarioSolicitan(usuario);
			newPrel.setUsuarioVentanilla(usuario);
			newPrel.setExentoPago(true);
			newPrel.setStatus(statusRepository.findOne(Constantes.Status.ASIGNADOR_A_VALIDADOR_DE_PREDIOS.getIdStatus()));
			if(antecedente.getTipoBusqueda().equals("4"))//PROPIEDAD TIPO FOLIO
				newPrel.setArea(areaRepository.findOne(Constantes.Area.PROPIEDAD.getIdArea()));//REGISTRO PROPIEDAD
			if(antecedente.getTipoBusqueda().equals("1"))//JURIDICO TIPO FOLIO
				newPrel.setArea(areaRepository.findOne(Constantes.Area.PERSONAS_MORALES.getIdArea()));// PERSONAS 
			
			newPrel.setFechaIngreso(new Date());
			newPrel.setOficina(usuario.getOficina());
			newPrel.setPrioridad(prioridadRepository.findOne(Constantes.Prioridad.NORMAL.getIdPrioridad()));
			newPrel.setVf(true);
			newPrel =  prelacionRepository.save(newPrel);
			
			//CREAR PRELACION_ANTE
			PrelacionAnte prelAnte = new PrelacionAnte();
			prelAnte.setPrelacion(newPrel);
			prelAnte.setOficina(oficinaRepository.findOne(Long.parseLong(antecedente.getOficina())));
			prelAnte.setSeccion(seccionRepository.findOne(Long.parseLong(antecedente.getSeccion())));
			prelAnte.setAnio(antecedente.getAnio());
			prelAnte.setLibro(antecedente.getLibro());//TOMO
			prelAnte.setLibroBis(antecedente.getLibroBis());//LIBRO
			prelAnte.setDocumento(antecedente.getDocumento());
			prelAnte.setVolumen(antecedente.getVolumen());
			prelAnte.setTipoFolio(antecedente.getTipoFolio());
			
			prelacionAnteRepository.save(prelAnte);
			
			return newPrel;
		}catch(Exception e) {
			throw new Exception("Error al iniciar la prelación");
		}
	
	}
   private String getCodigo(){
	   char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	   StringBuilder sb = new StringBuilder(6);
	   Random random = new Random();
	   for (int i = 0; i < 6; i++) {
	          char c = chars[random.nextInt(chars.length)];
	          sb.append(c);
	   }
   
	return sb.toString();
   }

   public List<Prelacion> save(List<Prelacion> prelaciones) {
	   return this.prelacionRepository.save(prelaciones);
   }

	public Optional<Prelacion> updatePrelacionPrioridad(Long idPrelacion, Long prioridadId) {

		Prelacion prelacion = prelacionRepository.findOne(idPrelacion);
		Optional<Prioridad> prioridad = Optional.ofNullable(prioridadRepository.findOne(prioridadId));

		if(prioridad.isPresent()) {
			prelacion.setPrioridad(prioridad.get());
			prelacionRepository.save(prelacion);
			return Optional.of(prelacion);
		}
		return Optional.empty();
	}
}
