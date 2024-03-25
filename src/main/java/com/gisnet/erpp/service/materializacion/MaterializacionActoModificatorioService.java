package com.gisnet.erpp.service.materializacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.domain.ActoRel;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioBitacora;
import com.gisnet.erpp.domain.PredioBitacoraColindancia;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.PredioRel;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.UnidadMedida;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioMontoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRelRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.ColindanciaRepository;
import com.gisnet.erpp.repository.FoliosFracCondRepository;
import com.gisnet.erpp.repository.OrientacionRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.PredioRelRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.repository.UnidadMedidaRepository;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.MaterializacionActoService;
import com.gisnet.erpp.service.PredioBitacoraService;
import com.gisnet.erpp.service.PredioPersonaService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.util.Constantes;

@Service
public class MaterializacionActoModificatorioService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private long MODULO_ID_ADQUIRIENTES_FUSION = 764L;
	private final long ORIENTACION_CAMPO_ID = 277;
	private final long COLINDANCIA_CAMPO_ID = 238;
	private final long UNIDAD_MEDIDA_CAMPO_ID = 5060;
	private final long SUPERFICIE_CAMPO_ID = 5061;
	private final long SUPERFICIEM2_CAMPO_ID = 5062;
	private final long TIPO_MODIFICACION_REGIMEN_CAMPO_ID = 38;
	private final long EXTINGUIR_FOLIO_CAMPO_ID = 697;
	private final long FOLIO_MATRIZ_REGIMEN_CAMPO_ID = 751;
	@Autowired
	TipoEntradaRepository tipoEntradaRepository;

	@Autowired
	StatusActoRepository statusActoRepository;

	@Autowired
	ActoService actoService;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	ActoUtilService actoUtilService;

	@Autowired
	PredioPersonaService predioPersonaService;

	@Autowired
	PredioRelRepository predioRelRepository;

	@Autowired
	PredioRepository predioRepository;

	@Autowired
	ActoRelRepository actoRelRepository;

	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	PredioPersonaRepository predioPersonaRepository;

	@Autowired
	TipoFolioRepository tipoFolioRepository;

	@Autowired
	TipoRelPersonaRepository tipoRelPersonaRepository;

	@Autowired
	PredioService predioService;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	PrelacionService prelacionService;

	@Autowired
	ActoPredioMontoRepository actoPredioMontoRepository;

	@Autowired
	ActoFirmaRepository actoFirmaRepository;

	@Autowired
	OrientacionRepository orientacionRepository;

	@Autowired
	UnidadMedidaRepository unidadMedidaRepository;

	@Autowired
	PredioBitacoraService predioBitacoraService;

	@Autowired
	ColindanciaRepository colindanciaRepository;
	@Autowired
	MaterializacionActoService materializacionActoService;
	
	
	@Autowired
	CampoValoresRepository campoValoresRepository; 
	
	@Transactional
	public void materializaModfificatorioInmueble(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no se le asociado un predio");
		}

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		List<HashMap<Integer, FraccionVO>> entradaSalida = actoUtilService
				.parseFraccionVOEntradaYSalida(actoPredioCampos);

		HashMap<Integer, FraccionVO> entrada = entradaSalida.get(0);
		HashMap<Integer, FraccionVO> salida = entradaSalida.get(1);

		HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService.parseActos(actoPredioCampos);

		// busca los propietarios para el primer predio de entrada
		//List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(entrada.get(1).predioId,
		//		false);
		
		HashMap<Integer, PredioPersona> adquitientes = actoUtilService.parseAdquirientes(actoPredioCampos,
				acto, MODULO_ID_ADQUIRIENTES_FUSION);

		saveEntradaSalida(acto, actoPredio, entrada, salida, new ArrayList<PredioPersona>(adquitientes.values()), actosSeleccionados);
				
		
	}
	
	@Transactional 
	public void deMaterializaModificacionRegimen(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		Predio predio =  actoPredio.getPredio();
		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		String tipoModificacion = null;
		Boolean extinguirFolio =  false;
		Integer folioMatrizRegimen =  null;
		for(ActoPredioCampo apc : actoPredioCampos) {
			long campoId = apc.getModuloCampo().getCampo().getId();
			if(campoId == TIPO_MODIFICACION_REGIMEN_CAMPO_ID && apc.getValor() !=null && !apc.getValor().isEmpty()){
				CampoValores cv = campoValoresRepository.findOne(Long.parseLong(apc.getValor()));
				tipoModificacion = (cv!=null) ?  cv.getNombre() : null;
			}
			if(campoId == EXTINGUIR_FOLIO_CAMPO_ID && apc.getValor() !=null && !apc.getValor().isEmpty()) {
				extinguirFolio = Boolean.parseBoolean(apc.getValor().toLowerCase());
			}
			
			if(campoId == FOLIO_MATRIZ_REGIMEN_CAMPO_ID && apc.getValor() !=null && !apc.getValor().trim().isEmpty()) {
				folioMatrizRegimen =  Integer.parseInt(apc.getValor().trim());
			}
			
		}
		
		if(tipoModificacion.toUpperCase().equals("EXTINGUIR FOLIOS") && extinguirFolio) 
		{
			   predio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			   predioRepository.save(predio);
		}
		
		
		if(tipoModificacion.toUpperCase().equals("AGREGAR FOLIOS") && folioMatrizRegimen!=null) 
		{
			predioRelRepository.deleteByActoId(acto.getId());
			predio.setProcedeDeFolio(null);
			predioRepository.save(predio);
		}
		
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
	}
	
	@Transactional 
	public void materializaModificacionRegimen(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		Predio predio =  actoPredio.getPredio();
		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		String tipoModificacion = null;
		Boolean extinguirFolio =  false;
		Integer folioMatrizRegimen =  null;
		for(ActoPredioCampo apc : actoPredioCampos) {
			long campoId = apc.getModuloCampo().getCampo().getId();
			if(campoId == TIPO_MODIFICACION_REGIMEN_CAMPO_ID && apc.getValor() !=null && !apc.getValor().isEmpty()){
				CampoValores cv = campoValoresRepository.findOne(Long.parseLong(apc.getValor()));
				tipoModificacion = (cv!=null) ?  cv.getNombre() : null;
			}
			if(campoId == EXTINGUIR_FOLIO_CAMPO_ID && apc.getValor() !=null && !apc.getValor().isEmpty()) {
				extinguirFolio = Boolean.parseBoolean(apc.getValor().toLowerCase());
			}
			if(campoId == FOLIO_MATRIZ_REGIMEN_CAMPO_ID && apc.getValor() !=null && !apc.getValor().trim().isEmpty()) {
				folioMatrizRegimen =  Integer.parseInt(apc.getValor().trim());
			}
			
		}
		
		if(tipoModificacion.toUpperCase().equals("EXTINGUIR FOLIOS") && extinguirFolio) 
		{
			   predio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
			   predioRepository.save(predio);
		}
		
		if(tipoModificacion.toUpperCase().equals("AGREGAR FOLIOS") && folioMatrizRegimen!=null) 
		{
			Predio predioRegimen =  predioRepository.findByNoFolio(folioMatrizRegimen);
			if(predioRegimen!=null) 
			{
				predio.setProcedeDeFolio(predioRegimen.getNoFolio().toString());
				predioRepository.save(predio);
				List<Predio> entrada = new ArrayList<>();
				entrada.add(predioRegimen);
				List<Predio> salida = new ArrayList<>();
				salida.add(predio);
				savePredioRel(entrada, salida, acto);
			}
		}
		
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		actoRepository.save(acto);
		
		
	}

	@Transactional
	private void saveEntradaSalida(Acto acto, ActoPredio actoPredio, HashMap<Integer, FraccionVO> entrada,
			HashMap<Integer, FraccionVO> salida, List<PredioPersona> predioPersonas,
			HashMap<Integer, ActoPorcentajeVO> actosSeleccionados) {
		StringBuilder procedeDeFolio = new StringBuilder();

		List<Predio> prediosEntrada = this.saveEntradas(entrada);

		for (Predio predio : prediosEntrada) {
			if (procedeDeFolio.length() > 0) {
				procedeDeFolio.append(",");
			}
			procedeDeFolio.append(predio.getNoFolio());
		}

		List<Predio> prediosSalida = saveSalidas(acto, actoPredio, salida, predioPersonas, procedeDeFolio,
				actosSeleccionados);

		this.savePredioRel(prediosEntrada, prediosSalida,acto);

		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		actoRepository.save(acto);

		// ACTO PREDIO ORIGEN ACTUALIZA A TIPO ENTRADA = 2
		actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
		actoPredioRepository.save(actoPredio);
	}

	private void savePredioRel(List<Predio> prediosEntrada, List<Predio> prediosSalida) {
		for (Predio predioEntrada : prediosEntrada) {
			for (Predio predioSalida : prediosSalida) {
				PredioRel predioRel = new PredioRel();
				predioRel.setPredio(predioEntrada);
				predioRel.setPredioSig(predioSalida);
				predioRelRepository.save(predioRel);
			}
		}
	}
	
	private void savePredioRel(List<Predio> prediosEntrada, List<Predio> prediosSalida,Acto acto) {
		for (Predio predioEntrada : prediosEntrada) {
			for (Predio predioSalida : prediosSalida) {
				PredioRel predioRel = new PredioRel();
				predioRel.setPredio(predioEntrada);
				predioRel.setPredioSig(predioSalida);
				predioRel.setActo(acto);
				predioRelRepository.save(predioRel);
			}
		}
	}


	@Transactional
	private List<Predio> saveEntradas(HashMap<Integer, FraccionVO> entrada) {
		List<Predio> prediosEntrada = new ArrayList<Predio>();

		entrada.forEach((index, fraccionVO) -> {
			if (fraccionVO.predioId > 0) {
				Predio predio = predioRepository.findOne(fraccionVO.predioId);
				/*predio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
				predioRepository.save(predio);*/
			
				prediosEntrada.add(predio);
			}
		});

		return prediosEntrada;
	}

	@Transactional
	public void deMaterializaModfificatorioInmueble(Acto acto) {
		log.debug("Iniciando dematerializacion de acto modificatorio del inmueble del acto = {}", acto);

		log.debug("cambiando a temporal el acto = {}", acto);
		//SOLO ACTOS NO CLONADOS DESMATERIALIZAN SOLO ACTOS FUENTES (FUSION,SUBDIVICION)
		if (acto.getClon() == null || !acto.getClon()) {

			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
			actoRepository.save(acto);

			List<Predio> prediosSalida = new ArrayList<Predio>();

			// borra relaciones acto y actos creados
			List<ActoRel> relaciones = actoRelRepository.findByActoId(acto.getId());
			for (ActoRel actoRel : relaciones) {
				List<ActoPredio> actosPredioSalida = actoPredioRepository
						.findAllByActoIdAndTipoEntrada(actoRel.getActoSig().getId(), Constantes.TipoEntrada.SALIDA);
				for (ActoPredio actoPredioSalida : actosPredioSalida) {
					Predio predioSalida = actoPredioSalida.getPredio();

					predioPersonaRepository.deleteByActoPredioId(actoPredioSalida.getId());

					log.debug("cambiando a TEMPORAL el predio = {} ", predioSalida);
					if (acto.getTipoActo().getId().equals( Constantes.REGIMEN_PROPIEDAD_CONDOMINIO ) ||
							acto.getTipoActo().getId() == Constantes.FRACCIONAMIENTO_TIPO_ACTO_ID) {
						predioSalida.setStatusActo(
								statusActoRepository.findOne(Constantes.StatusActo.EN_PROCESO.getIdStatusActo()));
					}else {
						predioSalida.setStatusActo(
								statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
					}
					
					predioRepository.save(predioSalida);

					prediosSalida.add(predioSalida);
				}
				actoRelRepository.delete(actoRel);
				actoService.deleteActo(actoRel.getActoSig().getId());
			}

			// ACTO SOURCE UPDATE TIPO ENTRADA
			List<ActoPredio> actosPredioSourceSalida = actoPredioRepository.findAllByActoIdAndTipoEntrada(acto.getId(),
					Constantes.TipoEntrada.SALIDA);
			for (ActoPredio ap : actosPredioSourceSalida) {
				ap.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
				actoPredioRepository.save(ap);
			}

			// regresa a ACTIVOS los predios fuente
			List<Predio> prediosSource = predioRepository
					.findDistinctByPredioRelesParaPrediosPredioSigIn(prediosSalida);

			for (Predio predioSource : prediosSource) {
				log.debug("cambiando a ACTIVO predio fuente = {}", predioSource);
				predioSource
						.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
				predioRepository.save(predioSource);
			}

			// ELIMINA ACTOS CLONADOS AL FOLIO RESULTANTE (HIPOTECAS, EMBARGOS, ETC.)
			for (Predio psalida : prediosSalida) {
				List<ActoPredio> actosPredioSalida = actoPredioRepository.findAllByPredioAndPrelacion(psalida.getId(),
						acto.getPrelacion().getId());
				for (ActoPredio ap : actosPredioSalida) {
					this.actoService.deleteActo(ap.getActo().getId());
				}
			}

			// borra relacio predio
			predioRelRepository.deleteByActoId(acto.getId());
			predioRelRepository.deleteByPredioSigIn(prediosSalida);
			materializacionActoService.deHeredarActos(acto);
		}
	}

	@Transactional
	public void deMaterializarModificatoriosSP(Acto acto) {
		this.actoRepository.deMaterializarModificatorio(acto.getId());
	}
	
	@Transactional
	public void materializarFraccionamiento(Acto acto) {
		
		/*ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no se le asociado un predio");
		}

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, FraccionVO> entrada = new HashMap<Integer, FraccionVO>();
		HashMap<Integer, FraccionVO> salida = new HashMap<Integer, FraccionVO>();
		HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService.parseActos(actoPredioCampos);
		
		
		Set<FoliosFracCond> fracciones = acto.getFoliosFracCondParaActos();
		entrada.put(1, new FraccionVO(actoPredio.getPredio().getId()));

		Iterator<FoliosFracCond> iter = fracciones.iterator();
		int index = 1;
		while (iter.hasNext()) {
			FoliosFracCond fraccion = iter.next();
			if (fraccion.getProcedente()) {
				salida.put(index, new FraccionVO(fraccion.getPredio().getId()));
				index++;
			}
		}

		List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(entrada.get(1).predioId,
				false);

		saveEntradaSalida(acto, actoPredio, entrada, salida, predioPersonas, actosSeleccionados);

		*/
		
		this.actoRepository.materializaModificatorio(acto.getId());
		
		
	}

	@Transactional
	private List<Predio> saveSalidas(Acto acto, ActoPredio actoPredio, HashMap<Integer, FraccionVO> salida,
			List<PredioPersona> predioPersonas, StringBuilder procedeDeFolio,
			HashMap<Integer, ActoPorcentajeVO> actosSeleccionados) {
		List<Predio> prediosSalida = new ArrayList<Predio>();

		salida.forEach((index, fraccionVO) -> {
			if (fraccionVO.predioId > 0) {
				Acto newActo = null;

				newActo = new Acto();
				actoUtilService.copyActo(acto, newActo);
				newActo.setClon(true);
				actoRepository.save(newActo);
				actoUtilService.copyActoReferences(acto, newActo, true);
				log.debug("Predios salida generando Acto = {}", newActo);

				ActoRel actoRel = new ActoRel();
				actoRel.setActo(acto);
				actoRel.setActoAnt(acto);
				actoRel.setActoSig(newActo);

				actoRelRepository.save(actoRel);

				Predio newPredio = predioRepository.findOne(fraccionVO.predioId);

				newPredio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
				newPredio.setCaratulaActualizada(true);
				newPredio.setBloqueado(false);
				newPredio.setProcedeDeFolio(procedeDeFolio.length() > 0 ? procedeDeFolio.toString() : null);
				if (newPredio.getSuperficieM2() == null) {
					newPredio.setSuperficie(String.valueOf(fraccionVO.superficie));
					newPredio.setSuperficieM2(fraccionVO.superficie);
				}
				predioRepository.save(newPredio);

				if (actosSeleccionados != null && actosSeleccionados.size() > 0 && (newPredio.getHeredaActo()!=null && newPredio.getHeredaActo())) {
					this.saveActosSeleccionados(actosSeleccionados, acto.getPrelacion(), newPredio,acto);
				}

				prediosSalida.add(newPredio);

				// Crea nuevo acto_predio
				ActoPredio newActoPredio = new ActoPredio();
				actoUtilService.copyActoPredio(actoPredio, newActoPredio);
				newActoPredio.setId(null);
				newActoPredio.setActo(newActo);
				newActoPredio.setPredio(newPredio);
				newActoPredio.setVersion(1);
				newActoPredio
						.setTipoFolio(tipoFolioRepository.findOne((long) Constantes.ETipoFolio.PREDIO.getTipoFolio()));
				newActoPredio.setTipoEntrada(
						tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));

				actoPredioRepository.save(newActoPredio);
				actoUtilService.copyActoPredioReferences(actoPredio, newActoPredio, null, true);

				for (PredioPersona predioPersona : predioPersonas) {
					PredioPersona newPredioPersona = new PredioPersona();
					BeanUtils.copyProperties(predioPersona, newPredioPersona);
					if (newPredioPersona.getTipoRelPersona() == null) {
						newPredioPersona.setTipoRelPersona(tipoRelPersonaRepository
								.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));
					}
					newPredioPersona.setId(null);
					newPredioPersona.setActoPredio(newActoPredio);
					newPredioPersona.setPredioPersonasParaPredioPropDesignans(null);
					predioPersonaRepository.save(newPredioPersona);
				}

				try {
					if(newPredio.getNoFolio() == null)
						predioService.createFolioPredio(newPredio.getId(), acto.getPrelacion().getOficina().getId(), false);
				} catch (Exception e) {
					throw new IllegalArgumentException(e.getMessage());
				}

			}
		});

		return prediosSalida;
	}

	@Transactional
	public void materializaFusion(Long prelacionId, List<ActoPredio> actos) {
		log.debug("materializaGrupoActos de {} ", prelacionId);

		Prelacion prelacion = prelacionService.findOne(prelacionId);

		// obtener campos dinamicos
		Set<ActoPredioCampo> actoPredioCampos = actos.get(0).getActoPredioCamposParaActoPredios();

		List<HashMap<Integer, FraccionVO>> entradaSalida = actoUtilService
				.parseFraccionVOEntradaYSalida(actoPredioCampos);

		HashMap<Integer, FraccionVO> entrada = entradaSalida.get(0);
		HashMap<Integer, FraccionVO> salida = entradaSalida.get(1);
		HashMap<Integer, PredioPersona> adquitientes = actoUtilService.parseAdquirientes(actoPredioCampos,
				actos.get(0).getActo(), MODULO_ID_ADQUIRIENTES_FUSION);
		HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService.parseActosFusion(actoPredioCampos);

		StringBuilder procedeDeFolio = new StringBuilder();

		List<Predio> prediosEntrada = this.saveEntradas(entrada);
		
		for (Predio predio : prediosEntrada) {
			if (procedeDeFolio.length() > 0) {
				procedeDeFolio.append(",");
			}
			procedeDeFolio.append(predio.getNoFolio());
		}
		boolean first = true;
		List<Predio> prediosSalida = null;
		Acto _acto = null;
		for (ActoPredio actoPredio : actos) {
			Acto acto = actoPredio.getActo();

			acto.setProcesado(true);
			acto.setFechaRegistro(new Date());
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			actoRepository.save(acto);

			// ACTO PREDIO ORIGEN ACTUALIZA A TIPO ENTRADA = 2
			actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
			actoPredioRepository.save(actoPredio);

			if (first) {
				_acto =  acto;
				// se crea un nuevo acto con el predioSalida
				prediosSalida = saveSalidas(acto, actoPredio, salida,
						new ArrayList<PredioPersona>(adquitientes.values()), procedeDeFolio, actosSeleccionados);
				first = false;
			}

		}

		this.savePredioRel(prediosEntrada, prediosSalida,_acto);
		
	
	}
	
	


	@Transactional
	public void materializaRectificacionMedidas(Acto acto) {
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		UnidadMedida newUnidadMedida = new UnidadMedida();
		String newSuperficie = null;
		Double newSuperficieM2 = null;

		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		HashMap<Integer, Colindancia> colindancias = new HashMap<Integer, Colindancia>();
		
		
		for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
			int index = actoPredioCampo.getOrden();
			if (actoPredioCampo.getValor() != null) {
				if (campoId == ORIENTACION_CAMPO_ID || campoId == COLINDANCIA_CAMPO_ID) {
					Colindancia colindancia = colindancias.get(index);
					if (colindancia == null) {
						colindancia = new Colindancia();
						colindancia.setPredio(actoPredio.getPredio());
						colindancias.put(index, colindancia);
					}
					if (campoId == ORIENTACION_CAMPO_ID) {
						colindancia.setOrientacion(
								orientacionRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
					}
					if (campoId == COLINDANCIA_CAMPO_ID) {
						colindancia.setNombre(actoPredioCampo.getValor());
					}

				}

				if (campoId == UNIDAD_MEDIDA_CAMPO_ID) {
					newUnidadMedida = unidadMedidaRepository.findOne(Long.valueOf(actoPredioCampo.getValor()));
				}
				if (campoId == SUPERFICIE_CAMPO_ID) {
					newSuperficie = actoPredioCampo.getValor();
				}
				if (campoId == SUPERFICIEM2_CAMPO_ID) {
					newSuperficieM2 = Double.valueOf(actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().isEmpty() ?  actoPredioCampo.getValor(): "0");
				}
				

			}
		}

		// GUARDAMOS BITACORA DEL PREDIO
		Predio predio = actoPredio.getPredio();
		predioBitacoraService.saveBitacoraPredio(predio, predio.getColindanciasParaPredios(), acto);

		// ACTUALIZAMOS PREDIO CON SUPERFICIE
		if (newSuperficie != null) {
			predio.setSuperficie(newSuperficie);
			predio.setSuperficieM2(newSuperficieM2);
			predio.setUnidadMedida(newUnidadMedida);
			predioRepository.saveAndFlush(predio);
		}

		// ELIMINO COLINDANCIAS
		colindanciaRepository.deleteByPredioId(predio.getId());

		// GUARDA NUEVAS COLINDANCIAS
		for (Map.Entry<Integer, Colindancia> entry : colindancias.entrySet()) {
			Colindancia c = (Colindancia) entry.getValue();
			colindanciaRepository.saveAndFlush(c);
		}
		

		// EXTINGE ACTOS ANTERIOR Y GUARDA ACTOREL
		Long[] tipoActo = { Constantes.RECTIFICACION_MEDIDAS_TIPO_ACTO_ID };
		Set<Acto> actosRectificacionActivos = actoPredioRepository.findActosPredioByPredioQuery(predio.getId(),
				tipoActo);
		Acto actoAnt = actosRectificacionActivos != null && actosRectificacionActivos.size() > 0
				? actosRectificacionActivos.iterator().next()
				: null;
		if (actoAnt != null) {
			actoAnt.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
			actoRepository.saveAndFlush(actoAnt);

			ActoRel actoRel = new ActoRel();
			actoRel.setActo(acto);
			actoRel.setActoAnt(actoAnt);
			actoRelRepository.saveAndFlush(actoRel);
		}

		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		actoRepository.saveAndFlush(acto);

		
	}
	
	
	
	
	@Transactional
	public void createFolioResultante(Integer noFolioOrigen,Predio newPredio,Acto acto,Boolean controlSuperficie) 
	{
		if(noFolioOrigen!=null) {
		  Predio predioOrigen =  predioRepository.findByNoFolio(noFolioOrigen);
		  if(predioOrigen!=null) {
			  newPredio.setProcedeDeFolio(noFolioOrigen.toString());
			  predioRepository.save(newPredio);
			  List<Predio> entrada = new ArrayList<Predio>();
			  entrada.add(predioOrigen);
			  List<Predio> salida = new ArrayList<Predio>();
			  salida.add(newPredio);
			  this.savePredioRel(entrada, salida,acto);
			  materializacionActoService.heredarActos(predioOrigen, newPredio, acto.getPrelacion(),acto);
			  
			  //CO
		  }
	
		}
	}

	@Transactional
	public void deMaterializarRectificacionMedidas(Acto acto) {
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		Predio predio = actoPredio.getPredio();
		PredioBitacora predioBtc = predioBitacoraService.findByActoId(acto.getId());
		Set<PredioBitacoraColindancia> colindanciaBtc = predioBtc.getColindancias();

		// REVIERTE PREDIO
		predio.setUnidadMedida(predioBtc.getUnidadMedida());
		predio.setSuperficie(predioBtc.getSuperficie());
		predio.setSuperficieM2(predioBtc.getSuperficieM2());
		predioRepository.save(predio);

		// REVIERTE COLINDANCIAS
		colindanciaRepository.deleteByPredioId(predio.getId());

		for (PredioBitacoraColindancia colBtc : colindanciaBtc) {
			Colindancia colindancia = new Colindancia();
			colindancia.setPredio(predio);
			colindancia.setOrientacion(colBtc.getOrientacion());
			colindancia.setNombre(colBtc.getNombre());
			colindanciaRepository.save(colindancia);
		}
		// ELIMINAMOS BITACORA
		predioBitacoraService.delete(predioBtc);
		
		//ELIMINA FOLIO RESULTANTE
		predioRelRepository.deleteByActoId(acto.getId());
		
		// ACTIVA ACTO ANTERIOR
		List<ActoRel> listActoRel = actoRelRepository.findByActoId(acto.getId());
		ActoRel actoRel = listActoRel != null && listActoRel.size() > 0 ? listActoRel.get(0) : null;
		if (actoRel != null) {
			Acto actoAnt = actoRel.getActoAnt();
			actoAnt.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			actoRepository.save(actoAnt);
			actoRelRepository.delete(actoRel);
		}

		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
	}

	public void saveActosSeleccionados(HashMap<Integer, ActoPorcentajeVO> actosSeleccionados, Prelacion prelacion,
			Predio predio,Acto actoContext) {
		for (ActoPorcentajeVO actoPorcentaje : actosSeleccionados.values()) {
			Acto acto = actoPorcentaje.actoPredioMonto.getActo();

			if(acto!=null) {
				Acto newActo = actoUtilService.cloneActo(acto, prelacion, predio, null,actoContext);

				if (acto.getTipoActo().getId() == Constantes.TIPO_ACTO_HIPOTECA) {
					Set<Acto> cancelaciones = new HashSet<>();
					// clonar adicionalmente registros en acto_predio_monto con acto_id
					List<ActoPredioMonto> actoPrediosMonto = actoPredioMontoRepository.findByActoId(acto.getId());
					for (ActoPredioMonto actoPredioMonto : actoPrediosMonto) {
						cancelaciones.add(actoPredioMonto.getActoPredio().getActo());
					}

					for (Acto can : cancelaciones) {
						log.debug("acto cancelacion = {}", can);

						actoUtilService.cloneActo(can, prelacion, predio, newActo,actoContext);
					}

				}
			}

		}

	}
				
}
