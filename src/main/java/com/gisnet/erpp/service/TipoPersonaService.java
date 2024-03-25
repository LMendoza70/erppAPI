package com.gisnet.erpp.service;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoPersona;
import com.gisnet.erpp.repository.TipoPersonaRepository;

@Service
public class TipoPersonaService {
	@Autowired
	TipoPersonaRepository tipoPersonaRepository;
	
	public TipoPersona findOne(Long id) {
		return tipoPersonaRepository.findOne(id);
	}
	
	public List<TipoPersona> findAll() {
		return tipoPersonaRepository.findAll();
	}

	public TipoPersona create(TipoPersona tipoPersona) {

		tipoPersonaRepository.save(tipoPersona);
		return tipoPersona;
	}
	public Long delete(TipoPersona tipoPersona)
	{
		tipoPersonaRepository.delete(tipoPersona);
		return 0l;
	}

}
