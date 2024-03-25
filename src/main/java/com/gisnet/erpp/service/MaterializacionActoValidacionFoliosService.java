package com.gisnet.erpp.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.repository.ActoPredioMontoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRelRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.EstadoCivilRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.repository.RegimenRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoMonedaRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class MaterializacionActoValidacionFoliosService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String FECHA_FORMATO_CAMPO_VALOR = "yyyy-mm-dd";

	
	private final long TIPO_DE_PERSONA_CAMPO_ID = 84;
	private final long NOMBRE_CAMPO_ID = 286;
	private final long PRIMER_APELLIDO_CAMPO_ID	= 287;
	private final long SEGUNDO_APELLIDO_CAMPO_ID =	288;
	private final long NACIONALIDAD_CAMPO_ID =	119;
	private final long DD_CAMPO_ID =	55;
	private final long UV_CAMPO_ID =	56;
	private final long SUPERFICIE_CAMPO_ID = 1055;
	private final long DESIGNO_BENEFICIARIO_CAMPO_ID = 62;
	private final long FECHA_NACIMIENTO_CAMPO_ID =	289;
	private final long RFC_CAMPO_ID =	290;
	private final long CURP_CAMPO_ID =	291;

	private final long MONTO_DEL_CREDITO_CAMPO_ID = 72;
	private final long PORCENTAJE_AFECTACION_DEL_INMUEBLE_CAMPO_ID = 263;
	private final long TIPO_DE_MONEDA_CAMPO_ID = 265;

	private final long PREDIO_A_SUBDIVIDIR_CAMPO_ID = 270;
	private final long SUPERFICIE_SUBDIVIDIDA_CAMPO_ID = 271;

	@Autowired
	PrelacionService prelacionService;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	ActoService actoService;

	@Autowired
	ActoPredioMontoRepository actoPredioMontoRepository;

	@Autowired
	PersonaRepository personaRepository;

	@Autowired
	TipoMonedaRepository tipoMonedaRepository;

	@Autowired
	StatusActoRepository statusActoRepository;

	@Autowired
	ActoRelRepository actoRelRepository;

	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	PredioPersonaRepository predioPersonaRepository;

	@Autowired
	TipoRelPersonaRepository tipoRelPersonaRepository;

	@Autowired
	NacionalidadRepository nacionalidadRepository;

	@Autowired
	DireccionRepository direccionRepository;

	@Autowired
	RegimenRepository regimenRepository;

	@Autowired
	TipoPersonaRepository tipoPersonaRepositor;

	@Autowired
	EstadoCivilRepository estadoCivilRepository;

	@Autowired
	PredioRepository predioRepository;
	
	@Transactional
	public void desMaterializarPredio(Long prelacionId, Long predioId) {
		List<ActoPredio> actos = actoPredioRepository.findAllByActoPrelacionIdAndPredioIdAndActoVf(prelacionId, predioId, true);

		for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
			ActoPredio actoPredio = (ActoPredio) iterator.next();			
			predioPersonaRepository.deleteByActoPredioId(actoPredio.getId());
		}

	}

	
	public void materializarPredio(Long prelacionId, Long predioId) {
		List<ActoPredio> actos = actoPredioRepository.findAllByActoPrelacionIdAndPredioIdAndActoVf(prelacionId, predioId, true);
		ActoPredio ultimoTraslativo= null;

		//Buscar el ultimo traslativo de la lista, para cambiar el status a ACTIVO
		for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
			ActoPredio actoPredio = (ActoPredio) iterator.next();
			
			Constantes.ClasifActo clasifActo = Constantes.ClasifActo.getClasifActo(actoPredio.getActo().getTipoActo().getClasifActo().getId());			
			if (clasifActo == Constantes.ClasifActo.TRASLATIVOS  ){
				ultimoTraslativo = actoPredio;
			}			
		}
		
		log.debug("ultimo Traslativo = {} ", ultimoTraslativo);


		for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
			ActoPredio actoPredio = (ActoPredio) iterator.next();

				materializar(actoPredio.getActo(), actoPredio.getId()==ultimoTraslativo.getId());
			
		}

		log.debug("Materializacion de prelacion {} y predio {} terminada", prelacionId, predioId);
	}

	public void materializar(Acto acto, boolean activo) {
		Constantes.ClasifActo clasifActo = Constantes.ClasifActo.getClasifActo(acto.getTipoActo().getClasifActo().getId());
		log.debug("Materizando acto={}", acto);

		switch (clasifActo) {
		case TRASLATIVOS:
			materializaTraslativo(acto, activo);
			break;
		case ACTOS_MODIFICATORIOS_DEL_INMUEBLE:
			materializaModfificatorioInmueble(acto);
			break;
		case GRAMAVAMES_LIMITATANES:
			materializarGravamenes(acto);
			break;
		case CANCELACIONES:
			materializarCancelacion(acto);
			break;
		default:
			log.warn("No existe materializacion para la clasificacion={}", clasifActo.name());
		}

	}

	private void materializaTraslativo(Acto acto, boolean activo) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		log.debug("actoPredio= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, PredioPersona> adquitientes = new HashMap<Integer, PredioPersona>();

		actoPredioCampos.forEach((actoPredioCampo) -> {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();

			// actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();
			
			log.debug("{} = {}", campoId, actoPredioCampo.getValor());

			if (campoId == NOMBRE_CAMPO_ID || campoId == PRIMER_APELLIDO_CAMPO_ID || campoId == SEGUNDO_APELLIDO_CAMPO_ID || campoId == FECHA_NACIMIENTO_CAMPO_ID || campoId == RFC_CAMPO_ID || campoId == CURP_CAMPO_ID  || campoId == NACIONALIDAD_CAMPO_ID || campoId == TIPO_DE_PERSONA_CAMPO_ID || campoId == DD_CAMPO_ID || campoId == UV_CAMPO_ID || campoId == SUPERFICIE_CAMPO_ID || campoId == DESIGNO_BENEFICIARIO_CAMPO_ID) {
				PredioPersona predioPersona = adquitientes.get(index);
				if (predioPersona == null) {
					predioPersona = new PredioPersona();
					predioPersona.setPersona(new Persona());
					adquitientes.put(index, predioPersona);
				}

				if (campoId == NOMBRE_CAMPO_ID) {
					predioPersona.getPersona().setNombre(actoPredioCampo.getValor());
				} else if (campoId == PRIMER_APELLIDO_CAMPO_ID) {
					predioPersona.getPersona().setPaterno(actoPredioCampo.getValor());
				} else if (campoId == SEGUNDO_APELLIDO_CAMPO_ID) {
					predioPersona.getPersona().setMaterno(actoPredioCampo.getValor());
				} else if (campoId == FECHA_NACIMIENTO_CAMPO_ID) {
					predioPersona.getPersona().setFechaNac(UtilFecha.toDate(actoPredioCampo.getValor(), FECHA_FORMATO_CAMPO_VALOR));
				} else if (campoId == RFC_CAMPO_ID) {
					predioPersona.getPersona().setRfc(actoPredioCampo.getValor());
				} else if (campoId == CURP_CAMPO_ID) {
					predioPersona.getPersona().setCurp(actoPredioCampo.getValor());
				} else if (campoId == NACIONALIDAD_CAMPO_ID) {
					Nacionalidad nacionalidad = nacionalidadRepository.findOne(Long.valueOf(actoPredioCampo.getValor()));
					predioPersona.getPersona().setNacionalidad(nacionalidad);
					predioPersona.setNacionalidad(nacionalidad);
				} else if (campoId == TIPO_DE_PERSONA_CAMPO_ID) {
					predioPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Long.valueOf(actoPredioCampo.getValor())));
				} else if (campoId == DD_CAMPO_ID) {
					predioPersona.setPorcentajeDd(Float.valueOf(actoPredioCampo.getValor()));
				} else if (campoId == UV_CAMPO_ID) {
					predioPersona.setPorcentajeUv(Float.valueOf(actoPredioCampo.getValor()));
				} else if (campoId == SUPERFICIE_CAMPO_ID) {
					predioPersona.setSuperficie(Double.valueOf(actoPredioCampo.getValor()));
				} else if (campoId == DESIGNO_BENEFICIARIO_CAMPO_ID) {
					predioPersona.setDesignoBeneficiario(Boolean.valueOf(actoPredioCampo.getValor()));
				}

			}

			Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
			if (tipoCampo == null) {
				log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}", actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());

			} 

		});

		log.debug("total de adquirienes={}", adquitientes.size());
		adquitientes.forEach((index, predioPersona) -> {
			log.debug("dd= {}, uv={}, superficie={},nacionalidad{}, pesona={}", predioPersona.getPorcentajeDd(), predioPersona.getPorcentajeUv(), predioPersona.getSuperficie(), predioPersona.getNacionalidad(), predioPersona.getPersona());
		});

		// crear nuevos registros para adquirientes
		if (activo){
			acto.statusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		} else {
			acto.statusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
		}
		actoRepository.save(acto);

		for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
			PredioPersona predioPersona = entry.getValue();

			Optional<Persona> persona2 = null;
			if (predioPersona.getPersona().getTipoPersona().getId().intValue()==Constantes.ETipoPersona.FISICA.getAsInteger().intValue() && predioPersona.getPersona().getCurp()!=null && predioPersona.getPersona().getCurp().length()>=18){
				persona2 = personaRepository.findByCurp(predioPersona.getPersona().getCurp());
			} else if (predioPersona.getPersona().getTipoPersona().getId().intValue()==Constantes.ETipoPersona.MORAL.getAsInteger().intValue() && predioPersona.getPersona().getRfc()!=null && predioPersona.getPersona().getRfc().length()>=12) {
				persona2 = personaRepository.findByCurp(predioPersona.getPersona().getRfc());
			}
			
			if (persona2!= null && persona2.isPresent()) {
				predioPersona.setPersona(persona2.get());
			} else {
				predioPersona.getPersona().setActivo(true);
				personaRepository.save(predioPersona.getPersona());
			}

			// TODO: Cambiar a constente PROPIETARIO
			predioPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(1L));
			predioPersona.setDireccion(direccionRepository.findOne(1L));
			predioPersona.setActoPredio(actoPredio);
			predioPersonaRepository.save(predioPersona);
		}
		
	}

	private void materializarCancelacion(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPrediocampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, ActoPredioMonto> actoPredioMontos = new HashMap<Integer, ActoPredioMonto>();

		actoPrediocampos.forEach((actoPrediocampo) -> {
			Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPrediocampo.getModuloCampo().getCampo().getTipoCampo().getId());
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			log.debug("{} = {}", tipoCampo.name(), actoPrediocampo.getValor());
			int index = actoPrediocampo.getOrden();

			if (tipoCampo == tipoCampo.ACTO || campoId == MONTO_DEL_CREDITO_CAMPO_ID || campoId == PORCENTAJE_AFECTACION_DEL_INMUEBLE_CAMPO_ID || campoId == TIPO_DE_MONEDA_CAMPO_ID) {
				ActoPredioMonto actoPredioMonto = actoPredioMontos.get(index);
				if (actoPredioMonto == null) {
					actoPredioMonto = new ActoPredioMonto();
					actoPredioMontos.put(index, actoPredioMonto);
				}

				switch (tipoCampo) {
				case ACTO:
					actoPredioMonto.setActo(actoService.findOne(Long.valueOf(actoPrediocampo.getValor())));
					break;
				}

				if (campoId == MONTO_DEL_CREDITO_CAMPO_ID) {
					actoPredioMonto.setMonto(new BigDecimal(actoPrediocampo.getValor()));
				}
				if (campoId == PORCENTAJE_AFECTACION_DEL_INMUEBLE_CAMPO_ID) {
					actoPredioMonto.setPorcentaje(Float.parseFloat(actoPrediocampo.getValor()));
				}

				if (campoId == TIPO_DE_MONEDA_CAMPO_ID) {
					actoPredioMonto.setTipoMoneda(tipoMonedaRepository.findOne(Long.parseLong(actoPrediocampo.getValor())));
				}
			}

		});

		log.debug("total de actos en siendo cancelados={}", actoPredioMontos.size());
		actoPredioMontos.forEach((index, actoPredioMonto) -> {
			log.debug("acto= {}, monto{}, porcentaje{}", actoPredioMonto.getActo(), actoPredioMonto.getMonto(), actoPredioMonto.getPorcentaje());
		});

		HashMap<Integer, TotalesVO> totales = new HashMap<Integer, TotalesVO>();

		// calcular totales en cancelaciones anteriores + nuevos para checar si
		// se cancela al 100%

		for (Map.Entry<Integer, ActoPredioMonto> entry : actoPredioMontos.entrySet()) {
			List<Acto> actosCancelacionesAnteriores = actoService.getActosCancelacionesVigentesParaActo(entry.getValue().getActo().getId());
			log.debug("acto de cancelacion anterior {} ", actosCancelacionesAnteriores);
			for (Acto a : actosCancelacionesAnteriores) {
				ActoPredio ap = a.getActoPrediosParaActosOrderedByVersion().first();
				ap.getActoPredioMontosParaActoPredios().forEach((actoPredioMonto) -> {
					if (actoPredioMonto.getActo().getId() == entry.getValue().getActo().getId()) {
						TotalesVO total = totales.get(entry.getKey());
						if (total == null) {
							total = new TotalesVO();
							totales.put(entry.getKey(), total);
						}
						total.totalPorcentaje += actoPredioMonto.getPorcentaje();
						total.totalMonto = total.totalMonto.add(actoPredioMonto.getMonto());
					}
				});
			}

			TotalesVO total = totales.get(entry.getKey());
			if (total == null) {
				total = new TotalesVO();
				totales.put(entry.getKey(), total);
			}
			total.totalPorcentaje += entry.getValue().getPorcentaje();
			total.totalMonto = total.totalMonto.add(entry.getValue().getMonto());
		}

		// Si es igual al 100% o el monto es igual entonces el acto debe ser NO
		// VIGENTE
		for (Map.Entry<Integer, ActoPredioMonto> entry : actoPredioMontos.entrySet()) {
			TotalesVO total = totales.get(entry.getKey());

			log.debug("totales para el acto {}, porcentaje{}, monto={}", entry.getValue().getActo().getId(), total.totalPorcentaje, total.totalMonto);

			Acto actoSiendoCancelado = entry.getValue().getActo();

			if (actoSiendoCancelado.getActoPrediosParaActos() == null && actoSiendoCancelado.getActoPrediosParaActos().size() == 0) {
				throw new IllegalArgumentException("El acto siendo cancelado no tiene un acto_predio");
			}

			if (actoSiendoCancelado.getActoPrediosParaActosOrderedByVersion().first().getActoPredioMontosParaActoPredios() == null || actoSiendoCancelado.getActoPrediosParaActosOrderedByVersion().first().getActoPredioMontosParaActoPredios().size() == 0) {
				throw new IllegalArgumentException("El acto siendo cancelado no tiene un acto_predio_monto");
			}

			if (total != null && (total.totalPorcentaje == 100 || total.totalMonto.equals(actoSiendoCancelado.getActoPrediosParaActosOrderedByVersion().first().getActoPredioMontosParaActoPredios().iterator().next().getMonto()))) {
				// ya se completo el acto siendo cancelado
				actoSiendoCancelado.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
				actoRepository.save(actoSiendoCancelado);
			}
		}

		// cambiar a vigente acto de cancelacion
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
		actoRepository.save(acto);

		for (Map.Entry<Integer, ActoPredioMonto> entry : actoPredioMontos.entrySet()) {
			ActoPredioMonto apm = entry.getValue();
			apm.setActoPredio(actoPredio);
			actoPredioMontoRepository.save(apm);
		}

	}

	private void materializarGravamenes(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPrediocampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, ActoPredioMonto> actoPredioMontos = new HashMap<Integer, ActoPredioMonto>();

		actoPrediocampos.forEach((actoPrediocampo) -> {
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			log.debug("campoid={} , valor={}", campoId, actoPrediocampo.getValor());
			int index = actoPrediocampo.getOrden();

			if (campoId == MONTO_DEL_CREDITO_CAMPO_ID || campoId == PORCENTAJE_AFECTACION_DEL_INMUEBLE_CAMPO_ID || campoId == TIPO_DE_MONEDA_CAMPO_ID) {
				ActoPredioMonto actoPredioMonto = actoPredioMontos.get(index);
				if (actoPredioMonto == null) {
					actoPredioMonto = new ActoPredioMonto();
					actoPredioMontos.put(index, actoPredioMonto);
				}

				if (campoId == MONTO_DEL_CREDITO_CAMPO_ID) {
					actoPredioMonto.setMonto(new BigDecimal(actoPrediocampo.getValor()));
				}
				if (campoId == PORCENTAJE_AFECTACION_DEL_INMUEBLE_CAMPO_ID) {
					actoPredioMonto.setPorcentaje(Float.parseFloat(actoPrediocampo.getValor()));
				}

				if (campoId == TIPO_DE_MONEDA_CAMPO_ID) {
					actoPredioMonto.setTipoMoneda(tipoMonedaRepository.findOne(Long.parseLong(actoPrediocampo.getValor())));
				}

			}

		});

		log.debug("total de actos en gravamen={}", actoPredioMontos.size());
		actoPredioMontos.forEach((index, actoPredioMonto) -> {
			log.debug("acto= {}, monto={}, porcentaje={}, tipo_moneda={}", actoPredioMonto.getActo(), actoPredioMonto.getMonto(), actoPredioMonto.getPorcentaje(), actoPredioMonto.getTipoMoneda() != null ? actoPredioMonto.getTipoMoneda().getNombre() : "null");
		});

		// cambiar a vigente acto de cancelacion
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
		actoRepository.save(acto);

		for (Map.Entry<Integer, ActoPredioMonto> entry : actoPredioMontos.entrySet()) {
			ActoPredioMonto apm = entry.getValue();
			apm.setActoPredio(actoPredio);
			actoPredioMontoRepository.save(apm);
		}

	}

	private void materializaModfificatorioInmueble(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no se le asociado un predio");
		}

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, FraccionVO> fracciones = new HashMap<Integer, FraccionVO>();
		Long[] predioId = new Long[1];

		actoPredioCampos.forEach((actoPredioCampo) -> {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
			log.debug("{} = {}", actoPredioCampo.getModuloCampo().getCampo().getNombre(), actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();

			if (campoId == PREDIO_A_SUBDIVIDIR_CAMPO_ID || campoId == SUPERFICIE_SUBDIVIDIDA_CAMPO_ID) {
				FraccionVO fraccionVO = fracciones.get(index);
				if (fraccionVO == null) {
					fraccionVO = new FraccionVO();
					fracciones.put(index, fraccionVO);
				}

				if (campoId == PREDIO_A_SUBDIVIDIR_CAMPO_ID) {
					predioId[0] = Long.valueOf(actoPredioCampo.getValor());
				} else if (campoId == SUPERFICIE_SUBDIVIDIDA_CAMPO_ID) {
					fraccionVO.superficie = Float.parseFloat(actoPredioCampo.getValor());
				}
			}

		});

		Predio predio = predioRepository.findOne(predioId[0]);

		log.debug("predio a fraccionar={}", predio);
		log.debug("total de fracciones={}", fracciones.size());
		fracciones.forEach((index, fraccionVO) -> {
			log.debug("superficie= {}", fraccionVO.superficie);
		});

		// Crear n nuevos predios

		fracciones.forEach((index, fraccionVO) -> {
			log.debug("superficie= {}", fraccionVO.superficie);

			Predio fraccion = new Predio();
			BeanUtils.copyProperties(predio, fraccion);

			fraccion.setId(null);

			fraccion.getColindanciasParaPredios().forEach((colindancia) -> {
				colindancia.setId(null);
			});

		});

	}

	private class TotalesVO {
		public float totalPorcentaje;
		public BigDecimal totalMonto;

		public TotalesVO() {
			totalPorcentaje = 0;
			totalMonto = BigDecimal.ZERO;
		}
	}

	private class FraccionVO {
		public float superficie;

		public FraccionVO() {
			superficie = 0;
		}
	}

	private void copyActo(Acto source, Acto target) {
		target.setTipoActo(source.getTipoActo());
		target.setPrelacion(source.getPrelacion());
		target.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		target.setOrden(source.getOrden());
		target.setVersion(source.getVersion());

	}

	private void copyActoPredio(ActoPredio source, ActoPredio target) {
		target.setPredio(source.getPredio());
		target.setTipoEntrada(source.getTipoEntrada());
		target.setVersion(source.getVersion());
	}

	private void copyPredioPersona(PredioPersona source, PredioPersona target) {
		target.setPersona(source.getPersona());
		;
		target.setDireccion(source.getDireccion());
		target.setTipoRelPersona(source.getTipoRelPersona());
		target.setPorcentajeDd(source.getPorcentajeDd());
		target.setPorcentajeUv(source.getPorcentajeUv());

		target.setParentesco(source.getParentesco());
		target.setEstadoCivil(source.getEstadoCivil());
		target.setRegimen(source.getRegimen());
		target.setNacionalidad(source.getNacionalidad());
	}
}
