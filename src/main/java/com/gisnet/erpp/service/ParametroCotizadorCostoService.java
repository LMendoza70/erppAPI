package com.gisnet.erpp.service;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.ParametroCotizador;
import com.gisnet.erpp.domain.ParametroCotizadorCosto;
import com.gisnet.erpp.repository.ParametroCotizadorCostoRepository;
import com.gisnet.erpp.repository.ParametroCotizadorRepository;
import com.gisnet.erpp.web.api.usuario.UsuarioDTO;

@Service
public class ParametroCotizadorCostoService {
	@Autowired
	ParametroCotizadorCostoRepository parametroCotizadorCostoRepository;

	ParametroCotizadorRepository parametroCotizadorRepository;
	
	/*public ParametroCotizadorCostoDTO findParamCoCosto(){
		List<ParametroCotizadorCosto> ParamCoCosto = parametroCotizadorCostoRepository.findAll();
		List<ParametroCotizador> paramCotizador = parametroCotizadorRepository.findAll();
		ParametroCotizadorCostoDTO parametroCotizadorCosto = new ParametroCotizadorCostoDTO();
		parametroCotizadorCosto.setParametroCotizadorCosto(ParamCoCosto);
		parametroCotizadorCosto.setParametroCotizador(paramCotizador);
		System.out.println("Parametro Cotizador Costo : " + parametroCotizadorCosto);
		
		return  parametroCotizadorCosto;
	}*/
	public List<ParametroCotizadorCosto> findParamCoCosto(){
		List<ParametroCotizadorCosto> ParamCoCosto = parametroCotizadorCostoRepository.findAll();
	
		
		System.out.println("Parametro Cotizador Costo : " + ParamCoCosto);
		
		return ParamCoCosto;
	}
	
	public List<ParametroCotizadorCosto> updateParametroCoCo(List <ParametroCotizadorCosto> parametroCoCo){
		parametroCotizadorCostoRepository.save(parametroCoCo);
		
		return parametroCoCo;
	}
	
	public boolean save(ParametroCotizadorCosto parametroCotizadorCosto) {
		ParametroCotizador ParametroCotizadorId = parametroCotizadorCosto.getParametroCotizador();
		Integer  anio = parametroCotizadorCosto.getAnio();
		Boolean status=false;
		//ParametroCotizadorCosto paramCoCo = new ParametroCotizadorCosto();
		
		try { 
		
			List<ParametroCotizadorCosto> paramCoCo =  parametroCotizadorCostoRepository.findByAnioAndParametroCotizadorCosto(anio, ParametroCotizadorId);
			
			System.out.println("------ tamaño ----->" + paramCoCo.size());
			System.out.println("------Id parametro Co ----->" + ParametroCotizadorId );
			System.out.println("------ AÑO ----->" + anio );
			
		
		if(paramCoCo.size()==0) {
			parametroCotizadorCostoRepository.saveAndFlush(parametroCotizadorCosto);
			status=true;
		}else {
			System.out.println("No se guaradaron los cambios ya que existe un registro con el mismo año y "
					+ "con el mismo Parametro Cotizador");
			status=false;
		}
		
		} catch (Exception e) {
			
		}
		return status;
	}
	
	public Hashtable<String, BigDecimal> findAllByAnio(Integer anio){
		Hashtable<String, BigDecimal> result = new Hashtable<String, BigDecimal>(); 
		List<ParametroCotizadorCosto> lista = parametroCotizadorCostoRepository.findByAnio(anio);
		for(ParametroCotizadorCosto parametroCotizadorCosto : lista){
			result.put(parametroCotizadorCosto.getParametroCotizador().getNombre(), parametroCotizadorCosto.getCosto());
		}
		return result;		
	}
}


