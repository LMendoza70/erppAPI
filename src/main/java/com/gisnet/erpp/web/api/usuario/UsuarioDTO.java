package com.gisnet.erpp.web.api.usuario;

import java.sql.Blob;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gisnet.erpp.domain.Acceso;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Certificado;
import com.gisnet.erpp.domain.FuncionRolUsu;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.TipoUsuario;
import com.gisnet.erpp.domain.UsuNotario;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioArea;
import com.gisnet.erpp.domain.UsuarioAreaRol;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.domain.UsuarioRol;

public class UsuarioDTO {
	
	
	private List <Usuario> usuario;
	
    private List <UsuarioOfiAreas> usuarioOfiAreas;
    
    
	public UsuarioDTO() {
		
	}


	public List<Usuario> getUsuario() {
		return usuario;
	}


	public void setUsuario(List<Usuario> usuario) {
		this.usuario = usuario;
	}


	public List<UsuarioOfiAreas> getUsuarioOfiAreas() {
		return usuarioOfiAreas;
	}


	public void setUsuarioOfiAreas(List<UsuarioOfiAreas> usuarioOfiAreas) {
		this.usuarioOfiAreas = usuarioOfiAreas;
	}

	    
	
}
