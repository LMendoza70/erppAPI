package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Asentamiento;
import com.gisnet.erpp.repository.AsentamientoRepository;

@Service
public class AsentamientoService {
	@Autowired
	AsentamientoRepository asentamientoRepository;

	@Transactional(readOnly = true)
	public List<Asentamiento> findByMunicipioIdAndTipoAsentId(Long municipioId, Long tipoAsentId){
		return asentamientoRepository.findByMunicipioIdAndTipoAsentId(municipioId, tipoAsentId);
	}

	
	@Transactional(readOnly = true)
	public Page<Asentamiento> findAllByNombre(String nombre, Long tipoUsuarioId, Pageable pageable, Long estadoId, Long municipioId) {
		return asentamientoRepository.findAllByNombre(nombre, tipoUsuarioId, pageable, estadoId, municipioId);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Long save(Asentamiento asentamiento) throws UnsupportedOperationException {
		try {
			asentamientoRepository.saveAndFlush(asentamiento);
		}
		
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
		return asentamiento.getId();
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void delete(Long id) throws UnsupportedOperationException {
		try {
			asentamientoRepository.delete(id);
		}
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
