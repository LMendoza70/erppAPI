package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoUsuario;
import com.gisnet.erpp.repository.TipoUsuarioRepository;

@Service
public class TipoUsuarioService{
	@Autowired
	TipoUsuarioRepository tipoRepository;

	public TipoUsuario findOne(Long id) {
		return tipoRepository.findOne(id);
}

	public List<TipoUsuario> findAll() {
			return tipoRepository.findAll();
	}
	
	public List<TipoUsuario> findAllSelfRegister() {
		return tipoRepository.findByRegistroTrue();
}

	
	
}
