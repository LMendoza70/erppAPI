package com.gisnet.erpp.vo.fichaRegistral;

public class MedidasVO {
	private String orientacion;
	private String nombre;
	
	public MedidasVO(){}

	public MedidasVO(String orientacion, String nombre) {
		super();
		this.orientacion = orientacion;
		this.nombre = nombre;
	}
	
	public String getOrientacion() {
		return orientacion;
	}
	public void setOrientacion(String orientacion) {
		this.orientacion = orientacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
