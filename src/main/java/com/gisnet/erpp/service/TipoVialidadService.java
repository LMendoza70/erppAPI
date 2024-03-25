package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoVialidad;
import com.gisnet.erpp.repository.TipoVialidadRepository;

@Service
public class TipoVialidadService {

	@Autowired
	private TipoVialidadRepository tipoVialidadRepository;
	
	public List<TipoVialidad> findAll(){
		return tipoVialidadRepository.findAll();
	}

	public TipoVialidad create(TipoVialidad tipovialidad) {
		tipoVialidadRepository.save(tipovialidad);
		return tipovialidad;
	}

	public Long delete(TipoVialidad tipovialidad) {
		tipoVialidadRepository.delete(tipovialidad);
		return 0l;
	}
	
	
	
}
