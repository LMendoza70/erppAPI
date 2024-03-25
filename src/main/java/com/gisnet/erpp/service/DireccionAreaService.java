package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.DireccionArea;
import com.gisnet.erpp.repository.DireccionAreaRepository;

@Service
public class DireccionAreaService {

	@Autowired	
	private DireccionAreaRepository direccionRepository;
	
	
	public List<DireccionArea> findAll(){
		return direccionRepository.findAll();
	}
}
