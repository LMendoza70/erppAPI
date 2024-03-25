package com.gisnet.erpp.vo;

import java.util.List;

public class ArchivoVO {
	
	private boolean exists;
	private List<String> ruta;
	
	public boolean isExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	public List<String> getRuta() {
		return ruta;
	}
	public void setRuta(List<String> ruta) {
		this.ruta = ruta;
	}
	
	

}
