package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoDocIdentif;
import com.gisnet.erpp.repository.TipoDocIdentifRepository;

@Service
public class TipoDocIdentifService {

	@Autowired
	private TipoDocIdentifRepository tipoDocIdentifRepository;
	
	public List<TipoDocIdentif> findAll(){
		return tipoDocIdentifRepository.findAll();
	}
}
