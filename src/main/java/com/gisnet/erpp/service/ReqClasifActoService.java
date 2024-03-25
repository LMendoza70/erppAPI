package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.ReqClasifActo;
import com.gisnet.erpp.repository.ReqClasifActoRepository;

@Service
public class ReqClasifActoService{
	@Autowired
	ReqClasifActoRepository reqClasifActoRepository;

	public ReqClasifActo findOne(Long id) {
		return reqClasifActoRepository.findOne(id);
}

	public List<ReqClasifActo> findByClasifActo(Long id) {
			return reqClasifActoRepository.findByClasifActoId(id);
	}
	
	
}
