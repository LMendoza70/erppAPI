package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Materia;
import com.gisnet.erpp.repository.MateriaRepository;

@Service
public class MateriaService{
	@Autowired
	MateriaRepository materiaRepository;

	public Materia findOne(Long id) {
		return materiaRepository.findOne(id);
}

	public List<Materia> findAll() {
			return materiaRepository.findAll();
	}

	public Materia create(Materia materia) {
		materiaRepository.save(materia);
		return materia;
	}
	
	public Long delete(Materia materia) {
		materiaRepository.delete(materia);
		return 0l;
	}
	
	
}
