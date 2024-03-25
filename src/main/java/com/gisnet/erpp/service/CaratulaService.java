package com.gisnet.erpp.service;

import static com.gisnet.erpp.util.Constantes.ETipoFolio.MUEBLE;
import static com.gisnet.erpp.util.Constantes.ETipoFolio.PERSONAS_AUXILIARES;
import static com.gisnet.erpp.util.Constantes.ETipoFolio.PERSONAS_JURIDICAS;
import static com.gisnet.erpp.util.Constantes.ETipoFolio.PREDIO;
import com.gisnet.erpp.util.Constantes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.dao.CaratulaDAO;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.BloqueoFolio;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.DevArt71;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioMigrado;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.TipoAsent;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.FolioSeccionAuxiliarRepository;
import com.gisnet.erpp.repository.MuebleRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.PjPersonaRepository;
import com.gisnet.erpp.repository.PredioMigradoRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.service.excepciones.PasesException;
import com.gisnet.erpp.service.excepciones.RequerimientoException;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.FolioRealVO;
import com.gisnet.erpp.vo.caratula.ActoVO;
import com.gisnet.erpp.vo.caratula.AntecedenteVO;
import com.gisnet.erpp.vo.caratula.CaratulaImpresionVO;
import com.gisnet.erpp.vo.caratula.ColindanciaVO;
import com.gisnet.erpp.vo.caratula.DevArt71VO;
import com.gisnet.erpp.vo.caratula.FolioSeccionAuxiliarVO;
import com.gisnet.erpp.vo.caratula.MuebleVO;
import com.gisnet.erpp.vo.caratula.PaseVO;
import com.gisnet.erpp.vo.caratula.PersonaJuridicaVO;
import com.gisnet.erpp.vo.caratula.PredioVO;
import com.gisnet.erpp.vo.caratula.PropietarioVO;
import com.gisnet.erpp.vo.caratula.TramiteVO;
import com.gisnet.erpp.web.api.imprimeprelacion.caratula.CaratulaVO;
import com.gisnet.erpp.vo.certificado.CertificadoLibertadOGravamenVO;
import com.ibm.icu.text.SimpleDateFormat;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import org.springframework.util.CollectionUtils;

@Service
public class CaratulaService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PrelacionRepository prelacionRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	ActoService actoService;

	@Autowired
	PredioRepository predioRepository;

	@Autowired
	PersonaJuridicaRepository personaJuridicaRepository;

	@Autowired
	MuebleRepository muebleRepository;

	@Autowired
	FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;

	@Autowired
	PasesService pasesService;

	@Autowired
	DevArt71Service devArt71Service;

	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	private CaratulaDAO caratulaDAO;

	@Autowired
	PredioMigradoRepository predioMigradoRepository;
	
	@Autowired
	private PredioPersonaService predioPersonaService;

	@Autowired
	private ColindanciaService colindanciaService;

	@Autowired
	private PjPersonaRepository pjPersonaRepository;

	@Autowired
	private BloqueoFolioService bloqueoFolioService;

	@Autowired
	PdfService pdfService;

	@Autowired
	private PredioService predioService;

	@Autowired
	private FormasPreCodificadasService formasService;

	@Autowired
	private PrelacionService prelacionService;

	@Autowired
	CertificadoService certificadoService;

	@Autowired
	private BoletaRegistralService boletaRegistralService;

	@Autowired
	private PersonaJuridicaService personaJuridicaService;

	@Autowired
	private PredioBitacoraService predioBitacoraService;

	@Autowired
	private ActoPredioService actoPredioService;

	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	OficinaRepository oficinaRepository;

	@Transactional(readOnly = true)
	public PredioVO getCaratulaPredio(Integer noFolio, Long oficinaId) {
		PredioVO predioVO = new PredioVO();
		Antecedente antecedente = null;

		Predio predio = predioRepository.findByNoFolioAndOficinaId(noFolio, oficinaId);

		if (predio == null) {
			System.out.println("PREDIO NULL ");
			return null;

		}

		return this.getCaratulaPredio(predio);
	}

	@Transactional(readOnly = true)
	public PredioVO getCaratulaPredioFolioAnterior(Integer noFolioAnterior, Long oficinaId) {
		PredioVO predioVO = new PredioVO();
		Antecedente antecedente = null;
		List<Antecedente> antecedentes = null;
		Predio predio = predioRepository.findByNumeroFolioRealAndOficinaIdAndAuxiliarIsNull(noFolioAnterior, oficinaId);

		if (predio == null) {
			System.out.println("PREDIO NULL ");
			return null;

		}

		return this.getCaratulaPredio(predio);
	}

	@Transactional(readOnly = true)
	public PredioVO getCaratulaPredioFolioAnteriorAuxiliar(Integer noFolioAnterior, Integer auxiliar, Long oficinaId) {
		PredioVO predioVO = new PredioVO();
		Antecedente antecedente = null;

		Predio predio = predioRepository.findByNumeroFolioRealAndAuxiliarAndOficinaId(noFolioAnterior, auxiliar,
				oficinaId);

		if (predio == null) {
			System.out.println("PREDIO NULL ");
			return null;

		}

		return this.getCaratulaPredio(predio);
	}

	@Transactional(readOnly = true)
	public PredioVO getCaratulaPredio(Long predioId) {
		Predio predio = predioRepository.findOne(predioId);
		// System.out.println("11OSIEL \n "+predio.getId());
		return this.getCaratulaPredio(predio);
	}

	@Transactional(readOnly = true)
	private PredioVO getCaratulaPredio(Predio predio) {
		PredioVO predioVO = new PredioVO();
		Antecedente antecedente = null;
		List<Antecedente> antecedentes = new ArrayList<Antecedente>();
		List<AntecedenteVO> antecedentesVo = new ArrayList<AntecedenteVO>();
		long countGravamenes = actoPredioRepository.countAllGravamenesByPredioId(predio.getId());
		log.debug("===> listGravamenes \n " + countGravamenes + "\n   " + predio.getId());
		predioVO.setLibre(true);
		List<ActoPredio> aps = actoPredioRepository.getAllGravamenesByPredioId(predio.getId());
		System.out.println("getAllGravamenesByPredioId Size:" + aps.size());
		for (ActoPredio ap : aps) {
			if (predioVO.isLibre()) {
				System.out.println("tipo acto :" + ap.getActo().getTipoActo().getId().intValue());
				switch (ap.getActo().getTipoActo().getId().intValue()) {
					case 25:
					case 27:
					case 28:
					case 31:// CÉDULA HIPOTECARIA
					case 227:// CESIÓN DE DERECHOS CREDITICIOS
					case 64:// CONVENIO MODIFICATORIO
					case 9: // credito o hipoteca
					case 32:// EMBARGO
					case 37:// FIANZA
					case 14://FIDEICOMISO
					case 38:// ASEGURAMIENTO DE BIENES
					case 95:// EMBARGO (AREA juridico)
					case 96:// ARRENDAMIENTO FINANCIERO
					case 34:// ARRENDAMIENTO FINANCIERO
					case 121:// RETENCIÓN
						predioVO.setLibre(false);
						break;
					default:
						predioVO.setLibre(true);
						break;
				}
			} else {
				break;
			}
		}
		
		if(predio.getFolioCalidad()!=null){
			predioVO.setFolioCalidad(predio.getFolioCalidad());
		}else {
			predioVO.setFolioCalidad(null);
		}
		
		predioVO.setExtinto(predio.getStatusActo().getId().equals(Constantes.StatusActo.INVALIDO.getIdStatusActo()));

		// (9,|14,31,32,|34,|35,37,38,|48,|55,64,95,96,121,|217,|219,227,|228,|230,|234
		/*
		 * if( countGravamenes == 0 ) { predioVO.setLibre( true ); } else {
		 * predioVO.setLibre( false ); }
		 */
		
		predioVO.setPredioId(predio.getId());
		predioVO.setNoFolio(predio.getNoFolio());
		predioVO.setstatusActo(predio.getStatusActo());
		if (predio.getNumeroFolioReal() != null) {
			predioVO.setNoFolioAnterior(predio.getNumeroFolioReal());
		}
		if (predio.getAuxiliar() != null) {
			predioVO.setAuxiliar(predio.getAuxiliar());
		}

		

		Boolean bloqueado = predio.getBloqueado();
		if (bloqueado != null && bloqueado) {
			predioVO.setBloqueado(bloqueado);
			return predioVO;
		}

		if (predio.getPredioAntesParaPredios() != null && predio.getPredioAntesParaPredios().size() > 0) {
			antecedente = predio.getPredioAntesParaPredios().iterator().next().getAntecedente();
			predio.getPredioAntesParaPredios().forEach(x -> {
				antecedentes.add( x.getAntecedente());
			});
		}

		predioVO.setProcedeFolio(predio.getProcedeDeFolio());

		Integer primerRegistro = predio.getPrimerRegistro();
		predioVO.setPrimerRegistro(primerRegistro == null || primerRegistro != 1 ? false : true);

		/*
		 * if( predioVO.isPrimerRegistro() ) { Set<PrelacionPredio> prelacionPredios =
		 * predio.getPrelacionPrediosParaPredios(); for( PrelacionPredio pp :
		 * prelacionPredios ) { if( pp.getPrelacion().getPrimerRegistro() ) { if(
		 * pp.getPrelacion().getStatus().getId() !=
		 * Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus() ) {
		 * predioVO.setEnProceso( true ); return predioVO; } } } }
		 */
		
		if(antecedentes != null) {
			for(Antecedente ant:antecedentes) {
				if (ant != null) {
					AntecedenteVO antecedenteVO = new AntecedenteVO();
					if (ant.getLibro() != null) {
						antecedenteVO.setLibro(ant.getLibro().getNumLibro().toString());
						antecedenteVO.setBis(ant.getLibro().getLibroBis());
						antecedenteVO.setSeccion(ant.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
						antecedenteVO.setOficina(ant.getLibro().getSeccionesPorOficina().getOficina().getNombre());
						antecedenteVO.setAnio(ant.getLibro().getAnio());
						antecedenteVO.setVolumen(ant.getLibro().getVolumen());
						antecedenteVO.setAnio(ant.getLibro().getAnio());
					}
					
					antecedenteVO.setDocumento(ant.getDocumento());
					antecedenteVO.setDocumentoBis(ant.getDocumentoBis());
					antecedentesVo.add(antecedenteVO);
					
				}else if (predioVO.getProcedeFolio() != null) {
					
					AntecedenteVO antecedenteVO = new AntecedenteVO();
					antecedenteVO.setProcedeFolio(predioVO.getProcedeFolio());
					antecedentesVo.add(antecedenteVO);
				}
			}
			predioVO.setAntecedentes(antecedentesVo);
		}

		if (antecedente != null) {
			AntecedenteVO antecedenteVO = new AntecedenteVO();
			if (antecedente.getLibro() != null) {
				antecedenteVO.setLibro(antecedente.getLibro().getNumLibro().toString());
				antecedenteVO.setBis(antecedente.getLibro().getLibroBis());
				antecedenteVO.setSeccion(antecedente.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
				antecedenteVO.setOficina(antecedente.getLibro().getSeccionesPorOficina().getOficina().getNombre());
				antecedenteVO.setAnio(antecedente.getLibro().getAnio());
				antecedenteVO.setVolumen(antecedente.getLibro().getVolumen());
				antecedenteVO.setAnio(antecedente.getLibro().getAnio());
			}
			
			antecedenteVO.setDocumento(antecedente.getDocumento());
			antecedenteVO.setDocumentoBis(antecedente.getDocumentoBis());

			predioVO.setAntecedente(antecedenteVO);
		} else if (predioVO.getProcedeFolio() != null) {
			AntecedenteVO antecedenteVO = new AntecedenteVO();
			antecedenteVO.setProcedeFolio(predioVO.getProcedeFolio());
			predioVO.setAntecedente(antecedenteVO);
		}

		predioVO.setTipoInmueble(predio.getTipoInmueble() != null ? predio.getTipoInmueble().getNombre() : "");
		predioVO.setNumInt(predio.getNumInt());
		predioVO.setNivel(predio.getNivel() != null ? predio.getNivel().getNombre() : "");
		predioVO.setEdificio(predio.getEdificio());
		predioVO.setNoLote(predio.getNoLote());
		predioVO.setLocalidadSector(predio.getLocalidadSector());
		predioVO.setFraccion(predio.getFraccion());
		predioVO.setManzana(predio.getManzana());
		predioVO.setMacroManzana(predio.getMacroManzana());

		predioVO.setTipoVialidad(predio.getTipoVialidad() != null ? predio.getTipoVialidad().getNombre() : "");
		predioVO.setVialidad(predio.getVialidad());
		predioVO.setNumExt(predio.getNumExt());
		predioVO.setEnlaceVialidad(predio.getEnlaceVialidad() != null ? predio.getEnlaceVialidad().getNombre() : "");
		predioVO.setNumInt(predio.getNumInt());
		predioVO.setTipoVialidad2(predio.getTipoVialidad2() != null ? predio.getTipoVialidad2().getNombre() : "");
		predioVO.setVialidad2(predio.getVialidad2());
		predioVO.setNumExt2(predio.getNumExt2());

		TipoAsent tipoAsent = predio.getTipoAsent();
		if (tipoAsent != null) {
			predioVO.setTipoAsentamiento(tipoAsent.getNombre());
		}
		predioVO.setAsentamiento(predio.getAsentamiento());
		predioVO.setNombreFracOCondo(predio.getFracOCondo() != null ? predio.getFracOCondo().getNombre() : "");
		predioVO.setNombreFracOCondo(predio.getNombreFracOCondo());
		predioVO.setEtapaOSeccion(predio.getEtapaOSeccion() != null ? predio.getEtapaOSeccion().getNombre() : "");

		predioVO.setZona(predio.getZona());
		predioVO.setClaveCatastral(predio.getClaveCatastral());
		predioVO.setCuentaCatastral(predio.getCuentaCatastral());
		predioVO.setClaveCatastralEstandard(predio.getClaveCatastralEstandard());

		if (predio.getMunicipio() != null) {
			predioVO.setEstado(predio.getMunicipio().getEstado().getNombre());
			predioVO.setMunicipio(predio.getMunicipio().getNombre());
		}

		if (predio.getOficina() != null) {
			predioVO.setOficinaId(predio.getOficina().getId());
			predioVO.setNomOficina(predio.getOficina().getNombre());
		}
		predioVO.setCp(predio.getCodigoPostal());
		predioVO.setSuperficie(predio.getSuperficie());
		predioVO.setUnidadMedida(predio.getUnidadMedida().getNombre());
		// predioVO.setSuperficieM2(predio.getSuperficieM2() );
		predioVO.setSuperficieM2(Optional.ofNullable(predio.getSuperficieM2()).orElse(0.0));
		predioVO.setUsoSuelo(predio.getUsoSuelo().getNombre());

		List<PaseVO> listaPases = pasesService.findPases(predio.getId());
		predioVO.setPases(listaPases);

		double totalSuperficieSegregada = pasesService.sumaSuperficie(listaPases);
		double superficie = pasesService.obtenSuperficieMetros(predio.getUnidadMedida().getId(),
				predio.getSuperficie());
		double superficieRestante = superficie - totalSuperficieSegregada;

		// log.debug( "==> totalSuperficieSegregada = "+totalSuperficieSegregada );
		// log.debug( "==> superficieRestante = "+superficieRestante );

		predioVO.setTotalSuperficieSegregada(totalSuperficieSegregada);
		predioVO.setSuperficieRestante(superficieRestante);

		List<DevArt71VO> listaDevoluciones = devArt71Service.findDevoluciones(predio.getId(), PREDIO.getTipoFolio());
		predioVO.setDevoluciones(listaDevoluciones);

		// List<Acto> actoTraslativo = new ArrayList<Acto>();
		// //actoService.getActosVigentesTraslativosParaPredio(predio.getId());

		// log.debug("acto_traslativo {}", actoTraslativo);

		List<PropietarioVO> propietarios = new ArrayList<PropietarioVO>();

		List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(predio.getId(), false);
		/*
		 * Propietarios
		 */

		predioPersonas.forEach((predioPersona) -> {
			PropietarioVO propietarioVO = new PropietarioVO();

			propietarioVO.setNombre(predioPersona.getPersona().getNombre());
			;
			propietarioVO.setPaterno(predioPersona.getPersona().getPaterno());
			;
			propietarioVO.setMaterno(predioPersona.getPersona().getMaterno());
			;
			propietarioVO.setFechaNac(predioPersona.getPersona().getFechaNac());
			;
			propietarioVO.setRfc(predioPersona.getPersona().getRfc());
			;
			propietarioVO.setCurp(predioPersona.getPersona().getCurp());
			;
			propietarioVO.setEstadoCivil(
					predioPersona.getEstadoCivil() != null ? predioPersona.getEstadoCivil().getNombre() : "");
			;
			propietarioVO.setRegimen(predioPersona.getRegimen() != null ? predioPersona.getRegimen().getNombre() : "");
			;
			propietarioVO.setNacionalidad(
					predioPersona.getNacionalidad() != null ? predioPersona.getNacionalidad().getNombre() : "");
			;
			propietarioVO.setDesignoBeneficiario(predioPersona.getDesignoBeneficiario());
			propietarioVO.setPorcentajeDd(predioPersona.getPorcentajeDd());
			;
			propietarioVO.setPorcentajeUv(predioPersona.getPorcentajeUv());
			;

			propietarios.add(propietarioVO);

		});

		/**
		 * if (propietarios.isEmpty()){ throw new IllegalArgumentException("El predio no
		 * tiene propietarios"); }
		 */

		predioVO.setPropietarios(propietarios);

		/*
		 * Colindancias
		 */

		// Set<Colindancia> col =
		// colindanciaService.findColindanciasActuales(predio.getId());
		Set<Colindancia> col = colindanciaService.findColindancias(predio.getId());

		List<ColindanciaVO> colindancias = new ArrayList<ColindanciaVO>();

		if (col != null) {
			for(Colindancia colindancia: col) {
				colindancias.add(new ColindanciaVO(colindancia.getId(), colindancia.getOrientacion().getNombre(),
						colindancia.getNombre()));
			}
		}
	
		predioVO.setColindancias(colindancias);

		/*
		 * devoluciones
		 */

		/*
		 * Sumatorio de actos por clasif acto
		 */

		List<ActoVO> clasifActos = caratulaDAO.findTotalActivosEInexitentesPorClasifActo(predio.getId(), PREDIO);
		// clasifActos.forEach((clasifActo) -> { if
		// (clasifActo.getClasifActoId()==Constantes.ClasifActo.TRASLATIVOS.getIdClasifActo()){
		// clasifActo.setExtintos(caratulaDAO.findTotalInexitentesParaTraslativos(predio.getId()));
		// } });

		predioVO.setSumatoriaActosPorClasifActo(clasifActos);

		/*
		 * tramites
		 */

		predioVO.setTramites(caratulaDAO.findTramitesParaPredio(predio.getId(), PREDIO));
		/*
		 * PREDIOS RESULTANTES
		 */
		predioVO.setResultantes(caratulaDAO.findFoliosResultantes(predioVO.getPredioId()));

		// BITACORA PREDIO POR ACTO RECTIFICACIÓN DE MEDIDAS
		predioVO.setPredioBitacora(predioBitacoraService.findByPredioActos(predioVO.getPredioId()));

		if(predio.getIndMatriz()!=null && predio.getIndMatriz()==true ||
			predioVO.getResultantes()!=null && !predioVO.getResultantes().isEmpty() ||
			predioVO.getPases()!=null && !predioVO.getPases().isEmpty()
		){
			predioVO.setIndMatriz("SI");
		}else{
			predioVO.setIndMatriz(null);
		}
		if(predio.getProcedeDeFolio()!=null && !predio.getProcedeDeFolio().trim().isEmpty()||
			predio.getAuxiliar()!=null && !predio.getAuxiliar().toString().isEmpty() 
		){
			predioVO.setIndAuxProcedeDe("SI");
		}else{
			predioVO.setIndAuxProcedeDe(null);
		}

		if(!CollectionUtils.isEmpty(predio.getActoPrediosParaPredios())) {
			predioVO.setActoPrediosParaPredios(predio.getActoPrediosParaPredios());
		}

		return predioVO;

	}

	@Transactional(readOnly = true)
	public List<TramiteVO> findTramitesInscritos(Integer predioId, Long statusActoId, Long clasifActoId,
			Integer tipoFolioId) {
		if (tipoFolioId == PERSONAS_JURIDICAS.getTipoFolio()) {
			return caratulaDAO.findTramitesInscritos(predioId, statusActoId, clasifActoId, PERSONAS_JURIDICAS);
		} else if (tipoFolioId == PERSONAS_AUXILIARES.getTipoFolio()) {
			return caratulaDAO.findTramitesInscritos(predioId, statusActoId, clasifActoId, PERSONAS_AUXILIARES);
		} else if (tipoFolioId == MUEBLE.getTipoFolio()) {
			return caratulaDAO.findTramitesInscritos(predioId, statusActoId, clasifActoId, MUEBLE);
		} else if (tipoFolioId == PREDIO.getTipoFolio()) {
			return caratulaDAO.findTramitesInscritos(predioId, statusActoId, clasifActoId, PREDIO);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public List<TramiteVO> findAllTramites(Long predioId, Integer tipoFolioId) {
		if (tipoFolioId == PERSONAS_JURIDICAS.getTipoFolio()) {
			return caratulaDAO.findTramitesParaPredio(predioId, PERSONAS_JURIDICAS);
		} else if (tipoFolioId == PERSONAS_AUXILIARES.getTipoFolio()) {
			return caratulaDAO.findTramitesParaPredio(predioId, PERSONAS_AUXILIARES);
		} else if (tipoFolioId == MUEBLE.getTipoFolio()) {
			return caratulaDAO.findTramitesParaPredio(predioId, MUEBLE);
		} else if (tipoFolioId == PREDIO.getTipoFolio()) {
			return caratulaDAO.findTramitesParaPredio(predioId, PREDIO);
		} else {
			return null;
		}
	}

	public PersonaJuridicaVO getCaratulaPersonaJuridicaByFolioAnterior(Integer folioAnterior, Long oficinaId) {
		PersonaJuridica personaJuridica = personaJuridicaRepository.findByNumeroFolioRealAndOficinaId(folioAnterior,
				oficinaId);
		return mapPersonaJuridica(personaJuridica);
	}

	private PersonaJuridicaVO mapPersonaJuridica(PersonaJuridica personaJuridica) {
		PersonaJuridicaVO personaJuridicaVO = new PersonaJuridicaVO();
		Antecedente antecedente = null;
		List<Antecedente> antecedentes = new ArrayList<Antecedente>();
		List<AntecedenteVO> antecedentesVo = new ArrayList<AntecedenteVO>();
		if (personaJuridica == null) {
			return null;
		}

		Boolean bloqueado = personaJuridica.getBloqueado();
		if (bloqueado != null && bloqueado) {
			personaJuridicaVO.setBloqueado(bloqueado);
			return personaJuridicaVO;
		}

		personaJuridicaVO.setPersonaJuridicaId(personaJuridica.getId());

		if (personaJuridica.getPjAnteParaPersonasJuridicas() != null
				&& personaJuridica.getPjAnteParaPersonasJuridicas().size() > 0) {
			antecedente = personaJuridica.getPjAnteParaPersonasJuridicas().iterator().next().getAntecedente();
			personaJuridica.getPjAnteParaPersonasJuridicas().forEach(x -> {
				antecedentes.add( x.getAntecedente());
			});
		}

		Integer primerRegistro = personaJuridica.getPrimerRegistro();
		personaJuridicaVO.setPrimerRegistro(primerRegistro == null || primerRegistro != 1 ? false : true);
		personaJuridicaVO.setNumeroFolioReal(personaJuridica.getNumeroFolioReal());
		/*
		 * if( personaJuridicaVO.isPrimerRegistro() ) { Set<ActoPredio> actoPredios =
		 * personaJuridica.getActoPrediosParaPersonasJuridicas(); for( ActoPredio ap :
		 * actoPredios ) { Prelacion p = ap.getActo().getPrelacion(); if(
		 * p.getPrimerRegistro() ) { if( p.getStatus().getId() !=
		 * Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus() ) {
		 * personaJuridicaVO.setEnProceso( true ); return personaJuridicaVO; } } } }
		 */

		personaJuridicaVO.setNoFolio(personaJuridica.getNoFolio());
		
		if(antecedentes != null) {
			for(Antecedente ant:antecedentes) {
				
					AntecedenteVO antecedenteVO = new AntecedenteVO();
					if (ant.getLibro() != null) {
						antecedenteVO.setLibro(ant.getLibro().getNumLibro().toString());
						antecedenteVO.setBis(ant.getLibro().getLibroBis());
						antecedenteVO.setSeccion(ant.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
						antecedenteVO.setOficina(ant.getLibro().getSeccionesPorOficina().getOficina().getNombre());
						antecedenteVO.setAnio(ant.getLibro().getAnio());
						antecedenteVO.setVolumen(ant.getLibro().getVolumen());
					}
					antecedenteVO.setAnio(ant.getLibro().getAnio());
					antecedenteVO.setDocumento(ant.getDocumento());
					antecedenteVO.setDocumentoBis(ant.getDocumentoBis());

					antecedentesVo.add(antecedenteVO);
				
			}
			personaJuridicaVO.setAntecedentes(antecedentesVo);
		}

		if (antecedente != null) {
			AntecedenteVO antecedenteVO = new AntecedenteVO();

			if (antecedente.getLibro() != null) {
				antecedenteVO.setLibro(antecedente.getLibro().getNumLibro());
				antecedenteVO.setBis(antecedente.getLibro().getLibroBis());
				antecedenteVO.setSeccion(antecedente.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
				antecedenteVO.setOficina(antecedente.getLibro().getSeccionesPorOficina().getOficina().getNombre());
				antecedenteVO.setAnio(antecedente.getLibro().getAnio());
				antecedenteVO.setVolumen(antecedente.getLibro().getVolumen());

			}
			antecedenteVO.setAnio(antecedente.getLibro().getAnio());
			antecedenteVO.setDocumento(antecedente.getDocumento());
			antecedenteVO.setDocumentoBis(antecedente.getDocumentoBis());

			personaJuridicaVO.setAntecedente(antecedenteVO);
		}

		if(personaJuridica.getTipoSociedad()!=null) {
			personaJuridicaVO.setTipoSociedad(personaJuridica.getTipoSociedad().getNombre());
		}
		personaJuridicaVO.setDenominacionNombre(personaJuridica.getDenominacionNombre());
		;

		personaJuridicaVO.setDuracion(personaJuridica.getDuracion());
		;
		personaJuridicaVO.setFechaApertura(personaJuridica.getFechaApertura());
		personaJuridicaVO.setObjeto(personaJuridica.getObjeto());
		;

		personaJuridicaVO
				.setMunicipio(personaJuridica.getMunicipio() != null ? personaJuridica.getMunicipio().getNombre() : "");
		;
		personaJuridicaVO.setEstado(
				personaJuridica.getMunicipio() != null ? personaJuridica.getMunicipio().getEstado().getNombre() : "");
		;

		personaJuridicaVO.setDireccion(personaJuridica.getDireccion());

		personaJuridicaVO.setNumSocios(personaJuridicaService
				.findTotalSociosOrganosApoderadoActuales(personaJuridica.getId(), Constantes.TipoRelPersona.SOCIO));
		personaJuridicaVO.setNumApoderados(personaJuridicaService
				.findTotalSociosOrganosApoderadoActuales(personaJuridica.getId(), Constantes.TipoRelPersona.APODERADO));
		personaJuridicaVO.setNumIntegrantes(personaJuridicaService.findTotalSociosOrganosApoderadoActuales(
				personaJuridica.getId(), Constantes.TipoRelPersona.ORGANO_ADMINISTRACION));

		List<ActoPredio> listGravamenes = actoPredioRepository
				.getAllGravamenesByPersonaJuridicaId(personaJuridica.getId());
		log.debug("===> listGravamenes = " + listGravamenes);
		if (listGravamenes == null || listGravamenes.isEmpty()) {
			personaJuridicaVO.setLibre(true);
		} else {
			personaJuridicaVO.setLibre(false);
		}

		List<DevArt71VO> listaDevoluciones = devArt71Service.findDevoluciones(personaJuridica.getId(),
				PERSONAS_JURIDICAS.getTipoFolio());
		personaJuridicaVO.setDevoluciones(listaDevoluciones);

		/*
		 * Sumatorio de actos por clasif acto
		 */

		List<ActoVO> clasifActos = caratulaDAO.findTotalActivosEInexitentesPorClasifActo(personaJuridica.getId(),
				PERSONAS_JURIDICAS);

		personaJuridicaVO.setSumatoriaActosPorClasifActo(clasifActos);

		/*
		 * tramites
		 */

		personaJuridicaVO.setTramites(caratulaDAO.findTramitesParaPredio(personaJuridica.getId(), PERSONAS_JURIDICAS));

		return personaJuridicaVO;
	}

	@Transactional(readOnly = true)
	public PersonaJuridicaVO getCaratulaPersonaJuridica(Integer noFolio, Long oficinaId) {
		PersonaJuridica personaJuridica = personaJuridicaRepository.findByNoFolioAndOficinaId(noFolio, oficinaId);
		return mapPersonaJuridica(personaJuridica);

	}

	@Transactional(readOnly = true)
	public MuebleVO getCaratulaMueble(Integer noFolio, Long oficinaId) {
		MuebleVO muebleVO = new MuebleVO();
		Antecedente antecedente = null;

		Mueble mueble = muebleRepository.findByNoFolioAndOficinaId(noFolio, oficinaId);

		if (mueble == null) {
			return null;
		}

		Boolean bloqueado = mueble.getBloqueado();
		if (bloqueado != null && bloqueado) {
			muebleVO.setBloqueado(bloqueado);
			return muebleVO;
		}

		muebleVO.setMuebleId(mueble.getId());

		if (mueble.getMuebleParaMuebles() != null && mueble.getMuebleParaMuebles().size() > 0) {
			antecedente = mueble.getMuebleParaMuebles().iterator().next().getAntecedente();
		}

		Integer primerRegistro = mueble.getPrimerRegistro();
		muebleVO.setPrimerRegistro(primerRegistro == null || primerRegistro != 1 ? false : true);

		/*
		 * if( muebleVO.isPrimerRegistro() ) { Set<ActoPredio> actoPredios =
		 * mueble.getActoPrediosParaMuebles(); for( ActoPredio ap : actoPredios ) {
		 * Prelacion p = ap.getActo().getPrelacion(); if( p.getPrimerRegistro() ) { if(
		 * p.getStatus().getId() != Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus()
		 * ) { muebleVO.setEnProceso( true ); return muebleVO; } } } }
		 */

		muebleVO.setNoFolio(mueble.getNoFolio());

		if (antecedente != null) {
			AntecedenteVO antecedenteVO = new AntecedenteVO();

			if (antecedente.getLibro() != null) {
				antecedenteVO.setLibro(antecedente.getLibro().getNombre());
				antecedenteVO.setBis(antecedente.getLibro().getLibroBis());
				antecedenteVO.setSeccion(antecedente.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
				antecedenteVO.setOficina(antecedente.getLibro().getSeccionesPorOficina().getOficina().getNombre());
				antecedenteVO.setAnio(antecedente.getLibro().getAnio());
				antecedenteVO.setVolumen(antecedente.getLibro().getVolumen());

			}

			antecedenteVO.setAnio(antecedente.getLibro().getAnio());
			antecedenteVO.setDocumento(antecedente.getDocumento());
			antecedenteVO.setDocumentoBis(antecedente.getDocumentoBis());

			muebleVO.setAntecedente(antecedenteVO);
		}

		muebleVO.setObjeto(mueble.getObjeto().getNombre());
		muebleVO.setEspecificacion(mueble.getEspecificacion());
		muebleVO.setMarca(mueble.getMarca());
		muebleVO.setModelo(mueble.getModelo());
		muebleVO.setNumSerie(mueble.getNumSerie());
		muebleVO.setNumDocIdentif(mueble.getNumDocIdentif());
		// muebleVO.setNumExt(mueble.getNumExt());
		muebleVO.setFechaDocIdentif(mueble.getFechaDocIdentif());

		/*
		 * Sumatorio de actos por clasif acto
		 */

		List<ActoVO> clasifActos = caratulaDAO.findTotalActivosEInexitentesPorClasifActo(mueble.getId(), MUEBLE);

		muebleVO.setSumatoriaActosPorClasifActo(clasifActos);

		/*
		 * tramites
		 */

		muebleVO.setTramites(caratulaDAO.findTramitesParaPredio(mueble.getId(), MUEBLE));

		return muebleVO;

	}

	@Transactional(readOnly = true)
	public FolioSeccionAuxiliarVO getCaratulaFolioSeccionAuxiliar(Integer noFolio, Long oficinaId) {
		FolioSeccionAuxiliarVO folioSeccionAuxiliarVO = new FolioSeccionAuxiliarVO();
		Antecedente antecedente = null;

		FolioSeccionAuxiliar folioSeccionAuxiliar = folioSeccionAuxiliarRepository.findOneByNoFolioAndOficinaId(noFolio,
				oficinaId);

		if (folioSeccionAuxiliar == null) {
			return null;
		}

		Boolean bloqueado = folioSeccionAuxiliar.getBloqueado();
		if (bloqueado != null && bloqueado) {
			folioSeccionAuxiliarVO.setBloqueado(bloqueado);
			return folioSeccionAuxiliarVO;
		}

		folioSeccionAuxiliarVO.setFolioSeccionAuxiliarId(folioSeccionAuxiliar.getId());

		if (folioSeccionAuxiliar.getAuxiliarAnteParaFolioSeccionAuxiliares() != null
				&& folioSeccionAuxiliar.getAuxiliarAnteParaFolioSeccionAuxiliares().size() > 0) {
			antecedente = folioSeccionAuxiliar.getAuxiliarAnteParaFolioSeccionAuxiliares().iterator().next()
					.getAntecedente();
		}

		Integer primerRegistro = folioSeccionAuxiliar.getPrimerRegistro();
		folioSeccionAuxiliarVO.setPrimerRegistro(primerRegistro == null || primerRegistro != 1 ? false : true);

		/*
		 * if( folioSeccionAuxiliarVO.isPrimerRegistro() ) { Set<ActoPredio> actoPredios
		 * = folioSeccionAuxiliar.getActoPrediosParaFolioSeccionAuxiliares(); for(
		 * ActoPredio ap : actoPredios ) { Prelacion p = ap.getActo().getPrelacion();
		 * if( p.getPrimerRegistro() ) { if( p.getStatus().getId() !=
		 * Constantes.Status.ENTREGADO_AL_USUARIO.getIdStatus() ) {
		 * folioSeccionAuxiliarVO.setEnProceso( true ); return folioSeccionAuxiliarVO; }
		 * } } }
		 */

		folioSeccionAuxiliarVO.setNoFolio(folioSeccionAuxiliar.getNoFolio());

		if (antecedente != null) {
			AntecedenteVO antecedenteVO = new AntecedenteVO();

			if (antecedente.getLibro() != null) {
				antecedenteVO.setLibro(antecedente.getLibro().getNombre());
				antecedenteVO.setBis(antecedente.getLibro().getLibroBis());
				antecedenteVO.setSeccion(antecedente.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
				antecedenteVO.setOficina(antecedente.getLibro().getSeccionesPorOficina().getOficina().getNombre());
				antecedenteVO.setAnio(antecedente.getLibro().getAnio());
				antecedenteVO.setVolumen(antecedente.getLibro().getVolumen());
			}

			antecedenteVO.setAnio(antecedente.getLibro().getAnio());
			antecedenteVO.setDocumento(antecedente.getDocumento());
			antecedenteVO.setDocumentoBis(antecedente.getDocumentoBis());

			folioSeccionAuxiliarVO.setAntecedente(antecedenteVO);
		}

		List<DevArt71VO> listaDevoluciones = devArt71Service.findDevoluciones(folioSeccionAuxiliar.getId(),
				PERSONAS_AUXILIARES.getTipoFolio());
		folioSeccionAuxiliarVO.setDevoluciones(listaDevoluciones);

		/*
		 * Sumatorio de actos por clasif acto
		 */

		List<ActoVO> clasifActos = caratulaDAO.findTotalActivosEInexitentesPorClasifActo(folioSeccionAuxiliar.getId(),
				PERSONAS_AUXILIARES);

		folioSeccionAuxiliarVO.setSumatoriaActosPorClasifActo(clasifActos);

		/*
		 * tramites
		 */

		folioSeccionAuxiliarVO
				.setTramites(caratulaDAO.findTramitesParaPredio(folioSeccionAuxiliar.getId(), PERSONAS_AUXILIARES));

		return folioSeccionAuxiliarVO;

	}

	@Transactional
	public PaseVO savePase(Long predioOrigenId, Long oficinaId, Integer folioReal,Acto acto) throws PasesException {

		Predio predio = predioRepository.findByNoFolio(folioReal);
		if (predio == null) {
			throw new PasesException("El folio destino no existe.");
		}

		Predio predioSegregado = predioRepository.findByNoFolioAndOficinaId(folioReal, oficinaId);
		if (predioSegregado == null) {
			throw new PasesException("La oficina no corresponde al folio destino.");
		}

		Predio predioOrigen = predioRepository.findOne(predioOrigenId);
		if (predioOrigen == null) {
			throw new PasesException("El folio origen no existe.");
		}

		List<PaseVO> listaPases = pasesService.findPases(predioOrigen.getId());
		for (PaseVO paseVO : listaPases) {
			if (paseVO.getNoFolio().equals(folioReal)) {
				throw new PasesException("El pase ya existe.");
			}
		}

		double totalSuperficieSegregada = pasesService.sumaSuperficie(listaPases);
		double superficie = pasesService.obtenSuperficieMetros(predioOrigen.getUnidadMedida().getId(),
				predioOrigen.getSuperficie());
		double superficieRestante = superficie - totalSuperficieSegregada;
		superficieRestante -= pasesService.obtenSuperficieMetros(predioSegregado.getUnidadMedida().getId(),
				predioSegregado.getSuperficie());

		// log.debug( "===> superficieRestante = "+superficieRestante );

		/*if (superficieRestante < 0 && acto==null) {
			throw new PasesException("La superficie no alcanza para ser segregada.");
		}*/

		return this.pasesService.savePase(predioOrigen, predioSegregado,acto);
	}
	
	


	public DevArt71VO saveDevolucion(Long predioId, Long personaJuridicaId, Long folioSeccionAuxiliarId,
			Integer consecutivo, String causaRechazo, String solicitud) {

		Prelacion prelacion = prelacionRepository.findAllByConsecutivo(consecutivo);
		if (prelacion == null) {
			return null;
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		Usuario usuario = usuarioRepository.findByUserName(userName);

		DevArt71 devArt71 = devArt71Service.saveDevolucion(prelacion, predioId, personaJuridicaId,
				folioSeccionAuxiliarId, usuario, causaRechazo, solicitud);

		DevArt71VO devolucionVO = new DevArt71VO();
		devolucionVO.setPrelacion(devArt71.getPrelacion().getConsecutivo());

		return devolucionVO;
	}

	public PredioMigrado getPredioMigrado(Long predioId) {
		Predio predio = new Predio();
		predio.setId(predioId);
		return predioMigradoRepository.findAllByPredio(predio);
	}

	public PredioMigrado getPredioMigradoPersonaJuridica(Long personaJuridicaId) {
		PersonaJuridica personaJuridica = new PersonaJuridica();
		personaJuridica.setId(personaJuridicaId);
		return predioMigradoRepository.findAllByPersonaJuridica(personaJuridica);
	}

	public PredioMigrado getPredioMigradoAuxiliar(Long auxiliarId) {
		FolioSeccionAuxiliar auxiliar = new FolioSeccionAuxiliar();
		auxiliar.setId(auxiliarId);
		return predioMigradoRepository.findAllByFolioSeccionAuxiliar(auxiliar);
	}

	public List<PjPersona> getSocios(Long personaJuridicaId) {
		return pjPersonaRepository.findSociosByPjId(personaJuridicaId);
	}

	public Usuario getUsuarioBloqueo(Long idPredio) {

		BloqueoFolio bloqueoFolio = bloqueoFolioService.getLastVersion(idPredio);

		Usuario usuario = usuarioRepository.findOne(bloqueoFolio.getUsuario().getId());

		return usuario;
	}

	public Usuario getUsuarioBloqueoMueble(Long idMueble) {

		BloqueoFolio bloqueoFolio = bloqueoFolioService.getLastVersionMueble(idMueble);

		Usuario usuario = usuarioRepository.findOne(bloqueoFolio.getUsuario().getId());

		return usuario;
	}

	public Usuario getUsuarioBloqueoSeccionAuxiliar(Long idAuxiliar) {

		BloqueoFolio bloqueoFolio = bloqueoFolioService.getLastVersionSeccionAuxiliar(idAuxiliar);

		Usuario usuario = usuarioRepository.findOne(bloqueoFolio.getUsuario().getId());

		return usuario;
	}

	public Usuario getUsuarioBloqueoPersonaJuridica(Long idPersonaJuridica) {

		BloqueoFolio bloqueoFolio = bloqueoFolioService.getLastVersionPersonaJuridica(idPersonaJuridica);

		Usuario usuario = usuarioRepository.findOne(bloqueoFolio.getUsuario().getId());

		return usuario;
	}

	@Transactional(readOnly = true)
	public List<TramiteVO> findAllTramitesInscritos(Integer predioId, Integer tipoFolioId) {
		if (tipoFolioId == PERSONAS_JURIDICAS.getTipoFolio()) {
			// return caratulaDAO.findTramitesInscritos(predioId, statusActoId,
			// clasifActoId,PERSONAS_JURIDICAS).stream().filter( x->
			// !x.getTipoActo().equals(0L)).collect(Collectors.toList());
			return null;
		} else if (tipoFolioId == PERSONAS_AUXILIARES.getTipoFolio()) {
			// return caratulaDAO.findTramitesInscritos(predioId, statusActoId,
			// clasifActoId,PERSONAS_AUXILIARES).stream().filter( x->
			// !x.getTipoActo().equals(0L)).collect(Collectors.toList());
			return null;
		} else if (tipoFolioId == MUEBLE.getTipoFolio()) {
			// return caratulaDAO.findTramitesInscritos(predioId, statusActoId,
			// clasifActoId,MUEBLE).stream().filter( x->
			// !x.getTipoActo().equals(0L)).collect(Collectors.toList());
			return null;
		} else if (tipoFolioId == PREDIO.getTipoFolio()) {
			return caratulaDAO.findTramitesInscritos(predioId, null, null, PREDIO).stream()
					.filter(x -> !x.getTipoActo().equals(0L)).collect(Collectors.toList());
		} else {
			return null;
		}
	}
	
	public List<TramiteVO> findAllTramitesTitulares(Long predioId){
		Set<Acto> actos  = actoPredioRepository.findActosTitulares(predioId);
		List<TramiteVO> result =  new ArrayList<>(); 
		actos.forEach(x->{
			TramiteVO vo =  new TramiteVO();
			vo.setActoId(x.getId());
			if(x.getTipoActo()!=null) {
				vo.setClasificacionId(x.getTipoActo().getClasifActo().getId());
				vo.setTramite(x.getTipoActo().getNombre());
			}
			vo.setStatusActo(x.getStatusActo().getNombre());
			vo.setTipoEntrada(2L);
			if(x.getPrelacion()!=null)
			 vo.setConsecutivo(Long.parseLong(x.getPrelacion().getConsecutivo().toString()));
			
			
			vo.setFechaActo(x.getFechaRegistro());
			result.add(vo);
		});
		return result;
	}

	public byte[] genBoletaImpresionFolios(Long prelacionId,boolean marcaAgua) {
		byte[] boleta = null;

		try {
			// boleta =
			// pdfService.appendPDF(boletaRegistralService.generPdfBoletaRegistral(prelacionId),
			// generCertificadoConAviso(prelacionId));
			boleta = pdfService.appendPDF(getCaratulaImpresionFolios(prelacionId,marcaAgua),
					getHojaCertificaImpresionFolios(prelacionId,marcaAgua));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return boleta;
	}

	public Predio validaFolioPredio(Integer noFolio, Integer noFolioAnterior, Integer auxiliar,Long oficinaId){
		Predio predio=null;
		Oficina oficina = oficinaRepository.findById(oficinaId);
		
		if(noFolio!=0){
			log.debug("Buscando folio SIREH: {}, tipo: {} y oficina {}",noFolio,oficinaId);
			predio = predioService.findByNoFolioAndOficina(noFolio, oficina);			
		} else if (auxiliar!=0){			
			log.debug ("Buscar por FOLIO REAL ANTERIOR:"+noFolioAnterior+" AUXILIAR:"+auxiliar);			
			predio = predioService.findPredioByNoFolioRealAndAuxiliarAndOficina(noFolioAnterior, auxiliar,oficina);
		} else {
			log.debug ("Buscar por FOLIO REAL ANTERIOR:"+noFolioAnterior);			
			predio = predioService.findByNoFolioRealAndOficina(noFolioAnterior, oficina);
		}

		return predio;
	}

	public byte[] genPreviewImpresionFoliosPropiedad(Long predioId,boolean marcaAgua) {
		byte[] boleta = null;

		try {
			boleta = getPreviewImpresionFolios(predioId,marcaAgua);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boleta;
	}

	private byte[] getCaratulaImpresionFolios(Long prelacionId,boolean marcaAgua) throws JRException {

		CaratulaImpresionVO cartatulaImpresionVO = new CaratulaImpresionVO();

		List<CaratulaVO> caratulaList = new ArrayList<CaratulaVO>();
		List<CaratulaImpresionVO> cartatulaImpresionList = new ArrayList<CaratulaImpresionVO>();
		Long[] statusActos = { Constantes.StatusActo.ACTIVO.getIdStatusActo(),
				Constantes.StatusActo.INVALIDO.getIdStatusActo() };
		Predio predio = null;

		List<PrelacionPredio> prelacionesPredio = predioService.findByPrelacionId(prelacionId);

		for (PrelacionPredio prelacionPredio : prelacionesPredio) {
			cartatulaImpresionVO = new CaratulaImpresionVO();
			cartatulaImpresionVO.setEntrada(""+prelacionPredio.getPrelacion().getConsecutivo());
			cartatulaImpresionVO.setSubindice(""+prelacionPredio.getPrelacion().getSubindice());
			cartatulaImpresionVO.setAnio(""+prelacionPredio.getPrelacion().getAnio());
			cartatulaImpresionVO.setNoFolio(""+prelacionPredio.getPredio().getNoFolio());
			if(prelacionPredio.getPredio().getNumeroFolioReal()!=null)
				cartatulaImpresionVO.setNumeroFolioReal(""+prelacionPredio.getPredio().getNumeroFolioReal());
			if(prelacionPredio.getPredio().getAuxiliar()!=null)
				cartatulaImpresionVO.setAuxiliar(""+prelacionPredio.getPredio().getAuxiliar());
			
			cartatulaImpresionVO.setOficina(""+prelacionPredio.getPredio().getOficina().getNombre().toUpperCase());
			cartatulaImpresionList.add(cartatulaImpresionVO);
			// solo si es predio
			if (Constantes.ETipoFolio.PREDIO.getTipoFolio().equals(prelacionPredio.getTipoFolio().getId().intValue())) {
				PredioVO predioVO = getCaratulaPredio(prelacionPredio.getPredio().getNoFolio(),
						prelacionPredio.getPredio().getOficina().getId());
				predio = predioService.findOne(predioVO.getPredioId());
				CaratulaVO caratula = new CaratulaVO(predioVO, predio);
				
				caratula.setValoresActos(formasService.obtenerValoresActos(predio, statusActos));
				caratulaList.add(caratula);
			}
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JRDataSource ds2 = new JRBeanCollectionDataSource(cartatulaImpresionList);		
		Map<String, Object> parameterMap2 = new HashMap<String, Object>();
		parameterMap2.put("datasource", ds2);
		parameterMap2.put("path", Constantes.IMG_PATH);
		InputStream jasperStream2 = this.getClass().getResourceAsStream("/reports/caratulaImpresionFolios.jasper");
		JasperReport jasperReport2 = (JasperReport) JRLoader.loadObject(jasperStream2);
		JasperPrint jasperPrint2 = JasperFillManager.fillReport(jasperReport2, parameterMap2, ds2);

		byte[] caratula = JasperExportManager.exportReportToPdf(jasperPrint2);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JRDataSource ds = new JRBeanCollectionDataSource(caratulaList);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+prelacionId;
		parameterMap.put("QR_BASE_URI", qR);
		if(marcaAgua)
			parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfImpresionActos.jasper");
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
		byte[] export = JasperExportManager.exportReportToPdf(jasperPrint);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		Set<Acto> actos = actoPredioService.findAllActosbyPredioAndStatusId(predio.getId(), statusActos);
		for (Acto a : actos) {
			if (a.getId_aaj() != null) {			
				try {
					String folder = parametroRepository.findByCve(Constantes.RUTA_ASIENTOS_CVE).getValor();
					Path path = Paths.get(folder + "/MIG_" + a.getIdAsiento() + "_ASIENTO.pdf");
					if(Files.exists(path)) {
					 byte[] media = Files.readAllBytes(path);
					 export = pdfService.appendPDF(export, media);
					}else {
						log.debug("PRELACION "+prelacionId+" ASIENTO NO EXISTE: "+ path.toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			export = pdfService.appendPDF(caratula,export);
			return export;	
		}
		catch(Exception e){
			e.printStackTrace();
			return null;	
		}
	}

	private byte[] getPreviewImpresionFolios(Long predioId,boolean marcaAgua) throws JRException {

		List<CaratulaVO> caratulaList = new ArrayList<CaratulaVO>();
		
		Long[] statusActos = { Constantes.StatusActo.ACTIVO.getIdStatusActo(),
				Constantes.StatusActo.INVALIDO.getIdStatusActo() };
		Predio predio = null;

		predio = predioService.findOne(predioId);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// solo si es predio
		//if (Constantes.ETipoFolio.PREDIO.getTipoFolio().equals(predio.getTipoFolio().getId().intValue())) {
			PredioVO predioVO = getCaratulaPredio(predio.getNoFolio(),
					predio.getOficina().getId());
			
			CaratulaVO caratula = new CaratulaVO(predioVO, predio);
			
			caratula.setValoresActos(formasService.obtenerValoresActos(predio, statusActos));
			caratulaList.add(caratula);
		//}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JRDataSource ds = new JRBeanCollectionDataSource(caratulaList);
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/0";
		parameterMap.put("QR_BASE_URI", qR);
		if(marcaAgua)
			parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/pdfImpresionActos.jasper");
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
		byte[] export = JasperExportManager.exportReportToPdf(jasperPrint);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		Set<Acto> actos = actoPredioService.findAllActosbyPredioAndStatusId(predio.getId(), statusActos);
		for (Acto a : actos) {
			if (a.getId_aaj() != null) {			
				try {
					String folder = parametroRepository.findByCve(Constantes.RUTA_ASIENTOS_CVE).getValor();
					Path path = Paths.get(folder + "/MIG_" + a.getIdAsiento() + "_ASIENTO.pdf");
					if(Files.exists(path)) {
					 byte[] media = Files.readAllBytes(path);
					 export = pdfService.appendPDF(export, media);
					}else {
						log.debug("PREDIO ID "+predioId+" ASIENTO NO EXISTE: "+ path.toString());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}	
		
		return export;
		
	}

	private byte[] getHojaCertificaImpresionFolios(Long prelacionId,boolean marcaAgua) throws JRException {
				
		Prelacion pre = prelacionService.findOne(prelacionId);
		CertificadoLibertadOGravamenVO clg = null;
		try {
			// IHM getCertificado()
			clg = getHojaCertificaImpresionFolios(pre);
			
		} catch (RequerimientoException ex) {
			log.debug("Excepcion de al generar Impresion de Folios " + ex.getMessage());
			return null;
		}		
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		certificados.add(clg);

		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);			
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+prelacionId;
		parameterMap.put("QR_BASE_URI", qR);
		
		if(marcaAgua)
			parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		
		InputStream jasperStream = this.getClass().getResourceAsStream(
				"/reports/certificaImpreFolio.jasper");
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
		
		return JasperExportManager.exportReportToPdf(jasperPrint);	
	}

	@Transactional(noRollbackFor = RequerimientoException.class)
	public CertificadoLibertadOGravamenVO getHojaCertificaImpresionFolios(Prelacion prelacion)
			throws RequerimientoException {
		CertificadoLibertadOGravamenVO clg = new CertificadoLibertadOGravamenVO();
		Predio predio = prelacion.getPrelacionPrediosParaPrelacions().iterator().next().getPredio();
		log.info("getHojaCertificaImpresionFolios");		
		clg.setNoFolio(predio.getNoFolio());
		clg.setLugar(prelacion.getOficina().getNombre()+" "+prelacion.getOficina().getEstado().getNombre());
		clg.setFechaGenerado(prelacion.getActosParaPrelacions().iterator().next().getFechaRegistro());
		clg.setUsuarioRegistrador(boletaRegistralService.buildUsuario(prelacion.getUsuarioAnalista()));
		clg.setUsuarioCoordinador(boletaRegistralService.buildUsuario(prelacion.getUsuarioCoordinador()));
		Acto impresionDeFolios = null;
		String certifica="";
		for(Acto a : prelacion.getActosParaPrelacions()){
			if(a.getTipoActo().getId()==215L){
				impresionDeFolios=a;
				break;
			}
		}
		if(impresionDeFolios!=null){
			ActoPredio ap = actoPredioRepository.findActoPredioByLastVersion(impresionDeFolios.getId());
			for(ActoPredioCampo apc:ap.getActoPredioCamposParaActoPredios()){
				Integer moduloCampo = apc.getModuloCampo().getId().intValue();
				switch(moduloCampo){
					case 20856:
						certifica = apc.getValor().toUpperCase().trim();
					break;
				}
			}

		}
		clg.setCertifica(certifica);
		
		

		// FIRMA ELECTRONICA REGISTRADOR
		clg.setFirmaRegistrador(boletaRegistralService.buildStringFirmaActos(prelacion.getId()));
		clg.setOficina(prelacion.getOficina().getNombre());
		// FIRMA ELECTRONICA COORDINADOR
		String firmaCoord = null;
		String firmaCoord2 = "-SIN FIRMA -";
		if (prelacion.getFirma() != null)
			firmaCoord = prelacion.getFirma();
					
		clg.setFirmaCoordinador(firmaCoord);
		
		return clg;
	}
	
	
	public FolioRealVO findFolioByNoFolio(Integer noFolio,Integer tipoFolio,boolean whitActos) throws Exception {
		 FolioRealVO  folioReal = new FolioRealVO();
		 Long[] status = {Constantes.StatusActo.ACTIVO.getIdStatusActo(),Constantes.StatusActo.INVALIDO.getIdStatusActo()};
		 if(Constantes.ETipoFolio.PREDIO.getTipoFolio().equals(tipoFolio)) {
			 folioReal.setPredio(predioRepository.findByNoFolio(noFolio));
			 if(folioReal.getPredio()==null)
				 throw new Exception("El predio no existe");
			 if(whitActos)
				  folioReal.setActos(actoPredioRepository.findActosPredioByPredioAndStatusQuery(folioReal.getPredio().getId(), status));
		 }else if (Constantes.ETipoFolio.PREDIO.getTipoFolio().equals(tipoFolio)) {
			 folioReal.setPersonaJuridica(personaJuridicaRepository.findByNoFolio(noFolio));
			 if(folioReal.getPersonaJuridica()==null)
				 throw new Exception("La persona moral no existe");
		 }
		 
		 return folioReal;
	}
	
	
	public List<AntecedenteVO> findAllAntecedentes(Long predioId, Integer tipoFolioId){
		List<AntecedenteVO> antecedentesVO = new ArrayList<AntecedenteVO>();
		List<Antecedente> antecedentes = new ArrayList<Antecedente>();
		if(tipoFolioId == Constantes.ETipoFolio.PREDIO.getTipoFolio()) {
			Predio predio  = predioRepository.findById(predioId);

			if (predio.getPredioAntesParaPredios() != null && predio.getPredioAntesParaPredios().size() > 0) {
				predio.getPredioAntesParaPredios().forEach(x -> {
					antecedentes.add( x.getAntecedente());
				});
			}

			if(antecedentes != null) {
				for(Antecedente ant:antecedentes) {
					if (ant != null) {
						AntecedenteVO antecedenteVO = new AntecedenteVO();
						if (ant.getLibro() != null) {
							antecedenteVO.setLibro(ant.getLibro().getNumLibro().toString());
							antecedenteVO.setBis(ant.getLibro().getLibroBis());
							antecedenteVO.setSeccion(ant.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
							antecedenteVO.setOficina(ant.getLibro().getSeccionesPorOficina().getOficina().getNombre());
							antecedenteVO.setAnio(ant.getLibro().getAnio());
							antecedenteVO.setVolumen(ant.getLibro().getVolumen());
						}						
						antecedenteVO.setDocumento(ant.getDocumento());
						antecedenteVO.setDocumentoBis(ant.getDocumentoBis());
						antecedenteVO.setId(ant.getId());
						
						antecedentesVO.add(antecedenteVO);

					}else if (predio.getProcedeDeFolio() != null) {

						AntecedenteVO antecedenteVO = new AntecedenteVO();
						antecedenteVO.setProcedeFolio(predio.getProcedeDeFolio());
						antecedentesVO.add(antecedenteVO);
					}
				}
			}
		} else {
			PersonaJuridica pj = personaJuridicaRepository.findOne(predioId);
			if (pj.getPjAnteParaPersonasJuridicas() != null	&& pj.getPjAnteParaPersonasJuridicas().size() > 0) {
				pj.getPjAnteParaPersonasJuridicas().forEach(x -> {
					antecedentes.add( x.getAntecedente());
				});
			}
			
			if(antecedentes != null) {
				for(Antecedente ant:antecedentes) {
					AntecedenteVO antecedenteVO = new AntecedenteVO();
					if (ant.getLibro() != null) {
						antecedenteVO.setLibro(ant.getLibro().getNumLibro().toString());
						antecedenteVO.setBis(ant.getLibro().getLibroBis());
						antecedenteVO.setSeccion(ant.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
						antecedenteVO.setOficina(ant.getLibro().getSeccionesPorOficina().getOficina().getNombre());
						antecedenteVO.setAnio(ant.getLibro().getAnio());
						antecedenteVO.setVolumen(ant.getLibro().getVolumen());
					}
					antecedenteVO.setAnio(ant.getLibro().getAnio());
					antecedenteVO.setDocumento(ant.getDocumento());
					antecedenteVO.setDocumentoBis(ant.getDocumentoBis());
					antecedenteVO.setId(ant.getId());
					
					antecedentesVO.add(antecedenteVO);
				}
			}
		}

		return antecedentesVO;
	}
	
	public Boolean marcarFolioCalidad(Long predioId) {
		Boolean resp = false;
		Predio predio = null;
		
		predio=predioRepository.getOne(predioId);
		if(predio!=null) {
			predio.setFolioCalidad(true);
			predioRepository.save(predio);			
			resp=true;
		}
		return resp;		
	}
	
	public Boolean quitarFolioCalidad(Long predioId) {
		Boolean resp = false;
		Predio predio = null;
		
		predio=predioRepository.getOne(predioId);
		if(predio!=null) {
			predio.setFolioCalidad(false);
			predioRepository.save(predio);			
			resp=true;
		}
		return resp;		
	}
	
	


}
