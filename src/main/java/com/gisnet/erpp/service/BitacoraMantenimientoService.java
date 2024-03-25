package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.BitacoraMantenimiento;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.BitacoraMantenimientoRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class BitacoraMantenimientoService {

	@Autowired
	BitacoraMantenimientoRepository bitacoraMantenimientoRepository ; 
	
	@Autowired
	ActoPredioRepository actoPredioRepository; 
	
	public Boolean deleteByActoPredio(Long actoPredioId) {
		
		bitacoraMantenimientoRepository.deleteByActoPredioId(actoPredioId);
		return true;
	}
	
	public String getTitularString(PredioPersona predioPersona) {
		StringBuilder sb = new  StringBuilder();
		Persona newPersona =  predioPersona.getPersona();
		if(newPersona!=null) {
			sb.append(newPersona.getNombre())
			.append(" ").append(newPersona.getPaterno()!=null ? newPersona.getPaterno() : "")
			.append(" ").append(newPersona.getMaterno()!=null ?  newPersona.getMaterno() : "")
			.append(" DD: ").append(predioPersona.getPorcentajeDd())
			.append(" UV: ").append(predioPersona.getPorcentajeUv());
		}
		return sb.toString();
	}
	
	public BitacoraMantenimiento create(Usuario usuario,PredioPersona predioPersona,String elemento,String accion) {
		BitacoraMantenimiento bitacoraMantenimiento = new BitacoraMantenimiento();
		bitacoraMantenimiento.setFecha(new Date());
		bitacoraMantenimiento.setActoPredio(predioPersona.getActoPredio());
		bitacoraMantenimiento.setAccion(accion);
		bitacoraMantenimiento.setElemento(elemento);
		bitacoraMantenimiento.setUsuario(usuario);
		return bitacoraMantenimientoRepository.saveAndFlush(bitacoraMantenimiento);
	}
	
	public BitacoraMantenimiento create(Usuario usuario,String elemento,String accion,Predio predio) {
		BitacoraMantenimiento bitacoraMantenimiento = new BitacoraMantenimiento();
		bitacoraMantenimiento.setFecha(new Date());
		bitacoraMantenimiento.setAccion(accion);
		bitacoraMantenimiento.setElemento(elemento);
		bitacoraMantenimiento.setUsuario(usuario);
		bitacoraMantenimiento.setPredio(predio);
		return bitacoraMantenimientoRepository.saveAndFlush(bitacoraMantenimiento);
	}
	
	
	public BitacoraMantenimiento create(Usuario usuario,ActoPredio actoPredio,String elemento,String accion) {
		BitacoraMantenimiento bitacoraMantenimiento = new BitacoraMantenimiento();
		bitacoraMantenimiento.setFecha(new Date());
		bitacoraMantenimiento.setActoPredio(actoPredio);
		bitacoraMantenimiento.setAccion(accion);
		bitacoraMantenimiento.setElemento(elemento);
		bitacoraMantenimiento.setUsuario(usuario);
		return bitacoraMantenimientoRepository.saveAndFlush(bitacoraMantenimiento);
	}
	
	public BitacoraMantenimiento create(Usuario usuario,Acto acto,String elemento,String accion) {
		Optional<ActoPredio> actoPredio  = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
		BitacoraMantenimiento bitacoraMantenimiento = new BitacoraMantenimiento();
		bitacoraMantenimiento.setFecha(new Date());
		if(actoPredio.isPresent())
		 bitacoraMantenimiento.setActoPredio(actoPredio.get());
		bitacoraMantenimiento.setAccion(accion);
		bitacoraMantenimiento.setElemento(elemento);
		bitacoraMantenimiento.setUsuario(usuario);
		return bitacoraMantenimientoRepository.saveAndFlush(bitacoraMantenimiento);
	}
	
	public List<BitacoraMantenimiento> findByTipoFolio(Long id,Long tipoFolioId){
		List<BitacoraMantenimiento> result = new ArrayList<>();
		if(tipoFolioId.equals((long)Constantes.ETipoFolio.PREDIO.getTipoFolio())) {
			result =  bitacoraMantenimientoRepository.findAllByPredioIdOrderByIdDesc(id);
		}
		
		if(tipoFolioId.equals((long)Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio())) {
			result =  bitacoraMantenimientoRepository.findAllByActoPredioPersonaJuridicaIdOrderByIdDesc(id);
		}
		return result;
	}
}
