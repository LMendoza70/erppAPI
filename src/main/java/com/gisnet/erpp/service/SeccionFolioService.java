package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.SeccionFolio;
import com.gisnet.erpp.repository.SeccionFolioRepository;

@Service
public class SeccionFolioService {

	@Autowired
	private SeccionFolioRepository seccionFolioRepository;

	public List<SeccionFolio> findAll(){
		return seccionFolioRepository.findAll();
	}
}
