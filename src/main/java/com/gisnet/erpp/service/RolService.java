package com.gisnet.erpp.service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.domain.Funcion;
import com.gisnet.erpp.domain.FuncionRol;
import com.gisnet.erpp.repository.RolRepository;
import com.gisnet.erpp.repository.AreaRolRepository;
import com.gisnet.erpp.repository.FuncionRepository;
import com.gisnet.erpp.repository.FuncionRolRepository;

@Service
public class RolService {
	@Autowired
	RolRepository rolRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	AreaRolRepository areaRolRepository;
	
	@Autowired
	FuncionRolRepository funcionRolRepository;

	@Autowired
	FuncionRepository funcionRepository;

	@Transactional(readOnly = true)
	public List<Rol> findAll() {
		return rolRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<Rol> findAllRppRolesAssignableByUserName() {
		int nivel = usuarioService.getMinRolLevelFromLogguedUser();
		System.out.println(nivel);
		
		List<Rol> roles = rolRepository.findAllByNivelIsNotNullOrderByNombreAsc();
		return roles.stream().filter(x ->  x.getNivel() >= nivel).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<AreaRol> findAllAreasByRolId(Long id) {
		return areaRolRepository.findAllWithRolsByRolId(id);
	}

	public Page<Rol> findAllBy(Pageable pageable, String nombre, Boolean activo) {
		return rolRepository.findAllBy(pageable, nombre, activo);
	}
	
	@Transactional
	public Long save(Rol rol) {
		Integer maxOrden = rolRepository.findMaxOrden();
		rol.setOrden(maxOrden +1); 
		rolRepository.save(rol);
		return rol.getId();
	}
	
	@Transactional
	public Rol updateFunciones(Rol rol) {
		Rol old = rolRepository.findOne(rol.getId());
		old.getFuncionRolesParaRols().forEach(x -> funcionRolRepository.delete(x.getId()));
		this.funcionRolRepository.flush();
		rol.getFuncionRolesParaRols().forEach(x -> {funcionRolRepository.saveAndFlush(x);});
		return rol;
	}
	
	public Rol findOneById(Long id) {
		return rolRepository.findOne(id);
	}
	
	public List <Funcion> findAllFunciones() {
		return funcionRepository.findAll();
	}
	
	public Set <FuncionRol> getAllFuncionesRolByRolId(Long id, Boolean getHijas, Boolean getPadres) {
		Set<FuncionRol> funciones = new HashSet<>();
		Set<FuncionRol> funcionesPadre = new HashSet<>();
		Set<FuncionRol> funcionesHijas = new HashSet<>();
		rolRepository.findOne(id).getFuncionRolesParaRols().forEach(x -> {
			Funcion funcion = x.getFuncion();
			if( funcion.getFuncionPadre() == null) {
				funcionesPadre.add(x);
			}
			else {
				funcionesHijas.add(x);
			}
		});
		
		if(getPadres) {
			funciones.addAll(funcionesPadre);
		}
		if(getHijas) {
			funciones.addAll(funcionesHijas);
		}
		return funciones;
	}
}
