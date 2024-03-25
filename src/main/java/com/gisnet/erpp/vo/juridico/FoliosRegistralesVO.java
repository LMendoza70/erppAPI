package com.gisnet.erpp.vo.juridico;

import java.util.Date;

public class FoliosRegistralesVO {
	private String	folio;
	private String seccion;
	private String oficina;

	public String getFolio(){
		return folio;
	}
	
	public void setFolio(String folio){
		this.folio = folio;
	}

	public String getSeccion(){
		return seccion;
	}
	
	public void setSeccion(String seccion){
		this.seccion = seccion;
	}

	public String getOficina(){
		return oficina;
	}
	
	public void setOficina(String oficina){
		this.oficina = oficina;
	}

}