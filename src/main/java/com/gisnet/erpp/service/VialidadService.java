package com.gisnet.erpp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Vialidad;
import com.gisnet.erpp.repository.VialidadRepository;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class VialidadService {
	@Autowired
	VialidadRepository vialidadRepository;
	
	@Transactional(readOnly = true)
	public Vialidad getVialidad(Long id) {
		return vialidadRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public List<Vialidad> findByMunicipioIdAndTipoVialidadId(Long municipioId, Long tipoVialidadId){
		return vialidadRepository.findByMunicipioIdAndTipoVialidadId(municipioId, tipoVialidadId);
	}


}
