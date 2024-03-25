package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Regimen;
import com.gisnet.erpp.repository.RegimenRepository;

@Service
public class RegimenService {

	@Autowired
	RegimenRepository regimenRepository;
	
	public Regimen findOne(Long id) {
		return regimenRepository.findOne(id);
}

	public List<Regimen> findAll() {
			return regimenRepository.findAll();
	}
	
	public Regimen create(Regimen regimen)
	{
		regimenRepository.save(regimen);
		return regimen;
	}
	
	public Long delete(Regimen regimen) {
		regimenRepository.save(regimen);
		return 0l;
	}
	
	
}
