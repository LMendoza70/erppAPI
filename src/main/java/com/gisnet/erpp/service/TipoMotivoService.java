package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoMotivo;
import com.gisnet.erpp.repository.TipoMotivoRepository;

@Service
public class TipoMotivoService {
	
	@Autowired
	TipoMotivoRepository tipoMotivoRepository;

	public TipoMotivo findOne(Long id) {
		return tipoMotivoRepository.findOne(id);
}

	public List<TipoMotivo> findAll() {
			return tipoMotivoRepository.findAll();
	}
	
	public TipoMotivo create(TipoMotivo tipoMotivo)
	{
		tipoMotivoRepository.save(tipoMotivo);
		return tipoMotivo;
	}
	
	public Long delete(TipoMotivo tipoMotivo) {
		tipoMotivoRepository.save(tipoMotivo);
		return 0l;
	}
}
