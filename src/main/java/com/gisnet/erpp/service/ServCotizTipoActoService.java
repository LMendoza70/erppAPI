package com.gisnet.erpp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.ServCotizTipoActo;
import com.gisnet.erpp.repository.ServCotizTipoActoRepository;

@Service
public class ServCotizTipoActoService {

	@Autowired
	private ServCotizTipoActoRepository servCotizTipoActoRepository;
	
	public ServCotizTipoActo findByTipoActoId(long tipoActoId){
		return servCotizTipoActoRepository.findByTipoActoId(tipoActoId);
	}
	
}

