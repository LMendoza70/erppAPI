package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.repository.ModuloTipoActoRepository;

@Service
public class ModuloTipoActoService {
	@Autowired
	ModuloTipoActoRepository moduloTipoActoRepository;
	
	public ModuloTipoActo findOne(Long id) {
		return moduloTipoActoRepository.findOne(id);
	}
	@Transactional(readOnly = true)
	public List<ModuloTipoActo> findByTipoActoId(Long id) {
		return moduloTipoActoRepository.findByTipoActoId(id);
	}
	@Transactional(readOnly = true)
	public Integer findModuloMaxOrden(ModuloTipoActo moduloTipoActo) { 
		return moduloTipoActoRepository.findModuloMaxOrden(moduloTipoActo.getTipoActo().getId());
	}
	
	@Transactional
	public Long save(ModuloTipoActo moduloTipoActo) {
		moduloTipoActoRepository.save(moduloTipoActo);
		return moduloTipoActo.getId();
	}
	
	@Transactional
	public Long delete(ModuloTipoActo moduloTipoActo) {
		moduloTipoActoRepository.delete(moduloTipoActo);
		return moduloTipoActo.getId();
	}
	
	@Transactional
	public Long ordenUp(ModuloTipoActo moduloTipoActo) {
		if (moduloTipoActo.getOrden() > 1 ) {
			ModuloTipoActo otroModuloTipoActo = moduloTipoActoRepository.findModuloPorOrden(moduloTipoActo.getTipoActo().getId(), moduloTipoActo.getOrden()-1);
			if (otroModuloTipoActo != null) {
				otroModuloTipoActo.setOrden(otroModuloTipoActo.getOrden() + 1);
				moduloTipoActoRepository.save(otroModuloTipoActo);
			}
			moduloTipoActo.setOrden(moduloTipoActo.getOrden() - 1);
			moduloTipoActoRepository.save(moduloTipoActo);
		}
		return moduloTipoActo.getId();
	}
	
	@Transactional
	public Long ordenDown(ModuloTipoActo moduloTipoActo) {
		Integer ordenMax = moduloTipoActoRepository.findModuloMaxOrden(moduloTipoActo.getTipoActo().getId());
		if (moduloTipoActo.getOrden() < ordenMax ) {
			ModuloTipoActo otroModuloTipoActo = moduloTipoActoRepository.findModuloPorOrden(moduloTipoActo.getTipoActo().getId(), moduloTipoActo.getOrden()+1);
			if (otroModuloTipoActo != null) {
				otroModuloTipoActo.setOrden(otroModuloTipoActo.getOrden() - 1);
				moduloTipoActoRepository.save(otroModuloTipoActo);
			}
			moduloTipoActo.setOrden(moduloTipoActo.getOrden() + 1);
			moduloTipoActoRepository.save(moduloTipoActo);
		}
		return moduloTipoActo.getId();
	}
}
