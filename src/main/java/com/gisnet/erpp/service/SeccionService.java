package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.repository.SeccionRepository;

@Service
public class SeccionService {

	@Autowired
	private SeccionRepository seccionRepository;
	
	public List<Seccion> findAll(){
		return seccionRepository.findAll();
	}
	
	public List<Seccion> findSeccionesOficina(Long idOficina){
		return seccionRepository.findBySeccionesPorOficinaOficinaId(idOficina);
	}
	
	public Seccion findSeccionById(Long idSeccion) {
		return seccionRepository.findById(idSeccion);
	}
}
