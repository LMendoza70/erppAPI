package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.FuncionRolUsu;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioRol;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.UsuarioAreaRolRepository;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class ActoDocumentoService {

	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioAreaRolRepository usuarioAreaRolRepository;
	
	public List<ActoDocumento> findDocumentosByActoId(Long id) {
		List<ActoDocumento> adoc = new ArrayList<>();
		final ActoDocumento maxVersionDoc = this.getAllActoDocumentoByActoId(id).stream().max(Comparator.comparing(x -> ((ActoDocumento)x).getVersion())).orElse(null);
		if(maxVersionDoc != null) {
			adoc = this.getAllActoDocumentoByActoId(id).stream().filter(x -> x.getVersion().equals(maxVersionDoc.getVersion())).collect(Collectors.toList());	
		}
		return adoc;
	}

	public List<ActoDocumento> getAllActoDocumentoByActoId(Long id) {
		return actoDocumentoRepository.getAllActoDocumentoByActoId(id);
}

	@Transactional
	public Long create(ActoDocumento actoDocumento){
		//Archivo archivo = new Archivo();

		actoDocumentoRepository.save(actoDocumento);



		return actoDocumento.getId();

	}
	@Transactional
	public void delete(Long id){
		
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Long rol = 0L;

		for (Iterator<FuncionRolUsu> it = usuario.getFuncionesRolesParaUsuarios().iterator(); it.hasNext(); ) {
			FuncionRolUsu f = it.next();
			rol = f.getFuncionRol().getRol().getId();
	    }

		if(rol == 4L || rol == 29L){
			actoDocumentoRepository.delete(id);	
		}else {
			actoDocumentoRepository.deleteByActoIdAndVersionNot(actoDocumentoRepository.getOne(id).getActo().getId(), 1);
		}

		
		actoDocumentoRepository.flush();
		return;
	}

	public Long asociar(Long actoId,Long documentoId){
		ActoDocumento ad = actoDocumentoRepository.findActoDocumentoByActoId(actoId);
		Integer version=null;
		if(ad!=null){
			version = ad.getVersion() + 1;
		}
		
		ActoDocumento actodocumento = new ActoDocumento();
		Acto  acto = new Acto();
		acto.setId(actoId);
		Documento documento = new Documento();
		documento.setId(documentoId);

		actodocumento.setActo(acto);
		actodocumento.setDocumento(documento);
		if(version == null)
			actodocumento.setVersion(1);
		else
			actodocumento.setVersion(version);
		
		actoDocumentoRepository.save(actodocumento);

		return actodocumento.getId();

	}

	public Set<ActoDocumento> getAllActoAssignableDocuments(Long id) {
		return actoDocumentoRepository.getAllActoAssignableDocuments(id);
	}
	public ActoDocumento findOne(Long id) {
		try {
			return this.actoDocumentoRepository.findOne(id);
		}
		catch (Exception e) {
			return new ActoDocumento();
		}
	}


}
