package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Nacionalidad;
import com.gisnet.erpp.repository.NacionalidadRepository;

@Service
public class NacionalidadService{
	@Autowired
	NacionalidadRepository nacionalidadRepository;

	public Nacionalidad findOne(Long id) {
		return nacionalidadRepository.findOne(id);
	}

	public List<Nacionalidad> findAll(){
		return nacionalidadRepository.findAll();
	}

	public Nacionalidad create(Nacionalidad nacionalidad) {
		nacionalidadRepository.save(nacionalidad);
		return nacionalidad;
	}
	
	public Long delete(Nacionalidad nacionalidad) {
		
		nacionalidadRepository.delete(nacionalidad);
		return 0l;
	}
	
	
}
