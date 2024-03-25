package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoDocumento;
import com.gisnet.erpp.repository.TipoDocumentoRepository;

@Service
public class TipoDocumentoService{
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;

	public TipoDocumento findOne(Long id) {
		return tipoDocumentoRepository.findOne(id);
}

	public List<TipoDocumento> findAll() {
			return tipoDocumentoRepository.findAll();
	}

	public TipoDocumento create(TipoDocumento tipodocumento) {

		tipoDocumentoRepository.save(tipodocumento);
		return tipodocumento;
	}

	public Long delete(TipoDocumento tipodocumento) {
		tipoDocumentoRepository.delete(tipodocumento);
		return 0l;
	}
	
	
}
