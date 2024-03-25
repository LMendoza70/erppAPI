package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoInmueble;
import com.gisnet.erpp.repository.TipoInmuebleRepository;

@Service
public class TipoInmuebleService {
	
	@Autowired
	private TipoInmuebleRepository tipoInmuebleRepository;

	public List<TipoInmueble> findAll(){
		return tipoInmuebleRepository.findAll();
	}

	public TipoInmueble create(TipoInmueble tipoinmueble) {
		tipoInmuebleRepository.save(tipoinmueble);
		return tipoinmueble;
	}
	
	public Long delete(TipoInmueble tipoinmueble) {
		tipoInmuebleRepository.delete(tipoinmueble);
		return 0l;
	}
}
