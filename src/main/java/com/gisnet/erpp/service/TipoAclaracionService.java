package com.gisnet.erpp.service;

import java.util.List;

import com.gisnet.erpp.domain.Prelacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.TipoAclaracion;
import com.gisnet.erpp.repository.TipoAclaracionRepository;

@Service
public class TipoAclaracionService {

	@Autowired
	private TipoAclaracionRepository tipoAclaracionRepository;
	
	public List<TipoAclaracion> findAll(){
		return tipoAclaracionRepository.findAll();
	}

	
	public List<TipoAclaracion> findAllByInterno(){
		return tipoAclaracionRepository.findAllByInterno(false);
	}

	public List<TipoAclaracion> findAllByExterno(){
		return tipoAclaracionRepository.findAllByInterno(true);
	}

    public TipoAclaracion findOne(Long id) {
		return tipoAclaracionRepository.findOne(id);
    }

}
