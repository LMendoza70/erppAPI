package com.gisnet.erpp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Juez;
import com.gisnet.erpp.repository.JuezRepository;

@Service
public class JuezService {
	@Autowired
	JuezRepository juezRepository;
	
	@Transactional(readOnly = true)
	public Page<Juez> findAllByNombre(String paterno, String materno, String nombre, Long materiaId, Integer noJuez,
			Pageable pageable) {
		return juezRepository.findAllByNombre(paterno, materno, nombre, noJuez, pageable); //, materiaId
	}

	public Juez findOne(Long id) {
		return juezRepository.findOne(id);
	}

	@Transactional(rollbackFor=Exception.class)
	public Juez save(Juez juez) throws UnsupportedOperationException {
		try {
			juezRepository.saveAndFlush(juez);
		}
		
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
		return juez;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void delete(Long id) throws UnsupportedOperationException {
		try {
			juezRepository.delete(id);
		}
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
