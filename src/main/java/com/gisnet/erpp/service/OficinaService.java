package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.SeccionPorOficina;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.SeccionesPorOficinaRepository;

import java.util.Set;
import java.util.HashSet;


@Service
public class OficinaService {

	@Autowired
	private OficinaRepository oficinaRepository;
	
	@Autowired
	private SeccionesPorOficinaRepository seccionesPorOficinaRepository;

	
	public List<Oficina> findAll(){
		return oficinaRepository.findAllByOrderByNumOficina();
	}

	public Oficina findById(Long id){
		return oficinaRepository.findById(id);
	}
	
	public Oficina create(Oficina oficina) {
            oficinaRepository.save(oficina);		
		return oficina;
	}

	public Long delete(Oficina oficina) {
		oficinaRepository.delete(oficina);
		return 0l;
	}
	
	public List<SeccionPorOficina> findAllSeccionesByOficinaId(Long oficinaId) {
		
		List<SeccionPorOficina> result = seccionesPorOficinaRepository.findAllWithSeccionByOficinaIdOrderBySeccionNombre(oficinaId);
		return result;
	}
	
	

	
}
