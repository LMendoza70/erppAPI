package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoOficio;
import com.gisnet.erpp.repository.TipoOficioRepository;

@Service
public class TipoOficioService {
	
	@Autowired
	TipoOficioRepository tipoOficioRepository;

	public TipoOficio findOne(Long id) {
		return tipoOficioRepository.findOne(id);
    }

	public List<TipoOficio> findAll() {
			return tipoOficioRepository.findAll();
	}
	
	public TipoOficio create(TipoOficio tipoOficio)
	{
		tipoOficioRepository.save(tipoOficio);
		return tipoOficio;
	}
	
	public Long delete(TipoOficio tipoOficio) {
		tipoOficioRepository.save(tipoOficio);
		return 0l;
	}
}
