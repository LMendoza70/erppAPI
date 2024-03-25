package com.gisnet.erpp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acceso;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.AccesoRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class AccesoService {

	@Autowired
	private AccesoRepository accesoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;


	@Transactional
	public void cerrarSesionesAbiertas() {
		accesoRepository.updateFechaFinWhereFechaFinNull(new Date());
	}
	
	@Transactional
	public void userUnlock(Long userId) {
		accesoRepository.userUnlock(new Date(), userId);
	}
	
	@Transactional
	public void   setLogoutError(String sessionId) {
		 Acceso acceso = accesoRepository.findOneBySessionIdAndUsuarioId(sessionId,SecurityUtils.getCurrentUserId());
	     acceso.setLogoutError(true);
	     acceso.setFechaFin(new Date());
		 accesoRepository.save(acceso);
	}
	
	public Acceso getLogoutError(Long userId) {
		Acceso acceso = accesoRepository.findFirstByUsuarioIdAndFechaFinIsNotNullOrderByFechaFinDesc(userId);
		
		return acceso;
	}

	public List<Acceso> findAll() {
		return accesoRepository.findAll();
	}

	public Acceso login(long usuarioId, String sessionId) {
		Usuario usuario = usuarioRepository.findOne(usuarioId);
		Acceso acceso = new Acceso();
		acceso.setFechaIni(new Date());
		acceso.setUsuario(usuario);
		acceso.setSessionId(sessionId);

		accesoRepository.save(acceso);
		return acceso;
	}

	public void logout(long usuarioId, String sessionId, boolean timeout) {
		Acceso acceso = accesoRepository.findOneByUsuarioIdAndSessionId(usuarioId, sessionId);
		acceso.setFechaFin(new Date());
		acceso.setTimeout(timeout);
		accesoRepository.save(acceso);

	}

	public int countOpenByUsuarioId(long usuarioId) {
		return accesoRepository.countByUsuarioIdAndFechaFinNull(usuarioId);

	}

}
