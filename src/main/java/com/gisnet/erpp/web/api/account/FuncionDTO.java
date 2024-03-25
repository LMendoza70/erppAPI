package com.gisnet.erpp.web.api.account;

import java.util.List;

import com.gisnet.erpp.domain.Funcion;

public class FuncionDTO {
	private Funcion funcion;
	private List<Funcion> funciones;
	
	public Funcion getFuncion() {
		return funcion;
	}
	public void setFuncion(Funcion funcion) {
		this.funcion = funcion;
	}
	public List<Funcion> getFunciones() {
		return funciones;
	}
	public void setFunciones(List<Funcion> funciones) {
		this.funciones = funciones;
	}
	

}
