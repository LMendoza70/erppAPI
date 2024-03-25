package com.gisnet.erpp.service;

import static com.gisnet.erpp.util.Constantes.ETipoFolio.PREDIO;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import java.text.DecimalFormat;

//import javax.lang.model.util.ElementScanner6;

import com.gisnet.erpp.vo.caratula.FolioSeccionAuxiliarVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.autoconfigure.RemoteDevToolsProperties.Debug;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.thymeleaf.context.Context;

//import ar.com.fdvs.dj.domain.BooleanExpression;

//import ch.qos.logback.core.joran.conditional.ElseAction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.dao.CaratulaDAO;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.ActoModulo;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.BusquedaResultado;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.Certificado;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Modulo;
import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Pases;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PjAnte;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioAnte;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.PrelacionServicio;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.domain.ReciboConcepto;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.BusquedaResultadoRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.CertificadoRepository;
import com.gisnet.erpp.repository.ModuloTipoActoRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.PasesRepository;
import com.gisnet.erpp.repository.PjAnteRepository;
import com.gisnet.erpp.repository.PredioAnteRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.NotarioRepository;
import com.gisnet.erpp.repository.ClasifActoRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.excepciones.RequerimientoException;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.EncryptAESUtil;
import com.gisnet.erpp.util.ServletUtil;
import com.gisnet.erpp.util.SignerUtil;
import com.gisnet.erpp.vo.ActoVO;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.BoletaRechazoVO;
import com.gisnet.erpp.vo.CopiaActoPublicitarioVO;
import com.gisnet.erpp.vo.GravamenCountVO;
import com.gisnet.erpp.vo.LimitacionCountVO;
import com.gisnet.erpp.vo.PredioAnteVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.actopublicitario.ActoPublicitarioVO;
import com.gisnet.erpp.vo.caratula.TramiteVO;
import com.gisnet.erpp.vo.certificado.CertificadoHistorialVO;
import com.gisnet.erpp.vo.certificado.CertificadoLibertadOGravamenVO;
import com.gisnet.erpp.vo.certificado.ColindanciaVO;
import com.gisnet.erpp.vo.certificado.PasesVO;
import com.gisnet.erpp.vo.certificado.PropietarioVO;
import com.gisnet.erpp.vo.certificado.CertificadoCopiasVO;
import com.gisnet.erpp.vo.juridico.CertificadoDeNoInscripcionVO;
import com.gisnet.erpp.vo.juridico.CertificadoPersonaJuridicasVO;
import com.gisnet.erpp.vo.prelacion.PrelacionBoletaRegistralVO;
import com.gisnet.erpp.service.PdfService;
import com.gisnet.erpp.web.api.firma.FirmaDTO;
import com.gisnet.erpp.web.api.firma.FirmaRequestDTO;
import com.ibm.icu.text.NumberFormat;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class CertificadoService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;

	@Autowired
	PredioService predioService;
	@Autowired
	CampoValoresRepository campoValoresRepository;
	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;
	@Autowired
	DocumentoService documentoService;

	@Autowired
	PrelacionRepository prelacionRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	ActoService actoService;

	@Autowired
	ActoPublicitarioService actoPublicitarioService;

	@Autowired
	CertificadoRepository certificadoRepository;

	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	private MailSenderService mailSenderService;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private FirmaService firmaService;

	@Autowired
	private PrelacionService prelacionService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private BusquedaResultadoService busquedaResultadoService;

	@Autowired
	private BusquedaService busquedaService;

	@Autowired
	private PredioPersonaService predioPersonaService;

	@Autowired
	private PasesRepository pasesRepository;

	@Autowired
	PjAnteRepository pjAnteRepository;

	@Autowired
	private ActoPredioRepository actoPredioRepository;

	@Autowired
	private ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	private BoletaRegistralService boletaRegistralService;

	@Autowired
	private CaratulaDAO caratulaDAO;

	@Autowired
	private PrelacionPredioService prelacionPredioService;

	@Autowired
	private PredioAnteRepository predioAnteRepository;

	@Autowired
	PdfService pdfService;

	@Autowired
	private NotarioRepository notarioRepository;

	@Autowired
	private CampoValoresRepository campoRepository;

	@Autowired
	private ActoPredioCampoService actoPredioCampoService;

	@Autowired
	private ClasifActoRepository clasifActoRepository;

	@Autowired
	ModuloTipoActoRepository moduloTipoActoRepository;

	@Autowired
	BusquedaResultadoRepository busquedaResultadoRepository;

	@Autowired
	CertificadoService certificadoService;

	@Autowired
	ColindanciaService colindanciaService;

	@Autowired
	ActoFirmaRepository actoFirmaRepository;

	@Autowired
	private CaratulaService caratulaService;

	public Certificado findByPrelacionId(Long prelacionId) {
		List<Certificado> certs = certificadoRepository.certificadoByPrelacionId(prelacionId);

		if (certs != null && certs.size() > 0)
			return certificadoRepository.certificadoByPrelacionId(prelacionId).get(0);
		else
			return null;
	}

	public CertificadoLibertadOGravamenVO loadCertificadoLibertadOGravamenVO(Integer secuencia) {
		Optional<Certificado> certificado = certificadoRepository.findOneBySecuencia(secuencia);
		if (!certificado.isPresent()) {
			throw new IllegalArgumentException("No se encontro ningun certificado con esa secuencia");
		}

		return certificadoTOCLg(certificado.get());
	}

	public CertificadoLibertadOGravamenVO certificadoTOCLg(Certificado certificado) {
		CertificadoLibertadOGravamenVO clg = jsonTOClg(certificado.getDatos());
		if (certificado.getPkcs7() == null)
			clg.setPkcs7("SIN -FIRMA");
		else
			clg.setPkcs7(certificado.getPkcs7());

		if (certificado.getFirma() == null)
			clg.setFirma("SIN -FIRMA");
		else
			clg.setFirma(certificado.getFirma());

		if (certificado.getSecuencia() == null)
			clg.setSecuencia(0);
		else
			clg.setSecuencia(certificado.getSecuencia());

		if (certificado.getHistorial() == null)
			clg.setHistorial("");
		else
			clg.setHistorial(certificado.getHistorial());

		System.out.println("HISTORIAL " + clg.getHistorial());
		return clg;
	}

	public List<CertificadoLibertadOGravamenVO> certificadoListTOCLg(Certificado certificado) {
		List<CertificadoLibertadOGravamenVO> clgs = jsonTOClgList(certificado.getDatos());
		int index = 0;
		for (CertificadoLibertadOGravamenVO clg : clgs) {
			if (certificado.getPkcs7() == null)
				clg.setPkcs7("SIN -FIRMA");
			else
				clg.setPkcs7(certificado.getPkcs7());

			if (certificado.getFirma() == null)
				clg.setFirma("SIN -FIRMA");
			else
				clg.setFirma(certificado.getFirma());

			if (certificado.getSecuencia() == null)
				clg.setSecuencia(0);
			else
				clg.setSecuencia(certificado.getSecuencia());

			if (certificado.getHistorial() == null)
				clg.setHistorial("");
			else
				clg.setHistorial(certificado.getHistorial());
		}

		return clgs;

	}

	public CertificadoPersonaJuridicasVO certificadoTOCLgJuridico(Certificado certificado) {
		CertificadoPersonaJuridicasVO clg = jsonTOClgJuridico(certificado.getDatos());
		return clg;
	}

	public CertificadoDeNoInscripcionVO certificadoTOCLgNoIncripcion(Certificado certificado) {
		CertificadoDeNoInscripcionVO clg = jsonTOClgNoInscripcion(certificado.getDatos());
		if (certificado.getDireccion() != null)
			clg.setDireccion(certificado.getDireccion());

		if (certificado.getaFavorDe() != null)
			clg.setFavorDe(certificado.getaFavorDe());

		if (certificado.getDirector() != null)
			clg.setNombreDirector(certificado.getDirector());

		if (certificado.getPropietarios() != null)
			clg.setPropietarios(certificado.getPropietarios());

		if (certificado.getFechaPlanoCartografico() != null) {

			SimpleDateFormat formatterFecha = new SimpleDateFormat("dd MMMM yyyy");

			clg.setFechaPlanoCartografico(formatterFecha.format(certificado.getFechaPlanoCartografico()));

		}

		return clg;
	}

	@Transactional(noRollbackFor = RequerimientoException.class)
	public CertificadoLibertadOGravamenVO generaCertificadoDeLivertadIGravamen(Prelacion prelacion, Predio predio)
			throws RequerimientoException {
		Boolean isCLGAviso = false;
		Notario notario = null;
		CertificadoLibertadOGravamenVO clg = null;
		// Certificado c = findByPrelacionId(prelacion.getId());
		// log.debug("ESTO ES CERTIFICADO{}", c);
		log.debug("NUEVO-CERTIFICADO");
		log.debug("generaCertificadoDeLivertadIGravamen ");
		// Predio predio =
		// prelacion.getPrelacionPrediosParaPrelacions().iterator().next().getPredio();
		// if (c != null) {
		// log.debug("UPDATE-CERTIFICADO");
		// clg = certificadoTOCLg(c);

		// } else {
		clg = new CertificadoLibertadOGravamenVO();
		// }

		/*
		 * if (prelacion.getPrelacionPrediosParaPrelacions() == null ||
		 * prelacion.getPrelacionPrediosParaPrelacions().size() != 1) { int n = 0; for
		 * (PrelacionPredio prelacionPredio :
		 * prelacion.getPrelacionPrediosParaPrelacions()) { if
		 * (prelacionPredio.getEstatus() == 1) { n += 1; } } if (n == 0 || n != 1) {
		 * throw new
		 * RequerimientoException("No se recibio el numero correcto de predios"); } }
		 */
		// cambiar el servicioId
		if (prelacion.getPrelacionServiciosParaPrelacions() == null
				|| prelacion.getPrelacionServiciosParaPrelacions().size() != 1) {
			throw new RequerimientoException(
					"No se recibio el numero correcto de servicios y/o servicio para la prelacion");
		}

		if (predio == null) {
			throw new RequerimientoException(
					"el predio no cumple con los requisitos bloqueado==false, cerrado==false, caratula_actualizada==true, reenviando a turnador");

		}
		if (predio.getBloqueado() != null && predio.getBloqueado() && predio.getCerrado() != null
				&& predio.getCerrado().length() > 0
				&& (predio.getCaratulaActualizada() == null || !predio.getCaratulaActualizada())) {
			throw new RequerimientoException(
					"el predio no cumple con los requisitos bloqueado==false, cerrado==false, caratula_actualizada==true, reenviando a turnador");
		}
		List<ActoPredio> actopredios = new ArrayList<ActoPredio>();
		List<ActoPredio> actoPrediosCLGAviso = new ArrayList<ActoPredio>();
		List<ActoPredio> actoPrediosCLG = new ArrayList<ActoPredio>();
		List<Acto> actos = actoRepository.findAllByPrelacion(prelacion);
		List<ActoPredioCampo> actoPredioCampos = new ArrayList<ActoPredioCampo>();
		StringBuilder sb = new StringBuilder();
		log.info("IHM Actos {}", actos);
		for (Acto ac : actos) {
			log.info("Tipo Acto:{}-{}", ac.getTipoActo(), ac.getTipoActo().getNombre());

			actopredios = actoPredioRepository.findLastVersionByActo(ac.getId());
			log.debug("actos predios {}", actopredios);
			if (ac.getTipoActo().getId() == 210L) { // AVISO DE CLG+AVISO
				actoPrediosCLGAviso = actopredios;
				isCLGAviso = true;
			} else if (ac.getTipoActo().getId() == 203L) { // CLG
				actoPrediosCLG = actopredios;
			}

			log.debug("ES CLG CON AVISO: " + isCLGAviso);

		}
		if (actoPrediosCLGAviso.size() > 0) {
			actopredios = actoPrediosCLGAviso;
		} else if (actoPrediosCLG.size() > 0) {
			actopredios = actoPrediosCLG;
		}

		for (ActoPredio ap : actopredios) {
			actoPredioCampos = actoPredioCampoRepository.findAllByActoPredio(ap);
			log.debug("actoPredioCampo {} ", actoPredioCampos);
		}

		String valor = "sin datos";
		String tipo = "sin datos2";
		String observacion = "sin datos2";
		String nombre = "";
		String paterno = "";
		String materno = "";
		String nombreNo = "";
		String paternoNo = "";
		String maternoNo = "";
		String noNotario = "";
		String estado = "";
		String municipio = "";

		String fechaAviso = "";
		log.info("AquiAPC1");
		if (actoPredioCampos.size() > 0) {
			// log.info("AquiGG "+ actoPredioCampos.size() );
			clg.setObservacion("-");
			for (ActoPredioCampo apc : actoPredioCampos) {
				if (isCLGAviso) {
					// log.info("AquiAPC2232 {}");
					if (apc.getModuloCampo().getId().equals(Constantes.NOTARIO) && apc.getValor() != null) {
						// log.info("AquiAPC2232 "+apc.getModuloCampo().getId());
						// log.info("AquiQQQ1 {}",apc);
						notario = notarioRepository.findById(new Long(apc.getValor().trim()));
						// log.info("AquiQQQ2"+ notario);
						noNotario = " NOTARIO NO. " + notario.getNoNotario();
						nombre = "LIC " + notario.getNombreCompleto();
						estado = " DEL ESTADO DE " + notario.getMunicipio().getEstado().getNombre().trim();
						municipio = " EN EL MUNICIPIO DE " + notario.getMunicipio().getNombre().trim();
						// log.info("AquiQQQ3");

					}
					log.info("SIN CAPTURA DEL PRIMER AVISO");
					if (apc.getModuloCampo().getId().equals(Constantes.FECHA_AVISO) && apc.getValor() != null
							|| !apc.getValor().equals("")) {
						// log.info("AquiQQQ3" );
						fechaAviso = apc.getValor();
						log.info("CLG+AVISO fechaAviso:{}", fechaAviso);
						// log.info("AquiQQQ4" );
					}

				} else {
					// log.info("AquiAPC3 {}", apc );
					if (apc.getModuloCampo().getId().equals(Constantes.TIPO_DE_SOLICITANTE)) {
						tipo = apc.getValor();
					}

					// log.info("entro a nombre "+Constantes.NOMBRE);
					if (apc.getModuloCampo().getId().equals(Constantes.NOMBRE)) {
						nombreNo = "LIC " + apc.getValor();
						log.info("entro a nombre " + nombre + " " + apc.getModuloCampo().getId());
					}

					if (apc.getModuloCampo().getId().equals(Constantes.PATERNO)) {
						paternoNo = " " + apc.getValor();
					}
					if (apc.getModuloCampo().getId().equals(Constantes.MATERNO)) {
						maternoNo = " " + apc.getValor();
					}
					if (apc.getModuloCampo().getId().equals(Constantes.NO_NOTARIO)) {
						noNotario = " NOTARIO NO. " + apc.getValor();
					}
					if (apc.getModuloCampo().getId().equals(Constantes.ESTADO)) {
						estado = " DEL ESTADO " + apc.getValor();
					}
					if (apc.getModuloCampo().getId().equals(Constantes.MUNICIPIO)) {
						municipio = " EN EL MUNICIPIO DE " + apc.getValor();
					}
					// log.info("DATOSA1UI666 "+ nombre + paterno + materno );

					if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_NOMBRE_SOLICITANDO_CLG)) {
						nombre = apc.getValor();
					}
					if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_PRIMER_APELLIDO_CLG)) {
						paterno = " " + apc.getValor();
					}
					if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_SEGUNDO_APELLIDO_CLG)) {
						materno = " " + apc.getValor();
					}
					log.info("Aqui422 " + nombre + paterno + materno);

				}

				if (apc.getModuloCampo().getId().equals(Constantes.OBSERVACIONES)) {// 431
					clg.setObservacion(apc.getValor());
					log.debug("Entro " + apc.getValor());
				}
			}
			log.info("Aqui433. " + nombre + paterno + materno);
			if (isCLGAviso) {
				valor = nombre + noNotario + municipio + estado;
				// log.info("CLG+AVISO Solicita:{}", valor);
				clg.setSolicitante(valor);
			} else {
				if (tipo.equalsIgnoreCase("2125")) {
					valor = nombreNo + paternoNo + maternoNo + noNotario + estado + municipio;
					clg.setSolicitante(valor);
				} else {
					valor = nombre + paterno + materno;
					clg.setSolicitante(valor);
				}

			}

		}

		clg.setPrelacionId(prelacion.getId());
		clg.setConsecutivo(prelacion.getConsecutivo());
		clg.setOficina(prelacion.getOficina().getNombre());
		clg.setFechaIngreso(prelacion.getFechaIngreso());
		clg.setLugar(prelacion.getOficina().getNombre());
		// clg.setSolicitante(prelacion.getUsuarioSolicitan().getNombreCompleto());
		clg.setUsuarioSolicitanteId(prelacion.getUsuarioSolicitan().getId());
		clg.setFechaGenerado(new Date());

		// Cargar ultimo traslativo del predio
		// Acto actoTraslativo =
		// actoRepository.findUltimoTraslativoRegistradoByPredioId(predio.getId(),
		// Constantes.ClasifActo.TRASLATIVOS.getIdClasifActo(),
		// Constantes.StatusActo.ACTIVO.getIdStatusActo());
		List<Acto> actoTraslativo = actoService.getActosVigentesTraslativosParaPredio(predio.getId());
		log.info("actoTraslativo={}", actoTraslativo);
		// log.debug("acto_traslativo {}", actoTraslativo);

		// TODO: validar si se tiene que filtrar algun otro parametro

		/*
		 * if (actoTraslativo==null || actoTraslativo.getActoPrediosParaActos() == null
		 * || actoTraslativo.getActoPrediosParaActos().size()!=1){ throw new
		 * RequerimientoException("El acto traslativo  mas reciente del predio, no contiene un solo predio"
		 * ); }
		 */

		clg.setPredioId(predio.getId());
		clg.setNoFolio(predio.getNoFolio());
		if (predio.getAuxiliar() != null) {
			clg.setNoFolioAux("" + predio.getAuxiliar());
		} else {
			clg.setNoFolioAux(null);
		}

		if (predio.getNumeroFolioReal() != null) {
			clg.setNoFolioFutuReg("" + predio.getNumeroFolioReal());
			clg.setLeyendaFolio("IDENTIFICADOR EN SISTEMA");
		} else {
			clg.setLeyendaFolio("FOLIO UNICO REAL ELECTRONICO");
			clg.setNoFolioFutuReg(null);
		}

		if (actoTraslativo != null && actoTraslativo.size() > 0)
			clg.setFechaRegistro(actoTraslativo.get(actoTraslativo.size() - 1).getFechaRegistro());

		clg.setCodigoPostal(predio.getCodigoPostal());
		if (predio.getMunicipio() != null) {
			clg.setPredioEstado(predio.getMunicipio().getEstado().getNombre());
			clg.setPredioMunicipio(predio.getMunicipio().getNombre());
		}

		Set<Recibo> recibos = prelacion.getRecibosParaPrelacions();
		if (recibos.size() == 0) {

			clg.setRecibo(0);
			clg.setMonto(BigDecimal.ZERO);
			clg.setConcepto("CERTIFICADOS DE LIBERTAD DE GRAVAMEN");

			clg.setReferencia("0");
		} else {
			Recibo recibo = recibos.iterator().next();
			Set<ReciboConcepto> reciboConceptos = recibo.getReciboConceptosParaRecibos();
			/*
			 * if (reciboConceptos.size()!=1){ throw new
			 * RequerimientoException("El recibo de la prelacion tiene mas de un concepto");
			 * }
			 */
			// clg.setConcepto(reciboConceptos.iterator().next().getConceptoPago().getNombre());
			clg.setReferencia(recibo.getReferencia());
			clg.setRecibo(recibo.getNoRecibo());
			clg.setMonto(recibo.getMonto());

		}

		List<PropietarioVO> propietarios = new ArrayList<PropietarioVO>();
		// aqui va los777
		List<PredioPersona> prediosPersona = predioPersonaService.findPropietariosActuales(predio.getId(), false);
		String predioPersona1Nombre = "", predioPersona1Paterno = "", predioPersona1Materno = "";
		for (PredioPersona predioPersona1 : prediosPersona) {
			if (predioPersona1.getPersona().getMaterno() != null) {
				predioPersona1Materno = predioPersona1.getPersona().getMaterno();
			}
			if (predioPersona1.getPersona().getPaterno() != null) {
				predioPersona1Paterno = predioPersona1.getPersona().getPaterno();
			}
			predioPersona1Nombre = predioPersona1.getPersona().getNombre() + " " + predioPersona1Paterno + " "
					+ predioPersona1Materno;
			propietarios.add(new PropietarioVO(predioPersona1Nombre, predioPersona1.getPorcentajeDd(),
					predioPersona1.getPorcentajeUv()));
		} ////

		if (propietarios.isEmpty()) {
			throw new RequerimientoException("El predio no tiene propietarios");
		}
		ActoPredio actoPredio;
		try {
			actoPredio = actoTraslativo.get(actoTraslativo.size() - 1).getActoPrediosParaActos().iterator().next();

		} catch (Exception e) {
			// TODO: handle exception
			actoPredio = null;
			log.debug("no se encuentran  traslativos en el folio " + predio.getNoFolio() + "predio_id  "
					+ predio.getId());

		}

		clg.setPropietarios(propietarios);
		String lote = "";

		lote = predio.getNoLote();
		if (predio.getNoLote() != null) {
			lote = predio.getNoLote();
		} else {
			lote = "-";
		}
		clg.setNoLote(lote);
		clg.setManzana(predio.getManzana());
		clg.setUbicacion(predio.getDireccionComplete());
		clg.setDireccion(predio.getDireccionComplete());

		String superficeUnidad = predio.getUnidadMedida() != null ? predio.getUnidadMedida().getNombre() : "";
		clg.setSuperficie(predio.getSuperficie() + " " + superficeUnidad);
		/*
		 * log.debug("SUPERFICE =" + predio.getSuperficie());
		 * log.debug("ACTO_PREDIO_ID =" + actoPredio.getId() + " ACTO_ID=" +
		 * actoPredio.getActo().getId() + " PREDIO_ID=" +
		 * actoPredio.getPredio().getId());
		 */
		Set<Colindancia> col = colindanciaService.findColindancias(predio.getId());
		List<ColindanciaVO> colindancias = new ArrayList<ColindanciaVO>();
		List<String> gravamenes = new ArrayList<String>();
		List<String> limitantes = new ArrayList<String>();
		List<String> avisos = new ArrayList<String>();

		for (Colindancia colindancia : col) {
			colindancias.add(new ColindanciaVO(colindancia.getOrientacion().getId(),
					colindancia.getOrientacion().getNombre(), colindancia.getNombre()));
			System.out.println("colindacia" + colindancia.getOrientacion().getId() + " "
					+ colindancia.getOrientacion().getNombre());
		}

		/*
		 * if (colindancias.isEmpty()) { throw new
		 * RequerimientoException("El predio no tiene colindancias"); }
		 */
		if (!colindancias.isEmpty()) {
			clg.setColindancias(colindancias);
		}
		System.out.println("Colindancias " + colindancias);
		GravamenCountVO gravamenCountVO;
		List<GravamenCountVO> gravamenCountList = new ArrayList<GravamenCountVO>();
		List<Object[]> countingGravamenes = actoRepository
				.countGravamenesByPredioId(Constantes.StatusActo.ACTIVO.getIdStatusActo(), predio.getId());

		Acto acto1 = null;
		ActoPredio actoPredio1 = null;
		ActoPredioCampo actoPredioCampo1 = null;
		NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
		DecimalFormat formateador = new DecimalFormat("###,###.##");
		for (Object[] countGravamen : countingGravamenes) {

			gravamenCountVO = new GravamenCountVO();

			System.out.println("countGravamen[0] " + countGravamen[0]);
			System.out.println("countGravamen[1] " + countGravamen[1]);
			System.out.println("countGravamen[2] " + countGravamen[2]);
			gravamenCountVO.setNombre((String) countGravamen[0]);
			// gravamenCountVO.setCounting(((BigInteger) countGravamen[1]).intValue());
			Long actoId = Long.parseLong(countGravamen[2].toString());
			HashMap<Integer, String> nombreG = new HashMap<Integer, String>();
			HashMap<Integer, String> paternoG = new HashMap<Integer, String>();
			HashMap<Integer, String> maternoG = new HashMap<Integer, String>();

			String montoG = "", tipoG = "", montoG3 = " ", tipoMonto = "";
			String DIA = "", MES = "", ANIO = "";
			String DocumentoG = "", BisG = "", OficinaG = "", AnioG = "", SeccionG = "", LibroG = "", TomoG = "",
					VolumenG = "", antecedentesCompleto = "";

			DecimalFormat formatoPesos = new DecimalFormat("###,###,###.######");
			Boolean montoG_Aux = false;
			Double montoG2 = 0.0;

			acto1 = actoRepository.findOne(actoId);

			actoPredio1 = acto1.getActoPrediosParaActosOrderedByVersion().first();
			Set<ActoPredioCampo> actoPredioCampos1 = actoPredio1.getActoPredioCamposParaActoPredios();

			int numActo = 0, clasif = 0;
			String fechaG = "";
			boolean vf = false, auxVf = false;
			try {

				// log.debug("7472BBBB2 {}",actoPredioCampos1);

				numActo = acto1.getTipoActo().getId().intValue();
				clasif = acto1.getTipoActo().getClasifActo().getId().intValue();
				vf = acto1.getVf() != null && acto1.getVf() ? true : false;
				if (acto1.getVf() == null || !acto1.getVf()) {
					// PARA PRIMEROS AVISOS TOMA FECHA DE INGRESO DE LA ENTRADA
					if (acto1.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO)
							|| acto1.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO2)) {
						fechaG = acto1.getPrelacion().getFechaIngreso().toString();
					} else {
						fechaG = acto1.getFechaRegistro().toString();
					}
				}

				// actoPredioCampos1.forEach((actoPredioCampo) ->

			} catch (NullPointerException e) {
				// TODO: handle exception

			}
			String tipoDeMoneda = "";

			HashMap<Integer, Persona> acreedores = new HashMap<Integer, Persona>();

			for (ActoPredioCampo actoPredioCampo : actoPredioCampos1)

			{
				try {

					int campoId = actoPredioCampo.getModuloCampo().getId().intValue();
					int index = actoPredioCampo.getOrden();

					if (actoPredioCampo.getModuloCampo().getCampo().getId() == 265L) {
						tipoDeMoneda = boletaRegistralService.obtenerValor(actoPredioCampo);
					}

					if (campoId == 492) {
						tipoMonto = actoPredioCampo.getValor();
						CampoValores cv = campoValoresRepository.findOne(Long.parseLong(actoPredioCampo.getValor()));
						tipoG = cv.getNombre();
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
								tipoDeMoneda = boletaRegistralService.obtenerValor(actoPredioCampo);
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
								tipoDeMoneda = boletaRegistralService.obtenerValor(actoPredioCampo);
							}
						break;
			
						
						case 66:// DEMANDAS Y/O NOTIFICACIONES DE JUICIOS DE AMPARO

							switch (campoId) {
								case 252:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 253:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 775:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							break;
						case 234:// ARRENDAMIENTO
							if (campoId == 89) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								/// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
								;
							}
							switch (campoId) {
								case 106:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 107:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 108:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}

							break;

						case 34:// ARRENDAMIENTO FINANCIERO

							switch (campoId) {
								case 106:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 107:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 108:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 89) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
								;
							}

							break;

						case 31:// CÉDULA HIPOTECARIA
							if (campoId == 187) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}
							switch (campoId) {
								case 843:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 844:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 845:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}

							break;

						case 32:// embargo
							if (campoId == 20792) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}
							switch (campoId) {
								case 843:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 844:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 845:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}

							break;
						case 115:// embargo CON PRORROGA
							if (campoId == 20792) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
								;
							}
							switch (campoId) {
								case 273:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 186:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 794:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}

							break;
						case 37:// FIANZA
							if (campoId == 89) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
								;
							}
							switch (campoId) {
								case 106:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 107:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 108:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}

							break;
						case 243:// ANOTACIÓN PREVENTIVA DE EMBARGO
							if (campoId == 20792) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
								;
							}
							switch (campoId) {
								case 843:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 844:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 845:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}

							break;
						case 217:// NOVACIÓN DE CONTRATO

							switch (campoId) {
								case 925:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 20792) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}

							break;

						/*
						 * case 228://ANOTACIÓN PREVENTIVA DE DEMANDA switch (campoId) { case
						 * 843:nombreG=actoPredioCampo.getValor(); break; case
						 * 844:paternoG=actoPredioCampo.getValor(); break; case
						 * 845:maternoG=actoPredioCampo.getValor(); break; } break;
						 */

						case 55:// COMODATO
							switch (campoId) {
								case 925:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 926:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 927:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							break;

						case 227:// CESION DE DERECHOS CREDITICIOS 1013 ,1014, 1015
							switch (campoId) {
								case 925:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 926:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 927:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							break;
						case 38:// ASEGURAMIENTO DE BIENES 925, 926, 927

							if (campoId == 20792) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}
							switch (campoId) {
								case 273:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 186:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 794:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}

							break;
						case 35:// CONSTITUCION DE PATRIMONIO FAMILIAR 252 ,253 ,775
							switch (campoId) {
								case 106:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 107:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 108:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							break;
						case 230:// CONTRATO DE OCUPACIÓN SUPERFICIAL 106 ,107 ,108
							switch (campoId) {
								case 106:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 107:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 108:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 124) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}

							break;
						/*
						 * case 118://ANOTACION DE DEMANDA switch (campoId) { case
						 * 273:nombreG=actoPredioCampo.getValor(); break; case
						 * 186:paternoG=actoPredioCampo.getValor(); break; case
						 * 794:maternoG=actoPredioCampo.getValor(); break; } break;
						 */
						case 98:// PRENDA
							switch (campoId) {
								case 843:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 844:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 845:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 124) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}

							break;
						case 14:// FIDECOMISO 479, 723, 724
							switch (campoId) {
								case 479:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 723:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 724:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 20792 || campoId == 89) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}

							break;
						case 64:// CONVENIO MODIFICATORIO
							switch (campoId) {
								case 843:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 844:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 845:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 20792) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}

							break;
						case 48:// SERVIDUMBRE 942 ,943, 944
							switch (campoId) {
								case 942:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 943:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 944:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							break;

						case 121:// retencion
							switch (campoId) {
								case 273: // nombre
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 186: // paterno
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 794: // materno
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 187) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor());// actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = " EN GARANTIA DE PAGO POR LA CANTIDAD DE " + montoG;
							}

							break;

						case 219:// USUFRUTO 20180 ,20181 ,20182, 20733
							switch (campoId) {
								case 537:
								case 20733:
								case 20180:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 20181:
								case 538:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 20182:
								case 539:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							break;
						case 223: // RESERVA DE DOMINIO
							switch (campoId) {
								case 537:
								case 1009:
								case 20180:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 1010:
								case 538:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 1011:
								case 539:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 89 && actoPredioCampo.getValor() != null
									&& !actoPredioCampo.getValor().trim().isEmpty()) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}
							break;
						case 9:// CRÉDITO O HIPOTECA
							switch (campoId) {
								case 843:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 844:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 845:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							if (campoId == 124) {
								montoG = this.montoGravamenes(actoPredio.getActo(), actoPredioCampo.getValor()); // actoPredioCampo.getValor();
								// montoG2 = new Double(montoG);
								montoG = "POR LA CANTIDAD DE " + montoG;
							}

							break;
						default:// 843, 844, 845

							int auxMonto = 0;
							// 360,361,376,689
							if (!montoG.isEmpty()) {

								if (tipoMonto.contains("360") || tipoMonto.contains("361") || tipoMonto.contains("389")
										|| tipoMonto.contains("376")) {
									montoG = " EN GARANTIA DE PAGO POR LA CANTIDAD DE " + (montoG);
									// montoG_Aux=true;

								} else {
									//formatoImporte = NumberFormat.getCurrencyInstance();
									//formateador = new DecimalFormat("###,###.##");
									//formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
									montoG = " EN GARANTIA DE PAGO POR LA CANTIDAD DE "
											+ CommonUtil.formatMoney(montoG);
									// montoG_Aux=true;
								}
							}
							switch (campoId) {
								case 843:
									nombreG.put(index, actoPredioCampo.getValor());
									break;
								case 844:
									paternoG.put(index, actoPredioCampo.getValor());
									break;
								case 845:
									maternoG.put(index, actoPredioCampo.getValor());
									break;
							}
							break;
					}// actos
					if (vf == true) {
						fechaG = "";
						/* 20720 DIA, 20721 MES, 20722 AÑO */
						switch (campoId) {
							case 20720:
								DIA = actoPredioCampo.getValor();
								break;
							case 20721:
								try {
									CampoValores cv = campoValoresRepository
											.findOne(Long.parseLong(actoPredioCampo.getValor()));
									MES = cv.getNombre();
								} catch (Exception e) {
								}

								break;
							case 20722:
								ANIO = actoPredioCampo.getValor();
								break;///
							case 20146:
								DocumentoG = actoPredioCampo.getValor();
								break;
							case 20144:
								AnioG = actoPredioCampo.getValor();
								break;
							case 20143:
								CampoValores cv2 = campoValoresRepository
										.findOne(Long.parseLong(actoPredioCampo.getValor()));
								SeccionG = cv2.getNombre();
								break;
							case 20142:
								LibroG = actoPredioCampo.getValor();
								break;
							case 20141:
								TomoG = actoPredioCampo.getValor();
								break;
							case 20145:
								VolumenG = actoPredioCampo.getValor();
								break;
							case 20160:
								BisG = actoPredioCampo.getValor();
								break;
						}
						fechaG = DIA + "/" + MES + "/" + ANIO;
						auxVf = true;

						antecedentesCompleto = " INSCRITO BAJO EL NUMERO " + DocumentoG + " "
								+ (BisG.contains("true") ? "BIS" : "") + " AÑO " + AnioG + " SECCION " + SeccionG
								+ " LIBRO " + LibroG + " TOMO " + TomoG + " VOLUMEN " + VolumenG + " ";
						// Documento="",Bis="",Anio="",Seccion="",Libro="",Tomo="",Volumen="";

					} // vf 2
						// else{log.debug("NO ENTRA REVISA2324 "+vf);}

				} catch (NullPointerException e) {
					// TODO: handle exception
					log.debug("ACTO_PREDIO_CAMPO NULL REVISA");
				}
			}

			try {
				if (!auxVf) {

					// actoPredio1.getActo().getPrelacion().getConsecutivo();
					antecedentesCompleto = " INSCRITO BAJO LA ENTRADA "
							+ actoPredio1.getActo().getPrelacion().getConsecutivo() + " ";

				}
			} catch (NullPointerException e) {
				log.debug("ANTECEDENTES NULL REVISA");
				// TODO: handle exception
			}

			// String cActoDetalle=this.construlleActoDetalle(acto1,actoPredioCampos1)+"\n";
			/*
			 * gravamenCountVO.setNombre((String) countGravamen[0] + " EN FAVOR DE " +
			 * nombreG + " " + paternoG + " " + maternoG + "," + montoG +
			 * antecedentesCompleto + " DE FECHA " + fechaG + "\n");
			 */
			StringBuilder afavorDe = new StringBuilder();
			nombreG.forEach((index, name) -> {
				if (name != null && !name.isEmpty())
					afavorDe.append(name);

				String apaterno = paternoG.containsKey(index) ? paternoG.get(index) : "";
				String amaterno = maternoG.containsKey(index) ? maternoG.get(index) : "";
				if (apaterno != null && !apaterno.isEmpty())
					afavorDe.append(" ").append(apaterno);
				if (amaterno != null && !amaterno.isEmpty())
					afavorDe.append(" ").append(amaterno);

				afavorDe.append(", ");

			});

			gravamenCountVO.setNombre((String) countGravamen[0] + " EN FAVOR DE " + afavorDe.toString().toUpperCase()
					+ "," + montoG + (tipoDeMoneda != null && !tipoDeMoneda.isEmpty() ? " " + tipoDeMoneda : "")
					+ antecedentesCompleto + " DE FECHA " + fechaG + "\n");

			// gravamenCountVO.setNombre((String) countGravamen[0] +"A favor de: ");

			gravamenCountList.add(gravamenCountVO);
		}

		clg.setGravamenCount(gravamenCountList.size() > 0 ? gravamenCountList : null);

		LimitacionCountVO limitacionCountVO;
		LimitacionCountVO observacionCountVO;
		LimitacionCountVO pasesCountVO;
		List<LimitacionCountVO> limitacionCountList = new ArrayList<LimitacionCountVO>();
		List<LimitacionCountVO> observacionCountList = new ArrayList<LimitacionCountVO>();
		List<LimitacionCountVO> pasesCountList = new ArrayList<LimitacionCountVO>();
		// log.debug("esto busca " + Constantes.StatusActo.ACTIVO.getIdStatusActo() + "
		// - " + predio.getId());
		List<Object[]> countingLimitaciones = actoRepository
				.countLimitacionesByPredioId(Constantes.StatusActo.ACTIVO.getIdStatusActo(), predio.getId()); // predio.getId()
		log.debug("ESTO ES countingLimitaciones {}", countingLimitaciones);
		// );
		Acto acto2 = null;
		ActoPredio actoPredio2 = null;
		ActoPredioCampo actoPredioCampo2 = null;
		String numNotario = "";
		String municipioDocumento = "";
		String representantePorCodigoPublico = "";
		String tipoDoc = "";
		String numeroDoc = "";
		String autoridad = "";

		List<Pases> pasesList = pasesRepository.findByPredioOrigenId(predio.getId());
		for (Object[] countLimitacion : countingLimitaciones) {
			log.info("countLimitacion{}" + countLimitacion);
			String entrada;
			String fechafirma = "";
			String observacionesAnotacion = "";
			Boolean auxIsNull = false;
			limitacionCountVO = new LimitacionCountVO();
			observacionCountVO = new LimitacionCountVO();
			Long actoId = Long.parseLong(countLimitacion[2].toString());
			String nombreG = "", paternoG = "", maternoG = "", montoG = " ", tipoG = "";
			String DIA = "", MES = "", ANIO = "";
			String DocumentoG = "", BisG = "", OficinaG = "", AnioG = "", SeccionG = "", LibroG = "", TomoG = "",
					VolumenG = "", antecedentesCompleto = "";

			acto2 = actoRepository.findOne(actoId);

			actoPredio2 = acto2.getActoPrediosParaActosOrderedByVersion().first();
			Set<ActoPredioCampo> actoPredioCampos2 = actoPredio2.getActoPredioCamposParaActoPredios();
			int clasif = 0;
			String fechaG = "";
			boolean vf = false, auxVf = false;

			try {
				log.debug("7472BBBB2 {}", actoPredioCampos2);

				clasif = acto2.getTipoActo().getClasifActo().getId().intValue();
				vf = acto2.getVf() != null && acto2.getVf() ? true : false;
				if (acto2.getVf() == null || !acto2.getVf()) {
					if (acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO)
							|| acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_PRIMER_AVISO2)) {
						fechaG = acto2.getPrelacion().getFechaIngreso().toString();
					} else {
						fechaG = acto2.getFechaRegistro().toString();
					}

				}

				log.debug("acto_predio {}", acto2.getActoPrediosParaActosOrderedByVersion().first().getId());

			} catch (NullPointerException e) {
				// TODO: handle exception

			}

			if (acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_ANOTACION)) {
				for (ActoPredioCampo actoPredioCampo : actoPredioCampos2) {
					try {
						int campoId = actoPredioCampo.getModuloCampo().getCampo().getId().intValue();
						if (campoId == 243)
							observacionesAnotacion = " " + actoPredioCampo.getValor();

					} catch (NullPointerException e) {
						// TODO: handle exception
						log.debug("ACTO_PREDIO_CAMPO NULL REVISA");
					}
				}
			}			
			Long[] tiposNotVf = { Constantes.TIPO_ACTO_ANOTACION };

			if (vf == true) {
				if (!Arrays.asList(tiposNotVf).contains(acto2.getTipoActo().getId())) {
					for (ActoPredioCampo actoPredioCampo : actoPredioCampos2) {
						try {

							int campoId = actoPredioCampo.getModuloCampo().getId().intValue();
							fechaG = "";
							/* 20720 DIA, 20721 MES, 20722 AÑO */
							switch (campoId) {
								case 20720:
									DIA = actoPredioCampo.getValor();
									break;
								case 20721:
									CampoValores cv = campoValoresRepository
											.findOne(Long.parseLong(actoPredioCampo.getValor()));
									MES = cv.getNombre();
									break;
								case 20722:
									ANIO = actoPredioCampo.getValor();
									break;///
								case 20146:
									DocumentoG = actoPredioCampo.getValor();
									break;
								case 20144:
									AnioG = actoPredioCampo.getValor();
									break;
								case 20143:
									CampoValores cv2 = campoValoresRepository
											.findOne(Long.parseLong(actoPredioCampo.getValor()));
									SeccionG = cv2.getNombre();
									break;
								case 20142:
									LibroG = actoPredioCampo.getValor();
									break;
								case 20141:
									TomoG = actoPredioCampo.getValor();
									break;
								case 20145:
									VolumenG = actoPredioCampo.getValor();
									break;
								case 20160:
									BisG = actoPredioCampo.getValor();
									break;
							}
							fechaG = DIA + "/" + MES + "/" + ANIO;
							auxVf = true;
							if (acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_ANOTACION_DOMINIO_PARCIAL)
									|| acto2.getTipoActo().getId()
											.equals(Constantes.TIPO_ACTO_MODIFICACION_MEDIDAS_LINDEROS)) {
								antecedentesCompleto = " INSCRITA BAJO EL NUMERO " + DocumentoG + " "
										+ (BisG.contains("true") ? "BIS" : " ") + " CON AÑO " + AnioG + " SECCIÓN "
										+ SeccionG + " LIBRO " + LibroG + " TOMO " + TomoG + " VOLUMEN " + VolumenG
										+ " ";
							} else {
								antecedentesCompleto = " INSCRITO BAJO EL NUMERO " + DocumentoG + " "
										+ (BisG.contains("true") ? "BIS" : " ") + " CON AÑO " + AnioG + " SECCIÓN "
										+ SeccionG + " LIBRO " + LibroG + " TOMO " + TomoG + " VOLUMEN " + VolumenG
										+ " ";
							}

							// Documento="",Bis="",Anio="",Seccion="",Libro="",Tomo="",Volumen="";

						} catch (NullPointerException e) {
							// TODO: handle exception
							log.debug("ACTO_PREDIO_CAMPO NULL REVISA");
						}
					}
				} else {
					auxVf = true;
				}
			} // vf 2
			else {
				log.debug("NO ENTRA REVISA2324 " + vf);
			}
			try {
				if (!auxVf) {

					if (acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_ANOTACION_DOMINIO_PARCIAL)
							|| acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_MODIFICACION_MEDIDAS_LINDEROS)) {
						antecedentesCompleto = " INSCRITA BAJO LA ENTRADA "
								+ actoPredio2.getActo().getPrelacion().getConsecutivo() + " ";
					} else {
						antecedentesCompleto = " INSCRITO BAJO LA ENTRADA "
								+ actoPredio2.getActo().getPrelacion().getConsecutivo() + " ";
					}

				}
			} catch (NullPointerException e) {
				log.debug("ANTECEDENTES NULL REVISA");
				// TODO: handle exception
			}
			if (acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_ANOTACION_DOMINIO_PARCIAL)) {
				antecedentesCompleto = "FRACCIÓN SEGREGADA" + antecedentesCompleto + observacionesAnotacion
						+ (fechaG != null && !fechaG.trim().isEmpty() && !fechaG.replace("/", "").trim().isEmpty()
								? " DE FECHA " + fechaG
								: "");
			} else {
				antecedentesCompleto = (String) countLimitacion[0] + antecedentesCompleto + observacionesAnotacion
						+ (fechaG != null && !fechaG.trim().isEmpty() && !fechaG.replace("/", "").trim().isEmpty()
								? " DE FECHA " + fechaG
								: "");
			}

			if (acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_ANOTACION_RECTIFICACION)) {
				String obs = "";
				String nom = "";
				String ap = "";
				String am = "";
				for (ActoPredioCampo actoPredioCampo : actoPredioCampos2) {
					try {
						int campoId = actoPredioCampo.getModuloCampo().getCampo().getId().intValue();
						switch (campoId){
							case 243:  // OBSERVACIONES
								obs = actoPredioCampo.getValor();
							break;
							case 1286:  // NOMBRE
								nom =" SOLICITADO POR: "+ actoPredioCampo.getValor();
							break;
							case 1287:  // PRIMER APELLIDO
								ap =" "+ actoPredioCampo.getValor();
							break;
							case 1288:  // SEGUNDO APELLIDO
								am =" "+ actoPredioCampo.getValor();
							break;
						}
					} catch (NullPointerException e) {						
						log.debug("ACTO_PREDIO_CAMPO NULL PARA: ANOTACION-RECTIFICACION");
					}
				}
				antecedentesCompleto = antecedentesCompleto + nom + ap + am + ". " + obs;
			}

			if (actoPredio2.getActo().getTipoActo().getId().equals(49L)
					|| actoPredio2.getActo().getTipoActo().getId().equals(50L)
					|| actoPredio2.getActo().getTipoActo().getId().equals(210L)
					|| actoPredio2.getActo().getTipoActo().getId().equals(124L)) {
				antecedentesCompleto += this.getDetalleAviso(actoPredioCampos2, acto2);
			}

			if (acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_ANOTACION_DOMINIO_PARCIAL)
					|| acto2.getTipoActo().getId().equals(Constantes.TIPO_ACTO_MODIFICACION_MEDIDAS_LINDEROS)) {
				observacionCountVO.setNombre(antecedentesCompleto);
				observacionCountVO.setCounting(((BigInteger) countLimitacion[1]).intValue());
				observacionCountList.add(observacionCountVO);
			} else {
				limitacionCountVO.setNombre(antecedentesCompleto);

				limitacionCountVO.setCounting(((BigInteger) countLimitacion[1]).intValue());
				limitacionCountList.add(limitacionCountVO);
				log.debug("{}", limitacionCountVO);
			}

		} // fin for

		clg.setLimitacionCount(limitacionCountList.size() > 0 ? limitacionCountList : null);
		clg.setObservacionCount(observacionCountList.size() > 0 ? observacionCountList : null);
		List<Acto> lim = actoRepository.findActivosByStatusActoAndClasifActoAndSubClasificacionAndPredioId(
				Constantes.StatusActo.ACTIVO.getIdStatusActo(), predio.getId());
		lim.forEach((acto) -> {
			limitantes.add(acto.getTipoActo().getNombre());
		});
		if (limitantes.size() > 0) {
			clg.setLimitaciones(String.join(",", avisos));
		} else {
			clg.setLimitaciones("NO REPORTA LIMITACIONES");
		}

		log.debug("limitaciones=" + clg.getLimitaciones());
		// encontrar Avisos vigentes

		List<ClasifActo> clasifActoList = new ArrayList<>(2);
		clasifActoList.add(clasifActoRepository.getOne(Constantes.ClasifActo.ANOTACIONES.getIdClasifActo()));
		List<Acto> sv = actoRepository.findDistinctByStatusActoIdAndActoPrediosParaActosPredioIdAndTipoActoClasifActoIn(
				Constantes.StatusActo.ACTIVO.getIdStatusActo(), predio.getId(), clasifActoList);
		sv.forEach((acto) -> {
			avisos.add(acto.getTipoActo().getNombre());
		});
		// clg.setAvisos(String.join(",", avisos));

		log.debug("avisos =" + clg.getAvisos());

		List<PasesVO> pases = new ArrayList<PasesVO>();

		log.debug("====> pases - predio1 = " + predio.getId());
		String superficie;

		log.debug("aqui va 1");
		for (Pases pase : pasesList) {
			superficie = pase.getSuperficie();
			pases.add(new PasesVO(superficie, pase.getPredioSegre().getNoFolio()));
		}
		log.debug("aqui va 2");
		clg.setPases(pases.size() > 0 ? pases : null);

		if (pasesList != null) {
			Map<Long, Pases> map = new HashMap<Long, Pases>(pasesList.size());
			List<Pases> pasesListClean = new ArrayList<Pases>();
			for (Pases r : pasesList) {
				map.put(r.getPredioSegre().getId(), r);
			}
			for (Entry<Long, Pases> r : map.entrySet()) {
				pasesListClean.add(r.getValue());
			}

			for (Pases p : pasesListClean) {
				pasesCountVO = new LimitacionCountVO();
				String pass = "";
				Predio pSegregado = p.getPredioSegre();
				List<PredioPersona> prediopresonas = predioPersonaService.findPropietariosActuales(pSegregado.getId(),
						false);
				if (prediopresonas != null) {
					pass = "FRACCIÓN SEGREGADA A FAVOR DE";
					for (PredioPersona pp : prediopresonas) {
						pass += " " + pp.getPersona().getNombre() + " ";
						pass += pp.getPersona().getPaterno() != null ? pp.getPersona().getPaterno() + " " : "";
						pass += pp.getPersona().getMaterno() != null ? pp.getPersona().getMaterno() + ", " : ",";
					}
				}
				pass += " CON SUPERFICIE " + pSegregado.getSuperficie() + " "
						+ pSegregado.getUnidadMedida().getNombre();
				pass += " BAJO EL FOLIO REAL " + pSegregado.getNoFolio();

				pasesCountVO.setNombre(pass);
				pasesCountVO.setCounting(1);
				pasesCountList.add(pasesCountVO);
			}
		}
		clg.setPasesCount(pasesCountList.size() > 0 ? pasesCountList : null);
		clg.setSecuencia(0);
		clg.setHistorial("");
		clg.setUsuarioCoordinador("");

		clg.setUsuarioRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
		clg.setUsuarioCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
		clg.setUsuarioAutorizo(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
		// FIRMA ELECTRONICA REGISTRADOR
		clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));
		clg.setOficina(prelacion.getOficina().getNombre());
		log.debug("OFICINA --- " + prelacion.getOficina().getNombre());
		// FIRMA ELECTRONICA COORDINADOR
		String firmaCoord = null;
		String firmaCoord2 = "-SIN FIRMA -";
		if (prelacion.getFirma() != null) {
			firmaCoord = prelacion.getFirma();
		}
		/*
		 * if (firmaCoord != null) { firmaCoord =
		 * firmaCoord.substring(firmaCoord.length() - 15, firmaCoord.length());
		 * System.out.println("Primera " + firmaCoord.substring(0, 10));
		 * System.out.println("Segunda " + firmaCoord.substring(firmaCoord.length() - 5,
		 * firmaCoord.length())); firmaCoord2 = firmaCoord.substring(0, 10) + "-" +
		 * firmaCoord.substring(firmaCoord.length() - 5, firmaCoord.length());
		 * System.out.println("TERCERA " + firmaCoord2); }
		 */
		////////// SECCION V ENTRADAS PRESENTADAS QUE NO SE ENCUENTRAN EN
		////////// PROCESO---------------------------------------------------------------
		StringBuilder sbpendientes = new StringBuilder();
		String mensaje = "Entradas encontradas en el proceso:";
		boolean existeRegistro = false;
		List<Prelacion> prelacionPredios = prelacionPredioService.findPrelacionesEnProcesoByPredio(clg.getPredioId(),
				prelacion.getId());
		for (Prelacion _prelacion : prelacionPredios) {
			sbpendientes.append(" " + _prelacion.getConsecutivo() + " ");
			existeRegistro = true;
		}
		if (existeRegistro == false) {
			mensaje = "No hay entradas pendientes";
		}

		clg.setPolitica(mensaje + sbpendientes.toString());

		////////// FIN SECCION V ENTRADAS PRESENTADAS QUE NO SE ENCUENTRAN EN

		clg.setFirmaCoordinador(firmaCoord);
		return clg;

	}

	private String getDetalleAviso(Set<ActoPredioCampo> actoPredioCampo, Acto acto) {
		String result = "";
		if (acto.getTipoActo().getId().equals(210L) || acto.getTipoActo().getId().equals(124L)) {
			result += this.getContratantesAviso(actoPredioCampo);
			result += this.getNotarioAvisoPredioCampo(actoPredioCampo);
		}

		if (acto.getTipoActo().getId().equals(49L) || acto.getTipoActo().getId().equals(50L)) {
			result += this.getContratantesAviso(actoPredioCampo);
			Optional<ActoDocumento> ad = actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
			if (ad.isPresent()) {
				if (ad.get().getDocumento().getNotario() != null) {
					result += this.parseNotarioAviso(ad.get().getDocumento().getNotario());
				}
			}
		}
		return result;
	}

	private String getContratantesAviso(Set<ActoPredioCampo> actoPredioCampo) {
		StringBuilder stContratantes = new StringBuilder(" A FAVOR DE ");
		HashMap<Integer, Persona> contratantes = new HashMap<Integer, Persona>();
		try {
			actoPredioCampo.forEach(x -> {
				long moduloCampoId = x.getModuloCampo().getId();
				int index = x.getOrden();
				if (moduloCampoId == Constantes.AVISO_MODULO_CAMPO_ID_PATERNO
						|| moduloCampoId == Constantes.AVISO_MODULO_CAMPO_ID_NOMBRE
						|| moduloCampoId == Constantes.AVISO_MODULO_CAMPO_ID_MATERNO) {
					Persona persona = contratantes.get(index);
					if (persona == null) {
						persona = new Persona();
						contratantes.put(index, persona);
					}

					if (moduloCampoId == Constantes.AVISO_MODULO_CAMPO_ID_NOMBRE) {
						persona.setNombre(x.getValor());
					}

					if (moduloCampoId == Constantes.AVISO_MODULO_CAMPO_ID_PATERNO) {
						persona.setPaterno(x.getValor());
					}

					if (moduloCampoId == Constantes.AVISO_MODULO_CAMPO_ID_MATERNO) {
						persona.setMaterno(x.getValor());
					}
				}
			});

			for (Map.Entry<Integer, Persona> entry : contratantes.entrySet()) {
				Persona pp = (Persona) entry.getValue();
				if (pp != null) {

					stContratantes.append(pp.getNombre() != null ? pp.getNombre() + " " : "");
					stContratantes.append(pp.getPaterno() != null ? pp.getPaterno() + " " : "");
					stContratantes.append(pp.getMaterno() != null ? pp.getMaterno() : "");
					stContratantes.append(", ");

				}
			}
		} catch (Exception e) {

		}
		return stContratantes.toString();
	}

	private String getNotarioAvisoPredioCampo(Set<ActoPredioCampo> actoPredioCampo) {
		StringBuilder stNotario = new StringBuilder();
		try {
			actoPredioCampo.forEach(x -> {
				Constantes.TipoCampo tipoCampo = Constantes.TipoCampo
						.getTipoCampo(x.getModuloCampo().getCampo().getTipoCampo().getId());
				if (tipoCampo == Constantes.TipoCampo.NOTARIO && x.getValor() != null) {
					Notario notario = notarioRepository.findById(new Long(x.getValor().trim()));
					if (notario != null) {
						stNotario.append(this.parseNotarioAviso(notario));
					}

				}
			});

		} catch (Exception e) {

		}
		return stNotario.toString();
	}

	private String parseNotarioAviso(Notario notario) {
		StringBuilder stNotario = new StringBuilder();
		stNotario.append(" OTORGADO POR LA NOTARIA No.");
		stNotario.append(notario.getNoNotario());
		if (notario.getMunicipio() != null) {
			stNotario.append(" DE ").append(notario.getMunicipio().getNombre()).append(", ")
					.append(notario.getMunicipio().getEstado().getNombre().trim()).append(". ");
		}
		stNotario.append("NOTARIO PÚBLICO ").append(notario.getNombreCompleto()).append(".");

		return stNotario.toString();
	}

	private String construlleActoDetalle(Acto acto, Set<ActoPredioCampo> apcs) {
		ArrayList<Acto> actosRecolectados = new ArrayList<Acto>();
		ArrayList<ActoPredioCampo> apcRecolectados = new ArrayList<ActoPredioCampo>();
		ArrayList<ActoModulo> actoModulos = new ArrayList<ActoModulo>();
		Set<Modulo> modulosRecolectados = new HashSet();
		StringBuilder stringBuilder = new StringBuilder();
		TipoActo tipoActo = null;
		// String check="";
		Long idActo = -1l;
		if (acto != null) {
			tipoActo = acto.getTipoActo();
		}
		for (ActoPredioCampo apc : apcs) {
			modulosRecolectados.add(apc.getModuloCampo().getModulo());
		}

		List<ModuloTipoActo> mtasRecolectados = moduloTipoActoRepository
				.findAllByModuloInOrderByOrdenAsc(modulosRecolectados, tipoActo.getId());

		Long idAux = 0L;
		for (int i = 0; i < mtasRecolectados.size(); i++) {

			ModuloTipoActo mta = mtasRecolectados.get(i);
			if ((i + 1) < modulosRecolectados.size()) {
				idAux = mtasRecolectados.get(i + 1).getModulo().getId();
			}

			if (idAux != mta.getModulo().getId()) {
				ActoModulo am = new ActoModulo();
				am.setActo(acto);
				am.setModuloTipoActo(mta);
				actoModulos.add(am);
			}

		}
		for (ActoModulo am : actoModulos) {

			// encabezados
			Integer imprimirEncabezdo = am.getModuloTipoActo().getInd_impresion();// cambiar
			// System.out.println("imprimirEncabezdo "+imprimirEncabezdo);
			if (imprimirEncabezdo == null || imprimirEncabezdo == 1) {
				String encabezado = am.getModuloTipoActo().getEtiqueta();
				stringBuilder.append("\n " + encabezado + " \n");
			} else {
				stringBuilder.append("\n ");
			}

			// llenado de campos
			ArrayList<ActoPredioCampo> apcRe = getActoPredioCampo(am.getActo());
			for (ActoPredioCampo apcAux : apcRe) {
				if (apcAux.getModuloCampo().getModulo().getId()
						.compareTo(am.getModuloTipoActo().getModulo().getId()) == 0) {
					stringBuilder.append(modoDeImpresion(apcAux));
				}
			}

		}
		// System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}

	private ArrayList<ActoPredioCampo> getActoPredioCampo(Acto acto) {
		ArrayList<ActoPredioCampo> actoPredioCampos = null;
		if (acto != null) {
			actoPredioCampos = new ArrayList();
			ActoPredio actoPredio = actoPredioRepository.findActoPredioByLastVersion(acto.getId());
			// log.debug("BolRegSer:356 {}", actoPredio);
			for (ActoPredioCampo apc : actoPredioCampoRepository.findAllByActoPredioOrderById(actoPredio)) {
				actoPredioCampos.add(apc);
				// log.debug("BolRegSer:360{}", apc);s
			}
		}
		return actoPredioCampos;
	}

	private String modoDeImpresion(ActoPredioCampo apc) {
		String cadena = "";

		Integer ind_impresion = apc.getModuloCampo().getInd_impresion();

		String val = boletaRegistralService.obtenerValor(apc);
		String etiquetaCampo = apc.getModuloCampo().getEtiqueta();

		if (ind_impresion == null) {
			cadena = "  " + etiquetaCampo + ":" + val;
			return cadena;
		}
		if (ind_impresion == 0) {
			cadena = " ";
			return cadena;
		}
		if (ind_impresion == 1) {
			cadena = "  " + etiquetaCampo + ":" + val;
			return cadena;
		}
		if (ind_impresion == 2) {
			cadena = "  " + val;
			return cadena;
		}
		return cadena;
	}

	private List<ActoVO> getActosServicio(Prelacion prelacion) {
		PrelacionServicio s = prelacion.getPrelacionServiciosParaPrelacions().iterator().next();
		List<Acto> actos = null;
		List<ActoVO> _result = new ArrayList<ActoVO>();
		if (s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_ID.getIdServicio())
			actos = actoRepository.findAllByPrelacionIdAndTipoActoId(prelacion.getId(),
					Constantes.TIPO_ACTO_CERTIFICADO_LG);
		else if (s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_CAUTELAR_ID
				.getIdServicio())
			actos = actoRepository.findAllByPrelacionIdAndTipoActoId(prelacion.getId(),
					Constantes.TIPO_ACTO_CERTIFICADO_LG_PRIMER_AVISO);
		if (actos != null) {
			actos.forEach(x -> {
				_result.add((new ActoVO(x.getId())));
			});
		}
		return _result;

	}

	@Transactional(noRollbackFor = RequerimientoException.class)
	public List<CertificadoLibertadOGravamenVO> getCertificadoDeLivertadIGravamen(Prelacion prelacion)
			throws RequerimientoException {
		List<CertificadoLibertadOGravamenVO> clgs = null;
		Certificado c = findByPrelacionId(prelacion.getId());

		if (c != null) {
			clgs = certificadoListTOCLg(c);

		}

		int i = 0;

		for (CertificadoLibertadOGravamenVO clg : clgs) {
			clg.setUsuarioRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
			clg.setUsuarioCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
			clg.setUsuarioAutorizo(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
			// FIRMA ELECTRONICA REGISTRADOR
			clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));
			clg.setOficina(prelacion.getOficina().getNombre());
			// FIRMA ELECTRONICA COORDINADOR
			String firmaCoord = null;
			String firmaCoord2 = "-SIN FIRMA -";
			if (prelacion.getFirma() != null)
				firmaCoord = prelacion.getFirma();

			clg.setFirmaCoordinador(firmaCoord);

			com.gisnet.erpp.vo.caratula.PredioVO predioVO = caratulaService.getCaratulaPredio(clg.getNoFolio(), prelacion.getOficina().getId());

			clg.setSumatoriaActosPorClasifActo(predioVO.getSumatoriaActosPorClasifActo());
			i++;
		}

		return clgs;
	}

	@Transactional(noRollbackFor = RequerimientoException.class)
	public CertificadoLibertadOGravamenVO getCertificadoDeImpresionFolios(Prelacion prelacion)
			throws RequerimientoException {
		CertificadoLibertadOGravamenVO clg = null;
		Certificado c = findByPrelacionId(prelacion.getId());
		log.debug("ESTO ES CERTIFICADO{}", c);
		log.debug("getCertificadoDeLivertadIGravamen ");
		if (c != null) {
			log.debug("ENTRO IF");
			clg = certificadoTOCLg(c);
		}
		//clg.setUsuarioRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
		clg.setUsuarioCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
		clg.setUsuarioAutorizo(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
		// FIRMA ELECTRONICA REGISTRADOR
		clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));
		clg.setOficina(prelacion.getOficina().getNombre());
		// FIRMA ELECTRONICA COORDINADOR
		String firmaCoord = null;
		String firmaCoord2 = "-SIN FIRMA -";
		if (prelacion.getFirma() != null)
			firmaCoord = prelacion.getFirma();

		/*
		 * if (firmaCoord != null) { firmaCoord =
		 * firmaCoord.substring(firmaCoord.length() - 15, firmaCoord.length());
		 * System.out.println("Primera " + firmaCoord.substring(0, 10));
		 * System.out.println("Segunda " + firmaCoord.substring(firmaCoord.length() - 5,
		 * firmaCoord.length())); firmaCoord2 = firmaCoord.substring(0, 10) + "-" +
		 * firmaCoord.substring(firmaCoord.length() - 5, firmaCoord.length());
		 * System.out.println("TERCERA " + firmaCoord2); }
		 */
		////////// SECCION V ENTRADAS PRESENTADAS QUE NO SE ENCUENTRAN EN
		////////// PROCESO---------------------------------------------------------------

		StringBuilder sbpendientes = new StringBuilder();
		String mensaje = "Entradas encontradas en el proceso:";
		boolean existeRegistro = false;
		List<Prelacion> prelacionPredios = prelacionPredioService.findPrelacionesEnProcesoByPredio(clg.getPredioId(),
				prelacion.getId());
		for (Prelacion _prelacion : prelacionPredios) {
			sbpendientes.append(" " + _prelacion.getConsecutivo() + " ");
			existeRegistro = true;
		}
		if (existeRegistro == false) {
			mensaje = "No hay entradas pendientes";
		}

		clg.setPolitica(mensaje + sbpendientes.toString());

		List<Acto> actos = actoService.findByPrelacionId(prelacion.getId());

		String actosVigentes = "";

		for (Acto acto : actos) {

			System.out.println("ACTO : " + acto.getTipoActo().getNombre());

			actosVigentes += actoPredioCampoService.getObservacionesActo(acto);

		}

		clg.setActosVigentes(actosVigentes);

		////////// FIN SECCION V ENTRADAS PRESENTADAS QUE NO SE ENCUENTRAN EN
		////////// PROCESO-------------------------------------------------------------------
		clg.setFirmaCoordinador(firmaCoord);

		return clg;
	}

	public CertificadoDeNoInscripcionVO generaCertificadoNoInscripcion(Prelacion prelacion)
			throws RequerimientoException {
		List<ActoPredio> actopredios = new ArrayList<ActoPredio>();
		List<Acto> actos = actoRepository.findAllByPrelacion(prelacion);
		
		Predio pre = null;
		Antecedente antecedente = null;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		PredioVO predioVO1 = null;
		log.info("Actos {}", actos);
		Acto acto =  new Acto();
		for (Acto ac : actos) {
			// actopredios = actoPredioRepository.findAllByActo(ac);
			if (ac.getVf() == null || ac.getVf() == false) {
				acto = ac;
			}
		}
		
        Optional<ActoPredio> actoPredio =  actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
        Set<ActoPredioCampo> actoPredioCampos = actoPredio.get().getActoPredioCamposParaActoPredios();
		// log.info("ActosPrediosCampo {}", actoPredioCampos);
		CertificadoDeNoInscripcionVO clg = null;
		Certificado c = findByPrelacionId(prelacion.getId());

		if (c != null) {

			clg = certificadoTOCLgNoIncripcion(c);
			clg.setExpedicion(prelacion.getOficina().getNombre().trim() + ","
					+ prelacion.getOficina().getEstado().getNombre().trim());

		} else {
			clg = new CertificadoDeNoInscripcionVO();

			HashSet<BusquedaResultado> busquedasGuardadas = busquedaResultadoService
					.getPrediosFromPrelacionId(prelacion.getId());

			clg.setNumPrelacion(prelacion.getConsecutivo().toString());

			clg.setSolicitante(prelacion.getUsuarioSolicitan().getNombreCompleto());

			SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat formatterFechaAnio = new SimpleDateFormat("yyyy");
			SimpleDateFormat formatterFechaMes = new SimpleDateFormat("MMMM");
			SimpleDateFormat formatterFechaDia = new SimpleDateFormat("dd/MM/yyyy");

			Date fechaGeneracion = new Date();
			clg.setFecha(formatterFecha.format(prelacion.getFechaIngreso()));

			clg.setFechaRecepcion(formatterFecha.format(prelacion.getFechaIngreso()));
			clg.setHorasNumero(formatterHora.format(fechaGeneracion));
			clg.setCiudad(prelacion.getOficina().getEstado().getNombre().trim());
			clg.setDia(formatterFechaDia.format(fechaGeneracion));
			clg.setMesLetra(formatterFechaMes.format(fechaGeneracion).toUpperCase());
			clg.setAnio(formatterFechaAnio.format(fechaGeneracion));

			clg.setMunicipio(prelacion.getOficina().getNombre().trim());
			clg.setCuentaCatastral(getCuentaCatastral(prelacion));
			// clg.setFechaCertificado(formatterFecha.format(prelacion.getFechaIngreso()));
			if (prelacion.getFechaEnvioFirma() != null) {
				clg.setFechaCertificado(buildLeyendaFecha(prelacion.getFechaEnvioFirma()));
			} else {
				clg.setFechaCertificado("-");
			}
			clg.setExpedicion(prelacion.getOficina().getNombre().trim() + ","
					+ prelacion.getOficina().getEstado().getNombre().trim());
			if (busquedasGuardadas != null && busquedasGuardadas.size() > 0) {
			} else {
			}
		}
		
		if (actoPredioCampos.size() > 0) {
			HashMap<Integer,Persona> solicitantes = new HashMap<Integer,Persona>();
			
			for (ActoPredioCampo apc : actoPredioCampos) {
				int index  =  apc.getOrden();
				if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_DATOS_PREDIO)) {
					clg.setDatosPredio(apc.getValor());
				}
				if(apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_NOMBRE_SOLICITANDO) || 
				  apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_PRIMER_APELLIDO) || 
				  apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_SEGUNDO_APELLIDO)) {
					  Persona  persona =  solicitantes.get(index);
					  	if(persona == null) {
						  persona =  new Persona();
						  solicitantes.put(index,persona);
					  	}
						if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_NOMBRE_SOLICITANDO)) {
							persona.setNombre(apc.getValor());
						}
						if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_PRIMER_APELLIDO)) {
							persona.setPaterno(apc.getValor());
						}
						if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_SEGUNDO_APELLIDO)) {
							persona.setMaterno(apc.getValor());
						}
				}

			}
			
			if(solicitantes.size()>0) {
				Collection<Persona> personas =  solicitantes.values();
				for(Persona item :  personas) {
					sb.append(item.getNombre()).append(" ").append(item.getPaterno()!= null ? item.getPaterno() : "").append(" ")
					.append(item.getNombre()!=null ? item.getMaterno() : "" ).append(", ");
				}
			}
		}

		if (prelacion.getFechaEnvioFirma() != null) {
			clg.setFechaCertificado(buildLeyendaFecha(prelacion.getFechaEnvioFirma()));
		} else {
			clg.setFechaCertificado("-");
		}
		clg.setExpide(withoutComma(sb.toString()));
		if (clg.getExpide() != null) {
			clg.setFavorDe(clg.getExpide());
		}
		clg.setRecibos(boletaRegistralService.buildRecibos(prelacion.getId()));

		List<ActoFirma> aFi = actoFirmaRepository.findAllByActoIdAndEsActoOrderByIdDesc(actos.get(0).getId(), true);
		if (aFi != null && aFi.size() > 0) {
			clg.setRegistrador(boletaRegistralService.buildUsuario(aFi.get(0).getUsuario()));
		} else {
			clg.setRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
		}
		clg.setCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));

		// FIRMA ELECTRONICA REGISTRADOR
		clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));

		// FIRMA ELECTRONICA COORDINADOR
		String firmaCoord = "-SIN FIRMA -";
		if (prelacion.getFirma() != null)
			firmaCoord = prelacion.getFirma() + "\r\n";
		clg.setFirmaCoordinador(firmaCoord);
		//////////////////////////// FOLIOS
		//////////////////////////// //////////////////////////////////////////////
		List<PredioAnteVO> result = busquedaService.listPredioAnteFromBusqueda(prelacion.getId());
		// AVM FILTRA SOLO PREDIOS SELECCIONADOS
		if (result != null && result.size() > 0)
			result = result.stream().filter(x -> x.getSeleccionado() != null && x.getSeleccionado())
					.collect(Collectors.toList());
		List<AntecedenteVO> anteListVO = new ArrayList<AntecedenteVO>();
		List<PredioVO> predioListVO = new ArrayList<PredioVO>();
		sb = new StringBuilder();
		sb2 = new StringBuilder();
		StringBuilder _antecedente = new StringBuilder();
		ArrayList<Predio> predio1 = new ArrayList<>();
		if (result != null) {
			if (result.size() > 0) {
				for (PredioAnteVO vo : result) {
					AntecedenteVO antVO = new AntecedenteVO();
					PredioVO preVO = new PredioVO();
					predioVO1 = vo.getPredio();
					preVO = vo.getPredio();
					predioListVO.add(preVO);
					Predio predio2 = predioService.findOne(predioVO1.getId());
					predio1.add(predio2);
					Optional<PredioAnte> pante = predioService.findFirstByPredioIdOrderByIdDesc(predio2.getId());

					_antecedente.append("FOLIO ").append(predio2.getNoFolio()).append(" - ");

					if (!pante.isPresent()) { // El folio no tiene Antecedentes
						if (predio2.getProcedeDeFolio() != null) {
							_antecedente.append(" PROCEDE DE FOLIO: ").append(predio2.getProcedeDeFolio());
						} else {
							_antecedente.append(" PRIMER REGISTRO");
						}

					} else {// El folio tiene antecedentes
						Antecedente ante = pante.get().getAntecedente();
						AntecedenteVO anteVO = AntecedenteVO.antecedenteTransform(ante);
						_antecedente.append("   Tomo: " + anteVO.getLibro() + "   Libro: " + anteVO.getLibroBis()
								+ "   Seccion: " + anteVO.getSeccion() + "   Oficina: " + anteVO.getOficina()
								+ "   Año: " + anteVO.getAnio() + "   Volumen: " + anteVO.getVolumen()
								+ "   Inscripcion: " + anteVO.getDocumento());
					}
					_antecedente.append("\n");

					sb.append(" " + predioVO1.getNoFolio());
					sb2.append(" FOLIO: " + predioVO1.getNoFolio() + " - " + predio2.getDireccionComplete() + " \n");

				}
			}
		}
		// "INSCRIPCIÓN: " +$F{numeroInscripcion}+ " AÑO: "+$F{anio}+" SECCION: "
		// +$F{seccion}+" LIBRO: " +$F{libroLibro}+" TOMO: "+$F{numLibro}+" VOLUMEN: "
		// +$F{volumen}

		clg.setProcedeDe(_antecedente.toString());
		clg.setFolios(sb.toString());
		clg.setDireccion(sb2.toString());
		clg.setAntecedente(anteListVO);
		clg.setPredio(predioListVO);
		//////////////////////////// propietarios
		//////////////////////////// //////////////////////////////////////////////
		List<PropietarioVO> propietarios = new ArrayList<PropietarioVO>();
		sb = new StringBuilder();
		for (Predio predio2 : predio1) {
			List<PredioPersona> prediosPersona = predioPersonaService.findPropietariosActuales(predio2.getId(), false);
			prediosPersona.forEach((predioPersona) -> {
				PropietarioVO pp = new PropietarioVO();
				pp.setNombre("");
				if (predioPersona.getPersona().getNombre() != null)
					pp.setNombre("Folio: " + predio2.getNoFolio() + " - " + predioPersona.getPersona().getNombre());
				if (predioPersona.getPersona().getPaterno() != null)
					pp.setNombre(pp.getNombre() + " " + predioPersona.getPersona().getPaterno());
				if (predioPersona.getPersona().getMaterno() != null)
					pp.setNombre(pp.getNombre() + " " + predioPersona.getPersona().getMaterno());
				if (predioPersona.getPorcentajeDd() != null)
					pp.setDd(predioPersona.getPorcentajeDd());
				if (predioPersona.getPorcentajeUv() != null)
					pp.setUv(predioPersona.getPorcentajeUv());
				propietarios.add(pp);
			});
		}
		System.out.println("tamaño propietarios" + propietarios.size());
		for (int i = 0; i < propietarios.size(); i++) {
			sb.append(propietarios.get(i).getNombre()).append("  DD:")
					.append(propietarios.get(i).getDd() != null ? propietarios.get(i).getDd() : 0).append("% ")
					.append("  UV:").append(propietarios.get(i).getUv() != null ? propietarios.get(i).getUv() : 0)
					.append("% ").append("\n");

		}
		clg.setPropietarios(sb.toString());
		clg.setOficina(prelacion.getOficina().getNombre());
		return clg;
	}

	public CertificadoDeNoInscripcionVO generaCertificadoInscripcion(Prelacion prelacion)
			throws RequerimientoException {
		List<ActoPredio> actopredios = new ArrayList<ActoPredio>();
		List<Acto> actos = actoRepository.findAllByPrelacion(prelacion);
		List<ActoPredioCampo> actoPredioCampos = new ArrayList<ActoPredioCampo>();
		Antecedente antecedente = null;
		ActoPredio actoPredioMax = new ActoPredio();
		List<ActoPredioCampo> actoPredioCampoAnotacion = new ArrayList<ActoPredioCampo>();
		StringBuilder sb = new StringBuilder();
		PredioVO predioVO1 = null;
		for (Acto ac : actos) {
			// actopredios = actoPredioRepository.findAllByActo(ac);
			if ((ac.getVf() == null || ac.getVf() == false) && (ac.getClon() == null || ac.getClon() == false))
				actopredios = actoPredioRepository.findLastVersionByActo(ac.getId());

		}
		for (ActoPredio ap : actopredios) {
			actoPredioCampos = actoPredioCampoRepository.findAllByActoPredio(ap);
		}

		CertificadoDeNoInscripcionVO clg = null;
		Certificado c = findByPrelacionId(prelacion.getId());

		List<PredioAnteVO> result = busquedaService.listPredioAnteFromBusqueda(prelacion.getId());
		// AVM FILTRA SOLO PREDIOS SELECCIONADOS
		if (result != null && result.size() > 0)
			result = result.stream().filter(x -> x.getSeleccionado() != null && x.getSeleccionado())
					.collect(Collectors.toList());

		List<AntecedenteVO> anteListVO = new ArrayList<AntecedenteVO>();
		List<PredioVO> predioListVO = new ArrayList<PredioVO>();
		if (c != null) {
			clg = certificadoTOCLgNoIncripcion(c);
			clg.setExpedicion(prelacion.getOficina().getNombre().trim() + ","
					+ prelacion.getOficina().getEstado().getNombre().trim());

		} else {
			clg = new CertificadoDeNoInscripcionVO();

			HashSet<BusquedaResultado> busquedasGuardadas = busquedaResultadoService
					.getPrediosFromPrelacionId(prelacion.getId());

			clg.setNumPrelacion(prelacion.getConsecutivo().toString());

			clg.setSolicitante(prelacion.getUsuarioSolicitan().getNombreCompleto());

			SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat formatterFechaAnio = new SimpleDateFormat("yyyy");
			SimpleDateFormat formatterFechaMes = new SimpleDateFormat("MMMM");
			SimpleDateFormat formatterFechaDia = new SimpleDateFormat("dd/MM/yyyy");

			Date fechaGeneracion = new Date();
			clg.setFecha(formatterFecha.format(prelacion.getFechaIngreso()));

			clg.setFechaRecepcion(formatterFecha.format(prelacion.getFechaIngreso()));
			clg.setHorasNumero(formatterHora.format(fechaGeneracion));
			clg.setCiudad(prelacion.getOficina().getEstado().getNombre().trim());
			clg.setDia(formatterFechaDia.format(fechaGeneracion));
			clg.setMesLetra(formatterFechaMes.format(fechaGeneracion).toUpperCase());
			clg.setAnio(formatterFechaAnio.format(fechaGeneracion));

			clg.setMunicipio(prelacion.getOficina().getNombre().trim());
			clg.setCuentaCatastral(getCuentaCatastral(prelacion));
			// clg.setFechaCertificado(formatterFecha.format(prelacion.getFechaIngreso()));
			if (prelacion.getFechaEnvioFirma() != null) {
				clg.setFechaCertificado(buildLeyendaFecha(prelacion.getFechaEnvioFirma()));
			} else {
				clg.setFechaCertificado("-");
			}
			clg.setExpedicion(prelacion.getOficina().getNombre().trim() + ","
					+ prelacion.getOficina().getEstado().getNombre().trim());
			if (busquedasGuardadas != null && busquedasGuardadas.size() > 0) {

			} else {

			}

		}
		String nombre = "", apaterno = "", amaterno = "";
		if (actoPredioCampos.size() > 0) {
			for (ActoPredioCampo apc : actoPredioCampos) {

				switch (apc.getModuloCampo().getModulo().getId().intValue()) {
					case 314: // ACREDITADOS
						switch (apc.getModuloCampo().getId().intValue()) {
							case 106: // 106 NOMBRE
								nombre = apc.getValor();
								break;
							case 107: // 107 A-PATERNO
								apaterno = apc.getValor();
								break;
							case 108: // 108 A-MATERNO
								amaterno = apc.getValor();
								break;
						}
						break;
					default:
						if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_DATOS_PREDIO)) {
							clg.setDatosPredio(apc.getValor());
						}
						break;
				}
			}
			clg.setFavorDe(nombre + " " + apaterno + " " + amaterno);
		}

		if (result != null) {
			boolean found;
			if (result.size() > 0) {
				for (PredioAnteVO vo : result) {
					AntecedenteVO antVO = new AntecedenteVO();
					PredioVO preVO = new PredioVO();
					antVO = vo.getAntecedente();
					preVO = vo.getPredio();
					predioVO1 = vo.getPredio();
					predioListVO.add(preVO);
					anteListVO.add(antVO);
					clg.setProcedeDe(preVO.getProcedeDeFolio() != null ? withoutComma(preVO.getProcedeDeFolio())
							: preVO.getProcedeDeFolio());
					clg.setTramites(caratulaDAO.findTramitesParaPredio(vo.getPredio().getId(), PREDIO));
					// if (preVO.getPrimerRegistro() == null) {
					// if(vo.getLibro()!=null) {
					// clg.setNumLibro(vo.getLibro().getNumLibro().toString());
					// clg.setLibroLibro(vo.getLibro().getLibroBis());
					// clg.setSeccion(vo.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
					//
					// clg.setAnio(vo.getLibro().getAnio().toString());
					// clg.setVolumen(vo.getLibro().getVolumen().toString());
					// clg.setNumeroInscripcion(vo.getAntecedente().getDocumento());
					// clg.setFecha(boletaRegistralService.buildAsiento(vo.getLibro().getFechaIngreso()));
					// }else {
					// clg.setNumLibro(" ");
					// clg.setLibroLibro("");
					// clg.setSeccion("");
					// clg.setVolumen("");
					// clg.setNumeroInscripcion("");
					// }
					// } else {
					// clg.setNumLibro(" ");
					// clg.setLibroLibro("");
					// clg.setSeccion("");
					// clg.setVolumen("");
					// }

					sb.append(" " + vo.getPredio().getNoFolio().toString());
				}
				clg.setFolios(sb.toString());
				clg.setAntecedente(anteListVO);
				clg.setPredio(predioListVO);

				for (TramiteVO tram : clg.getTramites()) {
					found = tram.getTramite().contains(Constantes.TIPO_TRAMITE_NOMBRE);
					if (found) {
						// Acto acto = actoRepository.findOne(tram.getActoId());
						actoPredioMax = actoPredioRepository.findActoPredioByLastVersion(tram.getActoId());
						log.debug("a {}", actoPredioMax);
						if (actoPredioMax != null) {
							if (actoPredioMax.getId() != null) {
								actoPredioCampoAnotacion = actoPredioCampoRepository
										.findByActoPredioId(actoPredioMax.getId());
							}
						}
					}
				}
				if (actoPredioCampoAnotacion.size() > 0) {
					for (ActoPredioCampo apc : actoPredioCampoAnotacion) {
						if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_DESCRIPCION_ANOTACION)) {
							clg.setAnotacion(apc.getValor());
						}
					}
				}
			}
		}
		if (prelacion.getFechaEnvioFirma() != null) {
			clg.setFechaCertificado(buildLeyendaFecha(prelacion.getFechaEnvioFirma()));
		} else {
			clg.setFechaCertificado("-");
		}
		clg.setRecibos(boletaRegistralService.buildRecibos(prelacion.getId()));
		List<ActoFirma> aFi = actoFirmaRepository.findAllByActoIdAndEsActoOrderByIdDesc(actos.get(0).getId(), true);
		if (aFi != null && aFi.size() > 0) {
			clg.setRegistrador(boletaRegistralService.buildUsuario(aFi.get(0).getUsuario()));
		} else {
			clg.setRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
		}

		clg.setCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));

		// FIRMA ELECTRONICA REGISTRADOR
		clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));

		// FIRMA ELECTRONICA COORDINADOR
		String firmaCoord = "-SIN FIRMA -";
		if (prelacion.getFirma() != null)
			firmaCoord = prelacion.getFirma() + "\r\n";
		clg.setFirmaCoordinador(firmaCoord);
		///////////////////////////////////// AGREGAR DIRECCION
		///////////////////////////////////// COMPLETA/////////////////////////////////////
		Predio predio1 = null;
		if(predioVO1!=null)
		 predio1 = predioService.findOne(predioVO1.getId());

		if (predio1 == null) {
			throw new RequerimientoException(
					"el predio no cumple con los requisitos bloqueado==false, cerrado==false, caratula_actualizada==true, reenviando a turnador");
		}
		/******** AGREGAR METODO getDireccionComplete() EN PREDIO***************** */
		clg.setDireccion(predio1.getDireccionComplete());

		if (predio1.getPredioAntesParaPredios() != null && predio1.getPredioAntesParaPredios().size() > 0) {
			antecedente = predio1.getPredioAntesParaPredios().iterator().next().getAntecedente();
		}
		if (antecedente != null) {
			if (antecedente.getLibro() != null) {
				clg.setNumLibro(antecedente.getLibro().getNumLibro().toString());
				clg.setLibroLibro(antecedente.getLibro().getLibroBis());
				clg.setSeccion(antecedente.getLibro().getSeccionesPorOficina().getSeccion().getNombre());

				clg.setAnio(antecedente.getLibro().getAnio().toString());
				clg.setVolumen(antecedente.getLibro().getVolumen().toString());
				clg.setNumeroInscripcion(antecedente.getDocumento());
				clg.setFecha(boletaRegistralService.buildAsiento(antecedente.getLibro().getFechaIngreso()));
			}
		} else {
			clg.setSeccion("");
		}

		//////////////////////////////////////////// AGREGAR
		//////////////////////////////////////////// PROPIETARIOS/////////////////////////////////////////
		// String nombreCompleto = "";
		StringBuilder nombreCompleto = new StringBuilder();
		List<PropietarioVO> propietarios = new ArrayList<PropietarioVO>();

		List<PredioPersona> prediosPersona = predioPersonaService.findPropietariosActuales(predio1.getId(), false);
		prediosPersona.forEach((predioPersona) -> {
			propietarios.add(new PropietarioVO(
					predioPersona.getPersona().getNombre() + " " + predioPersona.getPersona().getPaterno() + " "
							+ predioPersona.getPersona().getMaterno(),
					predioPersona.getPorcentajeDd(), predioPersona.getPorcentajeUv()));
		});
		for (PropietarioVO propietarioVO : propietarios) {
			nombreCompleto.append(propietarioVO.getNombre()).append("  DD: ")
					.append(propietarioVO.getDd() != null ? propietarioVO.getDd() : 0).append("% ").append("  UV: ")
					.append(propietarioVO.getUv() != null ? propietarioVO.getUv() : 0).append("% ").append("\n");
		}
		clg.setOficina(prelacion.getOficina().getNombre().trim());
		clg.setPropietarios(nombreCompleto.toString());
		return clg;
	}

	private String getCuentaCatastral(Prelacion prelacion) {
		Set<Acto> actos = prelacion.getActosParaPrelacions();

		String cuentaCatastral = "";
		String claveCatastral = "";

		if (actos != null) {
			for (Acto a : actos) {

				for (ActoPredio ap : a.getActoPrediosParaActos()) {
					Set<ActoPredioCampo> actoPredioCampos = ap.getActoPredioCamposParaActoPredios();
					for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
						long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
						log.debug("{} = {}", campoId, actoPredioCampo.getValor());

						if (campoId == 509)
							cuentaCatastral = actoPredioCampo.getValor();
						if (campoId == 510)
							claveCatastral = actoPredioCampo.getValor();
					}
				}
			}
		}

		return cuentaCatastral + "      clave " + claveCatastral;

	}

	public CertificadoPersonaJuridicasVO generaCertificadoDeLivertadIGravamenJuridico(Prelacion prelacion)
			throws RequerimientoException {

		CertificadoPersonaJuridicasVO clg = new CertificadoPersonaJuridicasVO();

		Certificado c = findByPrelacionId(prelacion.getId());
		if (c != null) {

			clg = certificadoTOCLgJuridico(c);
		} else {
			clg = new CertificadoPersonaJuridicasVO();
			if (prelacion.getPrelacionPrediosParaPrelacions() == null
					|| prelacion.getPrelacionPrediosParaPrelacions().size() != 1) {
				throw new RequerimientoException("No se recibio el numero correcto de predios");
			}
		}
		if (prelacion.getPrelacionServiciosParaPrelacions() == null
				|| prelacion.getPrelacionServiciosParaPrelacions().size() != 1) {
			throw new RequerimientoException(
					"No se recibio el numero correcto de servicios y/o servicio para la prelacion");
		}

		PrelacionPredio prePredios = prelacion.getPrelacionPrediosParaPrelacions().iterator().next();
		final List<PropietarioVO> propietarios = new ArrayList<PropietarioVO>();
		if (prePredios != null) {
			Constantes.ETipoFolio tipo = Constantes.ETipoFolio
					.fromInt(Math.toIntExact(prePredios.getTipoFolio().getId()));
			switch (tipo) {

				case PERSONAS_JURIDICAS:
					/*
					 * clg.setGravamenes("NO REPORTA GRAVAMEN"); clg.setPropietarios(propietarios);
					 */
					PjAnte antePj = pjAnteRepository
							.findOneByPersonaJuridicaId(prePredios.getPersonaJuridica().getId());

					if (antePj != null) {

						String datosRegistro = "";
						String inscripcion = "";

						if (antePj.getAntecedente().getLibro().getTipoDoc())
							inscripcion = "INSCRIPCIÓN";
						else
							inscripcion = "DOCUMENTO";

						datosRegistro = "Libro : " + antePj.getAntecedente().getLibro().getId() + "  Sección : "
								+ antePj.getAntecedente().getLibro().getSeccionesPorOficina().getSeccion().getNombre()
								+ "  Oficina : "
								+ antePj.getAntecedente().getLibro().getSeccionesPorOficina().getOficina().getNombre()
								+ "  Inscripción : " + inscripcion + "'\nFolio : "
								+ prePredios.getPersonaJuridica().getNoFolio();

						clg.setDatosRegistro(datosRegistro);
					} else {

						clg.setDatosRegistro("");
					}

					SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm:ss");
					SimpleDateFormat formatterFechaAnio = new SimpleDateFormat("yyyy");
					SimpleDateFormat formatterFechaMes = new SimpleDateFormat("MMMM");
					SimpleDateFormat formatterFechaDia = new SimpleDateFormat("dd/MM/yyyy");

					Date fechaGeneracion = new Date();

					clg.setFechaIngreso(formatterFecha.format(prelacion.getFechaIngreso()));
					clg.setHoraIngreso(formatterHora.format(prelacion.getFechaIngreso()));
					clg.setNoPrelacion(prelacion.getConsecutivo());

					clg.setSociedad(prePredios.getPersonaJuridica().getDenominacionNombre());
					clg.setSolicitud(prelacion.getUsuarioSolicitan().getNombreCompleto());
					clg.setCiudad("GUADALAJARA");
					clg.setHoras(formatterHora.format(fechaGeneracion));
					clg.setDia(formatterFechaDia.format(fechaGeneracion));
					clg.setMesLetra(formatterFechaMes.format(fechaGeneracion).toUpperCase());
					clg.setAnio(formatterFechaAnio.format(fechaGeneracion));

					clg.setReviso("");
					clg.setAutorizo("");
					clg.setCargoAutorizo("");
					clg.setFirma("");
					clg.setFirmaAutorizo("");

					/*
					 * List<String> elementosBusqueda = new ArrayList<String>();
					 * elementosBusqueda.add("--");
					 */
					clg.setBusqueda("");

					break;
				case PERSONAS_AUXILIARES:

					break;
				case MUEBLE:

					break;
				case PREDIO:
					Predio predio = prelacion.getPrelacionPrediosParaPrelacions().iterator().next().getPredio();

					if (predio == null) {
						throw new RequerimientoException(
								"el predio no cumple con los requisitos bloqueado==false, cerrado==false, caratula_actualizada==true, reenviando a turnador");

					}
					if (predio.getBloqueado() != null && predio.getBloqueado() && predio.getCerrado() != null
							&& predio.getCerrado().length() > 0
							&& (predio.getCaratulaActualizada() == null || !predio.getCaratulaActualizada())) {
						throw new RequerimientoException(
								"el predio no cumple con los requisitos bloqueado==false, cerrado==false, caratula_actualizada==true, reenviando a turnador");
					}
					// Cargar ultimo traslativo del predio
					// Acto actoTraslativo =
					// actoRepository.findUltimoTraslativoRegistradoByPredioId(predio.getId(),
					// Constantes.ClasifActo.TRASLATIVOS.getIdClasifActo(),
					// Constantes.StatusActo.ACTIVO.getIdStatusActo());
					List<Acto> actoTraslativo = actoService.getActosVigentesTraslativosParaPredio(predio.getId());
					log.debug("acto_traslativo {}", actoTraslativo);

					break;
			}
		}

		Set<Recibo> recibos = prelacion.getRecibosParaPrelacions();
		if (recibos.size() == 0) {

			clg.setNoRecibo("0");
			clg.setTotal("0");
			/*
			 * clg.setRecibo(0); clg.setMonto(BigDecimal.ZERO);
			 * clg.setConcepto("CERTIFICADOS DE LIBERTAD O GRAVAMEN"); clg.setReferencia(0);
			 */
		} else {
			Recibo recibo = recibos.iterator().next();
			Set<ReciboConcepto> reciboConceptos = recibo.getReciboConceptosParaRecibos();
			/*
			 * if (reciboConceptos.size()!=1){ throw new
			 * RequerimientoException("El recibo de la prelacion tiene mas de un concepto");
			 * }
			 */

			clg.setNoRecibo(recibo.getNoRecibo().toString());
			clg.setTotal(recibo.getMonto().toString());
		}

		return clg;
	}

	@Transactional(noRollbackFor = RequerimientoException.class)
	public CertificadoCopiasVO generaCertificadoCopias(Prelacion prelacion) throws RequerimientoException {

		CertificadoCopiasVO clg = null;
		Certificado c = findByPrelacionId(prelacion.getId());

		if (c != null) {

			clg = certificadoTOCopias(c);
		} else {
			clg = new CertificadoCopiasVO();

			// cambiar el servicioId
			if (prelacion.getPrelacionServiciosParaPrelacions() == null
					|| prelacion.getPrelacionServiciosParaPrelacions().size() != 1) {
				throw new RequerimientoException(
						"No se recibio el numero correcto de servicios y/o servicio para la prelacion");
			}

			clg.setPrelacionId(prelacion.getId());
			clg.setConsecutivo(prelacion.getConsecutivo() != null ? prelacion.getConsecutivo() : 0);
			clg.setFechaIngreso(prelacion.getFechaIngreso());
			clg.setLugar(prelacion.getOficina().getNombre());
			clg.setSolicitante(prelacion.getUsuarioSolicitan().getNombreCompleto());
			clg.setUsuarioSolicitanteId(prelacion.getUsuarioSolicitan().getId());
			clg.setFechaGenerado(new Date());
			clg.setFechaRegistro(new Date());
			Set<Acto> actos = prelacion.getActosParaPrelacions();
			Acto actoSer = null;
			HashSet<BusquedaResultado> busquedas = busquedaResultadoService
					.getPrediosFromPrelacionId(prelacion.getId());
			if (actos != null) {
				for (Acto a : actos) {
					actoSer = a;
					if (a.getTipoActo().getId().longValue() == Constantes.COPIAS_CERTIFICADAS.longValue()) {

						for (BusquedaResultado b : busquedas) {
							if (b.getFoliosrDigital() != null) {
								clg.setFolio(b.getFoliosrDigital().getNumFolioRegistral());
							}
						}

					}

					if (a.getTipoActo().getId().longValue() == Constantes.IMPRESION.longValue()) {

						for (BusquedaResultado b : busquedas) {
							if (b.getPredio() != null) {
								clg.setFolio(b.getPredio().getNoFolio());
							}
							if (b.getPersonaJuridica() != null) {
								clg.setFolio(b.getPersonaJuridica().getNoFolio());
							}
							if (b.getMueble() != null) {
								clg.setFolio(b.getMueble().getNoFolio());
							}
							if (b.getFolioSeccionAuxiliar() != null) {
								clg.setFolio(b.getFolioSeccionAuxiliar().getNoFolio());
							}
						}

					}

				}
			}

			Set<Recibo> recibos = prelacion.getRecibosParaPrelacions();
			if (recibos.size() == 0) {
				clg.setRecibo(0);
				clg.setMonto(BigDecimal.ZERO);
				clg.setConcepto("");

			} else {
				Recibo recibo = recibos.iterator().next();
				clg.setRecibo(recibo.getNoRecibo());
				clg.setMonto(recibo.getMonto());
				clg.setConcepto("");

			}

			/*
			 * 15-11-2018 Optional<PrelacionFirma> prelacionFirma =
			 * prelacionFirmaRepository.findByPrelacionId(prelacion.getId()); if
			 * (prelacionFirma.isPresent()) { clg.setFirma(prelacionFirma.get().getFirma());
			 * } else { clg.setFirma(""); } clg.setUsuarioAutorizo(getAutorizo(prelacion));
			 * 
			 * clg.setFirmaAutorizo(getActoFirma(actoSer));
			 */
		}

		if (prelacion.getUsuarioAnalista() != null) {
			clg.setRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
		} else {
			clg.setRegistrador("-");
		}

		if (prelacion.getUsuarioCoordinador() != null) {
			clg.setCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
		} else {
			clg.setCoordinador("-");
		}

		// FIRMA ELECTRONICA REGISTRADOR
		clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));

		// FIRMA ELECTRONICA COORDINADOR
		String firmaCoord = "-SIN FIRMA -";
		if (prelacion.getFirma() != null)
			firmaCoord = prelacion.getFirma() + "\r\n";
		clg.setFirmaCoordinador(firmaCoord);
		clg.setOficina(prelacion.getOficina().getNombre());
		return clg;

	}

	public byte[] getPdfCertificadoCopia(Long prelacion,boolean marcaAgua) throws JRException {
		Prelacion pre = prelacionService.findOne(prelacion);
		String certifica = " ";
		CertificadoCopiasVO certificado = null;

		try {

			certificado = certificadoService.generaCertificadoCopias(pre);
			certifica = certificadoService.generaTextoCertifica(pre);
		} catch (RequerimientoException ex) {

			log.debug("Excepcion de al generar certificado " + ex.getMessage());
			return null;

		}

		List<CertificadoCopiasVO> certificados = new ArrayList<CertificadoCopiasVO>();
		certificados.add(certificado);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("certifica", certifica);
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		if(marcaAgua)
			parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfCopiasCertificadas.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	@Transactional(noRollbackFor = RequerimientoException.class)
	public String generaTextoCertifica(Prelacion prelacion) throws RequerimientoException {
		String certifica = " ";
		List<Acto> actos = actoRepository.findAllByPrelacion(prelacion);
		List<ActoPredio> actopredios = new ArrayList<ActoPredio>();
		List<ActoPredioCampo> actoPredioCampos = new ArrayList<ActoPredioCampo>();

		for (Acto ac : actos) {
			if(ac.getTipoActo().getId().equals(Constantes.COPIAS_CERTIFICADAS)) {
				actopredios = actoPredioRepository.findLastVersionByActo(ac.getId());
			}
		}
		for (ActoPredio ap : actopredios) {
			actoPredioCampos = actoPredioCampoRepository.findAllByActoPredio(ap);

		}

		if (actoPredioCampos.size() > 0) {

			for (ActoPredioCampo apc : actoPredioCampos) {

				if (apc.getModuloCampo().getModulo().getNombre().equals("OBSERVACIONES")) {

					certifica = apc.getValor();

				}
				System.out.println("El acto predio campo es" + actoPredioCampos);
				System.out.println("El valor es " + certifica);

			}

		}
		return certifica;
	}

	public CertificadoHistorialVO generaCertificadoHistorial(Prelacion prelacion) throws RequerimientoException {
		CertificadoHistorialVO clg = clg = new CertificadoHistorialVO();

		if (prelacion.getPrelacionPrediosParaPrelacions() == null
				|| prelacion.getPrelacionPrediosParaPrelacions().size() != 1) {
			throw new RequerimientoException("No se recibio el numero correcto de predios");
		}
		// cambiar el servicioId
		if (prelacion.getPrelacionServiciosParaPrelacions() == null
				|| prelacion.getPrelacionServiciosParaPrelacions().size() != 1) {
			throw new RequerimientoException(
					"No se recibio el numero correcto de servicios y/o servicio para la prelacion");
		}

		Predio predio = prelacion.getPrelacionPrediosParaPrelacions().iterator().next().getPredio();

		if (predio.getBloqueado() != null && predio.getBloqueado() && predio.getCerrado() != null
				&& predio.getCerrado().length() > 0
				&& (predio.getCaratulaActualizada() == null || !predio.getCaratulaActualizada())) {
			throw new RequerimientoException(
					"el predio no cumple con los requisitos bloqueado==false, cerrado==false, caratula_actualizada==true, reenviando a turnador");
		}

		SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm");

		// clg.setPrelacionId(prelacion.getId());
		clg.setNoPrelacion(prelacion.getConsecutivo());
		clg.setFechaIngreso(formatterFecha.format(prelacion.getFechaIngreso()));
		clg.setCiudad(prelacion.getOficina().getNombre());
		clg.setSolicitud(prelacion.getUsuarioSolicitan().getNombreCompleto());
		List<Acto> actoTraslativo = actoService.getActosVigentesTraslativosParaPredio(predio.getId());

		Set<Recibo> recibos = prelacion.getRecibosParaPrelacions();
		if (recibos.size() != 1) {
			throw new RequerimientoException("La entrada no contiene un recibo de pago");
		}

		Recibo recibo = recibos.iterator().next();

		clg.setNoRecibo(recibo.getNoRecibo().toString());
		clg.setTotal(recibo.getMonto().toString());
		clg.setDatosRegistro("");
		clg.setSociedad("");
		clg.setSolicitud("");
		clg.setCiudad("");
		clg.setHoras("");
		clg.setDia("");
		clg.setMesLetra("");
		clg.setAnio("");

		clg.setReviso("");
		clg.setAutorizo("");
		clg.setCargoAutorizo("");

		clg.setHistorial("");
		clg.setFirma("");
		clg.setFirmaBoleta("");

		ActoPredio actoPredio = actoTraslativo.get(actoTraslativo.size() - 1).getActoPrediosParaActos().iterator()
				.next();

		return clg;

	}

	@Transactional
	public void enviaCertificadoDeLivertadIGravamen(Prelacion prelacion) throws RequerimientoException {
		try {
			prelacion = prelacionRepository.findOne(prelacion.getId());
			// IHM getCertificado()
			List<CertificadoLibertadOGravamenVO> clg = getCertificadoDeLivertadIGravamen(prelacion);

			// TODO REMPLAZAR 3L por CONSTANTE
			Usuario usuarioSellador = usuarioRepository.findOneByUsuarioRolesParaUsuariosRolIdAndOficinaId(3L,
					prelacion.getOficina().getId());

			if (usuarioSellador == null) {
				throw new RequerimientoException("No existe un solo usuario con el rol de 3");
			}
			if (usuarioSellador.getP12() == null || usuarioSellador.getP12().length() == 0) {
				throw new RequerimientoException("El usuario sellador no cuenta con el archivo p12");
			}
			if (usuarioSellador.getP12Password() == null || usuarioSellador.getP12Password().length() == 0) {
				throw new RequerimientoException("El usuario sellador no cuenta con el password para el archivo p12");
			}

			// clg.setUsuarioCoordinador(usuarioSellador.getNombreCompleto());
			// clg.setUsuarioCoordinadorId(usuarioSellador.getId());

			Certificado certificado = new Certificado();

			certificado.setDatos(clgToJSON(clg));

			String[] firma = SignerUtil.generatePkcs7SignedData(
					usuarioSellador.getP12().getBytes(1, (int) usuarioSellador.getP12().length()),
					EncryptAESUtil.decrypt(usuarioSellador.getP12Password()), clgToJSON(clg));

			FirmaRequestDTO firmaRequestDTO = new FirmaRequestDTO();
			firmaRequestDTO.setOriginal(clgToJSON(clg));
			firmaRequestDTO.setPkcs7(firma[1]);

			FirmaDTO firmaDTO = firmaService
					.parseEstampa(firmaService.registrarFirmaEnServicioEstatal(firmaRequestDTO));

			certificado.setFirma(firma[0]);
			certificado.setPkcs7(firma[1]);
			certificado.setSecuencia((int) firmaDTO.getSecuencia());
			certificado.setPolitica(firmaDTO.getPolitica());
			certificado.setPrelacionServicio(prelacion.getPrelacionServiciosParaPrelacions().iterator().next());
			certificado.setUsuario(usuarioSellador);

			log.debug("Guardando certificado " + certificado);
			certificadoRepository.save(certificado);

			byte[] report = generPdfCertificadoLibertadOGravamen(certificado);

			// TODO llamar a prelacionService para avanzar prelacion al status
			// correspondiente
			prelacion.setStatus(statusRepository.findOne(Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()));
			prelacionRepository.save(prelacion);

			String to = prelacion.getUsuarioSolicitan().getEmail();
			String body = "Test";

			if (to == null || to.length() == 0) {
				log.debug("No se envio correo");
			} else {
				log.debug("tratando de enviar email a " + to);

				Context context = new Context();
				context.setVariable("nombre", prelacion.getUsuarioSolicitan().getNombreCompleto());
				context.setVariable("consecutivo", prelacion.getConsecutivo());

				// mailSenderService.sendMailWithAttachment(parametroRepository.findByCve("MAIL_USERNAME").getValor(),
				// to,
				// "Certificado de la Entrada " + prelacion.getConsecutivo(),
				// "certificadoLibertadTemplate",
				// context, report);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);

			throw new RequerimientoException(e.getMessage());
		}

	}

	@Transactional
	public Certificado guardarCertificado(Prelacion prelacion, Integer tipoRespuesta) throws RequerimientoException {
		Certificado certificado = null;
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		PrelacionServicio s = null;
		try {
			certificado = findByPrelacionId(prelacion.getId());
			if (certificado == null) {
				log.info("NUEVO-Certificado");
				certificado = new Certificado();
			} else {
				log.info("UPDATE-Certificado:" + certificado);
			}
			certificado.setTipoRespuesta(tipoRespuesta);
			if (prelacion.getPrelacionServiciosParaPrelacions() != null
					&& prelacion.getPrelacionServiciosParaPrelacions().size() > 0) {

				s = prelacion.getPrelacionServiciosParaPrelacions().iterator().next();
				certificado.setPrelacionServicio(s);

				if (s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_JURIDICO.getIdServicio()) {

					CertificadoPersonaJuridicasVO clg = generaCertificadoDeLivertadIGravamenJuridico(prelacion);

					clg.setAutorizo(usuario.getNombreCompleto());
					// clg.set(usuario.getId());
					certificado.setUsuario(usuario);
					certificado.setDatos(clgJuridicoToJSON(clg));

				}

				if (s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_HISTORIAL.getIdServicio()
						|| s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_ID
								.getIdServicio()
						|| s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_CAUTELAR_ID
								.getIdServicio()) {

					List<Acto> actos = null;
					if (s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_ID.getIdServicio())
						actos = actoRepository.findAllByPrelacionIdAndTipoActoId(prelacion.getId(),
								Constantes.TIPO_ACTO_CERTIFICADO_LG);
					else if (s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_GRAVAMEN_CAUTELAR_ID
							.getIdServicio())
						actos = actoRepository.findAllByPrelacionIdAndTipoActoId(prelacion.getId(),
								Constantes.TIPO_ACTO_CERTIFICADO_LG_PRIMER_AVISO);

					List<CertificadoLibertadOGravamenVO> clgs = new ArrayList<CertificadoLibertadOGravamenVO>();
					if (actos != null && actos.size() > 0) {
						actos.forEach(x -> {
							Optional<ActoPredio> ap = actoPredioRepository
									.findFirstByActoIdOrderByVersionDesc(x.getId());
							if (ap.isPresent() && ap.get().getPredio() != null) {
								CertificadoLibertadOGravamenVO clg;
								try {
									clg = generaCertificadoDeLivertadIGravamen(prelacion, ap.get().getPredio());
									clg.setUsuarioCoordinador(usuario.getNombreCompleto());
									clg.setUsuarioCoordinadorId(usuario.getId());
									clgs.add(clg);
								} catch (RequerimientoException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});
					}

					certificado.setUsuario(usuario);
					certificado.setDatos(clgToJSON(clgs));

				}

				if (s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_NO_INSCRIPCION.getIdServicio()
						|| s.getServicio().getId() == Constantes.Servicio.CERTIFICADO_LIBERTAD_NO_PROPIEDAD
								.getIdServicio()) {

					CertificadoDeNoInscripcionVO clg = generaCertificadoNoInscripcion(prelacion);

					certificado.setUsuario(usuario);
					certificado.setDatos(clgNoInscripcionToJSON(clg));

				}

				if (s.getServicio().getId() == Constantes.Servicio.COPIAS_CERTIFICADA.getIdServicio()
						|| s.getServicio().getId() == Constantes.Servicio.IMPRESION_FOLIOS.getIdServicio()
						|| s.getServicio().getId() == Constantes.Servicio.COPIA_CERTIFICADA_DE_ACTOS_PUBLICITARIOS
								.getIdServicio()) {

					CertificadoCopiasVO clg = generaCertificadoCopias(prelacion);
					clg.setConLetra("");

					certificado.setUsuario(usuario);
					certificado.setDatos(copiasToJSON(clg));

				}

			}
			log.debug("Guardando certificado " + certificado);
			if (certificado != null)
				certificadoRepository.saveAndFlush(certificado);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

			throw new RequerimientoException(e.getMessage());
		}
		;
		return certificado;
	}

	@Transactional
	public Certificado updateCertificado(Certificado certificado) throws RequerimientoException {

		try {
			// log.info("updateCertificado:"+certificado);
			certificadoRepository.saveAndFlush(certificado);

		} catch (Exception e) {
			log.error(e.getMessage(), e);

			throw new RequerimientoException(e.getMessage());
		}
		;
		return certificado;
	}

	private byte[] generPdfCertificadoLibertadOGravamen(Certificado certificado) throws JRException {
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		Prelacion prelacion = certificado.getPrelacionServicio().getPrelacion();

		CertificadoLibertadOGravamenVO clg = certificadoTOCLg(certificado);
		certificados.add(clg);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("datasource", ds);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		parameterMap.put(JRParameter.REPORT_LOCALE, Constantes.locale);
		String qR = parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/certificado/"
				+ prelacion.getId();
		parameterMap.put("QR_BASE_URI", qR);

		InputStream jasperStream = this.getClass()
				.getResourceAsStream("/reports/certificadoLibertadOGravamen/pdfCertificadoLibertadOGravamen.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	private String copiasToJSON(CertificadoCopiasVO clg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(clg);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}

		return "";
	}

	private String clgToJSON(CertificadoLibertadOGravamenVO clg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(clg);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}

		return "";
	}

	private String clgToJSON(List<CertificadoLibertadOGravamenVO> clg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(clg);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}

		return "";
	}

	private String clgJuridicoToJSON(CertificadoPersonaJuridicasVO clg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(clg);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}

		return "";
	}

	private String clgNoInscripcionToJSON(CertificadoDeNoInscripcionVO clg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(clg);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}

		return "";
	}

	private CertificadoCopiasVO jsonTOCopias(String json) {
		System.out.println("CERTIFICADO  = " + json);

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, CertificadoCopiasVO.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	private CertificadoLibertadOGravamenVO jsonTOClg(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, CertificadoLibertadOGravamenVO.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	private List<CertificadoLibertadOGravamenVO> jsonTOClgList(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return Arrays.asList(mapper.readValue(json, CertificadoLibertadOGravamenVO[].class));
		} catch (IOException e) {
			log.trace(e.getCause().getMessage(), e);
		}
		return null;

	}

	private CertificadoPersonaJuridicasVO jsonTOClgJuridico(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, CertificadoPersonaJuridicasVO.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	private CertificadoDeNoInscripcionVO jsonTOClgNoInscripcion(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			return mapper.readValue(json, CertificadoDeNoInscripcionVO.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	private String buildLeyendaFecha(Date fecha) {

		if (fecha == null)
			return "FECHA NO VÁLIDA";

		Calendar date = new GregorianCalendar();
		date.setTime(fecha);

		String dia = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		String[] meses = new String[] { "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO",
				"SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE" };
		String mes = meses[date.get(Calendar.MONTH)];
		String anio = String.valueOf(date.get(Calendar.YEAR));
		String hora = String.format("%1$tl:%1$tM %1$tp", fecha);

		// return "SE PRESENTÓ PARA SU REGISTRO EL " + dia + " DE " + mes + " DE " +
		// anio + " A LAS " + hora;
		return "A LOS " + dia + " DIAS DEL MES DE " + mes + " DEL AÑO " + anio;

	}

	@Transactional(noRollbackFor = RequerimientoException.class)
	public CertificadoDeNoInscripcionVO generaCertificadoDeLivertadOGravamen(Prelacion prelacion)
			throws RequerimientoException {
		List<ActoPredio> actopredios = new ArrayList<ActoPredio>();
		List<Acto> actos = actoRepository.findAllByPrelacion(prelacion);
		List<ActoPredioCampo> actoPredioCampos = new ArrayList<ActoPredioCampo>();
		StringBuilder sb = new StringBuilder();
		for (Acto ac : actos) {
			actopredios = actoPredioRepository.findAllByActo(ac);
			log.debug("acots predciosss {}", actopredios);
		}
		for (ActoPredio ap : actopredios) {
			actoPredioCampos = actoPredioCampoRepository.findAllByActoPredio(ap);
			log.debug("actoPredioCampo {} ", actoPredioCampos);
		}

		CertificadoDeNoInscripcionVO clg = null;
		Certificado c = findByPrelacionId(prelacion.getId());

		if (c != null) {
			clg = certificadoTOCLgNoIncripcion(c);
			clg.setExpedicion(prelacion.getOficina().getNombre().trim() + ","
					+ prelacion.getOficina().getEstado().getNombre().trim());

		} else {
			clg = new CertificadoDeNoInscripcionVO();

			HashSet<BusquedaResultado> busquedasGuardadas = busquedaResultadoService
					.getPrediosFromPrelacionId(prelacion.getId());

			clg.setNumPrelacion(prelacion.getConsecutivo().toString());

			clg.setSolicitante(prelacion.getUsuarioSolicitan().getNombreCompleto());

			SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat formatterFechaAnio = new SimpleDateFormat("yyyy");
			SimpleDateFormat formatterFechaMes = new SimpleDateFormat("MMMM");
			SimpleDateFormat formatterFechaDia = new SimpleDateFormat("dd/MM/yyyy");

			Date fechaGeneracion = new Date();
			clg.setFecha(formatterFecha.format(prelacion.getFechaIngreso()));

			clg.setFechaRecepcion(formatterFecha.format(prelacion.getFechaIngreso()));
			clg.setHorasNumero(formatterHora.format(fechaGeneracion));
			clg.setCiudad(prelacion.getOficina().getEstado().getNombre().trim());
			clg.setDia(formatterFechaDia.format(fechaGeneracion));
			clg.setMesLetra(formatterFechaMes.format(fechaGeneracion).toUpperCase());
			clg.setAnio(formatterFechaAnio.format(fechaGeneracion));

			clg.setMunicipio(prelacion.getOficina().getNombre().trim());
			clg.setCuentaCatastral(getCuentaCatastral(prelacion));
			// clg.setFechaCertificado(formatterFecha.format(prelacion.getFechaIngreso()));
			if (prelacion.getFechaEnvioFirma() != null) {
				clg.setFechaCertificado(buildLeyendaFecha(prelacion.getFechaEnvioFirma()));
			} else {
				clg.setFechaCertificado("-");
			}
			clg.setExpedicion(prelacion.getOficina().getNombre().trim() + ","
					+ prelacion.getOficina().getEstado().getNombre().trim());
			if (busquedasGuardadas != null && busquedasGuardadas.size() > 0) {
			} else {
			}
		}
		if (actoPredioCampos.size() > 0) {
			for (ActoPredioCampo apc : actoPredioCampos) {
				if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_DATOS_PREDIO)) {
					clg.setDatosPredio(apc.getValor());
				}
				if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_NOMBRE_SOLICITANDO)) {
					sb.append(apc.getValor());
				}
				if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_PRIMER_APELLIDO)) {
					sb.append(" " + apc.getValor());
				}
				if (apc.getModuloCampo().getId().equals(Constantes.MODULO_CAMPO_SEGUNDO_APELLIDO)) {
					sb.append(" " + apc.getValor());
				}

			}
		}
		if (prelacion.getFechaEnvioFirma() != null) {
			clg.setFechaCertificado(buildLeyendaFecha(prelacion.getFechaEnvioFirma()));
		} else {
			clg.setFechaCertificado("-");
		}
		clg.setExpide(sb.toString());
		if (clg.getExpide() != null) {
			clg.setFavorDe(clg.getExpide());
		}
		clg.setRecibos(boletaRegistralService.buildRecibos(prelacion.getId()));
		clg.setRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
		clg.setCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));

		// FIRMA ELECTRONICA REGISTRADOR
		clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));

		// FIRMA ELECTRONICA COORDINADOR
		String firmaCoord = "-SIN FIRMA3 -";
		if (prelacion.getFirma() != null)
			firmaCoord = prelacion.getFirma() + "\r\n";
		clg.setFirmaCoordinador(firmaCoord);
		clg.setOficina(prelacion.getOficina().getNombre());
		log.debug("OFICINA_AQUI " + prelacion.getOficina().getNombre());
		return clg;

	}

	public int condicionGravamen(Prelacion prelacion) {
		int gravamen = 0;
		List<PrelacionPredio> prelacionPredioList = new ArrayList<PrelacionPredio>();
		List<ActoPredio> actoPredioCertificadoList = new ArrayList<ActoPredio>();
		prelacionPredioList = prelacionPredioService.findAllPrelacionPredioFromPrelacion(prelacion);

		for (PrelacionPredio pp : prelacionPredioList) {
			actoPredioCertificadoList = actoPredioRepository.findActopredioByPrelacionId(pp.getPredio().getId());
		}
		if (actoPredioCertificadoList.size() > 0) {
			for (ActoPredio apCertificado : actoPredioCertificadoList) {
				if (apCertificado.getActo().getStatusActo().getId().equals(1L)) {
					gravamen = 1;
					// LIBERTAD O GRAVAMEN
					if (apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_CEDULAS_HIPOTECARIAS)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_EMBARGOS)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_INSTITUCIONES_CREDITO)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_FIRA)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_RECONOCIMIENTO_DE_ADEUDO)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_PROCAMPO)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_CREDITO_SIMPLE)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_ARRENDAMIENTO)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_GARANTIA_REAL)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_SUBROGACION)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_COMODATO)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_PROPIEDAD_POSESION)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_FIDEICOMISO)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_TRANSMICION_DE_PROPIEDAD)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_FIANZAS)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_CREDITO_INSCRITO)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_FRUTOS_PENDIENTES)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_USUFRUCTO)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_DIVISION)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_CONVENIOS_JUDICIALES)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_SERVIDUMBRE)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_CONTRATOS_ENTRE_PARTICULARES)
							|| apCertificado.getActo().getTipoActo().getId()
									.equals(Constantes.TIPO_ACTO_CREDITO_AGRICOLA)
							|| apCertificado.getActo().getTipoActo().getId().equals(Constantes.TIPO_ACTO_INFONAVIT)) {
						// GRAVAMEN
						gravamen = 2;
						break;
					}
				}

			}
		}
		return gravamen;
	}

	public CertificadoCopiasVO certificadoTOCopias(Certificado certificado) {
		CertificadoCopiasVO clg = jsonTOCopias(certificado.getDatos());

		return clg;
	}

	public byte[] getCertificadoCopias(long prelacionId) {
		Prelacion pre = prelacionService.findOne(prelacionId);
		log.info("getCertificadoCopias " + prelacionId);
		log.info("pre " + pre);
		CertificadoCopiasVO certificado = null;

		JasperPrint jasperPrint = null;
		JasperReport jasperReport = null;
		InputStream jasperStream = null;
		byte[] dataReport = null;

		try {
			certificado = generaCertificadoCopias(pre);
		} catch (RequerimientoException ex) {
			log.debug("Excepcion de al generar certificado " + ex.getMessage());
			return null;
		}

		List<CertificadoCopiasVO> certificados = new ArrayList<CertificadoCopiasVO>();
		certificados.add(certificado);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("datasource", ds);
		parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		log.info("PARAMETROS_MAP: " + parameterMap);

		try {
			jasperStream = this.getClass().getResourceAsStream("/reports/pdfCopiasCertificadas.jasper");
			jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
			dataReport = JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (JRException e) {
			e.printStackTrace();
		}

		return dataReport;
	}

	public byte[] generCertificadoConAviso(Long prelacionId,boolean marcaAgua) throws JRException {
		Prelacion pre = prelacionService.findOne(prelacionId);
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		try {
			// IHM getCertificado()
			certificados = getCertificadoDeLivertadIGravamen(pre);
		} catch (RequerimientoException ex) {
			log.debug("Excepcion de al generar certificado " + ex.getMessage());
			return null;
		}

		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", ds);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		if(marcaAgua)
			parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		parameterMap.put(JRParameter.REPORT_LOCALE, Constantes.locale);


		String qR = parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/certificado/" + prelacionId;
		parameterMap.put("QR_BASE_URI", qR);

		InputStream jasperStream = this.getClass().getResourceAsStream(
				"/reports/certificadoLibertadOGravamen/pdfCertificadoLibertadOGravamenAviso.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	public byte[] generCertificadoConAviso(Long prelacionId,boolean marcaAgua,long predioId) throws JRException {
		Prelacion pre = prelacionService.findOne(prelacionId);
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		List<CertificadoLibertadOGravamenVO> certificadosOk = new ArrayList<CertificadoLibertadOGravamenVO>();
		
		try {
			// IHM getCertificado()
			certificados = getCertificadoDeLivertadIGravamen(pre);
			for(CertificadoLibertadOGravamenVO clg:certificados) {
				if(clg.getPredioId() == predioId) {
					certificadosOk.add(clg);
				}
			}
		} catch (RequerimientoException ex) {
			log.debug("Excepcion de al generar certificado " + ex.getMessage());
			return null;
		}

		JRDataSource ds = new JRBeanCollectionDataSource(certificadosOk);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", ds);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		if(marcaAgua)
			parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		parameterMap.put(JRParameter.REPORT_LOCALE, Constantes.locale);
		

		String qR = parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/certificado/" + prelacionId;
		parameterMap.put("QR_BASE_URI", qR);

		InputStream jasperStream = this.getClass().getResourceAsStream(
				"/reports/certificadoLibertadOGravamen/pdfCertificadoLibertadOGravamenAviso.jasper");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	public byte[] genBoletaCertificadoInserto(Long prelacionId,boolean marcaAgua) {
		byte[] boleta = null;
		List<Acto> actos = actoRepository.findAllByPrelacionId(prelacionId);
		try {
			for (Acto a : actos) {
				if (a.getTipoActo().getId() == 210L) {
					if (a.getStatusActo().getId() == 5L) {
						boleta = boleta == null ? boletaRegistralService.getBoletaRechazo(a.getId(),marcaAgua)
								: pdfService.appendPDF(boleta, boletaRegistralService.getBoletaRechazo(a.getId(),marcaAgua));
					}
				}
				if (a.getTipoActo().getId() == 123L) {
					if (a.getStatusActo().getId() == 5L) {
						boleta = boleta == null ? boletaRegistralService.getBoletaRechazo(a.getId(),marcaAgua)
								: pdfService.appendPDF(boleta, boletaRegistralService.getBoletaRechazo(a.getId(),marcaAgua));
					}
				}
			}

			boleta = boleta == null ? boletaRegistralService.generPdfBoletaRegistralPorActo(prelacionId, false, null,marcaAgua)
					: pdfService.appendPDF(boleta,
							boletaRegistralService.generPdfBoletaRegistralPorActo(prelacionId, false, null,marcaAgua));
			boleta = boleta == null ? generCertificadoConAviso(prelacionId,marcaAgua)
					: pdfService.appendPDF(boleta, generCertificadoConAviso(prelacionId,marcaAgua));

		} catch (Exception e) {
			log.debug("ERROR CERTIFICADO CON AVISO: " + e.getMessage());
		}
		return boleta;
	}

	public byte[] genBoletaCertificadoInsertoConPredio(Long prelacionId,boolean marcaAgua,long predioId) {
		byte[] boleta = null;
		try {
			boleta =  generCertificadoConAviso(prelacionId,marcaAgua,predioId);
			

		} catch (Exception e) {
			log.debug("ERROR CERTIFICADO CON AVISO: " + e.getMessage());
		}
		return boleta;
	}
	
	public byte[] getPdfCertificadoLg(Prelacion prelacion,boolean marcaAgua) throws JRException {
		byte[] clgs=null;
		int cont=0;
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		try {
			// IHM getCertificado();
			certificados = certificadoService.getCertificadoDeLivertadIGravamen(prelacion);
			if(certificados!=null && !certificados.isEmpty()) {
				for(CertificadoLibertadOGravamenVO clg:certificados){
					try {
						ArrayList<CertificadoLibertadOGravamenVO> certificado = new ArrayList<CertificadoLibertadOGravamenVO>();
						certificado.add(clg);
						byte[] aux=null;
						if(cont==0){					
							clgs = this.clgToPdfArrayByte(certificado, marcaAgua, prelacion) ;
						}else{
							aux = this.clgToPdfArrayByte(certificado, marcaAgua, prelacion) ;
							if(aux!=null)
								clgs = pdfService.appendPDF(clgs, aux);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}	
					cont++;
				}
			}
		
		} catch (RequerimientoException ex) {

			log.debug("Excepcion de al generar certificado " + ex.getMessage());
			return null;
		}

		return clgs;

	}

	public byte[] getPdfCertificadoLgConPredio(Prelacion prelacion,boolean marcaAgua,long predioId) throws JRException {
		byte[] clgs=null;
		int cont=0;
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		try {
			// IHM getCertificado();
			certificados = certificadoService.getCertificadoDeLivertadIGravamen(prelacion);
			if(certificados!=null && !certificados.isEmpty()) {
				for(CertificadoLibertadOGravamenVO clg:certificados){
					if(clg.getPredioId() == predioId){
						try {
							ArrayList<CertificadoLibertadOGravamenVO> certificado = new ArrayList<CertificadoLibertadOGravamenVO>();
							certificado.add(clg);
							byte[] aux=null;
							if(cont==0){					
								clgs = this.clgToPdfArrayByte(certificado, marcaAgua, prelacion) ;
							}else{
								aux = this.clgToPdfArrayByte(certificado, marcaAgua, prelacion) ;
								if(aux!=null)
									clgs = pdfService.appendPDF(clgs, aux);
							}
						}
						catch(Exception e) {
							e.printStackTrace();
						}	
						cont++;
					}					
				}
			}

		} catch (RequerimientoException ex) {

			log.debug("Excepcion de al generar certificado " + ex.getMessage());
			return null;
	}

		return clgs;

	}
	public byte[] getPdfCertificadoInscripcionNoInscripcion(Long prelacionId,boolean marcaAgua) {
		Prelacion pre = prelacionService.findOne(prelacionId);
		Certificado c = findByPrelacionId(prelacionId);
		byte[] result = null;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		if (c != null) {
			if (c.getTipoRespuesta() == 2) { // INSCRIPCION
				CertificadoDeNoInscripcionVO certificadoIns = null;

				try {
					certificadoIns = certificadoService.generaCertificadoInscripcion(pre);
					List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();
					certificados.add(certificadoIns);
					JRDataSource ds = new JRBeanCollectionDataSource(certificados);
					parameterMap.put("datasource", ds);
					if(marcaAgua)
						parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
					String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
					parameterMap.put("QR_BASE_URI", qR);
					InputStream jasperStream = this.getClass()
							.getResourceAsStream("/reports/pdfCertificadoInscripcion.jasper");

					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

					result = JasperExportManager.exportReportToPdf(jasperPrint);
				} catch (Exception ex) {

				}

			} else { // NO INSCRIPCION
				List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();

				CertificadoDeNoInscripcionVO certificadoNoIns = null;
				try {
					certificadoNoIns = certificadoService.generaCertificadoNoInscripcion(pre);
					certificados.add(certificadoNoIns);
					JRDataSource ds = new JRBeanCollectionDataSource(certificados);
					if(marcaAgua)
						parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
					
					String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
					parameterMap.put("QR_BASE_URI", qR);
					parameterMap.put("datasource", ds);
					InputStream jasperStream = this.getClass()
							.getResourceAsStream("/reports/pdfCertificadoNoInscripcion.jasper");

					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

					result = JasperExportManager.exportReportToPdf(jasperPrint);
				} catch (Exception ex) {

				}
			}
		}

		return result;

	}

	public byte[] getPdfCertificadoPropiedadNoPropiedad(Long prelacionId,boolean marcaAgua) {
		Prelacion pre = prelacionService.findOne(prelacionId);
		Certificado c = findByPrelacionId(prelacionId);
		byte[] result = null;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		
		if(marcaAgua)
			parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
		InputStream jasperStream = null;
		if (c != null) {
			if (c.getTipoRespuesta() == 2) { // INSCRIPCION
				jasperStream = this.getClass().getResourceAsStream("/reports/pdfCertificadoPropiedad.jasper");
			} else {
				jasperStream = this.getClass().getResourceAsStream("/reports/pdfCertificadoNoPropiedad.jasper");
			}

			CertificadoDeNoInscripcionVO certificadoIns = null;

			try {
				certificadoIns = certificadoService.generaCertificadoNoInscripcion(pre);
				List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();
				certificados.add(certificadoIns);
				JRDataSource ds = new JRBeanCollectionDataSource(certificados);
				parameterMap.put("datasource", ds);

				JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

				result = JasperExportManager.exportReportToPdf(jasperPrint);
			} catch (Exception ex) {

			}

		}

		return result;

	}

	@Transactional // CV
	public void delete(List<Certificado> certificados) {
		this.certificadoRepository.delete(certificados);
	}

	public boolean esClgConAviso(Long prelacionId) {

		List<Acto> actos = actoRepository.findAllByPrelacionId(prelacionId);

		Boolean es210 = false;
		for (Acto a : actos) {
			if (a.getTipoActo().getId() == 210L) {
				if (a.getStatusActo().getId() != 5L) {
					es210 = true;
				}
			}
		}
		return es210;
	}

	public byte[] printActoPublicitarioCertificadoByActoPublicitarioId(Long prelacionId,boolean marcaAgua) {
	
		byte[] pdfResult = null;
		try {	
			pdfResult = getCaratulaCopiaCertificadaActosPublicitarios(prelacionId,marcaAgua);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pdfResult;
	}

	public byte[] getCaratulaCopiaCertificadaActosPublicitarios(Long prelacionId,boolean marcaAgua) throws JRException {
		JasperPrint jasperPrint = null;
		JasperReport jasperReport = null;
		InputStream jasperStream = null;
		JRDataSource ds = null;

		byte[] asiento = null;
		byte[] aux = null;
		HashSet<BusquedaResultado> busquedaRs = busquedaResultadoRepository.findAllByPrelacionId(prelacionId);
		List<CopiaActoPublicitarioVO> CopiaActoPublicitariosVO = new ArrayList<CopiaActoPublicitarioVO>();
		Prelacion pre = prelacionService.findOne(prelacionId);
		int c=0;
		for (BusquedaResultado br : busquedaRs) {
				CopiaActoPublicitariosVO =new ArrayList<CopiaActoPublicitarioVO>();
				
				CopiaActoPublicitarioVO copiaActoPublicitario = busquedaResultadoService.generaCopiaActoPublicitario(br);
				CopiaActoPublicitariosVO.add(copiaActoPublicitario);
				ds = new JRBeanCollectionDataSource(CopiaActoPublicitariosVO);
				Map<String, Object> parameterMap = new HashMap<String, Object>();	
				parameterMap.put("datasource", ds);
				parameterMap.put("path", Constantes.IMG_PATH);
				
				if(marcaAgua)
					parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
				
				parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
				String qR = parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
				parameterMap.put("QR_BASE_URI", qR);
				jasperStream = this.getClass().getResourceAsStream("/reports/caratulaCopiasCertificadasDeActoPublicitario.jasper");
				jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
				if(c==0) {
					asiento = JasperExportManager.exportReportToPdf(jasperPrint);
				}else {
					aux = JasperExportManager.exportReportToPdf(jasperPrint);
					try {
						asiento = pdfService.appendPDF(asiento, aux);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (br.getActoPublicitario().getNum_folio_real() != null) {			
					aux= actoPublicitarioService.asientoPorActo(br.getActoPublicitario().getActo());
					try {
						asiento = pdfService.appendPDF(asiento, aux);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					aux = getActoPublicitarioByActoPublicitarioId(prelacionId,marcaAgua);
					try {
						asiento = pdfService.appendPDF(asiento, aux);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				c++;
			}
			if(pre.getStatus().getId() == 7 || pre.getStatus().getId() == 8 ){
				aux = imprimeCertificadoCopias(pre.getId(),marcaAgua);
				if(aux != null){
					try {
						asiento = pdfService.appendPDF(asiento, aux);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
			}
	        return asiento;

	    }
	   
	   public byte[] getActoPublicitarioByActoPublicitarioId(Long prelacionId,Boolean marcaDeAgua) throws JRException {
	        JasperPrint jasperPrint = null;
	        JasperReport jasperReport = null;
	        InputStream jasperStream = null;
	        JRDataSource ds = null;
	        
	        List<ActoPublicitarioVO> actosPublicitarios=null;
	        BusquedaResultado  busquedaR  =  busquedaResultadoRepository.findFirstByPrelacionIdOrderByIdDesc(prelacionId);

			actosPublicitarios=prelacionService.getActoPublicitarioById(busquedaR.getActoPublicitario().getId());

	        ds = new JRBeanCollectionDataSource(actosPublicitarios);

	        Map<String, Object> parameterMap = new HashMap<String, Object>();

	        parameterMap.put("datasource", ds);
	        parameterMap.put("path", Constantes.IMG_PATH);
			if(marcaDeAgua) {
				parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
			}
			
	        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
			String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+prelacionId;
			parameterMap.put("QR_BASE_URI", qR);
	        jasperStream = this.getClass().getResourceAsStream("/reports/actosPublicitarios/pdfActoPublicitario.jasper");
	        jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
	        jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

	        return JasperExportManager.exportReportToPdf(jasperPrint);

	    }
	   
		public byte[] imprimeCertificadoCopias(Long prelacion,Boolean marcaDeAgua) throws JRException {
			JasperPrint jasperPrint = null;
	        JasperReport jasperReport = null;
	        InputStream jasperStream = null;
			
			    Prelacion pre = prelacionService.findOne(prelacion);
				String certifica = " ";
			   CertificadoCopiasVO certificado = null;

			try{

				certificado=  certificadoService.generaCertificadoCopias(pre);
				certifica=  certificadoService.generaTextoCertifica(pre);
			}
			catch(RequerimientoException ex){

				log.debug("Excepcion de al generar certificado " +ex.getMessage());
				return null;

			}

			List<CertificadoCopiasVO> certificados = new ArrayList<CertificadoCopiasVO>();
			certificados.add(certificado);
			JRDataSource ds = new JRBeanCollectionDataSource(certificados);

			 Map<String, Object> parameterMap = new HashMap<String, Object>();

			    parameterMap.put("certifica", certifica);
		        parameterMap.put("datasource", ds);
		        parameterMap.put("path", Constantes.IMG_PATH);
		        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
				if(marcaDeAgua){
					parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
				}
				
				String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
				parameterMap.put("QR_BASE_URI", qR);
		        jasperStream = this.getClass().getResourceAsStream("/reports/pdfCopiasCertificadas.jasper");
		        jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		        jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

		        return JasperExportManager.exportReportToPdf(jasperPrint);
		}
		

		public void delete(Long id) {
			this.certificadoRepository.delete(id);
		}

		public void delete(Certificado certificado) {
			this.certificadoRepository.delete(certificado);
		}
		
		private String montoGravamenes(Acto acto, String monto) {
			String result =  "";
			DecimalFormat formatoPesos = new DecimalFormat("###,###,###.######");
			if(acto.getId_aaj() == null) 
			{
				try {
					if(monto!=null && !monto.isEmpty())
					   result = CommonUtil.formatDecimal(monto); //formatoPesos.format(new Double(monto));
				}catch(Exception e) {}
			}else {
				result = monto;
			}
			return result;
		}

		public String withoutComma(String cadena) {
			
			String cadenaCompleta="";
			if(cadena!=null && !cadena.isEmpty()) {
				String original = cadena.trim();
				if(original.charAt(0) == ',' ) {
					cadenaCompleta = original.substring(1, original.length());
				}else {
					cadenaCompleta = original;
				}
			}
			return cadenaCompleta;
		}
		
		private  byte[]  clgToPdfArrayByte(ArrayList<CertificadoLibertadOGravamenVO>certificados, Boolean marcaAgua, Prelacion prelacion) throws JRException {
			JRDataSource ds = new JRBeanCollectionDataSource(certificados);

			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("datasource", ds);
			parameterMap.put("path", Constantes.IMG_PATH);
			parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
			parameterMap.put("imgPath", Constantes.IMG_PATH);
			parameterMap.put("total", certificados != null ? certificados.size() : 0);
			
			if(marcaAgua)
				parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
			
			parameterMap.put(JRParameter.REPORT_LOCALE, Constantes.locale);
			String qR = parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/certificado/"
					+ prelacion.getId();
			parameterMap.put("QR_BASE_URI", qR);

			InputStream jasperStream = this.getClass()
					.getResourceAsStream("/reports/certificadoLibertadOGravamen/pdfCertificadoLibertadOGravamen.jasper");

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

			return JasperExportManager.exportReportToPdf(jasperPrint);
		}
}
