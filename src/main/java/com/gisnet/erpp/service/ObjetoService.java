package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Objeto;
import com.gisnet.erpp.repository.ObjetoRepository;

@Service
public class ObjetoService {

	
	@Autowired
	private ObjetoRepository objetoRepository;
	
	public List<Objeto> findAll(){
		return objetoRepository.findAll();
	}
}
