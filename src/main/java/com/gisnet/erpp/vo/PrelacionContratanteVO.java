package com.gisnet.erpp.vo;

public class PrelacionContratanteVO {
	
	public Long id;
	public String nombre;
	public String paterno;
	public String materno;
	
	public PrelacionContratanteVO( Long id, String nombre, String paterno, String materno ) {
		this.id = id;
		this.nombre = nombre;
		this.paterno = paterno;
		this.materno = materno;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPaterno() {
		return paterno;
	}
	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}
	public String getMaterno() {
		return materno;
	}
	public void setMaterno(String materno) {
		this.materno = materno;
	}
	
	

}
