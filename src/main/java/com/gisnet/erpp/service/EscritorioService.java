package com.gisnet.erpp.service;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Escritorio;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.EscritorioRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class EscritorioService {
	
	private static final Logger log = LoggerFactory.getLogger(EscritorioService.class);

	@Autowired
	private EscritorioRepository escritorioRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public void createEscritoriosByOficina(Long idOficina,Date fechaActual,Long idRol){
		List<Usuario> usuariosOficina = null;
		Long existentes = escritorioRepository.countByUsuarioOficinaIdAndDiaEscritorioAndRolId(idOficina, fechaActual,idRol);
		log.debug("Escritorios esxistentes para la fecha [{}], oficina [{}] : {}",fechaActual,idOficina,existentes);
		if(existentes.longValue() == 0){
			usuariosOficina = usuarioRepository.findByOficinaIdAndUsuarioRolesParaUsuariosRolIdAndActivo(idOficina, idRol,Boolean.TRUE);
			usuariosOficina.forEach(usuario -> {
				escritorioRepository.save(createEscritorio(usuario, fechaActual,idRol));
			});
		}
	}
	
	public List<Escritorio> findByDiaEscritorioAndRolIdAndUsuarioIdInOrderByPonderacionAsc(Date fecha,Long idRol,List<Long> idsUsuarios){
		return escritorioRepository.findByDiaEscritorioAndRolIdAndUsuarioIdInOrderByPonderacionAsc(fecha,idRol,idsUsuarios);
	}
	
	public void save(Escritorio escritorio){
		escritorioRepository.save(escritorio);
	}
	
	private Escritorio createEscritorio(Usuario usuario, Date fechaActual,Long idRol){	
		Rol rol = new Rol();
		rol.setId(idRol);
		Escritorio escritorio = new Escritorio();
		escritorio.setDiaEscritorio(fechaActual);
		escritorio.setPonderacion(Constantes.PONDERACION_INICIAL);
		escritorio.setUsuario(usuario);
		escritorio.setRol(rol);
		return escritorio;
	}
}
