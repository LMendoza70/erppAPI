package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.FracOCondo;
import com.gisnet.erpp.repository.FracOCondoRepository;

@Service
public class FracOCondoService {
	
	@Autowired
	private FracOCondoRepository fracOCondoRepository;

	public List<FracOCondo> findAll(){
		return fracOCondoRepository.findAll();
	}

	public FracOCondo create(FracOCondo tipoinmueble) {
		fracOCondoRepository.save(tipoinmueble);
		return tipoinmueble;
	}
	
	public Long delete(FracOCondo tipoinmueble) {
		fracOCondoRepository.delete(tipoinmueble);
		return 0l;
	}
}
