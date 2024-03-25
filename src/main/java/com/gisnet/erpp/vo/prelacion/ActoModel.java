package com.gisnet.erpp.vo.prelacion;

import com.gisnet.erpp.domain.Acto;

public class ActoModel {
	private String nombre;
	private String asiento ;
	private Acto acto;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAsiento() {
		return asiento;
	}

	public void setAsiento(String asiento) {
		this.asiento = asiento;
	}

	public void setActo(Acto acto){
		this.acto=acto;
	}

	public Acto getActo(){
		return this.acto;
	}
}
