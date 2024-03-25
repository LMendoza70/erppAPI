package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Orientacion;
import com.gisnet.erpp.repository.OrientacionRepository;

@Service
public class OrientacionService {

	@Autowired
	private OrientacionRepository orientacionRepository;
	
	public List<Orientacion> findAll(){
		return orientacionRepository.findAll();
	}
	
	public Orientacion create(Orientacion orientacion)
	{
		orientacionRepository.save(orientacion);
		return orientacion;
	}
	
	public Long delete(Orientacion orientacion) {
		orientacionRepository.save(orientacion);
		return 0l;
	}
}
