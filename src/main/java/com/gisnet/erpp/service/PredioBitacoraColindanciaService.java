package com.gisnet.erpp.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.gisnet.erpp.domain.PredioBitacoraColindancia;
import com.gisnet.erpp.repository.PredioBitacoraColindanciaRepository;
import com.gisnet.erpp.repository.PredioBitacoraRepository;




@Service
public class PredioBitacoraColindanciaService {

	
	@Autowired
	PredioBitacoraColindanciaRepository predioBitacoraColindanciaRepository;
	
	@Autowired
	PredioBitacoraRepository predioBitacoraRepository;

	public PredioBitacoraColindancia findOne(Long id) {
		return predioBitacoraColindanciaRepository.findOne(id);
	}
	
	
}
