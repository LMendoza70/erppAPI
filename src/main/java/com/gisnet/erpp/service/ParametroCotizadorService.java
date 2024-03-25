package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.ParametroCotizador;
import com.gisnet.erpp.repository.ParametroCotizadorRepository;

@Service
public class ParametroCotizadorService {

	@Autowired
	ParametroCotizadorRepository parametroCotizadorRepository;
	
	public ParametroCotizador findOne(Long id) {
		return parametroCotizadorRepository.findOne(id);
}

	public List<ParametroCotizador> findAll() {
			return parametroCotizadorRepository.findAll();
	}
	
	public ParametroCotizador create(ParametroCotizador parametroCotizador)
	{
		parametroCotizadorRepository.save(parametroCotizador);
		return parametroCotizador;
	}
	
	public Long delete(ParametroCotizador parametroCotizador) {
		parametroCotizadorRepository.save(parametroCotizador);
		return 0l;
	}
	
	
}
