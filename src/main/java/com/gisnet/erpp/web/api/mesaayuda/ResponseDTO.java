package com.gisnet.erpp.web.api.mesaayuda;

import java.util.Date;

public class ResponseDTO {
	private int accion;
	private long usuarioIdFrom;
	private String usuarioNombreFrom;
	private long usuarioIdTo;
	private String tema;
	private String mensaje;
	private Date fecha;
	private boolean incoming;
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}
	public long getUsuarioIdFrom() {
		return usuarioIdFrom;
	}
	public void setUsuarioIdFrom(long usuarioIdFrom) {
		this.usuarioIdFrom = usuarioIdFrom;
	}
	
	public String getUsuarioNombreFrom() {
		return usuarioNombreFrom;
	}
	public void setUsuarioNombreFrom(String usuarioNombreFrom) {
		this.usuarioNombreFrom = usuarioNombreFrom;
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public boolean isIncoming() {
		return incoming;
	}
	public void setIncoming(boolean incoming) {
		this.incoming = incoming;
	}
	

	
}
