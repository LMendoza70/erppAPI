package com.gisnet.erpp.service.materializacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioMontoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.EstadoCivilRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.RegimenRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.service.ActoControlHeredaService;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class ActoUtilService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public final static String FECHA_FORMATO_CAMPO_VALOR = "yyyy-MM-dd";
	// campo_ids utilizados, los tipos de campo ids se encuentran en la clase Constantes.TipoCampo
	public final static long NOMBRE_CAMPO_ID = 286;
	public final static long PATERNO_CAMPO_ID = 287;
	public final static long MATERNO_CAMPO_ID = 288;
	public final static long FECHA_NACIMIENTO_CAMPO_ID = 289;
	public final static long RFC_CAMPO_ID = 290;
	public final static long CURP_CAMPO_ID = 291;
	public final static long ESTADO_CIVIL_CAMPO_ID = 292;
	public final static long REGIMEN_CAMPO_ID = 293;
	public final static long NACIONALIDAD_CAMPO_ID = 119;
	public final static long TIPO_DE_PERSONA_CAMPO_ID = 84;
	public final static long TIPO_DE_PERSONA_FISICA_CAMPO_ID = 26;
	public final static long TIPO_DE_PERSONA_MORAL_CAMPO_ID = 18;
	public final static long DESIGNO_BENEFICIARIO_CAMPO_ID = 62;
	public final static long DD_CAMPO_ID = 55;
	public final static long UV_CAMPO_ID = 56;
	public final static long SUPERFICIE_CAMPO_ID = 1055;
	public final static long PERSONA_CAMPO_ID = 40;
	
	public final static long FIDUCIARIO_NOMBRE_CAMPO_ID = 671L;

	public final static long ACTO_SELECCIONADO_FUSION_CAMPO_ID = 261;
	public final static long MONTO_DEL_CREDITO_CAMPO_ID = 72;
	public final static long PORCENTAJE_A_CANCELAR_CAMPO_ID = 263;
	public final static long ACTO_SELECCIONADO_CAMPO_ID = 261;
	public final static long TIPO_DE_MONEDA_CAMPO_ID = 265;

	public final static long PREDIO_A_SUBDIVIDIR_CAMPO_ID = 270;
	public final static long SUPERFICIE_A_SUBDIVIDIR_CAMPO_ID = 269;
	public final static long SUPERFICIE_SUBDIVIDIDA_CAMPO_ID = 271;
	public final static long PREDIO_DETALLE_FORMA_CAMPO_ID = 500;
	
	public final static long SUPERFICIE_FOLIOS_A_FUSIONAR_CAMPO_ID = 298;
	public final static long PREDIO_POR_FUSIONAR_CAMPO_ID = 268;
	
	public final static long PREDIO_FUSIONADO_CAMPO_ID=500;
	public final static long SUPERFICIE_FUSIONADO_CAMPO_ID = 267;
	
	@Autowired
	PersonaRepository personaRepository;
	
	@Autowired
	EstadoCivilRepository estadoCivilRepository;
	
	@Autowired
	RegimenRepository regimenRepository;
	
	@Autowired
	NacionalidadRepository nacionalidadRepository;
	
	@Autowired
	TipoPersonaRepository tipoPersonaRepository;
	
	@Autowired
	ActoService actoService;
	
	@Autowired
	StatusActoRepository statusActoRepository;
	
	@Autowired
	ActoRepository actoRepository;
	
	@Autowired
	ActoPredioRepository actoPredioRepository;
	
	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;
	
	@Autowired
	ActoPredioMontoRepository actoPredioMontoRepository;
	
	@Autowired
	ActoFirmaRepository actoFirmaRepository;
	
	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;
	
	@Autowired
	ActoControlHeredaService actoControlHeredaService; 
	
	
	
	public HashMap<Integer, PredioPersona> parseTitulares(Set<ActoPredioCampo> actoPredioCampos) {
		HashMap<Integer, PredioPersona> titulares = new HashMap<Integer, PredioPersona>();
		
		log.debug("total de actoPredioCampos para acto = {}, {}", actoPredioCampos.size(), actoPredioCampos);

		actoPredioCampos.forEach((actoPredioCampo) -> {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();

			// actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();
			
			log.debug("{} = {}", campoId, actoPredioCampo.getValor());
			
			if (actoPredioCampo.getValor()!=null) {
	
				Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
				if (tipoCampo == null) {
					log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}", actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
	
				} else {
					if (tipoCampo == tipoCampo.PERSONA_PREDIO_TITULAR || tipoCampo == tipoCampo.DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR || tipoCampo == tipoCampo.USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR || tipoCampo == tipoCampo.SUPERFICE_A_TRANSMITIR) {
						PredioPersona predioPersona = titulares.get(index);
						if (predioPersona == null) {
							predioPersona = new PredioPersona();
							titulares.put(index, predioPersona);
						}
	
						switch (tipoCampo) {
						case PERSONA_PREDIO_TITULAR:
							predioPersona.setPersona(personaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
							break;
						case DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR:
							predioPersona.setPorcentajeDd(Float.valueOf(actoPredioCampo.getValor()));
							break;
						case USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR:						
							predioPersona.setPorcentajeUv(Float.valueOf(actoPredioCampo.getValor()));
							break;
						case SUPERFICE_A_TRANSMITIR:
							predioPersona.setSuperficie(Double.valueOf(actoPredioCampo.getValor()));
							break;
							
						}
					}
				}
			}

		});
	    
		log.debug("total de titulares={}", titulares.size());
		titulares.forEach((index, predioPersona) -> {
			log.debug("titulares dd= {}, uv={}, superficie={}, pesona={}", predioPersona.getPorcentajeDd(), predioPersona.getPorcentajeUv(), predioPersona.getSuperficie(), predioPersona.getPersona());
		});
		
		return titulares;
	}
	
	/**
	 * 
	 * @param actoPredioCampos
	 * @param acto
	 * @param moduloId si el moduloId es nulo, no se lee personaId
	 * @return
	 */
	public HashMap<Integer, PredioPersona> parseAdquirientes(Set<ActoPredioCampo> actoPredioCampos, Acto acto, Long moduloId) {
		HashMap<Integer, PredioPersona> adquitientes = new HashMap<Integer, PredioPersona>();
		
		log.debug("total de actoPredioCampos para acto = {}, {}", actoPredioCampos.size(), actoPredioCampos);

		actoPredioCampos.forEach((actoPredioCampo) -> {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();

			// actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();
			
			log.debug("{} = {}", campoId, actoPredioCampo.getValor());
			
			if (actoPredioCampo.getValor()!=null && (moduloId==null ||  actoPredioCampo.getModuloCampo().getModulo().getId().equals(moduloId))) {
				if ((campoId == PERSONA_CAMPO_ID && moduloId!=null) || campoId == NOMBRE_CAMPO_ID || campoId == FIDUCIARIO_NOMBRE_CAMPO_ID ||campoId == PATERNO_CAMPO_ID || campoId == MATERNO_CAMPO_ID || campoId == FECHA_NACIMIENTO_CAMPO_ID || campoId == RFC_CAMPO_ID || campoId == CURP_CAMPO_ID || campoId == ESTADO_CIVIL_CAMPO_ID || campoId == REGIMEN_CAMPO_ID || campoId == NACIONALIDAD_CAMPO_ID || campoId == TIPO_DE_PERSONA_CAMPO_ID || campoId == TIPO_DE_PERSONA_FISICA_CAMPO_ID || campoId == TIPO_DE_PERSONA_MORAL_CAMPO_ID || campoId == DD_CAMPO_ID || campoId == UV_CAMPO_ID || campoId == SUPERFICIE_CAMPO_ID || campoId == DESIGNO_BENEFICIARIO_CAMPO_ID ) {
					PredioPersona predioPersona = adquitientes.get(index);
					if (predioPersona == null) {
						predioPersona = new PredioPersona();
						predioPersona.setPersona(new Persona());
						adquitientes.put(index, predioPersona);
					}
					
					if (acto.getTipoActo().getId() == Constantes.TIPO_ACTO_FIDEICOMISO_ID) {
						if (campoId == FIDUCIARIO_NOMBRE_CAMPO_ID) {
							predioPersona.getPersona().setNombre(actoPredioCampo.getValor());
						}
					} else {
						if (campoId == PERSONA_CAMPO_ID) {
							predioPersona.setPersona(personaRepository.findOne(Long.parseLong(actoPredioCampo.getValor())));
						} else if (campoId == NOMBRE_CAMPO_ID) {
							predioPersona.getPersona().setNombre(actoPredioCampo.getValor());
						} else if (campoId == PATERNO_CAMPO_ID) {
							predioPersona.getPersona().setPaterno(actoPredioCampo.getValor());
						} else if (campoId == MATERNO_CAMPO_ID) {
							predioPersona.getPersona().setMaterno(actoPredioCampo.getValor());
						} else if (campoId == FECHA_NACIMIENTO_CAMPO_ID) {
							predioPersona.getPersona().setFechaNac(UtilFecha.toDate(actoPredioCampo.getValor(), FECHA_FORMATO_CAMPO_VALOR));
						} else if (campoId == RFC_CAMPO_ID) {
							predioPersona.getPersona().setRfc(actoPredioCampo.getValor());
						} else if (campoId == CURP_CAMPO_ID) {
							predioPersona.getPersona().setCurp(actoPredioCampo.getValor());
						} else if (campoId == ESTADO_CIVIL_CAMPO_ID) {
							    predioPersona.setEstadoCivil(estadoCivilRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
						} else if (campoId == REGIMEN_CAMPO_ID) {
							if (actoPredioCampo.getValor()!=null && actoPredioCampo.getValor().length()>0){					
								predioPersona.setRegimen(regimenRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
							}
						} else if (campoId == NACIONALIDAD_CAMPO_ID) {
							Nacionalidad nacionalidad = nacionalidadRepository.findOne(Long.valueOf(actoPredioCampo.getValor()));
							predioPersona.getPersona().setNacionalidad(nacionalidad);
							predioPersona.setNacionalidad(nacionalidad);
						} 
					}
						
						
					if (campoId == TIPO_DE_PERSONA_CAMPO_ID) {
						predioPersona.getPersona().setTipoPersona(tipoPersonaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
					} else if (campoId == TIPO_DE_PERSONA_FISICA_CAMPO_ID) {
						predioPersona.getPersona().setTipoPersona(tipoPersonaRepository.findOne(Constantes.TipoPersona.FISICA.getIdTipoPersona()));
					} else if (campoId == TIPO_DE_PERSONA_MORAL_CAMPO_ID) {
						predioPersona.getPersona().setTipoPersona(tipoPersonaRepository.findOne(Constantes.TipoPersona.MORAL.getIdTipoPersona()));
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
				
			}

		});

		for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
			PredioPersona pp = (PredioPersona) entry.getValue();
			log.debug("adquirientes dd= {}, uv={}, superficie={}, nacionalidad{}, estado_civil{},, pesona={}", pp.getPorcentajeDd(), pp.getPorcentajeUv(), pp.getSuperficie(), pp.getPorcentajeUv(), pp.getNacionalidad(), pp.getEstadoCivil() , pp.getPersona().toString());
		}
		
		return adquitientes;

	}
	public HashMap<Integer, ActoPorcentajeVO> parseActosFusion(Set<ActoPredioCampo> actoPrediocampos) {
		HashMap<Integer, ActoPorcentajeVO> actoPorcentajes = new HashMap<Integer, ActoPorcentajeVO>();

		actoPrediocampos.forEach((actoPrediocampo) -> {
			Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPrediocampo.getModuloCampo().getCampo().getTipoCampo().getId());
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			long moduloCampoId = actoPrediocampo.getModuloCampo().getId();
			if (tipoCampo != null) {
				log.debug("campoId = {}, tipo_campo {} , valor{}", campoId, tipoCampo.name(), actoPrediocampo.getValor());
				int index = actoPrediocampo.getOrden();
	
				if (tipoCampo == tipoCampo.ACTO || campoId == ACTO_SELECCIONADO_FUSION_CAMPO_ID ) {
					ActoPorcentajeVO actoPorcentaje = actoPorcentajes.get(index);
					if (actoPorcentaje == null) {
						actoPorcentaje = new ActoPorcentajeVO();
						actoPorcentaje.actoPredioMonto = new ActoPredioMonto();
						actoPorcentajes.put(index, actoPorcentaje);
					}
	
					switch (tipoCampo) {
					case ACTO:
						actoPorcentaje.actoPredioMonto.setActo(actoService.findOne(Long.valueOf(actoPrediocampo.getValor())));
						break;
					}
					
					if (campoId == ACTO_SELECCIONADO_FUSION_CAMPO_ID) {
						actoPorcentaje.actoSeleccionado = new Boolean(actoPrediocampo.getValor()).booleanValue();
					}
	
				}
			}

		});
		
		//remover actoPorcentajes no seleccionados
		actoPorcentajes.entrySet().removeIf(entry -> !entry.getValue().actoSeleccionado);

		log.debug("total de actos seleccionados={}", actoPorcentajes.size());

		
		return actoPorcentajes;

	}
	
	

	
	public HashMap<Integer, ActoPorcentajeVO> parseActos(Set<ActoPredioCampo> actoPrediocampos) {
		HashMap<Integer, ActoPorcentajeVO> actoPorcentajes = new HashMap<Integer, ActoPorcentajeVO>();

		actoPrediocampos.forEach((actoPrediocampo) -> {
			Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPrediocampo.getModuloCampo().getCampo().getTipoCampo().getId());
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			long moduloCampoId = actoPrediocampo.getModuloCampo().getId();
			if (tipoCampo != null) {
				log.debug("campoId = {}, tipo_campo {} , valor{}", campoId, tipoCampo.name(), actoPrediocampo.getValor());
				int index = actoPrediocampo.getOrden();
	
				if (tipoCampo == tipoCampo.ACTO || campoId == ACTO_SELECCIONADO_CAMPO_ID || campoId == PORCENTAJE_A_CANCELAR_CAMPO_ID ) {
					ActoPorcentajeVO actoPorcentaje = actoPorcentajes.get(index);
					if (actoPorcentaje == null) {
						actoPorcentaje = new ActoPorcentajeVO();
						actoPorcentaje.actoPredioMonto = new ActoPredioMonto();
						actoPorcentajes.put(index, actoPorcentaje);
					}
	
					switch (tipoCampo) {
					case ACTO:
						actoPorcentaje.actoPredioMonto.setActo(actoService.findOne(Long.valueOf(actoPrediocampo.getValor())));
						break;
					}
					
					if (campoId == ACTO_SELECCIONADO_CAMPO_ID) {
						actoPorcentaje.actoSeleccionado = new Boolean(actoPrediocampo.getValor()).booleanValue();
					}
	
					if (campoId == PORCENTAJE_A_CANCELAR_CAMPO_ID) {
						actoPorcentaje.actoPredioMonto.setPorcentaje(Float.parseFloat(actoPrediocampo.getValor()));
					}
				}
			}

		});
		
		//remover actoPorcentajes no seleccionados
		actoPorcentajes.entrySet().removeIf(entry -> !entry.getValue().actoSeleccionado);

		log.debug("total de actos seleccionados={}", actoPorcentajes.size());

		
		return actoPorcentajes;

	}
	
	public String parsePrediosPermuta(Set<ActoPredioCampo> actoPrediocampos) {
		String prediosPermuta = null;
		for (ActoPredioCampo actoPrediocampo : actoPrediocampos) {
			Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPrediocampo.getModuloCampo().getCampo().getTipoCampo().getId());
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			long moduloCampoId = actoPrediocampo.getModuloCampo().getId();
			if (tipoCampo != null) {
				int index = actoPrediocampo.getOrden();
	
				if (tipoCampo == tipoCampo.PREDIOS_PERMUTA ) {
					prediosPermuta = actoPrediocampo.getValor();
				}
			}
		}

		log.debug("prediosPermuta={}", prediosPermuta);

		return prediosPermuta;

	}


	
	public List<HashMap<Integer, FraccionVO>> parseFraccionVOEntradaYSalida(Set<ActoPredioCampo> actoPredioCampos) {		
		HashMap<Integer, FraccionVO> entrada = new HashMap<Integer, FraccionVO>();
		HashMap<Integer, FraccionVO> salida = new HashMap<Integer, FraccionVO>();

		actoPredioCampos.forEach((actoPredioCampo) -> {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
			log.debug("{} = {}", actoPredioCampo.getModuloCampo().getCampo().getNombre(), actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();

			if (campoId == SUPERFICIE_SUBDIVIDIDA_CAMPO_ID || campoId == PREDIO_DETALLE_FORMA_CAMPO_ID || campoId == PREDIO_FUSIONADO_CAMPO_ID || campoId == SUPERFICIE_FUSIONADO_CAMPO_ID ) {
				FraccionVO fraccionVO = salida.get(index);
				if (fraccionVO == null) {
					fraccionVO = new FraccionVO();
					salida.put(index, fraccionVO);
				}

				
				if (campoId == SUPERFICIE_SUBDIVIDIDA_CAMPO_ID || campoId == SUPERFICIE_FUSIONADO_CAMPO_ID) {
					fraccionVO.superficie = Double.parseDouble(actoPredioCampo.getValor());
				} else if (campoId == PREDIO_DETALLE_FORMA_CAMPO_ID || campoId == PREDIO_FUSIONADO_CAMPO_ID ) {
					fraccionVO.predioId = Long.parseLong(actoPredioCampo.getValor());
				}
			}
			
			if (campoId == PREDIO_A_SUBDIVIDIR_CAMPO_ID || campoId == SUPERFICIE_FOLIOS_A_FUSIONAR_CAMPO_ID || campoId == PREDIO_POR_FUSIONAR_CAMPO_ID || campoId == SUPERFICIE_A_SUBDIVIDIR_CAMPO_ID) {
				FraccionVO fraccionVO = entrada.get(index);
				if (fraccionVO == null) {
					fraccionVO = new FraccionVO();
					entrada.put(index, fraccionVO);
				}			
				
				if (campoId == PREDIO_A_SUBDIVIDIR_CAMPO_ID || campoId == PREDIO_POR_FUSIONAR_CAMPO_ID) {
					fraccionVO.predioId = Long.valueOf(actoPredioCampo.getValor());			
				} else if (campoId == SUPERFICIE_FOLIOS_A_FUSIONAR_CAMPO_ID || campoId == SUPERFICIE_A_SUBDIVIDIR_CAMPO_ID){
					fraccionVO.superficie = Double.parseDouble(actoPredioCampo.getValor());
				}
			}

		});
		
		entrada.forEach((index, fraccionVO) -> {
			log.debug("entrada predioId= {}, superficie= {}", fraccionVO.predioId ,fraccionVO.superficie);
		});
		
		salida.forEach((index, fraccionVO) -> {
			log.debug("salida predioId= {}, superficie= {}", fraccionVO.predioId ,fraccionVO.superficie);
		});

		
		List<HashMap<Integer, FraccionVO>> result = new ArrayList<HashMap<Integer, FraccionVO>>();
		result.add(entrada);
		result.add(salida);
		return result;

	}
	

	public void copyActo(Acto source, Acto target) {
		target.setTipoActo(source.getTipoActo());
		target.setPrelacion(source.getPrelacion());
		target.setFechaRegistro(source.getFechaRegistro());
		target.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		target.setOrden(source.getOrden());
		target.setArchivado(source.getArchivado());
		target.setPathArchivado(source.getPathArchivado());
		target.setVersion(source.getVersion());
		target.setId_entrada(source.getId_entrada());
		target.setId_aaj(source.getId_aaj());
		target.setIdAsiento(source.getIdAsiento());
		target.setContenidoXml(source.getContenidoXml());
		target.setHist(source.getHist());
		//target.setDocumentoDesc(source.getDocumentoDesc());
		if (source.isProcesado() != null) {
			target.setProcesado(source.isProcesado());
		}	
	}

	public void copyActoPredio(ActoPredio source, ActoPredio target) {
		target.setPredio(source.getPredio());	
		target.setVersion(source.getVersion());
		target.setTipoFolio(source.getTipoFolio());	
		target.setTipoEntrada(source.getTipoEntrada());
	}		

	public void copyPredioPersona(PredioPersona source, PredioPersona target) {
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
	
	/*@Transactional
	public Acto cloneActo(Acto acto, Prelacion newPrelacion, Predio newPredio, Acto newActoParaActoPredioMonto) {
		log.debug("acto= {}", acto);
		Acto newActo = new Acto();
		this.copyActo(acto, newActo);
		newActo.setProcesado(true);
		if (newPrelacion != null) {
			newActo.setPrelacion(newPrelacion);
		}
		
		actoRepository.save(newActo);
		actoFirmaRepository.save(newActo.getActoFirmasParaActos());
		
		for (ActoPredio actoPredio : acto.getActoPrediosParaActos()) {
			ActoPredio newActoPredio = new ActoPredio();
			this.copyActoPredio(actoPredio, newActoPredio);
			if (newPredio != null) {
				newActoPredio.setPredio(newPredio);
			}
			newActoPredio.setActo(newActo);
			newActoPredio.setTipoEntrada(actoPredio.getTipoEntrada());
			
			actoPredioRepository.save(newActoPredio);
			
			actoPredioCampoRepository.save(newActoPredio.getActoPredioCamposParaActoPredios());	
			
			if (newActoPredio.getActoPredioMontosParaActoPredios() != null) {
				for (ActoPredioMonto actoPredioMonto :  newActoPredio.getActoPredioMontosParaActoPredios()) {
					actoPredioMonto.setActo(newActoParaActoPredioMonto);
					actoPredioMontoRepository.save(actoPredioMonto);
				}
				
			}
			
		}
		
		return newActo;
	}
	*/
	
	public void copyActoDocumento(ActoDocumento source, ActoDocumento target) {
		target.setDocumento(source.getDocumento());
		target.setVersion(source.getVersion());		
	}
	
	public void copyActoReferences(Acto source, Acto target, boolean commit) {
		Set<ActoFirma> actosFirma = null;
		List<ActoFirma> _sourceActosFirma =  actoFirmaRepository.findAllByActoIdAndEsActo(source.getId(),true);
		List<ActoDocumento> _sourceActoDocumento = actoDocumentoRepository.getAllActoDocumentoByActoId(source.getId());
		for (ActoFirma actoFirma : _sourceActosFirma) {
			if (actosFirma == null) {
				actosFirma = new HashSet<ActoFirma>();
			}
			
			ActoFirma newActoFirma = new ActoFirma();
			BeanUtils.copyProperties(actoFirma, newActoFirma);
			newActoFirma.setId(null);
			newActoFirma.setActo(target);
			
			actosFirma.add(newActoFirma);
		}
		
		if (actosFirma != null) {
			target.setActoFirmasParaActos(actosFirma);
		}
		
		Set<ActoDocumento> actosDocumento = null;
		for (ActoDocumento actoDocumento : _sourceActoDocumento) {
			if (actosDocumento == null) {
				actosDocumento = new HashSet<ActoDocumento>();
			}
			
			ActoDocumento newActoDocumento = new ActoDocumento();
			this.copyActoDocumento(actoDocumento, newActoDocumento);
			newActoDocumento.setId(null);
			newActoDocumento.setActo(target);
			
			actosDocumento.add(newActoDocumento);
		}
		
		if (actosDocumento != null) {
			target.setActoDocumentosParaActos(actosDocumento);
		}
		
		if (commit) {
			actoFirmaRepository.save(target.getActoFirmasParaActos());
			actoDocumentoRepository.save(target.getActoDocumentosParaActos());
		}
				
	}
	
	/**
	 * copia actoPredioCampo, actoPredioMontos
	 * @param source
	 * @param target
	 * @param newActoParaActoPredioMonto
	 */
	public void copyActoPredioReferences(ActoPredio source, ActoPredio target, Acto newActoParaActoPredioMonto, boolean commit) {
		Set<ActoPredioCampo> newActoPredioCampos = null;
		for (ActoPredioCampo actoPredioCampo :  source.getActoPredioCamposParaActoPredios()) {
			if (newActoPredioCampos == null) {
				newActoPredioCampos = new HashSet<ActoPredioCampo>();
			}
			
			ActoPredioCampo newActoPredioCampo = new ActoPredioCampo();
			newActoPredioCampo.setActoPredio(target);
			newActoPredioCampo.setModuloCampo(actoPredioCampo.getModuloCampo());
			newActoPredioCampo.setOrden(actoPredioCampo.getOrden());
			newActoPredioCampo.setValor(actoPredioCampo.getValor());
			
			newActoPredioCampos.add(newActoPredioCampo);
		}
		
		if (newActoPredioCampos != null) {
			target.setActoPredioCamposParaActoPredios(newActoPredioCampos);
		}	
		
		Set<ActoPredioMonto> newActoPredioMontos = null;
		for (ActoPredioMonto actoPredioMonto :  source.getActoPredioMontosParaActoPredios()) {
			if (newActoPredioMontos == null) {
				newActoPredioMontos = new HashSet<ActoPredioMonto>();
			}
			
			ActoPredioMonto newActoPredioMonto = new ActoPredioMonto();
			newActoPredioMonto.setActoPredio(target);
			if ( newActoParaActoPredioMonto !=null) {
				newActoPredioMonto.setActo(newActoParaActoPredioMonto);
			} else {
				newActoPredioMonto.setActo(actoPredioMonto.getActo());
			}
			newActoPredioMonto.setMonto(actoPredioMonto.getMonto());
			newActoPredioMonto.setPorcentaje(actoPredioMonto.getPorcentaje());
			newActoPredioMonto.setTipoMoneda(actoPredioMonto.getTipoMoneda());
			
			newActoPredioMontos.add(newActoPredioMonto);
		}
		
		if (newActoPredioMontos != null) {
			target.setActoPredioMontosParaActoPredios(newActoPredioMontos);
		}
		
		if (commit) {
			actoPredioCampoRepository.save(target.getActoPredioCamposParaActoPredios());	
			actoPredioMontoRepository.save(target.getActoPredioMontosParaActoPredios());
		}
			
	}
	
	
	public Acto cloneActo(Acto acto, Prelacion newPrelacion, Predio newPredio, Acto newActoParaActoPredioMonto,Acto actoContext) {
		log.debug("acto= {}", acto);
		Acto newActo = new Acto();
		this.copyActo(acto, newActo);
		newActo.setProcesado(true);
		newActo.setClon(true);
		if (newPrelacion != null) {
			newActo.setPrelacion(newPrelacion);
		}
		
		actoRepository.save(newActo);
		this.copyActoReferences(acto, newActo, true);
		//CONTROL HEREDA ACTOS
		if(actoContext!=null)
		 this.actoControlHeredaService.save(actoContext, newActo);
		
		for (ActoPredio actoPredio : acto.getActoPrediosParaActos()) {
			ActoPredio newActoPredio = new ActoPredio();
			this.copyActoPredio(actoPredio, newActoPredio);
			if (newPredio != null) {
				newActoPredio.setPredio(newPredio);
			}
			newActoPredio.setActo(newActo);
			newActoPredio.setTipoEntrada(actoPredio.getTipoEntrada());
			
			actoPredioRepository.save(newActoPredio);
			
			this.copyActoPredioReferences(actoPredio, newActoPredio, newActoParaActoPredioMonto, true);
			
		}
		
		return newActo;
	}
	
	public Boolean getBooleanValor(ActoPredioCampo  actoPredioCampo) {
		return actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
				? "true".equals(actoPredioCampo.getValor().toLowerCase())
				: false;
	}


}
