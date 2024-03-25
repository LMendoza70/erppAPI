package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoAutoridad;
import com.gisnet.erpp.repository.TipoAutoridadRepository;

@Service
public class TipoAutoridadService{
	@Autowired
	TipoAutoridadRepository tipoAutoridadRepository;

	public TipoAutoridad findOne(Long id) {
		return tipoAutoridadRepository.findOne(id);
}

	public List<TipoAutoridad> findAll() {
			return tipoAutoridadRepository.findAll();
	}
	
	
}
