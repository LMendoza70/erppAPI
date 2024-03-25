package com.gisnet.erpp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.repository.IdentificacionRepository;
import com.gisnet.erpp.security.SecurityUtils;

import java.io.IOException;

@Service
public class IdentificacionService {
	@Autowired
	IdentificacionRepository identificacionRepository;
	
	@Transactional(readOnly = true)
	public Page<Identificacion> findAllByNombre(Pageable pageable, String valor, Long personaId, Long tipoIdenId) {
		return identificacionRepository.findAllByNombre(pageable, valor, personaId, tipoIdenId);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Long save(Identificacion identificacion) throws UnsupportedOperationException {
		try {
			identificacionRepository.saveAndFlush(identificacion);
		}
		
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
		return identificacion.getId();
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void delete(Long id) throws UnsupportedOperationException {
		try {
			identificacionRepository.delete(id);
		}
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}
}
