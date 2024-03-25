package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.repository.ClasifActoRepository;

@Service
public class ClasifActoService{
	@Autowired
	ClasifActoRepository clasifActoRepository;

	public ClasifActo findOne(Long id) {
		return clasifActoRepository.findOne(id);
	}

	public List<ClasifActo> findByAreaClasifActosAreaId(Long id) {
			return clasifActoRepository.findByAreaClasifActosParaClasifActosAreaId(id);
	}

	public List<ClasifActo> findAll() {
		return clasifActoRepository.findAll();
	}

	public List<ClasifActo> findByClasifActosServicioId(Long id) {
			return clasifActoRepository.findAllByServicio(id);
	}

	public List<ClasifActo> findByAreaIdAndSeccionFolioId(Long idArea,Long idSeccionFolio) {
			return clasifActoRepository.findAllByAreaIdAndSeccionFolioId(idArea,idSeccionFolio);
	}

	@Transactional(readOnly = true)
	public Page<ClasifActo> getAllClasifActos(Pageable pageable, String nombre) {
		return clasifActoRepository.getAllClasifActos(pageable, nombre);
	}

	@Transactional
	public Long save(ClasifActo clasifActo) {
		clasifActoRepository.save(clasifActo);
		return clasifActo.getId();
	}

	@Transactional
	public Long delete(ClasifActo clasifActo) {
		clasifActoRepository.delete(clasifActo);
		return clasifActo.getId();
	}
}
