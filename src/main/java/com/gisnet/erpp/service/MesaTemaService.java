package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.MesaTema;
import com.gisnet.erpp.repository.MesaTemaRepository;

@Service
public class MesaTemaService{
	@Autowired
	MesaTemaRepository mesaTemaRepository;

	public MesaTema findOne(Long id) {
		return mesaTemaRepository.findOne(id);
	}

	public List<MesaTema> findAll() {
			return mesaTemaRepository.findAll();
	}
	
}
