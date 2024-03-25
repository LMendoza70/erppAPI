package com.gisnet.erpp.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.CampoCancelaActo;
import com.gisnet.erpp.repository.CampoValoresRepository;
import com.gisnet.erpp.repository.CampoCancelaActoRepository;

@Service
public class CampoValorService{
	@Autowired
	CampoValoresRepository campoValoresRepository;
	@Autowired
	CampoCancelaActoRepository campoCancelaActoRepository;

	public CampoValores findOne(Long id) {
		return campoValoresRepository.findOne(id);
}

	public List<CampoValores> findByTipoCampoId(Long id) {
			return campoValoresRepository.findByCampoId(id);
	}
	
	 public Set<CampoCancelaActo> findCancelaActoByTipoCampoId(Long id) {
		 return campoCancelaActoRepository.findCancelaActoByCampoIdQuery(id);
	 }

	 public Set<CampoCancelaActo> findCancelaActoByCampoValorId(Long id) {
		 return campoCancelaActoRepository.findByCampoValorId(id);
	 }
}
