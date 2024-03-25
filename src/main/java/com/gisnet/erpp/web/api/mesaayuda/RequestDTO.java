package com.gisnet.erpp.web.api.mesaayuda;

public class RequestDTO {
	private int accion;
	private long usuarioIdTo;
	private String tema;
	private String mensaje;
	
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}
	public long getUsuarioIdTo() {
		return usuarioIdTo;
	}
	public void setUsuarioIdTo(long usuarioIdTo) {
		this.usuarioIdTo = usuarioIdTo;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

}
