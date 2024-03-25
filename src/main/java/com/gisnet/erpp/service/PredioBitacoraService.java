package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioBitacora;
import com.gisnet.erpp.domain.PredioBitacoraColindancia;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.AreaRepository;
import com.gisnet.erpp.repository.PredioBitacoraColindanciaRepository;
import com.gisnet.erpp.repository.PredioBitacoraRepository;

import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.web.api.predio.PredioDTO;

@Service
public class PredioBitacoraService {

	@Autowired
	PredioBitacoraRepository predioBitacoraRepository;
	
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	PredioBitacoraColindanciaRepository predioBitacoraColindanciaRepository;
	
	
	
	public PredioBitacora findOne(Long id) {
		return predioBitacoraRepository.findOne(id);
	}
	
	@Transactional
	public void saveBitacoraPredio(Predio predio, Set<Colindancia> colindancia,Acto acto){
			
			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			
			PredioBitacora predioBitacora = new PredioBitacora();
			predioBitacora.setAsentamiento(predio.getAsentamiento());
			predioBitacora.setClaveCatastral(predio.getClaveCatastral());
			predioBitacora.setClaveCatastralEstandard(predio.getClaveCatastralEstandard());
			predioBitacora.setCodigoPostal(predio.getCodigoPostal());
			predioBitacora.setCuentaCatastral(predio.getCuentaCatastral());
			predioBitacora.setEdificio(predio.getEdificio());
			predioBitacora.setEnlaceVialidad(predio.getEnlaceVialidad());
			predioBitacora.setEtapaOSeccion(predio.getEtapaOSeccion());
			predioBitacora.setFechaAct(UtilFecha.getCurrentDate());
			predioBitacora.setFraccion(predio.getFraccion());
			predioBitacora.setFracOCondo(predio.getFracOCondo());
			predioBitacora.setLocalidadSector(predio.getLocalidadSector());
			predioBitacora.setMacroManzana(predio.getMacroManzana());
			predioBitacora.setManzana(predio.getManzana());
			predioBitacora.setMunicipio(predio.getMunicipio());
			predioBitacora.setNivel(predio.getNivel());
			predioBitacora.setNoLote(predio.getNoLote());
			predioBitacora.setNumExt(predio.getNumExt());
			predioBitacora.setNumExt2(predio.getNumExt2());
			predioBitacora.setNumInt(predio.getNumInt());
			predioBitacora.setOficina(predio.getOficina());
			predioBitacora.setPredio(predio);
			predioBitacora.setSuperficie(predio.getSuperficie());
			predioBitacora.setTipoAsent(predio.getTipoAsent());
			predioBitacora.setTipoInmueble(predio.getTipoInmueble());
			predioBitacora.setTipoVialidad(predio.getTipoVialidad());
			predioBitacora.setTipoVialidad2(predio.getTipoVialidad2());
			predioBitacora.setUnidadMedida(predio.getUnidadMedida());
			predioBitacora.setUsoSuelo(predio.getUsoSuelo());
			predioBitacora.setUsuarioAct(usuario);
			predioBitacora.setVialidad(predio.getVialidad());
			predioBitacora.setVialidad2(predio.getVialidad2());
			predioBitacora.setZona(predio.getZona());
			predioBitacora.setSuperficieM2(predio.getSuperficieM2());
			predioBitacora.setProcedeDeFolio(predio.getProcedeDeFolio());
			predioBitacora.setLibro(predio.getLibro());
			predioBitacora.setDocumento(predio.getDocumento());

			if(acto!=null)
				 predioBitacora.setActoRectificacion(acto);
			
			PredioBitacora obj =  predioBitacoraRepository.saveAndFlush(predioBitacora);

			for (Colindancia col : colindancia){			
				
				PredioBitacoraColindancia bitacoraColindancia = new PredioBitacoraColindancia();

				bitacoraColindancia.setNombre(col.getNombre());
				bitacoraColindancia.setOrientacion(col.getOrientacion());
				bitacoraColindancia.setTipoColin(col.getTipoColin());
				bitacoraColindancia.setVialidad(col.getVialidad());
				bitacoraColindancia.setNombre(col.getNombre());
				bitacoraColindancia.setPredioBitacora(obj);
				predioBitacoraColindanciaRepository.save(bitacoraColindancia);
				
			}/**/
			
		
	}
	
	public PredioBitacora findByActoId(Long actoId) {
		return predioBitacoraRepository.findFirstByActoRectificacionId(actoId);
	}
	
	public void delete(PredioBitacora predio) 
	{
		predioBitacoraColindanciaRepository.delete(predio.getColindancias());
	   	predioBitacoraRepository.delete(predio);
	}
	
	public List<PredioBitacoraColindancia> findBitacoraColindancia(Long predioBitacoraId){
		return predioBitacoraColindanciaRepository.findByPredioBitacoraId(predioBitacoraId);
	}
	
	public List<PredioBitacora> findByPredioActos(Long predioId){
		return  predioBitacoraRepository.findByPredioIdAndActoRectificacionIsNotNullOrderByIdDesc(predioId);
	}
	
	
	
}
