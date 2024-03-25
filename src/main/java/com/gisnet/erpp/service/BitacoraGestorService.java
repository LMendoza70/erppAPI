package com.gisnet.erpp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.BitacoraGestor;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.BitacoraGestorRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class BitacoraGestorService {

	@Autowired
	private BitacoraGestorRepository bitacoraGestorRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	public void saveBitacora(BitacoraGestor BitacoraGestor){
		BitacoraGestor bitacoraGestor = new BitacoraGestor();
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		bitacoraGestor.setUsuarioGestor(usuario);
		bitacoraGestor.setFecha(UtilFecha.getCurrentDate());
		bitacoraGestorRepository.save(bitacoraGestor);
	}
}
