package com.gisnet.erpp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.web.api.imprimeprelacion.caratula.ModuloVO;
import com.gisnet.erpp.web.api.imprimeprelacion.caratula.ValoresActo;
import com.gisnet.erpp.web.api.tipoacto.ModuloDTO;
import com.gisnet.erpp.web.api.tipoacto.TipoActoDTO;

@Service
public class FormasPreCodificadasService {
	private static final String DOCUMENTO = "DOCUMENTO";
	private static final String C_REGISTRADOR = "C. REGISTRADOR";

	@Autowired
	private TipoPersonaService tipoPersonaService;

	@Autowired
	private ParentescoService parentescoService;

	@Autowired
	private NacionalidadService nacionalidadService;

	@Autowired
	private OrientacionService orientacionService;

	@Autowired
	private TipoColinService tipoColinService;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private NotarioService notarioService;

	@Autowired
	private CampoValorService campoValorService;

	@Autowired
	private PredioService predioService;

	@Autowired
	private ActoPredioService actoPredioService;

	@Autowired
	private TipoActoService tipoActoService;

	@Autowired
	private ActoPredioCampoService actoPredioCampoService;

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private ActoService tramiteService;

	@Autowired
	private ActoDocumentoService actoDocumentoService;

	public List<ValoresActo> obtenerValoresActos(Predio predio, Long[] status) {
		Set<Acto> actos = actoPredioService.findAllActosbyPredioAndStatusId(predio.getId(), status);
		return obtenerValoresActos(predio, actos, false);
	}

	public List<ValoresActo> obtenerValoresActosGravamenOLimitacion(Predio predio, Long[] status) {
		Set<Acto> actos = actoPredioService.findAllActosbyPredioAndStatusId(predio.getId(), status);
		return obtenerValoresActos(predio, actos, true);
	}

	public List<ValoresActo> obtenerValoresActos(Predio predio, Set<Acto> actos, boolean soloGravamenOLimitacion) {
		List<ValoresActo> valoresActos = new ArrayList<>();

		for (Acto acto : actos) {
			System.out.println("a vfId_aaj "+acto.getId_aaj()+" id "+acto.getId()+" vf? "+acto.getVf()+" tipo "+acto.getTipoActo().getNombre());
			if (acto == null || acto.getTipoActo() == null) continue;
			if (soloGravamenOLimitacion && !actoEsGravamenOLimitacion(acto.getTipoActo())) {
				continue;
			}
			if(acto.getId_aaj()==null){
				ActoPredio ap = actoPredioService.findMaxVersion(acto.getId());
				ValoresActo va = new ValoresActo();
				va.setModulos(obtenerModuloActo(acto, ap, soloGravamenOLimitacion));
				va.setNombreActo(acto.getTipoActo().getNombre()+(acto.getStatusActo().getId()==4?" - EXTINTO":" "));
				valoresActos.add(va);
			}
			
		}

		return valoresActos;
	}

	private boolean actoEsGravamenOLimitacion(TipoActo tipoActo) {
		return (tipoActo.getClasifActo().getId() !=null && tipoActo.getClasifActo().getId()==2);
	}

	private List<ModuloVO> obtenerModuloActo(Acto acto, ActoPredio ap, boolean incluirFirma) {
		List<ModuloVO> modulosVo = new ArrayList<>();
		if (acto.getTipoActo() == null) {
			return modulosVo;
		}

		TipoActoDTO ta = new TipoActoDTO(tipoActoService.findOneWithModulosById(acto.getTipoActo().getId()));
		Set<ModuloDTO> modulos = ta.getModulos();
		
		List<ModuloDTO> listOrdenada = modulos.stream()
				.sorted((m1,m2) -> m1.getOrden().compareTo(m2.getOrden()))
				.collect(Collectors.toList());
	
		if(acto.getTipoActo().getId() == 9L) {
			ArrayList<ModuloDTO> aux = new  ArrayList<>();
			for(ModuloDTO m:listOrdenada) {
				if(m.getLabel().equalsIgnoreCase("OBSERVACIONES")){
					aux.add(m);
				}
			}
			
			if(aux.size() > 1) {
				for(int i = 0; i < aux.size(); i++) {
					if(i > 0) {
						listOrdenada.remove(aux.get(i));
					}
				}
			}
			
		}
		
		
		
		ModuloVO actoId = new ModuloVO();
		actoId.setCampo("ACTO ID");
		actoId.setValor(acto.getId() + "");

		mostrarDocumentosAsociados(modulosVo, acto);

		for (ModuloDTO m : listOrdenada) {
			/*if (ocultarModulo(m)) {
				continue;
			}*/

			String valor = obtenerValorDeModulo(m, ap);
			if (valor == null || valor.trim().isEmpty()) {
				continue;
			}
		

			ModuloVO mVo = new ModuloVO();
			Integer imprimirEncabezdo=m.getInd_impresion();
			System.out.println("imprimirEncabezdo "+imprimirEncabezdo+" "+m.getLabel()+" valor "+valor);
			if(!valor.isEmpty()){
				
				mVo.setCampo(m.getLabel());
				mVo.setValor(valor);
				modulosVo.add(mVo);
			}
			
		}

		if (incluirFirma) {
			ActoFirma firma = tramiteService.getFirmaActo(acto.getId());
			if (firma != null && firma.getUsuario() != null && firma.getUsuario().getPersona() != null) {
				ModuloVO mVo = new ModuloVO();
				mVo.setCampo(C_REGISTRADOR);
				mVo.setValor(construirPersona(firma.getUsuario().getPersona()));
				modulosVo.add(mVo);
			}
		}

		return modulosVo;
	}

	private void mostrarDocumentosAsociados(List<ModuloVO> modulosVo, Acto acto) {
		List<ActoDocumento> actosDoc = actoDocumentoService.findDocumentosByActoId(acto.getId());
		actosDoc.stream().forEach(ad -> {
			ModuloVO doc = crearNodoDocumento(ad.getDocumento(), acto);

			if (doc != null) {
				modulosVo.add(doc);
			}
		});
	}

	private ModuloVO crearNodoDocumento(Documento documento, Acto acto) {
		int tipo = documento.getTipoDocumento().getId().intValue();
		try {

			switch(tipo) {
			case 1:
				return crearNodoEscritura(documento);
			case 2:
				return crearNodoRatJuez(documento);
			case 3:
				return crearNodoTitulo(documento);
			case 4:
				return crearNodoResolucion(documento);
			case 5:
				return crearNodoOficioFund77(documento, acto);
			case 6:
				return crearNodoOficioJur(documento);
			case 7:
				return crearNodoEscNotarial(documento);
			case 8:
				return crearNodoSolicRatNotario(documento);
			case 9:
				return crearNodoOfRatComNac(documento);
			case 10:
				return crearNodoSentencia(documento);
			case 11:
				return crearNodoEscrito(documento);
			case 12:
				return crearNodoOficioPropiedad(documento);
			case 13:
				return crearNodoPoliza(documento);
			case 14:
				return crearNodoEscritoRatNot(documento);
			case 15:
				return crearNodoDecreto(documento);
			case 16:
				return crearNodoContratoPriv(documento);
			case 17:
				return crearNodoSolicNotario(documento, acto);
			case 18:
				return crearNodoContPrivInfona(documento);
			case 19:
				return crearNodoOrdenAut(documento);
			case 20:
				return crearNodoEscritoPart(documento);

			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private ModuloVO crearNodoEscritura(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		sb.append("<b>");
		sb.append(d.getTipoDocumento().getNombre());
		sb.append("</b><br>");

		sb.append("<b>NUMERO DE ESCRITURA</b>: ");
		sb.append(d.getNumero2());
		sb.append("<br>");

		sb.append("<b>FECHA ESCRITURA</b>: ");
		sb.append(sdf.format(d.getFecha()));

		sb.append("<br>");
		sb.append("<b>NO. NOTARIO</b>: ");
		sb.append(d.getNotario().getId());

		sb.append("   ");
		sb.append("<b>TIPO</b>: ");
		sb.append(d.getNotario().getTipoNotario().getNombre());

		sb.append("<br>");
		sb.append("<b>NOTARIO: </b>: ");
		sb.append(d.getNotario().getPersona().getNombre());

		sb.append(" ");		
		sb.append(d.getNotario().getPersona().getPaterno());

		sb.append(" ");
		sb.append(d.getNotario().getPersona().getMaterno());

		sb.append("<br>");
		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getNotario().getMunicipio().getNombre());

		sb.append("   ");
		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getNotario().getMunicipio().getEstado().getNombre());
	
		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoRatJuez(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>FECHA</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>JUZGADO</b>: ");
		sb.append(d.getJuzgado());
		sb.append("<br>");

		sb.append("<b>NOMBRE DEL JUEZ</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getMunicipio().getEstado().getNombre());
		sb.append("<br>");

		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoTitulo(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO TITULO</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>FECHA TITULO</b>: ");
		sb.append(sdf.format(d.getFecha()));

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoResolucion(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>FECHA OFICIO</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getMunicipio().getEstado().getNombre());
		sb.append("<br>");

		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoOficioFund77(Documento d, Acto acto) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO DE DOCUMENTO A REPONER</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>FECHA OFICIO</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>NUMERO DE OFICIO</b>: ");
		sb.append(d.getNumero2());

		if (acto.getTipoActo().getId().equals(5L)) {
			sb.append("<br>");
			sb.append("<b>NOMBRE</b>: ");
			sb.append(d.getNombre());
			sb.append("<br>");

			if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
				sb.append("<b>APELLIDO PATERNO</b>: ");
				sb.append(d.getPaterno());
				sb.append("<br>");
			}
			if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
				sb.append("<b>APELLIDO MATERNO</b>: ");
				sb.append(d.getMaterno());
				sb.append("<br>");
			}
		}

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoOficioJur(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO DE OFICIO</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>EXPEDIENTE</b>: ");
		sb.append(d.getNumero2());
		sb.append("<br>");

		sb.append("<b>FECHA</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>CARGO DEL FIRMANTE</b>: ");
		sb.append(d.getCargoFirmante());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoEscNotarial(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		sb.append("<b>FECHA</b>: ");
		sb.append(sdf.format(d.getFecha()));

		sb.append("<br>");
		sb.append("<b>NOTARIO</b>: ");
		sb.append(d.getNotario().getId());

		sb.append("<br>");
		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNotario().getPersona().getNombre());

		sb.append("<br>");
		sb.append("<b>APELLIDO PATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getPaterno());

		sb.append("<br>");
		sb.append("<b>APELLIDO MATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getMaterno());

		sb.append("<br>");
		sb.append("<b>NUMERO</b>: ");
		sb.append(d.getNotario().getNoNotario());

		sb.append("<br>");
		sb.append("<b>TIPO</b>: ");
		sb.append(d.getNotario().getTipoNotario().getNombre());

		sb.append("<br>");
		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getNotario().getMunicipio().getEstado().getNombre());

		sb.append("<br>");
		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getNotario().getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoSolicRatNotario(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO DE ESCRITURA</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>FECHA ESCRITURA</b>: ");
		sb.append(sdf.format(d.getFecha()));

		sb.append("<br>");
		sb.append("<b>NOTARIO</b>: ");
		sb.append(d.getNotario().getId());

		sb.append("<br>");
		sb.append("<b>TIPO</b>: ");
		sb.append(d.getNotario().getTipoNotario().getNombre());

		sb.append("<br>");
		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNotario().getPersona().getNombre());

		sb.append("<br>");
		sb.append("<b>APELLIDO PATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getPaterno());

		sb.append("<br>");
		sb.append("<b>APELLIDO MATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getMaterno());

		sb.append("<br>");
		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getNotario().getMunicipio().getEstado().getNombre());

		sb.append("<br>");
		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getNotario().getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoOfRatComNac(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		sb.append("<b>FECHA OFICIO</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		sb.append("<b>EN CALIDAD DE</b>: ");
		sb.append(d.getEnCalidadDe());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoSentencia(Documento d) {
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO DE EXPEDIENTE</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>BIS</b>: ");
		sb.append(d.isBis() != null && d.isBis() ? "SI" : "NO");
		sb.append("<br>");

		sb.append("<b>JUZGADO</b>: ");
		sb.append(d.getJuzgado());
		sb.append("<br>");

		sb.append("<b>NOMBRE DEL JUEZ</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getMunicipio().getEstado().getNombre());
		sb.append("<br>");

		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoEscrito(Documento d) {
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		sb.append("<b>NOMBRE DEL SOLICITANTE</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		sb.append("<b>EXPEDIENTE</b>: ");
		sb.append(d.getNumero2());
		sb.append("<br>");

		sb.append("<b>JUZGADO</b>: ");
		sb.append(d.getJuzgado());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoOficioPropiedad(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO DE OFICIO</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>FECHA</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		sb.append("<b>EN CALIDAD DE</b>: ");
		sb.append(d.getEnCalidadDe());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoPoliza(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO DE POLIZA</b>: ");
		sb.append(d.getNumero2());
		sb.append("<br>");

		sb.append("<b>FECHA POLIZA</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>OBJETO POLIZA</b>: ");
		sb.append(d.getObjeto());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoEscritoRatNot(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		sb.append("<b>FECHA</b>: ");
		sb.append(sdf.format(d.getFecha()));

		sb.append("<br>");
		sb.append("<b>NUMERO DE NOTARIO</b>: ");
		sb.append(d.getNotario().getNoNotario());

		sb.append("<br>");
		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNotario().getPersona().getNombre());

		sb.append("<br>");
		sb.append("<b>APELLIDO PATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getPaterno());

		sb.append("<br>");
		sb.append("<b>APELLIDO MATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getMaterno());

		sb.append("<br>");
		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getNotario().getMunicipio().getEstado().getNombre());

		sb.append("<br>");
		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getNotario().getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoDecreto(Documento d) {
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>CAUSA DE UTILIDAD PÚBLICA</b>: ");
		sb.append(d.getCausaUtilidad());
		sb.append("<br>");

		sb.append("<b>REQUERIDO POR</b>: ");
		sb.append(d.getRequeridoPor());
		sb.append("<br>");

		sb.append("<b>EXPROPIANTE</b>: ");
		sb.append(d.getExpropiante());
		sb.append("<br>");

		sb.append("<b>NOMBRE DEL REPRESENTANTE</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		sb.append("<b>EN CALIDAD DE</b>: ");
		sb.append(d.getEnCalidadDe());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoContratoPriv(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		sb.append("<b>FECHA DEL CONTRATO</b>: ");
		sb.append(sdf.format(d.getFecha()));

		sb.append("<br>");
		sb.append("<b>NO NOTARIO</b>: ");
		sb.append(d.getNotario().getNoNotario());

		sb.append("<br>");
		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNotario().getPersona().getNombre());

		sb.append("<br>");
		sb.append("<b>APELLIDO PATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getPaterno());

		sb.append("<br>");
		sb.append("<b>APELLIDO MATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getMaterno());

		sb.append("<br>");
		sb.append("<b>TIPO</b>: ");
		sb.append(d.getNotario().getTipoNotario().getNombre());

		sb.append("<br>");
		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getNotario().getMunicipio().getEstado().getNombre());

		sb.append("<br>");
		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getNotario().getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoSolicNotario(Documento d, Acto acto) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		if (acto.getTipoActo().getId().equals(49L)) {
			sb.append("<b>NUMERO DE ESCRITURA</b>: ");
			sb.append(sdf.format(d.getNumero()));
			sb.append("<br>");
			sb.append("<b>BIS</b>: ");
			sb.append(d.isBis() != null && d.isBis() ? "SI" : "NO");
			sb.append("<br>");
		}

		if (acto.getTipoActo().getId().equals(59L)) {
			sb.append("<b>NUMERO DE ESCRITURA</b>: ");
			sb.append(sdf.format(d.getNumero()));
			sb.append("<br>");
		}

		sb.append("<b>FECHA</b>: ");
		sb.append(sdf.format(d.getFecha()));

		sb.append("<br>");
		sb.append("<b>NUMERO DE NOTARIO</b>: ");
		sb.append(d.getNotario().getNoNotario());

		sb.append("<br>");
		sb.append("<b>TIPO</b>: ");
		sb.append(d.getNotario().getTipoNotario().getNombre());

		sb.append("<br>");
		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNotario().getPersona().getNombre());

		sb.append("<br>");
		sb.append("<b>APELLIDO PATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getPaterno());

		sb.append("<br>");
		sb.append("<b>APELLIDO MATERNO</b>: ");
		sb.append(d.getNotario().getPersona().getMaterno());

		sb.append("<br>");
		sb.append("<b>ESTADO</b>: ");
		sb.append(d.getNotario().getMunicipio().getEstado().getNombre());

		sb.append("<br>");
		sb.append("<b>MUNICIPIO</b>: ");
		sb.append(d.getNotario().getMunicipio().getNombre());

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoContPrivInfona(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>FECHA</b>: ");
		sb.append(sdf.format(d.getFecha()));

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoOrdenAut(Documento d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();
		sb.append("<b>NUMERO</b>: ");
		sb.append(d.getNumero());
		sb.append("<br>");

		sb.append("<b>FECHA OFICIO</b>: ");
		sb.append(sdf.format(d.getFecha()));
		sb.append("<br>");

		sb.append("<b>NOMBRE</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		doc.setValor(sb.toString());
		return doc;
	}

	private ModuloVO crearNodoEscritoPart(Documento d) {
		ModuloVO doc = new ModuloVO();
		doc.setCampo(DOCUMENTO);

		StringBuilder sb = new StringBuilder();

		sb.append("<b>NOMBRE DEL SOLICITANTE</b>: ");
		sb.append(d.getNombre());
		sb.append("<br>");

		if(d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO PATERNO</b>: ");
			sb.append(d.getPaterno());
			sb.append("<br>");
		}
		if( d.getMaterno()!=null && !d.getMaterno().isEmpty() ) {
			sb.append("<b>APELLIDO MATERNO</b>: ");
			sb.append(d.getMaterno());
			sb.append("<br>");
		}

		doc.setValor(sb.toString());
		return doc;
	}

	private boolean ocultarModulo(ModuloDTO m) {
		return m.cf.equals(1) || (m.getHist() != null && m.getHist());
	}

	private String obtenerValorDeModulo(ModuloDTO m, ActoPredio ap) {
		Map<Long,Set<ActoPredioCampo>> apcM = actoPredioCampoService.findActosModulosCamposByActoIdModuloId(ap.getId(), m.getTipoModuloId());
		Set<Long> keys = apcM.keySet();
		StringBuilder sb = new StringBuilder("");
		for (Long k : keys) {
			Set<ActoPredioCampo> apcS = apcM.get(k);
			List<ActoPredioCampo> listaOrdenada = apcS.stream()
					.sorted((a1, a2) -> a1.getModuloCampo().getOrden() - a2.getModuloCampo().getOrden())
					.collect(Collectors.toList());

			for (ActoPredioCampo apc : listaOrdenada) {
				String valor = obtenerValor(apc);
				
				Integer ind_impresion = apc.getModuloCampo().getInd_impresion();
				System.out.println("ind_impresion "+apc.getModuloCampo().getInd_impresion());
				
				
				if (valor == null || valor.isEmpty()) {
					continue;
				}

				if (apc.getModuloCampo()!=null) {
					/*
					0  no imprime etiqueta ni valor
					1 o NULL imprime etiqueta y valor
					2  sólo imprime valor
					*/
					if(ind_impresion==null){					
						sb.append(sb.toString().trim().isEmpty()?
							" "+apc.getModuloCampo().getEtiqueta().toUpperCase()+": "+valor.toUpperCase():
							", "+apc.getModuloCampo().getEtiqueta().toUpperCase()+": "+valor.toUpperCase());
					}else {
						if(ind_impresion==0){sb.append("");}
						if(ind_impresion==1){
							sb.append(sb.toString().trim().isEmpty()?
							" "+apc.getModuloCampo().getEtiqueta().toUpperCase()+": "+valor.toUpperCase():
							", "+apc.getModuloCampo().getEtiqueta().toUpperCase()+": "+valor.toUpperCase());
						}
						if(ind_impresion==2){sb.append(valor!=null? " "+" "+valor.toUpperCase(): "");}
					}
					
					
					
				}
			}
			
		}

		return sb.toString();
	}

	private String obtenerValor(ActoPredioCampo apc) {
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
				return orientacionService.findAll().stream().filter(o -> o.getId().equals(Long.parseLong(apc.getValor()))).findFirst().get().getNombre();

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
		return campoValorService.findByTipoCampoId(campoId)
				.stream()
				.filter(cv -> cv.getId().equals(id))
				.findFirst().get()
				.getNombre();
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
		StringBuilder v = new StringBuilder();	
		v.append(" Acto: ");
		v.append(a.getTipoActo().getNombre());
		v.append(" - Fecha ");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		v.append(simpleDateFormat.format(a.getFechaRegistro()));
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
