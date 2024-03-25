package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoSociedad;
import com.gisnet.erpp.repository.TipoSociedadRepository;

@Service
public class TipoSociedadService {

	
	@Autowired
	private TipoSociedadRepository tipoSociedadRepository; 
	
	public List<TipoSociedad> findAll(){
		return tipoSociedadRepository.findAll();
	}
}
