package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.CampoCotizador;
import com.gisnet.erpp.repository.CampoCotizadorRepository;

@Service
public class CampoCotizadorService {

	@Autowired
	private CampoCotizadorRepository campoCotizadorRepository;
	
	
	public List<CampoCotizador> findByConfCotizadoresParaCamposCotizadorConceptoPagoId(Long conceptoPagoId){
		return campoCotizadorRepository.findByConfCotizadoresParaCamposCotizadorConceptoPagoId(conceptoPagoId);
	}
	
	public List<CampoCotizador> findAll(){
		return campoCotizadorRepository.findAll();
	}


	public CampoCotizador create(CampoCotizador campoCotizador) {
		campoCotizadorRepository.save(campoCotizador);
		return campoCotizador;
	}
	
	public Long delete(CampoCotizador campoCotizador) {
		campoCotizadorRepository.delete(campoCotizador);
		return 0l;
	}
	
}
