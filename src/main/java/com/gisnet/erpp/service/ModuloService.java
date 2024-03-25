package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Modulo;
import com.gisnet.erpp.repository.ModuloRepository;

@Service
public class ModuloService {
	@Autowired
	ModuloRepository moduloRepository;

	public Modulo findOne(Long id) {
		return moduloRepository.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public Page<Modulo> getAllModulos(Pageable pageable, String nombre) {
		return moduloRepository.getAllModulos(pageable, nombre);
	}
	
	@Transactional
	public Long save(Modulo modulo) {
		moduloRepository.save(modulo);
		return modulo.getId();
	}
	
	@Transactional
	public Long delete(Modulo modulo) {
		moduloRepository.delete(modulo);
		return modulo.getId();
	}
}
