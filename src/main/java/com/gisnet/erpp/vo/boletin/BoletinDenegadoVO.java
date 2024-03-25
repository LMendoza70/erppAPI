package com.gisnet.erpp.vo.boletin;

import java.util.List;


public class BoletinDenegadoVO{
String fecha;
String oficina;
List<BoletinDenegadoModel> denegados;
List<BoletinSuspensionModel> suspendidos;
String noRegistros;

	 public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina (String oficina) {
		this.oficina= oficina;
	}

	public List<BoletinDenegadoModel> getDenegados() {
        return this.denegados;
    }

    public void setDenegados(List<BoletinDenegadoModel> denegados) {
        this.denegados = denegados;
    }

	public String getNoRegistros() {
		return noRegistros;
	}

	public void setNoRegistros(String noRegistros) {
		this.noRegistros = noRegistros;
	}

	public List<BoletinSuspensionModel> getSuspendidos() {
		return suspendidos;
	}

	public void setSuspendidos(List<BoletinSuspensionModel> suspendidos) {
		this.suspendidos = suspendidos;
	}

	
	
}