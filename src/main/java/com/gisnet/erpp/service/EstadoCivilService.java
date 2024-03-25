package com.gisnet.erpp.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.EstadoCivil;
import com.gisnet.erpp.repository.EstadoCivilRepository;

@Service
public class EstadoCivilService{
	@Autowired
	EstadoCivilRepository estadoCivilRepository;

	public EstadoCivil findOne(Long id) {
		return estadoCivilRepository.findOne(id);
}

	public List<EstadoCivil> findAll() {
			return estadoCivilRepository.findAll();
	}
	
	public EstadoCivil create(EstadoCivil estadoCivil)
	{
		estadoCivilRepository.save(estadoCivil);
		return estadoCivil;
	}
	
	public Long delete(EstadoCivil estadoCivil) {
		estadoCivilRepository.save(estadoCivil);
		return 0l;
	}
	
}
