package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Parentesco;
import com.gisnet.erpp.repository.ParentescoRepository;

@Service
public class ParentescoService{
	@Autowired
	ParentescoRepository parentescoRepository;

	public Parentesco findOne(Long id) {
		return parentescoRepository.findOne(id);
	}

	public List<Parentesco> findAll(){
		return parentescoRepository.findAll();
	}

	public Parentesco create(Parentesco parentesco) {
		parentescoRepository.save(parentesco);
		return parentesco;
	}

	public Long delete(Parentesco parentesco) {
		parentescoRepository.delete(parentesco);
		return 0l;
	}
	
	
}
