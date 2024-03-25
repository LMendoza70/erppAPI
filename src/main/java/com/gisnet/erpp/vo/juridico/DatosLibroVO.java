package com.gisnet.erpp.vo.juridico;

import java.util.Date;

public class DatosLibroVO {
	private String  libro;
	private String tipoLibro;
	private String seccion;
	private String oficinaLibro;
	private String documento;
	private String tipoDoc;
	private String vol;
	private String anio;

	public String getLibro(){
		return libro;
	}
	
	public void setLibro(String libro){
		this.libro = libro;
	}

	public String getTipoLibro(){
		return tipoLibro;
	}
	
	public void setTipoLibro(String tipoLibro){
		this.tipoLibro = tipoLibro;
	}

	public String getSeccion(){
		return seccion;
	}
	
	public void setSeccion(String seccion){
		this.seccion = seccion;
	}


	public String getOficinaLibro(){
		return oficinaLibro;
	}
	
	public void setOficinaLibro(String oficinaLibro){
		this.oficinaLibro = oficinaLibro;
	}

	public String getDocumento(){
		return documento;
	}
	
	public void setDocumento(String documento){
		this.documento = documento;
	}

	public String getTipoDoc(){
		return tipoDoc;
	}
	
	public void setTipoDoc(String tipoDoc){
		this.tipoDoc = tipoDoc;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

}
