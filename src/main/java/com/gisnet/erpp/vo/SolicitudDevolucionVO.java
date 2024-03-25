package com.gisnet.erpp.vo;

import java.util.Date;
import java.util.List;

public class SolicitudDevolucionVO {
	private Long id;
	private String consecutivo;
	private String entrada;
	private String oficina;
	private String usuarioRecepciono;
	private String nombreSolicita;
	private String emailSolicita;
	private String observaciones;
	private Date fechaIngreso;
	private List<ReciboVO> recibos;
	
	public SolicitudDevolucionVO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getEntrada() {
		return entrada;
	}

	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}

	public String getOficina() {
		return oficina;
	}

	public void setOficina(String oficina) {
		this.oficina = oficina;
	}

	public String getUsuarioRecepciono() {
		return usuarioRecepciono;
	}

	public void setUsuarioRecepciono(String usuarioRecepciono) {
		this.usuarioRecepciono = usuarioRecepciono;
	}

	public String getNombreSolicita() {
		return nombreSolicita;
	}

	public void setNombreSolicita(String nombreSolicita) {
		this.nombreSolicita = nombreSolicita;
	}

	public String getEmailSolicita() {
		return emailSolicita;
	}

	public void setEmailSolicita(String emailSolicita) {
		this.emailSolicita = emailSolicita;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public List<ReciboVO> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<ReciboVO> recibos) {
		this.recibos = recibos;
	}
}
