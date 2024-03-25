package com.gisnet.erpp.web.api.catalogo;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gisnet.erpp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Asentamiento;
import com.gisnet.erpp.domain.CampoCancelaActo;
import com.gisnet.erpp.domain.CampoCotizador;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.domain.ClasificacionConcepto;
import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.DiaInhabil;
import com.gisnet.erpp.domain.DireccionArea;
import com.gisnet.erpp.domain.EnlaceVialidad;
import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.domain.EtapaOSeccion;
import com.gisnet.erpp.domain.FracOCondo;
import com.gisnet.erpp.domain.InhabilLocal;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Materia;
import com.gisnet.erpp.domain.Motivo;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Nivel;
import com.gisnet.erpp.domain.Objeto;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Orientacion;
import com.gisnet.erpp.domain.Parametro;
import com.gisnet.erpp.domain.ParametroCotizador;
import com.gisnet.erpp.domain.Parentesco;
import com.gisnet.erpp.domain.Prioridad;
import com.gisnet.erpp.domain.Regimen;
import com.gisnet.erpp.domain.ReqClasifActo;
import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.domain.SeccionFolio;
import com.gisnet.erpp.domain.ServCotizTipoActo;
import com.gisnet.erpp.domain.ServiciosCotizador;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.TipoAsent;
import com.gisnet.erpp.domain.TipoAutoridad;
import com.gisnet.erpp.domain.TipoCert;
import com.gisnet.erpp.domain.TipoColin;
import com.gisnet.erpp.domain.TipoDocIdentif;
import com.gisnet.erpp.domain.TipoDocumento;
import com.gisnet.erpp.domain.TipoFolio;
import com.gisnet.erpp.domain.TipoIden;
import com.gisnet.erpp.domain.TipoInmueble;
import com.gisnet.erpp.domain.TipoMotivo;
import com.gisnet.erpp.domain.TipoNotario;
import com.gisnet.erpp.domain.TipoOficio;
import com.gisnet.erpp.domain.TipoPersona;
import com.gisnet.erpp.domain.TipoSociedad;
import com.gisnet.erpp.domain.TipoUsuario;
import com.gisnet.erpp.domain.TipoVialidad;
import com.gisnet.erpp.domain.TipoAclaracion;
import com.gisnet.erpp.domain.TipoRechazo;
import com.gisnet.erpp.domain.UnidadMedida;
import com.gisnet.erpp.domain.UsoSuelo;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Vialidad;
import com.gisnet.erpp.security.SecurityUtils;


@RestController
@RequestMapping(value = "/api/catalogo", produces = MediaType.APPLICATION_JSON_VALUE)
public class CatalogoRestController {

	@Autowired
	private AreaService areaService;

	@Autowired
	private ClasifActoService clasifActoService;

	@Autowired
	private TipoActoService tipoActoService;

	@Autowired
	private TipoUsuarioService tipoUsuarioService;

	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Autowired
	private ReqClasifActoService reqClasifActoService;

	@Autowired
	private TipoCertService tipoCertService;

	@Autowired
	private TipoAutoridadService tipoAutoridadService;

	@Autowired
	private MateriaService materiaService;

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private CampoValorService campoValorService;

	@Autowired
	private TipoVialidadService tipoVialidadService;
	
	@Autowired
	private VialidadService vialidadService;

	@Autowired
	private TipoAsentamientoService tipoAsentamientoService;
	
	@Autowired
	private AsentamientoService asentamientoService;

	@Autowired
	private UnidadMedidaService unidadMedidaService;

	@Autowired
	private UsoSueloService usoSueloService;

	@Autowired
	private LibroService libroService;

	@Autowired
	private SeccionService SeccionService;

	@Autowired
	private OficinaService oficinaService;

	@Autowired
	private ParentescoService parentescoService;

	@Autowired
	private NacionalidadService nacionalidadService;

	@Autowired
	private TipoPersonaService tipoPersonaService;

	@Autowired
	private TipoAsentService tipoAsentService;

	@Autowired
	private TipoIdenService tipoIdenService;

	@Autowired
	private TipoNotarioService tipoNotarioService;

	@Autowired
	private OrientacionService orientacionService;

	@Autowired
	private TipoInmuebleService tipoInmuebleService;
	
	@Autowired
	private NivelService nivelService;

	@Autowired
	private FracOCondoService fracOCondoService;
	
	@Autowired
	private EtapaOSeccionService etapaOSeccionService;


	@Autowired
	private CampoCotizadorService campoCotizadorService;

	@Autowired
	private SeccionService seccionService;

	@Autowired
	private SeccionFolioService seccionFolioService;

	@Autowired
	private ConceptoPagoService conceptoPagoService;

	@Autowired
	private EnlaceVialidadService enlaceVialidadService;

	@Autowired
	private EstadoCivilService estadoCivilService;

	@Autowired
	private TipoMotivoService tipoMotivoService;

	@Autowired
	private TipoColinService tipoColinService;

	@Autowired
	private RegimenService regimenService;

	@Autowired
	private ParametroCotizadorService parametroCotizadorService;

	@Autowired
	private OrientacionService orientacionCotizadorService;

	@Autowired
	private MotivoService motivoService;


	@Autowired
	private DiaInhabilService diaInhabilService;

	@Autowired
	private InhabilLocalService inhabilLocalService;

	@Autowired
	private ObjetoService objetoService;

	@Autowired
	private TipoDocIdentifService tipoDocIdentifService;

	@Autowired
	private TipoSociedadService tipoSociedadService;

	@Autowired
	private ServiciosCotizadorService serviciosCotizadorService;

	@Autowired
	private ClasificacionConceptoService clasificacionConceptoService;

	@Autowired
	private TipoOficioService tipoOficioService;
	
	@Autowired
	private TipoFolioService tipoFolioService;
	
	@Autowired
	private ParametroService parametroService;
	
	@Autowired
	private TipoAclaracionService tipoAclaracionService;

	@Autowired
	private DireccionAreaService direccionAreaService;
	
	@Autowired
	private ServCotizTipoActoService servCotizTipoActoService;

	@Autowired
	private PrioridadService prioridadService;

	@Autowired
	private StatusService statusService;

	@Autowired
	private ObservacionesService observacionesService;
	
	@Autowired
	UsuarioService usuarioService;


	@GetMapping(value="/area")
    public ResponseEntity<List<Area>> listAreas() {
        return ResponseEntity.ok(areaService.findAll());
    }
	
	@GetMapping(value="/area-filter")
    public ResponseEntity<List<Area>> listAreasFilter() {
        return ResponseEntity.ok(areaService.findFilter());
    }
	
	@GetMapping(value="/area/tipo-usuario")
    public ResponseEntity<List<Area>> listAreasByTipoUsuario() {
        return ResponseEntity.ok(areaService.findAllByTipoUsuario());
    }

	@GetMapping(value = "/clasif-acto/area/{id}")
	public ResponseEntity<List<ClasifActo>> findByAreaClasifActosAreaId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(clasifActoService.findByAreaClasifActosAreaId(id));
	}

	@GetMapping(value = "/clasif-acto/servicio/{id}")
	public ResponseEntity<List<ClasifActo>> listClasifActosByServicioId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(clasifActoService.findByClasifActosServicioId(id));
	}

	@GetMapping(value = "/clasif-acto/area/{idArea}/seccionFolio/{idSeccionFolio}")
	public ResponseEntity<List<ClasifActo>> listClasifActosByAreaIdAndSeccionFolioId(@PathVariable("idArea") Long idArea,@PathVariable("idSeccionFolio") Long idSeccionFolio) {
		return ResponseEntity.ok(clasifActoService.findByAreaIdAndSeccionFolioId(idArea,idSeccionFolio));
	}

	@GetMapping(value = "/tipo-acto/clasif-acto/{id}")
	public ResponseEntity<List<TipoActo>> findTipoActosByClasifActo(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoActoService.findByClasifActoId(id));
	}
	
	@GetMapping(value = "/tipo-acto")
	public ResponseEntity<List<TipoActo>> findTipoActos() {
		return ResponseEntity.ok(tipoActoService.findAll());
	}

	@GetMapping(value = "/tipo-acto/exclude-servicios")
	public ResponseEntity<List<TipoActo>> findTipoActosSinServicios() {
		return ResponseEntity.ok(tipoActoService.findAllExcludeServicios());
	}
	
	@GetMapping(value = "/tipo-acto/tipo-folio/{id}")
	public ResponseEntity<List<TipoActo>> findAllByTipoFolioId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoActoService.findAllByTipoFolioId(id));
	}	

	@GetMapping(value = "/tipo-acto/area/{id}")
	public ResponseEntity<List<TipoActo>> findAllByAreaId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoActoService.findAllByAreaId(id));
	}	

	@GetMapping(value = "/requisito/clasif-acto/{id}/requisitos")
	public ResponseEntity<List<ReqClasifActo>> findRequisitos(@PathVariable("id") Long id) {
		return ResponseEntity.ok(reqClasifActoService.findByClasifActo(id));
	}

	@GetMapping(value = "/municipio/estado/{id}")
	public ResponseEntity<List<Municipio>> findMunicipiosbyEstadoId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(municipioService.findAllbyEstadoId(id));
	}

	@GetMapping(value = "/tipo-usuario")
	public ResponseEntity<List<TipoUsuario>> findTiposUsuario() {
		return ResponseEntity.ok(tipoUsuarioService.findAll());
	}

	@GetMapping(value = "/tipo-documento")
	public ResponseEntity<List<TipoDocumento>> findTiposDocumento() {
		return ResponseEntity.ok(tipoDocumentoService.findAll());
	}

	@PostMapping(value = "/tipo-documento/crear")
    public ResponseEntity<TipoDocumento> tipoDocumentoCrear(@RequestBody TipoDocumento tipodocumento) {
		return ResponseEntity.ok(tipoDocumentoService.create(tipodocumento));
	}

     @PostMapping(value = "/tipo-documento/eliminar")
    public ResponseEntity<Long> tipoDocumentoEliminar(@RequestBody TipoDocumento tipodocumento) {
		return ResponseEntity.ok(tipoDocumentoService.delete(tipodocumento));
	}

	@GetMapping(value = "/tipo-cert")
	public ResponseEntity<List<TipoCert>> findTiposCertificados() {
		return ResponseEntity.ok(tipoCertService.findAll());
	}

	 @PostMapping(value = "/tipo-cert/crear")
	    public ResponseEntity<TipoCert> tipoCertCrear(@RequestBody TipoCert tipoCert) {
			return ResponseEntity.ok(tipoCertService.create(tipoCert));
		}

	@PostMapping(value = "/tipo-cert/eliminar")
	    public ResponseEntity<Long> tipoCertEliminar(@RequestBody TipoCert tipoCert) {
			return ResponseEntity.ok(tipoCertService.delete(tipoCert));
		}

	@GetMapping(value = "/tipo-motivo")
    public ResponseEntity<List<TipoMotivo>> findTiposMotivos() {
		return ResponseEntity.ok(tipoMotivoService.findAll());
	}

	 @PostMapping(value = "/tipo-motivo/crear")
	    public ResponseEntity<TipoMotivo> tipoMotivoCrear(@RequestBody TipoMotivo tipoMotivo) {
			return ResponseEntity.ok(tipoMotivoService.create(tipoMotivo));
		}

	@PostMapping(value = "/tipo-motivo/eliminar")
	    public ResponseEntity<Long> tipoMotivoEliminar(@RequestBody TipoMotivo tipoMotivo) {
			return ResponseEntity.ok(tipoMotivoService.delete(tipoMotivo));
		}

	@GetMapping(value = "/tipo-colin")
    public ResponseEntity<List<TipoColin>> findAllTiposColin() {
		return ResponseEntity.ok(tipoColinService.findAll());
	}

	 @PostMapping(value = "/tipo-colin/crear")
	    public ResponseEntity<TipoColin> tipoColinCrear(@RequestBody TipoColin tipoColin) {
			return ResponseEntity.ok(tipoColinService.create(tipoColin));
		}

	@PostMapping(value = "/tipo-colin/eliminar")
	    public ResponseEntity<Long> tipoColinEliminar(@RequestBody TipoColin tipoColin) {
			return ResponseEntity.ok(tipoColinService.delete(tipoColin));
		}

	@GetMapping(value = "/tipo-autoridad")
	public ResponseEntity<List<TipoAutoridad>> findTiposAutoridad() {
		return ResponseEntity.ok(tipoAutoridadService.findAll());
	}

	@GetMapping(value = "/materia")
	public ResponseEntity<List<Materia>> findMaterias() {
		return ResponseEntity.ok(materiaService.findAll());
	}

	 @PostMapping(value = "/materia/crear")
public ResponseEntity<Materia> materiaCrear(@RequestBody Materia materia) {
return ResponseEntity.ok(materiaService.create(materia));
}

@PostMapping(value = "/materia/eliminar")
public ResponseEntity<Long> materiaEliminar(@RequestBody Materia materia) {
return ResponseEntity.ok(materiaService.delete(materia));
}

@GetMapping(value = "/regimen")
public ResponseEntity<List<Regimen>> findAllRegimen() {
	return ResponseEntity.ok(regimenService.findAll());
}

 @PostMapping(value = "/regimen/crear")
public ResponseEntity<Regimen> regimenCrear(@RequestBody Regimen regimen) {
return ResponseEntity.ok(regimenService.create(regimen));
}

@PostMapping(value = "/regimen/eliminar")
public ResponseEntity<Long> regimenEliminar(@RequestBody Regimen regimen) {
return ResponseEntity.ok(regimenService.delete(regimen));
}


 @PostMapping(value = "/orientacion/crear")
public ResponseEntity<Orientacion> orientacionCrear(@RequestBody Orientacion orientacion) {
return ResponseEntity.ok(orientacionService.create(orientacion));
}

@PostMapping(value = "/orientacion/eliminar")
public ResponseEntity<Long> orientacionEliminar(@RequestBody Orientacion orientacion) {
return ResponseEntity.ok(orientacionService.delete(orientacion));
}

@GetMapping(value = "/parametro-cotizador")
public ResponseEntity<List<ParametroCotizador>> findAllParametroCotizador() {
	return ResponseEntity.ok(parametroCotizadorService.findAll());
}

 @PostMapping(value = "/parametro-cotizador/crear")
public ResponseEntity<ParametroCotizador> parametroCotizadorCrear(@RequestBody ParametroCotizador parametroCotizador) {
return ResponseEntity.ok(parametroCotizadorService.create(parametroCotizador));
}

@PostMapping(value = "/parametro-cotizador/eliminar")
public ResponseEntity<Long> parametroCotizadorEliminar(@RequestBody ParametroCotizador parametroCotizador) {
return ResponseEntity.ok(parametroCotizadorService.delete(parametroCotizador));
}


@GetMapping(value = "/estado-civil")
public ResponseEntity<List<EstadoCivil>> findAllEstadoCivil() {
	return ResponseEntity.ok(estadoCivilService.findAll());
}

 @PostMapping(value = "/estado-civil/crear")
public ResponseEntity<EstadoCivil> estadoCivilCrear(@RequestBody EstadoCivil estadoCivil) {
return ResponseEntity.ok(estadoCivilService.create(estadoCivil));
}

@PostMapping(value = "/estado-civil/eliminar")
public ResponseEntity<Long> estadoCivilEliminar(@RequestBody EstadoCivil estadoCivil) {
return ResponseEntity.ok(estadoCivilService.delete(estadoCivil));
}

	@GetMapping(value = "/municipio")
	public ResponseEntity<List<Municipio>> findMunicipios() {
		return ResponseEntity.ok(municipioService.findAll());
	}

	@PostMapping(value = "/municipio/crear")
	public ResponseEntity<Boolean> municipioCrear(@RequestBody Municipio municipio) {
		return ResponseEntity.ok(municipioService.create(municipio));
	}
//aqui
	@PostMapping(value = "/municipio/eliminar")
	public ResponseEntity<Long> municipioEliminar(@RequestBody Municipio municipio) {
		return ResponseEntity.ok(municipioService.delete(municipio));
	}


	@GetMapping(value = "/estado")
	public ResponseEntity<List<Estado>> findEstados() {
		return ResponseEntity.ok(estadoService.findAll());
	}

	@GetMapping(value = "/campo-valor/campo/{id}")
	public ResponseEntity<List<CampoValores>> findByTipoCampoId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(campoValorService.findByTipoCampoId(id));
	}
	@GetMapping(value = "/campo-valor/cancela-acto/{id}")
	public ResponseEntity<Set<CampoCancelaActo>> findCancelaActoByTipoCampoId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(campoValorService.findCancelaActoByTipoCampoId(id));
	}
	@GetMapping(value = "/campo-valor/cancela-acto/id/{id}")
	public ResponseEntity<Set<CampoCancelaActo>> findCancelaActoByCampoValorId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(campoValorService.findCancelaActoByCampoValorId(id));
	}
	
	@GetMapping(value = "/vialidad/{municipioId}/{tipoVialidadId}")
	public ResponseEntity<List<Vialidad>> findByMunicipioIdAndTipoVialidadId(@PathVariable("municipioId")Long municipioId, @PathVariable("tipoVialidadId") Long tipoVialidadId) {
		return ResponseEntity.ok(vialidadService.findByMunicipioIdAndTipoVialidadId(municipioId, tipoVialidadId));
	}
	
	@GetMapping(value = "/asentamiento/{municipioId}/{tipoAsentId}")
	public ResponseEntity<List<Asentamiento>> findByMunicipioIdAndTipoAsentId(@PathVariable("municipioId")Long municipioId, @PathVariable("tipoAsentId") Long tipoAsentId) {
		return ResponseEntity.ok(asentamientoService.findByMunicipioIdAndTipoAsentId(municipioId, tipoAsentId));
	}


	@GetMapping(value = "/tipo-vialidad")
	public ResponseEntity<List<TipoVialidad>> findTiposVialidad() {
		return ResponseEntity.ok(tipoVialidadService.findAll());
	}
	@PostMapping(value = "/tipo-vialidad/crear")
    public ResponseEntity<TipoVialidad> tipoVialidadCrear(@RequestBody TipoVialidad tipovialidad) {
		return ResponseEntity.ok(tipoVialidadService.create(tipovialidad));
	}

     @PostMapping(value = "/tipo-vialidad/eliminar")
    public ResponseEntity<Long> tipoVialidadEliminar(@RequestBody TipoVialidad tipovialidad) {
		return ResponseEntity.ok(tipoVialidadService.delete(tipovialidad));
	}


	@GetMapping(value = "/tipo-asentamiento")
	public ResponseEntity<List<TipoAsent>> findTiposAsentamiento() {
		return ResponseEntity.ok(tipoAsentamientoService.findAll());
	}

	@GetMapping(value = "/unidad-medida")
	public ResponseEntity<List<UnidadMedida>> findUnidadesMedida() {
		return ResponseEntity.ok(unidadMedidaService.findAll());
	}
	
	@GetMapping(value = "/unidad-medida-filter")
	public ResponseEntity<List<UnidadMedida>> findUnidadesMedidaFilter() {
		return ResponseEntity.ok(unidadMedidaService.findFilter());
	}

	@PostMapping(value = "/unidad-medida/crear")
	public ResponseEntity<UnidadMedida> unidadMedidaCrear(@RequestBody UnidadMedida unidadMedida) {
		return ResponseEntity.ok(unidadMedidaService.create(unidadMedida));
	}

	@PostMapping(value = "/unidad-medida/eliminar")
	public ResponseEntity<Long> unidadMedidaEliminar(@RequestBody UnidadMedida unidadMedida) {
		return ResponseEntity.ok(unidadMedidaService.delete(unidadMedida));
	}


	@GetMapping(value = "/uso-suelo")
	public ResponseEntity<List<UsoSuelo>> findUsosSuelo() {
		return ResponseEntity.ok(usoSueloService.findAll());
	}

	@PostMapping(value = "/uso-suelo/crear")
	public ResponseEntity<UsoSuelo> usoSueloCrear(@RequestBody UsoSuelo usoSuelo) {
		return ResponseEntity.ok(usoSueloService.create(usoSuelo));
	}

	@PostMapping(value = "/uso-suelo/eliminar")
	public ResponseEntity<Long> usoSueloEliminar(@RequestBody UsoSuelo usoSuelo) {
		return ResponseEntity.ok(usoSueloService.delete(usoSuelo));
	}


	@GetMapping(value = "/libro")
	public ResponseEntity<List<Libro>> findLibros() {
		return ResponseEntity.ok(libroService.findAll());
	}

	@GetMapping(value = "/seccion")
	public ResponseEntity<List<Seccion>> findSecciones() {
		return ResponseEntity.ok(SeccionService.findAll());
	}

	@GetMapping(value = "/seccion-folio")
	public ResponseEntity<List<SeccionFolio>> findSeccionesFolio() {
		return ResponseEntity.ok(seccionFolioService.findAll());
	}

	@GetMapping(value = "/oficina")
	public ResponseEntity<List<Oficina>> findOficinas() {
         Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		if(usuario.getOficina()!=null) {
			Oficina of = new Oficina();
			of = usuario.getOficina();
			List<Oficina> oficinas = new ArrayList<Oficina>() ;
			
			oficinas.add(of);
			return ResponseEntity.ok(oficinas);
			
		}else {

		return ResponseEntity.ok(oficinaService.findAll());
		}	
		
	}

	 @PostMapping(value = "/oficina/crear")
public ResponseEntity<Oficina> oficinaCrear(@RequestBody Oficina oficina) {
return ResponseEntity.ok(oficinaService.create(oficina));
}

@PostMapping(value = "/oficina/eliminar")
public ResponseEntity<Long>  oficinaEliminar(@RequestBody Oficina oficina) {
return ResponseEntity.ok(oficinaService.delete(oficina));
}

	@GetMapping(value = "/parentescos")
	public ResponseEntity<List<Parentesco>> findParentescos() {
		return ResponseEntity.ok(parentescoService.findAll());
	}
	 @PostMapping(value = "/parentescos/crear")
	    public ResponseEntity<Parentesco> parentescoCrear(@RequestBody Parentesco parentesco) {
			return ResponseEntity.ok(parentescoService.create(parentesco));
		}

	@PostMapping(value = "/parentescos/eliminar")
	    public ResponseEntity<Long> parentescoEliminar(@RequestBody Parentesco parentesco) {
			return ResponseEntity.ok(parentescoService.delete(parentesco));
		}


	@GetMapping(value = "/nacionalidad")
	public ResponseEntity<List<Nacionalidad>> findNacionalidad() {
		return ResponseEntity.ok(nacionalidadService.findAll());
	}

	 @PostMapping(value = "/nacionalidad/crear")
public ResponseEntity<Nacionalidad> nacionalidadCrear(@RequestBody Nacionalidad nacionalidad) {
return ResponseEntity.ok(nacionalidadService.create(nacionalidad));
}

@PostMapping(value = "/nacionalidad/eliminar")
public ResponseEntity<Long> nacionalidadEliminar(@RequestBody Nacionalidad nacionalidad) {
return ResponseEntity.ok(nacionalidadService.delete(nacionalidad));
}

	@GetMapping(value = "/tipo-persona")
	public ResponseEntity<List<TipoPersona>> findTiposPersona() {
		return ResponseEntity.ok(tipoPersonaService.findAll());
	}

	@PostMapping(value = "/tipo-persona/crear")
    public ResponseEntity<TipoPersona> tipoPersonaCrear(@RequestBody TipoPersona tipoPersona) {
		return ResponseEntity.ok(tipoPersonaService.create(tipoPersona));
	}

	@PostMapping(value = "/tipo-persona/eliminar")
		public ResponseEntity<Long> tipoPersonaEliminar(@RequestBody TipoPersona tipoPersona) {
		return ResponseEntity.ok(tipoPersonaService.delete(tipoPersona));
	}

	@GetMapping(value = "/tipo-asent")
		public ResponseEntity<List<TipoAsent>> findTiposAsent() {
		return ResponseEntity.ok(tipoAsentService.findAll());
	}

	@GetMapping(value = "/tipo-iden")
		public ResponseEntity<List<TipoIden>> findTiposIden() {
		return ResponseEntity.ok(tipoIdenService.findAll());
	}

	@PostMapping(value = "/tipo-iden/crear")
		public ResponseEntity<TipoIden> tipoPersonaCrear(@RequestBody TipoIden tipoIden) {
		return ResponseEntity.ok(tipoIdenService.create(tipoIden));
	}

	@PostMapping(value = "/tipo-iden/eliminar")
	    public ResponseEntity<Long> tipoPersonaEliminar(@RequestBody TipoIden tipoIden) {
			return ResponseEntity.ok(tipoIdenService.delete(tipoIden));
	}

	@GetMapping(value = "/tipo-notario")
	public ResponseEntity<List<TipoNotario>> findTiposNotario() {
		return ResponseEntity.ok(tipoNotarioService.findAll());
	}

	@PostMapping(value = "/tipo-notario/crear")
		public ResponseEntity<TipoNotario> tipoDocumentoCrear(@RequestBody TipoNotario tiponotario) {
		return ResponseEntity.ok(tipoNotarioService.create(tiponotario));
	}

	@PostMapping(value = "/tipo-notario/eliminar")
		public ResponseEntity<Long> tipoDocumentoEliminar(@RequestBody TipoNotario tiponotario) {
		return ResponseEntity.ok(tipoNotarioService.delete(tiponotario));
	}

	@GetMapping(value = "/orientacion")
		public ResponseEntity<List<Orientacion>> findAllOrientaciones() {
		return ResponseEntity.ok(orientacionService.findAll());
	}

	@GetMapping(value = "/campos-cotizador/{conceptoPagoId}")
	public ResponseEntity<List<CampoCotizador>> findCamposCotizadorByConceptoPago(@PathVariable("conceptoPagoId") Long conceptoPagoId) {
		return ResponseEntity.ok(campoCotizadorService.findByConfCotizadoresParaCamposCotizadorConceptoPagoId(conceptoPagoId));
	}

	@GetMapping(value = "/campos-cotizador")
	public ResponseEntity<List<CampoCotizador>> findAllCamposCotizador(){
		return ResponseEntity.ok(campoCotizadorService.findAll());
	}


	 @PostMapping(value = "/campos-cotizador/crear")
public ResponseEntity<CampoCotizador> campoCotizadorCrear(@RequestBody CampoCotizador campoCotizador) {
return ResponseEntity.ok(campoCotizadorService.create(campoCotizador));
}

@PostMapping(value = "/campos-cotizador/eliminar")
public ResponseEntity<Long> campoCotizadorEliminar(@RequestBody CampoCotizador campoCotizador) {
return ResponseEntity.ok(campoCotizadorService.delete(campoCotizador));
}

	@GetMapping(value = "/tipo-inmueble")
	public ResponseEntity<List<TipoInmueble>> findTiposInmuebles() {
		return ResponseEntity.ok(tipoInmuebleService.findAll());
	}
	
	@GetMapping(value = "/nivel")
	public ResponseEntity<List<Nivel>> findNiveles() {
		return ResponseEntity.ok(nivelService.findAll());
	}
	
	@GetMapping(value = "/frac-o-condo")
	public ResponseEntity<List<FracOCondo>> findFraccionamientoOCondominios() {
		return ResponseEntity.ok(fracOCondoService.findAll());
	}
	
	@GetMapping(value = "/etapa-o-seccion")
	public ResponseEntity<List<EtapaOSeccion>> findEtapaOSecciones() {
		return ResponseEntity.ok(etapaOSeccionService.findAll());
	}




	   @PostMapping(value = "/tipo-inmueble/crear")
	    public ResponseEntity<TipoInmueble> tipoDocumentoCrear(@RequestBody TipoInmueble tipoinmueble) {
			return ResponseEntity.ok(tipoInmuebleService.create(tipoinmueble));
		}

	@PostMapping(value = "/tipo-inmueble/eliminar")
	    public ResponseEntity<Long> tipoDocumentoEliminar(@RequestBody TipoInmueble tipoinmueble) {
			return ResponseEntity.ok(tipoInmuebleService.delete(tipoinmueble));
		}

	@GetMapping(value = "/enlace-vialidad")
    public ResponseEntity<List<EnlaceVialidad>> findAllEnlaceVialidad() {
		return ResponseEntity.ok(enlaceVialidadService.findAll());
	}

			 @PostMapping(value = "/enlace-vialidad/crear")
    public ResponseEntity<EnlaceVialidad> enlaceVialidadCrear(@RequestBody EnlaceVialidad enlaceVialidad) {
		return ResponseEntity.ok(enlaceVialidadService.create(enlaceVialidad));
	}

	@PostMapping(value = "/enlace-vialidad/eliminar")
    public ResponseEntity<Long>  enlaceVialidadEliminar(@RequestBody EnlaceVialidad enlaceVialidad) {
		return ResponseEntity.ok(enlaceVialidadService.delete(enlaceVialidad));
	}

	@GetMapping(value = "/seccion/oficina/{id}")
	public ResponseEntity<List<Seccion>> findSeccionesOficina(@PathVariable("id") Long id) {
		return ResponseEntity.ok(seccionService.findSeccionesOficina(id));
	}


	@GetMapping(value = "/concepto-pago")
	public ResponseEntity<List<ConceptoPago>> findConceptosPago() {
		return ResponseEntity.ok(conceptoPagoService.findAll());
	}

	@GetMapping(value = "/motivos/{tipoMotivo}")
	public ResponseEntity<List<Motivo>> findMotivosByMotivoId(@PathVariable("tipoMotivo") String tipoMotivo) {
		return ResponseEntity.ok(motivoService.findAllMotivosBy(tipoMotivo));
	}

	@GetMapping(value = "/fundamentos/rechazo")
	public ResponseEntity<List<TipoRechazo>> findFundamentoRechazoActo() {
		return ResponseEntity.ok(motivoService.findAllByTipoRechazo());
	}

	@GetMapping(value = "/fundamentos/rechazo/{tipoMotivo}")
	public ResponseEntity<List<TipoRechazo>> findFundamentoRechazoActo(@PathVariable("tipoMotivo") String tipoMotivo) {
		return ResponseEntity.ok(motivoService.findAllByTipoRechazo(tipoMotivo));
	}

	@GetMapping(value = "/motivos")
	public ResponseEntity<List<Motivo>> getMotivos() {
		return ResponseEntity.ok(motivoService.findAllByActivo(true));
	}


	@GetMapping(value = "/motivo")
    public ResponseEntity<List<Motivo>> findAllMotivos() {
		return ResponseEntity.ok(motivoService.findAll());
	}

	@PostMapping(value = "/motivo/crear")
    public ResponseEntity<Motivo> motivoCrear(@RequestBody Motivo motivo) {
		return ResponseEntity.ok(motivoService.create(motivo));
	}

	@PostMapping(value = "/motivo/eliminar")
    public ResponseEntity<Long>  motivoEliminar(@RequestBody Motivo motivo) {
		return ResponseEntity.ok(motivoService.delete(motivo));
	}

	@GetMapping(value = "/dia-inhabil")
    public ResponseEntity<List<DiaInhabil>> findAllDiaInhabil() {
		return ResponseEntity.ok(diaInhabilService.findAll());
	}

	@PostMapping(value = "/dia-inhabil/crear")
    public ResponseEntity<DiaInhabil> diaInCrear(@RequestBody DiaInhabil diaInhabil) {
		return ResponseEntity.ok(diaInhabilService.create(diaInhabil));
	}

	@PostMapping(value = "/dia-inhabil/eliminar")
    public ResponseEntity<Long>  diaInEliminar(@RequestBody DiaInhabil diaInhabil) {
		return ResponseEntity.ok(diaInhabilService.delete(diaInhabil));
	}

	@GetMapping(value = "/inhabil-local")
    public ResponseEntity<List<InhabilLocal>> findAllInhabilLocal() {
		return ResponseEntity.ok(inhabilLocalService.findAll());
	}

	@PostMapping(value = "/inhabil-local/crear")
    public ResponseEntity<InhabilLocal> InLocalCrear(@RequestBody InhabilLocal inhabilLocal) {
		return ResponseEntity.ok(inhabilLocalService.create(inhabilLocal));
	}

	@PostMapping(value = "/inhabil-local/eliminar")
    public ResponseEntity<Long>  InLocalEliminar(@RequestBody InhabilLocal inhabilLocal) {
		return ResponseEntity.ok(inhabilLocalService.delete(inhabilLocal));
	}

	@GetMapping(value = "/objeto")
	public ResponseEntity<List<Objeto>> findObjetos() {
		return ResponseEntity.ok(objetoService.findAll());
	}

	@GetMapping(value = "/tipo-doc-identificacion")
	public ResponseEntity<List<TipoDocIdentif>> findTipoDocIdentificacion() {
		return ResponseEntity.ok(tipoDocIdentifService.findAll());
	}

	@GetMapping(value = "/tipo-sociedad")
	public ResponseEntity<List<TipoSociedad>> findTipoSociedad() {
		return ResponseEntity.ok(tipoSociedadService.findAll());
	}

		@GetMapping(value = "/servicios-cotizador")
    public ResponseEntity<List<ServiciosCotizador>> findAllServiciosCotizador() {
		return ResponseEntity.ok(serviciosCotizadorService.findAll());
	}

	@GetMapping(value = "/clasificacion-concepto/servicio-cotizador/{id}")
	public ResponseEntity<List<ClasificacionConcepto>> findByServicioConcepto(@PathVariable("id") Long id) {
		return ResponseEntity.ok(clasificacionConceptoService.findByServiciosCotizadorId(id));
	}
	@GetMapping(value = "/tipo-oficio")
	public ResponseEntity<List<TipoOficio>> listOficios() {
		return ResponseEntity.ok(tipoOficioService.findAll());

	}
	
	@GetMapping(value="/tipo-folio")
    public ResponseEntity<List<TipoFolio>> listTiposFolio() {
        return ResponseEntity.ok(tipoFolioService.findAll());
    }

	@GetMapping(value="/tipo-aclaracion")
    public ResponseEntity<List<TipoAclaracion>> listTipoAclaracion() {
        return ResponseEntity.ok(tipoAclaracionService.findAll());
    }

	

    @GetMapping(value ="/direccion-area")
	public ResponseEntity<List<DireccionArea>> findDireccionArea() {
		return ResponseEntity.ok(direccionAreaService.findAll());
	}


	
	@GetMapping(value = "/parametro/{cveParametro}")
	public ResponseEntity<Parametro> findRequisitos(@PathVariable("cveParametro") String cveParametro) {
		return ResponseEntity.ok(parametroService.findByCve(cveParametro));
	}
	
	@GetMapping(value = "/serv-cotiz-tipo-acto/tipo-acto/{tipoActoId}")
	public ResponseEntity<ServCotizTipoActo> servCotizTipoActofindByTipoActoId(@PathVariable("tipoActoId") long tipoActoId) {
		return ResponseEntity.ok(servCotizTipoActoService.findByTipoActoId(tipoActoId));
	}

	@GetMapping(value = "/prioridad")
	public ResponseEntity<?> listPrioridad() {
		return ResponseEntity.ok(prioridadService.findAll());
	}

	@GetMapping(value = "/status")
	public ResponseEntity<?> listStatus() {
		return ResponseEntity.ok(statusService.findAllUsables());
	}

	@GetMapping(value = "/status-coordinador")
	public ResponseEntity<?> listStatusCoordinador() {
		return ResponseEntity.ok(statusService.findAllUsablesCoordinador());
	}
	
	@GetMapping(value = "/status-upkeepPrelacion")
	public ResponseEntity<?> listStatuUpkeep() {
		return ResponseEntity.ok(statusService.findAllUpkeepPrelacion());
	}

	@GetMapping(value = "/observaciones")
	public ResponseEntity<?> listObservacionesByArea(Long area) {

		if (area == null)
			return ResponseEntity.ok(observacionesService.findAll());

		return ResponseEntity.ok(observacionesService.findAllByArea(area));
	}
	
/*	@GetMapping(value="/tipo-aclaracion/find")
    public ResponseEntity<List<TipoAclaracion>> listTipoAclaracionFind() {
        return ResponseEntity.ok(tipoAclaracionService.findAll());
    }*/
	
	/*@PostMapping(value = "/tipo-aclaracion/crear")
    public ResponseEntity<Boolean> tipoAclaracionCrear(@RequestBody TipoAclaracion tipoAclaracion) {
		return ResponseEntity.ok(tipoAclaracionService.create(tipoAclaracion));
	}*/
	@GetMapping(value = "/seccion/byId/{id}")
	public  ResponseEntity<Seccion> findSeccionById(@PathVariable("id")Long  id) {  	
		return ResponseEntity.ok(seccionService.findSeccionById(id));
		
	}
	@GetMapping(value = "/oficinaSinFiltro")
	public ResponseEntity<List<Oficina>> findOficinasSinFiltro() {
        
		return ResponseEntity.ok(oficinaService.findAll());
	}
	
	@GetMapping(value = "/tipo-acto/clasif-acto-certificado/{id}")
	public ResponseEntity<List<TipoActo>> findTipoActosByClasifActoCertificado(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoActoService.findByClasifActoCertificadoLG(id));
	}
}
