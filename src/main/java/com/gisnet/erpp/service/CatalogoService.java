package com.gisnet.erpp.service;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Asentamiento;
import com.gisnet.erpp.domain.EnlaceVialidad;
import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.domain.Materia;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.domain.Orientacion;
import com.gisnet.erpp.domain.Parentesco;
import com.gisnet.erpp.domain.Regimen;
import com.gisnet.erpp.domain.TipoAclaracion;
import com.gisnet.erpp.domain.TipoCert;
import com.gisnet.erpp.domain.TipoColin;
import com.gisnet.erpp.domain.TipoDocumento;
import com.gisnet.erpp.domain.TipoIden;
import com.gisnet.erpp.domain.TipoInmueble;
import com.gisnet.erpp.domain.TipoMotivo;
import com.gisnet.erpp.domain.TipoNotario;
import com.gisnet.erpp.domain.TipoPersona;
import com.gisnet.erpp.domain.TipoVialidad;
import com.gisnet.erpp.domain.UnidadMedida;
import com.gisnet.erpp.domain.UsoSuelo;
import com.gisnet.erpp.repository.AsentamientoRepository;
import com.gisnet.erpp.repository.EnlaceVialidadRepository;
import com.gisnet.erpp.repository.EstadoCivilRepository;
import com.gisnet.erpp.repository.MateriaRepository;
import com.gisnet.erpp.repository.MunicipioRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.OrientacionRepository;
import com.gisnet.erpp.repository.ParentescoRepository;
import com.gisnet.erpp.repository.RegimenRepository;
import com.gisnet.erpp.repository.TipoAclaracionRepository;
import com.gisnet.erpp.repository.TipoCertRepository;
import com.gisnet.erpp.repository.TipoColinRepository;
import com.gisnet.erpp.repository.TipoDocumentoRepository;
import com.gisnet.erpp.repository.TipoIdenRepository;
import com.gisnet.erpp.repository.TipoInmuebleRepository;
import com.gisnet.erpp.repository.TipoMotivoRepository;
import com.gisnet.erpp.repository.TipoNotarioRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.repository.TipoVialidadRepository;
import com.gisnet.erpp.repository.UnidadMedidaRepository;
import com.gisnet.erpp.repository.UsoSueloRepository;

@Service
public class CatalogoService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EstadoCivilRepository estadoCivilRepository;
	
	@Autowired
	private UsoSueloRepository usoSueloRepository;
	
	@Autowired
	private UnidadMedidaRepository unidadMedidaRepository;
	
	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;
	
	@Autowired
	private TipoVialidadRepository tipoVialidadRepository;
	
	@Autowired
	private TipoInmuebleRepository tipoInmuebleRepository;
	
	@Autowired
	private TipoNotarioRepository tipoNotarioRepository;
	
	@Autowired
	private TipoPersonaRepository tipoPersonaRepository;
	
	@Autowired
	private TipoIdenRepository tipoIdenRepository;
	
	@Autowired
	private TipoCertRepository tipoCertRepository;
	
	@Autowired
	private ParentescoRepository parentescoRepository;
	
	@Autowired
	private NacionalidadRepository nacionalidadRepository;
	
	@Autowired
	private MateriaRepository materiaRepository;
	
	@Autowired
	private EnlaceVialidadRepository enlaceVialidadRepository;
	
	@Autowired
	private OrientacionRepository orientacionRepository;
	
	@Autowired
	private RegimenRepository regimenRepository;
	
	@Autowired
	private TipoMotivoRepository tipoMotivoRepository;
	
	@Autowired
	private TipoColinRepository tipoColinRepository;
	
	@Autowired
	private AsentamientoRepository asentamientoRepository;
	
	@Autowired
	private MunicipioRepository municipioRepository;
	
	@Autowired
	private TipoAclaracionRepository tipoAclaracionRepository;
	
	
	public Boolean validacionCatalogoGeneral (String catalogo, String EC) {
		
		Boolean bandera = false; 
		List<EstadoCivil> listEstadoCivil = new ArrayList<EstadoCivil>();
		List<UsoSuelo> listUsoSuelo = new ArrayList<UsoSuelo>();
		List<UnidadMedida> listUnidadMedida = new ArrayList<UnidadMedida>();
		List<TipoDocumento> listTipoDocumento = new ArrayList<TipoDocumento>();
		List<TipoVialidad> listTipoVialidad = new ArrayList<TipoVialidad>();
		List<TipoInmueble> listTipoInmueble = new ArrayList<TipoInmueble>();
		List<TipoNotario> listTipoNotario = new ArrayList<TipoNotario>();
		List<TipoPersona> listTipoPersona = new ArrayList<TipoPersona>();
		List<TipoIden> listTipoIdentificacion = new ArrayList <TipoIden>();
		List<TipoCert> listTipoCert = new ArrayList<TipoCert>();
		List<Parentesco> listParentesco = new ArrayList<Parentesco>();
		List<Nacionalidad> listNacionalidad = new ArrayList<Nacionalidad>();
		List<Materia> listaMateria = new ArrayList<Materia>();
		List<EnlaceVialidad> listaEnlaceVialidad = new ArrayList<EnlaceVialidad>();
		List<Orientacion> listaOrientacion = new ArrayList<Orientacion>();
		List<Regimen> listaRegimen = new ArrayList<Regimen>();
		List<TipoMotivo> listaTipoMotivo = new ArrayList<TipoMotivo>();
		List<TipoColin> listaTipoColin = new ArrayList<TipoColin>();
		List <Asentamiento> listaAsentamiento = new ArrayList<Asentamiento>();
		List<TipoAclaracion> listaTipoAclaracion = new ArrayList<TipoAclaracion>();
		
		switch (catalogo) {
		 case "EstadoCivil": 	
			listEstadoCivil = estadoCivilRepository.findByEstadoCivil(EC ); 
			if(listEstadoCivil.size() > 0) {
				bandera= false;
			}else {
				bandera = true;
			}
			 break;
		 case "UsoSuelo":
			 listUsoSuelo = usoSueloRepository.findUsoSueloByNombre(EC);
			 if(listUsoSuelo.size() > 0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
		 case "UnidadMedida":
			 listUnidadMedida = unidadMedidaRepository.findUnidadMedidaByNombre(EC);
			 if(listUnidadMedida.size() > 0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
		 case "TipoDocumento":
			 listTipoDocumento = tipoDocumentoRepository.findTipoDocumentoByNombre(EC);
			 if(listTipoDocumento.size()> 0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
			 
		 case "TipoVialidad":
			 listTipoVialidad = tipoVialidadRepository.findTipoVialidadByNombre(EC);
			 if(listTipoVialidad.size()> 0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
			 
		 case "TipoInmueble":
			 listTipoInmueble = tipoInmuebleRepository.findTipoInmuebleByNombre (EC);
			 if(listTipoInmueble.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
			 
		 case "TipoNotario":
			 listTipoNotario = tipoNotarioRepository.findTipoNotarioByNombre(EC);
			 if(listTipoNotario.size()>0) {
				 bandera= false;
			 }else {
				 bandera = true;
			 }
			 break;
			 
		 case "TipoPersona":
			 listTipoPersona = tipoPersonaRepository.findTipoPersonaByNombre(EC);
			 if(listTipoPersona.size()>0) {
				 bandera= false;
			 }else {
				 bandera=true;
			 }
			 break;
			 
		 case "TipoIdentificacion":
			 listTipoIdentificacion = tipoIdenRepository.findTipoIdentificacionByNombre(EC);
			 if(listTipoIdentificacion.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
			 
		 case "TipoCert":
			 listTipoCert = tipoCertRepository.findTipoCertByNombre(EC);
			 if(listTipoCert.size()>0) {
				 bandera = false;
			 }else {
				 bandera= true;
			 }
			 break;
			 
		 case "Parentesco":
			 listParentesco = parentescoRepository.findParentescoByNombre(EC);
			 if(listParentesco.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
			 // falta 
		 case "Nacionalidad":
			 listNacionalidad = nacionalidadRepository.findNacionalidadByNombre(EC);
			 if(listNacionalidad.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
			 
			
		 case "Materia":
			 listaMateria =  materiaRepository.findMateriaByNombre(EC);
			if(listaMateria.size()>0) {
				bandera = false;
			}else {
				bandera = true;
			}
			break;
		 case "EnlaceVialidad":
			 listaEnlaceVialidad = enlaceVialidadRepository.findEnlaceVialidadByNombre(EC);
			 if(listaEnlaceVialidad.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
		 case "Orientacion":
			 listaOrientacion = orientacionRepository.findOrientacionByNombre(EC);
			 if(listaOrientacion.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
		 case "Regimen":
			 listaRegimen = regimenRepository.findRegimenByNombre(EC);
			 if(listaRegimen.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
		 case"TipoMotivo":
			 listaTipoMotivo = tipoMotivoRepository.findTipoMotivoByNombre(EC);
			 if(listaTipoMotivo.size()>0) {
				 bandera= false;
			 }else {
				 bandera= true;
			 }
			 break;
				 
		 case "TipoColin":
			 listaTipoColin = tipoColinRepository.findTipoColinByNombre(EC);
			 if(listaTipoColin.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;
			 
		 
		 /*case "TipoAclaracion":
			 listaTipoAclaracion = tipoAclaracionRepository.findTipoAclaracionByNombre(EC);
			 if(listaTipoAclaracion.size()>0) {
				 bandera = false;
			 }else {
				 bandera = true;
			 }
			 break;*/
		 }
		return bandera;
	} 
	 
}
