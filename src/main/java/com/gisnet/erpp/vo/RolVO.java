package com.gisnet.erpp.vo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Date;


import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.FuncionRol;

import com.gisnet.erpp.service.RolService;;

public class RolVO {

	private Long id;
	private String nombre = "";
	private String cve = "";
	private Boolean activo = false;
	private String icono;
	private Integer orden;
	private Set<FuncionRol> funcionRolesParaRols= new HashSet<>();
	
	@Autowired
	RolService rolService;
	
	

	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
			this.id = id;
	}

	public void setNombre(String nombre) {
			this.nombre = nombre;
	}
	public String getNombre() {
		return this.nombre;
	}
	
	public void setIcono(String icono) {
		this.icono = icono;
	}
	public String getIcono() {
		return this.icono;
	}

	public void setActivo(Boolean activo) {
			this.activo = activo;
	}

	public Boolean isActivo() {
			return this.activo;
	}

	public void setCve(String cve) {
		this.cve = cve;			
	}

	public String getCve() {
			return this.cve;
	}


	public Integer getOrden() {
			return this.orden;
	}
	
	public void setOrden(Integer orden) {
		this.orden = orden;
	}

		
	public void setFuncionRolesParaRols(FuncionRol[] frol) {
		for(FuncionRol funRol :frol) {
			this.funcionRolesParaRols.add(funRol);
		}
	}
	
	public Set<FuncionRol> getFuncionRolesParaRols() {
		return this.funcionRolesParaRols;
	}
	
    public Rol transformIntoRol() {
    	
		Rol rol = new Rol();
		
		rol.setId(this.getId());
		rol.activo(this.isActivo());
		rol.setCve(this.getCve());
		rol.setIcono(this.getIcono());
		rol.setNombre(this.getNombre());
		rol.setOrden(this.getOrden());
		if (this.getFuncionRolesParaRols() != null) {
			rol.setFuncionRolesParaRols(this.getFuncionRolesParaRols());    		
		}
		return rol;
    }
}