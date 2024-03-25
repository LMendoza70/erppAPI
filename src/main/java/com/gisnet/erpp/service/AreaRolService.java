package com.gisnet.erpp.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.repository.AreaRolRepository;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class AreaRolService {
	@Autowired
	AreaRolRepository areaRolRepository;

		
	@Transactional(readOnly = true)
	public AreaRol findOne(Long areaRolId) {
		return areaRolRepository.findOne(areaRolId);
	}	
	
}
