package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.repository.BitacoraRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class BitacoraService {
	@Autowired
	private BitacoraRepository bitacoraRepository;
	@Autowired
	private PrelacionRepository prelacionRepository;
	
	public Bitacora findBitacoraByprelacion(Prelacion prelacion) {
		Status sts = new Status();
		sts.setId(prelacion.getStatus().getId());
		//return  bitacoraRepository.findBitacoraByPrelacionId(prelacion.getId());
		return  bitacoraRepository.findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(prelacion.getId(), sts.getId());
	}	
	
	//JADV-SUSPENSION
	public Bitacora findOneBitacoraByPrelacionIdAndStatusId(Prelacion prelacion) {
		Status sts = new Status();
		sts.setId(Constantes.Status.SUSPENDIDA_CALIFICADOR.getIdStatus());
		return  bitacoraRepository.findTop1BitacoraByPrelacionIdAndStatusIdOrderByIdDesc(prelacion.getId(), sts.getId());
	}	
	
	public List<Bitacora> findByPrelacionAndTipoMotivo(Long prelacionId, Long tipoMotivoId) 
	{
	  	return bitacoraRepository.findByPrelacionIdAndTipoMotivoId(prelacionId, tipoMotivoId);
	}
	
	public List<Bitacora> findByPrelacionAndReproceso(Long prelacionId) 
	{
	  	return bitacoraRepository.findByPrelacionIdAndReprocesoIsTrue(prelacionId);
	}
	
	public List<Bitacora> findBitacoraDevolucion(Long prelacionId) {
		Status sts = new Status();
		sts.setId(Constantes.Status.DEVOLUCION_DOCUMENTO_AUTORIZADO.getIdStatus());
		return  bitacoraRepository.findAllBitacoraByPrelacionIdAndStatusId(prelacionId, sts.getId());
	}
	
	public List<Bitacora> findBitacoraSuspencion(Long prelacionId) {
		Prelacion p = prelacionRepository.findOne(prelacionId);
		Long statusActual=p.getStatus().getId();
		if(statusActual == Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus()){
			Status sts = new Status();
			sts.setId(Constantes.Status.SUSPENDIDA_REGISTRADOR.getIdStatus());
			return  bitacoraRepository.findByPrelacionIdAndStatusIdAndMotivoIdIsNotNullOrderByFechaDesc(prelacionId, sts.getId());
		}else{
			return null;
		}
		
	}
}
