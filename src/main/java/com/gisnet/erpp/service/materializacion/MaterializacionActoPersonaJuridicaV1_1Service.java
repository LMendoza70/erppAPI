package com.gisnet.erpp.service.materializacion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle.Control;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoRel;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.ControlTipoRelPersona;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PersonaJuridicaBitacora;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.PredioPersonaControl;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.ControlTipoRelPersonaRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.EstadoCivilRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PjPersonaRepository;
import com.gisnet.erpp.repository.PredioPersonaControlRepository;
import com.gisnet.erpp.repository.RegimenRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.PersonaJuridicaBitacoraService;
import com.gisnet.erpp.service.PersonaJuridicaService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class MaterializacionActoPersonaJuridicaV1_1Service {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final String FECHA_FORMATO_CAMPO_VALOR = "yyyy-mm-dd";
	private final long NOMBRE_CAMPO_ID = 286;
	private final long PATERNO_CAMPO_ID = 287;
	private final long MATERNO_CAMPO_ID = 288;
	private final long FECHA_NACIMIENTO_CAMPO_ID = 289;
	private final long RFC_CAMPO_ID = 290;
	private final long CURP_CAMPO_ID = 291;
	private final long ESTADO_CIVIL_CAMPO_ID = 292;
	private final long REGIMEN_CAMPO_ID = 293;
	private final long NACIONALIDAD_CAMPO_ID = 119;
	private final long MAYOR_O_MENOR_EDAD_CAMPO_ID = 294;
	private final long COLECTIVO_ORGANO_CAMPO_ID = 315;
	private final long TIPO_ORGANO_PERSONA__CAMPO_ID = 316;
	private final long TIPO_ORGANO_CAMPO_ID = 317;
	//private final long APORTACION_CAMPO_ID = 727;

	private final long SOCIO_MODULO_ID = 123;
	private final long ORGANO_ADMINISTRACION_MODULO_ID = 187;
	private final long APODERADO_MODULO_ID = 146;
	
	private final long SOCIO_ACTUAL_MODULO_ID = 1558;
	private final long ORGANO_ACTUAL_MODULO_ID = 1559;
	private final long APODERADO_ACTUAL_MODULO_ID = 1560;
	

	@Autowired
	PrelacionService prelacionService;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	ActoService actoService;

	@Autowired
	PersonaRepository personaRepository;

	@Autowired
	StatusActoRepository statusActoRepository;

	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	PjPersonaRepository pjPersonaRepository;

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
	CampoValoresRepository campoValoresRepository;
	
	@Autowired
	PersonaJuridicaRepository personaJuridicaRepository;
	
	@Autowired
	TipoEntradaRepository tipoEntradaRepository;
	
	@Autowired 
	ControlTipoRelPersonaRepository controlTipoRelPersonaRepository;
	
	@Autowired
	PersonaJuridicaService personaJuridicaService;
	
	@Autowired
	PredioPersonaControlRepository  predioPersonaControlRepository;
	
	@Autowired
	PersonaJuridicaBitacoraService personaJuridicaBitacoraService; 

	public void materializaConstitucionPersonaJuridica(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		if (actoPredio.getPersonaJuridica() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}

		log.debug("actoPredio= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, PjPersona> sociosActuales = new HashMap<Integer, PjPersona>();
		HashMap<Integer, PjPersona> organosActuales = new HashMap<Integer, PjPersona>();
		HashMap<Integer, PjPersona> apoderadosActuales = new HashMap<Integer, PjPersona>();
		
		HashMap<Integer, PjPersona> socios = new HashMap<Integer, PjPersona>();
		HashMap<Integer, PjPersona> organosAdministracion = new HashMap<Integer, PjPersona>();
		HashMap<Integer, PjPersona> apoderados = new HashMap<Integer, PjPersona>();
		
		
		CampoValores organo = null;
		CampoValores tipoColectivo = null;
		BigDecimal capital = null;
		String objeto = null;
		log.debug("total de actoPredioCampos para acto = {}, {}", actoPredioCampos.size(), actoPredioCampos);

		for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
			long moduloId = actoPredioCampo.getModuloCampo().getModulo().getId();
			// actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();
		
			
			log.debug("campoId={} moduloId={} valor={}", campoId, actoPredioCampo.getModuloCampo().getModulo().getId(), actoPredioCampo.getValor());

			if (actoPredioCampo.getValor() != null) {

				if (campoId == NOMBRE_CAMPO_ID || campoId == PATERNO_CAMPO_ID || campoId == MATERNO_CAMPO_ID 
				|| campoId == FECHA_NACIMIENTO_CAMPO_ID || campoId == RFC_CAMPO_ID 
				|| campoId == CURP_CAMPO_ID || campoId == ESTADO_CIVIL_CAMPO_ID 
				|| campoId == REGIMEN_CAMPO_ID 
				|| campoId == NACIONALIDAD_CAMPO_ID || campoId == COLECTIVO_ORGANO_CAMPO_ID 
				|| campoId == TIPO_ORGANO_CAMPO_ID || campoId == TIPO_ORGANO_PERSONA__CAMPO_ID
				//|| campoId == APORTACION_CAMPO_ID
				) {
					PjPersona pjPersona = null;
					if (moduloId == SOCIO_MODULO_ID) {
						pjPersona = socios.get(index);
					} else if (moduloId == ORGANO_ADMINISTRACION_MODULO_ID) {
						pjPersona = organosAdministracion.get(index);
					} else if (moduloId == APODERADO_MODULO_ID) {
						pjPersona = apoderados.get(index);
					}

					if (pjPersona == null) {
						pjPersona = new PjPersona();
						pjPersona.setPersona(new Persona());

						if (actoPredioCampo.getModuloCampo().getModulo().getId() == SOCIO_MODULO_ID) {
							socios.put(index, pjPersona);
						} else if (actoPredioCampo.getModuloCampo().getModulo().getId() == ORGANO_ADMINISTRACION_MODULO_ID) {
							organosAdministracion.put(index, pjPersona);
						} else if (actoPredioCampo.getModuloCampo().getModulo().getId() == APODERADO_MODULO_ID) {
							apoderados.put(index, pjPersona);
						}
					}

					if (campoId == NOMBRE_CAMPO_ID) {
						pjPersona.getPersona().setNombre(actoPredioCampo.getValor());
					} else if (campoId == PATERNO_CAMPO_ID) {
						pjPersona.getPersona().setPaterno(actoPredioCampo.getValor());
					} else if (campoId == MATERNO_CAMPO_ID) {
						pjPersona.getPersona().setMaterno(actoPredioCampo.getValor());
					} else if (campoId == FECHA_NACIMIENTO_CAMPO_ID) {
						pjPersona.getPersona().setFechaNac(UtilFecha.toDate(actoPredioCampo.getValor(), FECHA_FORMATO_CAMPO_VALOR));
					} else if (campoId == RFC_CAMPO_ID) {
						pjPersona.getPersona().setRfc(actoPredioCampo.getValor());
					} else if (campoId == CURP_CAMPO_ID) {
						pjPersona.getPersona().setCurp(actoPredioCampo.getValor());
					} else if (campoId == ESTADO_CIVIL_CAMPO_ID) {
						pjPersona.setEstadoCivil(estadoCivilRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
					} else if (campoId == REGIMEN_CAMPO_ID) {
						if (actoPredioCampo.getValor() != null && actoPredioCampo.getValor().length() > 0) {
							pjPersona.setRegimen(regimenRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
						}
					} else if (campoId == COLECTIVO_ORGANO_CAMPO_ID) {
						if (actoPredioCampo.getValor() != null && actoPredioCampo.getValor().length() > 0) {
							organo = campoValoresRepository.findOne(Long.valueOf(actoPredioCampo.getValor()));
						}
					} else if (campoId == TIPO_ORGANO_CAMPO_ID) {
						if (actoPredioCampo.getValor() != null && actoPredioCampo.getValor().length() > 0) {
							tipoColectivo = campoValoresRepository.findOne(Long.valueOf(actoPredioCampo.getValor()));
						}
					}/* else if(campoId==APORTACION_CAMPO_ID) {
						BigDecimal aportacion = actoPredioCampo.getValor() != null 
												&& !actoPredioCampo.getValor().trim().isEmpty() ?  
												new BigDecimal(actoPredioCampo.getValor())  : null; 
						  pjPersona.setAportacion(aportacion);
					}*/ else if (campoId == TIPO_ORGANO_PERSONA__CAMPO_ID) {
						if (actoPredioCampo.getValor() != null && actoPredioCampo.getValor().length() > 0) {
							
							ControlTipoRelPersona ctrp =  controlTipoRelPersonaRepository.findFirstByCampoValoresId(Long.valueOf(actoPredioCampo.getValor()));
							pjPersona.setTipoOrgano(ctrp!=null ? ctrp.getTipoOrgano() : null);
						}
					} else if (campoId == NACIONALIDAD_CAMPO_ID) {
						Nacionalidad nacionalidad = nacionalidadRepository.findOne(Long.valueOf(actoPredioCampo.getValor()));
						pjPersona.getPersona().setNacionalidad(nacionalidad);
						pjPersona.setNacionalidad(nacionalidad);
					} else if (campoId == MAYOR_O_MENOR_EDAD_CAMPO_ID) {
						if (actoPredioCampo.getValor() != null && actoPredioCampo.getValor().length() > 0) {
							if (String.valueOf(Constantes.MAYOR_DE_EDAD_CAMPO_VALOR_ID).equals(actoPredioCampo.getValor())) {
								pjPersona.setMenorEdad(false);
							} else if (String.valueOf(Constantes.MENOR_DE_EDAD_CAMPO_VALOR_ID).equals(actoPredioCampo.getValor())) {
								pjPersona.setMenorEdad(true);
							}
						}
					}
				}
				
				
				Constantes.TipoCampo tipoCampo = Constantes.TipoCampo.getTipoCampo(actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
				if (tipoCampo == null) {
					log.warn("No se encontro el tipo campo en Constantes.TipoCampo={}", actoPredioCampo.getModuloCampo().getCampo().getTipoCampo().getId());
	
				} else {
					if (tipoCampo == tipoCampo.PERSONA_PREDIO_TITULAR || tipoCampo == tipoCampo.ACTIVACION_TRANSMITENTE) {
						
						PjPersona pjPersona = null;
						if(moduloId == SOCIO_ACTUAL_MODULO_ID) {
							pjPersona = sociosActuales.get(index);
						}else if(moduloId == ORGANO_ACTUAL_MODULO_ID) {
							pjPersona = organosActuales.get(index);
						}else if(moduloId == APODERADO_ACTUAL_MODULO_ID) {
							pjPersona =  apoderadosActuales.get(index);
						}
						
						if (pjPersona == null) {
							pjPersona = new PjPersona();
							if(moduloId == SOCIO_ACTUAL_MODULO_ID) {
								sociosActuales.put(index,pjPersona);
							}else if(moduloId == ORGANO_ACTUAL_MODULO_ID) {
								organosActuales.put(index,pjPersona);
							}else if(moduloId == APODERADO_ACTUAL_MODULO_ID) {
								apoderadosActuales.put(index, pjPersona);
							}
						}
	
						switch (tipoCampo) {
						case PERSONA_PREDIO_TITULAR:
							pjPersona.setPersona(personaRepository.findOne(Long.valueOf(actoPredioCampo.getValor())));
							break;
						
						case ACTIVACION_TRANSMITENTE:
							pjPersona.setExtinto( actoPredioCampo.getValor()!= null ?  Boolean.parseBoolean(actoPredioCampo.getValor()) : false);
							break;
							
						}
					}
					
					/*if(tipoCampo == tipoCampo.MONTO_CAPITAL_SOCIAL) {
						if(!actoPredioCampo.getValor().trim().isEmpty()) {
							capital = new BigDecimal(actoPredioCampo.getValor());
						}
					}*/
					
					if(tipoCampo == tipoCampo.OBJETO_PJ) {
						if(actoPredioCampo.getValor() != null && !actoPredioCampo.getValor().trim().isEmpty()) {
							objeto = actoPredioCampo.getValor();
						}
					}
				}
			}

		}

		
		socios.forEach((index, pjPersona) -> {
			pjPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Constantes.TipoPersona.FISICA.getIdTipoPersona()));
			pjPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.SOCIO.getIdTipoRelPersona()));
		});

		
		organosAdministracion.forEach((index, pjPersona) -> {
			pjPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Constantes.TipoPersona.FISICA.getIdTipoPersona()));
			pjPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.ORGANO_ADMINISTRACION.getIdTipoRelPersona()));
		});

		
		apoderados.forEach((index, pjPersona) -> {
			pjPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Constantes.TipoPersona.FISICA.getIdTipoPersona()));
			pjPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.APODERADO.getIdTipoRelPersona()));
		});


		this.save(socios, actoPredio);
		this.save(organosAdministracion, actoPredio);
		this.save(apoderados, actoPredio);
		
		
		PersonaJuridica personaJuridica = actoPredio.getPersonaJuridica();
		
		//BITACORA PERSONA JURIDICA
		personaJuridicaBitacoraService.save(personaJuridica,acto);
			
		if(organo!=null)
			personaJuridica.setOrgano(organo);
		if(tipoColectivo!=null)
			personaJuridica.setTipoColectivo(tipoColectivo);
		if(objeto!=null)
			personaJuridica.setObjeto(objeto);
	
		
		personaJuridicaRepository.save(personaJuridica);
		
		
		//EXTINGUIR SOCIOS SELECCIONADOS
		List<PjPersona> extSocios = personaJuridicaService.findSociosOrganosApoderadosActuales(personaJuridica.getId(),Constantes.TipoRelPersona.SOCIO);
		extinguirPersonas(extSocios, sociosActuales, acto);
		
		//EXTINGUIR ORGANOS SELECCIONADOS
		List<PjPersona> extOrganos = personaJuridicaService.findSociosOrganosApoderadosActuales(personaJuridica.getId(),Constantes.TipoRelPersona.ORGANO_ADMINISTRACION);
		extinguirPersonas(extOrganos, organosActuales, acto);
		
		//EXTINGUIR APODERADOS SELECCIONADOS
		List<PjPersona> extApoderados = personaJuridicaService.findSociosOrganosApoderadosActuales(personaJuridica.getId(),Constantes.TipoRelPersona.APODERADO);
		extinguirPersonas(extApoderados, apoderadosActuales, acto);

		actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
		actoPredioRepository.save(actoPredio);
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
		actoRepository.save(acto);

	}
	
	private void extinguirPersonas(List<PjPersona> pjActuales,HashMap<Integer, PjPersona> pjSeleccionadas,Acto acto) {
		if(pjActuales!=null && pjActuales.size()> 0) {
			pjActuales.forEach(x->{
				for(Map.Entry<Integer, PjPersona> entry : pjSeleccionadas.entrySet()) {
					PjPersona pp = entry.getValue();
					if(x.getPersona().getId() == pp.getPersona().getId() && pp.getExtinto()!=null &&  pp.getExtinto()) {
						 x.setExtinto(pp.getExtinto());
						 pjPersonaRepository.save(x);
						 PredioPersonaControl ppc =  new PredioPersonaControl();
						   ppc.setActo(acto);
						   ppc.setPjPersona(x);
						   predioPersonaControlRepository.save(ppc);
					}
				}
			});
		}
	}
	
	public void deMaterializaConstitucionPersonaJuridica(Acto acto) {
	    List<ActoPredio> salidas = actoPredioRepository.findAllByActoIdAndTipoEntrada(acto.getId(), Constantes.TipoEntrada.SALIDA);
	    for (ActoPredio actoPredioSalida : salidas) {
			pjPersonaRepository.deleteByActoPredioId(actoPredioSalida.getId());
			actoPredioSalida.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada()));
			actoPredioRepository.save(actoPredioSalida);
	    }
	    
	    //OBTINE BITACORA
	    Optional<PersonaJuridicaBitacora> bitacora = personaJuridicaBitacoraService.findByActoId(acto.getId());
	    if(bitacora.isPresent()) {
	    	PersonaJuridicaBitacora pjBitacora = bitacora.get(); 
	    	PersonaJuridica pj =  pjBitacora.getPersonaJuridica();
	    	pj.setObjeto(pjBitacora.getObjeto());
	    	//pj.setCapital(pjBitacora.getCapital());
	    	pj.setTipoColectivo(pjBitacora.getTipoColectivo());
	    	pj.setOrgano(pjBitacora.getOrgano());
	    	personaJuridicaRepository.save(pj);
	    	personaJuridicaBitacoraService.deleteByActoId(acto.getId());
	    }
	    
	    
		//CONTROL PREDIO PERSONA (TITUALRES)
		List<PredioPersonaControl> personasExtintas  = predioPersonaControlRepository.findByActoId(acto.getId());
		personasExtintas.forEach(x->{
			PjPersona predioPersona =  x.getPjPersona();
			predioPersona.setExtinto(false);
			pjPersonaRepository.save(predioPersona);
			predioPersonaControlRepository.delete(x);
		});
		
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));
		actoRepository.save(acto);
	}

	private void save(HashMap<Integer, PjPersona> mapa, ActoPredio actoPredio) {
		for (Map.Entry<Integer, PjPersona> entry : mapa.entrySet()) {
			PjPersona pjPersona = entry.getValue();

			// crear persona si no se encuentra por curp o rfc si es persona moral
			log.debug("Buscando una persona tipo persona= {}, con curp={} o rfc={} ", pjPersona.getPersona().getTipoPersona().getId(), pjPersona.getPersona().getCurp(), pjPersona.getPersona().getRfc());

			Optional<Persona> persona2 = null;
			if (pjPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.FISICA.getAsInteger().intValue() && pjPersona.getPersona().getCurp() != null && pjPersona.getPersona().getCurp().length() >= 18) {
				persona2 = personaRepository.findByCurp(pjPersona.getPersona().getCurp());
			} else if (pjPersona.getPersona().getTipoPersona().getId().intValue() == Constantes.ETipoPersona.MORAL.getAsInteger().intValue() && pjPersona.getPersona().getRfc() != null && pjPersona.getPersona().getRfc().length() >= 12) {
				persona2 = personaRepository.findByRfc(pjPersona.getPersona().getRfc());
			}

			if (persona2 != null && persona2.isPresent()) {
				pjPersona.setPersona(persona2.get());
			} else {
				pjPersona.getPersona().setActivo(true);
				personaRepository.save(pjPersona.getPersona());
			}

			pjPersona.setDireccion(direccionRepository.findOne(1L));
			pjPersona.setActoPredio(actoPredio);
			pjPersonaRepository.save(pjPersona);
		}
	}

}