package com.gisnet.erpp.vo;


import java.util.Date;

public class ConsultaPrelacionDetalleTramiteVO {

	private Long id;
	private Integer consecutivo;
	private Date fechaRecepcion;
	private Date fechaEnvioCalificador;
	private Date fechaEnvioCoordinador;
	private Date fechaFirmaYEnvioUsuario;
	private Date fechaImpresionUsuario;
	private Date fechaEnvioAnalisis;
	private String prioridad;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}
	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	public Date getFechaEnvioCalificador() {
		return fechaEnvioCalificador;
	}
	public void setFechaEnvioCalificador(Date fechaEnvioCalificador) {
		this.fechaEnvioCalificador = fechaEnvioCalificador;
	}
	public Date getFechaEnvioCoordinador() {
		return fechaEnvioCoordinador;
	}
	public void setFechaEnvioCoordinador(Date fechaEnvioCoordinador) {
		this.fechaEnvioCoordinador = fechaEnvioCoordinador;
	}
	public Date getFechaFirmaYEnvioUsuario() {
		return fechaFirmaYEnvioUsuario;
	}
	public void setFechaFirmaYEnvioUsuario(Date fechaFirmaYEnvioUsuario) {
		this.fechaFirmaYEnvioUsuario = fechaFirmaYEnvioUsuario;
	}
	public Date getFechaImpresionUsuario() {
		return fechaImpresionUsuario;
	}
	public void setFechaImpresionUsuario(Date fechaImpresionUsuario) {
		this.fechaImpresionUsuario = fechaImpresionUsuario;
	}
	public Date getFechaEnvioAnalisis() {
		return fechaEnvioAnalisis;
	}
	public void setFechaEnvioAnalisis(Date fechaEnvioAnalisis) {
		this.fechaEnvioAnalisis = fechaEnvioAnalisis;
	}
	public String getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
}
