package com.gisnet.erpp.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.Aviso;
import com.gisnet.erpp.domain.AvisoActoValorTipoActo;
import com.gisnet.erpp.domain.DiaInhabil;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.InhabilLocal;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Parametro;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Requisito;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.AvisoActoValorTipoActoRepository;
import com.gisnet.erpp.repository.AvisoRepository;
import com.gisnet.erpp.repository.NotarioRepository;
import com.gisnet.erpp.web.api.aviso.AvisoDTO;
import com.gisnet.erpp.web.api.aviso.ParteVO;
import com.gisnet.erpp.web.api.aviso.PersonaVO;
import com.gisnet.erpp.web.api.aviso.RequisitosAviso;
import com.ibm.icu.util.Calendar;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Service
public class AvisoService {
	private static final String CVE_VIGENCIA_CAUTELAR = "VIGENCIA_CAUTELAR";
	private static final String CVE_VIGENCIA_PREVENTIVA= "VIGENCIA_PREVENTIVA";
	private static final String CVE_TIPO_VIGENCIA = "TIPO_VIGENCIA";
	private static final String TIPO_HABIL = "HABIL";
	private static final Integer DIAS_CAUTELAR_DEF = 90;
	private static final Integer DIAS_PREVENT_DEF = 30;
	private static final String ZONE_MEXICO = "Mexico/General";
	private static final Long MODULO_PARTES = 168L;
	private static final int MC_NOMBRE = 526;
	private static final int MC_P_APELLIDO = 994;
	private static final int MC_S_APELLIDO = 995;
	private static final int MC_EN_CALIDAD_DE = 527;
	private static final String C_ACREDITADO = "395";
	private static final String C_ACREDITANTE = "396";
	private static final String C_ADQUIRENTE = "397";
	private static final String C_APODERADO = "975";
	private static final String C_ENAJENANTE = "398";
	private static final String C_FIDEICOMISARIO = "399";
	private static final String C_FIDEICOMITENTE = "400";
	private static final String C_FIDUCIARIO = "401";
	private static final String C_GARANTE = "402";
	private static final Integer[] MODS_ACREDITADO = { 314, 389, 740 };
	private static final Integer[] MODS_ACREDITANTE = { 388, 231, 739 };
	private static final Integer[] MODS_ADQUIRIENTE = { 172, 404, 338, 216, 382, 406, 342, 244, 720, 519 };
	private static final Integer[] MODS_APODERADO = { 191, 192, 479, 481, 430, 146, 148, 469 };
	// FALTA private static final Integer[] MODS_ENAJENTE = { 314, 389, 740 };
	private static final Integer[] MODS_FIDEICOMISARIO = { 157, 491, 557, 527, 536, 548 };
	private static final Integer[] MODS_FIDECOMITENTE = { 344, 554, 524, 533, 545, 516 };
	private static final Integer[] MODS_FIDUCIARIO = { 407, 156, 578, 530 };
	private static final Integer[] MODS_GARANTE = { 25, 390 };

	@Autowired
	private AvisoRepository repo;

	@Autowired
	private ParametroService paramService;

	@Autowired
	private DiaInhabilService diaInhabilService;

	@Autowired
	private InhabilLocalService inhabilLocalService;

	@Autowired
	private PrelacionService prelacionService;

	@Autowired
	private ActoRepository actoRepo;
	
	@Autowired
	private NotarioRepository notarioRepository;

	@Autowired
	private ActoDocumentoService actoDocumentoServ;

	@Autowired
	private ActoPredioCampoRepository actoPredioCampoRepo;

	@Autowired
	private ActoPredioCampoService apcService;

	@Autowired
	private AvisoActoValorTipoActoRepository avisoActoValorRepo;

	public Aviso guardarNuevo(Acto acto) {
		Prelacion prelacion = prelacionService.findOne(acto.getPrelacion().getId());
		Aviso aviso = new Aviso();
		aviso.setActo(acto);
		aviso.setTipoActo(acto.getTipoActo());
		aviso.setPrelacion(prelacion);
	/* 	Calendar calendar = Calendar.getInstance();
      	calendar.setTime(prelacion.getFechaIngreso()); 
		calendar.add(Calendar.DAY_OF_YEAR, 1);  		
		aviso.setFechaIni(calendar.getTime()); */
		aviso.setFechaIni(prelacion.getFechaIngreso());
		aviso.setFechaExp(calcularFecha(prelacion, acto));
		System.out.println("GUARDANDO AVISO CON ACTO ID: "+acto.getId());
		return repo.save(aviso);
	}

	public Long eliminarAviso(Aviso a){
		if(new Date().compareTo(a.getFechaExp()) == 0 || new Date().compareTo(a.getFechaExp()) > 0){
			return repo.deleteByActoId(a.getActo().getId());
		}
		return repo.deleteByActoId(a.getActo().getId());
	}

	private Date calcularFecha(Prelacion prelacion, Acto acto) {
		Parametro param = paramService.findByCve(CVE_TIPO_VIGENCIA);
		if (param == null || param.getValor() == null || param.getValor().equals(TIPO_HABIL)) {
			return calcularFechaHabil(prelacion, acto);
		}

		return calcularFechaInhabil(prelacion, acto);
	}

	private Date calcularFechaHabil(Prelacion prelacion, Acto acto) {
		int dias = obtenerDiasACalcular(acto);

		Instant instFechaInicio = prelacion.getFechaIngreso().toInstant();
		ZonedDateTime zdtFechaInicio = instFechaInicio.atZone(ZoneId.of(ZONE_MEXICO));
		LocalDate fechaInicio = zdtFechaInicio.toLocalDate();
		LocalDate fechaFin = fechaInicio.plusDays(0);

		int diasContados = 0;
		while(diasContados < dias) {
			fechaFin = fechaFin.plusDays(1);
			if (esDiaHabil(fechaFin, prelacion)) {
				diasContados++;
			}
		}

		return Date.from(fechaFin.atStartOfDay(ZoneId.of(ZONE_MEXICO)).toInstant());
	}

	private boolean esDiaHabil(LocalDate fecha, Prelacion prelacion) {
		switch(fecha.getDayOfWeek()) {
		case MONDAY:
		case TUESDAY:
		case WEDNESDAY:
		case THURSDAY:
		case FRIDAY:	
		case SATURDAY:
		case SUNDAY:
			return esDiaEntreSemanaHabil(fecha, prelacion);
	
		default:
			return false;
		}
	}

	private boolean esDiaEntreSemanaHabil(LocalDate fecha, Prelacion prelacion) {
		List<DiaInhabil> diaInhabil = diaInhabilService
				.findAllByMesAndDia(fecha.getMonthValue(), fecha.getDayOfMonth());
		if (diaInhabil == null || diaInhabil.isEmpty()) {
			return true;
		}

		if (diaInhabil.stream().anyMatch(d -> d.getTodoElEstado())) {
			return false;
		}

		return diaInhabil.stream().anyMatch(d -> esDiaHabilParaOficina(d, prelacion));
	}

	private boolean esDiaHabilParaOficina(DiaInhabil diaInhabil, Prelacion prelacion) {
		Oficina oficina = prelacion.getOficina();
		InhabilLocal local = inhabilLocalService.findAllByDiaInhabilAndOficina(diaInhabil, oficina);
		return local != null;
	}

	private int obtenerDiasACalcular(Acto acto) {
		switch(acto.getTipoActo().getId().intValue()) {
		case 49:
		case 210:
			return obtenerDiasCalcularPreventivo();

		case 50:		
			return obtenerDiasCalcularCautelar();

		default:
			return 0;
		}
	}

	private Integer obtenerDiasCalcularCautelar() {
		Parametro param = paramService.findByCve(CVE_VIGENCIA_CAUTELAR);
		if (param == null || param.getValor() == null || param.getValor().isEmpty()) {
			return DIAS_CAUTELAR_DEF;
		}

		return Integer.parseInt(param.getValor());
	}

	private int obtenerDiasCalcularPreventivo() {
		Parametro param = paramService.findByCve(CVE_VIGENCIA_PREVENTIVA);
		if (param == null || param.getValor() == null || param.getValor().isEmpty()) {
			return DIAS_PREVENT_DEF;
		}

		return Integer.parseInt(param.getValor());
	}

	private Date calcularFechaInhabil(Prelacion prelacion, Acto acto) {
		int dias = obtenerDiasACalcular(acto);

		Instant instFechaInicio = prelacion.getFechaIngreso().toInstant();
		ZonedDateTime zdtFechaInicio = instFechaInicio.atZone(ZoneId.of(ZONE_MEXICO));
		LocalDate fechaInicio = zdtFechaInicio.toLocalDate();
		LocalDate fechaFin = fechaInicio.plusDays(0);

		int diasContados = 0;
		while(diasContados < dias) {
			fechaFin = fechaFin.plusDays(1);
			if (!esDiaHabil(fechaFin, prelacion)) {
				diasContados++;
			}
		}

		return Date.from(fechaFin.atStartOfDay(ZoneId.of(ZONE_MEXICO)).toInstant());
	}
	public Aviso buscarAvisoPorActoId(Long actoId) {
		Aviso aviso = repo.findOneByActoId(actoId);
		return aviso;
	}
	public Aviso buscarPorActoId(Long actoId) {
		Aviso aviso = repo.findOneByActoId(actoId);
		if (aviso == null) {
			Acto acto = actoRepo.findOne(actoId);
			if (acto == null || acto.getStatusActo().getId()==3L) return null;
			return guardarNuevo(acto);
		}
 
		return aviso;
	}

	public List<Acto> buscarActosCaducados() {
		Date fecha = Date.from(Instant.now());
		return repo.findActosActivosVigenciaCaduca(fecha);
	}

	public RequisitosAviso prelacionCumpleRequisitoAviso(List<ActoPredio> actosPredio, AvisoDTO aviso, ActoPredio actoPredioAviso) {
		RequisitosAviso requisito = new RequisitosAviso();
		System.out.println("prelacionCumpleRequisitoAviso");
		//requisito.setCumpleActos(prelacionCumpleActos(actosPredio, actoPredioAviso));
		//requisito.setCumpleNotario(true);
		requisito.setCumpleActos(true);
		requisito.setCumplePartes(true);	

		if (aviso.getActo().getTipoActo().getId().equals(50L)) {
			requisito.setCumpleDocumento(prelacionCumpleDocumento(actosPredio, actoPredioAviso));
		}
		if (aviso.getActo().getTipoActo().getId().equals(49L) || aviso.getActo().getTipoActo().getId().equals(210L)) {
			
			requisito.setCumpleDocumento(true);
			
			List<ActoPredioCampo> apcs = actoPredioCampoRepo.findAllByActoPredio(actoPredioAviso);
			Notario notarioPrimerAviso = null;
			
			if(apcs!=null) {
				for(ActoPredioCampo apc:apcs) {
					int moduloCampoAviso = apc.getModuloCampo().getId().intValue();
					switch(moduloCampoAviso) {
						case 1310:
							if(apc.getValor()!=null && !apc.getValor().isEmpty()) {
							Long id = Long.parseLong(apc.getValor());
							notarioPrimerAviso = notarioRepository.findOne(id);
							break;
						}
					}
				}
			}
			else {
				requisito.setCumpleNotario(false);
				return requisito;
			}
			if(notarioPrimerAviso!=null) {
				boolean cumple = true;
				List<Documento> documentosActos = agruparDocumentos(actosPredio);
				
				System.out.println("documentosActos "+ documentosActos);
				cumple = cumple && existeNotario(notarioPrimerAviso, documentosActos);
				System.out.println("cumpleNotarioPrimerAviso? "+ cumple);
				requisito.setCumpleNotario(cumple);
				return requisito;
			}
			else {
				requisito.setCumpleNotario(false);
				return requisito;
			}
		}
		requisito.setCumpleNotario(prelacionCumpleNotario(actosPredio, actoPredioAviso));
		
		System.out.println("cumpleNotario? "+ requisito.isCumpleNotario());
		System.out.println("cumpleDocumento? "+ requisito.isCumpleDocumento());
		return requisito;
	}

	private boolean prelacionCumpleActos(List<ActoPredio> actosPredio, ActoPredio actoPredioAviso) {
		boolean cumple = true;

		List<ActoPredioCampo> campos = apcService.getActoPredioCampoByActoPredio(actoPredioAviso);

		for (ActoPredioCampo campo : campos) {
			int moduloCampo = campo.getModuloCampo().getId().intValue();
			System.out.println("modulo "+moduloCampo+" acto predio campo "+campo+" valor "+campo.getValor());
			if(moduloCampo==3416 || moduloCampo==39){cumple = cumple && revisarPredioCumplaActo(actosPredio, campo.getValor());}
			if(moduloCampo==3417 || moduloCampo==40){cumple = cumple && revisarPredioCumplaActo(actosPredio, campo.getValor());}
			if(moduloCampo==3418 || moduloCampo==38){cumple = cumple && revisarPredioCumplaActo(actosPredio, campo.getValor());}
			if(moduloCampo==3419 || moduloCampo==37){cumple = cumple && revisarPredioCumplaActo(actosPredio, campo.getValor());}
			if(moduloCampo==3420 || moduloCampo==41){cumple = cumple && revisarPredioCumplaActo(actosPredio, campo.getValor());}
			if(moduloCampo==3421 || moduloCampo==42){cumple = cumple && revisarPredioCumplaActo(actosPredio, campo.getValor());}

		/* 	switch (moduloCampo) {
			case 3416:
			case 3417://ó 40
			case 3418:
			case 3419:
			case 3420:
			case 3421:
				cumple = cumple && revisarPredioCumplaActo(actosPredio, campo.getValor());
			} */

			if (!cumple) break;
		}

		return cumple;
	}

	private boolean revisarPredioCumplaActo(List<ActoPredio> actosPredioPrelacion, String valor) {

		List<AvisoActoValorTipoActo> actoValores = avisoActoValorRepo.findByCampoValoresId(Long.parseLong(valor));

		List<Long> tiposActosRequeridos = actoValores.stream()
				.map(av -> av.getTipoActo().getId())
				.collect(Collectors.toList());

		List<Acto> actos = actosPredioPrelacion.stream().map(ap -> ap.getActo()).collect(Collectors.toList());

		return actos.stream()
				.anyMatch(a ->
					tiposActosRequeridos.stream().anyMatch(tipo -> tipo.equals(a.getTipoActo().getId()))
					);
	}

	private boolean prelacionCumpleNotario(List<ActoPredio> actosPredio, ActoPredio actoPredioAviso) {
		System.out.println("****************prelacionCumpleNotario**********\n\n");
		System.out.println("List<ActoPredio> actosPredio "+actosPredio);
		System.out.println("ActoPredio actoPredioAviso "+ actoPredioAviso);

		boolean cumple = true;
		Acto actoAviso = actoPredioAviso.getActo();
		List<Documento> documentosAviso = actoDocumentoServ.findDocumentosByActoId(actoAviso.getId())
				.stream().map(ActoDocumento::getDocumento).collect(Collectors.toList());

		System.out.println("documentosAviso "+ documentosAviso);

		List<Documento> documentosActos = agruparDocumentos(actosPredio);
		
		System.out.println("documentosActos "+ documentosActos);

		for (Documento documentoAviso : documentosAviso) {
			System.out.println("documentoAviso "+ documentoAviso);
			cumple = cumple && existeNotario(documentoAviso.getNotario(), documentosActos);
			System.out.println("cumple? "+ cumple);
			if (!cumple) break;
		}

		return cumple;
	}

	private List<Documento> agruparDocumentos(List<ActoPredio> actosPredio) {
		List<Documento> documentos = new ArrayList<>();
		for (ActoPredio actoPredio : actosPredio) {
			List<Documento> documentosActo = actoDocumentoServ.findDocumentosByActoId(actoPredio.getActo().getId())
					.stream().map(ActoDocumento::getDocumento).collect(Collectors.toList());
			documentos.addAll(documentosActo);
		}

		return documentos;
	}

	private boolean existeNotario(Notario notario, List<Documento> documentosActos) {
		/*
		*******CODIGO ORIGINAL*******
		Long notarioId = notario.getId();	
		return documentosActos.stream().filter(d -> d.getNotario() != null).anyMatch(d -> notarioId.equals(d.getNotario().getId()));
		*/

		//por  no_notario y municipio
		try {
			Integer noNotario = notario.getNoNotario();	
			Long noMunicipio = notario.getMunicipio().getId();
			return documentosActos.stream().filter(d -> d.getNotario() != null).anyMatch(d -> noNotario.equals(d.getNotario().getNoNotario()) && noMunicipio.equals(d.getMunicipio().getId()));	
		}catch(Exception e) {
			return false;
		}
		

	}

	private boolean prelacionCumpleDocumento(List<ActoPredio> actosPredio, ActoPredio actoPredioAviso) {
		boolean cumple = true;
		Acto actoAviso = actoPredioAviso.getActo();
		System.out.println("acto---"+actoAviso.getId());
		List<Documento> documentosAviso = actoDocumentoServ.findDocumentosByActoId(actoAviso.getId())
				.stream().map(ActoDocumento::getDocumento).collect(Collectors.toList());

		List<Documento> documentosActos = agruparDocumentos(actosPredio);

		for (Documento documentoAviso : documentosAviso) {
			System.out.println("documentoAviso-"+documentoAviso);
			cumple = cumple && existeNumeroDocumento(documentoAviso, documentosActos);
			//if (!cumple) break;
		}

		return cumple;
	}

	private boolean existeNumeroDocumento(Documento documentoAviso, List<Documento> documentosActos) {
		String numero = String.valueOf(documentoAviso.getNumero2()).trim();
		System.out.println("numero de doc "+numero);
		for(Documento d:documentosActos){System.out.println("docc--"+d);}
		return documentosActos.stream().filter(d -> d.getNumero2() != null).anyMatch(d -> numero.equals(String.valueOf(d.getNumero2()).trim()));
	}

	private boolean prelacionCumpleRequisito(List<ActoPredio> actosPredio, ActoPredio actoPredioAviso) {
		boolean cumple = true;

		List<ParteVO> partes = obtenerPartes(actoPredioAviso);
		for (ParteVO parte : partes) {
			cumple = cumple && prelacionCumpleParte(actosPredio, parte);
		}

		return cumple;
	}

	private List<ParteVO> obtenerPartes(ActoPredio actoPredioAviso) {
		HashMap<Integer, ParteVO> partesHash = new HashMap<>();

		List<ActoPredioCampo> valoresPartes = actoPredioCampoRepo.findAllByActoPredioIdAndModuloCampoModuloId(actoPredioAviso.getId(), MODULO_PARTES);

		for (ActoPredioCampo valor : valoresPartes) {
			Integer orden = valor.getOrden();
			if (!partesHash.containsKey(orden)) {
				partesHash.put(orden, new ParteVO());
			}

			ParteVO parte = partesHash.get(orden);

			switch (valor.getModuloCampo().getId().intValue()) {
				case MC_NOMBRE:
					parte.setNombre(valor.getValor());
					break;
				case MC_P_APELLIDO:
					parte.setPrimerApellido(valor.getValor());
					break;
				case MC_S_APELLIDO:
					parte.setSegundoApellido(valor.getValor());
					break;
				case MC_EN_CALIDAD_DE:
					parte.setEnCalidadDe(valor.getValor());
					break;
			}
		}

		List<ParteVO> partes = new ArrayList<>();
		List<Integer> ordenes = partesHash.keySet().stream().sorted().collect(Collectors.toList());

		for (Integer orden : ordenes) {
			partes.add(partesHash.get(orden));
		}

		return partes;
	}

	private boolean prelacionCumpleParte(List<ActoPredio> actosPredio, ParteVO parte) {
		switch(parte.getEnCalidadDe()) {
			case C_ACREDITADO:
				return prelacionCumpleAcreditado(actosPredio, parte);
			case C_ACREDITANTE:
				return prelacionCumpleAcreditante(actosPredio, parte);
			case C_ADQUIRENTE:
				return prelacionCumpleAdquiriente(actosPredio, parte);
			case C_APODERADO:
				return prelacionCumpleApoderado(actosPredio, parte);
			case C_ENAJENANTE:
				return prelacionCumpleEnajente(actosPredio, parte);
			case C_FIDEICOMISARIO:
				return prelacionCumpleFideicomisario(actosPredio, parte);
			case C_FIDEICOMITENTE:
				return prelacionCumpleFideiComitente(actosPredio, parte);
			case C_FIDUCIARIO:
				return prelacionCumpleFiduciario(actosPredio, parte);
			case C_GARANTE:
				return prelacionCumpleGarante(actosPredio, parte);
		}

		return false;
	}

	private boolean prelacionCumpleAcreditado(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_ACREDITADO);
		System.out.println("Parte: " + parte.getNombreCompleto());

		for (PersonaVO persona : acreditados) {
			System.out.println("Persona: " + persona.getNombreCompleto());
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private boolean prelacionCumpleAcreditante(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_ACREDITANTE);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private boolean prelacionCumpleAdquiriente(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_ADQUIRIENTE);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private boolean prelacionCumpleApoderado(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_APODERADO);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private boolean prelacionCumpleEnajente(List<ActoPredio> actosPredio, ParteVO parte) {
		//TODO
		return false;
		/*
		List<PersonaVO> acreditados = obtenerPersonasPartes(MODS_ACREDITADO);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;*/
	}

	private boolean prelacionCumpleFideicomisario(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_FIDEICOMISARIO);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private boolean prelacionCumpleFideiComitente(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_FIDECOMITENTE);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private boolean prelacionCumpleFiduciario(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_FIDUCIARIO);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private boolean prelacionCumpleGarante(List<ActoPredio> actosPredio, ParteVO parte) {
		List<PersonaVO> acreditados = obtenerPersonasPartes(actosPredio, MODS_GARANTE);

		for (PersonaVO persona : acreditados) {
			if (persona.cumpleParte(parte)) {
				return true;
			}
		}

		return false;
	}

	private List<PersonaVO> obtenerPersonasPartes(List<ActoPredio> actosPredioPrelacion, Integer[] modulos) {
		List<PersonaVO> personas = new ArrayList<>();
		for (ActoPredio actoPredio : actosPredioPrelacion) {
			System.out.println(actoPredio.getActo().getTipoActo().getNombre());
			for (Integer modulo : modulos) {
				HashMap<Integer, PersonaVO> personasHash = new HashMap<>();
				List<ActoPredioCampo> valoresPersonas= actoPredioCampoRepo.findAllByActoPredioIdAndModuloCampoModuloId(actoPredio.getId(), (long)modulo);

				for (ActoPredioCampo valor : valoresPersonas) {
					Integer orden = valor.getOrden();
					if (!personasHash.containsKey(orden)) {
						personasHash.put(orden, new PersonaVO());
					}

					PersonaVO persona = personasHash.get(orden);

					switch (valor.getModuloCampo().getModulo().getId().intValue()) {
						case 314:
						case 389:
						case 739:
						case 740:
						case 244:
						case 390:
						case 191:
						case 192:
						case 479:
						case 481:
						case 430:
						case 469:
							switch(valor.getModuloCampo().getCampo().getId().intValue()) {
								case 1286:
									persona.setNombre(valor.getValor());
									break;
								case 1287:
									persona.setPrimerApellido(valor.getValor());
									break;
								case 1288:
									persona.setSegundoApellido(valor.getValor());
									break;
							}
							break;
						case 388:
						case 231:
						case 172:
						case 404:
						case 338:
						case 216:
						case 382:
						case 406:
						case 342:
						case 720:
						case 519:
						case 554:
						case 524:
						case 533:
						case 516:
						case 578:
						case 530:
						case 146:
							switch(valor.getModuloCampo().getCampo().getId().intValue()) {
								case 286:
									persona.setNombre(valor.getValor());
									break;
								case 287:
									persona.setPrimerApellido(valor.getValor());
									break;
								case 288:
									persona.setSegundoApellido(valor.getValor());
									break;
							}
							break;
						case 157:
						case 491:
						case 557:
						case 527:
						case 536:
						case 548:
							switch(valor.getModuloCampo().getCampo().getId().intValue()) {
								case 675:
									persona.setNombre(valor.getValor());
									break;
								case 676:
									persona.setPrimerApellido(valor.getValor());
									break;
								case 677:
									persona.setSegundoApellido(valor.getValor());
									break;
							}
							break;
						case 344:
						case 407:
							switch(valor.getModuloCampo().getCampo().getId().intValue()) {
								case 40:
									persona.setNombre(valor.getValor());
									break;
							}
							break;
						case 156:
							switch(valor.getModuloCampo().getCampo().getId().intValue()) {
								case 671:
									persona.setNombre(valor.getValor());
									break;
							}
							break;
						case 25:
							switch(valor.getModuloCampo().getCampo().getId().intValue()) {
								case 659:
									persona.setNombre(valor.getValor());
									break;
								case 660:
									persona.setPrimerApellido(valor.getValor());
									break;
								case 661:
									persona.setSegundoApellido(valor.getValor());
									break;
							}
							break;
						case 148:
							switch(valor.getModuloCampo().getCampo().getId().intValue()) {
								case 247:
									persona.setNombre(valor.getValor());
									break;
							}
							break;
					}
				}

				List<Integer> ordenes = personasHash.keySet().stream().sorted().collect(Collectors.toList());

				for (Integer orden : ordenes) {
					personas.add(personasHash.get(orden));
				}
			}
		}

		return personas;
	}

}
