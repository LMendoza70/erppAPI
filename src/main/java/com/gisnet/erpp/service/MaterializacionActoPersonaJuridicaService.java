package com.gisnet.erpp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.PjPersona;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.EstadoCivilRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PjPersonaRepository;
import com.gisnet.erpp.repository.RegimenRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class MaterializacionActoPersonaJuridicaService {
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
	private final long TIPO_REL_PERSONA__CAMPO_ID = 316;
	private final long TIPO_ORGANO_CAMPO_ID = 317;

	private final long SOCIO_MODULO_ID = 123;
	private final long ORGANO_ADMINISTRACION_MODULO_ID = 187;
	private final long APODERADO_MODULO_ID = 146;

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

	public void materializaConstitucionPersonaJuridica(Acto acto) {
		// obtener campos dinamicos
		ActoPredio actoPredio = acto.getActoPrediosParaActosOrderedByVersion().first();

		if (actoPredio.getPersonaJuridica() == null) {
			throw new IllegalArgumentException("El acto no tiene un acto_predio");
		}

		log.debug("actoPredio= {}", actoPredio);

		Set<ActoPredioCampo> actoPredioCampos = actoPredio.getActoPredioCamposParaActoPredios();

		HashMap<Integer, PjPersona> socios = new HashMap<Integer, PjPersona>();
		HashMap<Integer, PjPersona> organosAdministracion = new HashMap<Integer, PjPersona>();
		HashMap<Integer, PjPersona> apoderados = new HashMap<Integer, PjPersona>();
		CampoValores organo = null;
		CampoValores tipoColectivo = null;

		log.debug("total de actoPredioCampos para acto = {}, {}", actoPredioCampos.size(), actoPredioCampos);

		for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
			long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();

			// actoPredioCampo.getValor());
			int index = actoPredioCampo.getOrden();

			log.debug("campoId={} moduloId={} valor={}", campoId, actoPredioCampo.getModuloCampo().getModulo().getId(), actoPredioCampo.getValor());

			if (actoPredioCampo.getValor() != null) {

				if (campoId == NOMBRE_CAMPO_ID || campoId == PATERNO_CAMPO_ID || campoId == MATERNO_CAMPO_ID || campoId == FECHA_NACIMIENTO_CAMPO_ID || campoId == RFC_CAMPO_ID || campoId == CURP_CAMPO_ID || campoId == ESTADO_CIVIL_CAMPO_ID || campoId == REGIMEN_CAMPO_ID || campoId == NACIONALIDAD_CAMPO_ID || campoId == COLECTIVO_ORGANO_CAMPO_ID || campoId == TIPO_ORGANO_CAMPO_ID || campoId == TIPO_REL_PERSONA__CAMPO_ID) {
					PjPersona pjPersona = null;
					if (actoPredioCampo.getModuloCampo().getModulo().getId() == SOCIO_MODULO_ID) {
						pjPersona = socios.get(index);
					} else if (actoPredioCampo.getModuloCampo().getModulo().getId() == ORGANO_ADMINISTRACION_MODULO_ID) {
						pjPersona = organosAdministracion.get(index);
					} else if (actoPredioCampo.getModuloCampo().getModulo().getId() == APODERADO_MODULO_ID) {
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
					} else if (campoId == TIPO_REL_PERSONA__CAMPO_ID) {
						if (actoPredioCampo.getValor() != null && actoPredioCampo.getValor().length() > 0) {
							pjPersona.setTipoRelPersona(tipoRelPersonaRepository.findByCampoValorId(Long.valueOf(actoPredioCampo.getValor())));
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
			}

		}

		log.debug("total de socios={}", socios.size());
		socios.forEach((index, pjPersona) -> {
			pjPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Constantes.TipoPersona.FISICA.getIdTipoPersona()));
			pjPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.SOCIO.getIdTipoRelPersona()));
			log.debug("socios nacionalidad{}, estado_civil{},, pesona={}", pjPersona.getNacionalidad(), pjPersona.getEstadoCivil(), pjPersona.getPersona());
		});

		log.debug("total de organosAdministracion={}", organosAdministracion.size());
		organosAdministracion.forEach((index, pjPersona) -> {
			pjPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Constantes.TipoPersona.FISICA.getIdTipoPersona()));
			log.debug("organosAdministracion colectivo={}, tipo={}, tipoRelPersona={}, pesona={}", pjPersona.getTipoRelPersona(), pjPersona.getPersona());
		});

		log.debug("total de apoderados={}", apoderados.size());
		apoderados.forEach((index, pjPersona) -> {
			pjPersona.getPersona().setTipoPersona(tipoPersonaRepositor.findOne(Constantes.TipoPersona.FISICA.getIdTipoPersona()));
			pjPersona.setTipoRelPersona(tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.APODERADO.getIdTipoRelPersona()));
			log.debug("apoderados  pesona={}", pjPersona.getPersona());
		});
		
		log.debug("organo = {} ", organo);
		log.debug("tipoColectivo = {} ", tipoColectivo);

		this.save(socios, actoPredio);
		this.save(organosAdministracion, actoPredio);
		this.save(apoderados, actoPredio);
		
		PersonaJuridica personaJuridica = actoPredio.getPersonaJuridica();
		personaJuridica.setOrgano(organo);
		personaJuridica.setTipoColectivo(tipoColectivo);
		personaJuridicaRepository.save(personaJuridica);

		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setFechaRegistro(new Date());
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
				persona2 = personaRepository.findByCurp(pjPersona.getPersona().getRfc());
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