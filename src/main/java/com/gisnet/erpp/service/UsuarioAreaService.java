package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.UsuarioArea;
import com.gisnet.erpp.repository.UsuarioAreaRepository;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class UsuarioAreaService {
	@Autowired
	UsuarioAreaRepository usuarioAreaRepository;

		
	@Transactional(readOnly = true)
	public UsuarioArea findFirstByAreaId(Long areaId) {
		return usuarioAreaRepository.findFirstByAreaId(areaId);
	}	
	
	public List<UsuarioArea> findAllByUsuarioId(Long idUser) {
		List<UsuarioArea> usAreas = usuarioAreaRepository.findAllByUserId(idUser);
		return  usAreas;
	}
	
}
