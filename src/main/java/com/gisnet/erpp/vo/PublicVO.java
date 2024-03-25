package com.gisnet.erpp.vo;

import java.util.Date;

public class PublicVO {
	public Long id;
	private Date fechaIngreso = null;
	private String usuarioSolicita = "";
	private String mensaje = "";
	private String fundamento = "";
	private String motivo = "";
	private String observaciones = "";
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getUsuarioSolicita() {
		return usuarioSolicita;
	}
	public void setUsuarioSolicita(String usuarioSolicita) {
		this.usuarioSolicita = usuarioSolicita;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getFundamento() {
		return fundamento;
	}
	public void setFundamento(String fundamento) {
		this.fundamento = fundamento;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	
}
