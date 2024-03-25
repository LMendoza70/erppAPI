package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.EstructuraOrg;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.EstructuraOrgRepository;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class EstructuraOrgService {
	
	@Autowired
	EstructuraOrgRepository estructuraOrgRepository;
	

	public List <Usuario> getSubordinadosByUsuario(Usuario usuario) {
		
		List<EstructuraOrg> subUsuarios = estructuraOrgRepository.findByUsuarioMaster(usuario);
		List<Usuario> subordinados = new ArrayList<Usuario>();
		
		for(EstructuraOrg eo : subUsuarios){subordinados.add(eo.getUsuarioDetail());}
		
		return subordinados;
	}
	
	public List<Usuario> getSuperioresByUsuario(Usuario usuario){
		List<EstructuraOrg> subUsuarios = estructuraOrgRepository.findByUsuarioDetail(usuario);
		List <Usuario> superiores = new ArrayList<Usuario>();
		
		for(EstructuraOrg eo : subUsuarios) {
			superiores.add(eo.getUsuarioMaster());
		}
		return superiores;
	}

		
}
