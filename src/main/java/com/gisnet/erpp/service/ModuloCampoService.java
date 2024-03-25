package com.gisnet.erpp.service;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.ModuloCampo;
import com.gisnet.erpp.repository.ModuloCampoRepository;

@Service
public class ModuloCampoService {
	@Autowired
	ModuloCampoRepository moduloCampoRepository;
	
	public ModuloCampo findOne(Long id) {
		return moduloCampoRepository.findOne(id);
	}
	@Transactional(readOnly = true)
	public List<ModuloCampo> findByModuloId(Long id) {
		return moduloCampoRepository.findByModuloId(id);
	}
	@Transactional(readOnly = true)
	public Integer findCampoMaxOrden(ModuloCampo moduloCampo) { 
		return moduloCampoRepository.findCampoMaxOrden(moduloCampo.getModulo().getId());
	}
	
	@Transactional
	public Long save(ModuloCampo moduloCampo) {
		moduloCampoRepository.save(moduloCampo);
		return moduloCampo.getId();
	}
	
	@Transactional
	public Long delete(ModuloCampo moduloCampo) {
		moduloCampoRepository.delete(moduloCampo);
		return moduloCampo.getId();
	}
	
	@Transactional
	public Long ordenUp(ModuloCampo moduloCampo) {
		if (moduloCampo.getOrden() > 1 ) {
			ModuloCampo otroModuloCampo = moduloCampoRepository.findCampoPorOrden(moduloCampo.getModulo().getId(), moduloCampo.getOrden()-1);
			if (otroModuloCampo != null) {
				otroModuloCampo.setOrden(otroModuloCampo.getOrden() + 1);
				moduloCampoRepository.save(otroModuloCampo);
			}
			moduloCampo.setOrden(moduloCampo.getOrden() - 1);
			moduloCampoRepository.save(moduloCampo);
		}
		return moduloCampo.getId();
	}
	
	@Transactional
	public Long ordenDown(ModuloCampo moduloCampo) {
		Integer ordenMax = moduloCampoRepository.findCampoMaxOrden(moduloCampo.getModulo().getId());
		if (moduloCampo.getOrden() < ordenMax ) {
			ModuloCampo otroModuloCampo = moduloCampoRepository.findCampoPorOrden(moduloCampo.getModulo().getId(), moduloCampo.getOrden()+1);
			if (otroModuloCampo != null) {
				otroModuloCampo.setOrden(otroModuloCampo.getOrden() - 1);
				moduloCampoRepository.save(otroModuloCampo);
			}
			moduloCampo.setOrden(moduloCampo.getOrden() + 1);
			moduloCampoRepository.save(moduloCampo);
		}
		return moduloCampo.getId();
	}
	
	@Transactional(readOnly = true)
	public Hashtable<Long, ModuloCampo> findByIds(List<Long> ids){	
		Hashtable<Long, ModuloCampo> result = new Hashtable<Long, ModuloCampo>(); 
		List<ModuloCampo> listaResult = moduloCampoRepository.findAllByIdIn(ids);
		for(ModuloCampo moduloCampo : listaResult){
			result.put(moduloCampo.getId(), moduloCampo);
		}
		return result;		
	}
}
