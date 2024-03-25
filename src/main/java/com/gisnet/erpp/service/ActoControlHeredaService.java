package com.gisnet.erpp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoControlHereda;
import com.gisnet.erpp.repository.ActoControlHeredaRepository;

@Service
public class ActoControlHeredaService {

	@Autowired
	ActoControlHeredaRepository actoControlHeredaRepository;
	
	
	public List<ActoControlHereda> findByActoId(Long actoId){
		return actoControlHeredaRepository.findAllByActoId(actoId);
	}
	
	@Transactional
	public ActoControlHereda save(Acto acto, Acto actoHereda) {
		ActoControlHereda ace = new ActoControlHereda();
		ace.setActo(acto);
		ace.setActoHeredado(actoHereda);
		actoControlHeredaRepository.save(ace);
		return ace;
	}
	
	
	@Transactional
	public boolean save(Acto acto, List<Acto> actoHereda) {
		try {
			actoHereda.forEach(x->{
				ActoControlHereda ace = new ActoControlHereda();
				ace.setActo(acto);
				ace.setActoHeredado(x);
				actoControlHeredaRepository.save(ace);
			});
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	@Transactional
	public Long deleteByActoId(Long actoId) {
		return actoControlHeredaRepository.deleteByActoId(actoId);
	}
	
	
}
