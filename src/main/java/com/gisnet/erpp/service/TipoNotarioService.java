package com.gisnet.erpp.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoNotario;
import com.gisnet.erpp.repository.TipoNotarioRepository;

@Service
public class TipoNotarioService {
	@Autowired
	TipoNotarioRepository tipoNotarioRepository;
	
	public List<TipoNotario> findAll() {
		return tipoNotarioRepository.findAll();
	}

	public TipoNotario create(TipoNotario tiponotario) {
		tipoNotarioRepository.save(tiponotario);
		return tiponotario;
	}

	public Long delete(TipoNotario tiponotario) {
		tipoNotarioRepository.delete(tiponotario);
		return 0l;
	}

}
