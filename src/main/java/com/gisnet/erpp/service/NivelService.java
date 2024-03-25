package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Nivel;
import com.gisnet.erpp.repository.NivelRepository;

@Service
public class NivelService {
	
	@Autowired
	private NivelRepository nivelRepository;

	public List<Nivel> findAll(){
		return nivelRepository.findAll();
	}

	public Nivel create(Nivel tipoinmueble) {
		nivelRepository.save(tipoinmueble);
		return tipoinmueble;
	}
	
	public Long delete(Nivel tipoinmueble) {
		nivelRepository.delete(tipoinmueble);
		return 0l;
	}
}
