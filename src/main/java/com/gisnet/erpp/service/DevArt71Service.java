package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.DevArt71;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.DevArt71Repository;
import com.gisnet.erpp.vo.caratula.DevArt71VO;

/**
 * @author Jorge Medina
 *
 */
@Service
public class DevArt71Service {

	private static final Logger log = LoggerFactory.getLogger(PasesService.class);

	@Autowired
	DevArt71Repository devArt71Repository;
	
	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;
	
	@Autowired
	ActoRepository actoRepository;
	
	
	public DevArt71 saveDevolucion(Prelacion prelacion,Long predioId,Long personaJuridicaId,Long folioSeccionAuxiliarId, Usuario usuario,String causaRechazo,String solicitud) {

		Usuario usuarioSolicitante = prelacion.getUsuarioSolicitan();
		
		String expediente = "";
		List<ActoDocumento> listDocumento = 
				actoDocumentoRepository.getAllDocumentoRechazoByActoPrelacionId( prelacion.getId() );
		if( listDocumento != null && !listDocumento.isEmpty() ) {
			expediente = ""+listDocumento.get(0).getDocumento().getId();
		}
		
		Predio predio = null;
		if( predioId != null ) {
			predio = new Predio();
			predio.setId( predioId );
		}
		PersonaJuridica personaJuridica = null;
		if( personaJuridicaId != null ) {
			personaJuridica = new PersonaJuridica();
			personaJuridica.setId( personaJuridicaId );
		}
		FolioSeccionAuxiliar folioSeccionAuxiliar = null;
		if( folioSeccionAuxiliarId != null ) {
			folioSeccionAuxiliar = new FolioSeccionAuxiliar();
			folioSeccionAuxiliar.setId( folioSeccionAuxiliarId );
		}
		
		DevArt71 devolucion = new DevArt71();
		devolucion.setUsuario( usuario );
		devolucion.setPrelacion( prelacion );
		devolucion.setPredio( predio );
		devolucion.setPersonaJuridica( personaJuridica );
		devolucion.setFolioSeccionAuxiliar( folioSeccionAuxiliar );
		devolucion.setOficina( prelacion.getOficina() );
		devolucion.setSolicitante( usuarioSolicitante.getNombreCompleto() );
		devolucion.setSolicitud( solicitud );
		devolucion.setFechaIngreso( prelacion.getFechaIngreso() );
		devolucion.setExpediente( expediente );
		devolucion.setCausaRechazo( causaRechazo );
		devolucion.setFechaNota( new Date() );
		
		return devArt71Repository.save(devolucion);
	}



	public List<DevArt71VO> findDevoluciones(Long id, Integer tipoFolio) {
		List<DevArt71VO> listaVOs = new ArrayList<DevArt71VO>();
		DevArt71VO devArt71VO = null;

		List<DevArt71> listaDevoluciones = null;
		if( tipoFolio == 1 ) {
			listaDevoluciones = devArt71Repository.findByPersonaJuridicaId(id);
		} else if( tipoFolio == 2 ) {
			listaDevoluciones = devArt71Repository.findByFolioSeccionAuxiliarId(id);
		} else if( tipoFolio == 4 ) {
			listaDevoluciones = devArt71Repository.findByPredioId(id);
		}
		if( listaDevoluciones != null ) {
			for( DevArt71 devArt71 : listaDevoluciones ) {
				devArt71VO = new DevArt71VO();
				devArt71VO.setPrelacion( devArt71.getPrelacion().getConsecutivo() );
				devArt71VO.setFechaNota( devArt71.getFechaNota() );
				devArt71VO.setExpediente( devArt71.getExpediente() );
				devArt71VO.setSolicitante( devArt71.getSolicitante() );
				devArt71VO.setSolicitud( devArt71.getSolicitud() );
				listaVOs.add( devArt71VO );
			}
		}
		
		return listaVOs;
	}
	
}
