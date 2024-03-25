package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Campo;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.repository.CampoRepository;
import com.gisnet.erpp.repository.CampoValoresRepository;

@Service
public class CampoService{
	@Autowired
	CampoRepository campoRepository;
	@Autowired
	CampoValoresRepository campoValoresRepository;

	public Campo findOne(Long id) {
		return campoRepository.findOne(id);
	}

	public List<Campo> findAll() {
			return campoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Page<Campo> getAllCampos(Pageable pageable, String nombre) {
		return campoRepository.getAllCampos(pageable, nombre);
	}
	
	@Transactional
	public Long save(Campo campo) {
		campoRepository.save(campo);
		 List<CampoValores> datos = campoValoresRepository.findByCampoId(campo.getId());
		 for (CampoValores valor : campo.getCampoValoresParaCampos()) {
				int i = datos.indexOf(valor);
				if (i >= 0 ) {
					datos.remove(i);
				}
				valor.setCampo(campo);
				campoValoresRepository.save(valor);
		}
		campoValoresRepository.delete(datos);
		
		return campo.getId();
	}
	
	@Transactional
	public Long delete(Campo campo) {
		campoRepository.delete(campo);
		return campo.getId();
	}
}
