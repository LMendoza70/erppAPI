package com.gisnet.erpp.vo;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Date;


import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.UsuarioArea;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.UsuarioRol;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Identificacion;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.TipoUsuario;
import com.gisnet.erpp.domain.UsuNotario;
import com.gisnet.erpp.domain.Usuario;

public class UsuarioVO2 {

	
	private Usuario usuario;
	private Set<UsuarioArea> usuarioAreasParaUsuarios = new HashSet<>();
	private Set<UsuarioRol> usuarioRolesParaUsuarios = new HashSet<>();
	private Set<UsuNotario> usuNotariosParaUsuarios = new HashSet<>();
	
	private String hashFila = "";
	//private Set<Direccion> direccionesParaPersonas = new HashSet();
	 

	
	public void setUsuario(Usuario usuario) {
		System.out.println("///////////////////////////////////");
		System.out.println(usuario);
		this.usuario = usuario;
	}
	
	public void setUsuarioAreasParaUsuarios(ObjectAreas2[] areas) {
		UsuarioArea usuarioArea;
		for(ObjectAreas2 objectArea : areas) {
			usuarioArea = new UsuarioArea();
			usuarioArea.setArea(objectArea.area);
			this.usuarioAreasParaUsuarios.add(usuarioArea);
		}
		this.usuarioAreasParaUsuarios.forEach(x -> System.out.println(x.getArea().getId()));
	}
	
	public Set<UsuarioArea> getUsuarioAreasParaUsuarios() {
		return this.usuarioAreasParaUsuarios;
	}
	
	public void setUsuarioRolesParaUsuarios(ObjectRoles2[] roles) {
		UsuarioRol usuarioRol;
		for(ObjectRoles2 objectRol : roles) {
			usuarioRol = new UsuarioRol();
			usuarioRol.setRol(objectRol.rol);
			this.usuarioRolesParaUsuarios.add(usuarioRol);
		}
	}
	
	public Set<UsuarioRol> getUsuarioRolesParaUsuarios() {
		return this.usuarioRolesParaUsuarios;
	}
	
	
	public void setUsuNotariosParaUsuarios(ObjectUsuaNotarios2[] notarios) {
		UsuNotario usuNotario;
		for(ObjectUsuaNotarios2 objectUsuaNotario : notarios) {
			usuNotario = new UsuNotario();
			usuNotario.setNotario(objectUsuaNotario.notario);;
			this.usuNotariosParaUsuarios.add(usuNotario);
		}
	}
	
	public Set<UsuNotario> getUsuNotariosParaUsuarios() {
		return this.usuNotariosParaUsuarios;
	}

    public Usuario transformIntoUsuario() {
    	System.out.println(usuario);
    	
    	this.usuario.setUsuarioAreasParaUsuarios(this.getUsuarioAreasParaUsuarios());
    	this.usuario.setUsuarioRolesParaUsuarios(this.getUsuarioRolesParaUsuarios());
    	this.usuario.setUsuNotariosParaUsuarios(this.getUsuNotariosParaUsuarios());
    	return this.usuario;
    }
    
}

class ObjectRoles2 {
	public Rol rol;
}


class ObjectAreas2 {
	public Area area;
}

class ObjectUsuaNotarios2 {
	public Notario notario;
}

