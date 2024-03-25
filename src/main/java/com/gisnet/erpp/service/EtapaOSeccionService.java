package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.EtapaOSeccion;
import com.gisnet.erpp.repository.EtapaOSeccionRepository;

@Service
public class EtapaOSeccionService {
	
	@Autowired
	private EtapaOSeccionRepository etapaOSeccionRepository;

	public List<EtapaOSeccion> findAll(){
		return etapaOSeccionRepository.findAll();
	}

	public EtapaOSeccion create(EtapaOSeccion tipoinmueble) {
		etapaOSeccionRepository.save(tipoinmueble);
		return tipoinmueble;
	}
	
	public Long delete(EtapaOSeccion tipoinmueble) {
		etapaOSeccionRepository.delete(tipoinmueble);
		return 0l;
	}
}
