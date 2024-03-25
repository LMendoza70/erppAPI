package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.UsoSuelo;
import com.gisnet.erpp.repository.UsoSueloRepository;

@Service
public class UsoSueloService {

	@Autowired
	private UsoSueloRepository usoSueloRepository;
	
	public List<UsoSuelo> findAll(){
		return usoSueloRepository.findAll();
	}
	
	public UsoSuelo create(UsoSuelo entidadUsosuelo)
	{
		usoSueloRepository.save(entidadUsosuelo);
		
		return entidadUsosuelo;
	}
	
	public Long delete(UsoSuelo entidadUsosuelo)
	{
		usoSueloRepository.delete(entidadUsosuelo);
		
		return 0L;
	}
}
