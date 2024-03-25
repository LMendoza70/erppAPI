package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoAsent;
import com.gisnet.erpp.repository.TipoAsentRepository;

@Service
public class TipoAsentamientoService {

	@Autowired
	private TipoAsentRepository tipoAsentRepository;
	
	public List<TipoAsent> findAll(){
		return tipoAsentRepository.findAll();
	}
}
