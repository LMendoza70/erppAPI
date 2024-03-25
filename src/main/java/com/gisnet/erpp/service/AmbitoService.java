package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Ambito;
import com.gisnet.erpp.repository.AmbitoRepository;

@Service
public class AmbitoService{
	@Autowired
	AmbitoRepository ambitoRepository;

	public Ambito findOne(Long id) {
		return ambitoRepository.findOne(id);
	}

	public List<Ambito> findAll() {
			return ambitoRepository.findAll();
	}
}
