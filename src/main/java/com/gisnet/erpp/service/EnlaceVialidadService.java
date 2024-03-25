package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.EnlaceVialidad;
import com.gisnet.erpp.repository.EnlaceVialidadRepository;

@Service
public class EnlaceVialidadService {
	@Autowired
	EnlaceVialidadRepository enlaceVialidadRepository;
	
	public EnlaceVialidad findOne(Long id) {
		return enlaceVialidadRepository.findOne(id);
}

	public List<EnlaceVialidad> findAll() {
			return enlaceVialidadRepository.findAll();
	}
	
	public EnlaceVialidad create(EnlaceVialidad enlaceVialidad)
	{
		enlaceVialidadRepository.save(enlaceVialidad);
		return enlaceVialidad;
	}
	
	public Long delete(EnlaceVialidad enlaceVialidad) {
		enlaceVialidadRepository.save(enlaceVialidad);
		return 0l;
	}

}
