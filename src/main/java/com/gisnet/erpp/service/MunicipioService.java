package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.repository.MunicipioRepository;

@Service
public class MunicipioService{
	@Autowired
	MunicipioRepository municipioRepository;

	public Municipio findOne(Long id) {
		return municipioRepository.findOne(id);
}

	public List<Municipio> findAll() {
			return municipioRepository.findAllByOrderByNombreAsc();
	}
	
	public List<Municipio> findAllbyEstadoId(Long id) {
		return municipioRepository.findByEstadoIdAndActivoTrueOrderByNombreAsc(id);
	}
	
	public Boolean create(Municipio entidadMunicipio)
	{   Boolean bandera;
		Estado estado = entidadMunicipio.getEstado();
		String  nombre  = entidadMunicipio.getNombre().replace(" ", "");
		
		List <Municipio> listaMunicipio = municipioRepository.findMunicipioByEstadodoAndNombre(nombre, estado);
		
		if(listaMunicipio.size()>0) {
			bandera = false;
			System.out.println("Ya existe un registro para este municipio");
		}else {
			nombre.toUpperCase();
			entidadMunicipio.setNombre(nombre);
			municipioRepository.save(entidadMunicipio);
			bandera = true;
		}
		
	
	//System.out.println((municipioRepository.findOne(entidadMunicipio.getId())));
	//	return municipioRepository.findOne(entidadMunicipio.getId());
		return bandera;
	}
	
	public Long delete(Municipio entidadMunicipio)
	{
		municipioRepository.delete(entidadMunicipio);
		
		return 0L;
	}

	
	

	
}
