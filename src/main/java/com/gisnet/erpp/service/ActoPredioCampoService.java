package com.gisnet.erpp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongPredicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ModuloCampo;
import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ActoFolioReciboRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.ModuloCampoRepository;
import com.gisnet.erpp.repository.ModuloTipoActoRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class ActoPredioCampoService {
	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;
	@Autowired
	ActoPredioRepository actoPredioRepository;
	@Autowired
	ModuloCampoRepository moduloCampoRepository;
	@Autowired
	PrelacionContratanteService prelacionContratanteService;

	@Autowired
	ReciboService reciboService;

	@Autowired
	ActoFolioReciboRepository actoFolioReciboRepository;

	@Autowired
	ModuloCampoService moduloCampoService;

	@Autowired
	ActoService actoService;

	@Autowired
	TipoEntradaRepository tipoEntradaRepository;

	@Autowired
	PredioPersonaService predioPersonaService;

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
	PrelacionService prelacionService;

	@Autowired
	ActoRepository actoRepository;
	
	@Autowired
	BitacoraMantenimientoService bitacoraMantenimientoService;
	
	@Autowired
	UsuarioService usuarioService;

	private static final long[] MODULOS_PRELACION_CONTRATANTE = { 172, 216, 234, 244 };

	@Transactional(readOnly = true)
	public Page<ActoPredioCampo> findAll(Pageable pageable) {
		return actoPredioCampoRepository.findAll(pageable);
	}

	@Transactional
	public Long save(List<ActoPredioCampo> actoPredioCampo, Long cambiaVersion, Long tipoEntradaId,boolean mantenimiento) {
		if (!actoPredioCampo.isEmpty()) {
			List<Long> ids = new ArrayList<Long>(actoPredioCampo.size());
			actoPredioCampo.forEach(listaItem -> ids.add(listaItem.getModuloCampo().getId()));
			Hashtable<Long, ModuloCampo> reattached = moduloCampoService.findByIds(ids);
			for (ActoPredioCampo valor : actoPredioCampo) {
				valor.setModuloCampo(reattached.get(valor.getModuloCampo().getId()));
			}

			ActoPredioCampo muestra = actoPredioCampo.get(0);
			long actoPredioId = muestra.getActoPredio().getId();
			ActoPredio actoPredio = actoPredioRepository.findOne(actoPredioId);
			
			
			if (cambiaVersion != 0) {
				ActoPredio newActoPredio = new ActoPredio();

				newActoPredio.setPredio(actoPredio.getPredio());
				newActoPredio.setPersonaJuridica(actoPredio.getPersonaJuridica());
				newActoPredio.setFolioSeccionAuxiliar(actoPredio.getFolioSeccionAuxiliar());
				newActoPredio.setMueble(actoPredio.getMueble());
				newActoPredio.setActo(actoPredio.getActo());
				if(mantenimiento)
					tipoEntradaId = Constantes.TipoEntrada.ENTRADA.getIdTipoEntrada();
				
				newActoPredio.setTipoEntrada(tipoEntradaRepository.findOne(tipoEntradaId));
				newActoPredio.setTipoFolio(actoPredio.getTipoFolio());
				newActoPredio.setVersion((actoPredio.getVersion() == null ? 0 : actoPredio.getVersion()) + 1);
				newActoPredio = actoPredioRepository.save(newActoPredio);
				if(mantenimiento) {
					Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
					String comentario = "ACTUALIZO LA CAPTURA DEL ACTO "+actoPredio.getActo().getTipoActo().getNombre()+ " DE FECHA: "
					+ UtilFecha.formatToDatePattern(actoPredio.getActo().getFechaRegistro());
					bitacoraMantenimientoService.create(usuario,actoPredio,comentario,"UPDATE_CAPTURA");
				}

				for (ActoPredioCampo valor : actoPredioCampo) {
					if (valor == null || valor.getValor() == null) {
						continue;
					}

					ActoPredioCampo nuevo = new ActoPredioCampo();

					colocarValorANuevoCampo(nuevo, valor);

					nuevo.setOrden(valor.getOrden());
					nuevo.setModuloCampo(valor.getModuloCampo());
					nuevo.setActoPredio(newActoPredio);
					actoPredioCampoRepository.save(nuevo);

					if (campoEsDeContratante(nuevo) && nuevo.getValor() != null) {
						prelacionContratanteService.guardarCampoDeContratante(nuevo);
					}

					if (valor.getModuloCampo().getCampo().getTipoCampo()
							.getId() == Constantes.TipoCampo.ASOCIA_ACTO_RECIBO.getIdTipoCampo()) {
						reciboService.saveActoFoliosFromIds(valor.getValor(), newActoPredio);
					}

					if (valor.getModuloCampo().getCampo().getTipoCampo()
							.getId() == Constantes.TipoCampo.FECHA_REGISTRO_ACTO.getIdTipoCampo()) {
						actoService.updateFechaRegistro(actoPredio.getActo().getId(),
								UtilFecha.toDate(valor.getValor(), Constantes.FECHA_FORMATO_CAMPO_VALOR));
					}
				}

				if (actoPredio.getActo().getStatusActo().getId() == 1L
						&& !actoPredio.getPredioPersonasParaActoPredios().isEmpty()) {

					Set<PredioPersona> listPredioPersona = actoPredio.getPredioPersonasParaActoPredios();

					for (PredioPersona obj : listPredioPersona) {

						PredioPersona predioPersona = new PredioPersona();
						predioPersona.setActoPredio(newActoPredio);
						predioPersona.setPorcentajeDd(obj.getPorcentajeDd());
						predioPersona.setPorcentajeUv(obj.getPorcentajeUv());
						predioPersona.setNacionalidad(obj.getNacionalidad());
						predioPersona.setPersona(obj.getPersona());
						predioPersona.setTipoRelPersona(obj.getTipoRelPersona());

						predioPersonaService.savePredioPersona(predioPersona);

					}

				}

				return newActoPredio.getId();
			} else {
				List<ActoPredioCampo> datos = actoPredioCampoRepository.findByActoPredioId(actoPredioId);

				for (ActoPredioCampo valor : actoPredioCampo) {
					if (valor == null || valor.getValor() == null) {
						continue;
					}

					int i = datos.indexOf(valor);
					if (i >= 0) {
						datos.remove(i);
					}

					aplicarMayusculaAValor(valor);
					actoPredioCampoRepository.save(valor);

					if (campoEsDeContratante(valor) && valor.getValor() != null) {
						prelacionContratanteService.guardarCampoDeContratante(valor);
					}

					if (valor.getModuloCampo().getCampo().getTipoCampo()
							.getId() == Constantes.TipoCampo.ASOCIA_ACTO_RECIBO.getIdTipoCampo()) {
						actoFolioReciboRepository.deleteByActoPredioId(valor.getActoPredio().getId());
						reciboService.saveActoFoliosFromIds(valor.getValor(), valor.getActoPredio());
					}

					if (valor.getModuloCampo().getCampo().getTipoCampo()
							.getId() == Constantes.TipoCampo.FECHA_REGISTRO_ACTO.getIdTipoCampo()) {
						actoService.updateFechaRegistro(actoPredio.getActo().getId(),
								UtilFecha.toDate(valor.getValor(), Constantes.FECHA_FORMATO_CAMPO_VALOR));
					}

				}
				actoPredioCampoRepository.delete(datos);
			}
		}
		return 1l;
	}

	private boolean campoEsDeContratante(ActoPredioCampo nuevo) {
		ModuloCampo modCampo = nuevo.getModuloCampo();
		LongPredicate longPred = (l) -> l == modCampo.getModulo().getId();
		return Arrays.stream(MODULOS_PRELACION_CONTRATANTE).anyMatch(longPred);
	}

	private void aplicarMayusculaAValor(ActoPredioCampo valor) {
		ModuloCampo condiciones = valor.getModuloCampo();

		if (condiciones != null && condiciones.getMayusculas() != null && condiciones.getMayusculas() && valor != null
				&& valor.getValor() != null) {
			valor.setValor(valor.getValor().toUpperCase());
		}
	}

	private void colocarValorANuevoCampo(ActoPredioCampo nuevo, ActoPredioCampo valor) {
		ModuloCampo condiciones = valor.getModuloCampo();

		if (condiciones != null && condiciones.getMayusculas() != null && condiciones.getMayusculas() && valor != null
				&& valor.getValor() != null) {
			nuevo.setValor(valor.getValor().toUpperCase());
		} else {
			nuevo.setValor(valor.getValor());
		}
	}

	/*
	 * @Transactional(readOnly = true) public Map<Long, Set< ActoPredioCampo >>
	 * findActosModulosCamposByActoIdModuloId(Long actoPredioId, Long moduloId) {
	 * return null; }
	 */

	@Transactional(readOnly = true)
	public Map<Long, Set<ActoPredioCampo>> findActosModulosCamposByActoIdModuloId(Long actoPredioId, Long moduloId) {

		return actoPredioCampoRepository.getActoPredioCampoValuesByActoPredioIdModuloId(actoPredioId, moduloId);
	}

	public List<ActoPredioCampo> getActoPredioCampoByActoPredio(ActoPredio actoPredio) {

		return actoPredioCampoRepository.findAllByActoPredio(actoPredio);

	}

	public void ModificaActoPredioCampo(ActoPredioCampo apc) {

		actoPredioCampoRepository.save(apc);

	}

	public void DeleteActoPredioCampo(ActoPredioCampo apc) {

		actoPredioCampoRepository.delete(apc);

	}

	public boolean UpdateTitulares(Long actoPredioId) {

		ActoPredio actoPredio = actoPredioRepository.findOne(actoPredioId);

		List<PredioPersona> predioPersonas = predioPersonaService
				.findPropietariosActuales(actoPredio.getPredio().getId(), true);

		List<ActoPredioCampo> actoPredioCampos = actoPredioCampoRepository.findByActoPredioId(actoPredioId).stream()
				.filter(item -> item.getModuloCampo().getId() == 537L || item.getModuloCampo().getId() == 538L
						|| item.getModuloCampo().getId() == 539L || item.getModuloCampo().getId() == 1013L
						|| item.getModuloCampo().getId() == 1014L || item.getModuloCampo().getId() == 1015L)
				.collect(Collectors.toList());
		int personasAPC = 0;

		for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
			personasAPC = actoPredioCampo.getOrden();
		}

		if (personasAPC == predioPersonas.size()) {
			int contador = 1;

			for (PredioPersona predioPersona : predioPersonas) {

				for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {

					if (contador == actoPredioCampo.getOrden()) {

						switch (actoPredioCampo.getModuloCampo().getId().intValue()) {
						case 537: // nombre 172
							actoPredioCampo.setValor(predioPersona.getPersona().getNombre());
							break;
						case 538: // paterno 172
							actoPredioCampo.setValor(predioPersona.getPersona().getPaterno());
							break;
						case 539: // materno 172
							actoPredioCampo.setValor(predioPersona.getPersona().getMaterno());
							break;
						case 1013: // nombre 503
							actoPredioCampo.setValor(predioPersona.getPersona().getNombre());
							break;
						case 1014: // paterno 503
							actoPredioCampo.setValor(predioPersona.getPersona().getPaterno());
							break;
						case 1015: // materno 503
							actoPredioCampo.setValor(predioPersona.getPersona().getMaterno());
							break;

						}

						actoPredioCampoRepository.save(actoPredioCampo);

					}

				}
				contador++;
			}

		} else {
			System.out.println("El numero de titulares del acto no corresponde con predio persona");

			return false;
		}

		return true;
	}

	public ArrayList<ActoPredioCampo> getActoPredioCampo(Acto acto) {
		ArrayList<ActoPredioCampo> actoPredioCampos = null;
		if (acto != null) {
			actoPredioCampos = new ArrayList();
			ActoPredio actoPredio = actoPredioRepository.findActoPredioByLastVersion(acto.getId());
			// log.debug("BolRegSer:356 {}", actoPredio);
			for (ActoPredioCampo apc : actoPredioCampoRepository
					.findByActoPredioIdOrderByModuloCampoCampoId(actoPredio.getId())) {
				actoPredioCampos.add(apc);
				// log.debug("BolRegSer:360{}", apc);
			}
		}
		return actoPredioCampos;
	}

	public String getObservacionesActo(Acto acto) {

		String cdnObs = "";
	
							cdnObs += "\n\n" + "ACTO" + ": " + acto.getTipoActo().getNombre()
									+ " , FECHA DE INSCRIPCION: " + UtilFecha.formatToDatePattern(
											prelacionService.buscaFechaInscripcion(acto.getId()))
									+ "\n\n";
							cdnObs += moduloCampoFor(getActoPredioCampo(acto));
		
		return cdnObs;
	}

	private String moduloCampoFor(List<ActoPredioCampo> actoPredioCampos) {

		List<ModuloCampo> moduloCampo = new ArrayList<ModuloCampo>();
		String cdnObs = "";
		int index = 0;

		// Campos no incluidos por ID MODULO_CAMPO_ID
		List<Long> camposNoIncluidos = new ArrayList<Long>();
		camposNoIncluidos.add(Constantes.MODULO_CAMPO_CHECK_ACTO);
		camposNoIncluidos.add(Constantes.MODULO_CAMPO_ACTO);

		// camposNoIncluidos.add(1402L);
		// camposNoIncluidos.add(942L);
		// camposNoIncluidos.add(20103L);
		// camposNoIncluidos.add(20104L);
		// camposNoIncluidos.add(20105L);
		// camposNoIncluidos.add(843L);
		// camposNoIncluidos.add(844L);
		// camposNoIncluidos.add(845L);
		// camposNoIncluidos.add(20113L);

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

		// Campos no incluidos por Etiqueta
		List<String> camposNoIncluidosPer = new ArrayList<String>();
		camposNoIncluidosPer.add("TIPO DE PERSONA");
		camposNoIncluidosPer.add("NACIONALIDAD");

		// Etiquetas No Incluidas
		List<String> camposNoIncluidosPerEtiqueta = new ArrayList<String>();
		// camposNoIncluidosPer.add("TIPO DE PERSONA");
		camposNoIncluidosPerEtiqueta.add("NACIONALIDAD");
		camposNoIncluidosPerEtiqueta.add("NOMBRE(S) / DENOMINACIÓN");
		camposNoIncluidosPerEtiqueta.add("PRIMER APELLIDO");
		camposNoIncluidosPerEtiqueta.add("SEGUNDO APELLIDO");
		camposNoIncluidosPerEtiqueta.add("NOMBRE(S)/DENOMINACIÓN DESIGNANTE");
		camposNoIncluidosPerEtiqueta.add("DESCRIPCION SERVIDUMBRE");
		camposNoIncluidosPerEtiqueta.add("MOTIVO CANCELACIÓN");

		actoPredioCampos = actoPredioCampos.stream()
				.filter(x -> !camposNoIncluidos.contains(x.getModuloCampo().getId())
						&& !camposNoIncluidosPer.contains(x.getModuloCampo().getEtiqueta()))
				.collect(Collectors.toList());

		for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
			moduloCampo.add(actoPredioCampo.getModuloCampo());
		}

		Long idMod = 0L;
		// for(ModuloCampo mod : moduloCampo) {

		for (ActoPredioCampo apc : actoPredioCampos) {

			ModuloCampo mod = apc.getModuloCampo();

			if (mod.getId() == apc.getModuloCampo().getId()) {

				if (idMod != mod.getModulo().getId()) {

					List<ModuloTipoActo> mta = moduloTipoActoRepository.findAllByTipoActoIdAndModuloId(
							apc.getActoPredio().getActo().getTipoActo().getId(), mod.getModulo().getId());

					cdnObs += "" + mta.get(0).getEtiqueta() + " // ";
					// cdnObs += "" + mod.getModulo().getNombre()+" // ";
				}

//    			log.debug("===> MODULO " + mod.getModulo().getNombre() + " ---- ID : " + mod.getModulo().getId() );	
//        		log.debug("===> MODULO_CAMPO " + mod.getEtiqueta() + " ----  ID : " + mod.getId() );
//        		log.debug("===> VALOR " + obtenerValor(apc));

				cdnObs += "" + apc.getModuloCampo().getEtiqueta() + ": ";
				cdnObs += obtenerValor(apc) + ", ";

//        		if(!camposNoIncluidosPerEtiqueta.contains(mod.getEtiqueta())) {
//        			
//        			if(!mod.getModulo().getNombre().equals(mod.getEtiqueta())){
//        				cdnObs += "" + mod.getEtiqueta() + ": " ;	
//        			}
//        			cdnObs += obtenerValor(apc) + ", " ;
//        		}else if(camposNoIncluidosPerEtiqueta.contains(mod.getEtiqueta())) {
//        			cdnObs += obtenerValor(apc) + " " ;
//        		}else {
//        			cdnObs += ","+obtenerValor(apc) + "," ;
//        		}

				idMod = mod.getModulo().getId();

			}

		}

//		if(idMod != mod.getModulo().getId()) {
//			cdnObs += "\n";
//		}

		// }

		return cdnObs;
	}

	public String obtenerValor(ActoPredioCampo apc) {
		if (apc == null || apc.getValor() == null) {
			return null;
		}

		try {
			int tipo = apc.getModuloCampo().getCampo().getTipoCampo().getId().intValue();
			switch (tipo) {
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
				return orientacionService.findAll().stream()
						.filter(o -> o.getId().equals(Long.parseLong(apc.getValor()))).findFirst().get().getNombre();

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
		return campoValorService.findByTipoCampoId(campoId).stream().filter(cv -> cv.getId().equals(id)).findFirst()
				.get().getNombre();
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
//	StringBuilder v = new StringBuilder("Prelacion: ");
//	v.append(a.getPrelacion().getConsecutivo());
		StringBuilder v = new StringBuilder(" ");
		// v.append(" Acto: ");
		v.append(a.getTipoActo().getNombre());
		v.append(" - Fecha ");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		v.append(simpleDateFormat.format(prelacionService.buscaFechaInscripcion(a.getId())));
//	v.append(" - Orden: ");
//	v.append(a.getOrden());
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

}
