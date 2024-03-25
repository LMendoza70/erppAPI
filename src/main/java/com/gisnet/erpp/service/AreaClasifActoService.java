package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.AreaClasifActo;
import com.gisnet.erpp.repository.AreaClasifActoRepository;

@Service
public class AreaClasifActoService {
	@Autowired
	AreaClasifActoRepository areaClasifActoRepository;
	
	public List<AreaClasifActo> findByClasifActoId(Long id) {
		return areaClasifActoRepository.findAllByClasifActoId(id);
	}
	
	public List<AreaClasifActo> findByAreaId(Long id) {
		return areaClasifActoRepository.findAllByAreaId(id);
	}
	
	@Transactional
	public Long save(AreaClasifActo areaClasifActo) {
		areaClasifActoRepository.save(areaClasifActo);
		return areaClasifActo.getId();
	}
	
	@Transactional
	public Long delete(AreaClasifActo areaClasifActo) {
		areaClasifActoRepository.delete(areaClasifActo);
		return areaClasifActo.getId();
	}
}
