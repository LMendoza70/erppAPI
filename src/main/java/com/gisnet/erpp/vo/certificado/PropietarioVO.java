package com.gisnet.erpp.vo.certificado;

import java.io.Serializable;

public class PropietarioVO implements Serializable {
	private String nombre;
	private Float dd;
	private Float uv;
	
	public PropietarioVO(){}
	
	public PropietarioVO(String nombre, Float dd, Float uv) {
		super();
		this.nombre = nombre;
		this.dd = dd;
		this.uv = uv;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Float getDd() {
		return dd;
	}
	public void setDd(Float dd) {
		this.dd = dd;
	}
	public Float getUv() {
		return uv;
	}
	public void setUv(Float uv) {
		this.uv = uv;
	} 
	
	

}
