package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.ServiciosCotizador;
import com.gisnet.erpp.repository.ServiciosCotizadorRepository;

@Service
public class ServiciosCotizadorService {

	@Autowired
	private ServiciosCotizadorRepository serviciosCotizadorRepository;
	
	public List<ServiciosCotizador> findAll(){
		return serviciosCotizadorRepository.findAllByOrderByLeyendaTipoServicio();
	}
}
