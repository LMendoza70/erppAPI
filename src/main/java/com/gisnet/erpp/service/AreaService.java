package com.gisnet.erpp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.AreaClasifActo;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.AreaRepository;

@Service
public class AreaService{
	@Autowired
	AreaRepository areaRepository;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	AreaClasifActoService areaClasifActoService;

	public Area findOne(Long id) {
		return areaRepository.findOne(id);
	}

	public List<Area> findAll() {
			return areaRepository.findAll();
	}


	public List<Area> findAllByTipoUsuario() {
			Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			return areaRepository.findAllByTipoUsuario(user.getTipoUsuario().getId());
	}





	@Transactional(readOnly = true)
	public Page<Area> getAllAreas(Pageable pageable, String nombre) {
		return areaRepository.getAllAreas(pageable, nombre);
	}

	@Transactional
	public Long save(Area area) {
		areaRepository.save(area);
		return area.getId();
	}

	@Transactional
	public Long delete(Area area) {
		areaRepository.delete(area);
		return area.getId();
	}

	public List<Area> findByClasifActoId(Long idClasifActo) {
		List<AreaClasifActo> areasClasifActo = areaClasifActoService.findByClasifActoId(idClasifActo);
		return areasClasifActo.stream().map(clasifActo -> clasifActo.getArea()).collect(Collectors.toList());
	}
	
	public List<Area> findFilter() {
		Integer[] idUsables = new Integer[] { 1,9 };
		return areaRepository.findAreafilter(idUsables);
	}
}
