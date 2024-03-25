package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.repository.EstadoRepository;

@Service
public class EstadoService{
	@Autowired
	EstadoRepository estadoRepository;

	public Estado findOne(Long id) {
		return estadoRepository.findOne(id);
}

	public List<Estado> findAll() {
			return estadoRepository.findAll();
	}
	
	
}
