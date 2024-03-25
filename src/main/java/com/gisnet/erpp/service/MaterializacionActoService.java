package com.gisnet.erpp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.autoconfigure.RemoteDevToolsProperties.Debug;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoControlHereda;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.domain.ActoPublicitario;
import com.gisnet.erpp.domain.ActoRel;
import com.gisnet.erpp.domain.AvisoCanceladoPorActo;
import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.PredioPersonaControl;
import com.gisnet.erpp.domain.PredioRel;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.Regimen;
import com.gisnet.erpp.domain.StatusActo;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioMontoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoPublicitarioRepository;
import com.gisnet.erpp.repository.ActoRelRepository;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.AvisoCanceladoPorActoRepository;
import com.gisnet.erpp.repository.AvisoRepository;
import com.gisnet.erpp.repository.BitacoraRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.EstadoCivilRepository;
import com.gisnet.erpp.repository.FolioSeccionAuxiliarRepository;
import com.gisnet.erpp.repository.MuebleRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.PasesRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PredioPersonaControlRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.PredioRelRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.PrelacionContratanteRepository;
import com.gisnet.erpp.repository.RegimenRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.service.excepciones.PasesException;
import com.gisnet.erpp.service.materializacion.ActoPorcentajeVO;
import com.gisnet.erpp.service.materializacion.ActoUtilService;
import com.gisnet.erpp.service.materializacion.MaterializacionActoCancelacionService;
import com.gisnet.erpp.service.materializacion.MaterializacionActoGravamenService;
import com.gisnet.erpp.service.materializacion.MaterializacionActoModificatorioService;
import com.gisnet.erpp.service.materializacion.MaterializacionActoPersonaJuridicaV1_1Service;
import com.gisnet.erpp.service.materializacion.MaterializacionActoPublicitarioService;
import com.gisnet.erpp.service.materializacion.MaterializacionActoTraslativoService;
import com.gisnet.erpp.service.materializacion.MaterializacionRecursoInconformidadService;
import com.gisnet.erpp.service.materializacion.PredioPersonaConPorcentajes;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.repository.TipoMonedaRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.vo.caratula.PaseVO;
import com.gisnet.erpp.web.api.aviso.AvisoDTO;

@Service
public class MaterializacionActoService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String FECHA_FORMATO_CAMPO_VALOR = "yyyy-MM-dd";
	// campo_ids utilizados, los tipos de campo ids se encuentran en la clase
	// Constantes.TipoCampo
	private final long NOMBRE_CAMPO_ID = 286;
	private final long PATERNO_CAMPO_ID = 287;
	private final long MATERNO_CAMPO_ID = 288;
	private final long FECHA_NACIMIENTO_CAMPO_ID = 289;
	private final long RFC_CAMPO_ID = 290;
	private final long CURP_CAMPO_ID = 291;
	private final long ESTADO_CIVIL_CAMPO_ID = 292;
	private final long REGIMEN_CAMPO_ID = 293;
	private final long NACIONALIDAD_CAMPO_ID = 119;
	private final long TIPO_DE_PERSONA_CAMPO_ID = 84;
	private final long DESIGNO_BENEFICIARIO_CAMPO_ID = 62;
	private final long DD_CAMPO_ID = 55;
	private final long UV_CAMPO_ID = 56;
	private final long SUPERFICIE_CAMPO_ID = 1055;

	private final long MONTO_DEL_CREDITO_CAMPO_ID = 72;
	private final long PORCENTAJE_A_CANCELAR_CAMPO_ID = 263;
	private final long ACTO_SELECCIONADO_CAMPO_ID = 261;
	private final long TIPO_DE_MONEDA_CAMPO_ID = 265;
	private final long MODULO_TITULARES_ID = 10;
	private final long MODULO_ADQUIRIENTES_ID = 172;
	//ACTOS PUBLICITARIOS

	
	private final long TRASLADA_DOMONIO_CAMPO_ID = 5063;
	
	
	private  Set<ActoPredioCampo> actoPredioCampo2; 
	private  Set<ActoPredioCampo> actoPredioCampo3;
	@Autowired
	PrelacionService prelacionService;

	@Autowired
	PasesService pasesService;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	ActoService actoService;

	@Autowired
	AvisoRepository avisoRepository;

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
	AvisoCanceladoPorActoRepository avisoCanceladoPorActoRepository;

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
	
	@Autowired
	PredioService predioService;
	
	@Autowired
	TipoFolioRepository tipoFolioRepository;
	
	@Autowired
	TipoEntradaRepository tipoEntradaRepository;
	
	@Autowired
	ActoPredioService actoPredioService;
	
	@Autowired
	PredioPersonaService predioPersonaService;
	
	@Autowired
	PredioRelRepository predioRelRepository;	

	@Autowired
	PrelacionContratanteRepository prelacionContratanteRepository;
	
	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	StatusRepository statusService;

	@Autowired
    PrelacionRepository prelacionRepository;
	
	@Autowired
	BitacoraRepository bitacoraRepository;

	@Autowired
	MuebleRepository muebleRepository;
	
	@Autowired
	PersonaJuridicaRepository personaJuridicaRepository;
	
	@Autowired
	FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;

	@Autowired
	MaterializacionActoPersonaJuridicaService materializacionActoPersonaJuridicaService;
	
	@Autowired
	MaterializacionActoTraslativoService materializacionActoTraslativoService;
	
	@Autowired
	MaterializacionActoModificatorioService materializacionActoModificatorioService;
	
	@Autowired
	MaterializacionActoGravamenService materializacionActoGravamenService;	
	
	@Autowired
	MaterializacionActoCancelacionService materializacionActoCancelacionService;

	@Autowired
	ActoFirmaRepository actoFirmaRepository;
	
	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;
	
	@Autowired
	ActoControlHeredaService actoControlHeredaService;
	
	@Autowired
	ActoUtilService actoUtilService;
	
	@Autowired
	AvisoService avisoService;

	@Autowired
	PrelacionPredioService ppService;

	@Autowired
	MaterializacionActoPersonaJuridicaV1_1Service materializacionActoPersonaJuridicaV1_1Service;
	
	@Autowired
	CaratulaService caratulaService;
	
	@Autowired
	PasesRepository pasesRepository;
	
	@Autowired
	UsuarioService usuarioService;

	@Autowired
	PredioPersonaControlRepository predioPersonaControlRepository;
	
	@Autowired
	MaterializacionRecursoInconformidadService materializacionRecursoInconformidadService;
	
	@Autowired
	MaterializacionActoPublicitarioService materializacionActoPublicitarioService; 
	
	@Transactional
	public void desMaterializarPredio(Long prelacionId) {
		List<ActoPredio> actos = prelacionService.findAllActoPredioByPrelacionAndVfFalse(prelacionId);

		for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
			ActoPredio actoPredio = (ActoPredio) iterator.next();			
			predioPersonaRepository.deleteByActoPredioId(actoPredio.getId());
		}

	}

	@Transactional
	public void materializaActo(Acto acto) {
		ActoPredio _actoPredio = actoPredioRepository.findAllByActoAndVf(acto.getId(), false,
				Constantes.TipoEntrada.ENTRADA);
		if(_actoPredio!=null) {
			Acto _acto =  _actoPredio.getActo();
			//SI EL ESTATUS NO ES RECHAZO
			if (!_acto.getStatusActo().getId().equals(Constantes.StatusActo.RECHAZADO.getIdStatusActo())) {
				if (_acto.getTipoActo().getId().equals(Constantes.FUSION_TIPO_ACTO_ID)) {
						//obtenemos todos los actos fusión
					List<ActoPredio> actos = prelacionService
							.findAllActosFusionByPrelacionAndVfFalse(_acto.getPrelacion().getId());
						materializacionActoModificatorioService.materializaFusion(_acto.getPrelacion().getId(), actos);						
				} else {
						 materializar(_acto);
					}
			}
			 actualizarCaratulaActualizada(_actoPredio);
		}
	}
	
	@Transactional
	public void materializarPrelacion(Long prelacionId) {
		List<ActoPredio> actos = prelacionService.findAllActoPredioByPrelacionAndVfFalse(prelacionId);
		
		boolean materializaFusion = false;
		boolean materializaPermuta = false;
		
		for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
			ActoPredio actoPredio = (ActoPredio) iterator.next();
		
			if (actoPredio.getActo().getTipoActo().getId() == Constantes.FUSION_TIPO_ACTO_ID) {
				materializaFusion = true;
			} else if (actoPredio.getActo().getTipoActo().getId() == Constantes.PERMUTA_TIPO_ACTO_ID) {
				materializaPermuta = true;
			}
		
		}
		
		if (materializaFusion) {
			materializacionActoModificatorioService.materializaFusion(prelacionId, actos);
		} else if (materializaPermuta) {
			materializacionActoTraslativoService.materializaPermuta(prelacionId, actos);
		} else {	
			for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
				ActoPredio actoPredio = (ActoPredio) iterator.next();
				if(actoPredio.getActo().getStatusActo().getId() != Constantes.StatusActo.RECHAZADO.getIdStatusActo()) {
					materializar(actoPredio.getActo());
					log.debug("Materializacion de acto {} terminada",actoPredio.getActo().getId());
				} else{
					log.debug("No se realizo materializacion del acto {} por que tiene status rechazado",
							actoPredio.getActo().getId());
				}
			}
			
			ConfigDacionPagoGravamenes(actos);
		}
		log.debug("Materializacion de prelacion {} terminada", prelacionId);
	}
	
	@Transactional
	public void updateStatusFinalPrelacion(Prelacion prel) {
		com.gisnet.erpp.domain.Status stus =  null; 
		List<ActoPredio> actos = prelacionService.findAllActoPredioByPrelacionAndVfFalse(prel.getId());
		boolean rechazo = actos != null && actos.size() > 0 ? actos.get(0).getActo().getStatusActo().getId()
				.equals(Constantes.StatusActo.RECHAZADO.getIdStatusActo()) : false;
		if(!rechazo) {
			if(prel.getTipoEntrega()==1L) {
	    		stus = statusService.findOne(7L); 
	    		prel.status(stus);
	    		prelacionRepository.save(prel); 
	    		bitacoraRepository.save(prelacionService.createBitacora(prel));
	    	}else if(prel.getTipoEntrega()== 2L) {
	    		stus = statusService.findOne(8L); 
	    		prel.status(stus);
	    		prelacionRepository.save(prel); 
                bitacoraRepository.save(prelacionService.createBitacora(prel));
	    	}
		} else {
		
			stus = statusService.findOne(10L); 
			prel.setStatus(stus);
			prelacionRepository.save(prel); 
			bitacoraRepository.save(prelacionService.createBitacora(prel)); 
		}
	}

	private void actualizarCaratulaActualizada(ActoPredio actoPredio) {
		if (actoPredio.getTipoFolio()!=null) {
			Constantes.ETipoFolio tipoFolio = Constantes.ETipoFolio.fromLong(actoPredio.getTipoFolio().getId());
			
			switch (tipoFolio) {
			case PREDIO:
				if(actoPredio.getPredio()!=null) {
					Predio predio = predioRepository.findById(actoPredio.getPredio().getId());
					//if (predio.isPrimerRegistro()) {
						predio.setCaratulaActualizada(true);
						predioRepository.save(predio);
					//}
				}
				
				break;
			case MUEBLE:
				Mueble mueble = actoPredio.getMueble();
				//if (mueble.isPrimerRegistro()) {
					mueble.setCaratulaActualizada(true);
					muebleRepository.save(mueble);
				//}
				break;
			case PERSONAS_JURIDICAS:
				if(actoPredio.getPersonaJuridica()!=null) {
					PersonaJuridica personaJuridica = actoPredio.getPersonaJuridica();
					//if ( personaJuridica.isPrimerRegistro()) {
						personaJuridica.setCaratulaActualizada(true);
						personaJuridicaRepository.save(personaJuridica);
					//}
				}
			
				break;
			case PERSONAS_AUXILIARES:
				FolioSeccionAuxiliar folioSeccionAuxiliar = actoPredio.getFolioSeccionAuxiliar();
				//if (folioSeccionAuxiliar.isPrimerRegistro()) {
					folioSeccionAuxiliar.setCaratulaActualizada(true);
					folioSeccionAuxiliarRepository.save(folioSeccionAuxiliar);
				//}
				break;
			}
		}
		
	}
	
	//Quita todos los gravamenes activos al momento de realizar el acto de dacion
	public void ConfigDacionPagoGravamenes(List<ActoPredio> actos) {
		
		Predio predio = new Predio();
		
		for (Iterator iterator = actos.iterator(); iterator.hasNext();) {
			ActoPredio actoPredio = (ActoPredio) iterator.next();
			
			if (actoPredio.getActo().getTipoActo().getId() == 18) {
				
				predio = actoPredio.getPredio();
		
			    Set<ActoPredio> listActoPredio =	predio.getActoPrediosParaPredios();
				
				for (ActoPredio item : listActoPredio) {
					
					if(item.getActo().getTipoActo()!= null) {				
						
						System.out.println(item.getActo().getTipoActo().getClasifActo().getId().longValue());
						System.out.println(item.getActo().getTipoActo().getClasifActo().getNombre());
						System.out.println(item.getActo().getStatusActo().getId().longValue());
						System.out.println(item.getActo().getStatusActo().getNombre());
						
						if (item.getActo().getTipoActo().getClasifActo().getId().longValue() == 2L // acto es
																									// clasificacion
																									// gravamen
																									// limitativo
								&& (item.getActo().getStatusActo().getId().longValue() == 1L // acto tiene estatus
																								// activo
										|| item.getActo().getStatusActo().getId().longValue() == 2L // o acto tiene
																									// estatus en
																									// proceso
										|| item.getActo().getStatusActo().getId().longValue() == 3L) // o acto tiene
																										// estatus
																										// temporal
						) {
							StatusActo statusActo = statusActoRepository.findOne(4L);
							
							item.getActo().setStatusActo(statusActo);
							
							actoRepository.save(item.getActo());
							
						}
					}
					
				}
			    
			} 
			
		}
		
	}

	@Transactional
	public void deMaterializar(Acto acto) {
		Constantes.ClasifActo clasifActo = Constantes.ClasifActo
				.getClasifActo(acto.getTipoActo().getClasifActo().getId());
		log.debug("deMaterizando acto={}", acto);
		
		if (clasifActo != null) {
			switch (clasifActo) {
			case TRASLATIVOS:
				deMaterializarTraslativo(acto,false);
				break;
			case ACTOS_MODIFICATORIOS_DEL_INMUEBLE:
				deMaterializaModfificatorioInmueble(acto);
				break;
			case GRAMAVAMES_LIMITATANES:
				deMaterializarGravamenes(acto);
				break;
			case CANCELACIONES:
				deMaterializarCancelacion(acto);
				break;
			default:
				deMaterializacionDefault(acto);
			}
		} else {
			log.warn("No existe dematerializacion para la clasificacion= null");
		}
	}
	
	public void deMaterializarNew(Acto acto) {
		Constantes.ClasifActo clasifActo = Constantes.ClasifActo
				.getClasifActo(acto.getTipoActo().getClasifActo().getId());
		log.debug("Materizando acto={}", acto);
		System.out.println("clasifActo--"+clasifActo);
		if (acto.getStatusActo().getId().equals(Constantes.StatusActo.RECHAZADO.getIdStatusActo())) {
			this.deMaterializacionDefault(acto);
			return;
		}
		switch (acto.getTipoActo().getId().intValue()) {
			case 41:  //FUSION
				materializacionActoModificatorioService.deMaterializaModfificatorioInmueble(acto);
				break;
			case 40:  //FRACCIONAMIENTO
			case 39:  // CONDOMINIO
			case 245: // LOTIFICACION
			case 246: // RELOTIFICACION
				materializacionActoModificatorioService.deMaterializarModificatoriosSP(acto);
			break;
			case 43://MODIFICACION DE MEDIDAS Y LINDEROS
				materializacionActoModificatorioService.deMaterializarRectificacionMedidas(acto);
				break;
			case 80:
			case 110: // CONSTITUCION_PERSONA_JURIDICA
				materializacionActoPersonaJuridicaV1_1Service.deMaterializaConstitucionPersonaJuridica(acto);
			break;
			case 219: //USUFRUCTO
			case 223: //RESERVA DE DOMINIO
				deMaterializarUsufructo(acto,false);
			break;
			case 25://FIDEICOMISO
			case 27://FIDEICOMISO
				deMaterializarTraslativo(acto,false);
				break;
			case 210:
				deMaterializacionAviso(acto);				
				break;
			case 70:
			case 249:
				materializacionRecursoInconformidadService.deMaterializaRecursoInconformidad(acto);
				break;
			case 4:
				materializacionActoPublicitarioService.deMaterializaCancelacion(acto);
				break;
			default:
				log.info("materializarPorClasifcacionActo(");
				if(clasifActo!=null){
					switch (clasifActo) {
						case TRASLATIVOS:
							deMaterializarTraslativo(acto,false);
							break;
						case ACTOS_MODIFICATORIOS_DEL_INMUEBLE:
							materializacionActoModificatorioService.deMaterializaModfificatorioInmueble(acto);
							break;
						case GRAMAVAMES_LIMITATANES:
						case REGISTRO_POR_ORDENAMIENTO_DE_AUTORIDAD:
							deMaterializarGravamenes(acto);
							break;
						case CANCELACIONES:
							deMaterializarCancelacion(acto);
							break;
						case ANOTACIONES:
						case AVISOS:
							deMaterializacionAviso(acto);
							break;
						case ACTOS_PUBLICITARIOS:
							deMaterializacionDefault(acto);
							materializacionActoPublicitarioService.deMaterializaActosPublicitarios(acto);
						break;
						default:
							deMaterializacionDefault(acto);
						}

				}
			break;
		}
		this.deMaterializacionDefault(acto);
	}
	
	private void deMaterializacionDefault(Acto acto) {
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
	}
	
	
	@Transactional
	public void deMaterializarTraslativo(Acto acto,boolean isCancelacion) {		
		log.debug("Iniciando dematerializacion del acto = {}", acto);
	
		List<ActoRel> relaciones = actoRelRepository.findByActoId(acto.getId());
	
		//CONTROL TITULARES
		ActoPredio actoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId()).get();
		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		HashMap<Integer, PredioPersonaConPorcentajes> titulares = new HashMap<Integer, PredioPersonaConPorcentajes>();
		
		actoPredioCampos.forEach(actoPredioCampo->{
				int index = actoPredioCampo.getOrden();
				
				if (actoPredioCampo.getValor()!=null) {

					Constantes.TipoCampo tipoCampo = Constantes.TipoCampo
							.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
					if (tipoCampo == null) {
						log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}",
								actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
		
					} else {
						if (tipoCampo == tipoCampo.PERSONA_PREDIO_TITULAR
								|| tipoCampo == tipoCampo.DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR
								|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR
								|| tipoCampo == tipoCampo.SUPERFICE_A_TRANSMITIR || tipoCampo == tipoCampo.DOMINIO_DIRECTO
								|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO || tipoCampo == tipoCampo.ACTIVACION_TRANSMITENTE) {
							PredioPersonaConPorcentajes predioPersona = titulares.get(index);

							if (predioPersona == null) {
								predioPersona = new PredioPersonaConPorcentajes();
								titulares.put(index, predioPersona);
							}
		
							switch (tipoCampo) {
							case PERSONA_PREDIO_TITULAR:
									predioPersona.setPersona(
											personaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
								break;
							case DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR:
								predioPersona.setPorcentajeDd(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR:						
								predioPersona.setPorcentajeUv(
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case SUPERFICE_A_TRANSMITIR:
								predioPersona.setSuperficie(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Double.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case USUFRUCTO_VITALICIO:
								predioPersona.setPorcentajeUvAnterior(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case DOMINIO_DIRECTO:
								predioPersona.setPorcentajeDdAnterior(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case ACTIVACION_TRANSMITENTE:
								predioPersona.setTransmite(actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().isEmpty() ? "true".equals(actoPredioCampo.getValor().toLowerCase()) : false );
								break;
								
							}
						}
					}
					
				}
			
		});
		
		
		//ACTUALIZA TITULARES ACTUALES
		if (titulares!=null && titulares.size()>0) {
			List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(actoPredio.getPredio().getId(),false);
			List<PredioPersonaControl> ppExtintos =  predioPersonaControlRepository.findByActoId(acto.getId());
			for (Map.Entry<Integer, PredioPersonaConPorcentajes> entry : titulares.entrySet()) {
				PredioPersonaConPorcentajes  titular = entry.getValue();
				Optional<PredioPersona> pp = predioPersonas.stream().filter(x->x.getPersona().getId().equals(titular.getPersona().getId())).findAny();
				if(pp.isPresent()) {
						PredioPersona predioPersona = pp.get();
						predioPersona.setPorcentajeUv(titular.getPorcentajeUvAnterior());
						predioPersona.setPorcentajeDd(titular.getPorcentajeDdAnterior());
						
						if(predioPersona.getExtinto() != null && predioPersona.getExtinto() && 
								((predioPersona.getPorcentajeDd()!=null && predioPersona.getPorcentajeDd()>0) || (predioPersona.getPorcentajeUv()!=null && predioPersona.getPorcentajeUv()>0)) )
							predioPersona.setExtinto(null);
						  	
						predioPersonaRepository.save(predioPersona);
						if(!isCancelacion)
						  this.controlExtincionActosTitulares(predioPersona.getActoPredio());
				}
				
				//ACTOS EXTINTOS
				Optional<PredioPersonaControl> ppExt =  ppExtintos.stream().filter(x->x.getPredioPersona().getPersona().getId().equals(titular.getPersona().getId())).findAny();
				if(ppExt.isPresent()) {
					PredioPersona predioPersona = ppExt.get().getPredioPersona();
					predioPersona.setPorcentajeUv(titular.getPorcentajeUvAnterior());
					predioPersona.setPorcentajeDd(titular.getPorcentajeDdAnterior());
					
					if(predioPersona.getExtinto() != null && predioPersona.getExtinto() && 
							((predioPersona.getPorcentajeDd()!=null && predioPersona.getPorcentajeDd()>0) || (predioPersona.getPorcentajeUv()!=null && predioPersona.getPorcentajeUv()>0)) )
						predioPersona.setExtinto(null);
					  	
					predioPersonaRepository.save(predioPersona);
				   if(!isCancelacion)
					 this.controlExtincionActosTitulares(predioPersona.getActoPredio());
				}
				
			}
		}
		
		
		if(!isCancelacion) {
			
	  	 if(relaciones!=null && !relaciones.isEmpty()){
			for (ActoRel actoRel : relaciones) {
				Acto actoAnt = actoRel.getActoAnt();
				Acto actoSig = actoRel.getActoSig();
				
				if (actoAnt != null) {
					log.debug("cambiando a status activo el acto = {}", actoAnt);
					actoAnt.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
					actoAnt.setRemplazado(null);
					actoRepository.save(actoAnt);
				} else {
					actoSig.setStatusActo(
							statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
					actoRepository.save(actoSig);
					List<ActoPredio> salidas = actoPredioRepository.findAllByActoIdAndTipoEntrada(actoSig.getId(),
							Constantes.TipoEntrada.SALIDA);
					System.out.println("List<ActoPredio> "+relaciones.size());
					for (ActoPredio actoPredioSalida : salidas) {
						actoPredioSalida.setTipoEntrada(
								tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
						predioPersonaRepository.deleteByActoPredioId(actoPredioSalida.getId());
						actoPredioRepository.save(actoPredioSalida);
					}
					
				}
				
				if (actoSig != null && actoAnt != null) {
					actoService.deleteActo(actoSig.getId());
				}
				actoRelRepository.delete(actoRel);
			}
		}else{	
			//actoService.deleteActo(acto.getId());	
		} 		
		
		 predioPersonaControlRepository.deleteByActoId(acto.getId());
		 pasesRepository.deleteByActoId(acto.getId());
		 this.deHeredarActos(acto);
		}
	}

	/*@Transactional
	public void deMaterializarTraslativo(Acto acto) {		
		log.debug("Iniciando dematerializacion del acto = {}", acto);
	
		List<ActoRel> relaciones = actoRelRepository.findByActoId(acto.getId());
	
		System.out.println("List<ActoRel> "+relaciones.size());
		if(relaciones!=null && !relaciones.isEmpty()){
			for (ActoRel actoRel : relaciones) {
				Acto actoAnt = actoRel.getActoAnt();
				Acto actoSig = actoRel.getActoSig();
				
				if (actoAnt != null) {
					log.debug("cambiando a status activo el acto = {}", actoAnt);
					actoAnt.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
					actoAnt.setRemplazado(null);
					actoRepository.save(actoAnt);
				} else {
					actoSig.setStatusActo(
							statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
					actoRepository.save(actoSig);
					List<ActoPredio> salidas = actoPredioRepository.findAllByActoIdAndTipoEntrada(actoSig.getId(),
							Constantes.TipoEntrada.SALIDA);
					System.out.println("List<ActoPredio> "+relaciones.size());
					for (ActoPredio actoPredioSalida : salidas) {
						actoPredioSalida.setTipoEntrada(
								tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
						predioPersonaRepository.deleteByActoPredioId(actoPredioSalida.getId());
						actoPredioRepository.save(actoPredioSalida);
					}
					
				}
				
				// si actoAnt==null indica que no tiene acto anterior por lo tanto solo se
				// actualiza el status y el tipo de entrada en el if anterior
				if (actoSig != null && actoAnt != null) {
					actoService.deleteActo(actoSig.getId());
				}
				actoRelRepository.delete(actoRel);
			}
		}else{	
			actoService.deleteActo(acto.getId());	
		} 
		List<ActoPredio> aPredios =actoPredioRepository.findAllByActo(acto);
		
		
		
		pasesRepository.deleteByActoId(acto.getId());
		this.deHeredarActos(acto);
	}*/
	
	@Transactional 
	public void deMaterializaModfificatorioInmueble(Acto acto) {
		log.debug("Iniciando dematerializacion de acto modificatorio del inmueble del acto = {}", acto);
		
		log.debug("cambiando a temporal el acto = {}", acto);
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
		
		List<Predio> prediosSalida = new ArrayList<Predio>();
		
		/*
		 * borra relaciones acto y actos creados List<ActoRel> relaciones =
		 * actoRelRepository.findByActoId(acto.getId()); for (ActoRel actoRel :
		 * relaciones) { List<ActoPredio> actosPredioSalida =
		 * actoPredioRepository.findAllByActoIdAndTipoEntrada(actoRel.getActoSig().getId
		 * (), Constantes.TipoEntrada.SALIDA); for (ActoPredio actoPredioSalida :
		 * actosPredioSalida) { Predio predioSalida = actoPredioSalida.getPredio();
		 * 
		 * predioPersonaRepository.deleteByActoPredioId(actoPredioSalida.getId());
		 * 
		 * log.debug("cambiando a TEMPORAL el predio = {} ", predioSalida);
		 * predioSalida.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo
		 * .TEMPORAL.getIdStatusActo())); predioRepository.save(predioSalida);
		 * 
		 * prediosSalida.add(predioSalida); } actoRelRepository.delete(actoRel);
		 * actoService.deleteActo(actoRel.getActoSig().getId()); }
		 * 
		*/
		//regresa a ACTIVOS los predios fuente
		List<Predio> prediosSource = predioRepository.findDistinctByPredioRelesParaPrediosPredioSigIn(prediosSalida);
		
		for (Predio predioSource : prediosSource) {
			log.debug("cambiando a ACTIVO predio fuente = {}", predioSource);
			predioSource.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			predioRepository.save(predioSource);
		}

		//borra relacio predio
		predioRelRepository.deleteByPredioIn(prediosSource);
		predioRelRepository.deleteByPredioSigIn(prediosSalida);
		
	}
	
	@Transactional
	private void deMaterializarGravamenes(Acto acto) {
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
		
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
			
			actoPredioMontoRepository.deleteByActoPredio(actoPredio);
		}

	@Transactional
	public void materializar(Acto acto) {
		Constantes.ClasifActo clasifActo = Constantes.ClasifActo
				.getClasifActo(acto.getTipoActo().getClasifActo().getId());
		log.debug("Materizando acto={}", acto);
		System.out.println("clasifActo--"+clasifActo);
		switch (acto.getTipoActo().getId().intValue()) {
			case 40:  //FRACCIONAMIENTO
			case 39:  // CONDOMINIO
			case 245: // LOTIFICACION
			case 246: // RELOTIFICACION
				log.info("materializarFraccionamiento(");
				materializacionActoModificatorioService.materializarFraccionamiento(acto);
			break;
			case 210:
				materializacionAviso(acto);
				break;
			case 43://MODIFICACION DE MEDIDAS Y LINDEROS
				materializacionActoModificatorioService.materializaRectificacionMedidas(acto);
				break;
			case 80:
			case 110: // CONSTITUCION_PERSONA_JURIDICA
				materializacionActoPersonaJuridicaV1_1Service.materializaConstitucionPersonaJuridica(acto);
			break;
			case 219: //USUFRUCTO
			case 223:  //RESERVA DE DOMINIO
				materializarUsufructo(acto,false);
			break;
			case 25:
			case 27:
				materializaTraslativo(acto);
			break;
			case 70://RECURSO DE INCONFORMIDAD
			case 249:
				materializacionRecursoInconformidadService.materializaRecursoInconformidad(acto);
			break;
			case 4: //CANCELACION PUBLICITARIOS
				materializacionActoPublicitarioService.materializarActoPublicitario(acto);
				materializacionActoPublicitarioService.materializaCancelacion(acto);
			break;
			default:
				log.info("materializarPorClasifcacionActo(");
				if(clasifActo!=null){
					switch (clasifActo) {
						case TRASLATIVOS:
							materializaTraslativo(acto);
							break;
						case ACTOS_MODIFICATORIOS_DEL_INMUEBLE:
							materializacionActoModificatorioService.materializaModfificatorioInmueble(acto);
							break;
						case GRAMAVAMES_LIMITATANES:
						case REGISTRO_POR_ORDENAMIENTO_DE_AUTORIDAD:
							materializarGravamenes(acto);
							break;
						case CANCELACIONES:
							materializarCancelacion(acto);
							break;
						case ANOTACIONES:
							materializacionDefault(acto);
							break;
						case AVISOS:
							materializacionAviso(acto);
							break;
						case ACTOS_PUBLICITARIOS:
							materializacionDefault(acto);
							materializacionActoPublicitarioService.materializarActoPublicitario(acto);
						break;
						default:
							materializacionDefault(acto);
						}

				}
			break;
		}
	}
	
	

	
	
	private void materializarUsufructo(Acto acto,boolean isCancelacion) {
		ActoPredio actoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId()).get();
		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}
		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		Boolean[] traslada = {false};
		actoPredioCampos.forEach(x->{
			if(x.getModuloCampo().getCampo().getId().equals(TRASLADA_DOMONIO_CAMPO_ID)) {
				traslada[0] = x.getValor()!=null && !x.getValor().isEmpty() ? 
						     "true".equals(x.getValor().toLowerCase()) : false;
			}
		});
		
		if(traslada[0]) {
			
			HashMap<Integer, PredioPersona> adquitientes = new HashMap<Integer, PredioPersona>();
			HashMap<Integer, PredioPersonaConPorcentajes> titulares = new HashMap<Integer, PredioPersonaConPorcentajes>();
						
			actoPredioCampos.forEach((actoPredioCampo) -> {
				long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
				int index = actoPredioCampo.getOrden();
				long moduloId =  actoPredioCampo.getModuloCampo().getModulo().getId();
				
				if (actoPredioCampo.getValor()!=null) {

					if (campoId == NOMBRE_CAMPO_ID || campoId == PATERNO_CAMPO_ID || campoId == MATERNO_CAMPO_ID
							|| campoId == FECHA_NACIMIENTO_CAMPO_ID || campoId == RFC_CAMPO_ID || campoId == CURP_CAMPO_ID
							|| (campoId == ESTADO_CIVIL_CAMPO_ID  && moduloId==MODULO_ADQUIRIENTES_ID)
							|| (campoId == REGIMEN_CAMPO_ID && moduloId==MODULO_ADQUIRIENTES_ID)
							|| campoId == NACIONALIDAD_CAMPO_ID || campoId == TIPO_DE_PERSONA_CAMPO_ID
							|| campoId == DD_CAMPO_ID || campoId == UV_CAMPO_ID || campoId == SUPERFICIE_CAMPO_ID
							|| campoId == DESIGNO_BENEFICIARIO_CAMPO_ID) {
						PredioPersona predioPersona = adquitientes.get(index);
						if (predioPersona == null) {
							predioPersona = new PredioPersona();
							predioPersona.setPersona(new Persona());
							adquitientes.put(index, predioPersona);
						}
		
						if (campoId == NOMBRE_CAMPO_ID) {
							predioPersona.getPersona().setNombre(actoPredioCampo.getValor());
						} else if (campoId == PATERNO_CAMPO_ID) {
							predioPersona.getPersona().setPaterno(actoPredioCampo.getValor());
						} else if (campoId == MATERNO_CAMPO_ID) {
							predioPersona.getPersona().setMaterno(actoPredioCampo.getValor());
						} else if (campoId == FECHA_NACIMIENTO_CAMPO_ID) {
							predioPersona.getPersona()
									.setFechaNac(UtilFecha.toDate(actoPredioCampo.getValor(), FECHA_FORMATO_CAMPO_VALOR));
						} else if (campoId == RFC_CAMPO_ID) {
							predioPersona.getPersona().setRfc(actoPredioCampo.getValor());
						} else if (campoId == CURP_CAMPO_ID) {
							predioPersona.getPersona().setCurp(actoPredioCampo.getValor());
						} else if (campoId == ESTADO_CIVIL_CAMPO_ID && actoPredioCampo.getValor()!=null &&  !actoPredioCampo.getValor().isEmpty() ) {
							Optional<EstadoCivil> estadoCivil =  estadoCivilRepository.findOneByCampoValoresId(Long.valueOf(actoPredioCampo.getValor()));
							predioPersona.setEstadoCivil(estadoCivil.isPresent()? estadoCivil.get() : null);
						} else if (campoId == REGIMEN_CAMPO_ID && actoPredioCampo.getValor()!=null &&  !actoPredioCampo.getValor().isEmpty()) {
							 Optional<Regimen> regimen = regimenRepository.findOneByCampoValoresId(Long.valueOf(actoPredioCampo.getValor()));
							 predioPersona.setRegimen(regimen.isPresent() ?  regimen.get() : null);
						} else if (campoId == NACIONALIDAD_CAMPO_ID) {
							Nacionalidad nacionalidad = nacionalidadRepository
									.findOne(Long.valueOf(actoPredioCampo.getValor()));
							predioPersona.getPersona().setNacionalidad(nacionalidad);
							predioPersona.setNacionalidad(nacionalidad);
						} else if (campoId == TIPO_DE_PERSONA_CAMPO_ID) {
							predioPersona.getPersona()
									.setTipoPersona(tipoPersonaRepositor.findOne(Long.valueOf(actoPredioCampo.getValor())));
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
		
					Constantes.TipoCampo tipoCampo = Constantes.TipoCampo
							.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
					if (tipoCampo == null) {
						log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}",
								actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
		
					} else {
						if (tipoCampo == tipoCampo.PERSONA_PREDIO_TITULAR
								|| tipoCampo == tipoCampo.DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR
								|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR
								|| tipoCampo == tipoCampo.SUPERFICE_A_TRANSMITIR || tipoCampo == tipoCampo.DOMINIO_DIRECTO
								|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO || tipoCampo == tipoCampo.ACTIVACION_TRANSMITENTE) {
							PredioPersonaConPorcentajes predioPersona = titulares.get(index);

							if (predioPersona == null) {
								predioPersona = new PredioPersonaConPorcentajes();
								titulares.put(index, predioPersona);
							}
		
							switch (tipoCampo) {
							case PERSONA_PREDIO_TITULAR:
									predioPersona.setPersona(
											personaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
								break;
							case DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR:
								predioPersona.setPorcentajeDd(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR:						
								predioPersona.setPorcentajeUv(
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case SUPERFICE_A_TRANSMITIR:
								predioPersona.setSuperficie(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Double.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case USUFRUCTO_VITALICIO:
								predioPersona.setPorcentajeUvAnterior(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case DOMINIO_DIRECTO:
								predioPersona.setPorcentajeDdAnterior(								
											actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
													? Float.valueOf(actoPredioCampo.getValor())
													: 0);
								break;
							case ACTIVACION_TRANSMITENTE:
								predioPersona.setTransmite(actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().isEmpty() ? "true".equals(actoPredioCampo.getValor().toLowerCase()) : false );
								break;
								
							}
						}
					}
					
				}

			});

			
		//GUARDA USUFRUCTUARIOS
		if(!isCancelacion) { // SI NO ES CANCELACION
			
				ActoRel actoRel = new ActoRel();
				actoRel.setActo(acto);
				actoRel.setActoAnt(null);
				actoRel.setActoSig(acto);
				actoRelRepository.save(actoRel);
	
			for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
				PredioPersona predioPersona = entry.getValue();
				
				
	
				Optional<Persona> persona2 = null;
				if (predioPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.FISICA
						.getAsInteger().intValue() && predioPersona.getPersona().getCurp() != null
						&& predioPersona.getPersona().getCurp().length() >= 18) {
					persona2 = personaRepository.findByCurp(predioPersona.getPersona().getCurp());
				} else if (predioPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.MORAL
						.getAsInteger().intValue() && predioPersona.getPersona().getRfc() != null
						&& predioPersona.getPersona().getRfc().length() >= 12) {
					persona2 = personaRepository.findByRfc(predioPersona.getPersona().getRfc());
				}
				
				if (persona2!= null && persona2.isPresent()) {
					predioPersona.setPersona(persona2.get());
				} else {
					predioPersona.getPersona().setActivo(true);
					personaRepository.save(predioPersona.getPersona());
				}
	
				predioPersona.setTipoRelPersona(
						tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));
				predioPersona.setDireccion(direccionRepository.findOne(1L));
				predioPersona.setActoPredio(actoPredio);
				predioPersonaRepository.save(predioPersona);
			}
			
		}
			
		//ACTUALIZA TITULARES ACTUALES
		if (titulares!=null && titulares.size()>0) {
			List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(actoPredio.getPredio().getId(),
					false);
			for (Map.Entry<Integer, PredioPersonaConPorcentajes> entry : titulares.entrySet()) {
				PredioPersonaConPorcentajes  titular = entry.getValue();
				if(titular.getTransmite()!= null && titular.getTransmite()) {
					Optional<PredioPersona> pp = predioPersonas.stream().filter(x->x.getPersona().getId().equals(titular.getPersona().getId())).findAny();
					if(pp.isPresent()) {
						PredioPersona predioPersona = pp.get();
						float ddAnterior =  titular.getPorcentajeDdAnterior()!= null ? titular.getPorcentajeDdAnterior(): 0;
						float uvAnterior =  titular.getPorcentajeUvAnterior()!= null ? titular.getPorcentajeUvAnterior() : 0;
						float dd =  titular.getPorcentajeDd()!= null ? titular.getPorcentajeDd() : 0;
						float uv =  titular.getPorcentajeUv()!= null ? titular.getPorcentajeUv() : 0;
						
						predioPersona.setPorcentajeUv(uvAnterior - uv);
						predioPersona.setPorcentajeDd(ddAnterior - dd);
						
						if((predioPersona.getPorcentajeDd()==null || predioPersona.getPorcentajeDd()<=0) && (predioPersona.getPorcentajeUv()==null || predioPersona.getPorcentajeUv()<=0) )
						{ 
							predioPersona.setExtinto(true);
							 PredioPersonaControl ppc =  new PredioPersonaControl();
							 ppc.setActo(acto);
							 ppc.setPredioPersona(predioPersona);
							 predioPersonaControlRepository.save(ppc);
						}
						
						predioPersonaRepository.save(predioPersona);
						this.controlExtincionActosTitulares(predioPersona.getActoPredio());
					}
				}
			}
		}
			
			
			
			
		}
		
		
			actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
			actoPredioRepository.save(actoPredio);
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			actoRepository.save(acto);
	}
	
	private void deMaterializarUsufructo(Acto acto,boolean isCancelacion) {
		ActoPredio actoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId()).get();
		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}
		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();
		Boolean[] traslada = {false};

		HashMap<Integer, PredioPersonaConPorcentajes> titulares = new HashMap<Integer, PredioPersonaConPorcentajes>();
		
		actoPredioCampos.forEach(actoPredioCampo->{
			if(actoPredioCampo.getModuloCampo().getCampo().getId().equals(TRASLADA_DOMONIO_CAMPO_ID)) {
				traslada[0] = actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().isEmpty() ? 
						     "true".equals(actoPredioCampo.getValor().toLowerCase()) : false;
			}	
		});
		
		if(traslada[0]) {
			  deMaterializarTraslativo(acto,isCancelacion);
		}
	    
		if(!isCancelacion) {
			actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
			actoPredioRepository.save(actoPredio);
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
			actoRepository.save(acto);
		}
		
		
	}
	
	

	private void materializacionDefault(Acto acto) {
		System.out.println("aqui1111-11 "+acto+" "+Constantes.StatusActo.ACTIVO.getIdStatusActo());
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		actoRepository.save(acto);
	}
	
	@Transactional
	public void controlExtincionActosTitulares(ActoPredio actoPredio) {		
		Acto acto  = actoPredio.getActo();
		Set<PredioPersona> predioPersonas  = actoPredio.getPredioPersonasParaActoPredios();
		int totalPredioPersona =  predioPersonas !=null && predioPersonas.size()>0 ?  actoPredio.getPredioPersonasParaActoPredios().size() : 0;
		int totalExtintos =  predioPersonas !=null && predioPersonas.size()>0 ?  actoPredio.getPredioPersonasParaActoPredios().stream().filter(x->x.getExtinto()!=null && x.getExtinto()).collect(Collectors.toList()).size() : 0;
		if(totalPredioPersona == totalExtintos) 
		{	
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
		}else {
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		}
		actoRepository.save(acto);
	}
	
	
	private void materializaTraslativo(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId()).get();
		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}
		
		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, PredioPersona> adquitientes = new HashMap<Integer, PredioPersona>();
		HashMap<Integer, PredioPersonaConPorcentajes> titulares = new HashMap<Integer, PredioPersonaConPorcentajes>();
		
		Integer folio[] =  {0};
		
		actoPredioCampos.forEach((actoPredioCampo) -> {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
			long moduloId = actoPredioCampo.getModuloCampo().getModulo().getId();
			// actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();
			
			
			if (actoPredioCampo.getValor()!=null) {

				if (campoId == NOMBRE_CAMPO_ID || campoId == PATERNO_CAMPO_ID || campoId == MATERNO_CAMPO_ID
						|| campoId == FECHA_NACIMIENTO_CAMPO_ID || campoId == RFC_CAMPO_ID || campoId == CURP_CAMPO_ID
						|| (campoId == ESTADO_CIVIL_CAMPO_ID  && moduloId==MODULO_ADQUIRIENTES_ID)
						|| (campoId == REGIMEN_CAMPO_ID && moduloId==MODULO_ADQUIRIENTES_ID)
						|| campoId == NACIONALIDAD_CAMPO_ID || campoId == TIPO_DE_PERSONA_CAMPO_ID
						|| campoId == DD_CAMPO_ID || campoId == UV_CAMPO_ID || campoId == SUPERFICIE_CAMPO_ID
						|| campoId == DESIGNO_BENEFICIARIO_CAMPO_ID) {
					PredioPersona predioPersona = adquitientes.get(index);
					if (predioPersona == null) {
						predioPersona = new PredioPersona();
						predioPersona.setPersona(new Persona());
						adquitientes.put(index, predioPersona);
					}
	
					if (campoId == NOMBRE_CAMPO_ID) {
						predioPersona.getPersona().setNombre(actoPredioCampo.getValor());
					} else if (campoId == PATERNO_CAMPO_ID) {
						predioPersona.getPersona().setPaterno(actoPredioCampo.getValor());
					} else if (campoId == MATERNO_CAMPO_ID) {
						predioPersona.getPersona().setMaterno(actoPredioCampo.getValor());
					} else if (campoId == FECHA_NACIMIENTO_CAMPO_ID) {
						predioPersona.getPersona()
								.setFechaNac(UtilFecha.toDate(actoPredioCampo.getValor(), FECHA_FORMATO_CAMPO_VALOR));
					} else if (campoId == RFC_CAMPO_ID) {
						predioPersona.getPersona().setRfc(actoPredioCampo.getValor());
					} else if (campoId == CURP_CAMPO_ID) {
						predioPersona.getPersona().setCurp(actoPredioCampo.getValor());
					} else if (campoId == ESTADO_CIVIL_CAMPO_ID && actoPredioCampo.getValor()!=null &&  !actoPredioCampo.getValor().isEmpty() ) {
						Optional<EstadoCivil> estadoCivil =  estadoCivilRepository.findOneByCampoValoresId(Long.valueOf(actoPredioCampo.getValor()));
						predioPersona.setEstadoCivil(estadoCivil.isPresent()? estadoCivil.get() : null);
					} else if (campoId == REGIMEN_CAMPO_ID && actoPredioCampo.getValor()!=null &&  !actoPredioCampo.getValor().isEmpty()) {
						 Optional<Regimen> regimen = regimenRepository.findOneByCampoValoresId(Long.valueOf(actoPredioCampo.getValor()));
						 predioPersona.setRegimen(regimen.isPresent() ?  regimen.get() : null);
					} else if (campoId == NACIONALIDAD_CAMPO_ID) {
						Nacionalidad nacionalidad = nacionalidadRepository
								.findOne(Long.valueOf(actoPredioCampo.getValor()));
						predioPersona.getPersona().setNacionalidad(nacionalidad);
						predioPersona.setNacionalidad(nacionalidad);
					} else if (campoId == TIPO_DE_PERSONA_CAMPO_ID) {
						predioPersona.getPersona()
								.setTipoPersona(tipoPersonaRepositor.findOne(Long.valueOf(actoPredioCampo.getValor())));
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
	
				Constantes.TipoCampo tipoCampo = Constantes.TipoCampo
						.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
				if (tipoCampo == null) {
					log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}",
							actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
	
				} else {
					if (tipoCampo == tipoCampo.PERSONA_PREDIO_TITULAR
							|| tipoCampo == tipoCampo.DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR
							|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR
							|| tipoCampo == tipoCampo.SUPERFICE_A_TRANSMITIR || tipoCampo == tipoCampo.DOMINIO_DIRECTO
							|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO 
							|| (campoId == ESTADO_CIVIL_CAMPO_ID  && moduloId==MODULO_TITULARES_ID)
							|| (campoId == REGIMEN_CAMPO_ID && moduloId==MODULO_TITULARES_ID)) {
						PredioPersonaConPorcentajes predioPersona = titulares.get(index);

						if (predioPersona == null) {
							predioPersona = new PredioPersonaConPorcentajes();
							titulares.put(index, predioPersona);
						}
	
						if (campoId == ESTADO_CIVIL_CAMPO_ID && actoPredioCampo.getValor()!=null &&  !actoPredioCampo.getValor().isEmpty() ) {
							Optional<EstadoCivil> estadoCivil =  estadoCivilRepository.findOneByCampoValoresId(Long.valueOf(actoPredioCampo.getValor()));
							predioPersona.setEstadoCivil(estadoCivil.isPresent()? estadoCivil.get() : null);
						} else if (campoId == REGIMEN_CAMPO_ID && actoPredioCampo.getValor()!=null &&  !actoPredioCampo.getValor().isEmpty()) {
							 Optional<Regimen> regimen = regimenRepository.findOneByCampoValoresId(Long.valueOf(actoPredioCampo.getValor()));
							 predioPersona.setRegimen(regimen.isPresent() ?  regimen.get() : null);
						} 
						
						switch (tipoCampo) {
						case PERSONA_PREDIO_TITULAR:
								predioPersona.setPersona(
										personaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
							break;
						case DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR:
							predioPersona.setPorcentajeDd(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR:						
							predioPersona.setPorcentajeUv(
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case SUPERFICE_A_TRANSMITIR:
							predioPersona.setSuperficie(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Double.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case USUFRUCTO_VITALICIO:
							predioPersona.setPorcentajeUvAnterior(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case DOMINIO_DIRECTO:
							predioPersona.setPorcentajeDdAnterior(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
							
						case ACTIVACION_TRANSMITENTE:
							predioPersona.setTransmite(actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().isEmpty() ? "true".equals(actoPredioCampo.getValor().toLowerCase()) : false );
						break;
							
						}
					}
				}
				
				if(tipoCampo.getIdTipoCampo().equals(Constantes.TipoCampo.FOLIO_MATRIZ.getIdTipoCampo()) && 
						actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().trim().isEmpty()) {
					folio[0] =  Integer.parseInt(actoPredioCampo.getValor().trim());
				}
			}

		});


		//CONTROL TITULARES ANTERIORES
		if (titulares!=null && titulares.size()>0) {
			List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(actoPredio.getPredio().getId(),
					false);
			for (Map.Entry<Integer, PredioPersonaConPorcentajes> entry : titulares.entrySet()) {
				PredioPersonaConPorcentajes  titular = entry.getValue();
				
					Optional<PredioPersona> pp = predioPersonas.stream().filter(x->x.getPersona().getId().equals(titular.getPersona().getId())).findAny();
					if(pp.isPresent()) {
						PredioPersona predioPersona = pp.get();
						float ddAnterior =  titular.getPorcentajeDdAnterior()!= null ? titular.getPorcentajeDdAnterior(): 0;
						float uvAnterior =  titular.getPorcentajeUvAnterior()!= null ? titular.getPorcentajeUvAnterior() : 0;
						float dd =  titular.getPorcentajeDd()!= null ? titular.getPorcentajeDd() : 0;
						float uv =  titular.getPorcentajeUv()!= null ? titular.getPorcentajeUv() : 0;
						
						predioPersona.setPorcentajeUv(uvAnterior - uv);
						predioPersona.setPorcentajeDd(ddAnterior - dd);
						predioPersona.setEstadoCivil(titular.getEstadoCivil());
						predioPersona.setRegimen(titular.getRegimen());
						if((predioPersona.getPorcentajeDd()==null || predioPersona.getPorcentajeDd()<=0) && (predioPersona.getPorcentajeUv()==null || predioPersona.getPorcentajeUv()<=0) ) {
							 predioPersona.setExtinto(true);
							 PredioPersonaControl ppc =  new PredioPersonaControl();
							 ppc.setActo(acto);
							 ppc.setPredioPersona(predioPersona);
							 predioPersonaControlRepository.save(ppc);
						}
						
						predioPersonaRepository.save(predioPersona);
						this.controlExtincionActosTitulares(predioPersona.getActoPredio());
					}
			}
		}
		
		
		
		 // crear nuevos registros para adquirientes
		 acto.statusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		 acto.setFechaRegistro(new Date());
		 actoRepository.save(acto);
		
		 actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
		 actoPredioRepository.save(actoPredio);

			ActoRel actoRel = new ActoRel();
			actoRel.setActo(acto);
			actoRel.setActoAnt(null);
			actoRel.setActoSig(acto);
			actoRelRepository.save(actoRel);

		for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
			PredioPersona predioPersona = entry.getValue();
			
			// crear persona si no se encuentra por curp o rfc si es persona moral
			log.debug("Buscando una persona tipo persona= {}, con curp={} o rfc={} ",
					predioPersona.getPersona().getTipoPersona().getId(), predioPersona.getPersona().getCurp(),
					predioPersona.getPersona().getRfc());

			Optional<Persona> persona2 = null;
			if (predioPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.FISICA
					.getAsInteger().intValue() && predioPersona.getPersona().getCurp() != null
					&& predioPersona.getPersona().getCurp().length() >= 18) {
				persona2 = personaRepository.findByCurp(predioPersona.getPersona().getCurp());
			} else if (predioPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.MORAL
					.getAsInteger().intValue() && predioPersona.getPersona().getRfc() != null
					&& predioPersona.getPersona().getRfc().length() >= 12) {
				persona2 = personaRepository.findByRfc(predioPersona.getPersona().getRfc());
			}
			
			if (persona2!= null && persona2.isPresent()) {
				predioPersona.setPersona(persona2.get());
			} else {
				predioPersona.getPersona().setActivo(true);
				personaRepository.save(predioPersona.getPersona());
			}

			predioPersona.setTipoRelPersona(
					tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));
			predioPersona.setDireccion(direccionRepository.findOne(1L));
			predioPersona.setActoPredio(actoPredio);
			predioPersonaRepository.save(predioPersona);
		}
		
		
		
		
		
		//TRASLATIVO PARCIAL RELACION PREDIOS HEREDA ACTOS
		if (folio[0]!=null && folio[0]>0) {
			Integer _folio  = folio[0];
			Predio predio  = actoPredio.getPredio();
			Predio predioMatriz = predioRepository.findByNoFolio(_folio);
			if(predio != null && predioMatriz != null) {
				try {
					caratulaService.savePase(predioMatriz.getId(), predioMatriz.getOficina().getId(),
							predio.getNoFolio(), actoPredio.getActo());
					predio.setProcedeDeFolio(predioMatriz.getNoFolio().toString());
					predioRepository.save(predio);
					HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService
							.parseActos(actoPredioCampos);
					materializacionActoModificatorioService.saveActosSeleccionados(actosSeleccionados,
							acto.getPrelacion(), predio, acto);
				} catch (PasesException e) {
			        log.debug("ERROR MATERIALIZA PASE"+e.getMessage());
				}
		    }
		}

		Set<ActoPredio> actoPredios = new HashSet<ActoPredio>();
		actoPredios.add(actoPredio);
		actoPredio.setPredioPersonasParaActoPredios(new HashSet<PredioPersona>(adquitientes.values()));
		acto.setActoPrediosParaActos(actoPredios);
		
	}

	
	/*private void materializaTraslativo(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		System.out.println("TipoActo--"+acto.getTipoActo().getId());
		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}
		
		

		log.debug("actoPredio= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, PredioPersona> adquitientes = new HashMap<Integer, PredioPersona>();
		HashMap<Integer, PredioPersonaConPorcentajes> titulares = new HashMap<Integer, PredioPersonaConPorcentajes>();
		
		Integer folio[] =  {0};
		
		log.debug("total de actoPredioCampos para acto = {}, {}", actoPredioCampos.size(), actoPredioCampos);

		actoPredioCampos.forEach((actoPredioCampo) -> {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();

			// actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();
			
			log.debug("{} = {}", campoId, actoPredioCampo.getValor());
			
			if (actoPredioCampo.getValor()!=null) {

				if (campoId == NOMBRE_CAMPO_ID || campoId == PATERNO_CAMPO_ID || campoId == MATERNO_CAMPO_ID
						|| campoId == FECHA_NACIMIENTO_CAMPO_ID || campoId == RFC_CAMPO_ID || campoId == CURP_CAMPO_ID
						|| campoId == ESTADO_CIVIL_CAMPO_ID || campoId == REGIMEN_CAMPO_ID
						|| campoId == NACIONALIDAD_CAMPO_ID || campoId == TIPO_DE_PERSONA_CAMPO_ID
						|| campoId == DD_CAMPO_ID || campoId == UV_CAMPO_ID || campoId == SUPERFICIE_CAMPO_ID
						|| campoId == DESIGNO_BENEFICIARIO_CAMPO_ID) {
					PredioPersona predioPersona = adquitientes.get(index);
					if (predioPersona == null) {
						predioPersona = new PredioPersona();
						predioPersona.setPersona(new Persona());
						adquitientes.put(index, predioPersona);
					}
	
					if (campoId == NOMBRE_CAMPO_ID) {
						predioPersona.getPersona().setNombre(actoPredioCampo.getValor());
					} else if (campoId == PATERNO_CAMPO_ID) {
						predioPersona.getPersona().setPaterno(actoPredioCampo.getValor());
					} else if (campoId == MATERNO_CAMPO_ID) {
						predioPersona.getPersona().setMaterno(actoPredioCampo.getValor());
					} else if (campoId == FECHA_NACIMIENTO_CAMPO_ID) {
						predioPersona.getPersona()
								.setFechaNac(UtilFecha.toDate(actoPredioCampo.getValor(), FECHA_FORMATO_CAMPO_VALOR));
					} else if (campoId == RFC_CAMPO_ID) {
						predioPersona.getPersona().setRfc(actoPredioCampo.getValor());
					} else if (campoId == CURP_CAMPO_ID) {
						predioPersona.getPersona().setCurp(actoPredioCampo.getValor());
					} else if (campoId == ESTADO_CIVIL_CAMPO_ID) {
						predioPersona.setEstadoCivil(
								estadoCivilRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
					} else if (campoId == REGIMEN_CAMPO_ID) {
						if (actoPredioCampo.getValor()!=null && actoPredioCampo.getValor().length()>0){					
							predioPersona
									.setRegimen(regimenRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
						}
					} else if (campoId == NACIONALIDAD_CAMPO_ID) {
						Nacionalidad nacionalidad = nacionalidadRepository
								.findOne(Long.valueOf(actoPredioCampo.getValor()));
						predioPersona.getPersona().setNacionalidad(nacionalidad);
						predioPersona.setNacionalidad(nacionalidad);
					} else if (campoId == TIPO_DE_PERSONA_CAMPO_ID) {
						predioPersona.getPersona()
								.setTipoPersona(tipoPersonaRepositor.findOne(Long.valueOf(actoPredioCampo.getValor())));
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
	
				Constantes.TipoCampo tipoCampo = Constantes.TipoCampo
						.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
				if (tipoCampo == null) {
					log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}",
							actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
	
				} else {
					if (tipoCampo == tipoCampo.PERSONA_PREDIO_TITULAR
							|| tipoCampo == tipoCampo.DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR
							|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR
							|| tipoCampo == tipoCampo.SUPERFICE_A_TRANSMITIR || tipoCampo == tipoCampo.DOMINIO_DIRECTO
							|| tipoCampo == tipoCampo.USUFRUCTO_VITALICIO) {
						PredioPersonaConPorcentajes predioPersona = titulares.get(index);

						if (predioPersona == null) {
							predioPersona = new PredioPersonaConPorcentajes();
							titulares.put(index, predioPersona);
						}
	
						switch (tipoCampo) {
						case PERSONA_PREDIO_TITULAR:
								predioPersona.setPersona(
										personaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
							break;
						case DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR:
							predioPersona.setPorcentajeDd(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR:						
							predioPersona.setPorcentajeUv(
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case SUPERFICE_A_TRANSMITIR:
							predioPersona.setSuperficie(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Double.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case USUFRUCTO_VITALICIO:
							predioPersona.setPorcentajeUvAnterior(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
						case DOMINIO_DIRECTO:
							predioPersona.setPorcentajeDdAnterior(								
										actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().isEmpty()
												? Float.valueOf(actoPredioCampo.getValor())
												: 0);
							break;
							
						case ACTIVACION_TRANSMITENTE:
							predioPersona.setTransmite(actoPredioCampo.getValor()!=null && !actoPredioCampo.getValor().isEmpty() ? "true".equals(actoPredioCampo.getValor().toLowerCase()) : false );
						break;
							
						}
					}
				}
				
				if(tipoCampo.getIdTipoCampo().equals(Constantes.TipoCampo.FOLIO_MATRIZ.getIdTipoCampo())) {
					folio[0] =  Integer.parseInt(actoPredioCampo.getValor());
				}
			}

		});

		boolean tieneSuperfice= false;

		for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
			PredioPersona pp = (PredioPersona) entry.getValue();
			log.debug("adquirientes dd= {}, uv={}, superficie={}, nacionalidad{}, estado_civil{},, pesona={}",
					pp.getPorcentajeDd(), pp.getPorcentajeUv(), pp.getSuperficie(), pp.getPorcentajeUv(),
					pp.getNacionalidad(), pp.getEstadoCivil(), pp.getPersona());
			if (pp.getSuperficie()!=null) {
				tieneSuperfice=true;
			}
		}
	    
		log.debug("total de titulares={}", titulares.size());
		titulares.forEach((index, predioPersona) -> {
			log.debug("titulares dd= {}, uv={}, ddAnterior= {}, uvAnterior={}, superficie={}, pesona={}",
					predioPersona.getPorcentajeDd(), predioPersona.getPorcentajeUv(),
					predioPersona.getPorcentajeDdAnterior(), predioPersona.getPorcentajeUvAnterior(),
					predioPersona.getSuperficie(), predioPersona.getPersona());
		});
		
		double superficiePrevia = 0;
		if (tieneSuperfice) {
			List<PredioPersona> actuales = predioPersonaService.findPropietariosActuales(actoPredio.getPredio().getId(),
					false);
			for (Iterator iterator = actuales.iterator(); iterator.hasNext();) {
				PredioPersona pp = (PredioPersona) iterator.next();
				superficiePrevia+= pp.getSuperficie();
			}
			
		}
		
		//checar si se esta enajenando el porcentaje completo
		List<ActoPredio> actoPrediosTitulares = actoPredioRepository
				.findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaId(actoPredio.getPredio().getId(),
						Constantes.ETipoFolio.PREDIO, Constantes.StatusActo.ACTIVO, Constantes.TipoEntrada.SALIDA);
		List<ActoRel> relaciones = new ArrayList<ActoRel>();
		
		// Si no vienen titulares, significa que no estan en la forma y que el traslado
		// debe ser completo
		if (titulares.size()==0) {
			log.debug("No hay valores para titulares se trasladara todo el dominio completo");
			for (ActoPredio actoPredioTitular : actoPrediosTitulares) {
				for (PredioPersona oldPredioPersona : actoPredioTitular.getPredioPersonasParaActoPredios()) {
					int index = 1;
					PredioPersonaConPorcentajes predioPersona = new PredioPersonaConPorcentajes();
					predioPersona.setPersona(oldPredioPersona.getPersona());
					predioPersona.setPorcentajeDd(oldPredioPersona.getPorcentajeDd());
					predioPersona.setPorcentajeUv(oldPredioPersona.getPorcentajeUv());
					predioPersona.setPorcentajeDdAnterior(oldPredioPersona.getPorcentajeDd());
					predioPersona.setPorcentajeUvAnterior(oldPredioPersona.getPorcentajeUv());
					predioPersona.setSuperficie(oldPredioPersona.getSuperficie());
					titulares.put(index++, predioPersona);					
				}				
			}
		}
		
		List<Acto> nuevosActos = new ArrayList<Acto>();
		Set<Integer> indicesActualizados = new HashSet<Integer>();
		for (ActoPredio actoPredioTitular : actoPrediosTitulares) {
			Acto actoTitular = actoPredioTitular.getActo();
			actoTitular.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
			actoRepository.save(actoTitular);
			
			Acto newActo = new Acto();
			copyActo(actoTitular, newActo);
					
			this.copyActoReferences(actoTitular, newActo, false);
			nuevosActos.add(newActo);

			ActoPredio newActoPredio = new ActoPredio();

			copyActoPredio(actoPredioTitular, newActoPredio);
			newActoPredio.setActo(newActo);
			newActoPredio
					.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
				
			SortedSet<ActoPredio> nuevosActosPredios = new TreeSet<ActoPredio>();
			nuevosActosPredios.add(newActoPredio);
			newActo.setActoPredios(nuevosActosPredios);

			ActoRel actoRel = new ActoRel();
			actoRel.setActo(acto);
			actoRel.setActoAnt(actoTitular);
			System.out.println("AQUIIIIIIII "+newActo);
			actoRel.setActoSig(newActo);

			relaciones.add(actoRel);

			Set<PredioPersona> nuevosPredioPersona = new HashSet<PredioPersona>();
			newActoPredio.setPredioPersonasParaActoPredios(nuevosPredioPersona);
			//--
			for (PredioPersona oldPredioPersona : actoPredioTitular.getPredioPersonasParaActoPredios()) {
				for (Map.Entry<Integer, PredioPersonaConPorcentajes> entry : titulares.entrySet()) {
					PredioPersonaConPorcentajes pp = entry.getValue();
					if (oldPredioPersona.getPersona().getId() == pp.getPersona().getId()) {
						// checa que sea igual que el anterior, de lo contrario
						if (oldPredioPersona.getPorcentajeDd() == pp.getPorcentajeDdAnterior()
								&& oldPredioPersona.getPorcentajeUv() == pp.getPorcentajeUvAnterior()) {
							PredioPersona newPredioPersona = new PredioPersona();
							copyPredioPersona(oldPredioPersona, newPredioPersona);

							copyPredioPersona(oldPredioPersona, newPredioPersona);

							newPredioPersona.setPorcentajeDd(
									(pp.getPorcentajeDdAnterior() != null ? pp.getPorcentajeDdAnterior() : 0)
											- (oldPredioPersona.getPorcentajeDd() != null
													? oldPredioPersona.getPorcentajeDd()
													: 0));

							newPredioPersona.setPorcentajeUv(
									(pp.getPorcentajeUvAnterior() != null ? pp.getPorcentajeUvAnterior() : 0)
											- (oldPredioPersona.getPorcentajeUv() != null
													? oldPredioPersona.getPorcentajeUv()
													: 0));
							if (newPredioPersona.getTipoRelPersona()==null) {
								newPredioPersona.setTipoRelPersona(tipoRelPersonaRepository
										.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));
							}
							if (newPredioPersona.getDireccion()==null) {
								newPredioPersona.setDireccion(direccionRepository.findOne(1L));
							}
							
							newPredioPersona.setActoPredio(newActoPredio);
							nuevosPredioPersona.add(newPredioPersona);

							indicesActualizados.add(entry.getKey());
						}
					}
				}
			}					
			//--	
			
			log.debug("nuevosPredioPersona  despues de restar el porcentaje anterior={}", nuevosPredioPersona.size());
			nuevosPredioPersona.forEach((predioPersona) -> {
				log.debug("nuevosPredioPersona dd= {}, uv={}, TipoRelPersona={} ", predioPersona.getPorcentajeDd(),
						predioPersona.getPorcentajeUv(), predioPersona.getTipoRelPersona());
			});
		
		}
	
		// remover 0s aqui se hace el acto rel 
		for (ActoRel actoRel : relaciones) {
			Acto newActo = actoRel.getActoSig();
			if (newActo != null) {
				for (ActoPredio newActoPredio : newActo.getActoPrediosParaActos()) {
					for (Iterator<PredioPersona> iterator = newActoPredio.getPredioPersonasParaActoPredios()
							.iterator(); iterator.hasNext();) {
						PredioPersona newPredioPersona = iterator.next();
						if ((newPredioPersona.getPorcentajeDd() != null
								&& newPredioPersona.getPorcentajeDd().floatValue() < 0)
								|| (newPredioPersona.getPorcentajeUv() != null
										&& newPredioPersona.getPorcentajeUv().floatValue() < 0)) {
							throw new IllegalArgumentException("los porcentajes no pueden ser negativos");
						} 
						
						if ((newPredioPersona.getPorcentajeDd() == null || newPredioPersona.getPorcentajeDd() == 0)
								&& (newPredioPersona.getPorcentajeUv() == null
										|| newPredioPersona.getPorcentajeUv() == 0)
								&& (newPredioPersona.getSuperficie() == null
										|| newPredioPersona.getSuperficie() <= 0)) {
							iterator.remove();
						} 						
					}
				}
			}
		}
		
		for (ActoRel actoRel : relaciones) {
			Acto newActo = actoRel.getActoSig();
			if (newActo != null) {
				for (ActoPredio newActoPredio : newActo.getActoPrediosParaActos()) {
					if (newActoPredio.getPredioPersonasParaActoPredios().size() == 0) {
						newActo.getActoPrediosParaActos().remove(newActoPredio);
					}
				}
			}
		}

		for (ActoRel actoRel : relaciones) {
			Acto newActo = actoRel.getActoSig();			
			if (newActo != null && newActo.getActoPrediosParaActos().size() == 0 ) {
				actoRel.setActoSig(null);
			}
		}
		
		//insertar resultado
		for (ActoRel actoRel : relaciones) {
			Acto newActo = actoRel.getActoSig();
			if (newActo!=null) {
				actoRepository.save(newActo);
								
				this.copyActoReferences(acto, newActo, true);				
				Acto actoAnt = actoRel.getActoAnt();
				actoAnt.setRemplazado(true);
				actoRepository.save(actoAnt);

				for (ActoPredio newActoPredio : newActo.getActoPrediosParaActos()) {
					actoPredioRepository.save(newActoPredio);
					for (PredioPersona newPredioPersona : newActoPredio.getPredioPersonasParaActoPredios()) {
						log.debug("newPredioPersona={}", newPredioPersona);
						predioPersonaRepository.save(newPredioPersona);
					}
					
					if ( newActoPredio.getActoPredioCamposParaActoPredios() != null) {
						actoPredioCampoRepository.save(newActoPredio.getActoPredioCamposParaActoPredios());
					}
				}	
			}			
			actoRelRepository.save(actoRel);			
		}
		

		// crear nuevos registros para adquirientes
		acto.statusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		actoRepository.save(acto);
		
		actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
		actoPredioRepository.save(actoPredio);

			ActoRel actoRel = new ActoRel();
			actoRel.setActo(acto);
			actoRel.setActoAnt(null);
			actoRel.setActoSig(acto);
			actoRelRepository.save(actoRel);

		for (Map.Entry<Integer, PredioPersona> entry : adquitientes.entrySet()) {
			PredioPersona predioPersona = entry.getValue();
			
			// crear persona si no se encuentra por curp o rfc si es persona moral
			log.debug("Buscando una persona tipo persona= {}, con curp={} o rfc={} ",
					predioPersona.getPersona().getTipoPersona().getId(), predioPersona.getPersona().getCurp(),
					predioPersona.getPersona().getRfc());

			Optional<Persona> persona2 = null;
			if (predioPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.FISICA
					.getAsInteger().intValue() && predioPersona.getPersona().getCurp() != null
					&& predioPersona.getPersona().getCurp().length() >= 18) {
				persona2 = personaRepository.findByCurp(predioPersona.getPersona().getCurp());
			} else if (predioPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.MORAL
					.getAsInteger().intValue() && predioPersona.getPersona().getRfc() != null
					&& predioPersona.getPersona().getRfc().length() >= 12) {
				persona2 = personaRepository.findByRfc(predioPersona.getPersona().getRfc());
			}
			
			if (persona2!= null && persona2.isPresent()) {
				predioPersona.setPersona(persona2.get());
			} else {
				predioPersona.getPersona().setActivo(true);
				personaRepository.save(predioPersona.getPersona());
			}

			predioPersona.setTipoRelPersona(
					tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));
			predioPersona.setDireccion(direccionRepository.findOne(1L));
			predioPersona.setActoPredio(actoPredio);
			predioPersonaRepository.save(predioPersona);
		}
		
		// si no se encontraron en los actos anteriores los titulares, insertarlos en el
		// nuevo
		if (indicesActualizados.size() != titulares.size()) {
			Set<PredioPersona> nuevosPredioPersona = new HashSet<PredioPersona>();
			
			for (Map.Entry<Integer, PredioPersonaConPorcentajes> entry : titulares.entrySet()) {
				
				if (!indicesActualizados.contains(entry.getKey())) {
					PredioPersona newPredioPersona = new PredioPersona();
					PredioPersonaConPorcentajes oldPredioPersona = entry.getValue();
					
					copyPredioPersona(oldPredioPersona, newPredioPersona);
					
					newPredioPersona.setPorcentajeDd((oldPredioPersona.getPorcentajeDdAnterior() != null
							? oldPredioPersona.getPorcentajeDdAnterior()
							: 0)
							- (oldPredioPersona.getPorcentajeDd() != null ? oldPredioPersona.getPorcentajeDd() : 0));
						
					newPredioPersona.setPorcentajeUv((oldPredioPersona.getPorcentajeUvAnterior() != null
							? oldPredioPersona.getPorcentajeUvAnterior()
							: 0)
							- (oldPredioPersona.getPorcentajeUv() != null ? oldPredioPersona.getPorcentajeUv() : 0));
				
					if (newPredioPersona.getTipoRelPersona()==null) {
						newPredioPersona.setTipoRelPersona(tipoRelPersonaRepository
								.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));
					}
					if (newPredioPersona.getDireccion()==null) {
						newPredioPersona.setDireccion(direccionRepository.findOne(1L));
					}
					
					newPredioPersona.setActoPredio(actoPredio);
					
					if ((newPredioPersona.getPorcentajeDd() != null && newPredioPersona.getPorcentajeDd() > 0)
							|| (newPredioPersona.getPorcentajeUv() != null && newPredioPersona.getPorcentajeUv() > 0)) {
						predioPersonaRepository.save(newPredioPersona);
					} 					
					
				}
			}
			
			nuevosPredioPersona.forEach((predioPersona) -> {
				if ((predioPersona.getPorcentajeDd() != null && predioPersona.getPorcentajeDd() > 0)
						|| (predioPersona.getPorcentajeUv() != null && predioPersona.getPorcentajeUv() > 0)) {
					predioPersonaRepository.save(predioPersona);
				} 				
			});			
		}
		
		//TRASLATIVO PARCIAL RELACION PREDIOS HEREDA ACTOS
		if (folio[0]!=null && folio[0]>0) {
			Integer _folio  = folio[0];
			Predio predio  = actoPredio.getPredio();
			Predio predioMatriz = predioRepository.findByNoFolio(_folio);
			if(predio != null && predioMatriz != null) {
				try {
					caratulaService.savePase(predioMatriz.getId(), predioMatriz.getOficina().getId(),
							predio.getNoFolio(), actoPredio.getActo());
					predio.setProcedeDeFolio(predioMatriz.getNoFolio().toString());
					predioRepository.save(predio);
					HashMap<Integer, ActoPorcentajeVO> actosSeleccionados = actoUtilService
							.parseActos(actoPredioCampos);
					materializacionActoModificatorioService.saveActosSeleccionados(actosSeleccionados,
							acto.getPrelacion(), predio, acto);
				} catch (PasesException e) {
			        log.debug("ERROR MATERIALIZA PASE"+e.getMessage());
				}
		    }
		}

		relaciones.add(actoRel);
		Set<ActoPredio> actoPredios = new HashSet<ActoPredio>();
		actoPredios.add(actoPredio);
		actoPredio.setPredioPersonasParaActoPredios(new HashSet<PredioPersona>(adquitientes.values()));
		acto.setActoPrediosParaActos(actoPredios);
		
	}*/
	
	private void materializarCancelacion(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPrediocampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, ActoPorcentaje> actoPorcentajes = new HashMap<Integer, ActoPorcentaje>();

		actoPrediocampos.forEach((actoPrediocampo) -> {
			Constantes.TipoCampo tipoCampo = Constantes.TipoCampo
					.getTipoCampo(actoPrediocampo.getModuloCampo().getCampo().getTipoCampo().getId());
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			long moduloCampoId = actoPrediocampo.getModuloCampo().getId();
			if (tipoCampo != null) {
				log.debug("campoId = {}, tipo_campo {} , valor{}", campoId, tipoCampo.name(),
						actoPrediocampo.getValor());
				int index = actoPrediocampo.getOrden();
	
				if (tipoCampo == tipoCampo.ACTO || campoId == ACTO_SELECCIONADO_CAMPO_ID
						|| campoId == PORCENTAJE_A_CANCELAR_CAMPO_ID) {
					ActoPorcentaje actoPorcentaje = actoPorcentajes.get(index);
					if (actoPorcentaje == null) {
						actoPorcentaje = new ActoPorcentaje();
						actoPorcentaje.actoPredioMonto = new ActoPredioMonto();
						actoPorcentajes.put(index, actoPorcentaje);
					}
	
					switch (tipoCampo) {
					case ACTO:
							actoPorcentaje.actoPredioMonto
									.setActo(actoService.findOne(Long.valueOf(actoPrediocampo.getValor())));
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

		log.debug("total de actos en siendo cancelados={}", actoPorcentajes.size());
		for(Iterator<Map.Entry<Integer, ActoPorcentaje>> it = actoPorcentajes.entrySet().iterator(); it.hasNext(); ) {
			ActoPorcentaje actoPorcentaje = it.next().getValue();
			if (actoPorcentaje.actoPredioMonto.getPorcentaje()==null) {
				actoPorcentaje.actoPredioMonto.setPorcentaje(100f);
			}		
			log.debug("seleccionado= {}, acto= {}, monto {}, porcentaje {}", actoPorcentaje.actoSeleccionado,
					actoPorcentaje.actoPredioMonto.getActo(), actoPorcentaje.actoPredioMonto.getMonto(),
					actoPorcentaje.actoPredioMonto.getPorcentaje());
			if (actoPorcentaje.actoPredioMonto.getPorcentaje() == 0) {
				it.remove();
			}
		}
		
		HashMap<Integer, TotalesVO> totales = new HashMap<Integer, TotalesVO>();

		// calcular totales en cancelaciones anteriores + nuevos para checar si se
		// cancela al 100%

		for (Map.Entry<Integer, ActoPorcentaje> entry : actoPorcentajes.entrySet()) {
			log.debug("procesando acto gravamen {}", entry.getValue().actoPredioMonto.getActo());
			List<Acto> actosCancelacionesAnteriores = actoService
					.getActosCancelacionesVigentesParaActo(entry.getValue().actoPredioMonto.getActo().getId());
			
			for (Acto a : actosCancelacionesAnteriores) {
				ActoPredio ap = a.getActoPrediosParaActosOrderedByVersion().first();
				ap.getActoPredioMontosParaActoPredios().forEach((actoPredioMonto) -> {
					if (actoPredioMonto.getActo().getId() == entry.getValue().actoPredioMonto.getActo().getId()) {
						TotalesVO total = totales.get(entry.getKey());
						if (total == null) {
							total = new TotalesVO();
							totales.put(entry.getKey(), total);
						}
						total.totalPorcentaje += actoPredioMonto.getPorcentaje();						
					}
				});
			}

			TotalesVO total = totales.get(entry.getKey());
			if (total == null) {
				total = new TotalesVO();
				totales.put(entry.getKey(), total);
				
			}
			
			total.totalPorcentaje += entry.getValue().actoPredioMonto.getPorcentaje();
			
		}

		// Si es igual al 100% o el monto es igual entonces el acto debe ser NO VIGENTE
		for (Map.Entry<Integer, ActoPorcentaje> entry : actoPorcentajes.entrySet()) {
			TotalesVO total = totales.get(entry.getKey());

			log.debug("totales para el acto {}, porcentaje {}", entry.getValue().actoPredioMonto.getActo().getId(),
					total.totalPorcentaje);

			Acto actoSiendoCancelado = entry.getValue().actoPredioMonto.getActo();

			if (actoSiendoCancelado.getActoPrediosParaActos() == null
					&& actoSiendoCancelado.getActoPrediosParaActos().size() == 0) {
				throw new IllegalArgumentException("El acto siendo cancelado no tiene un acto_predio");
			}

			/*
			 * if (actoSiendoCancelado.getActoPrediosParaActosOrderedByVersion().first().
			 * getActoPredioMontosParaActoPredios() == null ||
			 * actoSiendoCancelado.getActoPrediosParaActosOrderedByVersion().first().
			 * getActoPredioMontosParaActoPredios().size() == 0) { throw new
			 * IllegalArgumentException("El acto siendo cancelado no tiene un acto_predio_monto"
			 * ); }
			 */
			
			/*
			 * if (total == null || total.totalPorcentaje > 100) { throw new
			 * IllegalArgumentException("La cancelacion supera mas del 100%"); }
			 */

			if (total != null && (total.totalPorcentaje == 100)) {
				// ya se completo el acto siendo cancelado
				actoSiendoCancelado
						.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));
				actoRepository.save(actoSiendoCancelado);
				if((actoSiendoCancelado.getTipoActo().getId().equals(Constantes.TIPO_ACTO_USUFRUCTO)
					|| actoSiendoCancelado.getTipoActo().getId().equals(Constantes.TIPO_ACTO_RESARVA_DOMINIO) ) && 
				   actoSiendoCancelado.getId_entrada()==null) {
					this.deMaterializarUsufructo(actoSiendoCancelado,true);
				}
			}
		}

		// cambiar a vigente acto de cancelacion
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
		actoRepository.save(acto);

		for (Map.Entry<Integer, ActoPorcentaje> entry : actoPorcentajes.entrySet()) {
			ActoPredioMonto apm = entry.getValue().actoPredioMonto;
			apm.setActoPredio(actoPredio);
			actoPredioMontoRepository.save(apm);
		}

	}
	
	private void deMaterializarCancelacion(Acto acto) {
		//regresar gravamen a ACTIVO
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();
		
		for (ActoPredioMonto actoPredioMonto : actoPredio.getActoPredioMontosParaActoPredios()) {
			Acto actoGravamen = actoPredioMonto.getActo();
			actoGravamen.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			actoRepository.save(actoGravamen);
			
			if((actoGravamen.getTipoActo().getId().equals(Constantes.TIPO_ACTO_USUFRUCTO)) 
				|| actoGravamen.getTipoActo().getId().equals(Constantes.TIPO_ACTO_RESARVA_DOMINIO) && 
				actoGravamen.getId_entrada()==null) {
					this.materializarUsufructo(actoGravamen,true);
			}
			
		}
		
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoPredioMontoRepository.deleteByActoPredioActoId(acto.getId());
		actoRepository.save(acto);
		
	}

	private void materializarGravamenes(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		log.debug("predioActo= {}", actoPredio);

		Set<ActoPredioCampo> actoPrediocampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, ActoPredioMonto> actoPredioMontos = new HashMap<Integer, ActoPredioMonto>();

		actoPrediocampos.forEach((actoPrediocampo) -> {
			long campoId = actoPrediocampo.getModuloCampo().getCampo().getId();
			log.debug("modulo_campo_id = {}, campoid={} , valor={}", actoPrediocampo.getModuloCampo().getId(), campoId,
					actoPrediocampo.getValor());
			int index = actoPrediocampo.getOrden();

			if (campoId == MONTO_DEL_CREDITO_CAMPO_ID || campoId == TIPO_DE_MONEDA_CAMPO_ID) {
				ActoPredioMonto actoPredioMonto = actoPredioMontos.get(index);
				if (actoPredioMonto == null) {
					actoPredioMonto = new ActoPredioMonto();
					actoPredioMonto.setPorcentaje(100f);
					actoPredioMontos.put(index, actoPredioMonto);
				}

				if (campoId == MONTO_DEL_CREDITO_CAMPO_ID && actoPrediocampo.getValor()!=null) {
					String monto = 	actoPrediocampo.getValor().trim();
					monto = monto.replace(",","");
					monto = monto.replace("$","");
					actoPredioMonto.setMonto(new BigDecimal(monto));
				}

				if (campoId == TIPO_DE_MONEDA_CAMPO_ID) {
					actoPredioMonto
							.setTipoMoneda(tipoMonedaRepository.findOne(Long.parseLong(actoPrediocampo.getValor())));
				}

			}

		});

		log.debug("total de actos en gravamen={}", actoPredioMontos.size());
		actoPredioMontos.forEach((index, actoPredioMonto) -> {
			log.debug("acto= {}, monto={}, porcentaje={}, tipo_moneda={}", actoPredioMonto.getActo(),
					actoPredioMonto.getMonto(), actoPredioMonto.getPorcentaje(),
					actoPredioMonto.getTipoMoneda() != null ? actoPredioMonto.getTipoMoneda().getNombre() : "null");
		});

		// cambiar a vigente acto de cancelacion
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
		actoRepository.save(acto);
		
		actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
		actoPredioRepository.save(actoPredio);

		for (Map.Entry<Integer, ActoPredioMonto> entry : actoPredioMontos.entrySet()) {
			ActoPredioMonto apm = entry.getValue();
			apm.setActoPredio(actoPredio);
			actoPredioMontoRepository.save(apm);
		}

	}

	@Transactional
	private void materializarFraccionamiento(Acto acto) {				
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		if (actoPredio.getPredio() == null) {
			throw new IllegalArgumentException("El acto no se le asociado un predio");
		}

		log.debug("predioActoFRAC= {}", actoPredio);

		HashMap<Integer, FraccionVO> entrada = new HashMap<Integer, FraccionVO>();
		HashMap<Integer, FraccionVO> salida = new HashMap<Integer, FraccionVO>();
		
		log.debug("ACTO FRAC {}",acto);
		Set<FoliosFracCond> fracciones = acto.getFoliosFracCondParaActos();

		actoPredioCampo2 = actoPredio.getActoPredioCamposParaActoPredios();                   
		// Set<ActoPredioCampo> actoPredioCampo2 =
		// actoPredio.getActoPredioCamposParaActoPredios();////////////REVISAR AQUI
		
		log.debug("acto_predio_campoFRAC {}",actoPredioCampo2);
		
		entrada.put(1, new FraccionVO(actoPredio.getPredio().getId()));
		
		Iterator<FoliosFracCond> iter = fracciones.iterator();
		int index=1;
		while (iter.hasNext()) {			 
			FoliosFracCond fraccion = iter.next();
			if (fraccion.getProcedente()) {
				salida.put(index, new FraccionVO(fraccion.getPredio().getId()));				
				index++;
			}			
		}
		saveEntradaSalida(acto, entrada, salida);
	}   // FIN materializarFraccionamiento

	@Transactional
	private void saveEntradaSalida(Acto acto, HashMap<Integer, FraccionVO> entrada,
			HashMap<Integer, FraccionVO> salida) {
		StringBuilder procedeDeFolio = new StringBuilder();
		Long predioIdOrigen=entrada.get(1).predioId,predioIdRest=salida.get(1).predioId;
		log.debug("\npredio de origen1 "+predioIdOrigen+"\npredio de resultante2 "+predioIdRest);
		
		List<Predio> prediosEntrada = new ArrayList<Predio>();
		List<Predio> prediosSalida = new ArrayList<Predio>();
		
		entrada.forEach((index, fraccionVO) -> {
			if (fraccionVO.predioId>0) {
				Predio predio = predioRepository.findOne(fraccionVO.predioId);
				predio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo()));				
				if (procedeDeFolio.length()>0) {
					procedeDeFolio.append(",");
				}
				procedeDeFolio.append(predio.getNoFolio());
				log.debug("aquiiiii 7878 "+procedeDeFolio);
				log.debug("aqui7879 {}",predio);

				predioRepository.save(predio);
				
				prediosEntrada.add(predio);			
			}
		});
		
		// busca los propietarios para el primer predio de entrada
		List<PredioPersona> predioPersonas = predioPersonaService.findPropietariosActuales(entrada.get(1).predioId,
				false);
		log.debug("actoPredioCampo2322 {}",actoPredioCampo2);
		salida.forEach((index, fraccionVO) -> {
		//	Long predioIdOrigen=entrada.get(1).predioId,predioIdRest=fraccionVO.predioId;
			if (fraccionVO.predioId>0) {
				Acto newActo = new Acto();
				
				copyActo(acto, newActo);
				
				actoRepository.save(newActo);
				
				log.debug("Predios salida generando Acto = {}", newActo);
				log.debug("Predios origen {}", fraccionVO.predioId);
				log.debug("predio de origen2 {}",entrada.get(1).predioId);
				
				ActoRel actoRel = new ActoRel();
				actoRel.setActo(acto);
				actoRel.setActoAnt(acto);
				actoRel.setActoSig(newActo);
				
				actoRelRepository.save(actoRel);
				
				Predio newPredio = predioRepository.findOne(fraccionVO.predioId);
				newPredio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
				newPredio.setCaratulaActualizada(true);
				newPredio.setBloqueado(false);
				newPredio.setProcedeDeFolio(procedeDeFolio.length()>0 ? procedeDeFolio.toString() : null);
				log.debug("aqui VO " + "\n 1 " + newPredio.getSuperficieM2().doubleValue() + "\n 2 "
						+ fraccionVO.superficie);
				if (fraccionVO.superficie <0 ) {
					throw new IllegalArgumentException(
							"La superficie del predio no corresponde con la superficie de la fraccion");
				}
				predioRepository.save(newPredio);
				
				prediosSalida.add(newPredio);
				
				/////////////////////////////////////////////////////////////////////////////////////////////
				Predio predio1 = predioRepository.findOne(Long.valueOf(predioIdOrigen));
				for (ActoPredio actoPredio1 : predio1.getActoPrediosParaPredios()) {
					if(actoPredio1.getActo().getTipoActo()!=null) {
						if (actoPredio1.getActo().getTipoActo().getClasifActo().getId() == 2L
								|| actoPredio1.getActo().getTipoActo().getClasifActo().getId() == 6L
										&& actoPredio1.getActo().getStatusActo().getId() == 1L
										&& !actoPredio1.getActo().getStatusActo().getId().toString()
												.equals(Constantes.FRACCIONAMIENTO_TIPO_ACTO_ID.toString())
										&& !actoPredio1.getActo().getStatusActo().getId().toString()
												.equals(Constantes.CONDOMINIO_TIPO_ACTO_ID.toString())
										&& !actoPredio1.getActo().getStatusActo().getId().toString()
												.equals(Constantes.LOTIFICACION_TIPO_ACTO_ID.toString())
										&& !actoPredio1.getActo().getStatusActo().getId().toString()
												.equals(Constantes.RELOTIFICACION_TIPO_ACTO_ID.toString())) {// verifica
																												// la
																												// clasificacion
																												// y que
																												// el
																												// acto
																												// este
																												// vigente
																												// o
																												// activo
							log.debug("\n----------------ENTRO\n" + actoPredio1.getId() + "\n"
									+ actoPredio1.getActo().getTipoActo().getNombre());
							ActoPredio newActoPredio1 = new ActoPredio();
							Acto newActo1 = new Acto();
							//////////copy de acto
							this.copyActo(actoPredio1.getActo(),newActo1);
							newActo1.setProcesado(true);
							actoRepository.save(newActo1);
							log.debug("-------------------------------Aqui va ACTO_DUPLICADO {}",newActo1);
							///////copy de acto_predio
							this.copyActoPredio(actoPredio1, newActoPredio1);
							newActoPredio1.setActo(newActo1);
							newActoPredio1.setTipoEntrada(actoPredio1.getTipoEntrada());
							Predio predio2 = newPredio;///AQUI VA EL PREDIO RESULTANTE
							newActoPredio1.setPredio(predio2);///AQUI VA EL PREDIO RESULTANTE
							actoPredioRepository.save(newActoPredio1);
							log.debug("---------------------Aqui ACTO_PREDIO_DUPLICADO {}",newActoPredio1);
							actoPredioCampo3 = newActoPredio1.getActoPredioCamposParaActoPredios();
							//////////duplicado de acto_predio_campo
							if(actoPredioCampo3!=null) {
								actoPredioCampo3.forEach((actoPredioCampo) -> {
									ActoPredioCampo newActoPredioCampo = new ActoPredioCampo();
									newActoPredioCampo.setActoPredio(newActoPredio1);
									newActoPredioCampo.setModuloCampo(actoPredioCampo.getModuloCampo());
									newActoPredioCampo.setOrden(actoPredioCampo.getOrden());
									newActoPredioCampo.setValor(actoPredioCampo.getValor());
									actoPredioCampoRepository.save(newActoPredioCampo);
								});
							} else {
								log.debug("NO TIENE ACTO_PRDIO_CAMPOS");
							}
							
						}//fin de la verifficacion
					}

				}//fin
				
				/////////////////////////////////////////////////////////////////////////////////////////////
				//Crea nuevo acto_predio
				ActoPredio newActoPredio = new ActoPredio();
				newActoPredio.setActo(newActo);
				newActoPredio.setPredio(newPredio);	
				newActoPredio.setVersion(1);
				newActoPredio
						.setTipoFolio(tipoFolioRepository.findOne((long) Constantes.ETipoFolio.PREDIO.getTipoFolio()));
				newActoPredio.setTipoEntrada(
						tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
				
				actoPredioRepository.save(newActoPredio);
				log.debug("actoPredioCampo2 {}",actoPredioCampo2);
				log.debug("7472aquiii "+ newActoPredio.getId());
				log.debug("7473aquiii ");
				//float uv=0,dd=0;
				ArrayList personaId=new ArrayList();
				ArrayList dd=new ArrayList();
				ArrayList uv=new ArrayList();
				if(actoPredioCampo2!=null) {
					for(ActoPredioCampo actoPredioCampo:actoPredioCampo2) {
					ActoPredioCampo newActoPredioCampo = new ActoPredioCampo();
					newActoPredioCampo.setActoPredio(newActoPredio);
					newActoPredioCampo.setModuloCampo(actoPredioCampo.getModuloCampo());
					newActoPredioCampo.setOrden(actoPredioCampo.getOrden());
					newActoPredioCampo.setValor(actoPredioCampo.getValor());
					actoPredioCampoRepository.save(newActoPredioCampo);
						if (!newActo.getTipoActo().getId().toString()
								.equals(Constantes.FRACCIONAMIENTO_TIPO_ACTO_ID.toString())
								&& !newActo.getTipoActo().getId().toString()
										.equals(Constantes.CONDOMINIO_TIPO_ACTO_ID.toString())
								&& !newActo.getTipoActo().getId().toString()
										.equals(Constantes.LOTIFICACION_TIPO_ACTO_ID.toString())
								&& !newActo.getTipoActo().getId().toString()
										.equals(Constantes.RELOTIFICACION_TIPO_ACTO_ID.toString())) {
							if (actoPredioCampo.getModuloCampo().getId() == 20763) {
								if (actoPredioCampo.getValor() != null) {
									personaId.add(actoPredioCampo.getValor());
								} else {
									log.debug("valor null en 20763");
								}
							}
							if (actoPredioCampo.getModuloCampo().getId() == 20767) {
								if (actoPredioCampo.getValor() != null) {
									dd.add(actoPredioCampo.getValor());
								} else {
									log.debug("valor null en 20767");
								}
						}						
							if (actoPredioCampo.getModuloCampo().getId() == 20768) {
								if (actoPredioCampo.getValor() != null) {
									uv.add(actoPredioCampo.getValor());
								} else {
									log.debug("valor null en 20768");
						}
						}
					}
					
					}
				
			}//
				int auxI = 0;// variable auxiliar para revisar el orden en acto_predio_campo y extraer
								// 20767,20768
				for (PredioPersona predioPersona : predioPersonas) {
					PredioPersona newPredioPersona = new PredioPersona();					
					BeanUtils.copyProperties(predioPersona, newPredioPersona);//
					newPredioPersona.setId(null);
					newPredioPersona.setActoPredio(newActoPredio);
					newPredioPersona.setPredioPersonasParaPredioPropDesignans(null);
					//20767,20767,20768,20768 REVISAR EL ORDEN 20767 DD,20768 UV
					if (!newActo.getTipoActo().getId().toString()
							.equals(Constantes.FRACCIONAMIENTO_TIPO_ACTO_ID.toString())
							&& !newActo.getTipoActo().getId().toString()
									.equals(Constantes.CONDOMINIO_TIPO_ACTO_ID.toString())
							&& !newActo.getTipoActo().getId().toString()
									.equals(Constantes.LOTIFICACION_TIPO_ACTO_ID.toString())
							&& !newActo.getTipoActo().getId().toString()
									.equals(Constantes.RELOTIFICACION_TIPO_ACTO_ID.toString())) {
					newPredioPersona.setPorcentajeDd(Float.valueOf(dd.get(auxI).toString()));
					newPredioPersona.setPorcentajeUv(Float.valueOf(uv.get(auxI).toString()));
				}
					predioPersonaRepository.save(newPredioPersona);				
					auxI++;
				}
				
				try{
					predioService.createFolioPredio(newPredio.getId(), acto.getPrelacion().getOficina().getId(), false);
				}catch (Exception e ) {
					throw new IllegalArgumentException(e.getMessage());
        }
				
			}
		});////termina metodo de savedEntradaSalida
	
		//crea relacion entre predios
		for (Predio predioEntrada : prediosEntrada) {
			for (Predio predioSalida: prediosSalida) {
				PredioRel predioRel = new PredioRel();
				predioRel.setPredio(predioEntrada);
				predioRel.setPredioSig(predioSalida);
				predioRelRepository.save(predioRel);
			}
		}
		
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		actoRepository.save(acto);

	}
	
	private class ActoPorcentaje {
		public boolean actoSeleccionado;
		public ActoPredioMonto actoPredioMonto;
	}

	private class TotalesVO {
		public float totalPorcentaje;
		//public BigDecimal totalMonto;

		public TotalesVO() {
			totalPorcentaje = 0;
			//totalMonto = BigDecimal.ZERO;
		}
	}

	private class FraccionVO {
		public long predioId;
		public double superficie;

		public FraccionVO() {
			superficie = 0;
		}
		
		public FraccionVO(long predioId) {
			this.predioId = predioId;
			superficie = 0;
		}
	}
	
	private class Titular {
		public Float porcentajeUv;
		public Float porcentajeDd;
		public PredioPersona predioPersona;
	}

	private void copyActo(Acto source, Acto target) {
		target.setTipoActo(source.getTipoActo());
		target.setPrelacion(source.getPrelacion());
		target.setFechaRegistro(source.getFechaRegistro());
		target.setVf(source.getVf());
		target.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		target.setOrden(source.getOrden());
		target.setVersion(source.getVersion());
		target.setCopiadoModificado(true);
		if (source.isProcesado() != null) {
			target.setProcesado(source.isProcesado());
		}	
		target.setVf(true);

	}

	private void copyActoPredio(ActoPredio source, ActoPredio target) {
		target.setPredio(source.getPredio());	
		target.setVersion(source.getVersion());
		target.setTipoFolio(source.getTipoFolio());
		
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

	/**
	 * copia firma y documento
	 * 
	 * @param source
	 * @param target
	 */
	public void copyActoReferences(Acto source, Acto target, boolean commit) {
		Set<ActoFirma> actosFirma = null;
		for (ActoFirma actoFirma : source.getActoFirmasParaActos()) {
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
		for (ActoDocumento actoDocumento : source.getActoDocumentosParaActos()) {
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

	public void copyActoDocumento(ActoDocumento source, ActoDocumento target) {
		target.setDocumento(source.getDocumento());
		target.setVersion(source.getVersion());		
	}
		
	@Transactional
	public void deHeredarActos(Acto acto) {
		List<ActoControlHereda> actos =  actoControlHeredaService.findByActoId(acto.getId());
		if(actos!=null && actos.size() >0) {
			actoControlHeredaService.deleteByActoId(acto.getId());
			actos.forEach(x->{
				 actoService.deleteActo(x.getActoHeredado().getId());
			});
		}
	}
	
	@Transactional
	public void heredarActos(Predio source, Predio target, Prelacion prelacion, Acto actoContext) {
		List<Acto> actosSource = actoRepository
				.findDistinctByStatusActoIdAndActoPrediosParaActosPredioIdAndTipoActoClasifActoId(
						Constantes.StatusActo.ACTIVO.getIdStatusActo(), source.getId(),
				Constantes.ClasifActo.GRAMAVAMES_LIMITATANES.getIdClasifActo());
	  actosSource.forEach(x->{
		   actoUtilService.cloneActo(x, prelacion, target, null,actoContext);  
	  });
	}
		
	private void materializacionAviso(Acto acto) {
		materializacionDefault(acto);
		avisoService.guardarNuevo(acto);
		if (acto.getTipoActo().getId() == 50L || acto.getTipoActo().getId() == 124L) {
			extinguirPrimerosAvisosPorActo(acto);
		}

	}

	public List<AvisoCanceladoPorActo> extinguirPrimerosAvisosPorActo(Acto a) {
		ArrayList<AvisoCanceladoPorActo> avisosCancelados = new ArrayList<>();

		Usuario usuario=null;
		try {
			usuario = usuarioService.getLoguedUser();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Prelacion prelacion = a.getPrelacion();
		if (prelacion != null) {
			List<Predio> predios = ppService.findAllPrelacionPredioFromPrelacion(prelacion).stream()
				.map(PrelacionPredio::getPredio)
				.collect(Collectors.toList());		

			Boolean esSegundoAviso=false;
			for(Predio p : predios) {
				
				List<AvisoDTO> avisosPredio = obtenerDePredio(p);
				if (avisosPredio == null || avisosPredio.isEmpty()) continue;

				List<ActoPredio> actosPredio = actoPredioRepository.findAllMaxVersionByActoPrelacionIdAndPredioId(prelacion.getId(), p.getId());

				for (AvisoDTO aviso : avisosPredio) {
					System.out.println("AVISO: "+aviso.getId());
					if (aviso != null && aviso.getVigente() != null && aviso.getVigente()) {
					
						ActoPredio actoPredioAviso = aviso.getActo().getActoPrediosParaActos().stream()
								.filter(apa -> apa.getPredio() != null && apa.getPredio().getId().equals(p.getId()))
								.max((apa1, apa2) -> apa1.getVersion().compareTo(apa2.getVersion()))
								.orElse(null);

						if (actoPredioAviso == null) continue;

						
							actoService.cancelarActo(aviso.getActo());
							AvisoCanceladoPorActo avisoCancelado =  new AvisoCanceladoPorActo();
							avisoCancelado.setActo(a);
							avisoCancelado.setAviso(avisoRepository.findOne(aviso.getId()));
							avisoCancelado.setPrelacion(prelacion);
							avisoCancelado.setFechaCan(new Date());
							avisoCancelado.setTipoActo(a.getTipoActo());
							avisoCancelado.setCancelacionActiva(true);
							avisoCancelado.setUsuario(usuario);
							avisoCancelado = avisoCanceladoPorActoRepository.save(avisoCancelado);
							avisosCancelados.add(avisoCancelado);

					}
				}

			}
			return avisosCancelados;
		}
		return null;
	}	

	private List<AvisoDTO> obtenerDePredio(Predio predio) {
		Long[] status = {1L};
		List<AvisoDTO> avisos =  new ArrayList<AvisoDTO>();
		if(predio!=null) {
	
			Set<Acto> actosPredio = actoPredioService.findAllActosbyPredioAndStatusId(predio.getId(), status);
			
			 avisos = actosPredio.stream()
					.filter(this::actoEsPrimerAviso)
					.map(Acto::getId)
					.map(avisoService::buscarPorActoId)
					.map(AvisoDTO::new)
					.collect(Collectors.toList());
		}
		return avisos;
	}

	private boolean actoEsPrimerAviso(Acto acto) {
		int tipoActo = acto.getTipoActo().getId().intValue();
		switch (tipoActo) {
		case 49:
		case 210:
			return true;
		default:
			return false;
		}
	}


	private void deMaterializacionAviso(Acto acto) {
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
		avisoRepository.deleteByActoId(acto.getId());
		if(acto.getTipoActo().getId()==50L || acto.getTipoActo().getId() == 124L){
			restaurarPrimerAvisoPorActo(acto);
		}

	}
	private void restaurarPrimerAvisoPorActo(Acto a){
		List<AvisoCanceladoPorActo> avisosCancelados=avisoCanceladoPorActoRepository.findByActoId(a.getId());
		if(avisosCancelados!=null && !avisosCancelados.isEmpty()){
			for(AvisoCanceladoPorActo avisoCancelado : avisosCancelados){
				AvisoDTO aviso= new AvisoDTO(avisoCancelado.getAviso());
				if(aviso.getVigente() && avisoCancelado.getCancelacionActiva()){
					Acto primerAviso = avisoCancelado.getAviso().getActo();
					primerAviso.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
					actoRepository.save(primerAviso);
					avisoCancelado.setCancelacionActiva(false);
					avisoCanceladoPorActoRepository.save(avisoCancelado);
				}
			}
		}
		
	}
}