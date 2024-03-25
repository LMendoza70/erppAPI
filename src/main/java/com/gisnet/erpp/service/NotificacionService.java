package com.gisnet.erpp.service;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.EstructuraOrg;
import com.gisnet.erpp.domain.Notificacion;
import com.gisnet.erpp.domain.StatusNotificacion;
import com.gisnet.erpp.domain.TipoAclaracion;
import com.gisnet.erpp.domain.TipoNotificacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.repository.AreaRepository;
import com.gisnet.erpp.repository.EstructuraOrgRepository;
import com.gisnet.erpp.repository.NotificacionRepository;
import com.gisnet.erpp.repository.UsuarioRepository;

@Service
public class NotificacionService {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	NotificacionRepository notificacionRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	EstructuraOrgRepository estructuraOrgRepository;
	
	@Autowired
	private EstructuraOrgService estructuraOrgService;
	

	public List<Notificacion> findByUserLogged() {

		Usuario user = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		List<Long> status = new ArrayList<Long>();
		status.add(1L);
		status.add(2L);

		return notificacionRepository.findAllByUsuarioDestinatarioAndStatusNotificacionIdIn(user,status);
	}
	
	
	public Long findByUserMenu() {

		Usuario user = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		List<Long> status = new ArrayList<Long>();
		status.add(1L);
		status.add(2L);

		return notificacionRepository.countByUsuarioDestinatarioAndStatusNotificacionIdIn(user,status);
	}
	
	public Long notificacionVista(Notificacion notificacion) {
		
		System.out.println("Estatus ----- > " + notificacion.getStatusNotificacion().getId());
		//notificacion.getStatusNotificacion().setId(2L);
		
		notificacionRepository.saveAndFlush(notificacion);
		
		return null;
	}
	
	public Long generaNotificacion(String msg,Long tipo,Long destinatario,Long remitente) {
		
		try {
			Notificacion notificacion = new Notificacion();
			
			notificacion.setFechaEnvio(new Date());
			notificacion.setNotificacion(msg);
			
			StatusNotificacion statusNotificacion = new StatusNotificacion();
			statusNotificacion.setId(1L);
			notificacion.setStatusNotificacion(statusNotificacion);
			
			TipoNotificacion tipoNotificacion = new TipoNotificacion();
			tipoNotificacion.setId(tipo);
			notificacion.setTipoNotificacion(tipoNotificacion);
			
			
			notificacion.setUsuarioDestinatario(usuarioRepository.findOne(destinatario));
			
			if(remitente != null) {
			notificacion.setUsuarioRemitente(usuarioRepository.findOne(remitente));
			}
			
			notificacionRepository.saveAndFlush(notificacion);
			
			return 1L;
		} catch (Exception e) {
			return 0L;
		}
		
	}
	
	 
	public List<Usuario> obtenerDestinatarios(Long destinatario) {
		int d  = destinatario.intValue();
		Usuario user = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		
		List<Usuario> listaDestinatarios = new ArrayList<Usuario>();
		
		
		switch(d) {
		case 1:
			listaDestinatarios= estructuraOrgService.getSuperioresByUsuario(user);
			break;
		case 2:
			listaDestinatarios = estructuraOrgService.getSubordinadosByUsuario(user);
			break;
		case 3:
			Long idUser = user.getId();
			Long officeId = user.getOficina().getId();
			listaDestinatarios = usuarioService.getUsuariosByAreAndOffice( idUser, officeId );
			break;
		case 4:
			listaDestinatarios = usuarioRepository.findAllByTipoUsuarioAndActivo();
			break;
		}
		return listaDestinatarios;
	}
	
	public Long enviarComunicado( String msg, Long tipo, Long destinatario, List<Usuario> destinatarioSelect) {
		Long l = (long) destinatarioSelect.size();
		Long bnd = 0L;
		Usuario user = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		if(l== null ) {
			int d  = destinatario.intValue();
			
			List<Usuario> listaDestinatarios = usuarioRepository.findAllByTipoUsuarioAndActivo();
			
			switch (d) {
			case 4:
			    listaDestinatarios = usuarioRepository.findAllByTipoUsuarioAndActivo();
				Long remit = user.getId();
				
				for(Usuario usuarios : listaDestinatarios) {			
					Long des = usuarios.getId();
					generaNotificacion(msg,tipo,des,remit);
					bnd = 1L;
					
				}
				break;
			}
				
		}else {
			Long remit = user.getId();
			for(Usuario usuarios : destinatarioSelect) {
				Long des = usuarios.getId();
				generaNotificacion(msg,tipo,des,remit);
				bnd = 1L;
			}
		}
		return bnd;	
	}
	
	public List<Usuario> findSubordinados(List<Usuario> usuario) {
		List<EstructuraOrg> estructuraOrg = estructuraOrgRepository.findByUsuariosMaster(usuario);
		List<Usuario> subordinados = new ArrayList<Usuario>();
		for(EstructuraOrg EO : estructuraOrg) {
			subordinados.add(EO.getUsuarioDetail());	
		}
	return subordinados;
	}
	
	public List<Usuario> findSuperiores(List<Usuario> usuario){
		List<EstructuraOrg> estructuraOrg = estructuraOrgRepository.findByUsuariosDetail(usuario);
		List<Usuario> superiores = new ArrayList<Usuario>();
		for(EstructuraOrg EO : estructuraOrg) {
			superiores.add(EO.getUsuarioMaster());	
			
		}
		return superiores;
	}

}
